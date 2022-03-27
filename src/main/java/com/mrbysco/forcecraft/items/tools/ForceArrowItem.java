package com.mrbysco.forcecraft.items.tools;

import com.mrbysco.forcecraft.entities.projectile.ForceArrowEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.mrbysco.forcecraft.capabilities.CapabilityHandler.CAPABILITY_TOOLMOD;

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
				heldItem.getCapability(CAPABILITY_TOOLMOD).ifPresent(cap -> {
					if (cap.hasFreezing()) {
						forceArrow.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2, false, false));
					}
					if (cap.hasEnder()) {
						forceArrow.setEnder();
					}
					if (cap.hasBane()) {
						forceArrow.setBane();
					}
					if (cap.hasLight()) {
						forceArrow.setAppliesGlowing();
					}
					if (cap.hasBleed()) {
						forceArrow.setBleeding(cap.getBleedLevel());
					}
					if (cap.hasLuck()) {
						int luckValue = cap.getLuckLevel();
						forceArrow.setLuck(luckValue);
					}
					if (cap.getSpeedLevel() > 0) {
						forceArrow.setSpeedy();
					}
				});
			}
		}
		forceArrow.setEffectsFromItem(stack);
		return forceArrow;
	}
}
