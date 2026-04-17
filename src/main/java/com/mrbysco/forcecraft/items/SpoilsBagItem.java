package com.mrbysco.forcecraft.items;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.components.ForceComponents;
import com.mrbysco.forcecraft.menu.SpoilsBagMenu;
import com.mrbysco.forcecraft.registry.ForceTables;
import com.mrbysco.forcecraft.util.ItemHandlerUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
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
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

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
		IItemHandler blockInventory = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, face);
		IItemHandler handler = stack.getCapability(Capabilities.ItemHandler.ITEM);
		if (handler instanceof ComponentItemHandler itemHandler && blockInventory != null) {
			for (int i = 0; i < itemHandler.getSlots(); i++) {
				ItemStack bagStack = itemHandler.getStackInSlot(i);
				ItemStack remaining;
				if (!bagStack.isEmpty()) {
					remaining = ItemHandlerHelper.insertItem(blockInventory, bagStack, false);
					itemHandler.setStackInSlot(i, remaining);
				}
			}
			if (ItemHandlerUtils.isEmpty(itemHandler)) {
				stack.shrink(1);
			}
			return InteractionResult.SUCCESS;
		}

		return super.useOn(context);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		IItemHandler handler = stack.getCapability(Capabilities.ItemHandler.ITEM);
		if (handler != null) {
			this.populateBag(level, stack);
			playerIn.openMenu(this.getContainer(stack));
			return new InteractionResultHolder<ItemStack>(InteractionResult.PASS, stack);
		}
		return super.use(level, playerIn, handIn);
	}

	public ResourceKey<LootTable> getTable() {
		return switch (this.tier) {
			case 2 -> ForceTables.TIER_2;
			case 3 -> ForceTables.TIER_3;
			default -> ForceTables.TIER_1;
		};
	}

	public void populateBag(Level level, ItemStack stack) {
		if (!level.isClientSide() && !stack.has(ForceComponents.SPOILS_FILLED)) {
			IItemHandler handler = stack.getCapability(Capabilities.ItemHandler.ITEM);
			if (handler instanceof ComponentItemHandler componentItemHandler) {
				if (ItemHandlerUtils.isEmpty(handler)) {
					List<ItemStack> stacks = new ArrayList<>();
					do {
						LootTable table = level.getServer().reloadableRegistries().getLootTable(getTable());

						LootParams.Builder lootParams = (new LootParams.Builder((ServerLevel) level));
						List<ItemStack> lootStacks = table.getRandomItems(lootParams.create(LootContextParamSets.EMPTY));
						if (lootStacks.isEmpty()) {
							return;
						} else {
							Collections.shuffle(lootStacks);
							stacks = lootStacks;
						}
					} while (stacks.isEmpty());

					if (stacks.size() > 7) {
						int newSize = Math.min(8, Math.max(5, level.getRandom().nextInt(stacks.size())));
						if (stacks.size() < newSize) {
							newSize = stacks.size();
						}
						List<ItemStack> newStacks = new ArrayList<>();
						for (int i = 0; i < newSize; i++) {
							newStacks.add(stacks.get(i));
						}
						stacks = newStacks;
					}

					for (int i = 0; i < stacks.size(); i++) {
						componentItemHandler.setStackInSlot(i, stacks.get(i));
					}
					stack.set(ForceComponents.SPOILS_FILLED, true);
				}
			}
		}
	}

	@Nullable
	public MenuProvider getContainer(ItemStack stack) {
		return new SimpleMenuProvider((id, inventory, player) -> {
			return new SpoilsBagMenu(id, inventory, stack);
		}, stack.has(DataComponents.CUSTOM_NAME) ?
				((MutableComponent) stack.getHoverName()).withStyle(ChatFormatting.BLACK) :
				Component.translatable(Reference.MOD_ID + ".container.spoils_bag"));
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!level.isClientSide() && stack.has(ForceComponents.SPOILS_FILLED)) {
			IItemHandler handler = stack.getCapability(Capabilities.ItemHandler.ITEM);
			if (ItemHandlerUtils.isEmpty(handler)) {
				stack.shrink(1);
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		super.appendHoverText(stack, context, tooltip, tooltipFlag);
		builder.accept(Component.literal("Tier: " + tier).withStyle(ChatFormatting.GRAY));
	}
}
