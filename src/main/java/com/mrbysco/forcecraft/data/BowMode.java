package com.mrbysco.forcecraft.data;

import com.mojang.serialization.Codec;
import com.mrbysco.forcecraft.registry.ForceComponents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public enum BowMode implements StringRepresentable {
	NORMAL("forcecraft.bow.mode.normal", () -> null),
	TORCH("forcecraft.bow.mode.torch", () -> null),
	ICE("forcecraft.bow.mode.ice", ForceComponents.FREEZING::get),
	FIRE("forcecraft.bow.mode.fire", ForceComponents.HEAT::get);

	public static final Codec<BowMode> CODEC = StringRepresentable.fromEnum(BowMode::values);

	private final Supplier<DataComponentType<?>> optionalRequirement;
	private final String langKey;

	BowMode(String langKey, Supplier<DataComponentType<?>> optionalRequirement) {
		this.langKey = langKey;
		this.optionalRequirement = optionalRequirement;
	}

	public BowMode getNext(ItemStack bow) {
		BowMode[] modes = values();
		for (int i = 1; i < modes.length; i++) {
			BowMode mode = modes[(ordinal() + i) % modes.length];
			DataComponentType<?> requirement = mode.optionalRequirement.get();

			if (requirement == null || bow.has(requirement)) {
				return mode;
			}
		}
		return this;
	}

	public Component getName() {
		return Component.translatable(langKey);
	}

	@Override
	public String getSerializedName() {
		return String.valueOf(ordinal());
	}
}
