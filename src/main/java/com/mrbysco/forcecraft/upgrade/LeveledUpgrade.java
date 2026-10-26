package com.mrbysco.forcecraft.upgrade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.forcecraft.blockentity.infuser.InfuserUpgrade;
import com.mrbysco.forcecraft.registry.ForceComponents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record LeveledUpgrade(DataComponentType<Integer> component, int cap, Optional<TagKey<Item>> limitedTag) implements InfuserUpgrade {

	public static LeveledUpgrade of(DataComponentType<Integer> component, int cap) {
		return new LeveledUpgrade(component, cap, Optional.empty());
	}

	public static LeveledUpgrade of(DataComponentType<Integer> component, int cap, TagKey<Item> tag) {
		return new LeveledUpgrade(component, cap, Optional.of(tag));
	}

	@SuppressWarnings("unchecked")
	public static final MapCodec<LeveledUpgrade> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			BuiltInRegistries.DATA_COMPONENT_TYPE.byNameCodec()
					.xmap(type -> (DataComponentType<Integer>) type, type -> type)
					.fieldOf("component").forGetter(LeveledUpgrade::component),
			Codec.INT.fieldOf("cap").forGetter(LeveledUpgrade::cap),
			TagKey.codec(Registries.ITEM).optionalFieldOf("limitedTag").forGetter(LeveledUpgrade::limitedTag)
	).apply(i, LeveledUpgrade::new));

	@Override
	public MapCodec<LeveledUpgrade> codec() {
		return CODEC;
	}

	@Override
	public int levelCap(ItemStack tool) {
		if (limitedTag.isPresent() && tool.is(limitedTag.get())) return 1;
		return cap;
	}

	@Override
	public boolean apply(ItemStack tool, UpgradeBookData book) {
		int level = tool.getOrDefault(component, 0);
		if (level >= levelCap(tool)) return false;

		tool.set(component, level + 1);
		tool.set(ForceComponents.FORCE_INFUSED, true);
		return true;
	}
}
