package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.Tag.Named;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

public class ForceTags {
	public static void initialize() {

	}
	public static final Tag.Named<Block> MINEABLE_WITH_MITTS = forceBlockTag("mineable_with_mitts");
	public static final Tag.Named<Block> FORCE_BRICK = forceBlockTag("force_brick");

	public static final Tag.Named<Item> VALID_INFUSER_MODIFIERS = ItemTags.bind(Reference.MOD_ID + ":valid_infuser_modifiers");
	public static final Tag.Named<Item> VALID_INFUSER_TOOLS = ItemTags.bind(Reference.MOD_ID + ":valid_infuser_tools");
	public static final Tag.Named<Item> VALID_INFUSER_CHARGE = ItemTags.bind(Reference.MOD_ID + ":valid_infuser_charge");
	public static final Tag.Named<Item> FORCE_FUELS = ItemTags.bind(Reference.MOD_ID + ":force_fuel");
	public static final Tag.Named<Item> BACONATOR_FOOD = ItemTags.bind(Reference.MOD_ID + ":baconator_food");
	public static final Tag.Named<Item> VALID_FORCE_BELT = ItemTags.bind(Reference.MOD_ID + ":valid_force_belt");
	public static final Tag.Named<Item> ENDER = ItemTags.bind(Reference.MOD_ID + ":ender");

	public static final Tag.Named<Item> FORCE_INGOT = forgeItemTag("ingots/force");
	public static final Tag.Named<Item> FORCE_NUGGET = forgeItemTag("nuggets/force");
	public static final Tag.Named<Item> FORGE_GEM = forgeItemTag("gems/force");
	public static final Tag.Named<Item> HOLDS_ITEMS = forgeItemTag("holds_items");

	//Fuels
	public static final Tag.Named<Fluid> FORCE = forgeFluidTag("force");
	public static final IOptionalNamedTag<Fluid> FUEL = optionalForgeFluidTag("fuel");
	public static final IOptionalNamedTag<Fluid> BIOFUEL = optionalForgeFluidTag("biofuel");

	//Throttle Fluids
	public static final IOptionalNamedTag<Fluid> MILK = optionalForgeFluidTag("milk");


	public static final Tag.Named<Block> ENDERTOT_HOLDABLE = forceBlockTag("endertot_holdable");

	public static final Tag<EntityType<?>> FLASK_BLACKLIST = EntityTypeTags.createOptional(new ResourceLocation(Reference.MOD_ID, "flask_blacklist"));

	private static Named<Item> forgeItemTag(String name) {
		return ItemTags.bind("forge:" + name);
	}
	private static Named<Block> forceBlockTag(String name) {
		return BlockTags.bind("forcecraft:" + name);
	}
	private static Named<Fluid> forgeFluidTag(String name) {
		return FluidTags.bind("forge:" + name);
	}
	private static IOptionalNamedTag<Fluid> optionalForgeFluidTag(String name) {
		return FluidTags.createOptional(new ResourceLocation("forge", name));
	}

}
