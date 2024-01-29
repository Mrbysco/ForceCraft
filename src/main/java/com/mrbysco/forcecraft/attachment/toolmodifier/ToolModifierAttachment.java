package com.mrbysco.forcecraft.attachment.toolmodifier;

import com.mrbysco.forcecraft.items.ForceArmorItem;
import com.mrbysco.forcecraft.items.tools.ForceBowItem;
import com.mrbysco.forcecraft.items.tools.ForcePickaxeItem;
import com.mrbysco.forcecraft.items.tools.ForceRodItem;
import com.mrbysco.forcecraft.items.tools.ForceShearsItem;
import com.mrbysco.forcecraft.items.tools.ForceShovelItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.List;

import static com.mrbysco.forcecraft.attachment.CapabilityHandler.TOOL_MODIFIER;

public class ToolModifierAttachment implements IToolModifier, INBTSerializable<CompoundTag> {
	/**
	 * Modifier: Speed
	 * Item: Sugar
	 * Levels: 5
	 * Effect: Gives Player Haste [Level] when holding the tool
	 */

	private int speed;

	@Override
	public int getSpeedLevel() {
		return speed;
	}

	@Override
	public void incrementSpeed() {
		speed++;
	}

	@Override
	public void setSpeed(int newSpeed) {
		speed = newSpeed;
	}

	/**
	 * Modifier Heat
	 * Item: Golden Power Source
	 * Levels: 1
	 * Effect: Auto-Smelt Item drops
	 */

	private boolean heat;

	@Override
	public boolean hasHeat() {
		return heat;
	}

	@Override
	public void setHeat(boolean val) {
		heat = val;
	}

	/**
	 * Modifier: Force
	 * Item: Force Nugget
	 * Levels: 3
	 * Effect: Gives the Sword Knockback
	 */

	private int force;

	@Override
	public int getForceLevel() {
		return force;
	}

	@Override
	public boolean hasForce() {
		return force != 0;
	}

	@Override
	public void incrementForce() {
		force++;
	}

	@Override
	public void setForce(int newForce) {
		force = newForce;
	}

	/**
	 * Modifier Silk
	 * Item: Web
	 * Levels: 1
	 * Effect: Give Pick/Shovel/Axe Silk Touch
	 */

	private boolean silk;

	@Override
	public boolean hasSilk() {
		return silk;
	}

	@Override
	public void setSilk(boolean val) {
		silk = val;
	}

	/**
	 * Modifier: Sharpness
	 * Item: Claw
	 * Levels: 10
	 * Effect: Adds Sharpness to Force Sword
	 */

	private int sharpness;

	@Override
	public int getSharpLevel() {
		return sharpness;
	}

	@Override
	public boolean hasSharp() {
		return sharpness > 0;
	}

	@Override
	public void incrementSharp() {
		sharpness++;
	}

	@Override
	public void setSharp(int newSharp) {
		sharpness = newSharp;
	}

	/**
	 * Modifier: Luck
	 * Item: Fortune
	 * Levels: 5
	 * Effect: Adds Fortune to a tool or Looting to a sword
	 */

	private int luck;

	@Override
	public int getLuckLevel() {
		return luck;
	}

	@Override
	public boolean hasLuck() {
		return luck > 0;
	}

	@Override
	public void incrementLuck() {
		luck++;
	}

	@Override
	public void setLuck(int newLuck) {
		luck = newLuck;
	}

	/**
	 * Modifier: Sturdy
	 * Item: Bricks/Obsidian
	 * Levels: 3 (tools) 1 (armor)
	 * Effect: Adds 1 Level of Unbreaking to tool up to 10
	 */

	private int sturdy;

	@Override
	public int getSturdyLevel() {
		return sturdy;
	}

	@Override
	public boolean hasSturdy() {
		return sturdy > 0;
	}

	@Override
	public void incrementSturdy() {
		sturdy++;
	}

	@Override
	public void setSturdy(int newSturdy) {
		sturdy = newSturdy;
	}

	/**
	 * Modifier: Rainbow
	 * Items: Lapis Lazuli
	 * Levels: 1
	 * Effect: Makes sheep drop a random amount of colored wool
	 */

	boolean rainbow;

	@Override
	public boolean hasRainbow() {
		return rainbow;
	}

	@Override
	public void setRainbow(boolean val) {
		rainbow = val;
	}

	/**
	 * Modifier: Lumberjack
	 * Items: Force Log
	 * Levels: 1
	 * Effect: Allows an axe to chop an entire tree down
	 */

	boolean lumberJack;

	@Override
	public boolean hasLumberjack() {
		return lumberJack;
	}

	@Override
	public void setLumberjack(boolean val) {
		lumberJack = val;
	}

