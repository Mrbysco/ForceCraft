package com.mrbysco.forcecraft.registry.material;

import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

public class ModToolTiers {
	public static final ToolMaterial FORCE = new ToolMaterial(ForceTags.INCORRECT_FOR_FORCE, 1561, 8.0F, 8.0F, 22,
			ForceTags.FORCE_REPAIR_INGREDIENTS);


	public static final Tier FORCE = new SimpleTier(ForceTags.INCORRECT_FOR_FORCE, 1561, 8.0F, 8.0F, 22,
			() -> Ingredient.of(ForceRegistry.FORCE_GEM));
}
