package mrbysco.forcecraft.recipe;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import mrbysco.forcecraft.ForceCraft;
import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.tiles.InfuserTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class InfuseRecipe implements IRecipe<InfuserTileEntity> {

	private static final int MAX_SLOTS = 8;
	private static final Set<String> HASHES = new HashSet<>();
	public static final Set<InfuseRecipe> RECIPES = new HashSet<>();
	private final ResourceLocation id;
	public NonNullList<Ingredient> input = NonNullList.withSize(MAX_SLOTS, Ingredient.EMPTY);
	String result;
	ItemStack resultStack = ItemStack.EMPTY; // unused!!!! for now

	public InfuseRecipe(ResourceLocation id, NonNullList<Ingredient> input, String result, ItemStack itemStack) {
		super();
		this.id = id;
		this.input = input;
		this.result = result;
		resultStack = itemStack;
	}

	@Override
	public boolean matches(InfuserTileEntity inv, World worldIn) {
		for (int i = 0; i < input.size(); i++) {
			Ingredient current = input.get(i);
			ItemStack stack = inv.handler.getStackInSlot(i);
			if (current.test(stack)) {
				//
			}
		}
		return false;
	}

	@Override
	public boolean canFit(int width, int height) {
		return height == 1 && width < MAX_SLOTS;
	}

	@Override
	public ItemStack getRecipeOutput() {
		// output is the center of the infuser but modified, so this is unused
		return resultStack; // unused ? for now
	}

	@Override
	public ItemStack getCraftingResult(InfuserTileEntity inv) {
		return getRecipeOutput();
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public IRecipeType<?> getType() {
		return ModRecipeType.INFUSER;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return null;
	}

	public static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
		NonNullList<Ingredient> nonnulllist = NonNullList.create();

		for (int i = 0; i < ingredientArray.size(); ++i) {
			Ingredient ingredient = Ingredient.deserialize(ingredientArray.get(i));
			if (!ingredient.hasNoMatchingItems()) {
				nonnulllist.add(ingredient);
			}
		}

		return nonnulllist;
	}

	public static final SerializeGrinderRecipe SERIALIZER = new SerializeGrinderRecipe();

	public static class SerializeGrinderRecipe extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<InfuseRecipe> {

		SerializeGrinderRecipe() {
			// This registry name is what people will specify in their json files.
			this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "infuser"));
		}

		@Override
		public InfuseRecipe read(ResourceLocation recipeId, JsonObject json) {
			InfuseRecipe recipe = null;
			try {
				NonNullList<Ingredient> nonnulllist = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));

				if (nonnulllist.size() > MAX_SLOTS) {
					ForceCraft.LOGGER.info("error: Too Many Ingredients {} Error loading recipe {} ",
							nonnulllist.size(), recipeId);
					return null;
				}

				recipe = new InfuseRecipe(recipeId, nonnulllist, JSONUtils.getString(json, "result"), ItemStack.EMPTY);
				addRecipe(recipe);
				return recipe;
			} catch (Exception e) {
				ForceCraft.LOGGER.error("Error loading recipe " + recipeId, e);
				return null;
			}
		}

		@Override
		public InfuseRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {

			int i = buffer.readVarInt();
			NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

			for (int j = 0; j < nonnulllist.size(); ++j) {
				nonnulllist.set(j, Ingredient.read(buffer));
			}

			InfuseRecipe r = new InfuseRecipe(recipeId, nonnulllist, buffer.readString(), buffer.readItemStack());

			// server reading recipe from client or vice/versa
			addRecipe(r);
			return r;
		}

		@Override
		public void write(PacketBuffer buffer, InfuseRecipe recipe) {

			buffer.writeVarInt(recipe.input.size());

			for (Ingredient ingredient : recipe.input) {
				ingredient.write(buffer);
			}
			buffer.writeItemStack(recipe.getRecipeOutput());
		}
	}

	public static boolean addRecipe(InfuseRecipe r) {
		ResourceLocation id = r.getId();
		if (HASHES.contains(id.toString())) {
			return false;
		}
		RECIPES.add(r);
		HASHES.add(id.toString());
		ForceCraft.LOGGER.info("Recipe loaded {} -> {} , {}" , id.toString(), r.result, r.input.size());
		return true;
	}

}
