package mrbysco.forcecraft.util;

import mrbysco.forcecraft.potion.effects.EffectBleeding;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;

public class MobUtil {

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