	/**
	 * Modifier: Bleeding
	 * Items: Arrow
	 * Levels: 2
	 * Effect: Applies Bleeding Potion Effect
	 */

	int bleed;

	@Override
	public int getBleedLevel() {
		return bleed;
	}

	@Override
	public boolean hasBleed() {
		return bleed > 0;
	}

	@Override
	public void incrementBleed() {
		bleed++;
	}

	@Override
	public void setBleed(int newBleed) {
		bleed = newBleed;
	}

	/**
	 * Modifier: Bane
	 * Items: Spider Eye
	 * Levels: 4
	 * Effect: Applies Bane Potion Effect
	 */

	int bane;

	@Override
	public int getBaneLevel() {
		return bane;
	}

	@Override
	public boolean hasBane() {
		return bane > 0;
	}

	@Override
	public void incrementBane() {
		bane++;
	}

	@Override
	public void setBane(int newBane) {
		bane = newBane;
	}

	/**
	 * Modifier: Wing
	 * Items: Feathers
	 * Levels: 1
	 * Effect: If full armor set is equipped, player can fly
	 */

	boolean wing;

	@Override
	public boolean hasWing() {
		return wing;
	}

	@Override
	public void setWing(boolean val) {
		wing = val;
	}

	/**
	 * Modifier: Camo
	 * Items: Invisibility Potion
	 * Levels: 1
	 * Effect: Gives Invisibility to wearer/user
	 */

	boolean camo;

	@Override
	public boolean hasCamo() {
		return camo;
	}

	@Override
	public void setCamo(boolean val) {
		camo = val;
	}

	/**
	 * Modifier: Sight
	 * Items: Night Vision Potion
	 * Levels: 1
	 * Effect: Gives Night Vision
	 */

	boolean sight;

	@Override
	public boolean hasSight() {
		return sight;
	}

	@Override
	public void setSight(boolean val) {
		sight = val;
	}

	/**
	 * Modifier: Light
	 * Items: Glowstone Dust
	 * Levels: 1
	 * Effect: Shows mobs through walls
	 */

	boolean light;

	@Override
	public boolean hasLight() {
		return light;
	}

	@Override
	public void setLight(boolean val) {
		light = val;
	}

	/**
	 * Modifier: Ender
	 * Items: Ender Pearl / Eye of Ender
	 * Levels: 1
	 * Effect: Teleports target to random location
	 */

	boolean ender;

	@Override
	public boolean hasEnder() {
		return ender;
	}

	@Override
	public void setEnder(boolean val) {
		ender = val;
	}

	/**
	 * Modifier: Freezing
	 * Items: Snow Cookie
	 * Levels: 1
	 * Effect: Gives Slowness to enemy
	 */

	boolean freezing;

	@Override
	public boolean hasFreezing() {
		return freezing;
	}

	@Override
	public void setFreezing(boolean val) {
		freezing = val;
	}


	/**
	 * Modifier: Treasure
	 * Items: Treasure Core
	 * Levels: 1
	 * Effect: Allows treasure cards to drop upon killing mobs
	 */

	boolean treasure;

	@Override
	public boolean hasTreasure() {
		return treasure;
	}

	@Override
	public void setTreasure(boolean val) {
		treasure = val;
	}

