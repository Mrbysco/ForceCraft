package com.mrbysco.forcecraft.util;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.config.ForceConfig;
import com.mrbysco.forcecraft.data.TimeData;
import com.mrbysco.forcecraft.data.TimeMode;
import com.mrbysco.forcecraft.handler.infusion.TimeHandler;
import com.mrbysco.forcecraft.registry.ForceAttachments;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceEffects;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

public class RodHelper {
	public static final int[][] SPEED_BUFFS = {
			{0, 0, 0},
			{1, 1, 1},
			{2, 2, 2},
			{2, 3, 2},
			{3, 1, 3}
	};

	public static boolean applyBuffs(LivingEntity livingEntity, ItemStack stack) {
		if (stack.has(ForceComponents.HEALING)) {
			int healLevel = stack.getOrDefault(ForceComponents.HEALING, 1);
			return applyCappedEffect(livingEntity, MobEffects.REGENERATION, healLevel - 1, 4 * 20, 16 * 20);
		}
		if (stack.has(ForceComponents.HEAT)) {
			return applyCappedEffect(livingEntity, MobEffects.FIRE_RESISTANCE, 0, 20 * 20, 60 * 20);
		}
		if (stack.has(ForceComponents.CAMO)) {
			return applyCappedEffect(livingEntity, MobEffects.INVISIBILITY, 0, 30 * 20, 90 * 20);
		}
		if (stack.has(ForceComponents.SIGHT)) {
			return applyCappedEffect(livingEntity, MobEffects.NIGHT_VISION, 0, 60 * 20, 180 * 20);
		}
		if (stack.has(ForceComponents.SPEED)) {
			int speedLevel = Mth.clamp(stack.getOrDefault(ForceComponents.SPEED, 1), 1, SPEED_BUFFS.length);
			int[] amplifiers = SPEED_BUFFS[speedLevel - 1];
			int ticks = 8 * 20;
			int maxTicks = 36 * 20;

			if (!canApply(livingEntity, MobEffects.SPEED, amplifiers[0], ticks, maxTicks)
					|| !canApply(livingEntity, MobEffects.HASTE, amplifiers[1], ticks, maxTicks)
					|| !canApply(livingEntity, MobEffects.JUMP_BOOST, amplifiers[2], ticks, maxTicks)) {
				return false;
			}

			applyCappedEffect(livingEntity, MobEffects.SPEED, amplifiers[0], ticks, maxTicks);
			applyCappedEffect(livingEntity, MobEffects.HASTE, amplifiers[1], ticks, maxTicks);
			applyCappedEffect(livingEntity, MobEffects.JUMP_BOOST, amplifiers[2], ticks, maxTicks);
			return true;
		}
		return false;
	}

	public static boolean canApply(LivingEntity livingEntity, Holder<MobEffect> effect, int amplifier, int ticks, int maxTicks) {
		MobEffectInstance current = livingEntity.getEffect(effect);

		int currentDuration = (current != null && current.getAmplifier() == amplifier) ? current.getDuration() : 0;
		int potentialTicks = currentDuration + ticks;

		return potentialTicks <= maxTicks;
	}

	public static boolean applyCappedEffect(LivingEntity livingEntity, Holder<MobEffect> effect, int amplifier, int ticks, int maxTicks) {
		if (!canApply(livingEntity, effect, amplifier, ticks, maxTicks)) {
			return false;
		}

		MobEffectInstance current = livingEntity.getEffect(effect);
		int duration = ticks;
		if (current != null && current.getAmplifier() == amplifier) {
			duration = Math.min(current.getDuration() + ticks, maxTicks);
		}
		livingEntity.addEffect(new MobEffectInstance(effect, duration, amplifier, false, true, true));
		return true;
	}

	public static Identifier getRodCooldownGroup(ItemStack stack) {
		String variant;
		if (stack.has(ForceComponents.TIME)) {
			variant = "time";
		} else if (stack.has(ForceComponents.SPEED)) {
			variant = "speed_" + stack.getOrDefault(ForceComponents.SPEED, 1);
		} else if (stack.has(ForceComponents.HEALING)) {
			variant = "healing_" + stack.getOrDefault(ForceComponents.HEALING, 1);
		} else if (stack.has(ForceComponents.HEAT)) {
			variant = "heat";
		} else if (stack.has(ForceComponents.CAMO)) {
			variant = "camo";
		} else if (stack.has(ForceComponents.SIGHT)) {
			variant = "sight";
		} else if (stack.has(ForceComponents.ENDER)) {
			variant = "ender";
		} else {
			variant = "base";
		}
		return Identifier.fromNamespaceAndPath(Reference.MOD_ID, "rod_" + variant);
	}

	public static boolean isBasicRod(ItemStack stack) {
		return !stack.has(ForceComponents.TIME)
				&& !stack.has(ForceComponents.ENDER)
				&& !stack.has(ForceComponents.HEALING)
				&& !stack.has(ForceComponents.HEAT)
				&& !stack.has(ForceComponents.CAMO)
				&& !stack.has(ForceComponents.SIGHT)
				&& !stack.has(ForceComponents.SPEED);
	}

	public static void applyTimeField(ServerLevel level, BlockPos pos, TimeMode mode, Player player) {
		int entityDuration = ForceConfig.COMMON.entitySpeedDuration.getAsInt();
		int radius = ForceConfig.COMMON.timeRadius.getAsInt();
		AABB area = new AABB(pos).inflate(radius);
		for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, area,
				e -> e.isAlive() && !(e instanceof Player) && !e.is(ForceTags.EntityTypes.TICKING_NOT_SUPPORTED))) {
			target.removeEffect(mode.isSlowing() ? ForceEffects.TIME_SPEEDUP : ForceEffects.TIME_SLOWDOWN);
			target.addEffect(mode.createEffect(entityDuration), player);
		}

		float rate = TimeData.rateFor(mode);
		int blockDuration = ForceConfig.COMMON.blockSpeedDuration.getAsInt();
		for (BlockPos target : BlockPos.betweenClosed(pos.offset(-radius, -radius, -radius), pos.offset(radius, radius, radius))) {
			BlockEntity blockEntity = level.getBlockEntity(target);
			if (blockEntity == null || !(blockEntity.getBlockState().getBlock() instanceof EntityBlock)) continue;
			blockEntity.setData(ForceAttachments.TIME_ADJUSTMENT, new TimeData(level.getGameTime() + blockDuration, rate));
			TimeHandler.trackBlock(level, target.immutable());
		}
	}
}
