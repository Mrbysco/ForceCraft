package mrbysco.forcecraft.recipe;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.JsonObject;

import mrbysco.forcecraft.ForceCraft;
import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.blocks.infuser.InfuserModifierType;
import mrbysco.forcecraft.blocks.infuser.InfuserTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class InfuseRecipe implements IRecipe<InfuserTileEntity> {

	private static final int MAX_SLOTS = 8;
	private static final Set<String> HASHES = new HashSet<>();
	public static final Set<InfuseRecipe> RECIPES = new HashSet<>();
	private final ResourceLocation id;
	public Ingredient input = Ingredient.EMPTY;
	public InfuserModifierType modifier;
	ItemStack resultStack = ItemStack.EMPTY; // unused!!!! for now
	public int tier;

	public InfuseRecipe(ResourceLocation id, Ingredient input, InfuserModifierType result, int tier, ItemStack itemStack) {
		super();
		this.id = id;
		this.input = input;
		resultStack = itemStack;
		modifier = result; 
		this.tier = tier;
	}

	@Override
	public boolean matches(InfuserTileEntity inv, World worldIn) {
		for(int i = 0; i < inv.handler.getSlots(); i++) {
			ItemStack stack = inv.handler.getStackInSlot(i);
			if (input.test(stack)) {
				//at least one is true
				return true;
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
	public Ingredient getInput() {
		return input;
	}

	public void setInput(Ingredient input) {
		this.input = input;
	}

	public InfuserModifierType getModifier() {
		return modifier;
	}

	public void setModifier(InfuserModifierType modifier) {
		this.modifier = modifier;
	}

	public int getTier() {
		return tier;
	}

	public void setTier(int tier) {
		this.tier = tier;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static final SerializeInfuserRecipe SERIALIZER = new SerializeInfuserRecipe();

	public static class SerializeInfuserRecipe extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<InfuseRecipe> {

		SerializeInfuserRecipe() {
			// This registry name is what people will specify in their json files.
			this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "infuser"));
		}

		@Override
		public InfuseRecipe read(ResourceLocation recipeId, JsonObject json) {
			InfuseRecipe recipe = null;
			try {
				Ingredient ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "ingredient"));

				String result = JSONUtils.getString(json, "result");

				// hardcoded mod id: no api support rip
				InfuserModifierType type = InfuserModifierType.valueOf(result.replace("forcecraft:","").toUpperCase());
				int tier = JSONUtils.getInt(json, "tier");
				recipe = new InfuseRecipe(recipeId, ingredient, type, tier, ItemStack.EMPTY);
				addRecipe(recipe);
				return recipe;
			} catch (Exception e) {
				ForceCraft.LOGGER.error("Error loading recipe " + recipeId, e);
				return null;
			}
		}

		@Override
		public InfuseRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {

			Ingredient ing = Ingredient.read(buffer);
			int enumlon = buffer.readVarInt();
			
			InfuseRecipe r = new InfuseRecipe(recipeId, ing, InfuserModifierType.values()[enumlon], buffer.readInt() ,buffer.readItemStack());

			// server reading recipe from client or vice/versa
			addRecipe(r);
			return r;
		}

		@Override
		public void write(PacketBuffer buffer, InfuseRecipe recipe) {

			recipe.input.write(buffer);
			buffer.writeVarInt(recipe.modifier.ordinal());
			buffer.writeInt(recipe.tier);
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
		ForceCraft.LOGGER.info("Recipe loaded {} -> {} , {}" , id.toString(), r.modifier, r.input.serialize());
		return true;
	}

}
