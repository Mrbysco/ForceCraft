package com.mrbysco.forcecraft.items;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.config.ConfigHandler;
import com.mrbysco.forcecraft.util.ItemHandlerUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITagCollectionSupplier;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
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
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		IItemHandler handler = itemstack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
		if(playerIn.isShiftKeyDown()) {
			boolean isFull = ItemHandlerUtils.isFull(handler);
			if(!isFull) {
				//Fill with food
				boolean extracted = ItemHandlerUtils.extractStackFromPlayer(playerIn.inventory, handler, (stack) -> {
					ITagCollectionSupplier tagCollection = TagCollectionManager.getInstance();
					final ResourceLocation baconatorFood = new ResourceLocation(Reference.MOD_ID, "baconator_food");
					Tag<Item> tag = (Tag<Item>) tagCollection.getItems().getTag(baconatorFood);
					return !stack.isEmpty() && stack.isEdible() && tag != null && stack.getItem().is(tag);
				});
				boolean hasItems = ItemHandlerUtils.hasItems(handler);
				if(!extracted) {
					//set to auto-feed mode
					if(hasItems) {
						itemstack.setDamageValue(itemstack.getDamageValue() == 1 ? 0 : 1);
					}
				} else {
					worldIn.playSound((PlayerEntity) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.ITEM_PICKUP, SoundCategory.PLAYERS, 1.0F, 1.0F);
				}
				CompoundNBT tag = itemstack.getOrCreateTag();
				tag.putBoolean(HAS_FOOD_TAG, hasItems);
			}
		} else {
			if(ItemHandlerUtils.hasItems(handler)) {
				ItemStack firstStack = ItemHandlerUtils.getFirstItem(handler);
				if(!firstStack.isEmpty()) {
					if (playerIn.canEat(firstStack.getItem().getFoodProperties().canAlwaysEat())) {
						playerIn.startUsingItem(handIn);
					}
				}
			}
		}
		return ActionResult.pass(itemstack);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
		ItemStack firstStack = ItemHandlerUtils.getFirstItem(handler);
		if(firstStack != null && !firstStack.isEmpty()) {
			entityLiving.eat(worldIn, firstStack);
			worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.PIG_AMBIENT, SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
		return stack;
	}

	@Override
	public UseAction getUseAnimation(ItemStack stack) {
		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
		ItemStack firstStack = ItemHandlerUtils.getFirstItem(handler);
		return firstStack != null && firstStack.getItem().isEdible() ? UseAction.EAT : UseAction.NONE;
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(stack.getDamageValue() == 1 && entityIn instanceof PlayerEntity && worldIn.getGameTime() % 20 == 0) {
			PlayerEntity playerIn = (PlayerEntity)entityIn;
			if(!playerIn.abilities.instabuild && playerIn.canEat(false) && stack.getOrCreateTag().getBoolean(HAS_FOOD_TAG)) {
				IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
				ItemStack firstStack = ItemHandlerUtils.getFirstItem(handler);
				if(!firstStack.isEmpty()) {
					playerIn.eat(worldIn, firstStack);
					worldIn.playSound((PlayerEntity) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.PIG_AMBIENT, SoundCategory.PLAYERS, 1.0F, 1.0F);
				}
			}
		}
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
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
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if(Screen.hasShiftDown()) {
			tooltip.add(new TranslationTextComponent("forcecraft.baconator.shift.carrying").withStyle(TextFormatting.DARK_RED));
			IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
			if(handler != null) {
				int stacks = 0;
				for(int i = 0; i < handler.getSlots(); i++) {
					ItemStack foodStack = handler.getStackInSlot(i);
					if(!foodStack.isEmpty()) {
						tooltip.add(new StringTextComponent(foodStack.getCount() + "x ").append(foodStack.getHoverName()).withStyle(TextFormatting.GOLD));
						stacks++;
					}
				}
				if(stacks == 0) {
					tooltip.add(new TranslationTextComponent("forcecraft.baconator.shift.nothing").withStyle(TextFormatting.GRAY));
				}
			}
		} else {
			tooltip.add(new TranslationTextComponent("forcecraft.baconator.shift.text").withStyle(TextFormatting.GRAY));
		}
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		return new BaconatorItem.InventoryProvider();
	}

	private static class InventoryProvider implements ICapabilitySerializable<CompoundNBT> {
		private final LazyOptional<ItemStackHandler> inventory = LazyOptional.of(() -> new ItemStackHandler(ConfigHandler.COMMON.baconatorMaxStacks.get()) {
			@Override
			public boolean isItemValid(int slot, ItemStack stack) {
				ITagCollectionSupplier tagCollection = TagCollectionManager.getInstance();
				final ResourceLocation baconatorFood = new ResourceLocation(Reference.MOD_ID, "baconator_food");
				Tag<Item> tag = (Tag<Item>) tagCollection.getItems().getTag(baconatorFood);
				return stack.isEdible() && tag != null && stack.getItem().is(tag);
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
