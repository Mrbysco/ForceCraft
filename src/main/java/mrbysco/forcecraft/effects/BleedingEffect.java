package mrbysco.forcecraft.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

public class BleedingEffect extends Effect {

    public BleedingEffect(){
        super(EffectType.HARMFUL, 0);
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    @Override
    public boolean isBeneficial() {
        return false;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

	@Override
	public void performEffect(LivingEntity target, int amplifier) {
		//once per tick
		if(target.world.getGameTime() % 20 == 0) {
			// TODO: we could make a custom damage source
			target.attackEntityFrom(DamageSource.MAGIC, 2.0F);
		}
	}
}
