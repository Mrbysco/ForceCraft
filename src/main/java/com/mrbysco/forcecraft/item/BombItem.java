package com.mrbysco.forcecraft.item;

import com.mrbysco.forcecraft.entity.Bomb;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class BombItem extends Item {
	private final boolean superBomb;

	public BombItem(Properties properties, boolean superBomb) {
		super(properties.rarity(superBomb ? Rarity.UNCOMMON : Rarity.COMMON));
		this.superBomb = superBomb;
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack itemStack = player.getItemInHand(hand);
		if (level instanceof ServerLevel serverLevel) {
			BlockPos pos = player.blockPosition();
			Bomb bomb = new Bomb(level, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, player);
			bomb.setSuper(superBomb);
			serverLevel.addFreshEntity(bomb);
		}

		player.awardStat(Stats.ITEM_USED.get(this));
		itemStack.consume(1, player);
		return InteractionResult.SUCCESS;
	}

	@Override
	public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
		super.appendHoverText(itemStack, context, display, builder, tooltipFlag);

		if (superBomb) {
			builder.accept(Component.translatable("forcecraft.serious_bomb.tooltip"));
		}
	}
}
