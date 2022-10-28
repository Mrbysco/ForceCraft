package com.mrbysco.forcecraft.datagen;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.datagen.patchouli.PatchouliProvider;
import com.mrbysco.forcecraft.registry.ForceRegistry;
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

import static com.mrbysco.forcecraft.registry.ForceRegistry.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForceDataGen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(new Loots(generator));
//			generator.addProvider(new Recipes(generator));
			generator.addProvider(new PatchouliProvider(generator));
		}
		if (event.includeClient()) {
//			generator.addProvider(new Language(generator));
//			generator.addProvider(new BlockStates(generator, helper));
//			generator.addProvider(new ItemModels(generator, helper));
		}
	}

	private static class Loots extends LootTableProvider {
		public Loots(DataGenerator gen) {
			super(gen);
		}

		@Override
		protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
			return ImmutableList.of(Pair.of(ForceBlocks::new, LootParameterSets.BLOCK), Pair.of(SpoilsBagLootTables::new, LootParameterSets.GIFT));
		}

		private class ForceBlocks extends BlockLootTables {
			@Override
			protected void addTables() {
				add(POWER_ORE.get(), (ore) -> {
					return createSilkTouchDispatchTable(ore, applyExplosionDecay(POWER_ORE_ITEM.get(), ItemLootEntry.lootTableItem(FORCE_GEM.get()).apply(SetCount.setCount(RandomValueRange.between(2.0F, 4.0F))).apply(ApplyBonus.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))));
				});

				dropSelf(INFUSER.get());
				add(FORCE_FURNACE.get(), (furnace) -> createNameableBlockEntityTable(furnace));
				add(BLACK_FORCE_FURNACE.get(), (furnace) -> createNameableBlockEntityTable(furnace));
				add(BLUE_FORCE_FURNACE.get(), (furnace) -> createNameableBlockEntityTable(furnace));
				add(BROWN_FORCE_FURNACE.get(), (furnace) -> createNameableBlockEntityTable(furnace));
				add(CYAN_FORCE_FURNACE.get(), (furnace) -> createNameableBlockEntityTable(furnace));
				add(GRAY_FORCE_FURNACE.get(), (furnace) -> createNameableBlockEntityTable(furnace));
				add(GREEN_FORCE_FURNACE.get(), (furnace) -> createNameableBlockEntityTable(furnace));
				add(LIGHT_BLUE_FORCE_FURNACE.get(), (furnace) -> createNameableBlockEntityTable(furnace));
				add(LIGHT_GRAY_FORCE_FURNACE.get(), (furnace) -> createNameableBlockEntityTable(furnace));
				add(LIME_FORCE_FURNACE.get(), (furnace) -> createNameableBlockEntityTable(furnace));
				add(MAGENTA_FORCE_FURNACE.get(), (furnace) -> createNameableBlockEntityTable(furnace));
				add(ORANGE_FORCE_FURNACE.get(), (furnace) -> createNameableBlockEntityTable(furnace));
				add(PINK_FORCE_FURNACE.get(), (furnace) -> createNameableBlockEntityTable(furnace));
				add(PURPLE_FORCE_FURNACE.get(), (furnace) -> createNameableBlockEntityTable(furnace));
				add(RED_FORCE_FURNACE.get(), (furnace) -> createNameableBlockEntityTable(furnace));
				add(WHITE_FORCE_FURNACE.get(), (furnace) -> createNameableBlockEntityTable(furnace));

				add(FORCE_ENGINE.get(), (engine) -> createNameableBlockEntityTable(engine));

				dropSelf(FORCE_SAPLING.get());
				dropSelf(FORCE_LOG.get());
				dropSelf(FORCE_WOOD.get());
				dropSelf(FORCE_PLANKS.get());
				add(FORCE_LEAVES.get(), (leaves) -> {
					return createOakLeavesDrops(leaves, FORCE_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES);
				});

				dropSelf(FORCE_TORCH.get());
				dropSelf(FORCE_RED_TORCH.get());
				dropSelf(FORCE_ORANGE_TORCH.get());
				dropSelf(FORCE_GREEN_TORCH.get());
				dropSelf(FORCE_BLUE_TORCH.get());
				dropSelf(FORCE_WHITE_TORCH.get());
				dropSelf(FORCE_BLACK_TORCH.get());
				dropSelf(FORCE_BROWN_TORCH.get());
				dropSelf(FORCE_LIGHT_BLUE_TORCH.get());
				dropSelf(FORCE_MAGENTA_TORCH.get());
				dropSelf(FORCE_PINK_TORCH.get());
				dropSelf(FORCE_LIGHT_GRAY_TORCH.get());
				dropSelf(FORCE_LIME_TORCH.get());
				dropSelf(FORCE_CYAN_TORCH.get());
				dropSelf(FORCE_PURPLE_TORCH.get());
				dropSelf(FORCE_GRAY_TORCH.get());
				dropSelf(TIME_TORCH.get());

				dropOther(WALL_FORCE_TORCH.get(), FORCE_TORCH.get());
				dropOther(WALL_FORCE_RED_TORCH.get(), FORCE_RED_TORCH.get());
				dropOther(WALL_FORCE_ORANGE_TORCH.get(), FORCE_ORANGE_TORCH.get());
				dropOther(WALL_FORCE_GREEN_TORCH.get(), FORCE_GREEN_TORCH.get());
				dropOther(WALL_FORCE_BLUE_TORCH.get(), FORCE_BLUE_TORCH.get());
				dropOther(WALL_FORCE_WHITE_TORCH.get(), FORCE_WHITE_TORCH.get());
				dropOther(WALL_FORCE_BLACK_TORCH.get(), FORCE_BLACK_TORCH.get());
				dropOther(WALL_FORCE_BROWN_TORCH.get(), FORCE_BROWN_TORCH.get());
				dropOther(WALL_FORCE_LIGHT_BLUE_TORCH.get(), FORCE_LIGHT_BLUE_TORCH.get());
				dropOther(WALL_FORCE_MAGENTA_TORCH.get(), FORCE_MAGENTA_TORCH.get());
				dropOther(WALL_FORCE_PINK_TORCH.get(), FORCE_PINK_TORCH.get());
				dropOther(WALL_FORCE_LIGHT_GRAY_TORCH.get(), FORCE_LIGHT_GRAY_TORCH.get());
				dropOther(WALL_FORCE_LIME_TORCH.get(), FORCE_LIME_TORCH.get());
				dropOther(WALL_FORCE_CYAN_TORCH.get(), FORCE_CYAN_TORCH.get());
				dropOther(WALL_FORCE_PURPLE_TORCH.get(), FORCE_PURPLE_TORCH.get());
				dropOther(WALL_FORCE_GRAY_TORCH.get(), FORCE_GRAY_TORCH.get());
				dropSelf(WALL_TIME_TORCH.get());

				//Bricks
				dropSelf(FORCE_BRICK_RED.get());
				dropSelf(FORCE_BRICK_YELLOW.get());
				dropSelf(FORCE_BRICK_GREEN.get());
				dropSelf(FORCE_BRICK_BLUE.get());
				dropSelf(FORCE_BRICK_WHITE.get());
				dropSelf(FORCE_BRICK_BLACK.get());
				dropSelf(FORCE_BRICK_BROWN.get());
				dropSelf(FORCE_BRICK_ORANGE.get());
				dropSelf(FORCE_BRICK_LIGHT_BLUE.get());
				dropSelf(FORCE_BRICK_MAGENTA.get());
				dropSelf(FORCE_BRICK_PINK.get());
				dropSelf(FORCE_BRICK_LIGHT_GRAY.get());
				dropSelf(FORCE_BRICK_LIME.get());
				dropSelf(FORCE_BRICK_CYAN.get());
				dropSelf(FORCE_BRICK_PURPLE.get());
				dropSelf(FORCE_BRICK_GRAY.get());
				dropSelf(FORCE_BRICK.get());

				//Stairs
				dropSelf(FORCE_PLANK_STAIRS.get());
				dropSelf(FORCE_BRICK_RED_STAIRS.get());
				dropSelf(FORCE_BRICK_YELLOW_STAIRS.get());
				dropSelf(FORCE_BRICK_GREEN_STAIRS.get());
				dropSelf(FORCE_BRICK_BLUE_STAIRS.get());
				dropSelf(FORCE_BRICK_WHITE_STAIRS.get());
				dropSelf(FORCE_BRICK_BLACK_STAIRS.get());
				dropSelf(FORCE_BRICK_BROWN_STAIRS.get());
				dropSelf(FORCE_BRICK_ORANGE_STAIRS.get());
				dropSelf(FORCE_BRICK_LIGHT_BLUE_STAIRS.get());
				dropSelf(FORCE_BRICK_MAGENTA_STAIRS.get());
				dropSelf(FORCE_BRICK_PINK_STAIRS.get());
				dropSelf(FORCE_BRICK_LIGHT_GRAY_STAIRS.get());
				dropSelf(FORCE_BRICK_LIME_STAIRS.get());
				dropSelf(FORCE_BRICK_CYAN_STAIRS.get());
				dropSelf(FORCE_BRICK_PURPLE_STAIRS.get());
				dropSelf(FORCE_BRICK_GRAY_STAIRS.get());
				dropSelf(FORCE_BRICK_STAIRS.get());

				//Slabs
				add(FORCE_PLANK_SLAB.get(), (slab) -> {
					return createSlabItemTable((SlabBlock) slab);
				});
				add(FORCE_BRICK_RED_SLAB.get(), (slab) -> {
					return createSlabItemTable((SlabBlock) slab);
				});
				add(FORCE_BRICK_YELLOW_SLAB.get(), (slab) -> {
					return createSlabItemTable((SlabBlock) slab);
				});
				add(FORCE_BRICK_GREEN_SLAB.get(), (slab) -> {
					return createSlabItemTable((SlabBlock) slab);
				});
				add(FORCE_BRICK_BLUE_SLAB.get(), (slab) -> {
					return createSlabItemTable((SlabBlock) slab);
				});
				add(FORCE_BRICK_WHITE_SLAB.get(), (slab) -> {
					return createSlabItemTable((SlabBlock) slab);
				});
				add(FORCE_BRICK_BLACK_SLAB.get(), (slab) -> {
					return createSlabItemTable((SlabBlock) slab);
				});
				add(FORCE_BRICK_BROWN_SLAB.get(), (slab) -> {
					return createSlabItemTable((SlabBlock) slab);
				});
				add(FORCE_BRICK_ORANGE_SLAB.get(), (slab) -> {
					return createSlabItemTable((SlabBlock) slab);
				});
				add(FORCE_BRICK_LIGHT_BLUE_SLAB.get(), (slab) -> {
					return createSlabItemTable((SlabBlock) slab);
				});
				add(FORCE_BRICK_MAGENTA_SLAB.get(), (slab) -> {
					return createSlabItemTable((SlabBlock) slab);
				});
				add(FORCE_BRICK_PINK_SLAB.get(), (slab) -> {
					return createSlabItemTable((SlabBlock) slab);
				});
				add(FORCE_BRICK_LIGHT_GRAY_SLAB.get(), (slab) -> {
					return createSlabItemTable((SlabBlock) slab);
				});
				add(FORCE_BRICK_LIME_SLAB.get(), (slab) -> {
					return createSlabItemTable((SlabBlock) slab);
				});
				add(FORCE_BRICK_CYAN_SLAB.get(), (slab) -> {
					return createSlabItemTable((SlabBlock) slab);
				});
				add(FORCE_BRICK_PURPLE_SLAB.get(), (slab) -> {
					return createSlabItemTable((SlabBlock) slab);
				});
				add(FORCE_BRICK_GRAY_SLAB.get(), (slab) -> {
					return createSlabItemTable((SlabBlock) slab);
				});
				add(FORCE_BRICK_SLAB.get(), (slab) -> {
					return createSlabItemTable((SlabBlock) slab);
				});
			}

			@Override
			protected Iterable<Block> getKnownBlocks() {
				return (Iterable<Block>) ForceRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
			}
		}

		@Override
		protected void validate(Map<ResourceLocation, LootTable> map, @Nonnull ValidationTracker validationtracker) {
//			map.forEach((name, table) -> LootTableManager.validateLootTable(validationtracker, name, table));
		}
	}


	private static class Recipes extends RecipeProvider {
		public Recipes(DataGenerator gen) {
			super(gen);
		}

		@Override
		protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
			//Stairs
			ShapedRecipeBuilder.shaped(FORCE_PLANK_STAIRS.get(), 4).define('#', FORCE_PLANKS.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_planks", has(FORCE_PLANKS.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_RED_STAIRS.get(), 4).define('#', FORCE_BRICK_RED.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_RED.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_YELLOW_STAIRS.get(), 4).define('#', FORCE_BRICK_YELLOW.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_YELLOW.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_GREEN_STAIRS.get(), 4).define('#', FORCE_BRICK_GREEN.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_GREEN.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_BLUE_STAIRS.get(), 4).define('#', FORCE_BRICK_BLUE.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_BLUE.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_WHITE_STAIRS.get(), 4).define('#', FORCE_BRICK_WHITE.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_WHITE.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_BLACK_STAIRS.get(), 4).define('#', FORCE_BRICK_BLACK.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_BLACK.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_BROWN_STAIRS.get(), 4).define('#', FORCE_BRICK_BROWN.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_BROWN.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_ORANGE_STAIRS.get(), 4).define('#', FORCE_BRICK_ORANGE.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_ORANGE.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_LIGHT_BLUE_STAIRS.get(), 4).define('#', FORCE_BRICK_LIGHT_BLUE.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_LIGHT_BLUE.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_MAGENTA_STAIRS.get(), 4).define('#', FORCE_BRICK_MAGENTA.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_MAGENTA.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_PINK_STAIRS.get(), 4).define('#', FORCE_BRICK_PINK.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_PINK.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_LIGHT_GRAY_STAIRS.get(), 4).define('#', FORCE_BRICK_LIGHT_GRAY.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_LIGHT_GRAY.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_LIME_STAIRS.get(), 4).define('#', FORCE_BRICK_LIME.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_LIME.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_CYAN_STAIRS.get(), 4).define('#', FORCE_BRICK_CYAN.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_CYAN.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_PURPLE_STAIRS.get(), 4).define('#', FORCE_BRICK_PURPLE.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_PURPLE.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_GRAY_STAIRS.get(), 4).define('#', FORCE_BRICK_GRAY.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_GRAY.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_STAIRS.get(), 4).define('#', FORCE_BRICK.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK.get())).save(consumer);
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_RED.get()), FORCE_BRICK_RED_STAIRS.get()).unlocks("has_bricks", has(FORCE_BRICK_RED.get())).save(consumer, "force_brick_red_stairs_from_force_brick_red");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_YELLOW.get()), FORCE_BRICK_YELLOW_STAIRS.get()).unlocks("has_bricks", has(FORCE_BRICK_YELLOW.get())).save(consumer, "force_brick_yellow_stairs_from_force_brick_yellow");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_GREEN.get()), FORCE_BRICK_GREEN_STAIRS.get()).unlocks("has_bricks", has(FORCE_BRICK_GREEN.get())).save(consumer, "force_brick_green_stairs_from_force_brick_green");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_BLUE.get()), FORCE_BRICK_BLUE_STAIRS.get()).unlocks("has_bricks", has(FORCE_BRICK_BLUE.get())).save(consumer, "force_brick_blue_stairs_from_force_brick_blue");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_WHITE.get()), FORCE_BRICK_WHITE_STAIRS.get()).unlocks("has_bricks", has(FORCE_BRICK_WHITE.get())).save(consumer, "force_brick_white_stairs_from_force_brick_white");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_BLACK.get()), FORCE_BRICK_BLACK_STAIRS.get()).unlocks("has_bricks", has(FORCE_BRICK_BLACK.get())).save(consumer, "force_brick_black_stairs_from_force_brick_black");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_BROWN.get()), FORCE_BRICK_BROWN_STAIRS.get()).unlocks("has_bricks", has(FORCE_BRICK_BROWN.get())).save(consumer, "force_brick_brown_stairs_from_force_brick_brown");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_ORANGE.get()), FORCE_BRICK_ORANGE_STAIRS.get()).unlocks("has_bricks", has(FORCE_BRICK_ORANGE.get())).save(consumer, "force_brick_orange_stairs_from_force_brick_orange");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_LIGHT_BLUE.get()), FORCE_BRICK_LIGHT_BLUE_STAIRS.get()).unlocks("has_bricks", has(FORCE_BRICK_LIGHT_BLUE.get())).save(consumer, "force_brick_light_blue_stairs_from_force_brick_light_blue");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_MAGENTA.get()), FORCE_BRICK_MAGENTA_STAIRS.get()).unlocks("has_bricks", has(FORCE_BRICK_MAGENTA.get())).save(consumer, "force_brick_magenta_stairs_from_force_brick_magenta");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_PINK.get()), FORCE_BRICK_PINK_STAIRS.get()).unlocks("has_bricks", has(FORCE_BRICK_PINK.get())).save(consumer, "force_brick_pink_stairs_from_force_brick_pink");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_LIGHT_GRAY.get()), FORCE_BRICK_LIGHT_GRAY_STAIRS.get()).unlocks("has_bricks", has(FORCE_BRICK_LIGHT_GRAY.get())).save(consumer, "force_brick_light_gray_stairs_from_force_brick_light_gray");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_LIME.get()), FORCE_BRICK_LIME_STAIRS.get()).unlocks("has_bricks", has(FORCE_BRICK_LIME.get())).save(consumer, "force_brick_lime_stairs_from_force_brick_lime");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_CYAN.get()), FORCE_BRICK_CYAN_STAIRS.get()).unlocks("has_bricks", has(FORCE_BRICK_CYAN.get())).save(consumer, "force_brick_cyan_stairs_from_force_brick_cyan");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_PURPLE.get()), FORCE_BRICK_PURPLE_STAIRS.get()).unlocks("has_bricks", has(FORCE_BRICK_PURPLE.get())).save(consumer, "force_brick_purple_stairs_from_force_brick_purple");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_GRAY.get()), FORCE_BRICK_GRAY_STAIRS.get()).unlocks("has_bricks", has(FORCE_BRICK_GRAY.get())).save(consumer, "force_brick_gray_stairs_from_force_brick_gray");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK.get()), FORCE_BRICK_STAIRS.get()).unlocks("has_bricks", has(FORCE_BRICK.get())).save(consumer, "force_brick_stairs_from_force_brick");
			//Slabs
			ShapedRecipeBuilder.shaped(FORCE_PLANK_SLAB.get(), 6).define('#', FORCE_PLANKS.get()).pattern("###").unlockedBy("has_planks", has(FORCE_PLANKS.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_RED_SLAB.get(), 6).define('#', FORCE_BRICK_RED.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_RED.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_YELLOW_SLAB.get(), 6).define('#', FORCE_BRICK_YELLOW.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_YELLOW.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_GREEN_SLAB.get(), 6).define('#', FORCE_BRICK_GREEN.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_GREEN.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_BLUE_SLAB.get(), 6).define('#', FORCE_BRICK_BLUE.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_BLUE.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_WHITE_SLAB.get(), 6).define('#', FORCE_BRICK_WHITE.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_WHITE.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_BLACK_SLAB.get(), 6).define('#', FORCE_BRICK_BLACK.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_BLACK.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_BROWN_SLAB.get(), 6).define('#', FORCE_BRICK_BROWN.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_BROWN.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_ORANGE_SLAB.get(), 6).define('#', FORCE_BRICK_ORANGE.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_ORANGE.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_LIGHT_BLUE_SLAB.get(), 6).define('#', FORCE_BRICK_LIGHT_BLUE.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_LIGHT_BLUE.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_MAGENTA_SLAB.get(), 6).define('#', FORCE_BRICK_MAGENTA.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_MAGENTA.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_PINK_SLAB.get(), 6).define('#', FORCE_BRICK_PINK.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_PINK.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_LIGHT_GRAY_SLAB.get(), 6).define('#', FORCE_BRICK_LIGHT_GRAY.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_LIGHT_GRAY.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_LIME_SLAB.get(), 6).define('#', FORCE_BRICK_LIME.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_LIME.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_CYAN_SLAB.get(), 6).define('#', FORCE_BRICK_CYAN.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_CYAN.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_PURPLE_SLAB.get(), 6).define('#', FORCE_BRICK_PURPLE.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_PURPLE.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_GRAY_SLAB.get(), 6).define('#', FORCE_BRICK_GRAY.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_GRAY.get())).save(consumer);
			ShapedRecipeBuilder.shaped(FORCE_BRICK_SLAB.get(), 6).define('#', FORCE_BRICK.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK.get())).save(consumer);
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_RED.get()), FORCE_BRICK_RED_SLAB.get(), 2).unlocks("has_bricks", has(FORCE_BRICK_RED.get())).save(consumer, "force_brick_red_slab_from_force_brick_red");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_YELLOW.get()), FORCE_BRICK_YELLOW_SLAB.get(), 2).unlocks("has_bricks", has(FORCE_BRICK_YELLOW.get())).save(consumer, "force_brick_yellow_slab_from_force_brick_yellow");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_GREEN.get()), FORCE_BRICK_GREEN_SLAB.get(), 2).unlocks("has_bricks", has(FORCE_BRICK_GREEN.get())).save(consumer, "force_brick_green_slab_from_force_brick_green");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_BLUE.get()), FORCE_BRICK_BLUE_SLAB.get(), 2).unlocks("has_bricks", has(FORCE_BRICK_BLUE.get())).save(consumer, "force_brick_blue_slab_from_force_brick_blue");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_WHITE.get()), FORCE_BRICK_WHITE_SLAB.get(), 2).unlocks("has_bricks", has(FORCE_BRICK_WHITE.get())).save(consumer, "force_brick_white_slab_from_force_brick_white");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_BLACK.get()), FORCE_BRICK_BLACK_SLAB.get(), 2).unlocks("has_bricks", has(FORCE_BRICK_BLACK.get())).save(consumer, "force_brick_black_slab_from_force_brick_black");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_BROWN.get()), FORCE_BRICK_BROWN_SLAB.get(), 2).unlocks("has_bricks", has(FORCE_BRICK_BROWN.get())).save(consumer, "force_brick_brown_slab_from_force_brick_brown");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_ORANGE.get()), FORCE_BRICK_ORANGE_SLAB.get(), 2).unlocks("has_bricks", has(FORCE_BRICK_ORANGE.get())).save(consumer, "force_brick_orange_slab_from_force_brick_orange");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_LIGHT_BLUE.get()), FORCE_BRICK_LIGHT_BLUE_SLAB.get(), 2).unlocks("has_bricks", has(FORCE_BRICK_LIGHT_BLUE.get())).save(consumer, "force_brick_light_blue_slab_from_force_brick_light_blue");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_MAGENTA.get()), FORCE_BRICK_MAGENTA_SLAB.get(), 2).unlocks("has_bricks", has(FORCE_BRICK_MAGENTA.get())).save(consumer, "force_brick_magenta_slab_from_force_brick_magenta");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_PINK.get()), FORCE_BRICK_PINK_SLAB.get(), 2).unlocks("has_bricks", has(FORCE_BRICK_PINK.get())).save(consumer, "force_brick_pink_slab_from_force_brick_pink");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_LIGHT_GRAY.get()), FORCE_BRICK_LIGHT_GRAY_SLAB.get(), 2).unlocks("has_bricks", has(FORCE_BRICK_LIGHT_GRAY.get())).save(consumer, "force_brick_light_gray_slab_from_force_brick_light_gray");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_LIME.get()), FORCE_BRICK_LIME_SLAB.get(), 2).unlocks("has_bricks", has(FORCE_BRICK_LIME.get())).save(consumer, "force_brick_lime_slab_from_force_brick_lime");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_CYAN.get()), FORCE_BRICK_CYAN_SLAB.get(), 2).unlocks("has_bricks", has(FORCE_BRICK_CYAN.get())).save(consumer, "force_brick_cyan_slab_from_force_brick_cyan");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_PURPLE.get()), FORCE_BRICK_PURPLE_SLAB.get(), 2).unlocks("has_bricks", has(FORCE_BRICK_PURPLE.get())).save(consumer, "force_brick_purple_slab_from_force_brick_purple");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_GRAY.get()), FORCE_BRICK_GRAY_SLAB.get(), 2).unlocks("has_bricks", has(FORCE_BRICK_GRAY.get())).save(consumer, "force_brick_gray_slab_from_force_brick_gray");
			SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK.get()), FORCE_BRICK_SLAB.get(), 2).unlocks("has_bricks", has(FORCE_BRICK.get())).save(consumer, "force_brick_slab_from_force_brick");
		}

		@Override
		protected void saveAdvancement(DirectoryCache cache, JsonObject advancementJson, Path path) {
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

			singleTexture("force_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch"));
			singleTexture("force_red_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_red"));
			singleTexture("force_orange_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_orange"));
			singleTexture("force_green_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_green"));
			singleTexture("force_blue_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_blue"));
			singleTexture("force_white_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_white"));
			singleTexture("force_black_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_black"));
			singleTexture("force_brown_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_brown"));
			singleTexture("force_light_blue_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_light_blue"));
			singleTexture("force_magenta_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_magenta"));
			singleTexture("force_pink_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_pink"));
			singleTexture("force_light_gray_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_light_gray"));
			singleTexture("force_lime_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_lime"));
			singleTexture("force_cyan_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_cyan"));
			singleTexture("force_purple_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_purple"));
			singleTexture("force_gray_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_gray"));
			singleTexture("time_torch", mcLoc("item/generated"), "layer0", modLoc("block/time_torch"));

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