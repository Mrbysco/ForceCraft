package com.mrbysco.forcecraft.handler;

import com.mrbysco.forcecraft.data.TimeMode;
import com.mrbysco.forcecraft.item.tool.EnderData;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import com.mrbysco.forcecraft.upgrade.UpgradeBookData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;
import java.util.function.Consumer;

@EventBusSubscriber
public class TooltipHandler {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onTooltip(ItemTooltipEvent event) {
		List<Component> tooltips = event.getToolTip();
		ItemStack stack = event.getItemStack();
		addForceTooltips(stack, tooltips::add);
	}

	public static void addForceTooltips(ItemStack stack, Consumer<Component> builder) {
		if (stack.has(ForceComponents.FORCE_STORED)) {
			int storedForce = stack.getOrDefault(ForceComponents.FORCE_STORED, 0);
			if (storedForce > 0)
				builder.accept(Component.translatable("forcecraft.component.force_stored", storedForce).withStyle(ChatFormatting.GOLD));
		}

		if (stack.has(ForceComponents.FORCE)) {
			builder.accept(Component.translatable("forcecraft.component.force" + stack.get(ForceComponents.FORCE)).withStyle(ChatFormatting.GOLD));
		}
		if (stack.has(ForceComponents.DAMAGE)) {
			if (stack.is(ForceTags.Items.FORCE_ARMOR)) {
				builder.accept(Component.translatable("forcecraft.component.damage").withStyle(ChatFormatting.RED));
			} else {
				builder.accept(Component.translatable("forcecraft.component.damage" + stack.get(ForceComponents.DAMAGE)).withStyle(ChatFormatting.RED));
			}
		}
		if (stack.has(ForceComponents.SPEED)) {
			if (!stack.is(ForceRegistry.FORCE_ROD)) {
				builder.accept(Component.translatable("forcecraft.component.speed" + stack.get(ForceComponents.SPEED)).withStyle(ChatFormatting.YELLOW));
			}
		}
		if (stack.has(ForceComponents.LUMBERJACK)) {
			builder.accept(Component.translatable("forcecraft.component.lumberjack").withStyle(ChatFormatting.YELLOW));
		}
		if (stack.has(ForceComponents.RAINBOW)) {
			builder.accept(Component.translatable("forcecraft.component.rainbow").withStyle(ChatFormatting.GOLD));
		}
		if (stack.has(ForceComponents.LUCK)) {
			builder.accept(Component.translatable("forcecraft.component.luck" + stack.get(ForceComponents.LUCK)).withStyle(ChatFormatting.GREEN));
		}
//		if (stack.has(ForceComponents.GRINDING)) {
//			builder.accept(Component.translatable("forcecraft.component.grinding").withStyle(ChatFormatting.DARK_AQUA));
//		}
		if (stack.has(ForceComponents.FREEZING)) {
			builder.accept(Component.translatable("forcecraft.component.freezing").withStyle(ChatFormatting.DARK_BLUE));
		}
		if (stack.has(ForceComponents.FALSE)) {
			builder.accept(Component.translatable("forcecraft.component.false").withStyle(ChatFormatting.WHITE));
		}
		if (stack.has(ForceComponents.TOUCH)) {
			builder.accept(Component.translatable("forcecraft.component.touch").withStyle(ChatFormatting.AQUA));
		}
		if (stack.has(ForceComponents.BLEED)) {
			builder.accept(Component.translatable("forcecraft.component.bleed" + stack.get(ForceComponents.BLEED)).withStyle(ChatFormatting.RED));
		}
		if (stack.has(ForceComponents.CRAFT)) {
			builder.accept(Component.translatable("forcecraft.component.craft").withStyle(ChatFormatting.YELLOW));
		}
		if (stack.has(ForceComponents.TIME)) {
			if (stack.is(ForceRegistry.FORCE_ROD)) {
				TimeMode mode = stack.getOrDefault(ForceComponents.TIME_MODE, TimeMode.STOP);
				builder.accept(Component.translatable("forcecraft.component.time_mode", mode.getName()).withStyle(ChatFormatting.GRAY));
			} else {
				builder.accept(Component.translatable("forcecraft.component.time").withStyle(ChatFormatting.RED));
			}
		}
		if (stack.has(ForceComponents.HEAT)) {
			if (!stack.is(ForceRegistry.FORCE_ROD)) {
				builder.accept(Component.translatable("forcecraft.component.heat").withStyle(ChatFormatting.RED));
			}
		}
		if (stack.has(ForceComponents.BANE)) {
			builder.accept(Component.translatable("forcecraft.component.bane").withStyle(ChatFormatting.RED));
		}
		if (stack.has(ForceComponents.FORGE)) {
			builder.accept(Component.translatable("forcecraft.component.forge").withStyle(ChatFormatting.GOLD));
		}
		if (stack.has(ForceComponents.WING)) {
			builder.accept(Component.literal("NOT IMPLEMENTED YET"));
			builder.accept(Component.translatable("forcecraft.component.wing"));
		}
		if (stack.has(ForceComponents.HEALING)) {
			if (!stack.is(ForceRegistry.FORCE_ROD)) {
				builder.accept(Component.translatable("forcecraft.component.healing" + stack.get(ForceComponents.HEALING)).withStyle(ChatFormatting.GREEN));
			}
		}
		if (stack.has(ForceComponents.CAMO)) {
			if (!stack.is(ForceRegistry.FORCE_ROD)) {
				builder.accept(Component.translatable("forcecraft.component.camo").withStyle(ChatFormatting.DARK_GREEN));
			}
		}
		if (stack.has(ForceComponents.SIGHT)) {
			if (!stack.is(ForceRegistry.FORCE_ROD)) {
				builder.accept(Component.translatable("forcecraft.component.sight").withStyle(ChatFormatting.DARK_AQUA));
			}
		}
		if (stack.has(ForceComponents.STURDY)) {
			builder.accept(Component.translatable("forcecraft.component.sturdy" + stack.get(ForceComponents.STURDY)).withStyle(ChatFormatting.DARK_PURPLE));
		}
		if (stack.has(ForceComponents.ENDER)) {
			if (!stack.is(ForceRegistry.FORCE_ROD)) {
				builder.accept(Component.translatable("forcecraft.component.ender").withStyle(ChatFormatting.DARK_PURPLE));
			} else {
				if (stack.has(ForceComponents.ENDER_DATA)) {
					EnderData enderData = stack.get(ForceComponents.ENDER_DATA);
					builder.accept(Component.literal(enderData.name()).withStyle(ChatFormatting.GRAY));
				}
			}
		}
		if (stack.has(ForceComponents.SOUL)) {
			builder.accept(Component.translatable("forcecraft.component.soul").withStyle(ChatFormatting.DARK_PURPLE));
		}
		if (stack.has(ForceComponents.LIGHT)) {
			builder.accept(Component.translatable("forcecraft.component.light" + stack.get(ForceComponents.LIGHT)).withStyle(ChatFormatting.GOLD));
		}
		if (stack.has(ForceComponents.TREASURE)) {
			builder.accept(Component.translatable("forcecraft.component.treasure").withStyle(ChatFormatting.GOLD));
		}

		if (stack.has(ForceComponents.UPGRADE_BOOK)) {
			UpgradeBookData bookData = stack.getOrDefault(ForceComponents.UPGRADE_BOOK, UpgradeBookData.DEFAULT);

			builder.accept(Component.translatable("forcecraft.component.upgrade_book.tier", bookData.tier().asInt())
					.withStyle(ChatFormatting.AQUA));
			builder.accept(Component.translatable("forcecraft.component.upgrade_book.points",
							Component.literal(String.valueOf(bookData.points())).withStyle(ChatFormatting.WHITE))
					.withStyle(ChatFormatting.AQUA));
			if (bookData.tier().isFinal()) {
				builder.accept(Component.translatable("forcecraft.component.upgrade_book.max_tier")
						.withStyle(ChatFormatting.GOLD));
			} else {
				builder.accept(Component.translatable("forcecraft.component.upgrade_book.next_tier",
								Component.literal(String.valueOf(bookData.pointsToNextTier())).withStyle(ChatFormatting.WHITE))
						.withStyle(ChatFormatting.AQUA));
			}
		}
	}
}
