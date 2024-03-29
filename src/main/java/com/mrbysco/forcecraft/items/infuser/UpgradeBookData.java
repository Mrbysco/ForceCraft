package com.mrbysco.forcecraft.items.infuser;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.recipe.InfuseRecipe;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UpgradeBookData {
	private UpgradeBookTier tier = UpgradeBookTier.ZERO;
	private final Map<Integer, Set<ResourceLocation>> recipesUsed = new HashMap<>();
	private int points = 0;
	private String progressCache = "";

	public UpgradeBookData(ItemStack book) {
		if (book.getItem() != ForceRegistry.UPGRADE_TOME.get()) {
			ForceCraft.LOGGER.error("invalid book data entering book {}", book);
			return;
		}
		CompoundTag tag = book.getTag();
		if (tag != null && tag.contains("tier")) {
			// is not empty, load it up
			this.read(book, tag);
		} else {
			updateCache();
			this.write(book);
		}
		// else its a new craft, or showing in JEI etc
	}

	// how many we need for next tier increment
	public int nextTier() {
		if (getTier() == UpgradeBookTier.FINAL) {
			return 0;
		}
		return Math.max(0, getTier().pointsForLevelup() - points);
	}

	public void onRecipeApply(InfuseRecipe recipe, ItemStack bookStack) {
		Integer tier = recipe.getTier().ordinal();
		Set<ResourceLocation> tierSet = new HashSet<>();

		if (recipesUsed.containsKey(tier)) {
			tierSet = recipesUsed.get(tier);
		}

		tierSet.add(recipe.getId());
		recipesUsed.put(tier, tierSet);

		tryLevelUp();

		this.write(bookStack);
	}

	public void incrementPoints(int incoming) {
		points += incoming;
		tryLevelUp();
	}

	// update level and points if levelup is possible
	private void tryLevelUp() {
		if (canLevelUp()) {
			// then go
			points -= this.getTier().pointsForLevelup();
			setTier(getTier().incrementTier());

			updateCache();
		}
	}

	private void updateCache() {
		//Update tooltip
		Set<ResourceLocation> thisTier = this.recipesUsed.get(this.tier.ordinal());
		int recipesThisTier = (thisTier == null) ? 0 : thisTier.size();
		if (!InfuseRecipe.RECIPESBYLEVEL.isEmpty()) {
			int totalThisTier = InfuseRecipe.RECIPESBYLEVEL.get(this.tier.ordinal()).size();
			this.progressCache = recipesThisTier + "/" + totalThisTier;
		}
	}

	private boolean canLevelUp() {
		// check more
		Set<ResourceLocation> thisTier = this.recipesUsed.get(this.tier.ordinal());
		int recipesThisTier = (thisTier == null) ? 0 : thisTier.size();
		int totalThisTier = InfuseRecipe.RECIPESBYLEVEL.get(this.tier.ordinal()).size();

//		ForceCraft.LOGGER.debug("can lvlup?  ?  " + recipesThisTier + " >= " + totalThisTier);

		updateCache();

		if (points < this.getTier().pointsForLevelup() || getTier() == UpgradeBookTier.FINAL) {
			return false;
		}

		// if this tier has total=5 recipes, i need to craft at least 5 unique recipes
		// this tier
		return recipesThisTier >= totalThisTier;
	}

	private void read(ItemStack book, CompoundTag tag) {
		progressCache = tag.getString("progressCache");
		setTier(UpgradeBookTier.values()[tag.getInt("tier")]);
		points = tag.getInt("points");

		for (UpgradeBookTier tier : UpgradeBookTier.values()) {
			Set<ResourceLocation> tierSet = new HashSet<>();

			ListTag listTag = (ListTag) tag.get("tier" + tier.ordinal());

			if (listTag != null) {
				for (Tag value : listTag) {
					CompoundTag tg = (CompoundTag) value;
					String id = tg.getString("id");

					// i dont know where this bug comes from
					if (!id.isEmpty() && !"minecraft:".equalsIgnoreCase(id))
						tierSet.add(ResourceLocation.tryParse(id));
				}
			}
			recipesUsed.put(tier.ordinal(), tierSet);
		}
	}

	public CompoundTag write(ItemStack bookInSlot) {
		CompoundTag tag = bookInSlot.getOrCreateTag();
		tag.putString("progressCache", progressCache);
		tag.putInt("tier", getTier().ordinal());
		tag.putInt("points", points);

		for (UpgradeBookTier tier : UpgradeBookTier.values()) {
			Set<ResourceLocation> tierSet = recipesUsed.get(tier.ordinal());
			if (tierSet == null) {
				tierSet = new HashSet<>();
			}
			ListTag listTag = new ListTag();
			for (ResourceLocation id : tierSet) {
				// i dont know where this bug comes from
				if (!"minecraft:".equalsIgnoreCase(id.toString())) {
					CompoundTag tg = new CompoundTag();
					tg.putString("id", id.toString());
					listTag.add(tg);
				}
			}

			tag.put("tier" + tier.ordinal(), listTag);
		}

		return tag;
	}

	public String getProgressCache() {
		return progressCache;
	}

	public UpgradeBookTier getTier() {
		return tier;
	}

	public void setTier(UpgradeBookTier tier) {
		this.tier = tier;
	}

	public int getPoints() {
		return points;
	}

}