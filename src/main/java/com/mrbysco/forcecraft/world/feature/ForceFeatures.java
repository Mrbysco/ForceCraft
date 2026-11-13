package com.mrbysco.forcecraft.world.feature;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BlockStateProviders;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.BlockReplacement;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.RandomSelectorFeature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ForceFeatures {

	public static final ResourceKey<Feature> ORE_FORCE = createConfiguredKey("ore_force");
	public static final ResourceKey<Feature> ORE_FORCE_BURIED = createConfiguredKey("ore_force_buried");
	public static final ResourceKey<Feature> ORE_NETHER_FORCE = createConfiguredKey("ore_nether_force");

	public static final ResourceKey<Feature> FORCE_TREE = createConfiguredKey("force_tree");
	public static final ResourceKey<Feature> FORCE_TREE_WITH_MORE_BEEHIVES_CONFIG = createConfiguredKey("force_tree_with_bees");

	public static final ResourceKey<Feature> TREES_FORCE = createConfiguredKey("trees_force");

	public static ResourceKey<Feature> createConfiguredKey(String name) {
		return ResourceKey.create(Registries.FEATURE, Reference.modLoc(name));
	}

	public static final ResourceKey<PlacedFeature> PLACED_ORE_FORCE = createPlacedKey("ore_force");
	public static final ResourceKey<PlacedFeature> PLACED_ORE_FORCE_BURIED = createPlacedKey("ore_force_buried");
	public static final ResourceKey<PlacedFeature> PLACED_ORE_NETHER_FORCE = createPlacedKey("ore_nether_force");
	public static final ResourceKey<PlacedFeature> PLACED_FORCE_TREE = createPlacedKey("force_tree");
	public static final ResourceKey<PlacedFeature> PLACED_FORCE_TREE_BEES_002 = createPlacedKey("force_tree_bees_002");

	public static ResourceKey<PlacedFeature> createPlacedKey(String name) {
		return ResourceKey.create(Registries.PLACED_FEATURE, Reference.modLoc(name));
	}

	private static TreeFeature.Builder createStraightBlobTree(BlockState trunkState, BlockState foliageState,
	                                                          int baseHeight, int heightRandA, int heightRandB,
	                                                          int radius, Holder<BlockStateProvider> belowTrunkProvider) {
		return new TreeFeature.Builder(
				BlockStateProvider.of(trunkState),
				new StraightTrunkPlacer(baseHeight, heightRandA, heightRandB),
				BlockStateProvider.of(foliageState),
				new BlobFoliagePlacer(ConstantInt.of(radius), ConstantInt.of(0), 3),
				new TwoLayersFeatureSize(1, 0, 1),
				belowTrunkProvider
		);
	}

	private static TreeFeature.Builder createForceTree(Holder<BlockStateProvider> belowTrunkProvider) {
		return createStraightBlobTree(
				ForceRegistry.FORCE_LOG.get().defaultBlockState(),
				ForceRegistry.FORCE_LEAVES.get().defaultBlockState(),
				4, 2, 0, 2,
				belowTrunkProvider).ignoreVines();
	}

	public static void bootstrap(BootstrapContext<Feature> context) {
		HolderGetter<BlockStateProvider> blockStateProviders = context.lookup(Registries.BLOCK_STATE_PROVIDER);

		RuleTest stoneRuleTest = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
		RuleTest deepslateRuleTest = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

		List<BlockReplacement> targetList = List.of(
				BlockReplacement.replace(stoneRuleTest, ForceRegistry.POWER_ORE.get().defaultBlockState()),
				BlockReplacement.replace(deepslateRuleTest, ForceRegistry.DEEPSLATE_POWER_ORE.get().defaultBlockState())
		);
		HolderGetter<PlacedFeature> placedFeatureHolderGetter = context.lookup(Registries.PLACED_FEATURE);
		Holder<PlacedFeature> PLACED_FORCE_TREE_BEES = placedFeatureHolderGetter.getOrThrow(PLACED_FORCE_TREE_BEES_002);
		Holder<PlacedFeature> PLACED_FORCE_TREE = placedFeatureHolderGetter.getOrThrow(ForceFeatures.PLACED_FORCE_TREE);
		context.register(ORE_FORCE, new OreFeature(targetList, 6));
		context.register(ORE_FORCE_BURIED, new OreFeature(targetList, 6, 1.0F));

		RuleTest netherrack = new BlockMatchTest(Blocks.NETHERRACK);
		context.register(ORE_NETHER_FORCE, new OreFeature(netherrack, ForceRegistry.NETHER_POWER_ORE.get().defaultBlockState(), 6));

		Holder<BlockStateProvider> belowTrunkProvider = blockStateProviders.getOrThrow(BlockStateProviders.SOIL_BENEATH_TREE);
		BeehiveDecorator decorator_005 = new BeehiveDecorator(0.05F);
		context.register(FORCE_TREE, createForceTree(belowTrunkProvider).build());
		context.register(FORCE_TREE_WITH_MORE_BEEHIVES_CONFIG, createForceTree(belowTrunkProvider)
				.decorators(List.of(decorator_005)).build());

		context.register(
				TREES_FORCE,
				new RandomSelectorFeature(
						List.of(
								new WeightedPlacedFeature(PLACED_FORCE_TREE_BEES, 0.2F)
						),
						PLACED_FORCE_TREE
				)
		);
	}


	private static List<PlacementModifier> orePlacement(PlacementModifier modifier1, PlacementModifier modifier2) {
		return List.of(modifier1, InSquarePlacement.spread(), modifier2, BiomeFilter.biome());
	}

	private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier modifier) {
		return orePlacement(CountPlacement.of(count), modifier);
	}

	public static void placedBootstrap(BootstrapContext<PlacedFeature> context) {
		HolderGetter<Feature> configuredFeatures = context.lookup(Registries.FEATURE);

		PlacementUtils.register(context, PLACED_ORE_FORCE, configuredFeatures.getOrThrow(ORE_FORCE),
				commonOrePlacement(5, HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(48))));
		PlacementUtils.register(context, PLACED_ORE_FORCE_BURIED, configuredFeatures.getOrThrow(ORE_FORCE_BURIED),
				commonOrePlacement(3, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64))));
		PlacementUtils.register(context, PLACED_ORE_NETHER_FORCE, configuredFeatures.getOrThrow(ORE_NETHER_FORCE),
				commonOrePlacement(10, PlacementUtils.RANGE_10_10));

		PlacementUtils.register(context, PLACED_FORCE_TREE, configuredFeatures.getOrThrow(FORCE_TREE),
				VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(3), ForceRegistry.FORCE_SAPLING.get()));

		PlacementUtils.register(context, PLACED_FORCE_TREE_BEES_002, configuredFeatures.getOrThrow(FORCE_TREE_WITH_MORE_BEEHIVES_CONFIG),
				VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(3), ForceRegistry.FORCE_SAPLING.get()));
	}
}
