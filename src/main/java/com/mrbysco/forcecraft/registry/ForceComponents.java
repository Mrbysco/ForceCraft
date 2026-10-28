package com.mrbysco.forcecraft.registry;

import com.mojang.serialization.Codec;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.data.BowMode;
import com.mrbysco.forcecraft.data.TimeMode;
import com.mrbysco.forcecraft.item.tool.EnderData;
import com.mrbysco.forcecraft.upgrade.UpgradeBookData;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.UUID;
import java.util.function.Supplier;

public class ForceComponents {
	public static final DeferredRegister<DataComponentType<?>> COMPONENT_TYPE = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Reference.MOD_ID);

	public static final Supplier<DataComponentType<UpgradeBookData>> UPGRADE_BOOK = COMPONENT_TYPE.register("upgrade_book", () ->
			DataComponentType.<UpgradeBookData>builder()
					.persistent(UpgradeBookData.CODEC)
					.networkSynchronized(UpgradeBookData.STREAM_CODEC)
					.build());

	public static final Supplier<DataComponentType<Integer>> FORCE_STORED = COMPONENT_TYPE.register("force_stored", () ->
			DataComponentType.<Integer>builder()
					.persistent(ExtraCodecs.intRange(0, 10_000))
					.networkSynchronized(ByteBufCodecs.VAR_INT)
					.build());

	public static final Supplier<DataComponentType<Integer>> SLOTS_USED = COMPONENT_TYPE.register("slots_used", () ->
			DataComponentType.<Integer>builder()
					.persistent(ExtraCodecs.NON_NEGATIVE_INT)
					.networkSynchronized(ByteBufCodecs.VAR_INT)
					.build());
	public static final Supplier<DataComponentType<Integer>> SLOTS_TOTAL = COMPONENT_TYPE.register("slots_total", () ->
			DataComponentType.<Integer>builder()
					.persistent(ExtraCodecs.POSITIVE_INT)
					.networkSynchronized(ByteBufCodecs.VAR_INT)
					.build());
	public static final Supplier<DataComponentType<Integer>> PACK_TIER = COMPONENT_TYPE.register("pack_tier", () ->
			DataComponentType.<Integer>builder()
					.persistent(ExtraCodecs.NON_NEGATIVE_INT)
					.networkSynchronized(ByteBufCodecs.VAR_INT)
					.build());
	public static final Supplier<DataComponentType<DyeColor>> PACK_COLOR = COMPONENT_TYPE.register("pack_color", () ->
			DataComponentType.<DyeColor>builder()
					.persistent(DyeColor.CODEC)
					.networkSynchronized(DyeColor.STREAM_CODEC)
					.build());
	public static final Supplier<DataComponentType<UUID>> UUID = COMPONENT_TYPE.register("uuid", () ->
			DataComponentType.<UUID>builder()
					.persistent(UUIDUtil.CODEC)
					.networkSynchronized(UUIDUtil.STREAM_CODEC)
					.build());

	public static final Supplier<DataComponentType<Integer>> TOME_EXPERIENCE = COMPONENT_TYPE.register("tome_experience", () ->
			DataComponentType.<Integer>builder()
					.persistent(ExtraCodecs.NON_NEGATIVE_INT)
					.networkSynchronized(ByteBufCodecs.INT)
					.build());

	public static final Supplier<DataComponentType<Boolean>> FORCE_INFUSED = COMPONENT_TYPE.register("force_infused", () ->
			DataComponentType.<Boolean>builder()
					.persistent(Codec.BOOL)
					.networkSynchronized(ByteBufCodecs.BOOL)
					.build());

	public static final Supplier<DataComponentType<BowMode>> BOW_MODE = COMPONENT_TYPE.register("bow_mode", () ->
			DataComponentType.<BowMode>builder()
					.persistent(BowMode.CODEC)
					.networkSynchronized(ByteBufCodecs.fromCodec(BowMode.CODEC))
					.build());

	public static final Supplier<DataComponentType<TimeMode>> TIME_MODE = COMPONENT_TYPE.register("time_mode", () ->
			DataComponentType.<TimeMode>builder()
					.persistent(TimeMode.CODEC)
					.networkSynchronized(ByteBufCodecs.fromCodec(TimeMode.CODEC))
					.build());

	public static final Supplier<DataComponentType<EnderData>> ENDER_DATA = COMPONENT_TYPE.register("ender_data", () ->
			DataComponentType.<EnderData>builder()
					.persistent(EnderData.MAP_CODEC)
					.networkSynchronized(EnderData.STREAM_CODEC)
					.build());

	public static final Supplier<DataComponentType<Identifier>> BOUND_ID = COMPONENT_TYPE.register("bound_id", () ->
			DataComponentType.<Identifier>builder()
					.persistent(Identifier.CODEC)
					.networkSynchronized(Identifier.STREAM_CODEC)
					.build());

	public static final Supplier<DataComponentType<String>> FORTUNE = COMPONENT_TYPE.register("fortune", () ->
			DataComponentType.<String>builder()
					.persistent(Codec.STRING)
					.networkSynchronized(ByteBufCodecs.STRING_UTF8)
					.build());

	/**
	 * Modifier: Force
	 * Item: Force Nugget
	 * Levels: 2
	 * Effect: Turns certain items into more Forceful versions of themselves, It also seems to imbue Force Swords with Knockback.
	 */
	public static final Supplier<DataComponentType<Integer>> FORCE = COMPONENT_TYPE.register("force", () ->
			DataComponentType.<Integer>builder()
					.persistent(ExtraCodecs.intRange(1, 2))
					.networkSynchronized(ByteBufCodecs.VAR_INT)
					.build());

	/**
	 * Modifier: Damage
	 * Item: Claw
	 * Levels: 5
	 * Effect: Boosts damage to the weapon to which it is attached. On Force Swords it gives Sharpness, while Punch is given to Force Bows.
	 */
	public static final Supplier<DataComponentType<Integer>> DAMAGE = COMPONENT_TYPE.register("damage", () ->
			DataComponentType.<Integer>builder()
					.persistent(ExtraCodecs.intRange(1, 5))
					.networkSynchronized(ByteBufCodecs.VAR_INT)
					.build());

	/**
	 * Modifier: Speed
	 * Item: Sugar
	 * Levels: 5
	 * Effect: Imbues Force Tools with the ability to break blocks faster, and gives an overall speed boost to players when used on a Force Rod. If used to upgrade armor the player will move faster.
	 */
	public static final Supplier<DataComponentType<Integer>> SPEED = COMPONENT_TYPE.register("speed", () ->
			DataComponentType.<Integer>builder()
					.persistent(ExtraCodecs.intRange(1, 5))
					.networkSynchronized(ByteBufCodecs.VAR_INT)
					.build());

	/**
	 * Modifier: Lumberjack
	 * Item: Force Log
	 * Levels: 1
	 * Effect: Allows Force Axes to take down entire trees.
	 */
	public static final Supplier<DataComponentType<Boolean>> LUMBERJACK = COMPONENT_TYPE.register("lumberjack", () ->
			DataComponentType.<Boolean>builder()
					.persistent(Codec.BOOL)
					.networkSynchronized(ByteBufCodecs.BOOL)
					.build());

	/**
	 * Modifier: Rainbow
	 * Item: Lapis Lazuli
	 * Levels: 1
	 * Effect: This upgrade is only valid on Force Shears and randomly changes the color of wool dropped from sheep.
	 */
	public static final Supplier<DataComponentType<Boolean>> RAINBOW = COMPONENT_TYPE.register("rainbow", () ->
			DataComponentType.<Boolean>builder()
					.persistent(Codec.BOOL)
					.networkSynchronized(ByteBufCodecs.BOOL)
					.build());

	/**
	 * Modifier: Luck
	 * Item: Fortune
	 * Levels: 4
	 * Effect: Enhances the amount of loot one receives for performing almost any task. On the Force Sword it gives Looting. On any standard block breaking tool it gives Fortune.
	 */
	public static final Supplier<DataComponentType<Integer>> LUCK = COMPONENT_TYPE.register("luck", () ->
			DataComponentType.<Integer>builder()
					.persistent(ExtraCodecs.intRange(1, 4))
					.networkSynchronized(ByteBufCodecs.VAR_INT)
					.build());

