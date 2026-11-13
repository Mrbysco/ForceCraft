package com.mrbysco.forcecraft.advancement.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.forcecraft.registry.ForceComponents;
import net.minecraft.advancements.predicates.SingleComponentItemPredicate;
import net.minecraft.core.component.DataComponentType;

public record ForcePackTierPredicate(int minTier) implements SingleComponentItemPredicate<Integer> {
	public static final Codec<ForcePackTierPredicate> CODEC = RecordCodecBuilder.create(
			i -> i.group(
							Codec.INT.fieldOf("minTier").forGetter(ForcePackTierPredicate::minTier)
					)
					.apply(i, ForcePackTierPredicate::new)
	);

	@Override
	public DataComponentType<Integer> componentType() {
		return ForceComponents.PACK_TIER.get();
	}

	@Override
	public boolean matches(Integer value) {
		return value >= minTier;
	}
}
