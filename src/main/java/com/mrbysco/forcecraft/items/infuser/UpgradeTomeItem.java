package com.mrbysco.forcecraft.items.infuser;

import com.mrbysco.forcecraft.components.ForceComponents;
import com.mrbysco.forcecraft.items.BaseItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.List;
import java.util.function.Consumer;

public class UpgradeTomeItem extends BaseItem {

	public UpgradeTomeItem(Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
		UpgradeBookData bd = itemStack.getOrDefault(ForceComponents.UPGRADE_BOOK, UpgradeBookData.DEFAULT);

		MutableComponent tt = Component.translatable("item.forcecraft.upgrade_tome.tt.tier");
		tt.withStyle(Style.EMPTY.applyFormat(ChatFormatting.AQUA));
		tt.append(" " + bd.tier());
		if (!bd.progressCache().isEmpty()) {
			tt.append(" : " + bd.progressCache());
		}
		builder.accept(tt);

		if (bd.tier() == UpgradeBookTier.FINAL) {
			tt = Component.translatable("item.forcecraft.upgrade_tome.tt.max");
			tt.withStyle(Style.EMPTY.applyFormat(ChatFormatting.AQUA));
		} else {
			tt = Component.translatable("item.forcecraft.upgrade_tome.tt.points");
			tt.withStyle(Style.EMPTY.applyFormat(ChatFormatting.AQUA));
			tt.append(" " + bd.points());
			builder.accept(tt);

			tt = Component.translatable("item.forcecraft.upgrade_tome.tt.nexttier");
			tt.withStyle(Style.EMPTY.applyFormat(ChatFormatting.AQUA));
			tt.append(" " + bd.nextTier(itemStack));
		}
		builder.accept(tt);


		if (!tooltipFlag.hasShiftDown()) {
			builder.accept(Component.translatable("forcecraft.tooltip.press_shift"));
			return;
		}

		builder.accept(Component.translatable("item.forcecraft.upgrade_tome.tt.point_info"));
		tt.withStyle(Style.EMPTY.applyFormat(ChatFormatting.AQUA));
	}

	public static void onModifierApplied(ItemStack bookInSlot, ItemStack modifier, ItemStack tool) {
		UpgradeBookData.incrementPoints(bookInSlot, 25); // TODO: points per modifier upgrade value !!
	}
}
