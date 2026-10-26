package com.mrbysco.forcecraft.upgrade;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.forcecraft.blockentity.infuser.InfuserUpgrade;
import com.mrbysco.forcecraft.registry.ForceComponents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

public record FlagUpgrade(DataComponentType<Boolean> component) implements InfuserUpgrade {

	@SuppressWarnings("unchecked")
	public static final MapCodec<FlagUpgrade> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			BuiltInRegistries.DATA_COMPONENT_TYPE.byNameCodec()
					.xmap(type -> (DataComponentType<Boolean>) type, type -> type)
					.fieldOf("component").forGetter(FlagUpgrade::component)
	).apply(i, FlagUpgrade::new));

	@Override
	public MapCodec<FlagUpgrade> codec() {
		return CODEC;
	}

	public static FlagUpgrade of(DataComponentType<Boolean> component) {
		return new FlagUpgrade(component);
	}

	@Override
	public boolean apply(ItemStack tool, UpgradeBookData book) {
		if (tool.getOrDefault(component, false)) return false;

		tool.set(component, true);
		tool.set(ForceComponents.FORCE_INFUSED, true);
		return true;
	}

	@Override
	public int levelCap(ItemStack tool) {
		return 1;
	}
}
