package mrbysco.forcecraft.potion.potions;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class PotionBleeding extends Effect {

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
}
