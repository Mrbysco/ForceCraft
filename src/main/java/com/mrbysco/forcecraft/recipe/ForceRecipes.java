package com.mrbysco.forcecraft.recipe;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.recipe.FreezingRecipe.SerializerFreezingRecipe;
import com.mrbysco.forcecraft.recipe.GrindingRecipe.SerializerGrindingRecipe;
import com.mrbysco.forcecraft.recipe.InfuseRecipe.SerializeInfuserRecipe;
import com.mrbysco.forcecraft.recipe.ShapedNoRemainderRecipe.SerializerShapedNoRemainderRecipe;
import com.mrbysco.forcecraft.recipe.TransmutationRecipe.SerializerTransmutationRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForceRecipes {
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Reference.MOD_ID);

	public static final RecipeType<InfuseRecipe> INFUSER_TYPE = RecipeType.register(new ResourceLocation(Reference.MOD_ID, "infuser").toString());
	public static final RecipeType<FreezingRecipe> FREEZING = RecipeType.register(new ResourceLocation(Reference.MOD_ID, "freezing").toString());
	public static final RecipeType<GrindingRecipe> GRINDING = RecipeType.register(new ResourceLocation(Reference.MOD_ID, "grinding").toString());

	public static final RegistryObject<SerializeInfuserRecipe> INFUSER_SERIALIZER = RECIPE_SERIALIZERS.register("infuser", SerializeInfuserRecipe::new);
	public static final RegistryObject<SerializerTransmutationRecipe> TRANSMUTATION_SERIALIZER = RECIPE_SERIALIZERS.register("transmutation", SerializerTransmutationRecipe::new);
	public static final RegistryObject<SerializerShapedNoRemainderRecipe> SHAPED_NO_REMAINDER_SERIALIZER = RECIPE_SERIALIZERS.register("shaped_no_remainder", SerializerShapedNoRemainderRecipe::new);
	public static final RegistryObject<SerializerFreezingRecipe> FREEZING_SERIALIZER = RECIPE_SERIALIZERS.register("freezing", SerializerFreezingRecipe::new);
	public static final RegistryObject<SerializerGrindingRecipe> GRINDING_SERIALIZER = RECIPE_SERIALIZERS.register("grinding", SerializerGrindingRecipe::new);
}
