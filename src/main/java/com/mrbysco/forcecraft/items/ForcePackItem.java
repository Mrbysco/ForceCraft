package com.mrbysco.forcecraft.items;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.capabilities.pack.PackInventoryProvider;
import com.mrbysco.forcecraft.capabilities.pack.PackItemStackHandler;
import com.mrbysco.forcecraft.menu.ForcePackMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class ForcePackItem extends BaseItem {

	public static final String SLOTS_TOTAL = "SlotsTotal";
	public static final String SLOTS_USED = "SlotsUsed";

	public ForcePackItem(Item.Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);

		if (playerIn.isShiftKeyDown()) {
			if (level.isClientSide) {
				com.mrbysco.forcecraft.client.gui.pack.RenameAndRecolorScreen.openScreen(stack, handIn);
			}
		} else {
			if (!level.isClientSide) {
				NetworkHooks.openGui((ServerPlayer) playerIn, getContainer(stack), playerIn.blockPosition());
			}
		}
		// If it doesn't nothing bad happens
		return super.use(level, playerIn, handIn);
	}

	@Nullable
	public MenuProvider getContainer(ItemStack stack) {
		return new SimpleMenuProvider((id, inventory, player) -> {
			return new ForcePackMenu(id, inventory);
		}, stack.hasCustomHoverName() ? ((BaseComponent) stack.getHoverName()).withStyle(ChatFormatting.BLACK) : new TranslatableComponent(Reference.MOD_ID + ".container.pack"));
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
		int defaultAmount = 8;
		CompoundTag tag = stack.getOrCreateTag();
		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
		if (handler instanceof PackItemStackHandler) {
			defaultAmount = ((PackItemStackHandler) handler).getSlotsInUse();
		}
		tooltip.add(new TextComponent(String.format("%s/%s Slots", tag.getInt(SLOTS_USED), defaultAmount)));

		super.appendHoverText(stack, level, tooltip, flagIn);
	}

	@Override
	public Component getName(ItemStack stack) {
		return ((BaseComponent) super.getName(stack)).withStyle(ChatFormatting.YELLOW);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new PackInventoryProvider();
	}

	// ShareTag for server->client capability data sync
	@Override
	public CompoundTag getShareTag(ItemStack stack) {
		CompoundTag shareTag = stack.getOrCreateTag();
		// no capability, use all of it

		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
		if (handler instanceof PackItemStackHandler packHandler) {
			shareTag.putInt(PackItemStackHandler.NBT_UPGRADES, packHandler.getUpgrades());
		}

		return shareTag;
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
		if (nbt != null && nbt.contains(PackItemStackHandler.NBT_UPGRADES)) {
			stack.getOrCreateTag().putInt(SLOTS_USED, nbt.getInt(SLOTS_USED));

			IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
			if (handler instanceof PackItemStackHandler packHandler) {
				packHandler.setUpgrades(nbt.getInt(PackItemStackHandler.NBT_UPGRADES));

			}
		}
		super.readShareTag(stack, nbt);
	}
}
