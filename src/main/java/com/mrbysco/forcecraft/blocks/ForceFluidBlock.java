package com.mrbysco.forcecraft.blocks;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.entities.IColdMob;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IForgeShearable;

import java.util.function.Supplier;

public class ForceFluidBlock extends FlowingFluidBlock {

    public ForceFluidBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
        super(supplier, properties);
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entityIn;

            if(livingEntity instanceof PlayerEntity) {
                ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(Effects.REGENERATION, 10, 0, false, false));
            } else {
                CreatureAttribute creatureAttribute = livingEntity.getCreatureAttribute();
                EntityClassification classification = livingEntity.getClassification(false);
                boolean secondPassed = worldIn.getGameTime() % 20 == 0;
                if(classification == EntityClassification.MONSTER) {
                    if(creatureAttribute == CreatureAttribute.UNDEAD && creatureAttribute == CreatureAttribute.UNDEFINED || creatureAttribute == CreatureAttribute.ARTHROPOD) {
                        if(worldIn.getGameTime() % 10 == 0) {
                            livingEntity.attackEntityFrom(ForceCraft.LIQUID_FORCE_DAMAGE, 1.0F);
                        }
                    }
                } else {
                    if(worldIn.getGameTime() % 10 == 0) {
                        livingEntity.heal(0.5F);
                    }
                    if(livingEntity instanceof IForgeShearable && secondPassed) {
                        if(RANDOM.nextInt(10) <= 3) {
                            if(livingEntity instanceof SheepEntity) {
                                ((SheepEntity)livingEntity).setSheared(false);
                            } else if(livingEntity instanceof IColdMob) {
                                IColdMob coldMob = (IColdMob) livingEntity;
                                coldMob.transformMob(livingEntity, worldIn);
                            }
                        }
                    }
                }
            }
        }
    }
}
