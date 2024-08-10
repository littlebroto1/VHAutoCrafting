package io.github.littlebroto1.vhauto.blockentities;

import appeng.blockentity.crafting.MolecularAssemblerBlockEntity;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nullable;
import java.util.UUID;

public class VaultAssemblerTileEntity extends MolecularAssemblerBlockEntity {
    private UUID placedByUUID = null;
    private String placedByName = null;
    private FakePlayer cachedFakePlayer = null;

    public VaultAssemblerTileEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
    }

    public void setPlacedBy(@Nullable UUID uuid, @Nullable String name) {
        this.placedByUUID = uuid;
        this.placedByName = name;
        setChanged();
    }

    @Nullable
    public Player getPlacedBy() {
        if (level != null && placedByUUID != null && level instanceof ServerLevel serverLevel) {
            if (cachedFakePlayer == null) {
                cachedFakePlayer = new FakePlayer(serverLevel, new GameProfile(placedByUUID, placedByName));
            }
            return cachedFakePlayer;
        }
        return null;
    }

    @Override
    public void saveAdditional(CompoundTag data) {
        if (placedByUUID != null) {
            data.putUUID("PlacedByUUID", placedByUUID);
        }
        if (placedByName != null && !placedByName.isBlank()) {
            data.putString("PlacedByName", placedByName);
        }
        super.saveAdditional(data);
    }

    @Override
    public void loadTag(CompoundTag data) {
        if (data.contains("PlacedByUUID")) {
            this.placedByUUID = data.getUUID("PlacedByUUID");
        }
        if (data.contains("PlacedByName")) {
            this.placedByName = data.getString("PlacedByName");
        }
        super.loadTag(data);
    }
}
