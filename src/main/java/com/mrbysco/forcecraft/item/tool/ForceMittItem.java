package com.mrbysco.forcecraft.item.tool;

import com.mojang.datafixers.util.Pair;
import com.mrbysco.forcecraft.registry.ForceTags;
import com.mrbysco.forcecraft.registry.ModToolTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.ItemAbilities;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ForceMittItem extends Item {
	public ForceMittItem(Item.Properties properties) {
		super(ModToolTiers.FORCE
				.applyToolProperties(properties, ForceTags.Blocks.MINEABLE_WITH_MITTS,
						3.0F, -2.4F, 0.0F)
		);
	}

	@Override
	public boolean mineBlock(ItemStack itemStack, Level level, BlockState state, BlockPos pos, LivingEntity owner) {
		if (owner instanceof Player player && state.is(BlockTags.LEAVES)) {
			var axis = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE).getDirection().getAxis();

			int rx = axis.choose(0, 1, 1);
			int ry = axis.choose(1, 0, 1);
			int rz = axis.choose(1, 1, 0);

			for (int dx = -rx; dx <= rx; dx++) {
				for (int dy = -ry; dy <= ry; dy++) {
					for (int dz = -rz; dz <= rz; dz++) {
						if (dx == 0 && dy == 0 && dz == 0) continue;
						breakBlockAt(player, level, itemStack, pos.offset(dx, dy, dz));
					}
				}
			}

//			level.playSound((Player) null, pos, ForceSounds.WHOOSH.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
		}
		return super.mineBlock(itemStack, level, state, pos, owner);
	}

	public void breakBlockAt(Player player, Level level, ItemStack stack, BlockPos pos) {
		if (level.isClientSide() || !(player instanceof ServerPlayer serverPlayer)) return;

		ServerLevel serverLevel = serverPlayer.level();
		BlockState state = level.getBlockState(pos);
		if (!state.is(BlockTags.LEAVES) || !stack.isCorrectToolForDrops(state)) return;

		if (CommonHooks.fireBlockBreak(level, serverPlayer.gameMode.getGameModeForPlayer(), serverPlayer, pos, state).isCanceled()) return;

		BlockEntity blockEntity = level.getBlockEntity(pos);
		Block block = state.getBlock();

		if (block.onDestroyedByPlayer(state, level, pos, player, stack, true, level.getFluidState(pos))) {
			block.playerWillDestroy(level, pos, state, player);
			block.playerDestroy(level, player, pos, state, blockEntity, stack);
			block.popExperience(serverLevel, pos, block.getExpDrop(state, level, pos, null, player, stack));
		}

		serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, pos.getX(), pos.getY(), pos.getZ(), 1, 0, 0, 0, 0.0D);
		serverPlayer.connection.send(new ClientboundBlockUpdatePacket(level, pos));
	}

	@Override
	public @NonNull InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState toolModifiedState = level.getBlockState(pos).getToolModifiedState(context, ItemAbilities.HOE_TILL, false);
		Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> logicPair = toolModifiedState == null ? null : Pair.of(ctx -> true,
				changeIntoState(toolModifiedState));
		if (logicPair == null) {
			return InteractionResult.PASS;
		} else {
			Predicate<UseOnContext> predicate = logicPair.getFirst();
			Consumer<UseOnContext> action = logicPair.getSecond();
			if (predicate.test(context)) {
				Player player = context.getPlayer();
				level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
				if (!level.isClientSide()) {
					action.accept(context);
					if (player != null) {
						context.getItemInHand().hurtAndBreak(1, player, context.getHand());
					}
				}

				return InteractionResult.SUCCESS;
			} else {
				return InteractionResult.PASS;
			}
		}
	}

	public static Consumer<UseOnContext> changeIntoState(BlockState state) {
		return context -> {
			context.getLevel().setBlock(context.getClickedPos(), state, 11);
			context.getLevel().gameEvent(GameEvent.BLOCK_CHANGE, context.getClickedPos(), GameEvent.Context.of(context.getPlayer(), state));
		};
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return state.is(ForceTags.Blocks.MINEABLE_WITH_MITTS) ? 14.0F : 1.0F;
	}

	@Override
	public float getAttackDamageBonus(Entity victim, float damage, DamageSource damageSource) {
		return ModToolTiers.FORCE.attackDamageBonus();
	}

	@Override
	public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.hurtAndBreak(1, attacker, attacker.getUsedItemHand());
	}
}
