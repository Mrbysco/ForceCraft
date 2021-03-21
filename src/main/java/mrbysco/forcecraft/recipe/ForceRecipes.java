package mrbysco.forcecraft.recipe;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.recipe.InfuseRecipe.SerializeInfuserRecipe;
import mrbysco.forcecraft.recipe.TransmutationRecipe.SerializerTransmutationRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ForceRecipes {
	public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Reference.MOD_ID);

	public static final IRecipeType<InfuseRecipe> INFUSER_TYPE = IRecipeType.register(new ResourceLocation(Reference.MOD_ID, "infuser").toString());
	public static final IRecipeType<TransmutationRecipe> TRANSMUTATION = IRecipeType.register(new ResourceLocation(Reference.MOD_ID, "transmutation").toString());
	public static final RegistryObject<SerializeInfuserRecipe> INFUSER_SERIALIZER = RECIPE_SERIALIZERS.register("infuser", SerializeInfuserRecipe::new);
	public static final RegistryObject<SerializerTransmutationRecipe> TRANSMUTATION_SERIALIZER = RECIPE_SERIALIZERS.register("transmutation", SerializerTransmutationRecipe::new);
}
