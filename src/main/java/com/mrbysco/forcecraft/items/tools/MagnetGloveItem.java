package com.mrbysco.forcecraft.items.tools;

import com.mrbysco.forcecraft.attachment.magnet.MagnetAttachment;
import com.mrbysco.forcecraft.items.BaseItem;
import com.mrbysco.forcecraft.registry.ForceEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.mrbysco.forcecraft.attachment.CapabilityHandler.MAGNET;

public class MagnetGloveItem extends BaseItem {

	public MagnetGloveItem(Item.Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
		if (playerIn.isShiftKeyDown()) {
			ItemStack stack = playerIn.getItemInHand(handIn);
			MagnetAttachment attachment = stack.getData(MAGNET);
			boolean state = attachment.isActivated();
			attachment.setActivation(!state);
			stack.setData(MAGNET, attachment);
			level.playSound((Player) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.NEUTRAL, 1.0F, 1.0F);
		}
		return super.use(level, playerIn, handIn);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entityIn, int itemSlot, boolean isSelected) {
		if (entityIn instanceof Player && !(entityIn instanceof FakePlayer)) {
			if (itemSlot >= 0 && itemSlot <= Inventory.getSelectionSize()) {
				MagnetAttachment attachment = stack.getData(MAGNET);
				if (attachment.isActivated()) {
					((Player) entityIn).addEffect(new MobEffectInstance(ForceEffects.MAGNET.get(), 20, 1, true, false));
				}
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lores, TooltipFlag flagIn) {
		MagnetAttachment.attachInformation(stack, lores);
		super.appendHoverText(stack, level, lores, flagIn);
	}
}
