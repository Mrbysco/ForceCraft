package com.mrbysco.forcecraft.handler.infusion;

import com.google.common.collect.Lists;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.util.ForceTreeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

@EventBusSubscriber
public class LumberjackHandler {
	private static final Map<UUID, TreeChopTask> ACTIVE = new HashMap<>();

	@SubscribeEvent
	public static void onBlockBreak(BreakBlockEvent event) {
		Player player = event.getPlayer();
		if (player.level().isClientSide() || player.isShiftKeyDown()) return;
		if (ACTIVE.containsKey(player.getUUID())) return;

		ItemStack tool = player.getMainHandItem();
		if (!tool.has(ForceComponents.LUMBERJACK)) return;
		if (!ForceTreeUtil.isLog(player.level(), event.getPos())) return;

		TreeChopTask task = new TreeChopTask(tool, event.getPos(), player, 10);
		ACTIVE.put(player.getUUID(), task);
		NeoForge.EVENT_BUS.register(task);
	}

	@SubscribeEvent
	public static void onServerStopping(ServerStoppingEvent event) {
		for (TreeChopTask task : new ArrayList<>(ACTIVE.values())) {
			task.finish();
		}
		ACTIVE.clear();
	}

	public static class TreeChopTask {
		private final Level level;
		private final Player player;
		private final ItemStack tool;
		private final int blocksPerTick;

		private final Queue<BlockPos> blocks = Lists.newLinkedList();
		private final Set<BlockPos> visited = new HashSet<>();

		public TreeChopTask(ItemStack tool, BlockPos start, Player player, int blocksPerTick) {
			this.level = player.level();
			this.player = player;
			this.tool = tool;
			this.blocksPerTick = blocksPerTick;

			this.blocks.add(start.above());
		}

		@SubscribeEvent
		public void chop(LevelTickEvent.Post event) {
			Level eventLevel = event.getLevel();
			if (eventLevel.isClientSide() || !this.level.dimension().equals(eventLevel.dimension())) return;

			if (tool.isEmpty() || !player.isAlive()) {
				finish();
				return;
			}

			int left = blocksPerTick;
			while (left > 0) {
				if (blocks.isEmpty()) {
					finish();
					return;
				}

				BlockPos pos = blocks.remove();
				if (!visited.add(pos)) continue;
				if (!ForceTreeUtil.isLog(eventLevel, pos)) continue;

				for (Direction facing : Direction.Plane.HORIZONTAL) {
					BlockPos next = pos.relative(facing);
					if (!visited.contains(next)) blocks.add(next);
				}

				for (int x = -1; x <= 1; x++) {
					for (int z = -1; z <= 1; z++) {
						BlockPos next = pos.offset(x, 1, z);
						if (!visited.contains(next)) blocks.add(next);
					}
				}

				ForceTreeUtil.breakExtraBlock(tool, eventLevel, player, pos, pos);
				left--;
			}
		}

		private void finish() {
			ACTIVE.remove(player.getUUID());
			NeoForge.EVENT_BUS.unregister(this);
		}
	}
}