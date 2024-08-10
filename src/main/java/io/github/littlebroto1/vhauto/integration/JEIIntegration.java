package io.github.littlebroto1.vhauto.integration;

import io.github.littlebroto1.vhauto.VHAuto;
import io.github.littlebroto1.vhauto.VHAutoBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

@JeiPlugin
public class JEIIntegration implements IModPlugin {
    private static final ResourceLocation ID = new ResourceLocation(VHAuto.MOD_ID, "core");
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.getIngredientManager().addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, Collections.singletonList(new ItemStack(VHAutoBlocks.VAULT_ASSEMBLER.asItem())));
    }
}
