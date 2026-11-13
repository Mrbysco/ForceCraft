package com.mrbysco.forcecraft.advancement.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.upgrade.UpgradeBookData;
import net.minecraft.advancements.predicates.SingleComponentItemPredicate;
import net.minecraft.core.component.DataComponentType;

public record UpgradeTomeTierPredicate(int minTier) implements SingleComponentItemPredicate<UpgradeBookData> {
	public static final Codec<UpgradeTomeTierPredicate> CODEC = RecordCodecBuilder.create(
			i -> i.group(
							Codec.INT.fieldOf("minTier").forGetter(UpgradeTomeTierPredicate::minTier)
					)
					.apply(i, UpgradeTomeTierPredicate::new)
	);

	@Override
	public DataComponentType<UpgradeBookData> componentType() {
		return ForceComponents.UPGRADE_BOOK.get();
	}

	@Override
	public boolean matches(UpgradeBookData value) {
		return value.tier().asInt() >= minTier;
	}
}
