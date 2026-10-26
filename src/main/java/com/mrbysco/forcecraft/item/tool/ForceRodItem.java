package com.mrbysco.forcecraft.item.tool;

import com.mrbysco.forcecraft.config.ForceConfig;
import com.mrbysco.forcecraft.data.TimeMode;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceDatamaps;
import com.mrbysco.forcecraft.registry.ForceSounds;
import com.mrbysco.forcecraft.util.RodHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ARGB;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.UseCooldown;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class ForceRodItem extends Item {

	public ForceRodItem(Properties properties) {
		super(properties);
	}

	@Override
	public @NonNull InteractionResult use(Level level, Player player, @NonNull InteractionHand hand) {
		ItemStack heldStack = player.getItemInHand(hand);
		if (!level.isClientSide()) {
			if (heldStack.has(ForceComponents.TIME)) {
				TimeMode mode = heldStack.getOrDefault(ForceComponents.TIME_MODE, TimeMode.STOP);
				if (player.isShiftKeyDown()) {
					TimeMode nextMode = mode.getNext();
					heldStack.set(ForceComponents.TIME_MODE, nextMode);
					player.sendOverlayMessage(Component.translatable("forcecraft.component.time_mode", nextMode.getName()));
				}
				return InteractionResult.SUCCESS_SERVER;
			} else if (heldStack.has(ForceComponents.ENDER)) {
				if (!player.isShiftKeyDown()) {
					EnderData enderData = heldStack.get(ForceComponents.ENDER_DATA);
					if (enderData != null) {
						level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_TELEPORT, player.getSoundSource(), 1.0F, 1.0F);

						level.broadcastEntityEvent(player, EntityEvent.TELEPORT);
						player.teleportTo(enderData.x() + 0.5, enderData.y() + 0.5, enderData.z() + 0.5);
						level.broadcastEntityEvent(player, EntityEvent.TELEPORT);
						level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_TELEPORT, player.getSoundSource(), 1.0F, 1.0F);

						player.getCooldowns().addCooldown(heldStack, 20);
					}
				}
				return InteractionResult.SUCCESS_SERVER;
			} else if (!player.isShiftKeyDown() && RodHelper.applyBuffs(player, heldStack)) {
				player.getCooldowns().addCooldown(heldStack, 10);
				heldStack.hurtAndBreak(1, player, hand);
				return InteractionResult.SUCCESS_SERVER;
			}
		} else {
			if (player.isShiftKeyDown() && heldStack.has(ForceComponents.ENDER)) {
				if (player.isShiftKeyDown()) {
					com.mrbysco.forcecraft.client.screen.ChangeEnderPosScreen.openScreen(heldStack, hand);
				}
				return InteractionResult.SUCCESS;
			}
		}
		return super.use(level, player, hand);
	}

	@Override
	public @NonNull InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		ItemStack stack = context.getItemInHand();
		Player player = context.getPlayer();
		if (!level.isClientSide() && player != null && !player.isShiftKeyDown()) {
			BlockPos clickedPos = context.getClickedPos();

			if (RodHelper.isBasicRod(stack)) {
				BlockState state = level.getBlockState(clickedPos);
				Block block = state.getBlock();
				Block conversionBlock = block.builtInRegistryHolder().getData(ForceDatamaps.ROD_CONVERSIONS);
				if (conversionBlock != null) {
					level.setBlock(clickedPos, conversionBlock.defaultBlockState(), 3);
					stack.hurtAndBreak(1, player, context.getHand());
					((ServerLevel)level).sendParticles(
							ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, ARGB.opaque(0xfff755)),
							clickedPos.getX() + 0.5, clickedPos.getY() + 0.5, clickedPos.getZ() + 0.5,
							4, 0.25, 0.25, 0.25, 0.0);
					level.playSound(null,
							clickedPos.getX() + 0.5, clickedPos.getY() + 0.5, clickedPos.getZ() + 0.5,
							ForceSounds.FORCE_MAGIC.get(), player.getSoundSource(), 1.0F, 1.0F
					);
					return InteractionResult.SUCCESS_SERVER;
				}
			}

			if (stack.has(ForceComponents.TIME)) {
				TimeMode mode = stack.getOrDefault(ForceComponents.TIME_MODE, TimeMode.STOP);
				RodHelper.applyTimeField((ServerLevel) level, clickedPos, mode, player);
				spawnParticles((ServerLevel) level, clickedPos);
				player.getCooldowns().addCooldown(stack, ForceConfig.COMMON.timeCooldown.getAsInt());
				stack.hurtAndBreak(mode.getUsageCost(), player, context.getHand());
				return InteractionResult.SUCCESS_SERVER;
			}
		}
		return super.useOn(context);
	}

	private void spawnParticles(ServerLevel level, BlockPos pos) {
		int color = ARGB.opaque(0xE8C547);
		for (BlockPos target : BlockPos.betweenClosed(
				pos.offset(-2, -2, -2),
				pos.offset(2, 2, 2))) {
			level.sendParticles(
					ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, color),
					target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5,
					4, 0.25, 0.25, 0.25, 0.0);
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
		if (!stack.has(DataComponents.USE_COOLDOWN)) {
			Optional<Identifier> group = Optional.of(RodHelper.getRodCooldownGroup(stack));
			stack.set(DataComponents.USE_COOLDOWN, new UseCooldown(0.5F, group));
		}
		super.inventoryTick(stack, level, owner, slot);
	}

	@Override
	public @NonNull Component getName(ItemStack stack) {
		if (stack.has(ForceComponents.TIME)) {
			return Component.translatable(this.descriptionId + ".time").withStyle(ChatFormatting.LIGHT_PURPLE);
		}
		if (stack.has(ForceComponents.SPEED)) {
			return Component.translatable(this.descriptionId + ".speed" + stack.getOrDefault(ForceComponents.SPEED, 1));
		}
		if (stack.has(ForceComponents.HEALING)) {
			return Component.translatable(this.descriptionId + ".healing" + stack.getOrDefault(ForceComponents.HEALING, 1))
					.withStyle(ChatFormatting.AQUA);
		}
		if (stack.has(ForceComponents.HEAT)) {
			return Component.translatable(this.descriptionId + ".heat");
		}
		if (stack.has(ForceComponents.ENDER)) {
			return Component.translatable(this.descriptionId + ".ender").withStyle(ChatFormatting.LIGHT_PURPLE);
		}
		if (stack.has(ForceComponents.CAMO)) {
			return Component.translatable(this.descriptionId + ".camo").withStyle(ChatFormatting.LIGHT_PURPLE);
		}
		if (stack.has(ForceComponents.SIGHT)) {
			return Component.translatable(this.descriptionId + ".sight").withStyle(ChatFormatting.AQUA);
		}
		return super.getName(stack);
	}
}
