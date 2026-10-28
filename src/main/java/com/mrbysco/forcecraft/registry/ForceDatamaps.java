package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber
public class ForceDatamaps {
	public static final DataMapType<Block, Block> ICE_CONVERSIONS = DataMapType.builder(
			Reference.modLoc("ice_conversions"), Registries.BLOCK, BuiltInRegistries.BLOCK.byNameCodec())
			.synced(BuiltInRegistries.BLOCK.byNameCodec(), false).build();

	public static final DataMapType<Block, Block> ROD_CONVERSIONS = DataMapType.builder(
			Reference.modLoc("rod_conversions"), Registries.BLOCK, BuiltInRegistries.BLOCK.byNameCodec())
			.synced(BuiltInRegistries.BLOCK.byNameCodec(), false).build();

	public static final DataMapType<EntityType<?>, DyeColor> MOB_COLORS = DataMapType.builder(
			Reference.modLoc("mob_colors"), Registries.ENTITY_TYPE, DyeColor.CODEC)
			.synced(DyeColor.CODEC, true).build();

	@SubscribeEvent
	public static void register(RegisterDataMapTypesEvent event) {
		event.register(ICE_CONVERSIONS);
		event.register(ROD_CONVERSIONS);
		event.register(MOB_COLORS);
	}
}
