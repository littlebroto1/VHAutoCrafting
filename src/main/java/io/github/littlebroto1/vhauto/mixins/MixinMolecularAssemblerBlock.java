package io.github.littlebroto1.vhauto.mixins;

import appeng.block.crafting.MolecularAssemblerBlock;
import appeng.core.definitions.AEBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(MolecularAssemblerBlock.class)
public abstract class MixinMolecularAssemblerBlock {


    public void setPlacedBy() {

    }
}
