package com.mrbysco.forcecraft.handler.infusion;

import com.mrbysco.forcecraft.config.ForceConfig;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.Set;

@EventBusSubscriber
public class EnderHandler {
	@SubscribeEvent
	public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
		ItemStack stack = event.getItemStack();
		Player player = event.getEntity();
		Level level = player.level();
		if (!stack.is(ForceRegistry.FORCE_ROD) && stack.has(ForceComponents.ENDER) && level instanceof ServerLevel serverLevel && player.isShiftKeyDown()) {
			InteractionHand hand = event.getHand();
			BlockHitResult traceResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
			BlockPos lookPos = traceResult.getBlockPos().relative(traceResult.getDirection());
			EntityTeleportEvent teleportEvent = new EntityTeleportEvent(player, serverLevel, lookPos.getX(), lookPos.getY(), lookPos.getZ());
			if (!NeoForge.EVENT_BUS.post(teleportEvent).isCanceled()) {
				boolean flag2 = player.teleportTo(serverLevel, teleportEvent.getTargetX(), teleportEvent.getTargetY(), teleportEvent.getTargetZ(), Set.of(), player.getYRot(), player.getXRot(), false);
				if (flag2 && !player.isSilent()) {
					level.playSound((Player) null, player.xo, player.yo, player.zo, SoundEvents.PLAYER_TELEPORT, player.getSoundSource(), 1.0F, 1.0F);
					player.playSound(SoundEvents.PLAYER_TELEPORT, 1.0F, 1.0F);
					stack.hurtAndBreak(1, player, hand);
					int cooldown = ForceConfig.COMMON.enderSwordCooldown.getAsInt();
					if (cooldown > 0)
						player.getCooldowns().addCooldown(stack, cooldown);
				}
			}
		}
	}

	public static BlockHitResult getPlayerPOVHitResult(Level level, Player player, ClipContext.Fluid fluid) {
		Vec3 from = player.getEyePosition();
		Vec3 to = from.add(player.calculateViewVector(player.getXRot(), player.getYRot()).scale(16));
		return level.clip(new ClipContext(from, to, ClipContext.Block.OUTLINE, fluid, player));
	}
}
