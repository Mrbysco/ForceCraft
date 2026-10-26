package com.mrbysco.forcecraft.upgrade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public record UpgradeBookData(UpgradeBookTier tier, List<Identifier> recipesUsed, int points) {
	public static final UpgradeBookData DEFAULT = new UpgradeBookData(UpgradeBookTier.ONE, List.of(), 0);
	private static final int REPEAT_POINTS = 1;
	public static final Codec<UpgradeBookData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
			UpgradeBookTier.CODEC.fieldOf("tier").forGetter(UpgradeBookData::tier),
			Identifier.CODEC.listOf().fieldOf("recipesUsed").forGetter(UpgradeBookData::recipesUsed),
			Codec.INT.fieldOf("points").forGetter(UpgradeBookData::points)
	).apply(inst, UpgradeBookData::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, UpgradeBookData> STREAM_CODEC =
			ByteBufCodecs.fromCodecWithRegistries(CODEC);

	public UpgradeBookData withInfusion(Identifier id, UpgradeBookTier recipeTier, int incoming) {
		boolean firstTime = recipeTier == tier && !recipesUsed.contains(id);
		return withRecipeUsed(id, recipeTier)
				.withPoints(firstTime ? incoming : REPEAT_POINTS);
	}

	public UpgradeBookData withRecipeUsed(Identifier id, UpgradeBookTier recipeTier) {
		if (recipeTier != tier || recipesUsed.contains(id)) {
			return this;
		}
		List<Identifier> used = new ArrayList<>(recipesUsed);
		used.add(id);
		return new UpgradeBookData(tier, used, points);
	}

	public UpgradeBookData withPoints(int incoming) {
		int total = points + incoming;
		UpgradeBookTier newTier = tier;
		if (!newTier.isFinal() && total >= newTier.pointsForLevelup()) {
			newTier = newTier.next();
		}
		return new UpgradeBookData(newTier, newTier == tier ? recipesUsed : List.of(), total);
	}

	public UpgradeBookData withTier(UpgradeBookTier newTier) {
		return new UpgradeBookData(newTier, List.of(), points);
	}

	public int pointsToNextTier() {
		return tier.isFinal() ? 0 : Math.max(0, tier.pointsForLevelup() - points);
	}

	public String progress(int recipesInTier) {
		return recipesUsed.size() + "/" + recipesInTier;
	}

	private UpgradeBookData levelUpIfReady(int recipesInTier) {
		boolean ready = !tier.isFinal() && recipesUsed.size() >= recipesInTier;
		if (!ready) {
			return this;
		}
		return new UpgradeBookData(tier.next(), List.of(), points);
	}
}
