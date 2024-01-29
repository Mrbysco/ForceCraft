package com.mrbysco.forcecraft.items;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.attachment.storage.PackStackHandler;
import com.mrbysco.forcecraft.attachment.storage.PackStorage;
import com.mrbysco.forcecraft.attachment.storage.StorageManager;
import com.mrbysco.forcecraft.menu.ForcePackMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
				PackStorage data = StorageManager.getOrCreatePack(stack);
				playerIn.openMenu(Objects.requireNonNull(getContainer(stack, data.getInventory())), buf -> buf.writeInt(data.getInventory().getUpgrades()));
			}
		}
		// If it doesn't, nothing bad happens
		return super.use(level, playerIn, handIn);
	}

	@Nullable
	public MenuProvider getContainer(ItemStack stack, PackStackHandler handler) {
		return new SimpleMenuProvider((id, playerInv, player) -> new ForcePackMenu(id, playerInv, handler),
				stack.hasCustomHoverName() ? ((MutableComponent) stack.getHoverName()).withStyle(ChatFormatting.BLACK) :
						Component.translatable(Reference.MOD_ID + ".container.pack"));
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains(ForcePackItem.SLOTS_USED) && tag.contains(ForcePackItem.SLOTS_TOTAL)) {
			tooltip.add(Component.literal(String.format("%s/%s Slots", tag.getInt(ForcePackItem.SLOTS_USED), tag.getInt(ForcePackItem.SLOTS_TOTAL))));
		} else {
			tooltip.add(Component.literal("0/8 Slots"));
		}


		if (flagIn.isAdvanced() && stack.getTag() != null && stack.getTag().contains("uuid")) {
			UUID uuid = stack.getTag().getUUID("uuid");
			tooltip.add(Component.literal("ID: " + uuid.toString().substring(0, 8)).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
		}

		super.appendHoverText(stack, level, tooltip, flagIn);
	}

	@Override
	public Component getName(ItemStack stack) {
		return ((MutableComponent) super.getName(stack)).withStyle(ChatFormatting.YELLOW);
	}
}
