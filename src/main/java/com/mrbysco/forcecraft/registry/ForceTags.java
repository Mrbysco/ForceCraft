package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class ForceTags {

	public static class Blocks {
		public static final TagKey<Block> FORCE_LOGS = BlockTags.create(Reference.modLoc("force_logs"));
		public static final TagKey<Block> FORCE_BRICKS = BlockTags.create(Reference.modLoc("force_bricks"));
		public static final TagKey<Block> FORCE_TORCHES = BlockTags.create(Reference.modLoc("force_torches"));
		public static final TagKey<Block> INCORRECT_FOR_FORCE = BlockTags.create(Reference.modLoc("needs_force_tool"));
		public static final TagKey<Block> MINEABLE_WITH_MITTS = BlockTags.create(Reference.modLoc("mineable_with_mitts"));
		public static final TagKey<Block> TICKING_NOT_SUPPORTED = BlockTags.create(Reference.modLoc("ticking_not_supported"));

		public static final TagKey<Block> ORES = commonTag("ores");
		public static final TagKey<Block> ORES_POWER = commonTag("ores/power");


		private static TagKey<Block> commonTag(String name) {
			return BlockTags.create(Identifier.fromNamespaceAndPath("c", name));
		}
	}

	public static class Items {
		public static final TagKey<Item> FORCE_LOGS = ItemTags.create(Reference.modLoc("force_logs"));
		public static final TagKey<Item> FORCE_BRICKS = ItemTags.create(Reference.modLoc("force_bricks"));
		public static final TagKey<Item> FORCE_TORCHES = ItemTags.create(Reference.modLoc("force_torches"));
		public static final TagKey<Item> CAN_STORE_FORCE = ItemTags.create(Reference.modLoc("can_store_force"));
		public static final TagKey<Item> FORCE_REPAIR_INGREDIENTS = ItemTags.create(Reference.modLoc("force_repair_ingredients"));
		public static final TagKey<Item> FORCE_ARMOR = ItemTags.create(Reference.modLoc("force_armor"));
		public static final TagKey<Item> UPGRADE_CORES = ItemTags.create(Reference.modLoc("upgrade_cores"));
		public static final TagKey<Item> FORCE_FURNACES = ItemTags.create(Reference.modLoc("force_furnaces"));
		public static final TagKey<Item> FORCE_TOOLS = ItemTags.create(Reference.modLoc("force_tools"));
		public static final TagKey<Item> FORCE_WEAPONS = ItemTags.create(Reference.modLoc("force_weapons"));
		public static final TagKey<Item> MOB_CHUNKS = ItemTags.create(Reference.modLoc("mob_chunks"));
		public static final TagKey<Item> HOLDS_ITEMS = ItemTags.create(Identifier.fromNamespaceAndPath("c", "holds_items"));

		public static final TagKey<Item> VALID_INFUSER_MODIFIERS = ItemTags.create(Reference.modLoc("valid_infuser_modifiers"));
		public static final TagKey<Item> VALID_INFUSER_TOOLS = ItemTags.create(Reference.modLoc("valid_infuser_tools"));
		public static final TagKey<Item> VALID_INFUSER_CHARGE = ItemTags.create(Reference.modLoc("valid_infuser_charge"));
		public static final TagKey<Item> VALID_FORCE_BELT = ItemTags.create(Reference.modLoc("valid_force_belt"));

		public static final TagKey<Item> VALID_FORCE_ITEMS = ItemTags.create(Reference.modLoc("valid_items/force"));
		public static final TagKey<Item> VALID_DAMAGE_ITEMS = ItemTags.create(Reference.modLoc("valid_items/damage"));
		public static final TagKey<Item> VALID_SPEED_ITEMS = ItemTags.create(Reference.modLoc("valid_items/speed"));
		public static final TagKey<Item> VALID_LUMBERJACK_ITEMS = ItemTags.create(Reference.modLoc("valid_items/lumberjack"));
		public static final TagKey<Item> VALID_RAINBOW_ITEMS = ItemTags.create(Reference.modLoc("valid_items/rainbow"));
		public static final TagKey<Item> VALID_LUCK_ITEMS = ItemTags.create(Reference.modLoc("valid_items/luck"));
		public static final TagKey<Item> VALID_GRINDING_ITEMS = ItemTags.create(Reference.modLoc("valid_items/grinding"));
		public static final TagKey<Item> VALID_FREEZING_ITEMS = ItemTags.create(Reference.modLoc("valid_items/freezing"));
		public static final TagKey<Item> VALID_EXPERIENCE_ITEMS = ItemTags.create(Reference.modLoc("valid_items/experience"));
		public static final TagKey<Item> VALID_FALSE_ITEMS = ItemTags.create(Reference.modLoc("valid_items/false"));
		public static final TagKey<Item> VALID_TOUCH_ITEMS = ItemTags.create(Reference.modLoc("valid_items/touch"));
		public static final TagKey<Item> VALID_BLEED_ITEMS = ItemTags.create(Reference.modLoc("valid_items/bleed"));
		public static final TagKey<Item> VALID_CRAFT_ITEMS = ItemTags.create(Reference.modLoc("valid_items/craft"));
		public static final TagKey<Item> VALID_TIME_ITEMS = ItemTags.create(Reference.modLoc("valid_items/time"));
		public static final TagKey<Item> VALID_INFUSE_TORCHES = ItemTags.create(Reference.modLoc("valid_items/infuse_torch"));
		public static final TagKey<Item> VALID_HEAT_ITEMS = ItemTags.create(Reference.modLoc("valid_items/heat"));
		public static final TagKey<Item> VALID_BANE_ITEMS = ItemTags.create(Reference.modLoc("valid_items/bane"));
		public static final TagKey<Item> VALID_FORGE_ITEMS = ItemTags.create(Reference.modLoc("valid_items/forge"));
		public static final TagKey<Item> VALID_WING_ITEMS = ItemTags.create(Reference.modLoc("valid_items/wing"));
		public static final TagKey<Item> VALID_HEALING_ITEMS = ItemTags.create(Reference.modLoc("valid_items/healing"));
		public static final TagKey<Item> VALID_CAMO_ITEMS = ItemTags.create(Reference.modLoc("valid_items/camo"));
		public static final TagKey<Item> VALID_SIGHT_ITEMS = ItemTags.create(Reference.modLoc("valid_items/sight"));
		public static final TagKey<Item> VALID_STURDY_ITEMS = ItemTags.create(Reference.modLoc("valid_items/sturdy"));
		public static final TagKey<Item> VALID_ENDER_ITEMS = ItemTags.create(Reference.modLoc("valid_items/ender"));
		public static final TagKey<Item> VALID_SOUL_ITEMS = ItemTags.create(Reference.modLoc("valid_items/soul"));
		public static final TagKey<Item> VALID_LIGHT_ITEMS = ItemTags.create(Reference.modLoc("valid_items/light"));
		public static final TagKey<Item> VALID_TREASURE_ITEMS = ItemTags.create(Reference.modLoc("valid_items/treasure"));
		public static final TagKey<Item> LIMITED_STURDY_ITEMS = ItemTags.create(Reference.modLoc("valid_items/limited_sturdy"));
		public static final TagKey<Item> LIMITED_DAMAGE_ITEMS = ItemTags.create(Reference.modLoc("valid_items/limited_damage"));

		public static final TagKey<Item> ORES_POWER = commonTag("ores/power");
		public static final TagKey<Item> GEMS_FORCE = commonTag("gems/force");
		public static final TagKey<Item> INGOTS_FORCE = commonTag("ingots/force");
		public static final TagKey<Item> NUGGETS_FORCE = commonTag("nuggets/force");
		public static final TagKey<Item> RODS_FORCE = commonTag("rods/force");

		private static TagKey<Item> commonTag(String name) {
			return ItemTags.create(Identifier.fromNamespaceAndPath("c", name));
		}
	}

	public static class Fluids {
		public static final TagKey<Fluid> FORCE = commonTag("force");
		public static final TagKey<Fluid> FUEL = commonTag("fuel");
		public static final TagKey<Fluid> BIOFUEL = commonTag("biofuel");

		private static TagKey<Fluid> commonTag(String name) {
			return FluidTags.create(Identifier.fromNamespaceAndPath("c", name));
		}
	}


	public static class EntityTypes {
		public static final TagKey<EntityType<?>> VALID_BANE = tag("valid_entities/bane");
		public static final TagKey<EntityType<?>> TICKING_NOT_SUPPORTED = tag("ticking_not_supported");

		private static TagKey<EntityType<?>> tag(String name) {
			return TagKey.create(Registries.ENTITY_TYPE, Reference.modLoc(name));
		}
	}
}
