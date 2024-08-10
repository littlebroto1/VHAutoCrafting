package io.github.littlebroto1.vhauto.mixins;

import appeng.api.crafting.IPatternDetails;
import appeng.api.networking.IGridNode;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.blockentity.AEBaseInvBlockEntity;
import appeng.blockentity.crafting.MolecularAssemblerBlockEntity;
import appeng.crafting.CraftingEvent;
import com.mojang.authlib.GameProfile;
import io.github.littlebroto1.vhauto.blockentities.VaultAssemblerTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(MolecularAssemblerBlockEntity.class)
public class MixinMolecularAssemblerBlockEntity /*extends AEBaseBlockEntity implements IMixinMolecularAssemblerBlockEntity*/ {
    @Unique
    private static MolecularAssemblerBlockEntity BLOCK_ENTITY = null;

    /*
    @Unique
    private UUID placedByUUID = null;
    @Unique
    private String placedByName = null;
    @Unique
    private FakePlayer cachedFakePlayer = null;

    public MixinMolecularAssemblerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        // This does nothing
        super(blockEntityType, pos, blockState);
    }

    @Unique
    public void vH_AutoCrafting$setPlacedBy(@Nullable UUID uuid, @Nullable String name) {
        this.placedByUUID = uuid;
        this.placedByName = name;
        setChanged();
    }

    @Unique
    @Nullable
    public Player vH_AutoCrafting$getPlacedBy() {
        if (level != null && placedByUUID != null && level instanceof ServerLevel serverLevel) {
            if (cachedFakePlayer == null) {
                cachedFakePlayer = new FakePlayer(serverLevel, new GameProfile(placedByUUID, "Developer"));
            }
            return cachedFakePlayer;
        }
        return null;
    }
     */

    @Inject(method = "tickingRequest", at = @At("HEAD"), remap = false)
    public void assignBlockEntity(IGridNode node, int ticksSinceLastCall, CallbackInfoReturnable<TickRateModulation> cir) {
        BLOCK_ENTITY = (MolecularAssemblerBlockEntity)(Object)this;
    };

    @Inject(method = "tickingRequest", at = @At("RETURN"), remap = false)
    public void unassignBlockEntity(IGridNode node, int ticksSinceLastCall, CallbackInfoReturnable<TickRateModulation> cir) {
        BLOCK_ENTITY = null;
    };

    @Redirect(method = "tickingRequest", at = @At(value = "INVOKE", target = "Lappeng/crafting/CraftingEvent;fireAutoCraftingEvent(Lnet/minecraft/world/level/Level;Lappeng/api/crafting/IPatternDetails;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/Container;)V"), remap = false)
    public void redirectFireAutoCraftingEvent(Level level, IPatternDetails pattern, ItemStack craftedItem, Container container) {
        if (BLOCK_ENTITY instanceof VaultAssemblerTileEntity vaultAssemblerTile) {
            Player fakePlayer = vaultAssemblerTile.getPlacedBy();
            if (fakePlayer != null) {
                MinecraftForge.EVENT_BUS.post(new PlayerEvent.ItemCraftedEvent(fakePlayer, craftedItem, container));
            }
        } else {
            CraftingEvent.fireAutoCraftingEvent(level, pattern, craftedItem, container);
        }
    }
}
