package com.mrbysco.forcecraft.blockentities.energy;

import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class ForceEnergyStorage extends SimpleEnergyHandler {

	public ForceEnergyStorage(int capacity, int maxReceive) {
		super(capacity, maxReceive, maxReceive);
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	//use extractEnergy but always in simulate == false
	//make sure energy stays non-negative
	public void consumePower(int energy) {
		try (Transaction tx = Transaction.openRoot()) {
			if (this.extract(energy, tx) == energy) {
				tx.commit();
			}
		}
	}
}