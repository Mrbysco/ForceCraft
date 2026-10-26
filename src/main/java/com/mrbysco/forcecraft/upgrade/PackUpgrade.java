package com.mrbysco.forcecraft.upgrade;

import com.mojang.serialization.MapCodec;
import com.mrbysco.forcecraft.blockentity.infuser.InfuserUpgrade;
import com.mrbysco.forcecraft.item.ForcePackItem;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.storage.PackStackHandler;
import com.mrbysco.forcecraft.storage.StorageManager;
import com.mrbysco.forcecraft.util.ResourceHandlerUtils;
import net.minecraft.world.item.ItemStack;

public record PackUpgrade() implements InfuserUpgrade {
	public static final MapCodec<PackUpgrade> CODEC = MapCodec.unit(PackUpgrade::new);

	@Override
	public MapCodec<PackUpgrade> codec() {
		return CODEC;
	}

	@Override
	public boolean canApply(ItemStack tool, UpgradeBookTier recipeTier) {
		if (!(tool.getItem() instanceof ForcePackItem)) return false;
		return StorageManager.getOrCreatePack(tool).getInventory().getUpgrades() == recipeTier.ordinal() - 1;
	}

	@Override
	public boolean apply(ItemStack tool, UpgradeBookData book) {
		if (!(tool.getItem() instanceof ForcePackItem)) return false;

		PackStackHandler handler = StorageManager.getOrCreatePack(tool).getInventory();
		if (!handler.canUpgrade(book)) return false;

		handler.applyUpgrade();
		tool.set(ForceComponents.SLOTS_USED, ResourceHandlerUtils.getUsedSlots(handler));
		tool.set(ForceComponents.SLOTS_TOTAL, handler.getSlotsInUse());
		tool.set(ForceComponents.PACK_TIER, handler.getUpgrades());
		return true;
	}

	@Override
	public int levelCap(ItemStack tool) {
		return tool.getItem() instanceof ForcePackItem ? 4 : 0;
	}
}
