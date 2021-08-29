package com.mrbysco.forcecraft.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.util.ErrorUtil;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.compat.infuser.InfuserCategory;
import com.mrbysco.forcecraft.compat.multipleoutput.FreezingCategory;
import com.mrbysco.forcecraft.compat.multipleoutput.GrindingCategory;
import com.mrbysco.forcecraft.compat.transfer.ItemCardTransferHandler;
import com.mrbysco.forcecraft.recipe.ForceRecipes;
import com.mrbysco.forcecraft.recipe.FreezingRecipe;
import com.mrbysco.forcecraft.recipe.GrindingRecipe;
import com.mrbysco.forcecraft.recipe.InfuseRecipe;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Objects;

@JeiPlugin
public class JeiCompat implements IModPlugin {
	public static final ResourceLocation RECIPE_MULTIPLES_JEI = new ResourceLocation(Reference.MOD_ID, "textures/gui/jei/multiples.png");
	public static final ResourceLocation RECIPE_INFUSER_JEI = new ResourceLocation(Reference.MOD_ID, "textures/gui/jei/infuser.png");

	public static final ResourceLocation PLUGIN_UID = new ResourceLocation(Reference.MOD_ID, "main");
	public static final ResourceLocation FREEZING = new ResourceLocation(Reference.MOD_ID, "freezing");
	public static final ResourceLocation GRINDING = new ResourceLocation(Reference.MOD_ID, "grinding");
	public static final ResourceLocation INFUSER = new ResourceLocation(Reference.MOD_ID, "infuser");
	@Nullable
	private IRecipeCategory<FreezingRecipe> freezingCategory;
	@Nullable
	private IRecipeCategory<GrindingRecipe> grindingCategory;
	@Nullable
	private IRecipeCategory<InfuseRecipe> infuserCategory;

	@Override
	public ResourceLocation getPluginUid() {
		return PLUGIN_UID;
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.FREEZING_CORE.get()), FREEZING);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.GRINDING_CORE.get()), GRINDING);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.INFUSER.get()), INFUSER);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IJeiHelpers jeiHelpers = registration.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		registration.addRecipeCategories(
			freezingCategory = new FreezingCategory(guiHelper),
			grindingCategory = new GrindingCategory(guiHelper),
			infuserCategory = new InfuserCategory<InfuseRecipe>(guiHelper)
		);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(new ItemCardTransferHandler(), VanillaRecipeCategoryUid.CRAFTING);
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		ErrorUtil.checkNotNull(freezingCategory, "freezingCategory");
		ErrorUtil.checkNotNull(grindingCategory, "grindingCategory");
		ErrorUtil.checkNotNull(infuserCategory, "grindingCategory");

		ClientWorld world = Objects.requireNonNull(Minecraft.getInstance().world);
		registration.addRecipes(world.getRecipeManager().getRecipesForType(ForceRecipes.FREEZING), FREEZING);
		registration.addRecipes(world.getRecipeManager().getRecipesForType(ForceRecipes.GRINDING), GRINDING);
		registration.addRecipes(world.getRecipeManager().getRecipesForType(ForceRecipes.INFUSER_TYPE), INFUSER);
	}
}
