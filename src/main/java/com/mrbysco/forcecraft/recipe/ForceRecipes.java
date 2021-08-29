package com.mrbysco.forcecraft.recipe;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.recipe.FreezingRecipe.SerializerFreezingRecipe;
import com.mrbysco.forcecraft.recipe.GrindingRecipe.SerializerGrindingRecipe;
import com.mrbysco.forcecraft.recipe.InfuseRecipe.SerializeInfuserRecipe;
import com.mrbysco.forcecraft.recipe.ShapedNoRemainderRecipe.SerializerShapedNoRemainderRecipe;
import com.mrbysco.forcecraft.recipe.TransmutationRecipe.SerializerTransmutationRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ForceRecipes {
	public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Reference.MOD_ID);

	public static final IRecipeType<InfuseRecipe> INFUSER_TYPE = IRecipeType.register(new ResourceLocation(Reference.MOD_ID, "infuser").toString());
	public static final IRecipeType<FreezingRecipe> FREEZING = IRecipeType.register(new ResourceLocation(Reference.MOD_ID, "freezing").toString());
	public static final IRecipeType<GrindingRecipe> GRINDING = IRecipeType.register(new ResourceLocation(Reference.MOD_ID, "grinding").toString());

	public static final RegistryObject<SerializeInfuserRecipe> INFUSER_SERIALIZER = RECIPE_SERIALIZERS.register("infuser", SerializeInfuserRecipe::new);
	public static final RegistryObject<SerializerTransmutationRecipe> TRANSMUTATION_SERIALIZER = RECIPE_SERIALIZERS.register("transmutation", SerializerTransmutationRecipe::new);
	public static final RegistryObject<SerializerShapedNoRemainderRecipe> SHAPED_NO_REMAINDER_SERIALIZER = RECIPE_SERIALIZERS.register("shaped_no_remainder", SerializerShapedNoRemainderRecipe::new);
	public static final RegistryObject<SerializerFreezingRecipe> FREEZING_SERIALIZER = RECIPE_SERIALIZERS.register("freezing", SerializerFreezingRecipe::new);
	public static final RegistryObject<SerializerGrindingRecipe> GRINDING_SERIALIZER = RECIPE_SERIALIZERS.register("grinding", SerializerGrindingRecipe::new);
}
