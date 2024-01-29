package com.mrbysco.forcecraft.items.tools;

import com.mrbysco.forcecraft.attachment.toolmodifier.ToolModifierAttachment;
import com.mrbysco.forcecraft.entities.projectile.ForceArrowEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.mrbysco.forcecraft.attachment.CapabilityHandler.TOOL_MODIFIER;

public class ForceArrowItem extends ArrowItem {
	public ForceArrowItem(Properties builder) {
		super(builder);
	}

	@Override
	public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter) {
		ForceArrowEntity forceArrow = new ForceArrowEntity(level, shooter);
		if (shooter instanceof Player player) {
			ItemStack heldItem = player.getUseItem();
			if (heldItem.getItem() instanceof ForceBowItem) {
				ToolModifierAttachment attachment = heldItem.getData(TOOL_MODIFIER);
				if (attachment.hasFreezing()) {
					forceArrow.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2, false, false));
				}
				if (attachment.hasEnder()) {
					forceArrow.setEnder();
				}
				if (attachment.hasBane()) {
					forceArrow.setBane();
				}
				if (attachment.hasLight()) {
					forceArrow.setAppliesGlowing();
				}
				if (attachment.hasBleed()) {
					forceArrow.setBleeding(attachment.getBleedLevel());
				}
				if (attachment.hasLuck()) {
					int luckValue = attachment.getLuckLevel();
					forceArrow.setLuck(luckValue);
				}
				if (attachment.getSpeedLevel() > 0) {
					forceArrow.setSpeedy();
				}
			}
		}
		forceArrow.setEffectsFromItem(stack);
		return forceArrow;
	}
}
