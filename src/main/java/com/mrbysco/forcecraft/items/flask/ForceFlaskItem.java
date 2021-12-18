package com.mrbysco.forcecraft.items.flask;

import com.mrbysco.forcecraft.entities.projectile.FlaskEntity;
import com.mrbysco.forcecraft.items.BaseItem;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Credit to Buuz135 for the Mob Imprisonment Tool code <3
 */
public class ForceFlaskItem extends BaseItem {

    public ForceFlaskItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if(playerIn.isShiftKeyDown()) {
            worldIn.playSound((Player)null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.SPLASH_POTION_THROW,
                    SoundSource.NEUTRAL, 0.5F, 0.4F / (worldIn.random.nextFloat() * 0.4F + 0.8F));
            if (!worldIn.isClientSide) {
                FlaskEntity flaskEntity = new FlaskEntity(worldIn, playerIn);
                flaskEntity.setItem(new ItemStack(ForceRegistry.ENTITY_FLASK.get()));
                flaskEntity.shootFromRotation(playerIn, playerIn.xRotO, playerIn.getYRot(), -20.0F, 0.5F, 1.0F);
                worldIn.addFreshEntity(flaskEntity);
            }

            playerIn.awardStat(Stats.ITEM_USED.get(this));
            if (!playerIn.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
        }

        return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide());
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity entity, InteractionHand hand) {
        Level world = entity.level;
        if (world.isClientSide)
            return InteractionResult.PASS;

        if(entity instanceof Cow && !entity.isBaby()) {
            playerIn.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            ItemStack milkStack = ForceRegistry.MILK_FORCE_FLASK.get().getDefaultInstance();
            if(!playerIn.getInventory().add(milkStack)) {
                playerIn.spawnAtLocation(milkStack, 0F);
            }
            if(!playerIn.getAbilities().instabuild)
                stack.shrink(1);

            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, playerIn, entity, hand);
    }

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @javax.annotation.Nullable net.minecraft.nbt.CompoundTag nbt) {
        return new FlaskFluidHandler(stack);
    }
}
