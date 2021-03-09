package mrbysco.forcecraft.potion.effects;

import mrbysco.forcecraft.registry.ForceEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;

public class EffectBleeding extends EffectInstance {

    public EffectBleeding(int duration) {
        super(ForceEffects.BLEEDING.get(), duration, 0, false, true);
    }

    @Override
    public void performEffect(LivingEntity entityIn) {
        entityIn.attackEntityFrom(DamageSource.GENERIC, 1);
    }
}
