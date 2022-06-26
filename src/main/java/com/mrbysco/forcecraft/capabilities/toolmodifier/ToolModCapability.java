package com.mrbysco.forcecraft.capabilities.toolmodifier;

import com.mrbysco.forcecraft.items.ForceArmorItem;
import com.mrbysco.forcecraft.items.tools.ForceBowItem;
import com.mrbysco.forcecraft.items.tools.ForcePickaxeItem;
import com.mrbysco.forcecraft.items.tools.ForceRodItem;
import com.mrbysco.forcecraft.items.tools.ForceShearsItem;
import com.mrbysco.forcecraft.items.tools.ForceShovelItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.mrbysco.forcecraft.capabilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class ToolModCapability implements IToolModifier, ICapabilitySerializable<CompoundTag>, ICapabilityProvider {
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
		stack.getCapability(CAPABILITY_TOOLMOD).ifPresent(cap -> {
			Item item = stack.getItem();

			if (cap.getSpeedLevel() > 0) {
				if (item instanceof ForceBowItem || item instanceof ForceArmorItem || item instanceof ForceRodItem) {
					tooltip.add(new TranslatableComponent("item.infuser.tooltip.speed" + cap.getSpeedLevel()).withStyle(ChatFormatting.YELLOW));
				}
			}
			if (cap.hasLumberjack()) {
				tooltip.add(new TranslatableComponent("item.infuser.tooltip.lumberjack").withStyle(ChatFormatting.YELLOW));
			}
			if (cap.getLuckLevel() > 0) {
				if (item instanceof ForceBowItem || item instanceof ForceArmorItem) {
					tooltip.add(new TranslatableComponent("item.infuser.tooltip.luck" + cap.getLuckLevel()).withStyle(ChatFormatting.GREEN));
				}
			}
			if (cap.getBaneLevel() > 0) {
				tooltip.add(new TranslatableComponent("item.infuser.tooltip.bane").withStyle(ChatFormatting.LIGHT_PURPLE));
			}
//			if (cap.getForceLevel() > 0) {
//				tooltip.add(new TranslationTextComponent("item.infuser.tooltip.force" + cap.getForceLevel()));
//			}
			if (cap.getSturdyLevel() > 0) {
				if (stack.getItem() instanceof ForceArmorItem) {
					tooltip.add(new TranslatableComponent("item.infuser.tooltip.sturdy" + cap.getSturdyLevel()).withStyle(ChatFormatting.DARK_PURPLE));
				}
			}
			if (cap.hasWing()) {
				tooltip.add(new TranslatableComponent("item.infuser.tooltip.wing"));
			}
			if (cap.hasBleed()) {
				tooltip.add(new TranslatableComponent("item.infuser.tooltip.bleed" + cap.getBleedLevel()).withStyle(ChatFormatting.RED));
			}
			if (cap.hasRainbow()) {
				tooltip.add(new TranslatableComponent("item.infuser.tooltip.rainbow").withStyle(ChatFormatting.GOLD));
			}
			if (cap.hasHeat()) {
				if (item instanceof ForceShovelItem || item instanceof ForcePickaxeItem || item instanceof ForceShearsItem || item instanceof ForceArmorItem) {
					tooltip.add(new TranslatableComponent("item.infuser.tooltip.heat").withStyle(ChatFormatting.RED));
				}
			}
			if (cap.hasCamo()) {
				tooltip.add(new TranslatableComponent("item.infuser.tooltip.camo").withStyle(ChatFormatting.DARK_GREEN));
			}
			if (cap.hasEnder()) {
				tooltip.add(new TranslatableComponent("item.infuser.tooltip.ender").withStyle(ChatFormatting.DARK_PURPLE));
			}
			if (cap.hasFreezing()) {
				tooltip.add(new TranslatableComponent("item.infuser.tooltip.freezing").withStyle(ChatFormatting.BLUE));
			}
			if (cap.hasTreasure()) {
				tooltip.add(new TranslatableComponent("item.infuser.tooltip.treasure").withStyle(ChatFormatting.GOLD));
			}
			if (cap.hasLight()) {
				tooltip.add(new TranslatableComponent("item.infuser.tooltip.light").withStyle(ChatFormatting.GOLD));
			}
			if (cap.hasSharp()) {
				if (stack.getItem() instanceof ForceArmorItem) {
					tooltip.add(new TranslatableComponent("item.infuser.tooltip.sharp").withStyle(ChatFormatting.GOLD));
				}
			}
		});
	}

	@Override
	public CompoundTag serializeNBT() {
		return writeNBT(this);
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		readNBT(this, nbt);
	}

	public static CompoundTag writeNBT(IToolModifier instance) {
		if (instance == null) {
			return null;
		}
		CompoundTag tag = new CompoundTag();
		// Speed
		tag.putInt("speed", instance.getSpeedLevel());
		// Heat
		tag.putBoolean("heat", instance.hasHeat());
		// Force
		tag.putInt("force", instance.getForceLevel());
		// Silk
		tag.putBoolean("silk", instance.hasSilk());
		// Sharpness
		tag.putInt("sharp", instance.getSharpLevel());
		// Luck
		tag.putInt("luck", instance.getLuckLevel());
		// Sturdy
		tag.putInt("sturdy", instance.getSturdyLevel());
		// Rainbow
		tag.putBoolean("rainbow", instance.hasRainbow());
		// Lumberjack
		tag.putBoolean("lumber", instance.hasLumberjack());
		// Bleeding
		tag.putInt("bleed", instance.getBleedLevel());
		// Bane
		tag.putInt("bane", instance.getBaneLevel());
		// Wing
		tag.putBoolean("wing", instance.hasWing());
		// Camo
		tag.putBoolean("camo", instance.hasCamo());
		// Sight
		tag.putBoolean("sight", instance.hasSight());
		// Light
		tag.putBoolean("light", instance.hasLight());
		// Ender
		tag.putBoolean("ender", instance.hasEnder());
		// Freezing
		tag.putBoolean("freezing", instance.hasFreezing());
		// Treasure
		tag.putBoolean("treasure", instance.hasTreasure());
		return tag;
	}

	public static void readNBT(IToolModifier instance, CompoundTag tag) {
		instance.setSpeed(tag.getInt("speed"));
		instance.setHeat(tag.getBoolean("heat"));
		instance.setForce(tag.getInt("force"));
		instance.setSilk(tag.getBoolean("silk"));
		instance.setSharp(tag.getInt("sharp"));
		instance.setLuck(tag.getInt("luck"));
		instance.setSturdy(tag.getInt("sturdy"));
		instance.setRainbow(tag.getBoolean("rainbow"));
		instance.setLumberjack(tag.getBoolean("lumber"));
		instance.setBleed(tag.getInt("bleed"));
		instance.setBane(tag.getInt("bane"));
		instance.setWing(tag.getBoolean("wing"));
		instance.setCamo(tag.getBoolean("camo"));
		instance.setSight(tag.getBoolean("sight"));
		instance.setLight(tag.getBoolean("light"));
		instance.setEnder(tag.getBoolean("ender"));
		instance.setFreezing(tag.getBoolean("freezing"));
		instance.setTreasure(tag.getBoolean("treasure"));
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		return CAPABILITY_TOOLMOD.orEmpty(cap, LazyOptional.of(() -> this));
	}

	public final Capability<IToolModifier> getCapability() {
		return CAPABILITY_TOOLMOD;
	}
}
