package mrbysco.forcecraft.potion.potions;

import mrbysco.forcecraft.potion.effects.TickableEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PotionBleeding extends TickableEffect {

    public PotionBleeding(){
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
	public void tick(LivingUpdateEvent event) {
		LivingEntity target = event.getEntityLiving();
		
		//once per tick
		if(target.world.getGameTime() % 20 == 0) {
			// TODO: we could make a custom damage source
			target.attackEntityFrom(DamageSource.MAGIC, 0.5F);
		}
	}
}
