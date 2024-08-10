package io.github.littlebroto1.vhauto.block;

import appeng.block.crafting.MolecularAssemblerBlock;
import io.github.littlebroto1.vhauto.blockentities.VaultAssemblerTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;

public class VaultAssembler extends MolecularAssemblerBlock {
    public VaultAssembler(Properties properties) {
        super(properties);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack is) {
        if (level.getBlockEntity(pos) instanceof VaultAssemblerTileEntity assemblerEntity) {
            if (placer instanceof Player player && !(placer instanceof FakePlayer)) {
                assemblerEntity.setPlacedBy(player.getUUID(), player.getName().getContents());
            }
        }
        super.setPlacedBy(level, pos, state, placer, is);
    }
}
