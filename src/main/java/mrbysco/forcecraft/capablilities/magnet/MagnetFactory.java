package mrbysco.forcecraft.capablilities.magnet;

import net.minecraft.nbt.CompoundNBT;

import java.util.concurrent.Callable;

public class MagnetFactory implements Callable<IMagnet> {
    @Override
    public IMagnet call() throws Exception {
        return new IMagnet() {
            boolean active;

            @Override
            public boolean isActivated() {
                return active;
            }

            @Override
            public void activate() {
                active = true;
            }

            @Override
            public void deactivate() {
                active = false;
            }

            @Override
            public void setActivation(boolean active) {
                this.active = active;
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
