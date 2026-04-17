package com.mrbysco.forcecraft.util;

import com.mrbysco.forcecraft.components.ForceComponents;
import com.mrbysco.forcecraft.items.ForceArmorItem;
import com.mrbysco.forcecraft.items.tools.ForceBowItem;
import com.mrbysco.forcecraft.items.tools.ForcePickaxeItem;
import com.mrbysco.forcecraft.items.tools.ForceRodItem;
import com.mrbysco.forcecraft.items.tools.ForceShearsItem;
import com.mrbysco.forcecraft.items.tools.ForceShovelItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class TooltipUtil {

	public static void addForceTooltips(ItemStack stack, Consumer<Component> builder) {
		final Item item = stack.getItem();
		if (stack.has(ForceComponents.TOOL_SPEED)) {
			if (item instanceof ForceBowItem || item instanceof ForceArmorItem || item instanceof ForceRodItem) {
				builder.accept(Component.translatable("item.infuser.builder.speed" + stack.get(ForceComponents.TOOL_SPEED)).withStyle(ChatFormatting.YELLOW));
			}
		}
		if (stack.has(ForceComponents.TOOL_LUMBERJACK)) {
			builder.accept(Component.translatable("item.infuser.tooltip.lumberjack").withStyle(ChatFormatting.YELLOW));
		}
		if (stack.has(ForceComponents.TOOL_LUCK)) {
			if (item instanceof ForceBowItem || item instanceof ForceArmorItem) {
				builder.accept(Component.translatable("item.infuser.builder.luck" + stack.get(ForceComponents.TOOL_LUCK)).withStyle(ChatFormatting.GREEN));
			}
		}
		if (stack.has(ForceComponents.TOOL_FORCE)) {
			builder.accept(Component.translatable("item.infuser.builder.force" + stack.get(ForceComponents.TOOL_FORCE)).withStyle(ChatFormatting.GOLD));
		}
		if (stack.has(ForceComponents.TOOL_STURDY)) {
			if (stack.getItem() instanceof ForceArmorItem) {
				builder.accept(Component.translatable("item.infuser.builder.sturdy" + stack.get(ForceComponents.TOOL_STURDY)).withStyle(ChatFormatting.DARK_PURPLE));
			}
		}
		if (stack.has(ForceComponents.TOOL_WING)) {
			builder.accept(Component.translatable("item.infuser.tooltip.wing"));
		}
		if (stack.has(ForceComponents.TOOL_BLEED)) {
			builder.accept(Component.translatable("item.infuser.builder.bleed" + stack.get(ForceComponents.TOOL_BLEED)).withStyle(ChatFormatting.RED));
		}
		if (stack.has(ForceComponents.TOOL_BANE)) {
			builder.accept(Component.translatable("item.infuser.tooltip.bane").append(" ")
					.append(Component.translatable("enchantment.level." + stack.get(ForceComponents.TOOL_BANE))).withStyle(ChatFormatting.RED));
		}
		if (stack.has(ForceComponents.TOOL_RAINBOW)) {
			builder.accept(Component.translatable("item.infuser.tooltip.rainbow").withStyle(ChatFormatting.GOLD));
		}
		if (stack.has(ForceComponents.TOOL_HEAT)) {
			if (item instanceof ForceShovelItem || item instanceof ForcePickaxeItem || item instanceof ForceShearsItem || item instanceof ForceArmorItem) {
				builder.accept(Component.translatable("item.infuser.tooltip.heat").withStyle(ChatFormatting.RED));
			}
		}
		if (stack.has(ForceComponents.TOOL_CAMO)) {
			builder.accept(Component.translatable("item.infuser.tooltip.camo").withStyle(ChatFormatting.DARK_GREEN));
		}
		if (stack.has(ForceComponents.TOOL_ENDER)) {
			builder.accept(Component.translatable("item.infuser.tooltip.ender").withStyle(ChatFormatting.DARK_PURPLE));
		}
		if (stack.has(ForceComponents.TOOL_FREEZING)) {
			builder.accept(Component.translatable("item.infuser.tooltip.freezing").withStyle(ChatFormatting.BLUE));
		}
		if (stack.has(ForceComponents.TOOL_TREASURE)) {
			builder.accept(Component.translatable("item.infuser.tooltip.treasure").withStyle(ChatFormatting.GOLD));
		}
		if (stack.has(ForceComponents.TOOL_LIGHT)) {
			builder.accept(Component.translatable("item.infuser.tooltip.light").withStyle(ChatFormatting.GOLD));
		}
		if (stack.has(ForceComponents.TOOL_SHARPNESS)) {
			if (stack.getItem() instanceof ForceArmorItem) {
				builder.accept(Component.translatable("item.infuser.tooltip.sharp").withStyle(ChatFormatting.GOLD));
			}
		}
		if (stack.has(ForceComponents.FORCE)) {
			MutableComponent t = Component.translatable("item.infuser.tooltip.forcelevel");
			t.append("" + stack.get(ForceComponents.FORCE));
			t.withStyle(ChatFormatting.GOLD);
			builder.accept(t);
		}
	}
}
