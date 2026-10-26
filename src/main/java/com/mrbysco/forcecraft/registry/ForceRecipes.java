package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.recipe.FreezingRecipe;
import com.mrbysco.forcecraft.recipe.GrindingRecipe;
import com.mrbysco.forcecraft.recipe.InfuseRecipe;
import com.mrbysco.forcecraft.recipe.TransmutationRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@EventBusSubscriber
public class ForceRecipes {
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Reference.MOD_ID);
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Reference.MOD_ID);

	public static final Supplier<RecipeType<InfuseRecipe>> INFUSE = RECIPE_TYPES.register("infuse", () -> new RecipeType<>() {
	});
	public static final Supplier<RecipeType<FreezingRecipe>> FREEZING = RECIPE_TYPES.register("freezing", () -> new RecipeType<>() {
	});
	public static final Supplier<RecipeType<GrindingRecipe>> GRINDING = RECIPE_TYPES.register("grinding", () -> new RecipeType<>() {
	});

	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TransmutationRecipe>> TRANSMUTE_SERIALIZER = RECIPE_SERIALIZERS.register("transmute", () -> TransmutationRecipe.SERIALIZER);
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<InfuseRecipe>> INFUSE_SERIALIZER = RECIPE_SERIALIZERS.register("infuse", () -> InfuseRecipe.SERIALIZER);
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FreezingRecipe>> FREEZING_SERIALIZER = RECIPE_SERIALIZERS.register("freezing", () -> FreezingRecipe.SERIALIZER);
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<GrindingRecipe>> GRINDING_SERIALIZER = RECIPE_SERIALIZERS.register("grinding", () -> GrindingRecipe.SERIALIZER);

	@SubscribeEvent
	public static void onDatapackSync(OnDatapackSyncEvent event) {
		event.sendRecipes(ForceRecipes.INFUSE.get());
		event.sendRecipes(ForceRecipes.FREEZING.get());
		event.sendRecipes(ForceRecipes.GRINDING.get());
	}
}
