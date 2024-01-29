package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class ForceTags {

	public static final TagKey<Block> NEEDS_FORCE_TOOL = forceBlockTag("needs_force_tool");
	public static final TagKey<Block> MINEABLE_WITH_MITTS = forceBlockTag("mineable_with_mitts");
	public static final TagKey<Block> FORCE_BRICK = forceBlockTag("force_brick");

	public static final TagKey<Item> VALID_INFUSER_MODIFIERS = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "valid_infuser_modifiers"));
	public static final TagKey<Item> VALID_INFUSER_TOOLS = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "valid_infuser_tools"));
	public static final TagKey<Item> VALID_INFUSER_CHARGE = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "valid_infuser_charge"));
	public static final TagKey<Item> TOOLS = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "tools"));
	public static final TagKey<Item> FORCE_FUELS = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "force_fuel"));
	public static final TagKey<Item> BACONATOR_FOOD = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "baconator_food"));
	public static final TagKey<Item> FORTUNE = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "fortune"));
	public static final TagKey<Item> VALID_FORCE_BELT = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "valid_force_belt"));
	public static final TagKey<Item> VALID_BANE_TOOLS = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "valid_bane_tools"));
	public static final TagKey<Item> VALID_HEALING_TOOLS = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "valid_healing_tools"));
	public static final TagKey<Item> VALID_WING_TOOLS = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "valid_wing_tools"));
	public static final TagKey<Item> VALID_STURDY_TOOLS = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "valid_sturdy_tools"));
	public static final TagKey<Item> VALID_ENDER_TOOLS = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "valid_ender_tools"));
	public static final TagKey<Item> VALID_LIGHT_TOOLS = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "valid_light_tools"));
	public static final TagKey<Item> VALID_TREASURE_TOOLS = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "valid_treasure_tools"));
	public static final TagKey<Item> VALID_BLEEDING_TOOLS = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "valid_bleeding_tools"));
	public static final TagKey<Item> VALID_SILKY_TOOLS = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "valid_silky_tools"));
	public static final TagKey<Item> VALID_CAMO_TOOLS = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "valid_camo_tools"));
	public static final TagKey<Item> VALID_DAMAGE_TOOLS = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "valid_damage_tools"));
	public static final TagKey<Item> VALID_LUCKY_TOOLS = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "valid_lucky_tools"));
	public static final TagKey<Item> VALID_FREEZING_TOOLS = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "valid_freezing_tools"));
	public static final TagKey<Item> VALID_HEAT_TOOLS = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "valid_heat_tools"));
	public static final TagKey<Item> VALID_LUMBER_TOOLS = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "valid_lumber_tools"));
	public static final TagKey<Item> VALID_SPEED_TOOLS = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "valid_speed_tools"));
	public static final TagKey<Item> VALID_KNOCKBACK_TOOLS = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "valid_knockback_tools"));
	public static final TagKey<Item> ENDER = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "ender"));
	public static final TagKey<Item> FORCE_LOGS = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "force_logs"));
	public static final TagKey<Item> ENTITY_FLASKS = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "entity_flasks"));
	public static final TagKey<Item> CHU_JELLY = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "chu_jelly"));

	public static final TagKey<Item> FORCE_INGOT = forgeItemTag("ingots/force");
	public static final TagKey<Item> FORCE_NUGGET = forgeItemTag("nuggets/force");
	public static final TagKey<Item> FORCE_FURNACES = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "force_furnace"));
	public static final TagKey<Item> FORCE_GEM = forgeItemTag("gems/force");
	public static final TagKey<Item> FORCE_ROD = forgeItemTag("rods/force");
	public static final TagKey<Item> FORCE_GEAR = forgeItemTag("gears/force");
	public static final TagKey<Item> HOLDS_ITEMS = forgeItemTag("holds_items");

	//Fuels
	public static final TagKey<Fluid> FORCE = forgeFluidTag("force");
	public static final TagKey<Fluid> MILK = forgeFluidTag("milk");
	public static final TagKey<Fluid> FUEL = optionalForgeFluidTag("fuel");
	public static final TagKey<Fluid> BIOFUEL = optionalForgeFluidTag("biofuel");


	public static final TagKey<Block> ENDERTOT_HOLDABLE = forceBlockTag("endertot_holdable");

	public static final TagKey<EntityType<?>> FLASK_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Reference.MOD_ID, "flask_blacklist"));

	public static final TagKey<Biome> IS_PEACEFUL = forceBiomeTag("is_peaceful");


	private static TagKey<Item> forgeItemTag(String name) {
		return ItemTags.create(new ResourceLocation("forge", name));
	}

	private static TagKey<Block> forceBlockTag(String name) {
		return BlockTags.create(new ResourceLocation(Reference.MOD_ID, name));
	}

	private static TagKey<Fluid> forgeFluidTag(String name) {
		return FluidTags.create(new ResourceLocation("forge", name));
	}

	private static TagKey<Fluid> optionalForgeFluidTag(String name) {
		return FluidTags.create(new ResourceLocation("forge", name));
	}

	private static TagKey<Biome> forceBiomeTag(String name) {
		return TagKey.create(Registries.BIOME, new ResourceLocation(Reference.MOD_ID, name));
	}

}
