package com.mrbysco.forcecraft.items.tools;

import com.mrbysco.forcecraft.components.ForceComponents;
import com.mrbysco.forcecraft.items.BaseItem;
import com.mrbysco.forcecraft.registry.ForceEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

import static com.mrbysco.forcecraft.components.ForceComponents.MAGNET;

public class MagnetGloveItem extends BaseItem {

	public MagnetGloveItem(Item.Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public InteractionResult use(Level level, Player playerIn, InteractionHand handIn) {
		if (playerIn.isShiftKeyDown()) {
			ItemStack stack = playerIn.getItemInHand(handIn);
			boolean state = stack.getOrDefault(MAGNET, false);
			stack.set(MAGNET, !state);
			level.playSound((Player) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.NEUTRAL, 1.0F, 1.0F);
		}
		return super.use(level, playerIn, handIn);
	}


	@Override
	public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
		if (owner instanceof Player player && !(owner instanceof FakePlayer)) {
			if (slot != null && slot.getIndex() >= 0 && slot.getIndex() <= Inventory.getSelectionSize()) {
				boolean state = itemStack.getOrDefault(MAGNET, false);
				if (state) {
					player.addEffect(new MobEffectInstance(ForceEffects.MAGNET, 20, 1, true, false));
				}
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
		if (itemStack.has(ForceComponents.MAGNET)) {
			boolean activated = itemStack.getOrDefault(ForceComponents.MAGNET, false);
			if (activated) {
				builder.accept(Component.translatable("forcecraft.magnet_glove.active").withStyle(ChatFormatting.GREEN));
			} else {
				builder.accept(Component.translatable("forcecraft.magnet_glove.deactivated").withStyle(ChatFormatting.RED));
			}
			builder.accept(Component.empty());
			builder.accept(Component.translatable("forcecraft.magnet_glove.change").withStyle(ChatFormatting.BOLD));
		}
	}
}
