package mrbysco.forcecraft.util;

import mrbysco.forcecraft.potion.effects.EffectBleeding;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.CreeperSwellGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;

public class MobUtil {

    public static void removeCreeperExplodeTask(CreeperEntity entity) {
        entity.goalSelector.goals.removeIf(goal -> {
            boolean flag = goal.getGoal() instanceof CreeperSwellGoal || goal.getGoal() instanceof MeleeAttackGoal;
            if(flag) {
                entity.setCreeperState(-1);
            }
            return flag;
        });

        entity.targetSelector.goals.removeIf(goal -> {
            boolean flag = goal.getGoal() instanceof NearestAttackableTargetGoal;
            if(flag) {
                entity.setCreeperState(-1);
            }
            return flag;
        });
    }

    public static void addBleedingEffect(ItemStack stack, LivingEntity target){

    }

    public static void addBleedingEffect(int level, LivingEntity target){
        EffectInstance bleedingOne = new EffectBleeding(2);
        EffectInstance bleedingTwo = new EffectBleeding(4);
        EffectInstance bleedingThree = new EffectBleeding(5);
        EffectInstance bleedingFour = new EffectBleeding(16);


        if(level == 4){
            target.addPotionEffect(bleedingFour);
        }

        else if(level == 3){
            target.addPotionEffect(bleedingThree);
        }

        else if(level == 2){
            target.addPotionEffect(bleedingTwo);
        }

        else if(level == 1){
            target.addPotionEffect(bleedingOne);
        }
    }
}
