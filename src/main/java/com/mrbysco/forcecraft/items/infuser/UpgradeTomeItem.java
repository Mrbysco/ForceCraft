package com.mrbysco.forcecraft.items.infuser;

import com.mrbysco.forcecraft.items.BaseItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class UpgradeTomeItem extends BaseItem {

	public UpgradeTomeItem(Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, level, tooltip, flagIn);
		UpgradeBookData bd = new UpgradeBookData(stack);

		MutableComponent tt = Component.translatable("item.forcecraft.upgrade_tome.tt.tier");
		tt.withStyle(Style.EMPTY.applyFormat(ChatFormatting.AQUA));
		tt.append(" " + bd.getTier());
		if (!bd.getProgressCache().isEmpty()) {
			tt.append(" : " + bd.getProgressCache());
		}
		tooltip.add(tt);

		if (bd.getTier() == UpgradeBookTier.FINAL) {
			tt = Component.translatable("item.forcecraft.upgrade_tome.tt.max");
			tt.withStyle(Style.EMPTY.applyFormat(ChatFormatting.AQUA));
		} else {
			tt = Component.translatable("item.forcecraft.upgrade_tome.tt.points");
			tt.withStyle(Style.EMPTY.applyFormat(ChatFormatting.AQUA));
			tt.append(" " + bd.getPoints());
			tooltip.add(tt);

			tt = Component.translatable("item.forcecraft.upgrade_tome.tt.nexttier");
			tt.withStyle(Style.EMPTY.applyFormat(ChatFormatting.AQUA));
			tt.append(" " + bd.nextTier());
		}
		tooltip.add(tt);


		if (!Screen.hasShiftDown()) {
			tooltip.add(Component.translatable("forcecraft.tooltip.press_shift"));
			return;
		}

		tooltip.add(Component.translatable("item.forcecraft.upgrade_tome.tt.point_info"));
		tt.withStyle(Style.EMPTY.applyFormat(ChatFormatting.AQUA));
	}

	public static void onModifierApplied(ItemStack bookInSlot, ItemStack modifier, ItemStack tool) {
		UpgradeBookData bd = new UpgradeBookData(bookInSlot);
		bd.incrementPoints(25); // TODO: points per modifier upgrade value !! 
		bd.write(bookInSlot);
	}
}
