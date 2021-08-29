package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

public class ForceTags {
	public static final ITag.INamedTag<Block> FORCE_BRICK = forceBlockTag("force_brick");

	public static final ITag.INamedTag<Item> VALID_INFUSER_MODIFIERS = ItemTags.makeWrapperTag(Reference.MOD_ID + ":valid_infuser_modifiers");
	public static final ITag.INamedTag<Item> VALID_INFUSER_TOOLS = ItemTags.makeWrapperTag(Reference.MOD_ID + ":valid_infuser_tools");
	public static final ITag.INamedTag<Item> VALID_INFUSER_CHARGE = ItemTags.makeWrapperTag(Reference.MOD_ID + ":valid_infuser_charge");
	public static final ITag.INamedTag<Item> FORCE_FUELS = ItemTags.makeWrapperTag(Reference.MOD_ID + ":force_fuel");
	public static final ITag.INamedTag<Item> BACONATOR_FOOD = ItemTags.makeWrapperTag(Reference.MOD_ID + ":baconator_food");
	public static final ITag.INamedTag<Item> VALID_FORCE_BELT = ItemTags.makeWrapperTag(Reference.MOD_ID + ":valid_force_belt");
	public static final ITag.INamedTag<Item> ENDER = ItemTags.makeWrapperTag(Reference.MOD_ID + ":ender");

	public static final ITag.INamedTag<Item> FORCE_INGOT = forgeItemTag("ingots/force");
	public static final ITag.INamedTag<Item> FORCE_NUGGET = forgeItemTag("nuggets/force");
	public static final ITag.INamedTag<Item> FORGE_GEM = forgeItemTag("gems/force");
	public static final ITag.INamedTag<Item> HOLDS_ITEMS = forgeItemTag("holds_items");

	//Fuels
	public static final ITag.INamedTag<Fluid> FORCE = forgeFluidTag("force");
	public static final IOptionalNamedTag<Fluid> FUEL = optionalForgeFluidTag("fuel");
	public static final IOptionalNamedTag<Fluid> BIOFUEL = optionalForgeFluidTag("biofuel");

	//Throttle Fluids
	public static final IOptionalNamedTag<Fluid> MILK = optionalForgeFluidTag("milk");


	public static final ITag.INamedTag<Block> ENDERTOT_HOLDABLE = forceBlockTag("endertot_holdable");

	public static final ITag<EntityType<?>> FLASK_BLACKLIST = EntityTypeTags.createOptional(new ResourceLocation(Reference.MOD_ID, "flask_blacklist"));

	private static INamedTag<Item> forgeItemTag(String name) {
		return ItemTags.makeWrapperTag("forge:" + name);
	}
	private static INamedTag<Block> forceBlockTag(String name) {
		return BlockTags.makeWrapperTag("forcecraft:" + name);
	}
	private static INamedTag<Fluid> forgeFluidTag(String name) {
		return FluidTags.makeWrapperTag("forge:" + name);
	}
	private static IOptionalNamedTag<Fluid> optionalForgeFluidTag(String name) {
		return FluidTags.createOptional(new ResourceLocation("forge", name));
	}

}
