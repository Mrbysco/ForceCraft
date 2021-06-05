package mrbysco.forcecraft.items.tools;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.capablilities.toolmodifier.IToolModifier;
import mrbysco.forcecraft.capablilities.toolmodifier.ToolModProvider;
import mrbysco.forcecraft.capablilities.toolmodifier.ToolModStorage;
import mrbysco.forcecraft.items.infuser.ForceToolData;
import mrbysco.forcecraft.items.infuser.IForceChargingTool;
import mrbysco.forcecraft.registry.material.ModToolMaterial;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class ForceSwordItem extends SwordItem implements IForceChargingTool {

    public ForceSwordItem(Item.Properties properties) {
        super(ModToolMaterial.FORCE, -2, -2.4F, properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack heldStack = playerIn.getHeldItem(handIn);
		IToolModifier toolCap = heldStack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
		if(toolCap != null) {
			//Wing Modifier
			if(toolCap.hasWing()) {
				Vector3d vec = playerIn.getLookVec();
				double wantedVelocity = 1.7;
				playerIn.setMotion(vec.x * wantedVelocity,vec.y * wantedVelocity,vec.z * wantedVelocity);
				worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
				heldStack.damageItem(1, playerIn, (player) -> player.sendBreakAnimation(handIn));
				playerIn.getCooldownTracker().setCooldown(this, 20);
			}
			//Ender Modifier
			if(toolCap.hasEnder()) {
				BlockRayTraceResult traceResult = rayTrace(worldIn, playerIn, FluidMode.NONE);
				BlockPos lookPos = traceResult.getPos().offset(traceResult.getFace());
				net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(playerIn, lookPos.getX(), lookPos.getY(),
						lookPos.getZ(), 0);
				if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) {
					boolean flag2 = playerIn.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
					if (flag2 && !playerIn.isSilent()) {
						worldIn.playSound((PlayerEntity)null, playerIn.prevPosX, playerIn.prevPosY, playerIn.prevPosZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, playerIn.getSoundCategory(), 1.0F, 1.0F);
						playerIn.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
						heldStack.damageItem(1, playerIn, (player) -> player.sendBreakAnimation(handIn));
						playerIn.getCooldownTracker().setCooldown(this, 10);
					}
				}
			}
		}
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if(CAPABILITY_TOOLMOD == null) {
            return null;
        }
        return new ToolModProvider();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> lores, ITooltipFlag flagIn) {
    	ForceToolData fd = new ForceToolData(stack);
    	fd.attachInformation(lores);
    	ToolModStorage.attachInformation(stack, lores);
        super.addInformation(stack, worldIn, lores, flagIn);
    }

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return this.damageItem(stack,amount);
	}

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    // ShareTag for server->client capability data sync
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
    	CompoundNBT nbt = super.getShareTag(stack);
    	
		IToolModifier cap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
		if(cap != null) {
			CompoundNBT shareTag = ToolModStorage.serializeNBT(cap);
			nbt.put(Reference.MOD_ID, shareTag);
		}
        return nbt;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
    	if(nbt == null || !nbt.contains(Reference.MOD_ID)) {
    		return;
    	}

		IToolModifier cap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
		if(cap != null) {
	    	INBT shareTag = nbt.get(Reference.MOD_ID);
	    	ToolModStorage.deserializeNBT(cap, shareTag);
		}
		super.readShareTag(stack, nbt);
	}
}
