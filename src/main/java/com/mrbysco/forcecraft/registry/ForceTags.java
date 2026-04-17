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

	public static final TagKey<Block> INCORRECT_FOR_FORCE = forceBlockTag("needs_force_tool");
	public static final TagKey<Block> MINEABLE_WITH_MITTS = forceBlockTag("mineable_with_mitts");
	public static final TagKey<Block> FORCE_BRICKS = forceBlockTag("force_bricks");
	public static final TagKey<Block> FORCE_LOGS = forceBlockTag("force_logs");
	public static final TagKey<Block> TICKING_NOT_SUPPORTED = forceBlockTag("ticking_not_supported");

	public static final TagKey<Item> VALID_INFUSER_MODIFIERS = ItemTags.create(Reference.modLoc("valid_infuser_modifiers"));
	public static final TagKey<Item> VALID_INFUSER_TOOLS = ItemTags.create(Reference.modLoc("valid_infuser_tools"));
	public static final TagKey<Item> VALID_INFUSER_CHARGE = ItemTags.create(Reference.modLoc("valid_infuser_charge"));
	public static final TagKey<Item> TOOLS = ItemTags.create(Reference.modLoc("tools"));
	public static final TagKey<Item> FORCE_FUELS = ItemTags.create(Reference.modLoc("force_fuel"));
	public static final TagKey<Item> BACONATOR_FOOD = ItemTags.create(Reference.modLoc("baconator_food"));
	public static final TagKey<Item> FORTUNE = ItemTags.create(Reference.modLoc("fortune"));
	public static final TagKey<Item> VALID_FORCE_BELT = ItemTags.create(Reference.modLoc("valid_force_belt"));
	public static final TagKey<Item> VALID_BANE_TOOLS = ItemTags.create(Reference.modLoc("valid_bane_tools"));
	public static final TagKey<Item> VALID_HEALING_TOOLS = ItemTags.create(Reference.modLoc("valid_healing_tools"));
	public static final TagKey<Item> VALID_WING_TOOLS = ItemTags.create(Reference.modLoc("valid_wing_tools"));
	public static final TagKey<Item> VALID_STURDY_TOOLS = ItemTags.create(Reference.modLoc("valid_sturdy_tools"));
	public static final TagKey<Item> VALID_ENDER_TOOLS = ItemTags.create(Reference.modLoc("valid_ender_tools"));
	public static final TagKey<Item> VALID_LIGHT_TOOLS = ItemTags.create(Reference.modLoc("valid_light_tools"));
	public static final TagKey<Item> VALID_TREASURE_TOOLS = ItemTags.create(Reference.modLoc("valid_treasure_tools"));
	public static final TagKey<Item> VALID_BLEEDING_TOOLS = ItemTags.create(Reference.modLoc("valid_bleeding_tools"));
	public static final TagKey<Item> VALID_SILKY_TOOLS = ItemTags.create(Reference.modLoc("valid_silky_tools"));
	public static final TagKey<Item> VALID_CAMO_TOOLS = ItemTags.create(Reference.modLoc("valid_camo_tools"));
	public static final TagKey<Item> VALID_DAMAGE_TOOLS = ItemTags.create(Reference.modLoc("valid_damage_tools"));
	public static final TagKey<Item> VALID_LUCKY_TOOLS = ItemTags.create(Reference.modLoc("valid_lucky_tools"));
	public static final TagKey<Item> VALID_FREEZING_TOOLS = ItemTags.create(Reference.modLoc("valid_freezing_tools"));
	public static final TagKey<Item> VALID_HEAT_TOOLS = ItemTags.create(Reference.modLoc("valid_heat_tools"));
	public static final TagKey<Item> VALID_LUMBER_TOOLS = ItemTags.create(Reference.modLoc("valid_lumber_tools"));
	public static final TagKey<Item> VALID_SPEED_TOOLS = ItemTags.create(Reference.modLoc("valid_speed_tools"));
	public static final TagKey<Item> VALID_KNOCKBACK_TOOLS = ItemTags.create(Reference.modLoc("valid_knockback_tools"));
	public static final TagKey<Item> ENDER = ItemTags.create(Reference.modLoc("ender"));
	public static final TagKey<Item> FORCE_LOGS_ITEM = ItemTags.create(Reference.modLoc("force_logs"));
	public static final TagKey<Item> ENTITY_FLASKS = ItemTags.create(Reference.modLoc("entity_flasks"));
	public static final TagKey<Item> CHU_JELLY = ItemTags.create(Reference.modLoc("chu_jelly"));
	public static final TagKey<Item> FORCE_REPAIR_INGREDIENTS = ItemTags.create(Reference.modLoc("force_repair_ingredients"));

	public static final TagKey<Item> FORCE_INGOT = commonItemTag("ingots/force");
	public static final TagKey<Item> FORCE_NUGGET = commonItemTag("nuggets/force");
	public static final TagKey<Item> FORCE_FURNACES = ItemTags.create(Reference.modLoc("force_furnace"));
	public static final TagKey<Item> FORCE_GEM = commonItemTag("gems/force");
	public static final TagKey<Item> FORCE_ROD = commonItemTag("rods/force");
	public static final TagKey<Item> FORCE_GEAR = commonItemTag("gears/force");
	public static final TagKey<Item> HOLDS_ITEMS = commonItemTag("holds_items");

	//Fuels
	public static final TagKey<Fluid> FORCE = commonFluidTag("force");
	public static final TagKey<Fluid> MILK = commonFluidTag("milk");
	public static final TagKey<Fluid> FUEL = optionalCommonFluidTag("fuel");
	public static final TagKey<Fluid> BIOFUEL = optionalCommonFluidTag("biofuel");


	public static final TagKey<Block> ENDERTOT_HOLDABLE = forceBlockTag("endertot_holdable");

	public static final TagKey<EntityType<?>> FLASK_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, Reference.modLoc("flask_blacklist"));


	private static TagKey<Item> commonItemTag(String name) {
		return ItemTags.create(Identifier.fromNamespaceAndPath("c", name));
	}

	private static TagKey<Block> forceBlockTag(String name) {
		return BlockTags.create(Reference.modLoc(name));
	}

	private static TagKey<Fluid> commonFluidTag(String name) {
		return FluidTags.create(Identifier.fromNamespaceAndPath("c", name));
	}

	private static TagKey<Fluid> optionalCommonFluidTag(String name) {
		return FluidTags.create(Identifier.fromNamespaceAndPath("c", name));
	}

}
