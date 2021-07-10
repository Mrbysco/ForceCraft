package mrbysco.forcecraft.recipe;

import com.google.gson.JsonObject;
import mrbysco.forcecraft.ForceCraft;
import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.blocks.infuser.InfuserModifierType;
import mrbysco.forcecraft.blocks.infuser.InfuserTileEntity;
import mrbysco.forcecraft.capablilities.pack.PackItemStackHandler;
import mrbysco.forcecraft.items.infuser.UpgradeBookData;
import mrbysco.forcecraft.items.infuser.UpgradeBookTier;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InfuseRecipe implements IRecipe<InfuserTileEntity> {
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
	
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	@Override
	public boolean matches(InfuserTileEntity inv, World worldIn) {
		for(int i = 0; i < inv.handler.getSlots(); i++) {
			ItemStack stack = inv.handler.getStackInSlot(i);
			if (i < InfuserTileEntity.SLOT_TOOL) {
				return matchesModifier(inv, stack, false);
			}
		}
		return false;
	}

	public boolean matchesModifier(InfuserTileEntity inv, ItemStack modifier, boolean ignoreInfused) {
		//Has the correct tier
		UpgradeBookData bd = new UpgradeBookData(inv.getBookInSlot());
		int bookTier = bd.getTier().ordinal();
		if (getTier().ordinal() > bookTier) {
			return false;
		}

		//Does the center match
		boolean centerMatches = matchesTool(inv, inv.handler.getStackInSlot(InfuserTileEntity.SLOT_TOOL), ignoreInfused);
		if(!centerMatches) {
			return false;
		}
		ItemStack cent = inv.handler.getStackInSlot(InfuserTileEntity.SLOT_TOOL);
		if(!this.center.test(cent)) {
			// center doesn't match this recipe. move over
			return false;
		}
		if(!ignoreInfused) {
			//Ignore if the tool is infused in case of infusing for the first time
			if((cent.hasTag() && cent.getTag().getBoolean("ForceInfused"))) {
				return false;
			}
		}

//		ForceCraft.LOGGER.info(cent + " does  match center for "+this.id);

		if(modifier.getItem() == ForceRegistry.FORCE_PACK_UPGRADE.get()) {
			IItemHandler handler = cent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
			if(handler instanceof PackItemStackHandler) {
				if(((PackItemStackHandler) handler).getUpgrades() != getTier().ordinal() - 2) {
					return false;
				}
			}
		}

		if(input.test(modifier)) {
			return true;
		}
		return false;
	}

	public boolean matchesTool(InfuserTileEntity inv, ItemStack toolStack, boolean ignoreInfused) {
		if(!this.center.test(toolStack)) {
			// center doesn't match this recipe. move over
			return false;
		}
		if(!ignoreInfused) {
			//Ignore if the tool is infused in case of infusing for the first time
			if((toolStack.hasTag() && toolStack.getTag().getBoolean("ForceInfused"))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean canFit(int width, int height) {
		return height == 1 && width < MAX_SLOTS;
	}

	@Override
	public ItemStack getRecipeOutput() { 
		return output;  
	}
	
	public boolean hasOutput() {
		return !output.isEmpty(); //should also be for modifier == ITEM
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
		return ForceRecipes.INFUSER_TYPE;
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
	public IRecipeSerializer<?> getSerializer() {
		return ForceRecipes.INFUSER_SERIALIZER.get();
	}

	public static class SerializeInfuserRecipe extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<InfuseRecipe> {

		@Override
		public InfuseRecipe read(ResourceLocation recipeId, JsonObject json) {
			InfuseRecipe recipe = null;
			try {
				Ingredient ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "ingredient"));
				Ingredient center = Ingredient.deserialize(JSONUtils.getJsonObject(json, "center"));
				
				String result = JSONUtils.getString(json, "result");

				// hardcoded mod id: no api support rip
				InfuserModifierType modifier = InfuserModifierType.valueOf(result.replace(Reference.MOD_ID + ":","").toUpperCase());
				
		        ItemStack output = ItemStack.EMPTY;
		        if(modifier == InfuserModifierType.ITEM && JSONUtils.hasField(json, "output") ) {
		        	output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "output"));
		        }
				int tier = JSONUtils.getInt(json, "tier");
				
				recipe = new InfuseRecipe(recipeId, center, ingredient, modifier, UpgradeBookTier.values()[tier], output);
				recipe.setTime(JSONUtils.getInt(json, "time"));
				addRecipe(recipe);
				return recipe;
			} catch (Exception e) {
				ForceCraft.LOGGER.error("Error loading recipe " + recipeId, e);
				return null;
			}
		}

		@Override
		public InfuseRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			Ingredient center = Ingredient.read(buffer);
			Ingredient ing = Ingredient.read(buffer);
			int enumlon = buffer.readVarInt();
			int tier = buffer.readInt();
			
			InfuseRecipe r = new InfuseRecipe(recipeId, center, ing, InfuserModifierType.values()[enumlon], UpgradeBookTier.values()[tier], buffer.readItemStack());

			r.setTime(buffer.readInt());
			// server reading recipe from client or vice/versa
			addRecipe(r);
			return r;
		}

		@Override
		public void write(PacketBuffer buffer, InfuseRecipe recipe) {
			recipe.center.write(buffer);
			recipe.input.write(buffer);
			buffer.writeVarInt(recipe.resultModifier.ordinal());
			buffer.writeInt(recipe.getTier().ordinal());
			buffer.writeItemStack(recipe.getRecipeOutput());
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
		if(!RECIPESBYLEVEL.containsKey(thisTier)) {
			RECIPESBYLEVEL.put(thisTier, new ArrayList<>());
		}
		RECIPESBYLEVEL.get(thisTier).add(recipe);
		HASHES.add(id.toString());
		ForceCraft.LOGGER.info("Recipe loaded {} -> {} , {}" , id.toString(), recipe.resultModifier, recipe.input.serialize());
		return true;
	}
}
