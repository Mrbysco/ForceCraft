package com.mrbysco.forcecraft.items;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.capablilities.pack.PackInventoryProvider;
import com.mrbysco.forcecraft.capablilities.pack.PackItemStackHandler;
import com.mrbysco.forcecraft.container.ForcePackContainer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public class ForcePackItem extends BaseItem {

	public static final String SLOTS_TOTAL = "SlotsTotal";
	public static final String SLOTS_USED = "SlotsUsed";

	public ForcePackItem(Item.Properties properties) {
		super(properties.maxStackSize(1));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (playerIn.isSneaking()) {
			if (worldIn.isRemote) {
				com.mrbysco.forcecraft.client.gui.pack.RenameAndRecolorScreen.openScreen(stack, handIn);
			}
		} else {
			if (!worldIn.isRemote) {
				NetworkHooks.openGui((ServerPlayerEntity) playerIn, getContainer(stack), playerIn.getPosition());
			}
		}
		// If it doesn't nothing bad happens
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Nullable
	public INamedContainerProvider getContainer(ItemStack stack) {
		return new SimpleNamedContainerProvider((id, inventory, player) -> {
			return new ForcePackContainer(id, inventory);
		}, stack.hasDisplayName() ? ((TextComponent) stack.getDisplayName()).mergeStyle(TextFormatting.BLACK) : new TranslationTextComponent(Reference.MOD_ID + ".container.pack"));
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return false;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		int defaultAmount = 8;
		CompoundNBT tag = stack.getOrCreateTag();
		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
		if (handler instanceof PackItemStackHandler) {
			defaultAmount = ((PackItemStackHandler) handler).getSlotsInUse();
		}
		tooltip.add(new StringTextComponent(String.format("%s/%s Slots", tag.getInt(SLOTS_USED), defaultAmount)));

		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public ITextComponent getDisplayName(ItemStack stack) {
		return ((TextComponent) super.getDisplayName(stack)).mergeStyle(TextFormatting.YELLOW);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		return new PackInventoryProvider();
	}

	// ShareTag for server->client capability data sync
	@Override
	public CompoundNBT getShareTag(ItemStack stack) {
		CompoundNBT shareTag = stack.getOrCreateTag();
		// no capability, use all of it

		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
		if (handler instanceof PackItemStackHandler) {
			PackItemStackHandler packHandler = (PackItemStackHandler) handler;
			shareTag.putInt(PackItemStackHandler.NBT_UPGRADES, packHandler.getUpgrades());
		}

		return shareTag;
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
		if (nbt != null && nbt.contains(PackItemStackHandler.NBT_UPGRADES)) {
			stack.getOrCreateTag().putInt(SLOTS_USED, nbt.getInt(SLOTS_USED));

			IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
			if (handler instanceof PackItemStackHandler) {
				PackItemStackHandler packHandler = (PackItemStackHandler) handler;
				packHandler.setUpgrades(nbt.getInt(PackItemStackHandler.NBT_UPGRADES));

			}
		}
		super.readShareTag(stack, nbt);
	}
}
