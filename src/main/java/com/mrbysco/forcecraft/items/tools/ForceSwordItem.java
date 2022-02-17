package com.mrbysco.forcecraft.items.tools;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.capablilities.toolmodifier.IToolModifier;
import com.mrbysco.forcecraft.capablilities.toolmodifier.ToolModCapability;
import com.mrbysco.forcecraft.items.infuser.ForceToolData;
import com.mrbysco.forcecraft.items.infuser.IForceChargingTool;
import com.mrbysco.forcecraft.registry.material.ModToolTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.EntityTeleportEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class ForceSwordItem extends SwordItem implements IForceChargingTool {

    public ForceSwordItem(Item.Properties properties) {
        super(ModToolTiers.FORCE, -2, -2.4F, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
        ItemStack heldStack = playerIn.getItemInHand(handIn);
		IToolModifier toolCap = heldStack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
		if(toolCap != null) {
			//Wing Modifier
			if(toolCap.hasWing()) {
				Vec3 vec = playerIn.getLookAngle();
				double wantedVelocity = 1.7;
				playerIn.setDeltaMovement(vec.x * wantedVelocity,vec.y * wantedVelocity,vec.z * wantedVelocity);
				heldStack.hurtAndBreak(1, playerIn, (player) -> player.broadcastBreakEvent(handIn));
				playerIn.getCooldowns().addCooldown(this, 20);
				level.playSound((Player)null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);
			}
			//Ender Modifier
			if(toolCap.hasEnder()) {
				BlockHitResult traceResult = getPlayerPOVHitResult(level, playerIn, Fluid.NONE);
				BlockPos lookPos = traceResult.getBlockPos().relative(traceResult.getDirection());
				EntityTeleportEvent event = new EntityTeleportEvent(playerIn, lookPos.getX(), lookPos.getY(), lookPos.getZ());
				if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) {
					boolean flag2 = playerIn.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
					if (flag2 && !playerIn.isSilent()) {
						level.playSound((Player)null, playerIn.xo, playerIn.yo, playerIn.zo, SoundEvents.ENDERMAN_TELEPORT, playerIn.getSoundSource(), 1.0F, 1.0F);
						playerIn.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
						heldStack.hurtAndBreak(1, playerIn, (player) -> player.broadcastBreakEvent(handIn));
						playerIn.getCooldowns().addCooldown(this, 10);
					}
				}
			}
		}
        return super.use(level, playerIn, handIn);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if(CAPABILITY_TOOLMOD == null) {
            return null;
        }
        return new ToolModCapability();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lores, TooltipFlag flagIn) {
    	ForceToolData fd = new ForceToolData(stack);
    	fd.attachInformation(lores);
    	ToolModCapability.attachInformation(stack, lores);
        super.appendHoverText(stack, level, lores, flagIn);
    }

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return this.damageItem(stack,amount);
	}

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    // ShareTag for server->client capability data sync
    @Override
    public CompoundTag getShareTag(ItemStack stack) {
    	CompoundTag nbt = super.getShareTag(stack);
    	
		IToolModifier cap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
		if(cap != null) {
			CompoundTag shareTag = ToolModCapability.writeNBT(cap);
			nbt.put(Reference.MOD_ID, shareTag);
		}
        return nbt;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
    	if(nbt == null || !nbt.contains(Reference.MOD_ID)) {
    		return;
    	}

		IToolModifier cap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
		if(cap != null) {
			CompoundTag shareTag = nbt.getCompound(Reference.MOD_ID);
			ToolModCapability.readNBT(cap, shareTag);
		}
		super.readShareTag(stack, nbt);
	}
}
