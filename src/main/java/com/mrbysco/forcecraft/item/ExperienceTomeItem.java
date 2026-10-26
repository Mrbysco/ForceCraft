package com.mrbysco.forcecraft.item;

import com.mrbysco.forcecraft.registry.ForceComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.function.Consumer;

public class ExperienceTomeItem extends Item {
	public ExperienceTomeItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (player instanceof FakePlayer || hand != InteractionHand.MAIN_HAND || level.isClientSide()) {
			return InteractionResult.FAIL;
		}
		ItemStack stack = player.getItemInHand(hand);
		int storedExperience = stack.getOrDefault(ForceComponents.TOME_EXPERIENCE, 0);
		int extra = Math.min(
				Math.round(player.experienceProgress * player.getXpNeededForNextLevel()),
				player.getXpNeededForNextLevel() - 1
		);

		int experienceToTransfer;
		if (player.isShiftKeyDown()) {
			int missingForNextLevel = player.getXpNeededForNextLevel() - extra;
			experienceToTransfer = Math.min(missingForNextLevel, storedExperience);
		} else {
			int levelBelow = player.experienceLevel - 1;
			int fullLevelCost = levelBelow >= 30 ? 112 + (levelBelow - 30) * 9
					: levelBelow >= 15 ? 37 + (levelBelow - 15) * 5
					: 7 + levelBelow * 2;

			int depositable = extra > 0 ? extra
					: player.experienceLevel > 0 ? fullLevelCost
					: 0;
			int tomeSpace = Integer.MAX_VALUE - storedExperience;
			experienceToTransfer = -Math.min(depositable, tomeSpace);
		}

		if (experienceToTransfer == 0) {
			return InteractionResult.FAIL;
		}

		player.giveExperiencePoints(experienceToTransfer);
		stack.set(ForceComponents.TOME_EXPERIENCE, storedExperience - experienceToTransfer);
		return InteractionResult.SUCCESS_SERVER;
	}

	@Override
	public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
		builder.accept(Component.translatable("forcecraft.experience_tome.tooltip", itemStack.getOrDefault(ForceComponents.TOME_EXPERIENCE, 0)));
	}
}
