package com.mrbysco.forcecraft.datagen.server;

import com.mrbysco.forcecraft.registry.ForceDatamaps;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ForceDataMapProvider extends DataMapProvider {

	public ForceDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	protected void gather(HolderLookup.Provider provider) {
		final Builder<Block, Block> iceConversions = builder(ForceDatamaps.ICE_CONVERSIONS);
		iceConversions.add(Blocks.WATER.builtInRegistryHolder(), Blocks.ICE, false);
		iceConversions.add(Blocks.LAVA.builtInRegistryHolder(), Blocks.COBBLESTONE, false);
		iceConversions.add(Blocks.COBBLESTONE.builtInRegistryHolder(), Blocks.STONE, false);
		iceConversions.add(Blocks.STONE.builtInRegistryHolder(), Blocks.STONE_BRICKS, false);

		final Builder<Block, Block> rodConversions = builder(ForceDatamaps.ROD_CONVERSIONS);
		List<Block> saplings = List.of(
				Blocks.OAK_SAPLING, Blocks.SPRUCE_SAPLING, Blocks.BIRCH_SAPLING,
				Blocks.JUNGLE_SAPLING, Blocks.ACACIA_SAPLING, Blocks.DARK_OAK_SAPLING,
				Blocks.PALE_OAK_SAPLING, Blocks.AZALEA, Blocks.FLOWERING_AZALEA,
				Blocks.MANGROVE_PROPAGULE, Blocks.CHERRY_SAPLING
		);
		saplings.forEach(sapling -> rodConversions.add(sapling.builtInRegistryHolder(), ForceRegistry.FORCE_SAPLING.value(), false));

		final Builder<DyeColor, EntityType<?>> mobColors = builder(ForceDatamaps.MOB_COLORS);
		mobColors.add(EntityTypes.WITHER_SKELETON.builtInRegistryHolder(), DyeColor.BLACK, false);
		mobColors.add(EntityTypes.TROPICAL_FISH.builtInRegistryHolder(), DyeColor.BLACK, false);
		mobColors.add(EntityTypes.PANDA.builtInRegistryHolder(), DyeColor.BLACK, false);
		mobColors.add(EntityTypes.BLAZE.builtInRegistryHolder(), DyeColor.RED, false);
		mobColors.add(EntityTypes.MOOSHROOM.builtInRegistryHolder(), DyeColor.RED, false);
		mobColors.add(EntityTypes.STRIDER.builtInRegistryHolder(), DyeColor.RED, false);
		mobColors.add(EntityTypes.SNIFFER.builtInRegistryHolder(), DyeColor.RED, false);
		mobColors.add(EntityTypes.PARROT.builtInRegistryHolder(), DyeColor.RED, false);
		mobColors.add(EntityTypes.SLIME.builtInRegistryHolder(), DyeColor.GREEN, false);
		mobColors.add(EntityTypes.WITCH.builtInRegistryHolder(), DyeColor.GREEN, false);
		mobColors.add(EntityTypes.ZOMBIE.builtInRegistryHolder(), DyeColor.GREEN, false);
		mobColors.add(EntityTypes.ZOMBIE_VILLAGER.builtInRegistryHolder(), DyeColor.GREEN, false);
		mobColors.add(EntityTypes.ZOMBIE_NAUTILUS.builtInRegistryHolder(), DyeColor.GREEN, false);
		mobColors.add(EntityTypes.ZOMBIE_HORSE.builtInRegistryHolder(), DyeColor.GREEN, false);
		mobColors.add(EntityTypes.GUARDIAN.builtInRegistryHolder(), DyeColor.GREEN, false);
		mobColors.add(EntityTypes.ZOMBIFIED_PIGLIN.builtInRegistryHolder(), DyeColor.GREEN, false);
		mobColors.add(EntityTypes.BOGGED.builtInRegistryHolder(), DyeColor.GREEN, false);
		mobColors.add(EntityTypes.COW.builtInRegistryHolder(), DyeColor.BROWN, false);
		mobColors.add(EntityTypes.HORSE.builtInRegistryHolder(), DyeColor.BROWN, false);
		mobColors.add(EntityTypes.DONKEY.builtInRegistryHolder(), DyeColor.BROWN, false);
		mobColors.add(EntityTypes.MULE.builtInRegistryHolder(), DyeColor.BROWN, false);
		mobColors.add(EntityTypes.VILLAGER.builtInRegistryHolder(), DyeColor.BROWN, false);
		mobColors.add(EntityTypes.SPIDER.builtInRegistryHolder(), DyeColor.BROWN, false);
		mobColors.add(EntityTypes.CAMEL_HUSK.builtInRegistryHolder(), DyeColor.BROWN, false);
		mobColors.add(EntityTypes.TADPOLE.builtInRegistryHolder(), DyeColor.BROWN, false);
		mobColors.add(EntityTypes.RABBIT.builtInRegistryHolder(), DyeColor.BROWN, false);
		mobColors.add(EntityTypes.ARMADILLO.builtInRegistryHolder(), DyeColor.BROWN, false);
		mobColors.add(EntityTypes.CAVE_SPIDER.builtInRegistryHolder(), DyeColor.BLUE, false);
		mobColors.add(EntityTypes.WARDEN.builtInRegistryHolder(), DyeColor.BLUE, false);
		mobColors.add(EntityTypes.STRAY.builtInRegistryHolder(), DyeColor.BLUE, false);
		mobColors.add(EntityTypes.DROWNED.builtInRegistryHolder(), DyeColor.BLUE, false);
		mobColors.add(EntityTypes.WANDERING_TRADER.builtInRegistryHolder(), DyeColor.BLUE, false);
		mobColors.add(EntityTypes.ENDERMAN.builtInRegistryHolder(), DyeColor.PURPLE, false);
		mobColors.add(EntityTypes.ENDERMITE.builtInRegistryHolder(), DyeColor.PURPLE, false);
		mobColors.add(EntityTypes.SHULKER.builtInRegistryHolder(), DyeColor.PURPLE, false);
		mobColors.add(EntityTypes.GHAST.builtInRegistryHolder(), DyeColor.LIGHT_GRAY, false);
		mobColors.add(EntityTypes.HAPPY_GHAST.builtInRegistryHolder(), DyeColor.LIGHT_GRAY, false);
		mobColors.add(EntityTypes.IRON_GOLEM.builtInRegistryHolder(), DyeColor.LIGHT_GRAY, false);
		mobColors.add(EntityTypes.PHANTOM.builtInRegistryHolder(), DyeColor.LIGHT_GRAY, false);
		mobColors.add(EntityTypes.SKELETON.builtInRegistryHolder(), DyeColor.GRAY, false);
		mobColors.add(EntityTypes.SKELETON_HORSE.builtInRegistryHolder(), DyeColor.GRAY, false);
		mobColors.add(EntityTypes.SILVERFISH.builtInRegistryHolder(), DyeColor.GRAY, false);
		mobColors.add(EntityTypes.WOLF.builtInRegistryHolder(), DyeColor.GRAY, false);
		mobColors.add(EntityTypes.VINDICATOR.builtInRegistryHolder(), DyeColor.GRAY, false);
		mobColors.add(EntityTypes.RAVAGER.builtInRegistryHolder(), DyeColor.GRAY, false);
		mobColors.add(EntityTypes.PILLAGER.builtInRegistryHolder(), DyeColor.GRAY, false);
		mobColors.add(EntityTypes.EVOKER.builtInRegistryHolder(), DyeColor.GRAY, false);
		mobColors.add(EntityTypes.ELDER_GUARDIAN.builtInRegistryHolder(), DyeColor.GRAY, false);
		mobColors.add(EntityTypes.BAT.builtInRegistryHolder(), DyeColor.GRAY, false);
		mobColors.add(EntityTypes.CAT.builtInRegistryHolder(), DyeColor.GRAY, false);
		mobColors.add(EntityTypes.PIG.builtInRegistryHolder(), DyeColor.PINK, false);
		mobColors.add(EntityTypes.PIGLIN.builtInRegistryHolder(), DyeColor.PINK, false);
		mobColors.add(EntityTypes.PIGLIN_BRUTE.builtInRegistryHolder(), DyeColor.PINK, false);
		mobColors.add(EntityTypes.HOGLIN.builtInRegistryHolder(), DyeColor.PINK, false);
		mobColors.add(EntityTypes.PIG.builtInRegistryHolder(), DyeColor.PINK, false);
		mobColors.add(EntityTypes.AXOLOTL.builtInRegistryHolder(), DyeColor.PINK, false);
		mobColors.add(EntityTypes.CREEPER.builtInRegistryHolder(), DyeColor.LIME, false);
		mobColors.add(EntityTypes.TURTLE.builtInRegistryHolder(), DyeColor.LIME, false);
		mobColors.add(EntityTypes.PARCHED.builtInRegistryHolder(), DyeColor.YELLOW, false);
		mobColors.add(EntityTypes.HUSK.builtInRegistryHolder(), DyeColor.YELLOW, false);
		mobColors.add(EntityTypes.LLAMA.builtInRegistryHolder(), DyeColor.YELLOW, false);
		mobColors.add(EntityTypes.CAMEL.builtInRegistryHolder(), DyeColor.YELLOW, false);
		mobColors.add(EntityTypes.TRADER_LLAMA.builtInRegistryHolder(), DyeColor.YELLOW, false);
		mobColors.add(EntityTypes.PUFFERFISH.builtInRegistryHolder(), DyeColor.YELLOW, false);
		mobColors.add(EntityTypes.BEE.builtInRegistryHolder(), DyeColor.YELLOW, false);
		mobColors.add(EntityTypes.SQUID.builtInRegistryHolder(), DyeColor.LIGHT_BLUE, false);
		mobColors.add(EntityTypes.GLOW_SQUID.builtInRegistryHolder(), DyeColor.LIGHT_BLUE, false);
		mobColors.add(EntityTypes.VEX.builtInRegistryHolder(), DyeColor.LIGHT_BLUE, false);
		mobColors.add(EntityTypes.BREEZE.builtInRegistryHolder(), DyeColor.LIGHT_BLUE, false);
		mobColors.add(EntityTypes.ALLAY.builtInRegistryHolder(), DyeColor.LIGHT_BLUE, false);
		mobColors.add(EntityTypes.DOLPHIN.builtInRegistryHolder(), DyeColor.LIGHT_BLUE, false);
		mobColors.add(EntityTypes.OCELOT.builtInRegistryHolder(), DyeColor.ORANGE, false);
		mobColors.add(EntityTypes.COPPER_GOLEM.builtInRegistryHolder(), DyeColor.ORANGE, false);
		mobColors.add(EntityTypes.FOX.builtInRegistryHolder(), DyeColor.ORANGE, false);
		mobColors.add(EntityTypes.MAGMA_CUBE.builtInRegistryHolder(), DyeColor.ORANGE, false);
		mobColors.add(EntityTypes.CHICKEN.builtInRegistryHolder(), DyeColor.WHITE, false);
		mobColors.add(EntityTypes.SHEEP.builtInRegistryHolder(), DyeColor.WHITE, false);
		mobColors.add(EntityTypes.SNOW_GOLEM.builtInRegistryHolder(), DyeColor.WHITE, false);
		mobColors.add(EntityTypes.POLAR_BEAR.builtInRegistryHolder(), DyeColor.WHITE, false);
		mobColors.add(EntityTypes.GOAT.builtInRegistryHolder(), DyeColor.WHITE, false);
	}
}
