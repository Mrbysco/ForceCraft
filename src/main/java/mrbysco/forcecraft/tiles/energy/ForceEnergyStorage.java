package mrbysco.forcecraft.tiles.energy;

import net.minecraftforge.energy.EnergyStorage;

public class ForceEnergyStorage extends EnergyStorage {

    public ForceEnergyStorage(int capacity, int maxReceive) {
        super(capacity, maxReceive, maxReceive);
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    //use extractEnergy but always in simulate == false
    //make sure energy stays non-negative
    public void consumePower(int energy) {
        this.extractEnergy(energy, false);

        if(this.energy < 0) {
            this.energy = 0;
        }
    }
}