package com.mrbysco.forcecraft.items.flask;

import com.mrbysco.forcecraft.entities.projectile.FlaskEntity;
import com.mrbysco.forcecraft.items.BaseItem;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

/**
 * Credit to Buuz135 for the Mob Imprisonment Tool code <3
 */
public class ForceFlaskItem extends BaseItem {

    public ForceFlaskItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if(playerIn.isSneaking()) {
            worldIn.playSound((PlayerEntity)null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
            if (!worldIn.isRemote) {
                FlaskEntity flaskEntity = new FlaskEntity(worldIn, playerIn);
                flaskEntity.setItem(new ItemStack(ForceRegistry.ENTITY_FLASK.get()));
                flaskEntity.setDirectionAndMovement(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, -20.0F, 0.5F, 1.0F);
                worldIn.addEntity(flaskEntity);
            }

            playerIn.addStat(Stats.ITEM_USED.get(this));
            if (!playerIn.abilities.isCreativeMode) {
                itemstack.shrink(1);
            }
        }

        return ActionResult.func_233538_a_(itemstack, worldIn.isRemote());
    }

    @Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity entity, Hand hand) {
        World world = entity.world;
        if (world.isRemote)
            return ActionResultType.PASS;

        if(entity instanceof CowEntity && !entity.isChild()) {
            playerIn.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
            ItemStack milkStack = ForceRegistry.MILK_FORCE_FLASK.get().getDefaultInstance();
            if(!playerIn.inventory.addItemStackToInventory(milkStack)) {
                playerIn.entityDropItem(milkStack, 0F);
            }
            if(!playerIn.abilities.isCreativeMode)
                stack.shrink(1);

            return ActionResultType.SUCCESS;
        }
        return super.itemInteractionForEntity(stack, playerIn, entity, hand);
    }

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @javax.annotation.Nullable net.minecraft.nbt.CompoundNBT nbt) {
        return new FlaskFluidHandler(stack);
    }
}
