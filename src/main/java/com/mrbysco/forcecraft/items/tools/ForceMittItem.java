package com.mrbysco.forcecraft.items.tools;

import com.mojang.datafixers.util.Pair;
import com.mrbysco.forcecraft.registry.ForceSounds;
import com.mrbysco.forcecraft.registry.ForceTags;
import com.mrbysco.forcecraft.registry.material.ModToolTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.CommonHooks;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ForceMittItem extends Item {

	public ForceMittItem(Item.Properties properties) {
		super(ModToolTiers.FORCE
				.applyToolProperties(properties, ForceTags.MINEABLE_WITH_MITTS,
						3.0F, -2.4F, 0.0F)
				.durability(1000)
				.enchantable(0)
		);
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entityLiving) {
		if (entityLiving instanceof Player player && state.is(BlockTags.LEAVES)) {
			BlockHitResult traceResult = getPlayerPOVHitResult(level, player, Fluid.NONE);
			switch (traceResult.getDirection().getAxis()) {
				case X -> {
					breakBlockAt(player, level, stack, pos.above());
					breakBlockAt(player, level, stack, pos.below());
					breakBlockAt(player, level, stack, pos.north());
					breakBlockAt(player, level, stack, pos.north().above());
					breakBlockAt(player, level, stack, pos.north().below());
					breakBlockAt(player, level, stack, pos.south());
					breakBlockAt(player, level, stack, pos.south().above());
					breakBlockAt(player, level, stack, pos.south().below());
				}
				case Z -> {
					breakBlockAt(player, level, stack, pos.above());
					breakBlockAt(player, level, stack, pos.below());
					breakBlockAt(player, level, stack, pos.west());
					breakBlockAt(player, level, stack, pos.west().above());
					breakBlockAt(player, level, stack, pos.west().below());
					breakBlockAt(player, level, stack, pos.east());
					breakBlockAt(player, level, stack, pos.east().above());
					breakBlockAt(player, level, stack, pos.east().below());
				}
				case Y -> {
					breakBlockAt(player, level, stack, pos.north());
					breakBlockAt(player, level, stack, pos.east());
					breakBlockAt(player, level, stack, pos.west());
					breakBlockAt(player, level, stack, pos.west().north());
					breakBlockAt(player, level, stack, pos.west().east());
					breakBlockAt(player, level, stack, pos.east());
					breakBlockAt(player, level, stack, pos.east().north());
					breakBlockAt(player, level, stack, pos.east().east());
				}
			}

			level.playSound((Player) null, pos, ForceSounds.WHOOSH.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
		}
		return super.mineBlock(stack, level, state, pos, entityLiving);
	}

	public void breakBlockAt(Player player, Level level, ItemStack stack, BlockPos pos) {
		BlockEntity tileEntity = level.getBlockEntity(pos);
		if (level.getBlockState(pos).is(BlockTags.LEAVES)) {
			BlockState state = level.getBlockState(pos);
			if (!stack.isCorrectToolForDrops(state)) return;

			if (!level.isClientSide()) {
				if (CommonHooks.fireBlockBreak(level, ((ServerPlayer) player).gameMode.getGameModeForPlayer(),
						(ServerPlayer) player, pos, state).isCanceled()) {
					return;
				}

				FluidState fluidState = level.getFluidState(pos);
				Block block = state.getBlock();

				if (block.onDestroyedByPlayer(state, level, pos, player, stack, true, fluidState)) {
					block.playerWillDestroy(level, pos, state, player);
					block.playerDestroy(level, player, pos, state, tileEntity, stack);
					int exp = block.getExpDrop(state, level, pos, null, player, stack);
					block.popExperience((ServerLevel) level, pos, exp);
				}

				((ServerLevel) level).sendParticles(ParticleTypes.SWEEP_ATTACK, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), 1, 0, 0, 0, 0.0D);
				((ServerPlayer) player).connection.send(new ClientboundBlockUpdatePacket(level, pos));
			}
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
	public @NonNull InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState toolModifiedState = level.getBlockState(pos).getToolModifiedState(context, net.neoforged.neoforge.common.ItemAbilities.HOE_TILL, false);
		Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> logicPair = toolModifiedState == null ? null : Pair.of(ctx -> true, changeIntoState(toolModifiedState));
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
		return state.is(ForceTags.MINEABLE_WITH_MITTS) ? 14.0F : 1.0F;
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
