package mrbysco.forcecraft.util;

import mrbysco.forcecraft.registry.ForceEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;

public class MobUtil {
    // 10 seconds at 20 ticks/sec
    private static final int BLEEDING_SECONDS = 20;

    public static void addBleedingEffect(int level, LivingEntity target){
        if(level >= 4){
            EffectInstance bleedingFour = new EffectInstance(ForceEffects.BLEEDING.get(), BLEEDING_SECONDS * 15, 0, false, true);
            target.addPotionEffect(bleedingFour);
        } else if(level == 3){
            EffectInstance bleedingThree = new EffectInstance(ForceEffects.BLEEDING.get(), BLEEDING_SECONDS * 10, 0, false, true);
            target.addPotionEffect(bleedingThree);
        } else if(level == 2){
            EffectInstance bleedingTwo = new EffectInstance(ForceEffects.BLEEDING.get(), BLEEDING_SECONDS * 5, 0, false, true);
            target.addPotionEffect(bleedingTwo);
        } else if(level == 1){
            EffectInstance bleedingOne = new EffectInstance(ForceEffects.BLEEDING.get(), BLEEDING_SECONDS * 2, 0, false, true);
            target.addPotionEffect(bleedingOne);
        }
    }
}
