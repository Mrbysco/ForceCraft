package com.mrbysco.forcecraft.blockentities;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.capabilities.forcerod.IForceRodModifier;
import com.mrbysco.forcecraft.capabilities.toolmodifier.IToolModifier;
import com.mrbysco.forcecraft.config.ConfigHandler;
import com.mrbysco.forcecraft.items.ForceArmorItem;
import com.mrbysco.forcecraft.items.infuser.UpgradeBookData;
import com.mrbysco.forcecraft.items.tools.ForceAxeItem;
import com.mrbysco.forcecraft.items.tools.ForceBowItem;
import com.mrbysco.forcecraft.items.tools.ForcePickaxeItem;
import com.mrbysco.forcecraft.items.tools.ForceRodItem;
import com.mrbysco.forcecraft.items.tools.ForceShovelItem;
import com.mrbysco.forcecraft.items.tools.ForceSwordItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static com.mrbysco.forcecraft.capabilities.CapabilityHandler.CAPABILITY_FORCEROD;
import static com.mrbysco.forcecraft.capabilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public enum InfuserModifierType {
	SPEED, HEAT, FORCE, SILK, DAMAGE, FORTUNE, LIGHT, STURDY, LUMBERJACK, HEALING, ENDER, BLEEDING, BANE, WING, CAMO, RAINBOW, TIME,
	// pack is for upgrades, item gives crafting results
	PACK1, PACK2, PACK3, PACK4, GRINDING, FREEZING, EXP, STORAGE, SIGHT, TREASURE, ITEM;

	public boolean apply(ItemStack tool, ItemStack mod, UpgradeBookData bd) {
		switch (this) {
			case DAMAGE: // claw item
				return InfuserBlockEntity.addDamageModifier(tool);
			case ENDER:
				return InfuserBlockEntity.addEnderModifier(tool);
			case FORCE:
				return InfuserBlockEntity.addForceModifier(tool);
			case HEALING:
				return InfuserBlockEntity.addHealingModifier(tool);
			case HEAT:
				return InfuserBlockEntity.addHeatModifier(tool);
			case LIGHT:
				return InfuserBlockEntity.addLightModifier(tool);
			case FORTUNE:
				return InfuserBlockEntity.addLuckModifier(tool);
			case LUMBERJACK:
				return InfuserBlockEntity.addLumberjackModifier(tool);
			case SILK:
				return InfuserBlockEntity.addSilkTouchModifier(tool);
			case SPEED:
				return InfuserBlockEntity.addSpeedModifier(tool);
			case STURDY:
				return InfuserBlockEntity.addSturdyModifier(tool);
			case CAMO:
				return InfuserBlockEntity.applyCamo(tool, mod);
			case BANE:
				return InfuserBlockEntity.addBaneModifier(tool);
			case BLEEDING:
				return InfuserBlockEntity.addBleedingModifier(tool);
			case WING:
				return InfuserBlockEntity.addWingModifier(tool);
			case RAINBOW:
				return InfuserBlockEntity.addRainbowModifier(tool);
			case TIME:
				break;
			case TREASURE:
				return InfuserBlockEntity.addTreasureModifier(tool);
			case PACK1:
			case PACK2:
			case PACK3:
			case PACK4:
				// logic of which pack upgrade to use is inside
				return InfuserBlockEntity.upgradeBag(tool, bd);
			case ITEM:
//			ForceCraft.LOGGER.info("i need to give you item {}", this);

				return true;
			case EXP:
				break;
			case FREEZING:
				return InfuserBlockEntity.addFreezingModifier(tool);
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
				if (item instanceof ForceSwordItem || item instanceof ForceBowItem) {
					IToolModifier modifierCap = centerStack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
					if (modifierCap != null) {
						return ConfigHandler.COMMON.damageCap.get();
					}
				} else if (item instanceof ForceArmorItem) {
					IToolModifier modifierCap = centerStack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
					if (modifierCap != null) {
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
				if (item instanceof ForceSwordItem || item instanceof ForceAxeItem) {
					IToolModifier modifierCap = centerStack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
					if (modifierCap != null) {
						return ConfigHandler.COMMON.forceCap.get();
					}
				}
				break;
			case HEALING:
				if (item instanceof ForceRodItem) {
					IForceRodModifier rodCap = centerStack.getCapability(CAPABILITY_FORCEROD).orElse(null);
					if (rodCap != null) {
						return ConfigHandler.COMMON.healingCap.get();
					}
				}
				break;
			case FORTUNE:
				return ConfigHandler.COMMON.luckCap.get();
			case SPEED:
				if (item instanceof ForceShovelItem || item instanceof ForcePickaxeItem || item instanceof ForceAxeItem) {
					IToolModifier modifierCap = centerStack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
					if (modifierCap != null) {
						return ConfigHandler.COMMON.speedCap.get();
					}
				} else if (item instanceof ForceBowItem) {
					IToolModifier modifierCap = centerStack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
					if (modifierCap != null) {
						return 1;
					}
				} else if (item instanceof ForceArmorItem) {
					IToolModifier modifierCap = centerStack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
					if (modifierCap != null) {
						return 1;
					}
				} else if (item instanceof ForceRodItem) {
					IForceRodModifier rodCap = centerStack.getCapability(CAPABILITY_FORCEROD).orElse(null);
					if (rodCap != null) {
						return ConfigHandler.COMMON.rodSpeedCap.get();
					}
				}
				break;
			case STURDY:
				if (item instanceof ForceSwordItem || item instanceof ForceAxeItem || item instanceof ForceShovelItem || item instanceof ForcePickaxeItem || item instanceof ForceRodItem) {
					IToolModifier modifierCap = centerStack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
					if (modifierCap != null) {
						return ConfigHandler.COMMON.sturdyToolCap.get();
					}
				} else if (item instanceof ForceArmorItem) {
					IToolModifier modifierCap = centerStack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
					if (modifierCap != null) {
						return 1;
					}
				}
				break;
			case BLEEDING:
				if (item instanceof ForceSwordItem || item instanceof ForceBowItem || item instanceof ForceArmorItem) {
					IToolModifier modifierCap = centerStack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
					if (modifierCap != null) {
						return ConfigHandler.COMMON.bleedCap.get();
					}
				}
				break;
			case TIME:
				if (ConfigHandler.COMMON.timeTorchEnabled.get()) {
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