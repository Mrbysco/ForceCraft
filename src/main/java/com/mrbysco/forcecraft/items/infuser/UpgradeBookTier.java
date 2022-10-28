package com.mrbysco.forcecraft.items.infuser;

public enum UpgradeBookTier {
	//zero is unused, but we want .ordinal() to match the meaning
	ZERO(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7);

	private final int tier;

	private UpgradeBookTier(int tier) {
		this.tier = tier;
	}

	//which is last one
	public static final UpgradeBookTier FINAL = SEVEN;

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
		if (nextVal >= FINAL.ordinal()) {
			return FINAL;
		}
		UpgradeBookTier nextTier = values()[nextVal];
//		ForceCraft.LOGGER.info("Upgrade tome item: Tier level up {}", nextTier);
		return nextTier;
	}

	public int asInt() {
		return tier;
	}
}