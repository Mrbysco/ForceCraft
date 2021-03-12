package mrbysco.forcecraft.blocks.infuser;

import mrbysco.forcecraft.ForceCraft;
import net.minecraft.item.ItemStack;

public enum InfuserModifierType {
	SPEED, HEAT,FORCE,SILK,DAMAGE,FORTUNE,LIGHT,STURDY,LUMBERJACK,HEALING,ENDER,BLEEDING,BANE,WING,CAMO,
	RAINBOW,TIME;
	
	public boolean apply(ItemStack tool, ItemStack mod) {
		switch(this) {
		case DAMAGE:
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
		}
		ForceCraft.LOGGER.error("Error: No action for modifier {}", this);
		return false;
	}
}