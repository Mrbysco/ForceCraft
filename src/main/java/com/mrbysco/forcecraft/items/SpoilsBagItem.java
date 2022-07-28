package com.mrbysco.forcecraft.items;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.menu.SpoilsBagMenu;
import com.mrbysco.forcecraft.registry.ForceTables;
import com.mrbysco.forcecraft.util.ItemHandlerUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpoilsBagItem extends BaseItem {
	private final int tier;

	public SpoilsBagItem(Properties properties, int tier) {
		super(properties.stacksTo(1));
		this.tier = tier;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		ItemStack stack = context.getItemInHand();
		populateBag(level, stack);
		BlockPos pos = context.getClickedPos();
		Direction face = context.getClickedFace();
		BlockEntity tile = level.getBlockEntity(pos);
		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
		if (handler != null && tile != null && tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face).isPresent()) {
			IItemHandler tileInventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face).orElse(null);
			if (tileInventory != null && handler instanceof ItemStackHandler itemHandler) {
				for (int i = 0; i < itemHandler.getSlots(); i++) {
					ItemStack bagStack = itemHandler.getStackInSlot(i);
					ItemStack remaining = ItemHandlerHelper.copyStackWithSize(bagStack, bagStack.getCount());
					if (!bagStack.isEmpty()) {
						remaining = ItemHandlerHelper.insertItem(tileInventory, bagStack, false);
						itemHandler.setStackInSlot(i, remaining);
					}
				}
				if (ItemHandlerUtils.isEmpty(itemHandler)) {
					stack.shrink(1);
				}
				return InteractionResult.SUCCESS;
			}
		}

		return super.useOn(context);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
		if (handler != null) {
			this.populateBag(level, stack);
			playerIn.openMenu(this.getContainer(stack));
			return new InteractionResultHolder<ItemStack>(InteractionResult.PASS, stack);
		}
		return super.use(level, playerIn, handIn);
	}

	public ResourceLocation getTable() {
		return switch (this.tier) {
			default -> ForceTables.TIER_1;
			case 2 -> ForceTables.TIER_2;
			case 3 -> ForceTables.TIER_3;
		};
	}

	public void populateBag(Level level, ItemStack stack) {
		if (!level.isClientSide && !stack.getOrCreateTag().getBoolean("Filled")) {
			IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
			if (handler instanceof ItemStackHandler) {
				if (ItemHandlerUtils.isEmpty(handler)) {
					CompoundTag tag = stack.getOrCreateTag();
					List<ItemStack> stacks = new ArrayList<>();
					do {
						LootContext ctx = new LootContext.Builder((ServerLevel) level).create(LootContextParamSets.EMPTY);
						List<ItemStack> lootStacks = ((ServerLevel) level).getServer().getLootTables()
								.get(getTable()).getRandomItems(ctx);
						if (lootStacks.isEmpty()) {
							return;
						} else {
							Collections.shuffle(lootStacks);
							stacks = lootStacks;
						}
					} while (stacks.isEmpty());

					if (stacks.size() > 7) {
						int newSize = Math.min(8, Math.max(5, level.random.nextInt(stacks.size())));
						if (stacks.size() < newSize) {
							newSize = stacks.size();
						}
						List<ItemStack> newStacks = new ArrayList<>();
						for (int i = 0; i < newSize; i++) {
							newStacks.add(stacks.get(i));
						}
						stacks = newStacks;
					}

					ItemStackHandler stackhandler = (ItemStackHandler) handler;
					for (int i = 0; i < stacks.size(); i++) {
						stackhandler.setStackInSlot(i, stacks.get(i));
					}
					tag.putBoolean("Filled", true);
					stack.setTag(tag);
				}
			}
		}
	}

	@Nullable
	public MenuProvider getContainer(ItemStack stack) {
		return new SimpleMenuProvider((id, inventory, player) -> {
			return new SpoilsBagMenu(id, inventory, stack);
		}, stack.hasCustomHoverName() ? ((MutableComponent) stack.getHoverName()).withStyle(ChatFormatting.BLACK) : Component.translatable(Reference.MOD_ID + ".container.spoils_bag"));
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!level.isClientSide && stack.hasTag() && stack.getTag().getBoolean("Filled")) {
			IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
			if (ItemHandlerUtils.isEmpty(handler)) {
				stack.shrink(1);
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, level, tooltip, flagIn);
		tooltip.add(Component.literal("Tier: " + tier).withStyle(ChatFormatting.GRAY));
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new SpoilsBagItem.InventoryProvider();
	}

	private static class InventoryProvider implements ICapabilitySerializable<CompoundTag> {
		private final LazyOptional<ItemStackHandler> inventory = LazyOptional.of(() -> new ItemStackHandler(8) {
			@Override
			public boolean isItemValid(int slot, ItemStack stack) {
				//No inserting
				return false;
			}

			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
				return stack;
			}
		});

		@Nonnull
		@Override
		public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
			if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
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
