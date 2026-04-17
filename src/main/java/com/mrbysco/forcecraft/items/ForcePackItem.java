package com.mrbysco.forcecraft.items;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.components.ForceComponents;
import com.mrbysco.forcecraft.components.storage.PackStackHandler;
import com.mrbysco.forcecraft.components.storage.PackStorage;
import com.mrbysco.forcecraft.components.storage.StorageManager;
import com.mrbysco.forcecraft.menu.ForcePackMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class ForcePackItem extends BaseItem {

	public ForcePackItem(Item.Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	@NotNull
	public InteractionResult use(@NotNull Level level, Player playerIn, @NotNull InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);

		if (playerIn.isShiftKeyDown()) {
			if (level.isClientSide()) {
				com.mrbysco.forcecraft.client.gui.pack.RenameAndRecolorScreen.openScreen(stack, handIn);
			}
		} else {
			if (level.isClientSide()) {
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
				stack.has(DataComponents.CUSTOM_NAME) ? ((MutableComponent) stack.getHoverName()).withStyle(ChatFormatting.BLACK) :
						Component.translatable(Reference.MOD_ID + ".container.pack"));
	}

	@Override
	public boolean shouldCauseReequipAnimation(@NotNull ItemStack oldStack, @NotNull ItemStack newStack, boolean slotChanged) {
		return false;
	}

	@Override
	public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
		if (itemStack.has(ForceComponents.SLOTS_USED) && itemStack.has(ForceComponents.SLOTS_TOTAL)) {
			builder.accept(Component.literal(String.format("%s/%s Slots",
					itemStack.getOrDefault(ForceComponents.SLOTS_USED, 0),
					itemStack.getOrDefault(ForceComponents.SLOTS_TOTAL, 1))));
		} else {
			builder.accept(Component.literal("0/8 Slots"));
		}

		if (tooltipFlag.isAdvanced() && itemStack.has(ForceComponents.UUID)) {
			UUID uuid = itemStack.get(ForceComponents.UUID);
			builder.accept(Component.literal("ID: " + uuid.toString().substring(0, 8))
					.withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
		}
	}

	@Override
	@NotNull
	public Component getName(@NotNull ItemStack stack) {
		return ((MutableComponent) super.getName(stack)).withStyle(ChatFormatting.YELLOW);
	}
}
