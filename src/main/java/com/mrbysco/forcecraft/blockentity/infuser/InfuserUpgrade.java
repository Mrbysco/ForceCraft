package com.mrbysco.forcecraft.blockentity.infuser;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mrbysco.forcecraft.registry.ForceUpgradeTypes;
import com.mrbysco.forcecraft.upgrade.UpgradeBookData;
import com.mrbysco.forcecraft.upgrade.UpgradeBookTier;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public interface InfuserUpgrade {
	Codec<InfuserUpgrade> CODEC = Codec.lazyInitialized(() ->
			ForceUpgradeTypes.UPGRADE_TYPES.byNameCodec().dispatch(InfuserUpgrade::codec, Function.identity()));

	MapCodec<? extends InfuserUpgrade> codec();

	default boolean canApply(ItemStack tool, UpgradeBookTier recipeTier) {
		return true;
	}

	boolean apply(ItemStack tool, UpgradeBookData book);

	int levelCap(ItemStack tool);
}
