package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.recipe.FreezingRecipe.SerializerFreezingRecipe;
import com.mrbysco.forcecraft.recipe.GrindingRecipe.SerializerGrindingRecipe;
import com.mrbysco.forcecraft.recipe.InfuseRecipe.SerializeInfuserRecipe;
import com.mrbysco.forcecraft.recipe.ShapedNoRemainderRecipe.SerializerShapedNoRemainderRecipe;
import com.mrbysco.forcecraft.recipe.TransmutationRecipe.SerializerTransmutationRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ForceRecipeSerializers {
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Reference.MOD_ID);

	public static final Supplier<SerializeInfuserRecipe> INFUSER_SERIALIZER = RECIPE_SERIALIZERS.register("infuser", SerializeInfuserRecipe::new);
	public static final Supplier<SerializerTransmutationRecipe> TRANSMUTATION_SERIALIZER = RECIPE_SERIALIZERS.register("transmutation", SerializerTransmutationRecipe::new);
	public static final Supplier<SerializerShapedNoRemainderRecipe> SHAPED_NO_REMAINDER_SERIALIZER = RECIPE_SERIALIZERS.register("shaped_no_remainder", SerializerShapedNoRemainderRecipe::new);
	public static final Supplier<SerializerFreezingRecipe> FREEZING_SERIALIZER = RECIPE_SERIALIZERS.register("freezing", SerializerFreezingRecipe::new);
	public static final Supplier<SerializerGrindingRecipe> GRINDING_SERIALIZER = RECIPE_SERIALIZERS.register("grinding", SerializerGrindingRecipe::new);
}
