package com.mrbysco.forcecraft.attachment.magnet;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.List;

import static com.mrbysco.forcecraft.attachment.CapabilityHandler.MAGNET;

public class MagnetAttachment implements IMagnet, INBTSerializable<CompoundTag> {
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
		if (stack.hasData(MAGNET)) {
			MagnetAttachment attachment = stack.getData(MAGNET);
			if (attachment.isActivated()) {
				tooltip.add(Component.translatable("forcecraft.magnet_glove.active").withStyle(ChatFormatting.GREEN));
			} else {
				tooltip.add(Component.translatable("forcecraft.magnet_glove.deactivated").withStyle(ChatFormatting.RED));
			}
			tooltip.add(Component.empty());
			tooltip.add(Component.translatable("forcecraft.magnet_glove.change").withStyle(ChatFormatting.BOLD));
		}
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();
		tag.putBoolean("activated", this.isActivated());
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag tag) {
		this.setActivation(tag.getBoolean("activated"));
	}
}
