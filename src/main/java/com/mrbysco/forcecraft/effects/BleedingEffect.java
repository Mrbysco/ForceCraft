package com.mrbysco.forcecraft.effects;

import com.mrbysco.forcecraft.ForceCraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class BleedingEffect extends Effect {
    public BleedingEffect(){
        super(EffectType.HARMFUL, 0);
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
