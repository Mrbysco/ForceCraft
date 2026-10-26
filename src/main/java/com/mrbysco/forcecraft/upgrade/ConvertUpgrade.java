package com.mrbysco.forcecraft.upgrade;

import com.mojang.serialization.MapCodec;
import com.mrbysco.forcecraft.blockentity.infuser.InfuserUpgrade;
import net.minecraft.world.item.ItemStack;

public record ConvertUpgrade() implements InfuserUpgrade {
	public static final MapCodec<ConvertUpgrade> CODEC = MapCodec.unit(ConvertUpgrade::new);

	@Override
	public MapCodec<ConvertUpgrade> codec() {
		return CODEC;
	}

	@Override
	public boolean apply(ItemStack tool, UpgradeBookData book) {
		return true;
	}

	@Override
	public int levelCap(ItemStack tool) {
		return 1;
	}
}