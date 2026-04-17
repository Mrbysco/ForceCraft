package com.mrbysco.forcecraft.items;

import com.mojang.datafixers.util.Pair;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.components.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceTags;
import com.mrbysco.forcecraft.util.ItemHandlerUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.pig.PigSoundVariants;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class BaconatorItem extends BaseItem {
	public static final String HAS_FOOD_TAG = Reference.MOD_ID + ":hasItems";

	public BaconatorItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player playerIn, InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		ResourceHandler<ItemResource> handler = itemstack.getCapability(Capabilities.Item.ITEM, ItemAccess.forStack(itemstack));
		if (playerIn.isShiftKeyDown()) {
			boolean isFull = ItemHandlerUtils.isFull(handler);
			if (!isFull) {
				//Fill with food
				boolean extracted = ItemHandlerUtils.extractStackFromPlayer(playerIn.getInventory(), handler, (stack) -> {
					return !stack.isEmpty() && stack.has(DataComponents.FOOD) && stack.is(ForceTags.BACONATOR_FOOD);
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
				Pair<Integer, ItemResource> firstItem = this.getFirstItem(itemstack);
				if (firstItem != null && firstItem.getSecond().has(DataComponents.FOOD)) {
					ItemResource resource = firstItem.getSecond();
					FoodProperties foodProperties = resource.get(DataComponents.FOOD);
					if (foodProperties != null && playerIn.canEat(foodProperties.canAlwaysEat())) {
						playerIn.startUsingItem(handIn);
					}
				}
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
		consumeItem(stack, livingEntity);
		return stack;
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		Pair<Integer, ItemResource> firstItem = this.getFirstItem(stack);
		return firstItem != null && firstItem.getSecond().has(DataComponents.FOOD) ? ItemUseAnimation.EAT : ItemUseAnimation.NONE;
	}

	public Pair<Integer, ItemResource> getFirstItem(ItemStack stack) {
		ResourceHandler<ItemResource> handler = stack.getCapability(Capabilities.Item.ITEM, ItemAccess.forStack(stack));
		for (int i = 0; i < handler.size(); i++) {
			ItemResource resource = handler.getResource(i);
			if (!resource.isEmpty()) {
				return new Pair<>(i, resource);
			}
		}
		return null;
	}

	@Override
	public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
		if (itemStack.getDamageValue() == 1 && owner instanceof Player playerIn && level.getGameTime() % 20 == 0) {
			if (!playerIn.getAbilities().instabuild && playerIn.canEat(false) && itemStack.has(ForceComponents.STORED_FOOD)) {
				consumeItem(itemStack, playerIn);
			}
		}
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity livingEntity) {
		Pair<Integer, ItemResource> firstItem = this.getFirstItem(stack);
		if (firstItem != null && firstItem.getSecond().has(DataComponents.CONSUMABLE)) {
			Consumable consumable = firstItem.getSecond().get(DataComponents.CONSUMABLE);
			return consumable.consumeTicks();
		} else {
			return 0;
		}
	}

	private boolean consumeItem(ItemStack stack, LivingEntity livingEntity) {
		ResourceHandler<ItemResource> handler = stack.getCapability(Capabilities.Item.ITEM, ItemAccess.forStack(stack));
		Pair<Integer, ItemResource> firstItem = this.getFirstItem(stack);

		if (firstItem != null) {
			int i = firstItem.getFirst();
			ItemResource resource = firstItem.getSecond();
			ItemStack foodItemStack = resource.toStack(handler.getAmountAsInt(i));
			Consumable consumable = foodItemStack.get(DataComponents.CONSUMABLE);
			if (consumable.canConsume(livingEntity, foodItemStack)) {
				try (Transaction tx = Transaction.openRoot()) {
					if (handler.extract(handler.getResource(i), handler.getAmountAsInt(i), tx) == handler.getAmountAsInt(i)) {
						ItemStack returnStack = consumable.onConsume(livingEntity.level(), livingEntity, foodItemStack);
						livingEntity.level().playSound((Player) null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
								SoundEvents.PIG_SOUNDS.get(PigSoundVariants.SoundSet.CLASSIC).adultSounds().ambientSound(),
								SoundSource.PLAYERS, 1.0F, 1.0F);
						if (handler.insert(i, ItemResource.of(returnStack), returnStack.getCount(), tx) == returnStack.getCount()) {
							tx.commit();
						}
					}
				}
			}
		}

		return false;
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return stack.getDamageValue() == 1;
	}

	@Override
	public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
		if (tooltipFlag.hasShiftDown()) {
			builder.accept(Component.translatable("forcecraft.baconator.shift.carrying").withStyle(ChatFormatting.DARK_RED));
			ResourceHandler<ItemResource> handler = itemStack.getCapability(Capabilities.Item.ITEM, ItemAccess.forStack(itemStack));
			if (handler != null) {
				int stacks = 0;
				for (int i = 0; i < handler.size(); i++) {
					ItemStack foodStack = handler.getResource(i).toStack(handler.getAmountAsInt(i));
					if (!foodStack.isEmpty()) {
						builder.accept(Component.literal(foodStack.getCount() + "x ").append(foodStack.getHoverName()).withStyle(ChatFormatting.GOLD));
						stacks++;
					}
				}
				if (stacks == 0) {
					builder.accept(Component.translatable("forcecraft.baconator.shift.nothing").withStyle(ChatFormatting.GRAY));
				}
			}
		} else {
			builder.accept(Component.translatable("forcecraft.baconator.shift.text").withStyle(ChatFormatting.GRAY));
		}
	}
}