	public static void attachInformation(ItemStack stack, List<Component> tooltip) {
		if (stack.hasData(TOOL_MODIFIER)) {
			ToolModifierAttachment attachment = stack.getData(TOOL_MODIFIER);
			Item item = stack.getItem();

			if (attachment.getSpeedLevel() > 0) {
				if (item instanceof ForceBowItem || item instanceof ForceArmorItem || item instanceof ForceRodItem) {
					tooltip.add(Component.translatable("item.infuser.tooltip.speed" + attachment.getSpeedLevel()).withStyle(ChatFormatting.YELLOW));
				}
			}
			if (attachment.hasLumberjack()) {
				tooltip.add(Component.translatable("item.infuser.tooltip.lumberjack").withStyle(ChatFormatting.YELLOW));
			}
			if (attachment.getLuckLevel() > 0) {
				if (item instanceof ForceBowItem || item instanceof ForceArmorItem) {
					tooltip.add(Component.translatable("item.infuser.tooltip.luck" + attachment.getLuckLevel()).withStyle(ChatFormatting.GREEN));
				}
			}
			if (attachment.getBaneLevel() > 0) {
				tooltip.add(Component.translatable("item.infuser.tooltip.bane").withStyle(ChatFormatting.LIGHT_PURPLE));
			}
//			if (cap.getForceLevel() > 0) {
//				tooltip.add(new TranslationTextComponent("item.infuser.tooltip.force" + cap.getForceLevel()));
//			}
			if (attachment.getSturdyLevel() > 0) {
				if (stack.getItem() instanceof ForceArmorItem) {
					tooltip.add(Component.translatable("item.infuser.tooltip.sturdy" + attachment.getSturdyLevel()).withStyle(ChatFormatting.DARK_PURPLE));
				}
			}
			if (attachment.hasWing()) {
				tooltip.add(Component.translatable("item.infuser.tooltip.wing"));
			}
			if (attachment.hasBleed()) {
				tooltip.add(Component.translatable("item.infuser.tooltip.bleed" + attachment.getBleedLevel()).withStyle(ChatFormatting.RED));
			}
			if (attachment.hasRainbow()) {
				tooltip.add(Component.translatable("item.infuser.tooltip.rainbow").withStyle(ChatFormatting.GOLD));
			}
			if (attachment.hasHeat()) {
				if (item instanceof ForceShovelItem || item instanceof ForcePickaxeItem || item instanceof ForceShearsItem || item instanceof ForceArmorItem) {
					tooltip.add(Component.translatable("item.infuser.tooltip.heat").withStyle(ChatFormatting.RED));
				}
			}
			if (attachment.hasCamo()) {
				tooltip.add(Component.translatable("item.infuser.tooltip.camo").withStyle(ChatFormatting.DARK_GREEN));
			}
			if (attachment.hasEnder()) {
				tooltip.add(Component.translatable("item.infuser.tooltip.ender").withStyle(ChatFormatting.DARK_PURPLE));
			}
			if (attachment.hasFreezing()) {
				tooltip.add(Component.translatable("item.infuser.tooltip.freezing").withStyle(ChatFormatting.BLUE));
			}
			if (attachment.hasTreasure()) {
				tooltip.add(Component.translatable("item.infuser.tooltip.treasure").withStyle(ChatFormatting.GOLD));
			}
			if (attachment.hasLight()) {
				tooltip.add(Component.translatable("item.infuser.tooltip.light").withStyle(ChatFormatting.GOLD));
			}
			if (attachment.hasSharp()) {
				if (stack.getItem() instanceof ForceArmorItem) {
					tooltip.add(Component.translatable("item.infuser.tooltip.sharp").withStyle(ChatFormatting.GOLD));
				}
			}
		}
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();
		// Speed
		tag.putInt("speed", this.getSpeedLevel());
		// Heat
		tag.putBoolean("heat", this.hasHeat());
		// Force
		tag.putInt("force", this.getForceLevel());
		// Silk
		tag.putBoolean("silk", this.hasSilk());
		// Sharpness
		tag.putInt("sharp", this.getSharpLevel());
		// Luck
		tag.putInt("luck", this.getLuckLevel());
		// Sturdy
		tag.putInt("sturdy", this.getSturdyLevel());
		// Rainbow
		tag.putBoolean("rainbow", this.hasRainbow());
		// Lumberjack
		tag.putBoolean("lumber", this.hasLumberjack());
		// Bleeding
		tag.putInt("bleed", this.getBleedLevel());
		// Bane
		tag.putInt("bane", this.getBaneLevel());
		// Wing
		tag.putBoolean("wing", this.hasWing());
		// Camo
		tag.putBoolean("camo", this.hasCamo());
		// Sight
		tag.putBoolean("sight", this.hasSight());
		// Light
		tag.putBoolean("light", this.hasLight());
		// Ender
		tag.putBoolean("ender", this.hasEnder());
		// Freezing
		tag.putBoolean("freezing", this.hasFreezing());
		// Treasure
		tag.putBoolean("treasure", this.hasTreasure());

		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag tag) {
		this.setSpeed(tag.getInt("speed"));
		this.setHeat(tag.getBoolean("heat"));
		this.setForce(tag.getInt("force"));
		this.setSilk(tag.getBoolean("silk"));
		this.setSharp(tag.getInt("sharp"));
		this.setLuck(tag.getInt("luck"));
		this.setSturdy(tag.getInt("sturdy"));
		this.setRainbow(tag.getBoolean("rainbow"));
		this.setLumberjack(tag.getBoolean("lumber"));
		this.setBleed(tag.getInt("bleed"));
		this.setBane(tag.getInt("bane"));
		this.setWing(tag.getBoolean("wing"));
		this.setCamo(tag.getBoolean("camo"));
		this.setSight(tag.getBoolean("sight"));
		this.setLight(tag.getBoolean("light"));
		this.setEnder(tag.getBoolean("ender"));
		this.setFreezing(tag.getBoolean("freezing"));
		this.setTreasure(tag.getBoolean("treasure"));
	}
}
