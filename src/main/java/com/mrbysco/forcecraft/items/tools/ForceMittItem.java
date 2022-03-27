package com.mrbysco.forcecraft.items.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import com.mrbysco.forcecraft.networking.PacketHandler;
import com.mrbysco.forcecraft.registry.ForceSounds;
import com.mrbysco.forcecraft.registry.ForceTags;
import com.mrbysco.forcecraft.registry.material.ModToolTiers;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeHooks;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ForceMittItem extends DiggerItem {

	private final float attackDamage;
	private final Tier itemTier = ModToolTiers.FORCE;

	public ForceMittItem(Item.Properties properties) {
		super(8.0F, 8.0F, ModToolTiers.FORCE, ForceTags.MINEABLE_WITH_MITTS, properties.durability(1000));
		this.attackDamage = 3.0F;
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entityLiving) {
		if (entityLiving instanceof Player player && state.getMaterial() == Material.LEAVES) {
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
		if (level.getBlockState(pos).getMaterial() == Material.LEAVES) {
			BlockState state = level.getBlockState(pos);
			if (!ForgeHooks.isCorrectToolForDrops(state, player)) return;

			if (!level.isClientSide) {
				int xp = ForgeHooks.onBlockBreakEvent(level, ((ServerPlayer) player).gameMode.getGameModeForPlayer(), (ServerPlayer) player, pos);
				if (xp == -1) {
					return;
				}

				FluidState fluidState = level.getFluidState(pos);
				Block block = state.getBlock();

				if (block.onDestroyedByPlayer(state, level, pos, player, true, fluidState)) {
					block.playerWillDestroy(level, pos, state, player);
					block.playerDestroy(level, player, pos, state, tileEntity, stack);
					block.popExperience((ServerLevel) level, pos, xp);
				}

				((ServerLevel) level).sendParticles(ParticleTypes.SWEEP_ATTACK, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), 1, 0, 0, 0, 0.0D);
				PacketHandler.sendPacket(player, new ClientboundBlockUpdatePacket(level, pos));
			}
		}
	}

	@Override
	public int getEnchantmentValue() {
		return 0;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		BlockState blockstate = level.getBlockState(blockpos);
		Player player = context.getPlayer();
		ItemStack itemstack = context.getItemInHand();

		Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = HoeItem.TILLABLES.get(level.getBlockState(blockpos).getBlock());
		int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(context);
		if (hook != 0) return hook > 0 ? InteractionResult.SUCCESS : InteractionResult.FAIL;
		if (context.getClickedFace() != Direction.DOWN && level.isEmptyBlock(blockpos.above())) {
			if (pair == null) {
				return InteractionResult.PASS;
			} else {
				Predicate<UseOnContext> predicate = pair.getFirst();
				Consumer<UseOnContext> consumer = pair.getSecond();
				if (predicate.test(context)) {
					level.playSound(player, blockpos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
					if (!level.isClientSide) {
						consumer.accept(context);
						if (player != null) {
							context.getItemInHand().hurtAndBreak(1, player, (p_150845_) -> {
								p_150845_.broadcastBreakEvent(context.getHand());
							});
						}
					}

					return InteractionResult.sidedSuccess(level.isClientSide);
				} else {
					return InteractionResult.PASS;
				}
			}
		}
		Optional<BlockState> optional = Optional.ofNullable(blockstate.getToolModifiedState(level, blockpos, player, context.getItemInHand(),
				net.minecraftforge.common.ToolActions.AXE_STRIP));
		if (optional.isPresent()) {
			level.playSound(player, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
			if (player instanceof ServerPlayer) {
				CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, blockpos, itemstack);
			}

			level.setBlock(blockpos, optional.get(), 11);
			if (player != null) {
				itemstack.hurtAndBreak(1, player, (player1) -> {
					player1.broadcastBreakEvent(context.getHand());
				});
			}

			return InteractionResult.sidedSuccess(level.isClientSide);
		}

		return InteractionResult.PASS;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return state.is(this.blocks) ? 14.0F : 1.0F;
	}

	public float getAttackDamage() {
		return this.itemTier.getAttackDamageBonus();
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.hurtAndBreak(1, attacker, (attackerEntity) -> attackerEntity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
		return true;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
		Multimap<Attribute, AttributeModifier> multimap = super.getDefaultAttributeModifiers(equipmentSlot);
		if (equipmentSlot == EquipmentSlot.MAINHAND) {
			if (multimap != null) {
				Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
				builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon damage modifier", (double) this.attackDamage, Operation.ADDITION));
				builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon speed modifier", -2.4000000953674316D, Operation.ADDITION));
				multimap = builder.build();
			}
		}
		return multimap;
	}
}
