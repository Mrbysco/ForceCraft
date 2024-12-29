package com.mrbysco.forcecraft.items.flask;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.items.BaseItem;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.EffectCures;

import java.util.List;

public class MilkFlaskItem extends BaseItem {

	public MilkFlaskItem(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
		if (!level.isClientSide) entityLiving.removeEffectsCuredBy(EffectCures.MILK);

		if (entityLiving instanceof ServerPlayer serverPlayer) {
			CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
			serverPlayer.awardStat(Stats.ITEM_USED.get(this));
		}

		ItemStack flaskStack = ForceRegistry.FORCE_FLASK.get().getDefaultInstance();
		if (entityLiving instanceof Player playerIn) {
			stack.consume(1, playerIn);

			if (!playerIn.getInventory().add(flaskStack)) {
				playerIn.spawnAtLocation(flaskStack, 0F);
			}
		}

		return stack.isEmpty() ? flaskStack : stack;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity livingEntity) {
		return 32;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.DRINK;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
		return ItemUtils.startUsingInstantly(level, playerIn, handIn);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		super.appendHoverText(stack, context, tooltip, tooltipFlag);
		tooltip.add(Component.translatable("item.milk_force_flask.tooltip").withStyle(ChatFormatting.GRAY));
	}
}
