package com.mrbysco.forcecraft.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.event.EventHooks;

import java.util.Stack;

public class ForceTreeUtil {
	//Credit to Slimeknights for this code until I can logic through it on my own
	public static boolean isTree(Level level, BlockPos origin) {
		BlockPos pos = null;
		Stack<BlockPos> candidates = new Stack<>();
		candidates.add(origin);

		while (!candidates.isEmpty()) {
			BlockPos candidate = candidates.pop();
			if ((pos == null || candidate.getY() >= pos.getY()) && isLog(level, candidate)) {
				pos = candidate.above();
				// go up
				while (isLog(level, pos)) {
					pos = pos.above();
				}
				// check if we still have a way diagonally up
				candidates.add(pos.north());
				candidates.add(pos.east());
				candidates.add(pos.south());
				candidates.add(pos.west());
			}
		}

		// not even one match, so there were no logs.
		if (pos == null) {
			return false;
		}

		// check if there were enough leaves around the last position
		// pos now contains the block above the topmost log
		// we want at least 5 leaves in the surrounding 26 blocks
		int d = 3;
		int o = -1; // -(d-1)/2
		int leaves = 0;
		for (int x = 0; x < d; x++) {
			for (int y = 0; y < d; y++) {
				for (int z = 0; z < d; z++) {
					BlockPos leaf = pos.offset(o + x, o + y, o + z);
					BlockState state = level.getBlockState(leaf);
					if (state.is(BlockTags.LEAVES)) {
						if (++leaves >= 5) {
							return true;
						}
					}
				}
			}
		}

		// not enough leaves. sorreh
		return false;
	}

	public static boolean isLog(Level level, BlockPos pos) {
		return level.getBlockState(pos).is(BlockTags.LOGS);
	}

	public static void breakExtraBlock(ItemStack stack, Level level, Player player, BlockPos pos, BlockPos refPos) {
		if (!canBreakExtraBlock(stack, level, player, pos, refPos)) {
			return;
		}

		BlockState state = level.getBlockState(pos);
		FluidState fluidState = level.getFluidState(pos);
		Block block = state.getBlock();

		// callback to the tool the player uses. Called on both sides. This damages the tool n stuff.
		stack.mineBlock(level, state, pos, player);

		// server sided handling
		if (!level.isClientSide()) {
			// send the blockbreak event
			BlockState blockstate1 = level.getBlockState(pos);
			var event = net.neoforged.neoforge.common.CommonHooks.fireBlockBreak(level, ((ServerPlayer) player).gameMode.getGameModeForPlayer(), player, pos, blockstate1);
			if (event.isCanceled()) {
				return;
			}

			// serverside we reproduce ItemInWorldManager.tryHarvestBlock

			BlockEntity tileEntity = level.getBlockEntity(pos);
			// ItemInWorldManager.removeBlock
			if (block.onDestroyedByPlayer(state, level, pos, player, stack, true, fluidState)) { // boolean is if block can be harvested, checked above
				block.playerWillDestroy(level, pos, state, player);
				block.playerDestroy(level, player, pos, state, tileEntity, stack);
				int xp = block.getExpDrop(state, level, pos, null, player, stack);
				if (xp > 0)
					block.popExperience((ServerLevel) level, pos, xp);
			}

			// always send block update to client
			((ServerPlayer) player).connection.send(new ClientboundBlockUpdatePacket(level, pos));
		}
		// client sided handling
		else {
			// clientside we do a "this clock has been clicked on long enough to be broken" call. This should not send any new packets
			// the code above, executed on the server, sends a block-updates that give us the correct state of the block we destroy.

			// following code can be found in PlayerControllerMP.onPlayerDestroyBlock
			level.globalLevelEvent(2001, pos, Block.getId(state));
			if (block.onDestroyedByPlayer(state, level, pos, player, stack, true, fluidState)) {
				block.playerWillDestroy(level, pos, state, player);
			}
			// callback to the tool
			stack.mineBlock(level, state, pos, player);

			if (stack.getCount() == 0 && stack == player.getMainHandItem()) {
				EventHooks.onPlayerDestroyItem(player, stack, InteractionHand.MAIN_HAND);
				player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
			}

			// send an update to the server, so we get an update back
			//if(PHConstruct.extraBlockUpdates)
			ClientPacketListener netHandlerPlayClient = Minecraft.getInstance().getConnection();
			assert netHandlerPlayClient != null;
			netHandlerPlayClient.send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK, pos, Direction.DOWN)); //.getInstance().objectMouseOver.sideHit
		}
	}

	private static boolean canBreakExtraBlock(ItemStack stack, Level level, Player player, BlockPos pos, BlockPos refPos) {
		// prevent calling that stuff for air blocks, could lead to unexpected behaviour since it fires events
		if (level.isEmptyBlock(pos)) {
			return false;
		}

		// check if the block can be broken, since extra block breaks shouldn't instantly break stuff like obsidian
		// or precious ores you can't harvest while mining stone
		BlockState state = level.getBlockState(pos);
		FluidState fluidState = level.getFluidState(pos);
		Block block = state.getBlock();

		// only effective materials
		//TODO: Check for Effective

		BlockState refState = level.getBlockState(refPos);
		float refStrength = refState.getDestroyProgress(player, level, refPos);
		float strength = state.getDestroyProgress(player, level, pos);

		// only harvestable blocks that aren't impossibly slow to harvest
		if (!stack.isCorrectToolForDrops(state) || refStrength / strength > 10f) {
			return false;
		}

		// From this point on it's clear that the player CAN break the block

		if (player.getAbilities().instabuild) {
			block.playerWillDestroy(level, pos, state, player);
			if (block.onDestroyedByPlayer(state, level, pos, player, stack, false, fluidState)) {
				block.playerWillDestroy(level, pos, state, player);
			}

			// send update to client
			if (!level.isClientSide()) {
				((ServerPlayer) player).connection.send(new ClientboundBlockUpdatePacket(level, pos));
			}
			return false;
		}
		return true;
	}
}
