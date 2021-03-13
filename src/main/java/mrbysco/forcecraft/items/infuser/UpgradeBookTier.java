package mrbysco.forcecraft.items.infuser;

import mrbysco.forcecraft.ForceCraft;

public enum UpgradeBookTier {
	
	ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN;
	
	//which is last one
	static final UpgradeBookTier FINAL = SEVEN;
	
	 //at tier 7 is where OG would have the /dartcraftishard command giving you a book saying 'Mastered!'
	public int pointsForLevelup() {
		// each level has a different requirement 
		// we can rebalance this at any time
		
		return 96 + this.ordinal() * 4;
	}

	public UpgradeBookTier incrementTier() {
		// return a NEW tier the level after this one
		// do not go beyond the max
		int nextVal = this.ordinal() + 1;
		if(nextVal >= FINAL.ordinal()) {
			return FINAL;
		}
		UpgradeBookTier nextTier = values()[nextVal];
		ForceCraft.LOGGER.info("upgrade tome item: Tier level up {}", nextTier);
		return nextTier;
	}
	
	
}