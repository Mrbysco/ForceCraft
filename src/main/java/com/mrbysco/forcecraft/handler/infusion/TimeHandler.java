package com.mrbysco.forcecraft.handler.infusion;

import com.mrbysco.forcecraft.data.TimeData;
import com.mrbysco.forcecraft.item.tool.ForceBowItem;
import com.mrbysco.forcecraft.registry.ForceAttachments;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceEffects;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@EventBusSubscriber
public class TimeHandler {
	/**
	 * Entity Time adjustment section
	 */
	@SubscribeEvent
	public static void onDamage(LivingDamageEvent.Post event) {
		ItemStack weapon = event.getSource().getWeaponItem();
		if (weapon == null || weapon.isEmpty()) return;
		if (weapon.getItem() instanceof ForceBowItem) return;
		boolean hasTime = weapon.getOrDefault(ForceComponents.TIME, false);
		if (!hasTime) return;

		LivingEntity target = event.getEntity();
		if (!target.isAlive()) return;
		if (target.is(ForceTags.EntityTypes.TICKING_NOT_SUPPORTED)) return;

		target.addEffect(new MobEffectInstance(ForceEffects.TIME_SLOWDOWN, 100, 0), event.getSource().getEntity());
	}

	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Pre event) {
		if (event.getEntity() instanceof Mob mob && isFrozen(mob)) {
			MobEffectInstance instance = mob.getEffect(ForceEffects.TIME_SLOWDOWN);
			int interval = instance.getAmplifier() + 1;

			boolean allowTick = instance.getAmplifier() > 0 && mob.level().getGameTime() % interval == 0;
			if (!allowTick) {
				if (mob.invulnerableTime > 0) mob.invulnerableTime--;
				if (mob.hurtTime > 0) mob.hurtTime--;
				mob.tickEffects();
				event.setCanceled(true);
			}
		}
	}

	public static boolean isFrozen(LivingEntity livingEntity) {
		return !(livingEntity.isDeadOrDying()) && livingEntity.hasEffect(ForceEffects.TIME_SLOWDOWN);
	}

	/**
	 * Block Time adjustment section
	 */
	private static final Map<ResourceKey<Level>, Set<BlockPos>> TRACKED_BLOCKS = new HashMap<>();

	public static void trackBlock(ServerLevel level, BlockPos pos) {
		TRACKED_BLOCKS.computeIfAbsent(level.dimension(), k -> new HashSet<>()).add(pos);
	}

	@SubscribeEvent
	public static void onChunkLoad(ChunkEvent.Load event) {
		if (!(event.getLevel() instanceof ServerLevel level) || !(event.getChunk() instanceof LevelChunk chunk)) return;
		chunk.getBlockEntities().forEach((pos, blockEntity) -> {
			if (blockEntity.hasData(ForceAttachments.TIME_ADJUSTMENT)) trackBlock(level, pos);
		});
	}

	@SubscribeEvent
	public static void onLevelTick(LevelTickEvent.Pre event) {
		if (!(event.getLevel() instanceof ServerLevel level)) return;
		Set<BlockPos> set = TRACKED_BLOCKS.get(level.dimension());
		if (set == null || set.isEmpty()) return;

		long time = level.getGameTime();
		set.removeIf(pos -> !processAdjustedTick(level, pos, time));
	}

	private static boolean processAdjustedTick(ServerLevel level, BlockPos pos, long time) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity == null || !blockEntity.hasData(ForceAttachments.TIME_ADJUSTMENT)) return false;

		TimeData timeData = blockEntity.getData(ForceAttachments.TIME_ADJUSTMENT);
		LevelChunk chunk = level.getChunkAt(pos);

		if (!timeData.isActive(time)) {
			blockEntity.removeData(ForceAttachments.TIME_ADJUSTMENT);
			chunk.updateBlockEntityTicker(blockEntity);
			return false;
		}

		if (timeData.shouldSkip(time)) {
			chunk.removeBlockEntityTicker(pos);
		} else if (timeData.shouldSkip(time - 1)) {
			chunk.updateBlockEntityTicker(blockEntity);
		}

		for (int i = 0; i < timeData.extraTicks(); i++) {
			tickOnce(level, blockEntity);
		}
		return true;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private static void tickOnce(ServerLevel level, BlockEntity blockEntity) {
		BlockEntityTicker ticker = blockEntity.getBlockState().getTicker(level, (BlockEntityType<BlockEntity>) blockEntity.getType());
		if (ticker != null) {
			ticker.tick(level, blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity);
		}
	}
}
