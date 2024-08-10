package io.github.littlebroto1.vhauto.mixins;

import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.UUID;

public interface IMixinMolecularAssemblerBlockEntity {
    void vH_AutoCrafting$setPlacedBy(@Nullable UUID uuid, @Nullable String name);

    @Nullable
    Player vH_AutoCrafting$getPlacedBy();
}
