package mrbysco.forcecraft.blocks.infuser;

import mrbysco.forcecraft.ForceCraft;
import mrbysco.forcecraft.capablilities.forcerod.IForceRodModifier;
import mrbysco.forcecraft.capablilities.toolmodifier.IToolModifier;
import mrbysco.forcecraft.config.ConfigHandler;
import mrbysco.forcecraft.items.ForceArmorItem;
import mrbysco.forcecraft.items.infuser.UpgradeBookData;
import mrbysco.forcecraft.items.tools.ForceAxeItem;
import mrbysco.forcecraft.items.tools.ForceBowItem;
import mrbysco.forcecraft.items.tools.ForcePickaxeItem;
import mrbysco.forcecraft.items.tools.ForceRodItem;
import mrbysco.forcecraft.items.tools.ForceShovelItem;
import mrbysco.forcecraft.items.tools.ForceSwordItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_FORCEROD;
import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public enum InfuserModifierType {
	SPEED, HEAT, FORCE, SILK, DAMAGE, FORTUNE, LIGHT, STURDY, LUMBERJACK, HEALING, ENDER, BLEEDING, BANE, WING, CAMO, RAINBOW, TIME, 
	// pack is for upgrades, item gives crafting results
	PACK1, PACK2, PACK3, PACK4, GRINDING, FREEZING, EXP, STORAGE, SIGHT, TREASURE, ITEM;

	public boolean apply(ItemStack tool, ItemStack mod, UpgradeBookData bd) {
		switch (this) {
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
			break;
		case TREASURE:
			return InfuserTileEntity.addTreasureModifier(tool);
		case PACK1:
		case PACK2:
		case PACK3:
		case PACK4:
			// logic of which pack upgrade to use is inside
			return InfuserTileEntity.upgradeBag(tool, bd);
		case ITEM:
			ForceCraft.LOGGER.info("i need to give you item {}", this);

			return true;
		case EXP:
			break;
		case FREEZING:
			return InfuserTileEntity.addFreezingModifier(tool);
		case GRINDING:
			break;
		case SIGHT:
			break;
		case STORAGE:
			break;
		}
		ForceCraft.LOGGER.error("Error: No action for modifier {}", this);
		return false;
	}

	public int getLevelCap(ItemStack centerStack) {
		Item item = centerStack.getItem();
		switch (this) {
			case DAMAGE:
				if(item instanceof ForceSwordItem || item instanceof ForceBowItem) {
					IToolModifier modifierCap = centerStack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
					if(modifierCap != null ) {
						return ConfigHandler.COMMON.damageCap.get();
					}
				} else if (item instanceof ForceArmorItem) {
					IToolModifier modifierCap = centerStack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
					if(modifierCap != null ) {
						return 1;
					}
				}
				break;
			case ENDER:
			case HEAT:
			case LIGHT:
			case WING:
			case RAINBOW:
			case LUMBERJACK:
			case SILK:
			case CAMO:
			case TREASURE:
			case BANE:
			case FREEZING:
			case EXP:
			case GRINDING:
			case SIGHT:
			case STORAGE:
			case ITEM:
			case PACK1:
			case PACK2:
			case PACK3:
			case PACK4:
				return 1;
			case FORCE:
				if(item instanceof ForceSwordItem || item instanceof ForceAxeItem) {
					IToolModifier modifierCap = centerStack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
					if(modifierCap != null ) {
						return ConfigHandler.COMMON.forceCap.get();
					}
				}
				break;
			case HEALING:
				if (item instanceof ForceRodItem) {
					IForceRodModifier rodCap = centerStack.getCapability(CAPABILITY_FORCEROD).orElse(null);
					if(rodCap != null) {
						return ConfigHandler.COMMON.healingCap.get();
					}
				}
				break;
			case FORTUNE:
				return ConfigHandler.COMMON.luckCap.get();
			case SPEED:
				if(item instanceof ForceShovelItem || item instanceof ForcePickaxeItem || item instanceof ForceAxeItem) {
					IToolModifier modifierCap = centerStack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
					if(modifierCap != null ) {
						return ConfigHandler.COMMON.speedCap.get();
					}
				} else if (item instanceof ForceBowItem) {
					IToolModifier modifierCap = centerStack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
					if(modifierCap != null ) {
						return 1;
					}
				}
				else if (item instanceof ForceArmorItem) {
					IToolModifier modifierCap = centerStack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
					if(modifierCap != null ) {
						return 1;
					}
				}
				else if (item instanceof ForceRodItem) {
					IForceRodModifier rodCap = centerStack.getCapability(CAPABILITY_FORCEROD).orElse(null);
					if(rodCap != null) {
						return ConfigHandler.COMMON.rodSpeedCap.get();
					}
				}
				break;
			case STURDY:
				if(item instanceof ForceSwordItem || item instanceof ForceAxeItem || item instanceof ForceShovelItem || item instanceof ForcePickaxeItem || item instanceof ForceRodItem) {
					IToolModifier modifierCap = centerStack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
					if(modifierCap != null ) {
						return ConfigHandler.COMMON.sturdyToolCap.get();
					}
				}
				else if (item instanceof ForceArmorItem) {
					IToolModifier modifierCap = centerStack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
					if(modifierCap != null ) {
						return 1;
					}
				}
				break;
			case BLEEDING:
				if(item instanceof ForceSwordItem || item instanceof ForceBowItem || item instanceof ForceArmorItem) {
					IToolModifier modifierCap = centerStack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
					if(modifierCap != null) {
						return ConfigHandler.COMMON.bleedCap.get();
					}
				}
				break;
			case TIME:
				if(ConfigHandler.COMMON.timeTorchEnabled.get()) {
					return 1;
				}
				break;
		}

		return 0;
	}

	public String getTooltip() {
		return "gui.forcecraft.infuser.tooltip." + name().toLowerCase();
	}
}