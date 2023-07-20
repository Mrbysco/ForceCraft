package com.mrbysco.forcecraft.recipe;

import com.google.gson.JsonObject;
import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.blockentities.InfuserBlockEntity;
import com.mrbysco.forcecraft.blockentities.InfuserModifierType;
import com.mrbysco.forcecraft.capabilities.pack.PackItemStackHandler;
import com.mrbysco.forcecraft.items.infuser.UpgradeBookData;
import com.mrbysco.forcecraft.items.infuser.UpgradeBookTier;
import com.mrbysco.forcecraft.registry.ForceRecipeSerializers;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InfuseRecipe implements Recipe<InfuserBlockEntity> {
	private static final int MAX_SLOTS = 8;
	private static final Set<String> HASHES = new HashSet<>();
	public static final Map<Integer, List<InfuseRecipe>> RECIPESBYLEVEL = new HashMap<>();
	private final ResourceLocation id;
	public Ingredient input = Ingredient.EMPTY;
	public InfuserModifierType resultModifier;
	ItemStack output = ItemStack.EMPTY;
	private UpgradeBookTier tier;
	private Ingredient center;
	private int time;

	public InfuseRecipe(ResourceLocation id, Ingredient center, Ingredient input, InfuserModifierType result, UpgradeBookTier tier, ItemStack outputStack) {
		super();
		this.id = id;
		this.input = input;
		this.center = center;
		output = outputStack;
		resultModifier = result;
		this.setTier(tier);
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	@Override
	public boolean matches(InfuserBlockEntity inv, Level level) {
		for (int i = 0; i < inv.handler.getSlots(); i++) {
			ItemStack stack = inv.handler.getStackInSlot(i);
			if (i < InfuserBlockEntity.SLOT_TOOL) {
				return matchesModifier(inv, stack, false);
			}
		}
		return false;
	}

	public boolean matchesModifier(InfuserBlockEntity inv, ItemStack modifier, boolean ignoreInfused) {
		//Has the correct tier
		UpgradeBookData bd = new UpgradeBookData(inv.getBookInSlot());
		int bookTier = bd.getTier().ordinal();
		if (getTier().ordinal() > bookTier) {
			return false;
		}

		//Does the center match
		ItemStack centerStack = inv.handler.getStackInSlot(InfuserBlockEntity.SLOT_TOOL);
		boolean toolMatches = matchesTool(centerStack, ignoreInfused);
		boolean modifierMatches = matchesModifier(centerStack, modifier);

//		ForceCraft.LOGGER.info(cent + " does  match center for "+this.id);


		return toolMatches && modifierMatches;
	}

	public boolean matchesModifier(InfuserBlockEntity inv, ItemStack modifierStack) {
		ItemStack centerStack = inv.handler.getStackInSlot(InfuserBlockEntity.SLOT_TOOL);
		return matchesModifier(centerStack, modifierStack);
	}

	public boolean matchesModifier(ItemStack centerStack, ItemStack modifierStack) {
		if (modifierStack.getItem() == ForceRegistry.FORCE_PACK_UPGRADE.get()) {
			IItemHandler handler = centerStack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
			if (handler instanceof PackItemStackHandler) {
				if (((PackItemStackHandler) handler).getUpgrades() != getTier().ordinal() - 2) {
					return false;
				}
			}
		}

		return this.input.test(modifierStack);
	}

	public boolean matchesTool(ItemStack toolStack, boolean ignoreInfused) {
		if (!this.center.test(toolStack)) {
			// center doesn't match this recipe. move over
			return false;
		}
		if (!ignoreInfused) {
			//Ignore if the tool is infused in case of infusing for the first time
			return !toolStack.hasTag() || !toolStack.getTag().getBoolean("ForceInfused");
		}
		return true;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return height == 1 && width < MAX_SLOTS;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return output;
	}

	public boolean hasOutput() {
		return !output.isEmpty(); //should also be for modifier == ITEM
	}

	@Override
	public ItemStack assemble(InfuserBlockEntity inv, RegistryAccess registryAccess) {
		return getResultItem(registryAccess);
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public RecipeType<?> getType() {
		return ForceRecipes.INFUSER_TYPE.get();
	}

	public Ingredient getInput() {
		return input;
	}

	public Ingredient getCenter() {
		return center;
	}

	public InfuserModifierType getModifier() {
		return resultModifier;
	}

	public void setModifier(InfuserModifierType modifier) {
		this.resultModifier = modifier;
	}

	public UpgradeBookTier getTier() {
		return tier;
	}

	public void setTier(UpgradeBookTier tier) {
		this.tier = tier;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> ingredients = NonNullList.create();
		ingredients.add(center);
		ingredients.add(input);
		return ingredients;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ForceRecipeSerializers.INFUSER_SERIALIZER.get();
	}

	public static class SerializeInfuserRecipe implements RecipeSerializer<InfuseRecipe> {

		@Override
		public InfuseRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			InfuseRecipe recipe = null;
			try {
				Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
				Ingredient center = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "center"));

				String result = GsonHelper.getAsString(json, "result");

				// hardcoded mod id: no api support rip
				InfuserModifierType modifier = InfuserModifierType.valueOf(result.replace(Reference.MOD_ID + ":", "").toUpperCase());

				ItemStack output = ItemStack.EMPTY;
				if (modifier == InfuserModifierType.ITEM && GsonHelper.isValidNode(json, "output")) {
					output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
				}
				int tier = GsonHelper.getAsInt(json, "tier");

				recipe = new InfuseRecipe(recipeId, center, ingredient, modifier, UpgradeBookTier.values()[tier], output);
				recipe.setTime(GsonHelper.getAsInt(json, "time"));
				addRecipe(recipe);
				return recipe;
			} catch (Exception e) {
				ForceCraft.LOGGER.error("Error loading recipe " + recipeId, e);
				return null;
			}
		}

		@Override
		public InfuseRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			Ingredient center = Ingredient.fromNetwork(buffer);
			Ingredient ing = Ingredient.fromNetwork(buffer);
			int enumlon = buffer.readVarInt();
			int tier = buffer.readInt();

			InfuseRecipe r = new InfuseRecipe(recipeId, center, ing, InfuserModifierType.values()[enumlon], UpgradeBookTier.values()[tier], buffer.readItem());

			r.setTime(buffer.readInt());
			// server reading recipe from client or vice/versa
			addRecipe(r);
			return r;
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, InfuseRecipe recipe) {
			recipe.center.toNetwork(buffer);
			recipe.input.toNetwork(buffer);
			buffer.writeVarInt(recipe.resultModifier.ordinal());
			buffer.writeInt(recipe.getTier().ordinal());
			buffer.writeItem(recipe.getResultItem(null));
			buffer.writeInt(recipe.getTime());
		}
	}

	public static boolean addRecipe(InfuseRecipe recipe) {
		ResourceLocation id = recipe.getId();
		if (HASHES.contains(id.toString())) {
			return false;
		}
		int thisTier = recipe.getTier().ordinal();
		//by level is for the GUI 
		if (!RECIPESBYLEVEL.containsKey(thisTier)) {
			RECIPESBYLEVEL.put(thisTier, new ArrayList<>());
		}
		RECIPESBYLEVEL.get(thisTier).add(recipe);
		HASHES.add(id.toString());
		ForceCraft.LOGGER.info("Recipe loaded {} -> {} , {}", id.toString(), recipe.resultModifier, recipe.input.toJson());
		return true;
	}
}
