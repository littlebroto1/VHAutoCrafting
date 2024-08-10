package io.github.littlebroto1.vhauto;

import appeng.block.AEBaseEntityBlock;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.blockentity.ClientTickingBlockEntity;
import appeng.blockentity.ServerTickingBlockEntity;
import appeng.core.definitions.AEBlocks;
import com.google.common.base.Preconditions;
import io.github.littlebroto1.vhauto.blockentities.VaultAssemblerTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.concurrent.atomic.AtomicReference;

public final class VHAutoBlockEntities {

    private static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, VHAuto.MOD_ID);

    // I add it to the AE molecular assembler to override its default behavior
    public static final BlockEntityType<VaultAssemblerTileEntity> VAULT_ASSEMBLER = create("vault_assembler", VaultAssemblerTileEntity.class, VaultAssemblerTileEntity::new, VHAutoBlocks.VAULT_ASSEMBLER.block());

    //private static <T extends AEBaseBlockEntity> BlockEntityType<T> create(String shortID,)

    public static void initBlockEntities(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    private static <T extends AEBaseBlockEntity> BlockEntityType<T> create(String shortId,
        Class<T> entityClass,
        BlockEntityFactory<T> factory,
        AEBaseEntityBlock<?>... blockDefinitions) {

        Preconditions.checkArgument(blockDefinitions.length > 0);

        //ResourceLocation id = new ResourceLocation(VHAuto.MOD_ID, shortId); // modification here

        AtomicReference<BlockEntityType<T>> typeHolder = new AtomicReference<>();
        BlockEntityType.BlockEntitySupplier<T> supplier = (blockPos, blockState) -> factory.create(typeHolder.get(),
                blockPos, blockState);
        var type = BlockEntityType.Builder.of(supplier, blockDefinitions).build(null);
        //type.setRegistryName(id);
        typeHolder.set(type); // Makes it available to the supplier used above

        //AccessorAEBlockEntities.getMutableBlockEntityTypes().put(id, type); // modification here

        AEBaseBlockEntity.registerBlockEntityItem(type, blockDefinitions[0].asItem());

        // If the block entity classes implement specific interfaces, automatically register them
        // as tickers with the blocks that create that entity.
        BlockEntityTicker<T> serverTicker = null;
        if (ServerTickingBlockEntity.class.isAssignableFrom(entityClass)) {
            serverTicker = (level, pos, state, entity) -> {
                ((ServerTickingBlockEntity) entity).serverTick();
            };
        }
        BlockEntityTicker<T> clientTicker = null;
        if (ClientTickingBlockEntity.class.isAssignableFrom(entityClass)) {
            clientTicker = (level, pos, state, entity) -> {
                ((ClientTickingBlockEntity) entity).clientTick();
            };
        }

        for (var block : blockDefinitions) {
            AEBaseEntityBlock<T> baseBlock = (AEBaseEntityBlock<T>) block;
            baseBlock.setBlockEntity(entityClass, type, clientTicker, serverTicker);
        }

        BLOCK_ENTITIES.register(shortId, () -> type);

        return type;
    }

    @FunctionalInterface
    interface BlockEntityFactory<T extends AEBaseBlockEntity> {
        T create(BlockEntityType<T> type, BlockPos pos, BlockState state);
    }
}
