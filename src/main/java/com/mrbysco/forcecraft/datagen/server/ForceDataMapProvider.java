package com.mrbysco.forcecraft.datagen.server;

import com.mrbysco.forcecraft.registry.ForceDatamaps;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ForceDataMapProvider extends DataMapProvider {

	public ForceDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	protected void gather(HolderLookup.Provider provider) {
		compostables(ForceRegistry.FORCE_LEAVES.asItem(), 0.3F);
		compostables(ForceRegistry.FORCE_SAPLING.asItem(), 0.3F);

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
		mobColors.add(EntityType.WITHER_SKELETON.builtInRegistryHolder(), DyeColor.BLACK, false);
		mobColors.add(EntityType.TROPICAL_FISH.builtInRegistryHolder(), DyeColor.BLACK, false);
		mobColors.add(EntityType.PANDA.builtInRegistryHolder(), DyeColor.BLACK, false);
		mobColors.add(EntityType.BLAZE.builtInRegistryHolder(), DyeColor.RED, false);
		mobColors.add(EntityType.MOOSHROOM.builtInRegistryHolder(), DyeColor.RED, false);
		mobColors.add(EntityType.STRIDER.builtInRegistryHolder(), DyeColor.RED, false);
		mobColors.add(EntityType.SNIFFER.builtInRegistryHolder(), DyeColor.RED, false);
		mobColors.add(EntityType.PARROT.builtInRegistryHolder(), DyeColor.RED, false);
		mobColors.add(EntityType.SLIME.builtInRegistryHolder(), DyeColor.GREEN, false);
		mobColors.add(EntityType.WITCH.builtInRegistryHolder(), DyeColor.GREEN, false);
		mobColors.add(EntityType.ZOMBIE.builtInRegistryHolder(), DyeColor.GREEN, false);
		mobColors.add(EntityType.ZOMBIE_VILLAGER.builtInRegistryHolder(), DyeColor.GREEN, false);
		mobColors.add(EntityType.ZOMBIE_NAUTILUS.builtInRegistryHolder(), DyeColor.GREEN, false);
		mobColors.add(EntityType.ZOMBIE_HORSE.builtInRegistryHolder(), DyeColor.GREEN, false);
		mobColors.add(EntityType.GUARDIAN.builtInRegistryHolder(), DyeColor.GREEN, false);
		mobColors.add(EntityType.ZOMBIFIED_PIGLIN.builtInRegistryHolder(), DyeColor.GREEN, false);
		mobColors.add(EntityType.BOGGED.builtInRegistryHolder(), DyeColor.GREEN, false);
		mobColors.add(EntityType.COW.builtInRegistryHolder(), DyeColor.BROWN, false);
		mobColors.add(EntityType.HORSE.builtInRegistryHolder(), DyeColor.BROWN, false);
		mobColors.add(EntityType.DONKEY.builtInRegistryHolder(), DyeColor.BROWN, false);
		mobColors.add(EntityType.MULE.builtInRegistryHolder(), DyeColor.BROWN, false);
		mobColors.add(EntityType.VILLAGER.builtInRegistryHolder(), DyeColor.BROWN, false);
		mobColors.add(EntityType.SPIDER.builtInRegistryHolder(), DyeColor.BROWN, false);
		mobColors.add(EntityType.CAMEL_HUSK.builtInRegistryHolder(), DyeColor.BROWN, false);
		mobColors.add(EntityType.TADPOLE.builtInRegistryHolder(), DyeColor.BROWN, false);
		mobColors.add(EntityType.RABBIT.builtInRegistryHolder(), DyeColor.BROWN, false);
		mobColors.add(EntityType.ARMADILLO.builtInRegistryHolder(), DyeColor.BROWN, false);
		mobColors.add(EntityType.CAVE_SPIDER.builtInRegistryHolder(), DyeColor.BLUE, false);
		mobColors.add(EntityType.WARDEN.builtInRegistryHolder(), DyeColor.BLUE, false);
		mobColors.add(EntityType.STRAY.builtInRegistryHolder(), DyeColor.BLUE, false);
		mobColors.add(EntityType.DROWNED.builtInRegistryHolder(), DyeColor.BLUE, false);
		mobColors.add(EntityType.WANDERING_TRADER.builtInRegistryHolder(), DyeColor.BLUE, false);
		mobColors.add(EntityType.ENDERMAN.builtInRegistryHolder(), DyeColor.PURPLE, false);
		mobColors.add(EntityType.ENDERMITE.builtInRegistryHolder(), DyeColor.PURPLE, false);
		mobColors.add(EntityType.SHULKER.builtInRegistryHolder(), DyeColor.PURPLE, false);
		mobColors.add(EntityType.GHAST.builtInRegistryHolder(), DyeColor.LIGHT_GRAY, false);
		mobColors.add(EntityType.HAPPY_GHAST.builtInRegistryHolder(), DyeColor.LIGHT_GRAY, false);
		mobColors.add(EntityType.IRON_GOLEM.builtInRegistryHolder(), DyeColor.LIGHT_GRAY, false);
		mobColors.add(EntityType.PHANTOM.builtInRegistryHolder(), DyeColor.LIGHT_GRAY, false);
		mobColors.add(EntityType.SKELETON.builtInRegistryHolder(), DyeColor.GRAY, false);
		mobColors.add(EntityType.SKELETON_HORSE.builtInRegistryHolder(), DyeColor.GRAY, false);
		mobColors.add(EntityType.SILVERFISH.builtInRegistryHolder(), DyeColor.GRAY, false);
		mobColors.add(EntityType.WOLF.builtInRegistryHolder(), DyeColor.GRAY, false);
		mobColors.add(EntityType.VINDICATOR.builtInRegistryHolder(), DyeColor.GRAY, false);
		mobColors.add(EntityType.RAVAGER.builtInRegistryHolder(), DyeColor.GRAY, false);
		mobColors.add(EntityType.PILLAGER.builtInRegistryHolder(), DyeColor.GRAY, false);
		mobColors.add(EntityType.EVOKER.builtInRegistryHolder(), DyeColor.GRAY, false);
		mobColors.add(EntityType.ELDER_GUARDIAN.builtInRegistryHolder(), DyeColor.GRAY, false);
		mobColors.add(EntityType.BAT.builtInRegistryHolder(), DyeColor.GRAY, false);
		mobColors.add(EntityType.CAT.builtInRegistryHolder(), DyeColor.GRAY, false);
		mobColors.add(EntityType.PIG.builtInRegistryHolder(), DyeColor.PINK, false);
		mobColors.add(EntityType.PIGLIN.builtInRegistryHolder(), DyeColor.PINK, false);
		mobColors.add(EntityType.PIGLIN_BRUTE.builtInRegistryHolder(), DyeColor.PINK, false);
		mobColors.add(EntityType.HOGLIN.builtInRegistryHolder(), DyeColor.PINK, false);
		mobColors.add(EntityType.PIG.builtInRegistryHolder(), DyeColor.PINK, false);
		mobColors.add(EntityType.AXOLOTL.builtInRegistryHolder(), DyeColor.PINK, false);
		mobColors.add(EntityType.CREEPER.builtInRegistryHolder(), DyeColor.LIME, false);
		mobColors.add(EntityType.TURTLE.builtInRegistryHolder(), DyeColor.LIME, false);
		mobColors.add(EntityType.PARCHED.builtInRegistryHolder(), DyeColor.YELLOW, false);
		mobColors.add(EntityType.HUSK.builtInRegistryHolder(), DyeColor.YELLOW, false);
		mobColors.add(EntityType.LLAMA.builtInRegistryHolder(), DyeColor.YELLOW, false);
		mobColors.add(EntityType.CAMEL.builtInRegistryHolder(), DyeColor.YELLOW, false);
		mobColors.add(EntityType.TRADER_LLAMA.builtInRegistryHolder(), DyeColor.YELLOW, false);
		mobColors.add(EntityType.PUFFERFISH.builtInRegistryHolder(), DyeColor.YELLOW, false);
		mobColors.add(EntityType.BEE.builtInRegistryHolder(), DyeColor.YELLOW, false);
		mobColors.add(EntityType.SQUID.builtInRegistryHolder(), DyeColor.LIGHT_BLUE, false);
		mobColors.add(EntityType.GLOW_SQUID.builtInRegistryHolder(), DyeColor.LIGHT_BLUE, false);
		mobColors.add(EntityType.VEX.builtInRegistryHolder(), DyeColor.LIGHT_BLUE, false);
		mobColors.add(EntityType.BREEZE.builtInRegistryHolder(), DyeColor.LIGHT_BLUE, false);
		mobColors.add(EntityType.ALLAY.builtInRegistryHolder(), DyeColor.LIGHT_BLUE, false);
		mobColors.add(EntityType.DOLPHIN.builtInRegistryHolder(), DyeColor.LIGHT_BLUE, false);
		mobColors.add(EntityType.OCELOT.builtInRegistryHolder(), DyeColor.ORANGE, false);
		mobColors.add(EntityType.COPPER_GOLEM.builtInRegistryHolder(), DyeColor.ORANGE, false);
		mobColors.add(EntityType.FOX.builtInRegistryHolder(), DyeColor.ORANGE, false);
		mobColors.add(EntityType.MAGMA_CUBE.builtInRegistryHolder(), DyeColor.ORANGE, false);
		mobColors.add(EntityType.CHICKEN.builtInRegistryHolder(), DyeColor.WHITE, false);
		mobColors.add(EntityType.SHEEP.builtInRegistryHolder(), DyeColor.WHITE, false);
		mobColors.add(EntityType.SNOW_GOLEM.builtInRegistryHolder(), DyeColor.WHITE, false);
		mobColors.add(EntityType.POLAR_BEAR.builtInRegistryHolder(), DyeColor.WHITE, false);
		mobColors.add(EntityType.GOAT.builtInRegistryHolder(), DyeColor.WHITE, false);

	}

	public void compostables(Item item, float chance) {
		this.builder(NeoForgeDataMaps.COMPOSTABLES).add(BuiltInRegistries.ITEM.getKey(item), new Compostable(chance), false);
	}
}
