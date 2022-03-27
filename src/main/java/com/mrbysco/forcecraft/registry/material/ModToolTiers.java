package com.mrbysco.forcecraft.registry.material;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class ModToolTiers {
	public static final Tier FORCE = TierSortingRegistry.registerTier(
			new ForgeTier(3, 1561, 8.0F, 8.0F, 22, ForceTags.NEEDS_FORCE_TOOL, () -> Ingredient.of(Items.STICK)),
			new ResourceLocation(Reference.MOD_ID, "cactus"), List.of(Tiers.IRON), List.of(Tiers.DIAMOND));
}
