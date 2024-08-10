package io.github.littlebroto1.vhauto;

import appeng.block.AEBaseBlock;
import appeng.block.AEBaseBlockItem;
import appeng.block.crafting.MolecularAssemblerBlock;
import appeng.core.CreativeTab;
import appeng.core.definitions.BlockDefinition;
import io.github.littlebroto1.vhauto.block.VaultAssembler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class VHAutoBlocks {
    private static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, VHAuto.MOD_ID);
    private static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, VHAuto.MOD_ID);

    //public static MolecularAssemblerBlock VAULT_ASSEMBLER_BLOCK = new MolecularAssemblerBlock(AEBaseBlock.defaultProps(Material.METAL).noOcclusion());
    public static BlockDefinition<MolecularAssemblerBlock> VAULT_ASSEMBLER = block("Vault Assembler", "vault_assembler", () -> new VaultAssembler(AEBaseBlock.defaultProps(Material.METAL).noOcclusion()));

    //public static final BlockDefinition<MolecularAssemblerBlock> VAULT_ASSEMBLER = block("Vault Assembler", new ResourceLocation(VHAuto.MOD_ID, "vault_assembler"), () -> new MolecularAssemblerBlock(AEBaseBlock.defaultProps(Material.METAL).noOcclusion()));

    public static void initBlocks(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }

    private static <T extends Block> BlockDefinition<T> block(String englishName, String shortName, Supplier<T> blockSupplier) {
        return block(englishName, shortName, blockSupplier, null);
    }

    private static <T extends Block> BlockDefinition<T> block(String englishName, String shortName, Supplier<T> blockSupplier, @Nullable BiFunction<Block, Item.Properties, BlockItem> itemFactory) {
        ResourceLocation id = new ResourceLocation(VHAuto.MOD_ID, shortName);
        T block = blockSupplier.get();
        Item.Properties itemProperties = new Item.Properties().tab(VHAuto.VHAUTO_TAB);
        BlockItem item;
        if (itemFactory != null) {
            item = itemFactory.apply(block, itemProperties);
            if (item == null) {
                throw new IllegalArgumentException("BlockItem factory for " + id + " returned null");
            }
        } else if (block instanceof AEBaseBlock) {
            item = new AEBaseBlockItem(block, itemProperties);
        } else {
            item = new BlockItem(block, itemProperties);
        }

        BLOCKS.register(shortName, () -> block);
        ITEMS.register(shortName, () -> item);

        //CreativeTab.add(definition);
        //AccessorAEBlocks.getMutableBlocks().add(definition);

        return new BlockDefinition<>(englishName, id, block, item);
    }
}
