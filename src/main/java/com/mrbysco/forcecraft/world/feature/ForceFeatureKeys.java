package com.mrbysco.forcecraft.world.feature;

import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
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
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ForceFeatureKeys {

	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_FORCE = FeatureUtils.createKey("forcecraft:ore_force");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_FORCE_BURIED = FeatureUtils.createKey("forcecraft:ore_force_buried");

	public static final ResourceKey<ConfiguredFeature<?, ?>> FORCE_TREE = FeatureUtils.createKey("forcecraft:force_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> FORCE_TREE_WITH_MORE_BEEHIVES_CONFIG = FeatureUtils.createKey("forcecraft:force_tree_with_bees");

	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_FORCE = FeatureUtils.createKey("forcecraft:trees_force");

	public static final ResourceKey<PlacedFeature> PLACED_ORE_FORCE = PlacementUtils.createKey("forcecraft:ore_force");
	public static final ResourceKey<PlacedFeature> PLACED_ORE_FORCE_BURIED = PlacementUtils.createKey("forcecraft:ore_force_buried");
	public static final ResourceKey<PlacedFeature> PLACED_FORCE_TREE = PlacementUtils.createKey("forcecraft:force_tree");
	public static final ResourceKey<PlacedFeature> PLACED_FORCE_TREE_BEES_002 = PlacementUtils.createKey("forcecraft:force_tree_bees_002");

	private static TreeConfiguration.TreeConfigurationBuilder createStraightBlobTree(BlockState trunkState, BlockState foliageState, int baseHeight, int heightRandA, int heightRandB, int p_195152_) {
		return new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(trunkState), new StraightTrunkPlacer(baseHeight, heightRandA, heightRandB),
				BlockStateProvider.simple(foliageState), new BlobFoliagePlacer(ConstantInt.of(p_195152_), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1));
	}

	private static TreeConfiguration.TreeConfigurationBuilder createForceTree() {
		return createStraightBlobTree(ForceRegistry.FORCE_LOG.get().defaultBlockState(), ForceRegistry.FORCE_LEAVES.get().defaultBlockState(), 4, 2, 0, 2).ignoreVines();
	}

	public static void configuredBootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
		RuleTest stoneRuleTest = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
		RuleTest deepslateRuleTest = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
		List<OreConfiguration.TargetBlockState> list = List.of(
				OreConfiguration.target(stoneRuleTest, ForceRegistry.POWER_ORE.get().defaultBlockState()),
				OreConfiguration.target(deepslateRuleTest, ForceRegistry.DEEPSLATE_POWER_ORE.get().defaultBlockState())
		);
		HolderGetter<PlacedFeature> placedFeatureHolderGetter = context.lookup(Registries.PLACED_FEATURE);
		Holder<PlacedFeature> PLACED_FORCE_TREE_BEES = placedFeatureHolderGetter.getOrThrow(PLACED_FORCE_TREE_BEES_002);
		Holder<PlacedFeature> PLACED_FORCE_TREE = placedFeatureHolderGetter.getOrThrow(ForceFeatureKeys.PLACED_FORCE_TREE);
		FeatureUtils.register(context, ORE_FORCE, Feature.ORE, new OreConfiguration(list, 6));
		FeatureUtils.register(context, ORE_FORCE_BURIED, Feature.ORE, new OreConfiguration(list, 6, 1.0F));

		BeehiveDecorator decorator_005 = new BeehiveDecorator(0.05F);
		FeatureUtils.register(context, FORCE_TREE, ForceFeatures.FORCE_TREE.get(), createForceTree().build());
		FeatureUtils.register(context, FORCE_TREE_WITH_MORE_BEEHIVES_CONFIG, ForceFeatures.FORCE_TREE.get(), createForceTree().decorators(List.of(decorator_005)).build());

		FeatureUtils.register(context, TREES_FORCE,
				Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
						List.of(new WeightedPlacedFeature(PLACED_FORCE_TREE_BEES, 0.2F)), PLACED_FORCE_TREE));
	}


	private static List<PlacementModifier> orePlacement(PlacementModifier modifier1, PlacementModifier modifier2) {
		return List.of(modifier1, InSquarePlacement.spread(), modifier2, BiomeFilter.biome());
	}

	private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier modifier) {
		return orePlacement(CountPlacement.of(count), modifier);
	}

	public static void placedBootstrap(BootstapContext<PlacedFeature> context) {
		HolderGetter<ConfiguredFeature<?, ?>> holdergetter = context.lookup(Registries.CONFIGURED_FEATURE);

		PlacementUtils.register(context, PLACED_ORE_FORCE, holdergetter.getOrThrow(ORE_FORCE),
				commonOrePlacement(5, HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(48))));
		PlacementUtils.register(context, PLACED_ORE_FORCE_BURIED, holdergetter.getOrThrow(ORE_FORCE_BURIED),
				commonOrePlacement(3, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64))));

		PlacementUtils.register(context, PLACED_FORCE_TREE, holdergetter.getOrThrow(FORCE_TREE),
				VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(3), ForceRegistry.FORCE_SAPLING.get()));

		PlacementUtils.register(context, PLACED_FORCE_TREE_BEES_002, holdergetter.getOrThrow(FORCE_TREE_WITH_MORE_BEEHIVES_CONFIG),
				VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(3), ForceRegistry.FORCE_SAPLING.get()));
	}
}
