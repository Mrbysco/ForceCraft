package mrbysco.forcecraft.blocks.infuser;

import mrbysco.forcecraft.ForceCraft;
import mrbysco.forcecraft.items.infuser.UpgradeBookData;
import net.minecraft.item.ItemStack;

public enum InfuserModifierType {
	SPEED,HEAT,FORCE,SILK,DAMAGE,FORTUNE,LIGHT,STURDY,LUMBERJACK,HEALING,ENDER,BLEEDING,BANE,WING,CAMO,RAINBOW,TIME,
      PACK1,PACK2,PACK3,PACK4, // 4 upgrades: this one upgrades the Force Pack 
      // (once per tier starting on tier 2, allowing full upgrading on tier 5)
	 GRINDING, FREEZING, EXP, STORAGE, // TODO: look up old mod version
	 
	  //- for chests somehow?, FURNACE - item card enchant
	 SIGHT, //- night vision on a force rod ?
	 TREASURE //,CORE - make things drop treasure cards - craft into spoils bag
	;
	
	public boolean apply(ItemStack tool, ItemStack mod, UpgradeBookData bd) {
		switch(this) {
		case DAMAGE: // claw item
			return InfuserTileEntity.addDamageModifier(tool);
		case ENDER:
			return InfuserTileEntity.addEnderModifier(tool);
		case FORCE:
			return InfuserTileEntity.addForceModifier(tool);
		case HEALING:
			return InfuserTileEntity.addHealingModifier(tool);
		case HEAT:
			return InfuserTileEntity.addHeatModifier(tool); 
		case LIGHT:
			return InfuserTileEntity.addLightModifier(tool);
		case FORTUNE:
			return InfuserTileEntity.addLuckModifier(tool);
		case LUMBERJACK:
			return InfuserTileEntity.addLumberjackModifier(tool);
		case SILK:
			return InfuserTileEntity.addSilkTouchModifier(tool);
		case SPEED:
			return InfuserTileEntity.addSpeedModifier(tool); 
		case STURDY:
			return InfuserTileEntity.addSturdyModifier(tool);
		case CAMO:
			return InfuserTileEntity.applyCamo(tool, mod);
		case BANE:
            return InfuserTileEntity.addBaneModifier(tool);
		case BLEEDING:
            return InfuserTileEntity.addBleedingModifier(tool);
		case WING:
            return InfuserTileEntity.addWingModifier(tool);
		case RAINBOW:
            return InfuserTileEntity.addRainbowModifier(tool);
		case TIME:
            return InfuserTileEntity.addTimeModifier(tool, mod);
		case TREASURE:
            return InfuserTileEntity.addTreasureModifier(tool);
		case PACK1:
		case PACK2:
		case PACK3:
		case PACK4:
			// logic of which pack upgrade to use is inside
            return InfuserTileEntity.upgradeBag(tool, bd);

		}
		ForceCraft.LOGGER.error("Error: No action for modifier {}", this);
		return false;
	}
	
	public String getTooltip() {
		return "gui.forcecraft.infuser.tooltip." + name().toLowerCase();
	}
}