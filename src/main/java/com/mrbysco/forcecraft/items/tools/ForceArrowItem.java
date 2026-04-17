package com.mrbysco.forcecraft.items.tools;

import com.mrbysco.forcecraft.components.ForceComponents;
import com.mrbysco.forcecraft.entities.projectile.ForceArrowEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ForceArrowItem extends ArrowItem {
	public ForceArrowItem(Properties builder) {
		super(builder);
	}

	@Override
	@NotNull
	public AbstractArrow createArrow(@NotNull Level level, ItemStack stack, @NotNull LivingEntity shooter, @Nullable ItemStack weapon) {
		ForceArrowEntity forceArrow = new ForceArrowEntity(level, shooter, stack.copyWithCount(1), weapon);
		if (shooter instanceof Player player && weapon != null) {
			if (weapon.has(ForceComponents.TOOL_FREEZING)) {
				forceArrow.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 60, 2, false, false));
			}
			if (weapon.has(ForceComponents.TOOL_ENDER)) {
				forceArrow.setEnder();
			}
			if (weapon.has(ForceComponents.TOOL_BANE)) {
				forceArrow.setBane();
			}
			if (weapon.has(ForceComponents.TOOL_LIGHT)) {
				forceArrow.setAppliesGlowing();
			}
			if (weapon.has(ForceComponents.TOOL_BLEED)) {
				forceArrow.setBleeding(weapon.getOrDefault(ForceComponents.TOOL_BLEED, 0));
			}
			if (weapon.has(ForceComponents.TOOL_LUCK)) {
				int luckValue = weapon.getOrDefault(ForceComponents.TOOL_LUCK, 0);
				forceArrow.setLuck(luckValue);
			}
			if (weapon.has(ForceComponents.TOOL_SPEED)) {
				forceArrow.setSpeedy();
			}
		}
		return forceArrow;
	}
}
