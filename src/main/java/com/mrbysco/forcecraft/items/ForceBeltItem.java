package com.mrbysco.forcecraft.items;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.menu.ForceBeltMenu;
import com.mrbysco.forcecraft.registry.ForceTags;
import com.mrbysco.forcecraft.storage.BeltStorage;
import com.mrbysco.forcecraft.storage.StorageManager;
import com.mrbysco.forcecraft.storage.WSDCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ForceBeltItem extends BaseItem {

	public ForceBeltItem(Item.Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player playerIn, @NotNull InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		if (playerIn.isShiftKeyDown()) {
			if (level.isClientSide) {
				com.mrbysco.forcecraft.client.gui.pack.RenameAndRecolorScreen.openScreen(stack, handIn);
			}
		} else {
			if (!level.isClientSide) {
				BeltStorage data = StorageManager.getOrCreateBelt(stack);

				NetworkHooks.openScreen((ServerPlayer) playerIn, getContainer(stack, data.getInventory()));
			}
		}
		//If it doesn't nothing bad happens
		return super.use(level, playerIn, handIn);
	}

/*    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        if(player != null && !context.getLevel().isClientSide && stack.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
            player.openMenu(getContainer(stack));
            return InteractionResult.PASS;
        }
        //If it doesn't nothing bad happens
        return super.useOn(context);
    }*/

	@Nullable
	public MenuProvider getContainer(ItemStack stack, IItemHandler handler) {
		return new SimpleMenuProvider((id, inventory, player) -> new ForceBeltMenu(id, inventory, handler)
				, stack.hasCustomHoverName() ? ((MutableComponent) stack.getHoverName()).withStyle(ChatFormatting.BLACK) : Component.translatable(Reference.MOD_ID + ".container.belt"));
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

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new WSDCapability(stack);
	}

	public static boolean filter(ItemStack stack) {
		return !(stack.getItem() instanceof ForceBeltItem) && stack.is(ForceTags.VALID_FORCE_BELT);
	}
}
