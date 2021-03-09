package mrbysco.forcecraft.capablilities.banemodifier;

import net.minecraft.nbt.CompoundNBT;

import java.util.concurrent.Callable;

public class BaneFactory implements Callable<IBaneModifier> {
    @Override
    public IBaneModifier call() throws Exception {
        return new IBaneModifier() {

            boolean canTeleport = true;

            @Override
            public boolean canTeleport() {
                return canTeleport;
            }

            @Override
            public void setTeleportAbility(boolean canTeleport) {
                this.canTeleport = canTeleport;
            }

            @Override
            public CompoundNBT serializeNBT() {
                return null;
            }

            @Override
            public void deserializeNBT(CompoundNBT nbt) {

            }
        };
    }
}