//	/**
//	 * Modifier: Grinding
//	 * Item: Flint
//	 * Levels: 1
//	 * Effect: Allows for certain items and drops to be ground into powdery substances or other useful materials.
//	 */
//	public static final Supplier<DataComponentType<Boolean>> GRINDING = COMPONENT_TYPE.register("grinding", () ->
//			DataComponentType.<Boolean>builder()
//					.persistent(Codec.BOOL)
//					.networkSynchronized(ByteBufCodecs.BOOL)
//					.build());

	/**
	 * Modifier: Freezing
	 * Item: Snow Cookie
	 * Levels: 1
	 * Effect: Upgrading your Force Bow with Freezing will allow it to shoot Ice Arrows which will freeze your enemies in place for a short time, and even change some blocks in the world.
	 */
	public static final Supplier<DataComponentType<Boolean>> FREEZING = COMPONENT_TYPE.register("freezing", () ->
			DataComponentType.<Boolean>builder()
					.persistent(Codec.BOOL)
					.networkSynchronized(ByteBufCodecs.BOOL)
					.build());

	/**
	 * Modifier: False
	 * Item: Glass Powder
	 * Levels: 1
	 * Effect: Makes it so a Force Sword will never deal a finishing blow.
	 */
	public static final Supplier<DataComponentType<Boolean>> FALSE = COMPONENT_TYPE.register("false", () ->
			DataComponentType.<Boolean>builder()
					.persistent(Codec.BOOL)
					.networkSynchronized(ByteBufCodecs.BOOL)
					.build());

	/**
	 * Modifier: Touch
	 * Item: Cobweb
	 * Levels: 1
	 * Effect: Imbues Force Tools with Silk Touch which applies before upgrades like Grinding or Heat.
	 */
	public static final Supplier<DataComponentType<Boolean>> TOUCH = COMPONENT_TYPE.register("touch", () ->
			DataComponentType.<Boolean>builder()
					.persistent(Codec.BOOL)
					.networkSynchronized(ByteBufCodecs.BOOL)
					.build());

	/**
	 * Modifier: Bleed
	 * Item: Arrow
	 * Levels: 3
	 * Effect: Causes entities that are struck to quickly take small amounts of bleed damage over time. This works on both the Force Sword and Bow and does stack with burning damage.
	 */
	public static final Supplier<DataComponentType<Integer>> BLEED = COMPONENT_TYPE.register("bleed", () ->
			DataComponentType.<Integer>builder()
					.persistent(ExtraCodecs.intRange(1, 3))
					.networkSynchronized(ByteBufCodecs.VAR_INT)
					.build());


	/**
	 * Modifier: Craft
	 * Item: Crafting table
	 * Levels: 1
	 * Effect: This upgrade is valid for the Item card and will allow it to automatically craft the configured recipe from inside a Force Pack.
	 */
	public static final Supplier<DataComponentType<Boolean>> CRAFT = COMPONENT_TYPE.register("craft", () ->
			DataComponentType.<Boolean>builder()
					.persistent(Codec.BOOL)
					.networkSynchronized(ByteBufCodecs.BOOL)
					.build());

	/**
	 * Modifier: Time
	 * Item: Clock
	 * Levels: 1
	 * Effect: When applied on a Force Sword or Force Rod.
	 * On a sword it freezes/slows down a mob.
	 * When applied to a rod it can be used to speed up / slow down time within a certain range
	 * When applied to a Force Torch it turns it into a Time Torch which has a 3x3 radius
	 */
	public static final Supplier<DataComponentType<Boolean>> TIME = COMPONENT_TYPE.register("time", () ->
			DataComponentType.<Boolean>builder()
					.persistent(Codec.BOOL)
					.networkSynchronized(ByteBufCodecs.BOOL)
					.build());

	/**
	 * Modifier: Heat
	 * Item: Golden Power Source
	 * Levels: 1
	 * Effect: Valid on all Force Tools.
	 * On a weapon it will apply Fire Aspect.
	 * On a Bow it will ignite a 3x3x3 area centered on impact.
	 * On other tools it will auto smelt whatever is harvested.
	 * On a Force Rod it converts it to a Rod of Fire Resistance.
	 * On armor, it attaches flame to bare-handed attacks.
	 * On a Force Torch it converts it to a Heat Torch. (Ignites mobs (dealing fire damage) in a 4 block radius every 7.5 seconds)
	 * On an Item Card erases the specified item.
	 */
	public static final Supplier<DataComponentType<Boolean>> HEAT = COMPONENT_TYPE.register("heat", () ->
			DataComponentType.<Boolean>builder()
					.persistent(Codec.BOOL)
					.networkSynchronized(ByteBufCodecs.BOOL)
					.build());

	/**
	 * Modifier: Bane
	 * Item: Spider Eye
	 * Levels: 1
	 * Effect: When a mob is struck by a bane imbued tool they will lose some of their abilities. (Creepers, enderman)
	 */
	public static final Supplier<DataComponentType<Boolean>> BANE = COMPONENT_TYPE.register("bane", () ->
			DataComponentType.<Boolean>builder()
					.persistent(Codec.BOOL)
					.networkSynchronized(ByteBufCodecs.BOOL)
					.build());


	/**
	 * Modifier: Forge
	 * Item: Furnace
	 * Levels: 1
	 * Effect: Valid for Item Cards. It allows them to transmute items in the same tag.
	 */
	public static final Supplier<DataComponentType<Boolean>> FORGE = COMPONENT_TYPE.register("forge", () ->
			DataComponentType.<Boolean>builder()
					.persistent(Codec.BOOL)
					.networkSynchronized(ByteBufCodecs.BOOL)
					.build());

	/**
	 * Modifier: Wing
	 * Item: Roc's Wing
	 * Levels: 1
	 * Effect: On a Force Sword it will fling the user when they right-click while holding the jump key or allow small amounts of flight with a sneak jump right-click.
	 * On Armor, it will allow flight and increase the maximum duration of flight.
	 */
	public static final Supplier<DataComponentType<Boolean>> WING = COMPONENT_TYPE.register("wing", () ->
			DataComponentType.<Boolean>builder()
					.persistent(Codec.BOOL)
					.networkSynchronized(ByteBufCodecs.BOOL)
					.build());


	/**
	 * Modifier: Healing
	 * Item: Ghast Tear
	 * Levels: 2
	 * Effect: Healing will heal any entity it hits for 1 heart per level (unless they are undead). It can be applied to Sword, Bow and Rod.
	 */
	public static final Supplier<DataComponentType<Integer>> HEALING = COMPONENT_TYPE.register("healing", () ->
			DataComponentType.<Integer>builder()
					.persistent(ExtraCodecs.intRange(1, 2))
					.networkSynchronized(ByteBufCodecs.INT)
					.build());

	/**
	 * Modifier: Camo
	 * Item: Potion of Invisibility
	 * Levels: 1
	 * Effect: Makes Force armor invisible. On a Force Rod it gives short spurts of invisibility to its user.
	 */
	public static final Supplier<DataComponentType<Boolean>> CAMO = COMPONENT_TYPE.register("camo", () ->
			DataComponentType.<Boolean>builder()
					.persistent(Codec.BOOL)
					.networkSynchronized(ByteBufCodecs.BOOL)
					.build());

	/**
	 * Modifier: Sight
	 * Item: Potion of Night Vision
	 * Levels: 1
	 * Effect: Upgrades a Force Rod with night vision capabilities. Allowing users about a minute per use.
	 */
	public static final Supplier<DataComponentType<Boolean>> SIGHT = COMPONENT_TYPE.register("sight", () ->
			DataComponentType.<Boolean>builder()
					.persistent(Codec.BOOL)
					.networkSynchronized(ByteBufCodecs.BOOL)
					.build());

	/**
	 * Modifier: Sturdy
	 * Item: Diamond Dust
	 * Levels: 2
	 * Effect: Imbues unbreaking on all Force Tools but the Shears and Sword, or it can be attached to Force Armor once to reduce incoming damage by a significant amount.
	 */
	public static final Supplier<DataComponentType<Integer>> STURDY = COMPONENT_TYPE.register("sturdy", () ->
			DataComponentType.<Integer>builder()
					.persistent(ExtraCodecs.intRange(1, 2))
					.networkSynchronized(ByteBufCodecs.INT)
					.build());


	/**
	 * Modifier: Ender
	 * Item: Ender Pearl
	 * Levels: 1
	 * Effect: When imbued on a Force Sword or Rod will allow the user to teleport short distances.
	 */
	public static final Supplier<DataComponentType<Boolean>> ENDER = COMPONENT_TYPE.register("ender", () ->
			DataComponentType.<Boolean>builder()
					.persistent(Codec.BOOL)
					.networkSynchronized(ByteBufCodecs.BOOL)
					.build());

	/**
	 * Modifier: Soul
	 * Item: Soul Waver
	 * Levels: 1
	 * Effect: When imbued on Force Swords will allow Mob Chunks to occasionally drop. Which can be used to craft vanilla spawners or even be smelted into colored mob Ingots.
	 */
	public static final Supplier<DataComponentType<Boolean>> SOUL = COMPONENT_TYPE.register("soul", () ->
			DataComponentType.<Boolean>builder()
					.persistent(Codec.BOOL)
					.networkSynchronized(ByteBufCodecs.BOOL)
					.build());

	/**
	 * Modifier: Light
	 * Item: Glowstone Dust
	 * Levels: 5
	 * Effect: Imbues Force Swords with Smite
	 */
	public static final Supplier<DataComponentType<Integer>> LIGHT = COMPONENT_TYPE.register("light", () ->
			DataComponentType.<Integer>builder()
					.persistent(ExtraCodecs.intRange(1, 5))
					.networkSynchronized(ByteBufCodecs.INT)
					.build());


	/**
	 * Modifier: Treasure
	 * Item: Treasure Core
	 * Levels: 1
	 * Effect: Entities killed by a Treasure Imbued weapon will occasionally drop Treasure Cards which can be crafted into a Spoils Bag for random loot.
	 * Fortune will increase the drop rate.
	 */
	public static final Supplier<DataComponentType<Boolean>> TREASURE = COMPONENT_TYPE.register("treasure", () ->
			DataComponentType.<Boolean>builder()
					.persistent(Codec.BOOL)
					.networkSynchronized(ByteBufCodecs.BOOL)
					.build());
}
