package com.mrbysco.forcecraft.items.tools;

import com.google.common.collect.Lists;
import com.mrbysco.forcecraft.items.infuser.ForceToolData;
import com.mrbysco.forcecraft.items.infuser.IForceChargingTool;
import com.mrbysco.forcecraft.registry.material.ModToolTiers;
import com.mrbysco.forcecraft.util.ForceUtils;
import com.mrbysco.forcecraft.util.TooltipUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

import static com.mrbysco.forcecraft.util.ForceUtils.isLog;

public class ForceAxeItem extends AxeItem implements IForceChargingTool {

	public ForceAxeItem(Item.Properties properties) {
		super(ModToolTiers.FORCE, 0F, -3.1F, properties.enchantable(0));
	}

	public static boolean fellTree(ItemStack stack, BlockPos pos, Player player) {
		if (player.level().isClientSide()) {
			return true;
		}
		NeoForge.EVENT_BUS.register(new TreeChopTask(stack, pos, player, 10));
		return true;
	}

	public static class TreeChopTask {
		public final Level level;
		public final Player player;
		public final ItemStack tool;
		public final int blocksPerTick;

		public Queue<BlockPos> blocks = Lists.newLinkedList();
		public Set<BlockPos> visited = new HashSet<>();

		public TreeChopTask(ItemStack tool, BlockPos start, Player player, int blocksPerTick) {
			this.level = player.level();
			this.player = player;
			this.tool = tool;
			this.blocksPerTick = blocksPerTick;

			this.blocks.add(start.above());
		}

		@SubscribeEvent
		public void chop(LevelTickEvent.Post event) {
			Level level = event.getLevel();
			if (level.isClientSide()) {
				return;
			}
			// only if same dimension
			if (!this.level.dimension().identifier().equals(level.dimension().identifier())) {
				return;
			}

			// setup
			int left = blocksPerTick;

			// continue running
			BlockPos pos;
			while (left > 0) {
				// completely done or can't do our job anymore?!
				if (blocks.isEmpty()) {
					finish();
					return;
				}

				pos = blocks.remove();
				if (!visited.add(pos)) {
					continue;
				}

				// can we harvest the block and is effective?
				if (!isLog(level, pos)) {
					continue;
				}

				// save its neighbours
				for (Direction facing : new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST}) {
					BlockPos pos2 = pos.relative(facing);
					if (!visited.contains(pos2)) {
						blocks.add(pos2);
					}
				}

				// also add the layer above.. stupid acacia trees
				for (int x = 0; x < 3; x++) {
					for (int z = 0; z < 3; z++) {
						BlockPos pos2 = pos.offset(-1 + x, 1, -1 + z);
						if (!visited.contains(pos2)) {
							blocks.add(pos2);
						}
					}
				}

				// break it, wooo!
				ForceUtils.breakExtraBlock(tool, level, player, pos, pos);
				left--;
			}
		}

		private void finish() {
			// goodbye cruel world
			NeoForge.EVENT_BUS.unregister(this);
		}
	}

//	@Override
//	public int getEnchantmentValue() {
//		return 0;
//	}
//
//	@Override
//	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
//		return false;
//	}

	@Override
	public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
		TooltipUtil.addForceTooltips(itemStack, builder);
		ForceToolData fd = new ForceToolData(itemStack);
		fd.attachInformation(builder);
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
		return this.damageItem(stack, amount);
	}

}
