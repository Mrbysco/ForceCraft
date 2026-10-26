package com.mrbysco.forcecraft.client.tint;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.forcecraft.registry.ForceComponents;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public record RodColor(int color) implements ItemTintSource {
	public static final MapCodec<RodColor> MAP_CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default")
					.forGetter(RodColor::color)).apply(instance, RodColor::new)
	);

	public RodColor() {
		this(ARGB.opaque(0xfff755));
	}

	@Override
	public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
		if (stack.has(ForceComponents.SPEED)) {
			return ARGB.opaque(0xf8f8f8);
		} else if (stack.has(ForceComponents.HEALING)) {
			return ARGB.opaque(0x4ab118);
		} else if (stack.has(ForceComponents.HEAT)) {
			return ARGB.opaque(0xe76400);
		} else if (stack.has(ForceComponents.ENDER)) {
			return ARGB.opaque(0x105d50);
		} else if (stack.has(ForceComponents.TIME)) {
			return ARGB.opaque(0x006bf8);
		} else if (stack.has(ForceComponents.CAMO)) {
			return ARGB.opaque(0x258463);
		} else if (stack.has(ForceComponents.SIGHT)) {
			return ARGB.opaque(0x00cee7);
		}
		return color;
	}

	@Override
	public @NonNull MapCodec<RodColor> type() {
		return MAP_CODEC;
	}
}
