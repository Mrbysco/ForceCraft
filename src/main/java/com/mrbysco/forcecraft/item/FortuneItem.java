package com.mrbysco.forcecraft.item;

import com.mrbysco.forcecraft.config.ForceConfig;
import com.mrbysco.forcecraft.registry.ForceComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class FortuneItem extends Item {
	public FortuneItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!stack.has(ForceComponents.FORTUNE)) {
			addMessage(level.getRandom(), stack);
		}
		String message = stack.getOrDefault(ForceComponents.FORTUNE, "");

		if (!level.isClientSide()) {
			if (player != null && player.isShiftKeyDown()) {
				stack.consume(1, player);

				ItemStack paperStack = new ItemStack(Items.PAPER);
				if (!player.addItem(paperStack)) {
					player.spawnAtLocation((ServerLevel) level, paperStack);
				}
			} else {
				player.sendSystemMessage(Component.literal(message));
			}
			return InteractionResult.SUCCESS_SERVER;
		}

		return super.use(level, player, hand);
	}

	public static void addMessage(RandomSource random, ItemStack stack) {
		List<String> messages = new ArrayList<>(ForceConfig.COMMON.fortuneMessages.get());
		String message = "No fortune for you";
		if (!messages.isEmpty()) {
			int idx = random.nextInt(messages.size());
			message = messages.get(idx);
		}

		stack.set(ForceComponents.FORTUNE, message);
	}
}
