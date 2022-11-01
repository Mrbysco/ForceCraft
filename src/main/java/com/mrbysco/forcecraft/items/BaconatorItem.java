package com.mrbysco.forcecraft.items;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.config.ConfigHandler;
import com.mrbysco.forcecraft.registry.ForceTags;
import com.mrbysco.forcecraft.util.ItemHandlerUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BaconatorItem extends BaseItem {
	public static final String HAS_FOOD_TAG = Reference.MOD_ID + ":hasItems";

	public BaconatorItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		IItemHandler handler = itemstack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
		if (playerIn.isShiftKeyDown()) {
			boolean isFull = ItemHandlerUtils.isFull(handler);
			if (!isFull) {
				//Fill with food
				boolean extracted = ItemHandlerUtils.extractStackFromPlayer(playerIn.getInventory(), handler, (stack) -> {
					return !stack.isEmpty() && stack.isEdible() && stack.is(ForceTags.BACONATOR_FOOD);
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
				CompoundTag tag = itemstack.getOrCreateTag();
				tag.putBoolean(HAS_FOOD_TAG, hasItems);
			}
		} else {
			if (ItemHandlerUtils.hasItems(handler)) {
				ItemStack firstStack = ItemHandlerUtils.getFirstItem(handler);
				if (!firstStack.isEmpty()) {
					if (playerIn.canEat(firstStack.getItem().getFoodProperties().canAlwaysEat())) {
						playerIn.startUsingItem(handIn);
					}
				}
			}
		}
		return InteractionResultHolder.pass(itemstack);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
		IItemHandler handler = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
		ItemStack firstStack = ItemHandlerUtils.getFirstItem(handler);
		if (firstStack != null && !firstStack.isEmpty()) {
			entityLiving.eat(level, firstStack);
			level.playSound((Player) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.PIG_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);
		}
		return stack;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		IItemHandler handler = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
		ItemStack firstStack = ItemHandlerUtils.getFirstItem(handler);
		return firstStack != null && firstStack.getItem().isEdible() ? UseAnim.EAT : UseAnim.NONE;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entityIn, int itemSlot, boolean isSelected) {
		if (stack.getDamageValue() == 1 && entityIn instanceof Player playerIn && level.getGameTime() % 20 == 0) {
			if (!playerIn.getAbilities().instabuild && playerIn.canEat(false) && stack.getOrCreateTag().getBoolean(HAS_FOOD_TAG)) {
				IItemHandler handler = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
				ItemStack firstStack = ItemHandlerUtils.getFirstItem(handler);
				if (!firstStack.isEmpty()) {
					playerIn.eat(level, firstStack);
					level.playSound((Player) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.PIG_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);
				}
			}
		}
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		IItemHandler handler = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
		ItemStack firstStack = ItemHandlerUtils.getFirstItem(handler);
		if (!firstStack.isEmpty() && firstStack.getItem().isEdible()) {
			return firstStack.getItem().getFoodProperties().isFastFood() ? 16 : 32;
		} else {
			return 0;
		}
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return stack.getDamageValue() == 1;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			tooltip.add(Component.translatable("forcecraft.baconator.shift.carrying").withStyle(ChatFormatting.DARK_RED));
			IItemHandler handler = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
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
		super.appendHoverText(stack, level, tooltip, flagIn);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new BaconatorItem.InventoryProvider();
	}

	private static class InventoryProvider implements ICapabilitySerializable<CompoundTag> {
		private final LazyOptional<ItemStackHandler> inventory = LazyOptional.of(() -> new ItemStackHandler(ConfigHandler.COMMON.baconatorMaxStacks.get()) {
			@Override
			public boolean isItemValid(int slot, ItemStack stack) {
				return stack.isEdible() && stack.is(ForceTags.BACONATOR_FOOD);
			}
		});

		@Nonnull
		@Override
		public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
			if (cap == ForgeCapabilities.ITEM_HANDLER)
				return inventory.cast();
			else return LazyOptional.empty();
		}

		@Override
		public CompoundTag serializeNBT() {
			if (inventory.isPresent()) {
				return inventory.resolve().get().serializeNBT();
			}
			return new CompoundTag();
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			inventory.ifPresent(h -> h.deserializeNBT(nbt));
		}
	}
}
