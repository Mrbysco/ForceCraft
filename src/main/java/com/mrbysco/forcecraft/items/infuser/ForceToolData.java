package com.mrbysco.forcecraft.items.infuser;

import com.mrbysco.forcecraft.components.ForceComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class ForceToolData {
	private ItemStack stack;

	private int force = 0;

	public int getForce() {
		return force;
	}

	public void setForce(int force) {
		this.force = force;
	}

	public ForceToolData(ItemStack tool) {
		this.stack = tool;
		this.force = tool.getOrDefault(ForceComponents.FORCE, 0);
	}

	public void charge(int incoming) {
		this.force += incoming;
		this.stack.set(ForceComponents.FORCE, this.force);
	}

	public void attachInformation(Consumer<Component> builder) {
		if (this.force > 0) {
			MutableComponent t = Component.translatable("item.infuser.tooltip.forcelevel");
			t.append("" + this.force);
			t.withStyle(ChatFormatting.GOLD);
			builder.accept(t);
		}
	}
}
