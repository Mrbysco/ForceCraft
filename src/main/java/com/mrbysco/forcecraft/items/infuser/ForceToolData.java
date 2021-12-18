package com.mrbysco.forcecraft.items.infuser;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ForceToolData {

	private int force = 0;

	public int getForce() {
		return force;
	}

	public void setForce(int force) {
		this.force = force;
	}

	public ForceToolData(ItemStack tool) {
		CompoundTag tag = tool.getTag();
		if (tag != null && tag.contains("force")) {
			this.read(tool, tag);
		}
	}

	private void read(ItemStack tool, CompoundTag tag) {
		force = tag.getInt("force");
	}

	public CompoundTag write(ItemStack tool) {
		CompoundTag tag = tool.getOrCreateTag();
		tag.putInt("force", force);
		return tag;
	}

	public void charge(int incoming) {
		force += incoming;
	}

	public void attachInformation(List<Component> tooltip) {
		if (this.force > 0) {
			TranslatableComponent t = new TranslatableComponent("item.infuser.tooltip.forcelevel");
			t.append("" + this.force);
			t.withStyle(ChatFormatting.GOLD);
			tooltip.add(t);
		}
	}
}
