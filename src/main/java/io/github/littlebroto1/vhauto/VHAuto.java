package io.github.littlebroto1.vhauto;

import appeng.client.render.crafting.MolecularAssemblerRenderer;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(VHAuto.MOD_ID)
public class VHAuto
{
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "vhauto";


    public static final CreativeModeTab VHAUTO_TAB = new CreativeModeTab(MOD_ID) {
        @Override
        //@OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(VHAutoBlocks.VAULT_ASSEMBLER.asItem());
        }
    };

    public VHAuto()
    {
        // Register the enqueueIMC method for modloading
        VHAutoBlocks.initBlocks(FMLJavaModLoadingContext.get().getModEventBus());
        VHAutoBlockEntities.initBlockEntities(FMLJavaModLoadingContext.get().getModEventBus());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void setup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(VHAutoBlocks.VAULT_ASSEMBLER.block(), RenderType.cutout());
        BlockEntityRenderers.register(VHAutoBlockEntities.VAULT_ASSEMBLER, MolecularAssemblerRenderer::new);
        //AEBlocks.MOLECULAR_ASSEMBLER.block().setBlockEntity(VaultAssemblerTileEntity.class);
    }

}
