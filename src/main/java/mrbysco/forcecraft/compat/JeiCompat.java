package mrbysco.forcecraft.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.util.ErrorUtil;
import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.compat.multipleoutput.FreezingCategory;
import mrbysco.forcecraft.compat.multipleoutput.GrindingCategory;
import mrbysco.forcecraft.recipe.ForceRecipes;
import mrbysco.forcecraft.recipe.FreezingRecipe;
import mrbysco.forcecraft.recipe.GrindingRecipe;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Objects;

@JeiPlugin
public class JeiCompat implements IModPlugin {
	public static final ResourceLocation RECIPE_MULTIPLES_JEI = new ResourceLocation(Reference.MOD_ID, "textures/gui/jei/multiples.png");

	public static final ResourceLocation PLUGIN_UID = new ResourceLocation(Reference.MOD_ID, "main");
	public static final ResourceLocation FREEZING = new ResourceLocation(Reference.MOD_ID, "freezing");
	public static final ResourceLocation GRINDING = new ResourceLocation(Reference.MOD_ID, "grinding");
	@Nullable
	private IRecipeCategory<FreezingRecipe> freezingCategory;
	@Nullable
	private IRecipeCategory<GrindingRecipe> grindingCategory;

	@Override
	public ResourceLocation getPluginUid() {
		return PLUGIN_UID;
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.FREEZING_CORE.get()), FREEZING);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.GRINDING_CORE.get()), GRINDING);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IJeiHelpers jeiHelpers = registration.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		registration.addRecipeCategories(
			freezingCategory = new FreezingCategory(guiHelper),
			grindingCategory = new GrindingCategory(guiHelper)
		);
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		ErrorUtil.checkNotNull(freezingCategory, "freezingCategory");
		ErrorUtil.checkNotNull(grindingCategory, "grindingCategory");

		ClientWorld world = Objects.requireNonNull(Minecraft.getInstance().world);
		registration.addRecipes(world.getRecipeManager().getRecipesForType(ForceRecipes.FREEZING), FREEZING);
		registration.addRecipes(world.getRecipeManager().getRecipesForType(ForceRecipes.GRINDING), GRINDING);
	}
}
