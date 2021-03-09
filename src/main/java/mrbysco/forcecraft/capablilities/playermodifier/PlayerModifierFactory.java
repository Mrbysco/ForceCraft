package mrbysco.forcecraft.capablilities.playermodifier;

import net.minecraft.entity.ai.attributes.Attributes;

import java.util.concurrent.Callable;

public class PlayerModifierFactory implements Callable<IPlayerModifier> {
    @Override
    public IPlayerModifier call() throws Exception {
        return new IPlayerModifier() {

            private float attackDamage = ((float) Attributes.ATTACK_DAMAGE.getDefaultValue());
            private float wingPower = 0.0f;
            private float flightCounter = wingPower;
            private float heatDamage = 0.0f;
            private float damage = attackDamage + heatDamage;
            private int luck;


            @Override
            public float getAttackDamage() {
                return attackDamage;
            }

            @Override
            public void setAttackDamage(float newDamage) {
                attackDamage = newDamage;
            }

            @Override
            public void addAttackDamage(float newDamage) {
                attackDamage += newDamage;
            }

            @Override
            public float getWingPower() {
                return wingPower;
            }

            @Override
            public void setWingPower(float newWingPower) {
                wingPower = newWingPower;
            }

            @Override
            public float getFlightTimer() {
                return flightCounter;
            }

            @Override
            public void subtractFlightTimer() {
                flightCounter--;
            }

            @Override
            public void setFlightTimer(float newFlightCounter) {
                flightCounter = newFlightCounter;
            }

            @Override
            public float getHeatDamage() {
                return heatDamage;
            }

            @Override
            public void setHeatDamage(float newDamage) {
                heatDamage = newDamage;
            }

            @Override
            public void addHeatDamage(float newDamage) {
                heatDamage += newDamage;
            }

            @Override
            public float getDamage() {
                return damage;
            }

            @Override
            public void setDamage(float newDamage) {
                damage = newDamage;
            }

            @Override
            public int getLuckLevel() {
                return luck;
            }

            @Override
            public void setLuckLevel(int newLuck) {
                luck = newLuck;
            }

            @Override
            public void incrementLuckLevel(int newLuck) {
                luck += newLuck;
            }

        };
    }
}
