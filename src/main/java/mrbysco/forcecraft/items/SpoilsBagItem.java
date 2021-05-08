package mrbysco.forcecraft.items;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.container.SpoilsBagContainer;
import mrbysco.forcecraft.registry.ForceTables;
import mrbysco.forcecraft.util.ItemHandlerUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
		super(properties.maxStackSize(1));
		this.tier = tier;
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World worldIn = context.getWorld();
		ItemStack stack = context.getItem();
		populateBag(worldIn, stack);
		BlockPos pos = context.getPos();
		Direction face = context.getFace();
		TileEntity tile = worldIn.getTileEntity(pos);
		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
		if (handler != null && tile != null && tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face).isPresent()) {
			IItemHandler tileInventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face).orElse(null);
			if(tileInventory != null && handler instanceof ItemStackHandler) {
				ItemStackHandler itemHandler = (ItemStackHandler) handler;
				for(int i = 0; i < itemHandler.getSlots(); i++) {
					ItemStack bagStack = itemHandler.getStackInSlot(i);
					ItemStack remaining = ItemHandlerHelper.copyStackWithSize(bagStack, bagStack.getCount());
					if(!bagStack.isEmpty()) {
						remaining = ItemHandlerHelper.insertItem(tileInventory, bagStack, false);
						itemHandler.setStackInSlot(i, remaining);
					}
				}
				if(ItemHandlerUtils.isEmpty(itemHandler)) {
					stack.shrink(1);
				}
				return ActionResultType.SUCCESS;
			}
		}

		return super.onItemUse(context);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
		if(handler != null) {
			this.populateBag(worldIn, stack);
			playerIn.openContainer(this.getContainer(stack));
			return new ActionResult<ItemStack>(ActionResultType.PASS, stack);
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	public ResourceLocation getTable() {
		switch (this.tier) {
			default:
				return ForceTables.TIER_1;
			case 2:
				return ForceTables.TIER_2;
			case 3:
				return ForceTables.TIER_3;
		}
	}

	public void populateBag(World worldIn, ItemStack stack) {
		if(!worldIn.isRemote && !stack.getOrCreateTag().getBoolean("Filled")) {
			IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
			if(handler instanceof ItemStackHandler) {
				if(ItemHandlerUtils.isEmpty(handler)) {
					CompoundNBT tag = stack.getOrCreateTag();
					List<ItemStack> stacks = new ArrayList<>();
					do {
						LootContext ctx = new LootContext.Builder((ServerWorld) worldIn).build(LootParameterSets.EMPTY);
						List<ItemStack> lootStacks = ((ServerWorld) worldIn).getServer().getLootTableManager()
								.getLootTableFromLocation(getTable()).generate(ctx);
						if (lootStacks.isEmpty()) {
							return;
						} else {
							Collections.shuffle(lootStacks);
							stacks = lootStacks;
						}
					} while (stacks.isEmpty() );

					if(stacks.size() > 7) {
						int newSize = Math.min(8, Math.max(5, worldIn.rand.nextInt(stacks.size())));
						if(stacks.size() < newSize) {
							newSize = stacks.size();
						}
						List<ItemStack> newStacks = new ArrayList<>();
						for(int i = 0; i < newSize; i++) {
							newStacks.add(stacks.get(i));
						}
						stacks = newStacks;
					}

					ItemStackHandler stackhandler = (ItemStackHandler)handler;
					for(int i = 0; i < stacks.size(); i++) {
						stackhandler.setStackInSlot(i, stacks.get(i));
					}
					tag.putBoolean("Filled", true);
					stack.setTag(tag);
				}
			}
		}
	}

	@Nullable
	public INamedContainerProvider getContainer(ItemStack stack) {
		return new SimpleNamedContainerProvider((id, inventory, player) -> {
			return new SpoilsBagContainer(id, inventory, stack);
		}, stack.hasDisplayName() ? ((TextComponent)stack.getDisplayName()).mergeStyle(TextFormatting.BLACK) : new TranslationTextComponent(Reference.MOD_ID + ".container.spoils_bag"));
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(!worldIn.isRemote && stack.getOrCreateTag().getBoolean("Filled")) {
			IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
			if(ItemHandlerUtils.isEmpty(handler)) {
				stack.shrink(1);
			}
		}
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new StringTextComponent("Tier: " + tier).mergeStyle(TextFormatting.GRAY));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		return new SpoilsBagItem.InventoryProvider();
	}

	private static class InventoryProvider implements ICapabilitySerializable<CompoundNBT> {
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
		public CompoundNBT serializeNBT() {
			if (inventory.isPresent()) {
				return inventory.resolve().get().serializeNBT();
			}
			return new CompoundNBT();
		}

		@Override
		public void deserializeNBT(CompoundNBT nbt) {
			inventory.ifPresent(h -> h.deserializeNBT(nbt));
		}
	}
}
