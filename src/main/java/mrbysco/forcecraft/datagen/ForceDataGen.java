package mrbysco.forcecraft.datagen;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.SingleItemRecipeBuilder;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static mrbysco.forcecraft.registry.ForceRegistry.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForceDataGen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(new Loots(generator));
			generator.addProvider(new Recipes(generator));
		}
		if (event.includeClient()) {
//			generator.addProvider(new Language(generator));
			generator.addProvider(new BlockStates(generator, helper));
			generator.addProvider(new ItemModels(generator, helper));
		}
	}

	private static class Loots extends LootTableProvider {
		public Loots(DataGenerator gen) {
			super(gen);
		}

		@Override
		protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
			return ImmutableList.of(Pair.of(ForceBlocks::new, LootParameterSets.BLOCK));
		}

		private class ForceBlocks extends BlockLootTables {
			@Override
			protected void addTables() {
				registerLootTable(POWER_ORE.get(), (ore) -> {
					return droppingWithSilkTouch(POWER_ORE.get(), withExplosionDecay(POWER_ORE_ITEM.get(), ItemLootEntry.builder(FORCE_GEM.get()).acceptFunction(SetCount.builder(RandomValueRange.of(2.0F, 4.0F))).acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE))));
				});

				registerDropSelfLootTable(INFUSER.get());
				registerLootTable(FORCE_FURNACE.get(), (furnace) -> {
					return droppingWithName(FORCE_FURNACE.get());
				});

				registerDropSelfLootTable(FORCE_SAPLING.get());
				registerDropSelfLootTable(FORCE_LOG.get());
				registerDropSelfLootTable(FORCE_WOOD.get());
				registerDropSelfLootTable(FORCE_PLANKS.get());
				registerLootTable(FORCE_LEAVES.get(), (leaves) -> {
					return droppingWithChancesSticksAndApples(FORCE_PLANKS.get(), FORCE_SAPLING.get(), new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F});
				});

				registerDropSelfLootTable(FORCE_TORCH.get());
				registerDropSelfLootTable(WALL_FORCE_TORCH.get());
				registerDropSelfLootTable(TIME_TORCH.get());
				registerDropSelfLootTable(WALL_TIME_TORCH.get());

				//Bricks
				registerDropSelfLootTable(FORCE_BRICK_RED.get());
				registerDropSelfLootTable(FORCE_BRICK_YELLOW.get());
				registerDropSelfLootTable(FORCE_BRICK_GREEN.get());
				registerDropSelfLootTable(FORCE_BRICK_BLUE.get());
				registerDropSelfLootTable(FORCE_BRICK_WHITE.get());
				registerDropSelfLootTable(FORCE_BRICK_BLACK.get());
				registerDropSelfLootTable(FORCE_BRICK_BROWN.get());
				registerDropSelfLootTable(FORCE_BRICK_ORANGE.get());
				registerDropSelfLootTable(FORCE_BRICK_LIGHT_BLUE.get());
				registerDropSelfLootTable(FORCE_BRICK_MAGENTA.get());
				registerDropSelfLootTable(FORCE_BRICK_PINK.get());
				registerDropSelfLootTable(FORCE_BRICK_LIGHT_GRAY.get());
				registerDropSelfLootTable(FORCE_BRICK_LIME.get());
				registerDropSelfLootTable(FORCE_BRICK_CYAN.get());
				registerDropSelfLootTable(FORCE_BRICK_PURPLE.get());
				registerDropSelfLootTable(FORCE_BRICK_GRAY.get());
				registerDropSelfLootTable(FORCE_BRICK.get());

				//Stairs
				registerDropSelfLootTable(FORCE_PLANK_STAIRS.get());
				registerDropSelfLootTable(FORCE_BRICK_RED_STAIRS.get());
				registerDropSelfLootTable(FORCE_BRICK_YELLOW_STAIRS.get());
				registerDropSelfLootTable(FORCE_BRICK_GREEN_STAIRS.get());
				registerDropSelfLootTable(FORCE_BRICK_BLUE_STAIRS.get());
				registerDropSelfLootTable(FORCE_BRICK_WHITE_STAIRS.get());
				registerDropSelfLootTable(FORCE_BRICK_BLACK_STAIRS.get());
				registerDropSelfLootTable(FORCE_BRICK_BROWN_STAIRS.get());
				registerDropSelfLootTable(FORCE_BRICK_ORANGE_STAIRS.get());
				registerDropSelfLootTable(FORCE_BRICK_LIGHT_BLUE_STAIRS.get());
				registerDropSelfLootTable(FORCE_BRICK_MAGENTA_STAIRS.get());
				registerDropSelfLootTable(FORCE_BRICK_PINK_STAIRS.get());
				registerDropSelfLootTable(FORCE_BRICK_LIGHT_GRAY_STAIRS.get());
				registerDropSelfLootTable(FORCE_BRICK_LIME_STAIRS.get());
				registerDropSelfLootTable(FORCE_BRICK_CYAN_STAIRS.get());
				registerDropSelfLootTable(FORCE_BRICK_PURPLE_STAIRS.get());
				registerDropSelfLootTable(FORCE_BRICK_GRAY_STAIRS.get());
				registerDropSelfLootTable(FORCE_BRICK_STAIRS.get());

				//Slabs
				registerLootTable(FORCE_PLANK_SLAB.get(), (slab) -> {
					return droppingSlab((SlabBlock)FORCE_PLANK_SLAB.get());
				});
				registerLootTable(FORCE_BRICK_RED_SLAB.get(), (slab) -> {
					return droppingSlab((SlabBlock)FORCE_BRICK_RED_SLAB.get());
				});
				registerLootTable(FORCE_BRICK_YELLOW_SLAB.get(), (slab) -> {
					return droppingSlab((SlabBlock)FORCE_BRICK_YELLOW_SLAB.get());
				});
				registerLootTable(FORCE_BRICK_GREEN_SLAB.get(), (slab) -> {
					return droppingSlab((SlabBlock)FORCE_BRICK_GREEN_SLAB.get());
				});
				registerLootTable(FORCE_BRICK_BLUE_SLAB.get(), (slab) -> {
					return droppingSlab((SlabBlock)FORCE_BRICK_BLUE_SLAB.get());
				});
				registerLootTable(FORCE_BRICK_WHITE_SLAB.get(), (slab) -> {
					return droppingSlab((SlabBlock)FORCE_BRICK_WHITE_SLAB.get());
				});
				registerLootTable(FORCE_BRICK_BLACK_SLAB.get(), (slab) -> {
					return droppingSlab((SlabBlock)FORCE_BRICK_BLACK_SLAB.get());
				});
				registerLootTable(FORCE_BRICK_BROWN_SLAB.get(), (slab) -> {
					return droppingSlab((SlabBlock)FORCE_BRICK_BROWN_SLAB.get());
				});
				registerLootTable(FORCE_BRICK_ORANGE_SLAB.get(), (slab) -> {
					return droppingSlab((SlabBlock)FORCE_BRICK_ORANGE_SLAB.get());
				});
				registerLootTable(FORCE_BRICK_LIGHT_BLUE_SLAB.get(), (slab) -> {
					return droppingSlab((SlabBlock)FORCE_BRICK_LIGHT_BLUE_SLAB.get());
				});
				registerLootTable(FORCE_BRICK_MAGENTA_SLAB.get(), (slab) -> {
					return droppingSlab((SlabBlock)FORCE_BRICK_MAGENTA_SLAB.get());
				});
				registerLootTable(FORCE_BRICK_PINK_SLAB.get(), (slab) -> {
					return droppingSlab((SlabBlock)FORCE_BRICK_PINK_SLAB.get());
				});
				registerLootTable(FORCE_BRICK_LIGHT_GRAY_SLAB.get(), (slab) -> {
					return droppingSlab((SlabBlock)FORCE_BRICK_LIGHT_GRAY_SLAB.get());
				});
				registerLootTable(FORCE_BRICK_LIME_SLAB.get(), (slab) -> {
					return droppingSlab((SlabBlock)FORCE_BRICK_LIME_SLAB.get());
				});
				registerLootTable(FORCE_BRICK_CYAN_SLAB.get(), (slab) -> {
					return droppingSlab((SlabBlock)FORCE_BRICK_CYAN_SLAB.get());
				});
				registerLootTable(FORCE_BRICK_PURPLE_SLAB.get(), (slab) -> {
					return droppingSlab((SlabBlock)FORCE_BRICK_PURPLE_SLAB.get());
				});
				registerLootTable(FORCE_BRICK_GRAY_SLAB.get(), (slab) -> {
					return droppingSlab((SlabBlock)FORCE_BRICK_GRAY_SLAB.get());
				});
				registerLootTable(FORCE_BRICK_SLAB.get(), (slab) -> {
					return droppingSlab((SlabBlock)FORCE_BRICK_SLAB.get());
				});
			}

			@Override
			protected Iterable<Block> getKnownBlocks() {
				return (Iterable<Block>) ForceRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
			}
		}

		@Override
		protected void validate(Map<ResourceLocation, LootTable> map, @Nonnull ValidationTracker validationtracker) {
			map.forEach((name, table) -> LootTableManager.validateLootTable(validationtracker, name, table));
		}
	}


	private static class Recipes extends RecipeProvider {
		public Recipes(DataGenerator gen) {
			super(gen);
		}

		@Override
		protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
			//Stairs

			ShapedRecipeBuilder.shapedRecipe(FORCE_PLANK_STAIRS.get(), 4).key('#', FORCE_PLANKS.get()).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_planks", hasItem(FORCE_PLANKS.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_RED_STAIRS.get(), 4).key('#', FORCE_BRICK_RED.get()).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_RED.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_YELLOW_STAIRS.get(), 4).key('#', FORCE_BRICK_YELLOW.get()).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_YELLOW.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_GREEN_STAIRS.get(), 4).key('#', FORCE_BRICK_GREEN.get()).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_GREEN.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_BLUE_STAIRS.get(), 4).key('#', FORCE_BRICK_BLUE.get()).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_BLUE.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_WHITE_STAIRS.get(), 4).key('#', FORCE_BRICK_WHITE.get()).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_WHITE.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_BLACK_STAIRS.get(), 4).key('#', FORCE_BRICK_BLACK.get()).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_BLACK.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_BROWN_STAIRS.get(), 4).key('#', FORCE_BRICK_BROWN.get()).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_BROWN.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_ORANGE_STAIRS.get(), 4).key('#', FORCE_BRICK_ORANGE.get()).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_ORANGE.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_LIGHT_BLUE_STAIRS.get(), 4).key('#', FORCE_BRICK_LIGHT_BLUE.get()).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_LIGHT_BLUE.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_MAGENTA_STAIRS.get(), 4).key('#', FORCE_BRICK_MAGENTA.get()).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_MAGENTA.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_PINK_STAIRS.get(), 4).key('#', FORCE_BRICK_PINK.get()).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_PINK.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_LIGHT_GRAY_STAIRS.get(), 4).key('#', FORCE_BRICK_LIGHT_GRAY.get()).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_LIGHT_GRAY.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_LIME_STAIRS.get(), 4).key('#', FORCE_BRICK_LIME.get()).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_LIME.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_CYAN_STAIRS.get(), 4).key('#', FORCE_BRICK_CYAN.get()).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_CYAN.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_PURPLE_STAIRS.get(), 4).key('#', FORCE_BRICK_PURPLE.get()).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_PURPLE.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_GRAY_STAIRS.get(), 4).key('#', FORCE_BRICK_GRAY.get()).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_GRAY.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_STAIRS.get(), 4).key('#', FORCE_BRICK.get()).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK.get())).build(consumer);
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_RED.get()), FORCE_BRICK_RED_STAIRS.get()).addCriterion("has_bricks", hasItem(FORCE_BRICK_RED.get())).build(consumer, "force_brick_red_stairs_from_force_brick_red");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_YELLOW.get()), FORCE_BRICK_YELLOW_STAIRS.get()).addCriterion("has_bricks", hasItem(FORCE_BRICK_YELLOW.get())).build(consumer, "force_brick_yellow_stairs_from_force_brick_yellow");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_GREEN.get()), FORCE_BRICK_GREEN_STAIRS.get()).addCriterion("has_bricks", hasItem(FORCE_BRICK_GREEN.get())).build(consumer, "force_brick_green_stairs_from_force_brick_green");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_BLUE.get()), FORCE_BRICK_BLUE_STAIRS.get()).addCriterion("has_bricks", hasItem(FORCE_BRICK_BLUE.get())).build(consumer, "force_brick_blue_stairs_from_force_brick_blue");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_WHITE.get()), FORCE_BRICK_WHITE_STAIRS.get()).addCriterion("has_bricks", hasItem(FORCE_BRICK_WHITE.get())).build(consumer, "force_brick_white_stairs_from_force_brick_white");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_BLACK.get()), FORCE_BRICK_BLACK_STAIRS.get()).addCriterion("has_bricks", hasItem(FORCE_BRICK_BLACK.get())).build(consumer, "force_brick_black_stairs_from_force_brick_black");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_BROWN.get()), FORCE_BRICK_BROWN_STAIRS.get()).addCriterion("has_bricks", hasItem(FORCE_BRICK_BROWN.get())).build(consumer, "force_brick_brown_stairs_from_force_brick_brown");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_ORANGE.get()), FORCE_BRICK_ORANGE_STAIRS.get()).addCriterion("has_bricks", hasItem(FORCE_BRICK_ORANGE.get())).build(consumer, "force_brick_orange_stairs_from_force_brick_orange");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_LIGHT_BLUE.get()), FORCE_BRICK_LIGHT_BLUE_STAIRS.get()).addCriterion("has_bricks", hasItem(FORCE_BRICK_LIGHT_BLUE.get())).build(consumer, "force_brick_light_blue_stairs_from_force_brick_light_blue");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_MAGENTA.get()), FORCE_BRICK_MAGENTA_STAIRS.get()).addCriterion("has_bricks", hasItem(FORCE_BRICK_MAGENTA.get())).build(consumer, "force_brick_magenta_stairs_from_force_brick_magenta");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_PINK.get()), FORCE_BRICK_PINK_STAIRS.get()).addCriterion("has_bricks", hasItem(FORCE_BRICK_PINK.get())).build(consumer, "force_brick_pink_stairs_from_force_brick_pink");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_LIGHT_GRAY.get()), FORCE_BRICK_LIGHT_GRAY_STAIRS.get()).addCriterion("has_bricks", hasItem(FORCE_BRICK_LIGHT_GRAY.get())).build(consumer, "force_brick_light_gray_stairs_from_force_brick_light_gray");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_LIME.get()), FORCE_BRICK_LIME_STAIRS.get()).addCriterion("has_bricks", hasItem(FORCE_BRICK_LIME.get())).build(consumer, "force_brick_lime_stairs_from_force_brick_lime");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_CYAN.get()), FORCE_BRICK_CYAN_STAIRS.get()).addCriterion("has_bricks", hasItem(FORCE_BRICK_CYAN.get())).build(consumer, "force_brick_cyan_stairs_from_force_brick_cyan");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_PURPLE.get()), FORCE_BRICK_PURPLE_STAIRS.get()).addCriterion("has_bricks", hasItem(FORCE_BRICK_PURPLE.get())).build(consumer, "force_brick_purple_stairs_from_force_brick_purple");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_GRAY.get()), FORCE_BRICK_GRAY_STAIRS.get()).addCriterion("has_bricks", hasItem(FORCE_BRICK_GRAY.get())).build(consumer, "force_brick_gray_stairs_from_force_brick_gray");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK.get()), FORCE_BRICK_STAIRS.get()).addCriterion("has_bricks", hasItem(FORCE_BRICK.get())).build(consumer, "force_brick_stairs_from_force_brick");
			//Slabs
			ShapedRecipeBuilder.shapedRecipe(FORCE_PLANK_SLAB.get(), 6).key('#', FORCE_PLANKS.get()).patternLine("###").addCriterion("has_planks", hasItem(FORCE_PLANKS.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_RED_SLAB.get(), 6).key('#', FORCE_BRICK_RED.get()).patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_RED.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_YELLOW_SLAB.get(), 6).key('#', FORCE_BRICK_YELLOW.get()).patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_YELLOW.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_GREEN_SLAB.get(), 6).key('#', FORCE_BRICK_GREEN.get()).patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_GREEN.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_BLUE_SLAB.get(), 6).key('#', FORCE_BRICK_BLUE.get()).patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_BLUE.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_WHITE_SLAB.get(), 6).key('#', FORCE_BRICK_WHITE.get()).patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_WHITE.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_BLACK_SLAB.get(), 6).key('#', FORCE_BRICK_BLACK.get()).patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_BLACK.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_BROWN_SLAB.get(), 6).key('#', FORCE_BRICK_BROWN.get()).patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_BROWN.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_ORANGE_SLAB.get(), 6).key('#', FORCE_BRICK_ORANGE.get()).patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_ORANGE.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_LIGHT_BLUE_SLAB.get(), 6).key('#', FORCE_BRICK_LIGHT_BLUE.get()).patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_LIGHT_BLUE.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_MAGENTA_SLAB.get(), 6).key('#', FORCE_BRICK_MAGENTA.get()).patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_MAGENTA.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_PINK_SLAB.get(), 6).key('#', FORCE_BRICK_PINK.get()).patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_PINK.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_LIGHT_GRAY_SLAB.get(), 6).key('#', FORCE_BRICK_LIGHT_GRAY.get()).patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_LIGHT_GRAY.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_LIME_SLAB.get(), 6).key('#', FORCE_BRICK_LIME.get()).patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_LIME.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_CYAN_SLAB.get(), 6).key('#', FORCE_BRICK_CYAN.get()).patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_CYAN.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_PURPLE_SLAB.get(), 6).key('#', FORCE_BRICK_PURPLE.get()).patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_PURPLE.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_GRAY_SLAB.get(), 6).key('#', FORCE_BRICK_GRAY.get()).patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK_GRAY.get())).build(consumer);
			ShapedRecipeBuilder.shapedRecipe(FORCE_BRICK_SLAB.get(), 6).key('#', FORCE_BRICK.get()).patternLine("###").addCriterion("has_bricks", hasItem(FORCE_BRICK.get())).build(consumer);
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_RED.get()), FORCE_BRICK_RED_SLAB.get(), 2).addCriterion("has_bricks", hasItem(FORCE_BRICK_RED.get())).build(consumer, "force_brick_red_slab_from_force_brick_red");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_YELLOW.get()), FORCE_BRICK_YELLOW_SLAB.get(), 2).addCriterion("has_bricks", hasItem(FORCE_BRICK_YELLOW.get())).build(consumer, "force_brick_yellow_slab_from_force_brick_yellow");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_GREEN.get()), FORCE_BRICK_GREEN_SLAB.get(), 2).addCriterion("has_bricks", hasItem(FORCE_BRICK_GREEN.get())).build(consumer, "force_brick_green_slab_from_force_brick_green");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_BLUE.get()), FORCE_BRICK_BLUE_SLAB.get(), 2).addCriterion("has_bricks", hasItem(FORCE_BRICK_BLUE.get())).build(consumer, "force_brick_blue_slab_from_force_brick_blue");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_WHITE.get()), FORCE_BRICK_WHITE_SLAB.get(), 2).addCriterion("has_bricks", hasItem(FORCE_BRICK_WHITE.get())).build(consumer, "force_brick_white_slab_from_force_brick_white");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_BLACK.get()), FORCE_BRICK_BLACK_SLAB.get(), 2).addCriterion("has_bricks", hasItem(FORCE_BRICK_BLACK.get())).build(consumer, "force_brick_black_slab_from_force_brick_black");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_BROWN.get()), FORCE_BRICK_BROWN_SLAB.get(), 2).addCriterion("has_bricks", hasItem(FORCE_BRICK_BROWN.get())).build(consumer, "force_brick_brown_slab_from_force_brick_brown");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_ORANGE.get()), FORCE_BRICK_ORANGE_SLAB.get(), 2).addCriterion("has_bricks", hasItem(FORCE_BRICK_ORANGE.get())).build(consumer, "force_brick_orange_slab_from_force_brick_orange");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_LIGHT_BLUE.get()), FORCE_BRICK_LIGHT_BLUE_SLAB.get(), 2).addCriterion("has_bricks", hasItem(FORCE_BRICK_LIGHT_BLUE.get())).build(consumer, "force_brick_light_blue_slab_from_force_brick_light_blue");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_MAGENTA.get()), FORCE_BRICK_MAGENTA_SLAB.get(), 2).addCriterion("has_bricks", hasItem(FORCE_BRICK_MAGENTA.get())).build(consumer, "force_brick_magenta_slab_from_force_brick_magenta");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_PINK.get()), FORCE_BRICK_PINK_SLAB.get(), 2).addCriterion("has_bricks", hasItem(FORCE_BRICK_PINK.get())).build(consumer, "force_brick_pink_slab_from_force_brick_pink");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_LIGHT_GRAY.get()), FORCE_BRICK_LIGHT_GRAY_SLAB.get(), 2).addCriterion("has_bricks", hasItem(FORCE_BRICK_LIGHT_GRAY.get())).build(consumer, "force_brick_light_gray_slab_from_force_brick_light_gray");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_LIME.get()), FORCE_BRICK_LIME_SLAB.get(), 2).addCriterion("has_bricks", hasItem(FORCE_BRICK_LIME.get())).build(consumer, "force_brick_lime_slab_from_force_brick_lime");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_CYAN.get()), FORCE_BRICK_CYAN_SLAB.get(), 2).addCriterion("has_bricks", hasItem(FORCE_BRICK_CYAN.get())).build(consumer, "force_brick_cyan_slab_from_force_brick_cyan");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_PURPLE.get()), FORCE_BRICK_PURPLE_SLAB.get(), 2).addCriterion("has_bricks", hasItem(FORCE_BRICK_PURPLE.get())).build(consumer, "force_brick_purple_slab_from_force_brick_purple");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK_GRAY.get()), FORCE_BRICK_GRAY_SLAB.get(), 2).addCriterion("has_bricks", hasItem(FORCE_BRICK_GRAY.get())).build(consumer, "force_brick_gray_slab_from_force_brick_gray");
			SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(FORCE_BRICK.get()), FORCE_BRICK_SLAB.get(), 2).addCriterion("has_bricks", hasItem(FORCE_BRICK.get())).build(consumer, "force_brick_slab_from_force_brick");
		}

		@Override
		protected void saveRecipeAdvancement(DirectoryCache cache, JsonObject advancementJson, Path path) {
			// Nope
		}
	}

	private static class Language extends LanguageProvider {
		public Language(DataGenerator gen) {
			super(gen, Reference.MOD_ID, "en_us");
		}

		@Override
		protected void addTranslations() {
			//TODO: Make translations generate
		}
	}

	private static class ItemModels extends ItemModelProvider {
		public ItemModels(DataGenerator gen, ExistingFileHelper helper) {
			super(gen, Reference.MOD_ID, helper);
		}

		@Override
		protected void registerModels() {
			withExistingParent("power_ore", modLoc("block/power_ore"));

			withExistingParent("infuser", modLoc("block/infuser"));
			withExistingParent("force_furnace", modLoc("block/force_furnace"));

			withExistingParent("force_log", modLoc("block/force_log"));
			withExistingParent("force_wood", modLoc("block/force_wood"));
			withExistingParent("force_leaves", modLoc("block/force_leaves"));
			withExistingParent("force_planks", modLoc("block/force_planks"));

			withExistingParent("force_brick_red", modLoc("block/force_brick_red"));
			withExistingParent("force_brick_yellow", modLoc("block/force_brick_yellow"));
			withExistingParent("force_brick_green", modLoc("block/force_brick_green"));
			withExistingParent("force_brick_blue", modLoc("block/force_brick_blue"));
			withExistingParent("force_brick_white", modLoc("block/force_brick_white"));
			withExistingParent("force_brick_black", modLoc("block/force_brick_black"));
			withExistingParent("force_brick_brown", modLoc("block/force_brick_brown"));
			withExistingParent("force_brick_orange", modLoc("block/force_brick_orange"));
			withExistingParent("force_brick_light_blue", modLoc("block/force_brick_light_blue"));
			withExistingParent("force_brick_magenta", modLoc("block/force_brick_magenta"));
			withExistingParent("force_brick_pink", modLoc("block/force_brick_pink"));
			withExistingParent("force_brick_light_gray", modLoc("block/force_brick_light_gray"));
			withExistingParent("force_brick_lime", modLoc("block/force_brick_lime"));
			withExistingParent("force_brick_cyan", modLoc("block/force_brick_cyan"));
			withExistingParent("force_brick_purple", modLoc("block/force_brick_purple"));
			withExistingParent("force_brick_gray", modLoc("block/force_brick_gray"));
			withExistingParent("force_brick", modLoc("block/force_brick"));

			withExistingParent("force_plank_stairs", modLoc("block/force_plank_stairs"));
			withExistingParent("force_brick_red_stairs", modLoc("block/force_brick_red_stairs"));
			withExistingParent("force_brick_yellow_stairs", modLoc("block/force_brick_yellow_stairs"));
			withExistingParent("force_brick_green_stairs", modLoc("block/force_brick_green_stairs"));
			withExistingParent("force_brick_blue_stairs", modLoc("block/force_brick_blue_stairs"));
			withExistingParent("force_brick_white_stairs", modLoc("block/force_brick_white_stairs"));
			withExistingParent("force_brick_black_stairs", modLoc("block/force_brick_black_stairs"));
			withExistingParent("force_brick_brown_stairs", modLoc("block/force_brick_brown_stairs"));
			withExistingParent("force_brick_orange_stairs", modLoc("block/force_brick_orange_stairs"));
			withExistingParent("force_brick_light_blue_stairs", modLoc("block/force_brick_light_blue_stairs"));
			withExistingParent("force_brick_magenta_stairs", modLoc("block/force_brick_magenta_stairs"));
			withExistingParent("force_brick_pink_stairs", modLoc("block/force_brick_pink_stairs"));
			withExistingParent("force_brick_light_gray_stairs", modLoc("block/force_brick_light_gray_stairs"));
			withExistingParent("force_brick_lime_stairs", modLoc("block/force_brick_lime_stairs"));
			withExistingParent("force_brick_cyan_stairs", modLoc("block/force_brick_cyan_stairs"));
			withExistingParent("force_brick_purple_stairs", modLoc("block/force_brick_purple_stairs"));
			withExistingParent("force_brick_gray_stairs", modLoc("block/force_brick_gray_stairs"));
			withExistingParent("force_brick_stairs", modLoc("block/force_brick_stairs"));

			withExistingParent("force_plank_slab", modLoc("block/force_plank_slab"));
			withExistingParent("force_brick_red_slab", modLoc("block/force_brick_red_slab"));
			withExistingParent("force_brick_yellow_slab", modLoc("block/force_brick_yellow_slab"));
			withExistingParent("force_brick_green_slab", modLoc("block/force_brick_green_slab"));
			withExistingParent("force_brick_blue_slab", modLoc("block/force_brick_blue_slab"));
			withExistingParent("force_brick_white_slab", modLoc("block/force_brick_white_slab"));
			withExistingParent("force_brick_black_slab", modLoc("block/force_brick_black_slab"));
			withExistingParent("force_brick_brown_slab", modLoc("block/force_brick_brown_slab"));
			withExistingParent("force_brick_orange_slab", modLoc("block/force_brick_orange_slab"));
			withExistingParent("force_brick_light_blue_slab", modLoc("block/force_brick_light_blue_slab"));
			withExistingParent("force_brick_magenta_slab", modLoc("block/force_brick_magenta_slab"));
			withExistingParent("force_brick_pink_slab", modLoc("block/force_brick_pink_slab"));
			withExistingParent("force_brick_light_gray_slab", modLoc("block/force_brick_light_gray_slab"));
			withExistingParent("force_brick_lime_slab", modLoc("block/force_brick_lime_slab"));
			withExistingParent("force_brick_cyan_slab", modLoc("block/force_brick_cyan_slab"));
			withExistingParent("force_brick_purple_slab", modLoc("block/force_brick_purple_slab"));
			withExistingParent("force_brick_gray_slab", modLoc("block/force_brick_gray_slab"));
			withExistingParent("force_brick_slab", modLoc("block/force_brick_slab"));
		}

		@Override
		public String getName() {
			return "Item Models";
		}
	}

	private static class BlockStates extends BlockStateProvider {

		public BlockStates(DataGenerator gen, ExistingFileHelper helper) {
			super(gen, Reference.MOD_ID, helper);
		}

		@Override
		protected void registerStatesAndModels() {
			stairsBlock((StairsBlock) FORCE_PLANK_STAIRS.get(), modLoc("block/force_planks"));
			stairsBlock((StairsBlock) FORCE_BRICK_RED_STAIRS.get(), modLoc("block/force_brick_red"));
			stairsBlock((StairsBlock) FORCE_BRICK_YELLOW_STAIRS.get(), modLoc("block/force_brick_yellow"));
			stairsBlock((StairsBlock) FORCE_BRICK_GREEN_STAIRS.get(), modLoc("block/force_brick_green"));
			stairsBlock((StairsBlock) FORCE_BRICK_BLUE_STAIRS.get(), modLoc("block/force_brick_blue"));
			stairsBlock((StairsBlock) FORCE_BRICK_WHITE_STAIRS.get(), modLoc("block/force_brick_white"));
			stairsBlock((StairsBlock) FORCE_BRICK_BLACK_STAIRS.get(), modLoc("block/force_brick_black"));
			stairsBlock((StairsBlock) FORCE_BRICK_BROWN_STAIRS.get(), modLoc("block/force_brick_brown"));
			stairsBlock((StairsBlock) FORCE_BRICK_ORANGE_STAIRS.get(), modLoc("block/force_brick_orange"));
			stairsBlock((StairsBlock) FORCE_BRICK_LIGHT_BLUE_STAIRS.get(), modLoc("block/force_brick_light_blue"));
			stairsBlock((StairsBlock) FORCE_BRICK_MAGENTA_STAIRS.get(), modLoc("block/force_brick_magenta"));
			stairsBlock((StairsBlock) FORCE_BRICK_PINK_STAIRS.get(), modLoc("block/force_brick_pink"));
			stairsBlock((StairsBlock) FORCE_BRICK_LIGHT_GRAY_STAIRS.get(), modLoc("block/force_brick_light_gray"));
			stairsBlock((StairsBlock) FORCE_BRICK_LIME_STAIRS.get(), modLoc("block/force_brick_lime"));
			stairsBlock((StairsBlock) FORCE_BRICK_CYAN_STAIRS.get(), modLoc("block/force_brick_cyan"));
			stairsBlock((StairsBlock) FORCE_BRICK_PURPLE_STAIRS.get(), modLoc("block/force_brick_purple"));
			stairsBlock((StairsBlock) FORCE_BRICK_GRAY_STAIRS.get(), modLoc("block/force_brick_gray"));
			stairsBlock((StairsBlock) FORCE_BRICK_STAIRS.get(), modLoc("block/force_brick"));

			slabBlock((SlabBlock) FORCE_PLANK_SLAB.get(), modLoc("block/force_planks"), modLoc("block/force_planks"));
			slabBlock((SlabBlock) FORCE_BRICK_RED_SLAB.get(), modLoc("block/force_brick_red"), modLoc("block/force_brick_red"));
			slabBlock((SlabBlock) FORCE_BRICK_YELLOW_SLAB.get(), modLoc("block/force_brick_yellow"), modLoc("block/force_brick_yellow"));
			slabBlock((SlabBlock) FORCE_BRICK_GREEN_SLAB.get(), modLoc("block/force_brick_green"), modLoc("block/force_brick_green"));
			slabBlock((SlabBlock) FORCE_BRICK_BLUE_SLAB.get(), modLoc("block/force_brick_blue"), modLoc("block/force_brick_blue"));
			slabBlock((SlabBlock) FORCE_BRICK_WHITE_SLAB.get(), modLoc("block/force_brick_white"), modLoc("block/force_brick_white"));
			slabBlock((SlabBlock) FORCE_BRICK_BLACK_SLAB.get(), modLoc("block/force_brick_black"), modLoc("block/force_brick_black"));
			slabBlock((SlabBlock) FORCE_BRICK_BROWN_SLAB.get(), modLoc("block/force_brick_brown"), modLoc("block/force_brick_brown"));
			slabBlock((SlabBlock) FORCE_BRICK_ORANGE_SLAB.get(), modLoc("block/force_brick_orange"), modLoc("block/force_brick_orange"));
			slabBlock((SlabBlock) FORCE_BRICK_LIGHT_BLUE_SLAB.get(), modLoc("block/force_brick_light_blue"), modLoc("block/force_brick_light_blue"));
			slabBlock((SlabBlock) FORCE_BRICK_MAGENTA_SLAB.get(), modLoc("block/force_brick_magenta"), modLoc("block/force_brick_magenta"));
			slabBlock((SlabBlock) FORCE_BRICK_PINK_SLAB.get(), modLoc("block/force_brick_pink"), modLoc("block/force_brick_pink"));
			slabBlock((SlabBlock) FORCE_BRICK_LIGHT_GRAY_SLAB.get(), modLoc("block/force_brick_light_gray"), modLoc("block/force_brick_light_gray"));
			slabBlock((SlabBlock) FORCE_BRICK_LIME_SLAB.get(), modLoc("block/force_brick_lime"), modLoc("block/force_brick_lime"));
			slabBlock((SlabBlock) FORCE_BRICK_CYAN_SLAB.get(), modLoc("block/force_brick_cyan"), modLoc("block/force_brick_cyan"));
			slabBlock((SlabBlock) FORCE_BRICK_PURPLE_SLAB.get(), modLoc("block/force_brick_purple"), modLoc("block/force_brick_purple"));
			slabBlock((SlabBlock) FORCE_BRICK_GRAY_SLAB.get(), modLoc("block/force_brick_gray"), modLoc("block/force_brick_gray"));
			slabBlock((SlabBlock) FORCE_BRICK_SLAB.get(), modLoc("block/force_brick"), modLoc("block/force_brick"));
		}
	}
}
