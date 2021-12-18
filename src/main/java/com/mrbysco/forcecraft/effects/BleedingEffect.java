package com.mrbysco.forcecraft.effects;

import com.mrbysco.forcecraft.ForceCraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class BleedingEffect extends MobEffect {
    public BleedingEffect(){
        super(MobEffectCategory.HARMFUL, 0);
    }

    @Override
    public boolean isInstantenous() {
        return false;
    }

    @Override
    public boolean isBeneficial() {
        return false;
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

	@Override
	public void applyEffectTick(LivingEntity target, int amplifier) {
		//once per tick
		if(target.level.getGameTime() % 20 == 0) {
			target.hurt(ForceCraft.BLEEDING_DAMAGE, 2.0F);
		}
	}
}
