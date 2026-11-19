package com.mrbysco.forcecraft.items;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.components.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceTags;
import com.mrbysco.forcecraft.util.ItemHandlerUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.List;

public class BaconatorItem extends BaseItem {
	public static final String HAS_FOOD_TAG = Reference.MOD_ID + ":hasItems";

	public BaconatorItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		IItemHandler handler = itemstack.getCapability(Capabilities.ItemHandler.ITEM);
		if (playerIn.isShiftKeyDown()) {
			boolean isFull = ItemHandlerUtils.isFull(handler);
			if (!isFull) {
				//Fill with food
				boolean extracted = ItemHandlerUtils.extractStackFromPlayer(playerIn.getInventory(), handler, (stack) -> {
					return !stack.isEmpty() && stack.has(DataComponents.FOOD) && stack.is(ForceTags.Items.BACONATOR_FOOD);
				});
				boolean hasItems = ItemHandlerUtils.hasItems(handler);
				if (!extracted) {
					//set to auto-feed mode
					if (hasItems) {
						itemstack.setDamageValue(itemstack.getDamageValue() == 1 ? 0 : 1);
					}
				} else {
					level.playSound((Player) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
				}
			}
		} else {
			if (ItemHandlerUtils.hasItems(handler)) {
				ItemStack firstStack = ItemHandlerUtils.getFirstItem(handler);
				if (!firstStack.isEmpty()) {
					FoodProperties foodProperties = firstStack.getFoodProperties(playerIn);
					if (foodProperties != null && playerIn.canEat(foodProperties.canAlwaysEat())) {
						playerIn.startUsingItem(handIn);
					}
				}
			}
		}
		return InteractionResultHolder.pass(itemstack);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
		IItemHandler handler = stack.getCapability(Capabilities.ItemHandler.ITEM);
		ItemStack firstStack = ItemHandlerUtils.getFirstItem(handler);
		if (firstStack != null && !firstStack.isEmpty()) {
			entityLiving.eat(level, firstStack);
			level.playSound((Player) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.PIG_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);
		}
		return stack;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		IItemHandler handler = stack.getCapability(Capabilities.ItemHandler.ITEM);
		ItemStack firstStack = ItemHandlerUtils.getFirstItem(handler);
		return firstStack != null && firstStack.has(DataComponents.FOOD) ? UseAnim.EAT : UseAnim.NONE;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entityIn, int itemSlot, boolean isSelected) {
		if (stack.getDamageValue() == 1 && entityIn instanceof Player playerIn && level.getGameTime() % 20 == 0) {
			if (!playerIn.getAbilities().instabuild && playerIn.canEat(false) && stack.has(ForceComponents.STORED_FOOD)) {
				IItemHandler handler = stack.getCapability(Capabilities.ItemHandler.ITEM);
				ItemStack firstStack = ItemHandlerUtils.getFirstItem(handler);
				if (!firstStack.isEmpty()) {
					playerIn.eat(level, firstStack);
					level.playSound((Player) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.PIG_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);
				}
			}
		}
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity livingEntity) {
		IItemHandler handler = stack.getCapability(Capabilities.ItemHandler.ITEM);
		ItemStack firstStack = ItemHandlerUtils.getFirstItem(handler);
		if (!firstStack.isEmpty() && firstStack.has(DataComponents.FOOD)) {
			FoodProperties foodProperties = firstStack.getFoodProperties(livingEntity);
			return foodProperties.eatDurationTicks();
		} else {
			return 0;
		}
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return stack.getDamageValue() == 1;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, context, tooltip, flagIn);
		if (Screen.hasShiftDown()) {
			tooltip.add(Component.translatable("forcecraft.baconator.shift.carrying").withStyle(ChatFormatting.DARK_RED));
			IItemHandler handler = stack.getCapability(Capabilities.ItemHandler.ITEM);
			if (handler != null) {
				int stacks = 0;
				for (int i = 0; i < handler.getSlots(); i++) {
					ItemStack foodStack = handler.getStackInSlot(i);
					if (!foodStack.isEmpty()) {
						tooltip.add(Component.literal(foodStack.getCount() + "x ").append(foodStack.getHoverName()).withStyle(ChatFormatting.GOLD));
						stacks++;
					}
				}
				if (stacks == 0) {
					tooltip.add(Component.translatable("forcecraft.baconator.shift.nothing").withStyle(ChatFormatting.GRAY));
				}
			}
		} else {
			tooltip.add(Component.translatable("forcecraft.baconator.shift.text").withStyle(ChatFormatting.GRAY));
		}
	}
}
