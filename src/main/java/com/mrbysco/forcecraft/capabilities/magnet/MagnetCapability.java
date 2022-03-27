package com.mrbysco.forcecraft.capabilities.magnet;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.mrbysco.forcecraft.capabilities.CapabilityHandler.CAPABILITY_MAGNET;

public class MagnetCapability implements IMagnet, ICapabilitySerializable<CompoundTag>, ICapabilityProvider {
	boolean active;

	@Override
	public boolean isActivated() {
		return active;
	}

	@Override
	public void activate() {
		active = true;
	}

	@Override
	public void deactivate() {
		active = false;
	}

	@Override
	public void setActivation(boolean value) {
		active = value;
	}

	public static void attachInformation(ItemStack stack, List<Component> tooltip) {
		stack.getCapability(CAPABILITY_MAGNET).ifPresent(cap -> {
			if (cap.isActivated()) {
				tooltip.add(new TranslatableComponent("forcecraft.magnet_glove.active").withStyle(ChatFormatting.GREEN));
			} else {
				tooltip.add(new TranslatableComponent("forcecraft.magnet_glove.deactivated").withStyle(ChatFormatting.RED));
			}
			tooltip.add(TextComponent.EMPTY);
			tooltip.add(new TranslatableComponent("forcecraft.magnet_glove.change").withStyle(ChatFormatting.BOLD));
		});
	}

	@Override
	public CompoundTag serializeNBT() {
		return writeNBT(this);
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		readNBT(this, nbt);
	}

	public static CompoundTag writeNBT(IMagnet instance) {
		if (instance == null) {
			return null;
		}
		CompoundTag nbt = new CompoundTag();
		nbt.putBoolean("activated", instance.isActivated());

		return nbt;
	}

	public static void readNBT(IMagnet instance, CompoundTag tag) {
		instance.setActivation(tag.getBoolean("activated"));
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		return CAPABILITY_MAGNET.orEmpty(cap, LazyOptional.of(() -> this));
	}

	public final Capability<IMagnet> getCapability() {
		return CAPABILITY_MAGNET;
	}
}
