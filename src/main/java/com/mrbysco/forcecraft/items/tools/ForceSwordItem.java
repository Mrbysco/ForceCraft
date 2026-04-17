package com.mrbysco.forcecraft.items.tools;

import com.mrbysco.forcecraft.items.infuser.ForceToolData;
import com.mrbysco.forcecraft.items.infuser.IForceChargingTool;
import com.mrbysco.forcecraft.registry.material.ModToolTiers;
import com.mrbysco.forcecraft.util.TooltipUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static com.mrbysco.forcecraft.components.ForceComponents.TOOL_ENDER;
import static com.mrbysco.forcecraft.components.ForceComponents.TOOL_WING;

public class ForceSwordItem extends Item implements IForceChargingTool {

	public ForceSwordItem(Item.Properties properties) {
		super(properties.sword(ModToolTiers.FORCE, -2, -2.4F).enchantable(0));
	}

	@Override
	public InteractionResult use(Level level, Player playerIn, InteractionHand handIn) {
		ItemStack heldStack = playerIn.getItemInHand(handIn);
		//Wing Modifier
		if (heldStack.has(TOOL_WING)) {
			Vec3 vec = playerIn.getLookAngle();
			double wantedVelocity = 1.7;
			playerIn.setDeltaMovement(vec.x * wantedVelocity, vec.y * wantedVelocity, vec.z * wantedVelocity);
			heldStack.hurtAndBreak(1, playerIn, handIn.asEquipmentSlot());

			playerIn.getCooldowns().addCooldown(heldStack, 20);
			level.playSound((Player) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS,
					0.5F, 2.6F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.8F);
		}
		//Ender Modifier
		if (heldStack.has(TOOL_ENDER) && level instanceof ServerLevel serverLevel) {
			BlockHitResult traceResult = getPlayerPOVHitResult(level, playerIn, Fluid.NONE);
			BlockPos lookPos = traceResult.getBlockPos().relative(traceResult.getDirection());
			EntityTeleportEvent event = new EntityTeleportEvent(playerIn, serverLevel, lookPos.getX(), lookPos.getY(), lookPos.getZ());
			if (!NeoForge.EVENT_BUS.post(event).isCanceled()) {
				boolean flag2 = playerIn.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
				if (flag2 && !playerIn.isSilent()) {
					level.playSound((Player) null, playerIn.xo, playerIn.yo, playerIn.zo, SoundEvents.ENDERMAN_TELEPORT, playerIn.getSoundSource(), 1.0F, 1.0F);
					playerIn.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
					heldStack.hurtAndBreak(1, playerIn, handIn.asEquipmentSlot());
					playerIn.getCooldowns().addCooldown(heldStack, 10);
				}
			}
		}
		return super.use(level, playerIn, handIn);
	}

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

//	@Override
//	public int getEnchantmentValue() {
//		return 0;
//	}
//
//	@Override
//	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
//		return false;
//	}
}
