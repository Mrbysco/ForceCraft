package com.mrbysco.forcecraft.datagen.server;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.datagen.server.builder.InfuseRecipeBuilder;
import com.mrbysco.forcecraft.datagen.server.builder.MultiOutputRecipeBuilder;
import com.mrbysco.forcecraft.datagen.server.builder.TransmutationRecipeBuilder;
import com.mrbysco.forcecraft.recipe.condition.TorchEnabledCondition;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import com.mrbysco.forcecraft.registry.helper.ForceBrickReg;
import com.mrbysco.forcecraft.registry.helper.ForceFurnaceReg;
import com.mrbysco.forcecraft.registry.helper.ForceTorchReg;
import com.mrbysco.forcecraft.upgrade.FlagUpgrade;
import com.mrbysco.forcecraft.upgrade.LeveledUpgrade;
import com.mrbysco.forcecraft.upgrade.PackUpgrade;
import com.mrbysco.forcecraft.upgrade.UpgradeBookTier;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.util.concurrent.CompletableFuture;

public class ForceRecipeProvider extends RecipeProvider {

	public ForceRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
		super(provider, recipeOutput);
	}

	@Override
	protected void buildRecipes() {
		generateWoodRecipes();
		generateBrickRecipes();
		shaped(RecipeCategory.MISC, ForceRegistry.FORCE_INGOT, 3)
				.pattern("GI")
				.pattern("I ")
				.define('G', ForceRegistry.FORCE_GEM)
				.define('I', Tags.Items.INGOTS_GOLD)
				.unlockedBy("has_force_gem", this.has(ForceRegistry.FORCE_GEM))
				.unlockedBy("has_ingot", this.has(Tags.Items.INGOTS_GOLD))
				.save(this.output, ResourceKey.create(Registries.RECIPE, Identifier.parse("forcecraft:force_ingot_from_gold")));
		shaped(RecipeCategory.MISC, ForceRegistry.FORCE_INGOT, 2)
				.pattern("GI")
				.pattern("I ")
				.define('G', ForceRegistry.FORCE_GEM)
				.define('I', Tags.Items.INGOTS_IRON)
				.unlockedBy("has_force_gem", this.has(ForceRegistry.FORCE_GEM))
				.unlockedBy("has_ingot", this.has(Tags.Items.INGOTS_IRON))
				.save(this.output, ResourceKey.create(Registries.RECIPE, Identifier.parse("forcecraft:force_ingot_from_iron")));
		shapeless(RecipeCategory.MISC, ForceRegistry.FORCE_NUGGET, 9)
				.requires(ForceRegistry.FORCE_INGOT)
				.unlockedBy("has_force_ingot", this.has(ForceRegistry.FORCE_INGOT))
				.save(this.output, ResourceKey.create(Registries.RECIPE, Identifier.parse("forcecraft:force_nugget_from_ingot")));
		shaped(RecipeCategory.MISC, ForceRegistry.FORCE_INGOT)
				.define('#', ForceRegistry.FORCE_NUGGET)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_force_nugget", this.has(ForceRegistry.FORCE_NUGGET))
				.save(this.output, ResourceKey.create(Registries.RECIPE, Identifier.parse("forcecraft:force_ingot_from_nuggets")));

		SimpleCookingRecipeBuilder.smelting(tag(ForceTags.Items.FORCE_LOGS), RecipeCategory.MISC, CookingBookCategory.MISC,
						ForceRegistry.GOLDEN_POWER_SOURCE.get(), 0.15F, 200)
				.unlockedBy("has_force_logs", has(ForceTags.Items.FORCE_LOGS))
				.save(output);
		shaped(RecipeCategory.TOOLS, ForceRegistry.BOMB, 8)
				.pattern("FFF")
				.pattern("FGF")
				.pattern("FFF")
				.define('F', Items.FIRE_CHARGE)
				.define('G', ForceRegistry.GOLDEN_POWER_SOURCE)
				.unlockedBy("has_golden_power_source", this.has(ForceRegistry.GOLDEN_POWER_SOURCE))
				.unlockedBy("has_fire_charge", this.has(Items.FIRE_CHARGE))
				.save(this.output);
		shaped(RecipeCategory.TOOLS, ForceRegistry.SUPER_BOMB)
				.pattern("BBB")
				.pattern("BOB")
				.pattern("BBB")
				.define('B', ForceRegistry.BOMB)
				.define('O', ForceTags.Items.ORES_POWER)
				.unlockedBy("has_bomb", this.has(ForceRegistry.BOMB))
				.save(this.output);


		for (ForceTorchReg reg : ForceRegistry.getTorches()) {
			if (reg.getColor() != null) {
				TagKey<Item> tag = reg.getColor().getTag();
				shaped(RecipeCategory.MISC, reg.getTorchItem(), 8)
						.pattern("BBB")
						.pattern("BDB")
						.pattern("BBB")
						.define('B', ForceTags.Items.FORCE_TORCHES)
						.define('D', tag)
						.unlockedBy("has_torch", has(ForceTags.Items.FORCE_TORCHES))
						.unlockedBy("has_dye", has(tag))
						.save(output);
			} else {
				shaped(RecipeCategory.MISC, reg.getTorchItem(), 6)
						.pattern("G")
						.pattern("S")
						.define('G', ForceRegistry.GOLDEN_POWER_SOURCE)
						.define('S', ForceRegistry.FORCE_STICK)
						.unlockedBy("has_golden_power_source", this.has(ForceRegistry.GOLDEN_POWER_SOURCE))
						.unlockedBy("has_stick", this.has(ForceRegistry.FORCE_STICK))
						.save(this.output);
			}
		}

		shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_ROD, 1)
				.pattern("  I")
				.pattern(" S ")
				.pattern("N  ")
				.define('I', ForceRegistry.FORCE_INGOT)
				.define('S', ForceRegistry.FORCE_STICK)
				.define('N', ForceRegistry.FORCE_NUGGET)
				.unlockedBy("has_force_ingot", this.has(ForceRegistry.FORCE_INGOT))
				.save(output);

		ItemStackTemplate damagedRod = new ItemStackTemplate(ForceRegistry.FORCE_ROD, DataComponentPatch.builder()
				.set(DataComponents.DAMAGE, 73)
				.build());
		shaped(RecipeCategory.TOOLS, damagedRod)
				.pattern("  I")
				.pattern(" S ")
				.pattern("N  ")
				.define('I', ForceRegistry.FORCE_INGOT)
				.define('S', tag(Tags.Items.RODS_WOODEN))
				.define('N', ForceRegistry.FORCE_NUGGET)
				.unlockedBy("has_force_ingot", this.has(ForceRegistry.FORCE_INGOT))
				.save(output, ResourceKey.create(Registries.RECIPE, Identifier.parse("forcecraft:force_rod_from_sticks")));
		generateTransmutations();
		addInfuserRecipes();
		addMultiOutputRecipes();

		shaped(RecipeCategory.REDSTONE, ForceRegistry.FORCE_ENGINE.get())
				.pattern("FFF")
				.pattern(" # ")
				.pattern("GPG")
				.define('F', ForceTags.Items.INGOTS_FORCE)
				.define('#', Tags.Items.GLASS_BLOCKS)
				.define('G', ForceRegistry.FORCE_GEAR)
				.define('P', Items.PISTON)
				.unlockedBy("has_force_ingot", has(ForceTags.Items.INGOTS_FORCE))
				.unlockedBy("has_glass", has(Tags.Items.GLASS_BLOCKS))
				.unlockedBy("has_force_gear", has(ForceRegistry.FORCE_GEAR))
				.unlockedBy("has_piston", has(Items.PISTON))
				.save(output);

		shaped(RecipeCategory.MISC, ForceRegistry.FORCE_GEAR.get())
				.pattern(" F ")
				.pattern("F F")
				.pattern(" F ")
				.define('F', ForceTags.Items.INGOTS_FORCE)
				.unlockedBy("has_force_ingot", has(ForceTags.Items.INGOTS_FORCE))
				.save(output);

		shaped(RecipeCategory.REDSTONE, ForceRegistry.FORCE_FURNACE.get())
				.pattern(" F ")
				.pattern("F F")
				.pattern("IUI")
				.define('F', ForceTags.Items.INGOTS_FORCE)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('U', Items.FURNACE)
				.unlockedBy("has_force_ingot", has(ForceTags.Items.INGOTS_FORCE))
				.unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
				.unlockedBy("has_furnace", has(Items.FURNACE))
				.save(output);

		for (ForceFurnaceReg furnaceReg : ForceRegistry.getFurnaces()) {
			if (furnaceReg.getColor() != null) {
				TagKey<Item> tag = furnaceReg.getColor().getTag();
				shapeless(RecipeCategory.BUILDING_BLOCKS, furnaceReg.getFurnace())
						.requires(ForceTags.Items.FORCE_FURNACES)
						.requires(tag)
						.unlockedBy("has_force_furnace", has(ForceTags.Items.FORCE_FURNACES))
						.unlockedBy("has_dye", has(tag))
						.save(output);
			}
		}

		shapeless(RecipeCategory.FOOD, ForceRegistry.FORTUNE_COOKIE.get())
				.requires(Items.COOKIE)
				.requires(Items.PAPER)
				.unlockedBy("has_cookie", has(Items.COOKIE))
				.unlockedBy("has_paper", has(Items.PAPER))
				.save(output);

		shapeless(RecipeCategory.FOOD, ForceRegistry.SNOW_COOKIE.get())
				.requires(Items.SNOWBALL)
				.unlockedBy("has_snowball", has(Items.SNOWBALL))
				.save(output);

		shaped(RecipeCategory.FOOD, ForceRegistry.SOUL_WAFER.get())
				.pattern(" G ")
				.pattern("BSF")
				.pattern(" L ")
				.define('G', Tags.Items.GUNPOWDERS)
				.define('S', ForceRegistry.SNOW_COOKIE.get())
				.define('B', Tags.Items.BONES)
				.define('F', Items.ROTTEN_FLESH)
				.define('L', Tags.Items.DYES_LIGHT_BLUE)
				.unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDERS))
				.unlockedBy("has_snow_cookie", has(ForceRegistry.SNOW_COOKIE.get()))
				.unlockedBy("has_bone", has(Tags.Items.BONES))
				.unlockedBy("has_rotten_flesh", has(Items.ROTTEN_FLESH))
				.save(output);

		shaped(RecipeCategory.MISC, ForceRegistry.TREASURE_CORE.get())
				.pattern("FGF")
				.pattern("DND")
				.pattern("FGF")
				.define('F', ForceTags.Items.INGOTS_FORCE)
				.define('G', Tags.Items.STORAGE_BLOCKS_GOLD)
				.define('D', Tags.Items.GEMS_DIAMOND)
				.define('N', Tags.Items.NETHER_STARS)
				.unlockedBy("has_force_ingot", has(ForceTags.Items.INGOTS_FORCE))
				.unlockedBy("has_gold_block", has(Tags.Items.STORAGE_BLOCKS_GOLD))
				.unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))
				.unlockedBy("has_nether_star", has(Tags.Items.NETHER_STARS))
				.save(output);

		shaped(RecipeCategory.MISC, ForceRegistry.ROCS_FEATHER.get())
				.pattern(" D ")
				.pattern("GFG")
				.pattern(" D ")
				.define('D', ForceRegistry.FEATHER_DUST)
				.define('G', Tags.Items.DUSTS_GLOWSTONE)
				.define('F', Tags.Items.FEATHERS)
				.unlockedBy("has_feather_dust", has(ForceRegistry.FEATHER_DUST))
				.unlockedBy("has_glowstone_dust", has(Tags.Items.DUSTS_GLOWSTONE))
				.unlockedBy("has_feather", has(Tags.Items.FEATHERS))
				.save(output);


		shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_ARROW.get(), 6)
				.pattern("X")
				.pattern("#")
				.pattern("Y")
				.define('X', ForceTags.Items.NUGGETS_FORCE)
				.define('#', ForceTags.Items.RODS_FORCE)
				.define('Y', Tags.Items.FEATHERS)
				.unlockedBy("has_force_nugget", has(ForceTags.Items.NUGGETS_FORCE))
				.unlockedBy("has_force_rod", has(ForceTags.Items.RODS_FORCE))
				.unlockedBy("has_feather", has(Tags.Items.FEATHERS))
				.save(output);

		//Tools
		shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_AXE.get())
				.pattern("FF")
				.pattern("FS")
				.pattern(" S")
				.define('F', ForceTags.Items.INGOTS_FORCE)
				.define('S', ForceTags.Items.RODS_FORCE)
				.unlockedBy("has_force_ingot", has(ForceTags.Items.INGOTS_FORCE))
				.unlockedBy("has_force_rod", has(ForceTags.Items.RODS_FORCE))
				.save(output);

		shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_PICKAXE.get())
				.pattern("FFF")
				.pattern(" S ")
				.pattern(" S ")
				.define('F', ForceTags.Items.INGOTS_FORCE)
				.define('S', ForceTags.Items.RODS_FORCE)
				.unlockedBy("has_force_ingot", has(ForceTags.Items.INGOTS_FORCE))
				.unlockedBy("has_force_rod", has(ForceTags.Items.RODS_FORCE))
				.save(output);
		shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_SHOVEL.get())
				.pattern("F")
				.pattern("S")
				.pattern("S")
				.define('F', ForceTags.Items.INGOTS_FORCE)
				.define('S', ForceTags.Items.RODS_FORCE)
				.unlockedBy("has_force_ingot", has(ForceTags.Items.INGOTS_FORCE))
				.unlockedBy("has_force_rod", has(ForceTags.Items.RODS_FORCE))
				.save(output);
		shaped(RecipeCategory.COMBAT, ForceRegistry.FORCE_SWORD.get())
				.pattern("F")
				.pattern("F")
				.pattern("S")
				.define('F', ForceTags.Items.INGOTS_FORCE)
				.define('S', ForceRegistry.FORCE_ROD)
				.unlockedBy("has_force_ingot", has(ForceTags.Items.INGOTS_FORCE))
				.unlockedBy("has_force_rod", has(ForceRegistry.FORCE_ROD))
				.save(output);
		shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_SHEARS.get())
				.pattern("F ")
				.pattern(" F")
				.define('F', ForceTags.Items.INGOTS_FORCE)
				.unlockedBy("has_force_ingot", has(ForceTags.Items.INGOTS_FORCE))
				.save(output);
		shaped(RecipeCategory.COMBAT, ForceRegistry.FORCE_BOW.get())
				.pattern(" #S")
				.pattern("#NS")
				.pattern(" #S")
				.define('N', ForceTags.Items.NUGGETS_FORCE)
				.define('S', ForceTags.Items.RODS_FORCE)
				.define('#', Tags.Items.STRINGS)
				.unlockedBy("has_force_nugget", has(ForceTags.Items.NUGGETS_FORCE))
				.unlockedBy("has_force_rod", has(ForceTags.Items.RODS_FORCE))
				.save(output);
		shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_MITT.get())
				.pattern("CI ")
				.pattern("CFI")
				.pattern("CLL")
				.define('F', ForceTags.Items.INGOTS_FORCE)
				.define('L', Tags.Items.LEATHERS)
				.define('C', Tags.Items.COBBLESTONES_NORMAL)
				.define('I', Tags.Items.INGOTS_IRON)
				.unlockedBy("has_force_ingot", has(ForceTags.Items.INGOTS_FORCE))
				.unlockedBy("has_leather", has(Tags.Items.LEATHERS))
				.unlockedBy("has_cobblestone", has(Tags.Items.COBBLESTONES_NORMAL))
				.unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
				.save(output);

		//Armor
		shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_BOOTS.get())
				.pattern("F F")
				.pattern("F F")
				.define('F', ForceTags.Items.INGOTS_FORCE)
				.unlockedBy("has_force_ingot", has(ForceTags.Items.INGOTS_FORCE))
				.save(output);
		shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_LEGS.get())
				.pattern("FFF")
				.pattern("F F")
				.pattern("F F")
				.define('F', ForceTags.Items.INGOTS_FORCE)
				.unlockedBy("has_force_ingot", has(ForceTags.Items.INGOTS_FORCE))
				.save(output);
		shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_CHEST.get())
				.pattern("F F")
				.pattern("FFF")
				.pattern("FFF")
				.define('F', ForceTags.Items.INGOTS_FORCE)
				.unlockedBy("has_force_ingot", has(ForceTags.Items.INGOTS_FORCE))
				.save(output);
		shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_HELMET.get())
				.pattern("FFF")
				.pattern("F F")
				.define('F', ForceTags.Items.INGOTS_FORCE)
				.unlockedBy("has_force_ingot", has(ForceTags.Items.INGOTS_FORCE))
				.save(output);
	}

	private void generateWoodRecipes() {
		planksFromLogs(ForceRegistry.FORCE_PLANKS.getPlanks().get(), ForceTags.Items.FORCE_LOGS, 4);
		woodFromLogs(ForceRegistry.FORCE_WOOD.get(), ForceRegistry.FORCE_LOG.get());
		// TODO: Boat
		// TODO: Shelf
		buttonBuilder(ForceRegistry.FORCE_PLANKS.getButton(), Ingredient.of(ForceRegistry.FORCE_PLANKS.getPlanks().get()))
				.unlockedBy("has_force_planks", this.has(ForceRegistry.FORCE_PLANKS.getPlanks().get()))
				.save(this.output);
		doorBuilder(ForceRegistry.FORCE_PLANKS.getDoor(), Ingredient.of(ForceRegistry.FORCE_PLANKS.getPlanks().get()))
				.unlockedBy("has_force_planks", this.has(ForceRegistry.FORCE_PLANKS.getPlanks().get()))
				.save(this.output);
		fenceBuilder(ForceRegistry.FORCE_PLANKS.getFence(), Ingredient.of(ForceRegistry.FORCE_PLANKS.getPlanks().get()))
				.unlockedBy("has_force_planks", this.has(ForceRegistry.FORCE_PLANKS.getPlanks().get()))
				.save(this.output);
		fenceGateBuilder(ForceRegistry.FORCE_PLANKS.getFenceGate(), Ingredient.of(ForceRegistry.FORCE_PLANKS.getPlanks().get()))
				.unlockedBy("has_force_planks", this.has(ForceRegistry.FORCE_PLANKS.getPlanks().get()))
				.save(this.output);
		pressurePlate(ForceRegistry.FORCE_PLANKS.getPressurePlate(), ForceRegistry.FORCE_PLANKS.getPlanks().get());
		slab(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_PLANKS.getSlab(), ForceRegistry.FORCE_PLANKS.getPlanks().get());
		stairBuilder(ForceRegistry.FORCE_PLANKS.getStairs(), Ingredient.of(ForceRegistry.FORCE_PLANKS.getPlanks().get()))
				.unlockedBy("has_force_planks", this.has(ForceRegistry.FORCE_PLANKS.getPlanks().get()))
				.save(this.output);
		signBuilder(ForceRegistry.FORCE_PLANKS.getSign(), Ingredient.of(ForceRegistry.FORCE_PLANKS.getPlanks().get()))
				.unlockedBy("has_force_planks", this.has(ForceRegistry.FORCE_PLANKS.getPlanks().get()))
				.save(this.output);
		hangingSign(ForceRegistry.FORCE_PLANKS.getHangingSign(), ForceRegistry.FORCE_PLANKS.getPlanks().get());
		trapdoorBuilder(ForceRegistry.FORCE_PLANKS.getTrapdoor(), Ingredient.of(ForceRegistry.FORCE_PLANKS.getPlanks().get()))
				.unlockedBy("has_force_planks", this.has(ForceRegistry.FORCE_PLANKS.getPlanks().get()))
				.save(this.output);


		shaped(RecipeCategory.MISC, ForceRegistry.FORCE_STICK.get(), 4)
				.pattern(" W")
				.pattern("W ")
				.define('W', ForceRegistry.FORCE_PLANKS.getPlanks())
				.unlockedBy("has_force_planks", has(ForceRegistry.FORCE_PLANKS.getPlanks()))
				.save(output);
	}

	private void generateBrickRecipes() {
		for (ForceBrickReg brickReg : ForceRegistry.getBricks()) {
			if (brickReg.getColor() != null) {
				TagKey<Item> tag = brickReg.getColor().getTag();
				shaped(RecipeCategory.BUILDING_BLOCKS, brickReg.getBrick(), 8)
						.pattern("BBB")
						.pattern("BDB")
						.pattern("BBB")
						.define('B', ForceTags.Items.FORCE_BRICKS)
						.define('D', tag)
						.unlockedBy("has_force_brick", has(ForceTags.Items.FORCE_BRICKS))
						.unlockedBy("has_dye", has(tag))
						.save(output);
			}
			stairBuilder(brickReg.getStairs(), Ingredient.of(brickReg.getBrick()))
					.unlockedBy("has_force_bricks", this.has(brickReg.getBrick()))
					.save(this.output);
			wall(RecipeCategory.BUILDING_BLOCKS, brickReg.getWall().get(), brickReg.getBrick().get());
			slab(RecipeCategory.BUILDING_BLOCKS, brickReg.getSlab().get(), brickReg.getBrick().get());
			stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, brickReg.getStairs(), brickReg.getBrick(), 4);
			stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, brickReg.getSlab(), brickReg.getBrick(), 8);
			stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, brickReg.getSlab(), brickReg.getWall(), 1);
		}
	}

	private void generateTransmutations() {
		//Mushrooms
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.BROWN_MUSHROOM, Items.RED_MUSHROOM)
				.save(output, key("transmutation/red_to_brown_mushroom"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.RED_MUSHROOM, Items.BROWN_MUSHROOM)
				.save(output, key("transmutation/brown_to_red_mushroom"));

		//Raw food
		TransmutationRecipeBuilder.transmutation(RecipeCategory.FOOD, Items.BEEF, Items.CHICKEN)
				.unlockedBy("has_chicken", has(Items.CHICKEN))
				.save(output, key("transmutation/chicken_to_beef"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.FOOD, Items.CHICKEN, Items.MUTTON)
				.unlockedBy("has_mutton", has(Items.MUTTON))
				.save(output, key("transmutation/mutton_to_chicken"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.FOOD, Items.MUTTON, Items.PORKCHOP)
				.unlockedBy("has_porkchop", has(Items.PORKCHOP))
				.save(output, key("transmutation/porkchop_to_mutton"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.FOOD, Items.PORKCHOP, Items.BEEF)
				.unlockedBy("has_beef", has(Items.BEEF))
				.save(output, key("transmutation/beef_to_porkchop"));

		//Cooked food
		TransmutationRecipeBuilder.transmutation(RecipeCategory.FOOD, Items.COOKED_BEEF, Items.COOKED_CHICKEN)
				.unlockedBy("has_chicken", has(Items.COOKED_CHICKEN))
				.save(output, key("transmutation/cooked_chicken_to_beef"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.FOOD, Items.COOKED_CHICKEN, Items.COOKED_MUTTON)
				.unlockedBy("has_mutton", has(Items.COOKED_MUTTON))
				.save(output, key("transmutation/cooked_mutton_to_chicken"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.FOOD, Items.COOKED_MUTTON, Items.COOKED_PORKCHOP)
				.unlockedBy("has_porkchop", has(Items.COOKED_PORKCHOP))
				.save(output, key("transmutation/cooked_porkchop_to_mutton"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.FOOD, Items.COOKED_PORKCHOP, Items.COOKED_BEEF)
				.unlockedBy("has_beef", has(Items.COOKED_BEEF))
				.save(output, key("transmutation/cooked_beef_to_porkchop"));

		//Flowers
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.DANDELION, Items.POPPY)
				.unlockedBy("has_poppy", has(Items.POPPY))
				.save(output, key("transmutation/poppy_to_dandelion"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.POPPY, Items.DANDELION)
				.unlockedBy("has_dandelion", has(Items.DANDELION))
				.save(output, key("transmutation/dandelion_to_poppy"));

		//Diamond
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.DIAMOND, 2, Items.DIAMOND_AXE)
				.unlockedBy("has_tool", has(Items.DIAMOND_AXE))
				.save(output, key("transmutation/diamond_from_axe"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.DIAMOND, 2, Items.DIAMOND_HOE)
				.unlockedBy("has_tool", has(Items.DIAMOND_HOE))
				.save(output, key("transmutation/diamond_from_hoe"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.DIAMOND, 1, Items.DIAMOND_SHOVEL)
				.unlockedBy("has_tool", has(Items.DIAMOND_SHOVEL))
				.save(output, key("transmutation/diamond_from_shovel"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.DIAMOND, 2, Items.DIAMOND_SWORD)
				.unlockedBy("has_tool", has(Items.DIAMOND_SWORD))
				.save(output, key("transmutation/diamond_from_sword"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.DIAMOND, 2, Items.DIAMOND_PICKAXE)
				.unlockedBy("has_tool", has(Items.DIAMOND_PICKAXE))
				.save(output, key("transmutation/diamond_from_pickaxe"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.DIAMOND, 4, Items.DIAMOND_BOOTS)
				.unlockedBy("has_armor", has(Items.DIAMOND_BOOTS))
				.save(output, key("transmutation/diamond_from_boots"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.DIAMOND, 7, Items.DIAMOND_LEGGINGS)
				.unlockedBy("has_armor", has(Items.DIAMOND_LEGGINGS))
				.save(output, key("transmutation/diamond_from_leggings"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.DIAMOND, 8, Items.DIAMOND_CHESTPLATE)
				.unlockedBy("has_armor", has(Items.DIAMOND_CHESTPLATE))
				.save(output, key("transmutation/diamond_from_chestplate"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.DIAMOND, 5, Items.DIAMOND_HELMET)
				.unlockedBy("has_armor", has(Items.DIAMOND_HELMET))
				.save(output, key("transmutation/diamond_from_helmet"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.DIAMOND, 4, Items.DIAMOND_HORSE_ARMOR)
				.unlockedBy("has_horse_armor", has(Items.DIAMOND_HORSE_ARMOR))
				.save(output, key("transmutation/diamond_from_horse_armor"));

		//Gold
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.GOLD_INGOT, 2, Items.GOLDEN_AXE)
				.unlockedBy("has_tool", has(Items.GOLDEN_AXE))
				.save(output, key("transmutation/gold_ingot_from_axe"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.GOLD_INGOT, 2, Items.GOLDEN_HOE)
				.unlockedBy("has_tool", has(Items.GOLDEN_HOE))
				.save(output, key("transmutation/gold_ingot_from_hoe"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.GOLD_INGOT, 1, Items.GOLDEN_SHOVEL)
				.unlockedBy("has_tool", has(Items.GOLDEN_SHOVEL))
				.save(output, key("transmutation/gold_ingot_from_shovel"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.GOLD_INGOT, 2, Items.GOLDEN_SWORD)
				.unlockedBy("has_tool", has(Items.GOLDEN_SWORD))
				.save(output, key("transmutation/gold_ingot_from_sword"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.GOLD_INGOT, 2, Items.GOLDEN_PICKAXE)
				.unlockedBy("has_tool", has(Items.GOLDEN_PICKAXE))
				.save(output, key("transmutation/gold_ingot_from_pickaxe"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.GOLD_INGOT, 4, Items.GOLDEN_BOOTS)
				.unlockedBy("has_armor", has(Items.GOLDEN_BOOTS))
				.save(output, key("transmutation/gold_ingot_from_boots"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.GOLD_INGOT, 7, Items.GOLDEN_LEGGINGS)
				.unlockedBy("has_armor", has(Items.GOLDEN_LEGGINGS))
				.save(output, key("transmutation/gold_ingot_from_leggings"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.GOLD_INGOT, 8, Items.GOLDEN_CHESTPLATE)
				.unlockedBy("has_armor", has(Items.GOLDEN_CHESTPLATE))
				.save(output, key("transmutation/gold_ingot_from_chestplate"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.GOLD_INGOT, 5, Items.GOLDEN_HELMET)
				.unlockedBy("has_armor", has(Items.GOLDEN_HELMET))
				.save(output, key("transmutation/gold_ingot_from_helmet"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.GOLD_INGOT, 4, Items.GOLDEN_HORSE_ARMOR)
				.unlockedBy("has_horse_armor", has(Items.GOLDEN_HORSE_ARMOR))
				.save(output, key("transmutation/gold_ingot_from_horse_armor"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.GOLD_INGOT, 4, Items.CLOCK)
				.unlockedBy("has_clock", has(Items.CLOCK))
				.save(output, key("transmutation/gold_ingot_from_clock"));

		//Iron
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.IRON_INGOT, 2, Items.IRON_AXE)
				.unlockedBy("has_tool", has(Items.IRON_AXE))
				.save(output, key("transmutation/iron_ingot_from_axe"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.IRON_INGOT, 2, Items.IRON_HOE)
				.unlockedBy("has_tool", has(Items.IRON_HOE))
				.save(output, key("transmutation/iron_ingot_from_hoe"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.IRON_INGOT, 1, Items.IRON_SHOVEL)
				.unlockedBy("has_tool", has(Items.IRON_SHOVEL))
				.save(output, key("transmutation/iron_ingot_from_shovel"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.IRON_INGOT, 2, Items.IRON_SWORD)
				.unlockedBy("has_tool", has(Items.IRON_SWORD))
				.save(output, key("transmutation/iron_ingot_from_sword"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.IRON_INGOT, 2, Items.IRON_PICKAXE)
				.unlockedBy("has_tool", has(Items.IRON_PICKAXE))
				.save(output, key("transmutation/iron_ingot_from_pickaxe"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.IRON_INGOT, 4, Items.IRON_BOOTS)
				.unlockedBy("has_armor", has(Items.IRON_BOOTS))
				.save(output, key("transmutation/iron_ingot_from_boots"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.IRON_INGOT, 7, Items.IRON_LEGGINGS)
				.unlockedBy("has_armor", has(Items.IRON_LEGGINGS))
				.save(output, key("transmutation/iron_ingot_from_leggings"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.IRON_INGOT, 8, Items.IRON_CHESTPLATE)
				.unlockedBy("has_armor", has(Items.IRON_CHESTPLATE))
				.save(output, key("transmutation/iron_ingot_from_chestplate"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.IRON_INGOT, 5, Items.IRON_HELMET)
				.unlockedBy("has_armor", has(Items.IRON_HELMET))
				.save(output, key("transmutation/iron_ingot_from_helmet"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.IRON_INGOT, 2, Items.CHAINMAIL_BOOTS)
				.unlockedBy("has_armor", has(Items.CHAINMAIL_BOOTS))
				.save(output, key("transmutation/iron_ingot_from_chainmail_boots"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.IRON_INGOT, 4, Items.CHAINMAIL_LEGGINGS)
				.unlockedBy("has_armor", has(Items.CHAINMAIL_LEGGINGS))
				.save(output, key("transmutation/iron_ingot_from_chainmail_leggings"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.IRON_INGOT, 6, Items.CHAINMAIL_CHESTPLATE)
				.unlockedBy("has_armor", has(Items.CHAINMAIL_CHESTPLATE))
				.save(output, key("transmutation/iron_ingot_from_chainmail_chestplate"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.IRON_INGOT, 3, Items.CHAINMAIL_HELMET)
				.unlockedBy("has_armor", has(Items.CHAINMAIL_HELMET))
				.save(output, key("transmutation/iron_ingot_from_chainmail_helmet"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.IRON_INGOT, 4, Items.IRON_HORSE_ARMOR)
				.unlockedBy("has_horse_armor", has(Items.IRON_HORSE_ARMOR))
				.save(output, key("transmutation/iron_ingot_from_horse_armor"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.IRON_INGOT, 4, Items.COMPASS)
				.unlockedBy("has_compass", has(Items.COMPASS))
				.save(output, key("transmutation/irom_ingot_from_compass"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.IRON_INGOT, 7, Items.CAULDRON)
				.unlockedBy("has_cauldron", has(Items.CAULDRON))
				.save(output, key("transmutation/irom_ingot_from_cauldron"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.IRON_INGOT, 3, Items.BUCKET)
				.unlockedBy("has_bucket", has(Items.BUCKET))
				.save(output, key("transmutation/iron_ingot_from_bucket"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.IRON_INGOT, 2, Items.IRON_DOOR)
				.unlockedBy("has_door", has(Items.IRON_DOOR))
				.save(output, key("transmutation/iron_ingot_from_door"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.IRON_INGOT, 5, Items.MINECART)
				.unlockedBy("has_minecart", has(Items.MINECART))
				.save(output, key("transmutation/iron_ingot_from_minecart"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.IRON_INGOT, 31, Items.ANVIL)
				.unlockedBy("has_anvil", has(Items.ANVIL))
				.save(output, key("transmutation/iron_ingot_from_anvil"));

		//Force
		TransmutationRecipeBuilder.transmutation(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK.getBrick(), Items.STONE_BRICKS)
				.unlockedBy("has_stone_bricks", has(Items.STONE_BRICKS))
				.save(output, key("transmutation/force_brick_from_stone_bricks"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_SAPLING.get(), tag(ItemTags.SAPLINGS))
				.unlockedBy("has_sapling", has(ItemTags.SAPLINGS))
				.save(output, key("transmutation/force_sapling_from_sapling"));

		//Misc
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.EXPERIENCE_BOTTLE, Items.ENCHANTED_BOOK)
				.unlockedBy("has_enchanted_book", has(Items.ENCHANTED_BOOK))
				.save(output, key("transmutation/experience_bottle_from_enchanted_book"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.FOOD, ForceRegistry.FORTUNE.get(), ForceRegistry.FORTUNE_COOKIE.get())
				.unlockedBy("has_fortune_cookie", has(ForceRegistry.FORTUNE_COOKIE.get()))
				.save(output, key("transmutation/fortune_from_fortune_cookie"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, ForceRegistry.FORCE_INFUSER.get(), Items.ENCHANTING_TABLE)
				.unlockedBy("has_enchanting_table", has(Items.ENCHANTING_TABLE))
				.save(output, key("transmutation/infuser_from_enchanting_table"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.STRING, Items.BOW)
				.unlockedBy("has_bow", has(Items.BOW))
				.save(output, key("transmutation/string_from_bow"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.TRIPWIRE_HOOK, Items.CROSSBOW)
				.unlockedBy("has_bow", has(Items.CROSSBOW))
				.save(output, key("transmutation/tripwire_hook_from_crossbow"));

		//Stick
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.STICK, Items.STONE_AXE)
				.unlockedBy("has_tool", has(Items.STONE_AXE))
				.save(output, key("transmutation/stick_from_stone_axe"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.STICK, Items.STONE_HOE)
				.unlockedBy("has_tool", has(Items.STONE_HOE))
				.save(output, key("transmutation/stick_from_stone_hoe"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.STICK, Items.STONE_PICKAXE)
				.unlockedBy("has_tool", has(Items.STONE_PICKAXE))
				.save(output, key("transmutation/stick_from_stone_pickaxe"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.STICK, Items.STONE_SHOVEL)
				.unlockedBy("has_tool", has(Items.STONE_SHOVEL))
				.save(output, key("transmutation/stick_from_stone_shovel"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.STICK, Items.STONE_SWORD)
				.unlockedBy("has_tool", has(Items.STONE_SWORD))
				.save(output, key("transmutation/stick_from_stone_sword"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.STICK, Items.WOODEN_AXE)
				.unlockedBy("has_tool", has(Items.WOODEN_AXE))
				.save(output, key("transmutation/stick_from_wooden_axe"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.STICK, Items.WOODEN_HOE)
				.unlockedBy("has_tool", has(Items.WOODEN_HOE))
				.save(output, key("transmutation/stick_from_wooden_hoe"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.STICK, Items.WOODEN_PICKAXE)
				.unlockedBy("has_tool", has(Items.WOODEN_PICKAXE))
				.save(output, key("transmutation/stick_from_wooden_pickaxe"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.STICK, Items.WOODEN_SHOVEL)
				.unlockedBy("has_tool", has(Items.WOODEN_SHOVEL))
				.save(output, key("transmutation/stick_from_wooden_shovel"));
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, Items.STICK, Items.WOODEN_SWORD)
				.unlockedBy("has_tool", has(Items.WOODEN_SWORD))
				.save(output, key("transmutation/stick_from_wooden_sword"));

		//Cores
		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, ForceRegistry.UPGRADE_TOME, Items.BOOK)
				.unlockedBy("has_book", has(Items.BOOK))
				.save(output, key("transmutation/upgrade_tome"));

		TransmutationRecipeBuilder.transmutation(RecipeCategory.MISC, ForceRegistry.UPGRADE_CORE, ForceRegistry.EXPERIENCE_TOME)
				.unlockedBy("has_experience_tome", has(ForceRegistry.EXPERIENCE_TOME.get()))
				.save(output, key("transmutation/upgrade_core"));

	}

	private void addInfuserRecipes() {
		// Tier 1:
		InfuseRecipeBuilder.infuse(Ingredient.of(ForceRegistry.FORCE_NUGGET), tag(ForceTags.Items.VALID_FORCE_ITEMS),
						LeveledUpgrade.of(ForceComponents.FORCE.get(), 2), UpgradeBookTier.ONE, 25, 200)
				.save(output, Reference.modLoc("infuse/force"));

		InfuseRecipeBuilder.infuse(Ingredient.of(ForceRegistry.CLAW), tag(ForceTags.Items.VALID_DAMAGE_ITEMS),
						LeveledUpgrade.of(ForceComponents.DAMAGE.get(), 5), UpgradeBookTier.ONE, 30, 200)
				.save(output, Reference.modLoc("infuse/damage"));

		InfuseRecipeBuilder.infuse(Ingredient.of(Items.SUGAR), tag(ForceTags.Items.VALID_SPEED_ITEMS),
						LeveledUpgrade.of(ForceComponents.SPEED.get(), 5), UpgradeBookTier.ONE, 30, 200)
				.save(output, Reference.modLoc("infuse/speed"));

		InfuseRecipeBuilder.infuse(tag(ForceTags.Items.FORCE_LOGS), tag(ForceTags.Items.VALID_LUMBERJACK_ITEMS),
						FlagUpgrade.of(ForceComponents.LUMBERJACK.get()), UpgradeBookTier.ONE, 35, 200)
				.save(output, Reference.modLoc("infuse/lumberjack"));

		// Tier 2:
		InfuseRecipeBuilder.infuse(Ingredient.of(Items.LAPIS_LAZULI), tag(ForceTags.Items.VALID_RAINBOW_ITEMS),
						FlagUpgrade.of(ForceComponents.RAINBOW.get()), UpgradeBookTier.TWO, 10, 200)
				.save(output, Reference.modLoc("infuse/rainbow"));

		InfuseRecipeBuilder.infuse(Ingredient.of(ForceRegistry.FORTUNE), tag(ForceTags.Items.VALID_LUCK_ITEMS),
						LeveledUpgrade.of(ForceComponents.LUCK.get(), 4), UpgradeBookTier.TWO, 15, 200)
				.save(output, Reference.modLoc("infuse/luck"));

		InfuseRecipeBuilder.infuse(Ingredient.of(ForceRegistry.FORCE_PACK_UPGRADE), Ingredient.of(ForceRegistry.FORCE_PACK),
						new PackUpgrade(), UpgradeBookTier.TWO, 15, 200)
				.save(output, Reference.modLoc("infuse/pack_upgrade"));

		InfuseRecipeBuilder.convert(Ingredient.of(Items.FLINT), Ingredient.of(ForceRegistry.UPGRADE_CORE),
						new ItemStackTemplate(ForceRegistry.GRINDING_CORE), UpgradeBookTier.TWO, 15, 200)
				.save(output, Reference.modLoc("infuse/grinding_core"));

		InfuseRecipeBuilder.infuse(Ingredient.of(ForceRegistry.SNOW_COOKIE), tag(ForceTags.Items.VALID_FREEZING_ITEMS),
						FlagUpgrade.of(ForceComponents.FREEZING.get()), UpgradeBookTier.TWO, 15, 200).group("freezing")
				.save(output, Reference.modLoc("infuse/freezing"));

		InfuseRecipeBuilder.convert(Ingredient.of(ForceRegistry.SNOW_COOKIE), Ingredient.of(ForceRegistry.UPGRADE_CORE),
						new ItemStackTemplate(ForceRegistry.FROZEN_CORE), UpgradeBookTier.TWO, 15, 200).group("freezing")
				.save(output, Reference.modLoc("infuse/freezing_core"));

		InfuseRecipeBuilder.convert(Ingredient.of(Items.SUGAR), Ingredient.of(ForceRegistry.UPGRADE_CORE.get()),
						new ItemStackTemplate(ForceRegistry.SPEED_CORE), UpgradeBookTier.TWO, 15, 20)
				.save(output, Reference.modLoc("infuser/speed_core"));

		InfuseRecipeBuilder.convert(Ingredient.of(Items.EXPERIENCE_BOTTLE), Ingredient.of(ForceRegistry.UPGRADE_CORE.get()),
						new ItemStackTemplate(ForceRegistry.EXPERIENCE_CORE), UpgradeBookTier.TWO, 15, 20).group("experience")
				.save(output, Reference.modLoc("infuser/experience_core"));


		InfuseRecipeBuilder.convert(Ingredient.of(Items.EXPERIENCE_BOTTLE), tag(ForceTags.Items.VALID_EXPERIENCE_ITEMS),
						new ItemStackTemplate(ForceRegistry.EXPERIENCE_TOME), UpgradeBookTier.TWO, 15, 200).group("experience")
				.save(output, Reference.modLoc("infuse/experience"));

		// Tier 3:
		InfuseRecipeBuilder.infuse(Ingredient.of(Items.GLASS_BOTTLE), tag(ForceTags.Items.VALID_FALSE_ITEMS),
						FlagUpgrade.of(ForceComponents.FALSE.get()), UpgradeBookTier.THREE, 20, 200)
				.save(output, Reference.modLoc("infuse/false"));

		InfuseRecipeBuilder.infuse(Ingredient.of(Items.COBWEB), tag(ForceTags.Items.VALID_TOUCH_ITEMS),
						FlagUpgrade.of(ForceComponents.TOUCH.get()), UpgradeBookTier.THREE, 20, 200)
				.save(output, Reference.modLoc("infuse/touch"));

		InfuseRecipeBuilder.infuse(Ingredient.of(Items.ARROW), tag(ForceTags.Items.VALID_BLEED_ITEMS),
						LeveledUpgrade.of(ForceComponents.BLEED.get(), 3), UpgradeBookTier.THREE, 20, 200)
				.save(output, Reference.modLoc("infuse/bleed"));

		InfuseRecipeBuilder.infuse(Ingredient.of(ForceRegistry.FORCE_PACK_UPGRADE), Ingredient.of(ForceRegistry.FORCE_PACK),
						new PackUpgrade(), UpgradeBookTier.THREE, 20, 200)
				.save(output, Reference.modLoc("infuse/pack_upgrade2"));

//		InfuseRecipeBuilder.infuse(Ingredient.of(Items.CRAFTING_TABLE), tag(ForceTags.Items.VALID_CRAFT_ITEMS),
//						FlagUpgrade.of(ForceComponents.CRAFT.get()), UpgradeBookTier.THREE, 20, 200)
//				.save(output, Reference.modLoc("infuse/craft"));

		InfuseRecipeBuilder.infuse(Ingredient.of(Items.CLOCK), tag(ForceTags.Items.VALID_TIME_ITEMS),
						FlagUpgrade.of(ForceComponents.TIME.get()), UpgradeBookTier.THREE, 20, 200).group("time")
				.save(output, Reference.modLoc("infuse/time"));

		InfuseRecipeBuilder.convert(Ingredient.of(Items.CLOCK), tag(ForceTags.Items.VALID_INFUSE_TORCHES),
						new ItemStackTemplate(ForceRegistry.TIME_TORCH.getTorchItem()), UpgradeBookTier.THREE, 20, 200).group("time")
				.save(output.withConditions(new TorchEnabledCondition()), Reference.modLoc("infuse/time_torch"));

		// Tier 4:
		InfuseRecipeBuilder.convert(Ingredient.of(ForceRegistry.GOLDEN_POWER_SOURCE.get()), Ingredient.of(ForceRegistry.UPGRADE_CORE.get()),
						new ItemStackTemplate(ForceRegistry.HEAT_CORE), UpgradeBookTier.FOUR, 30, 200).group("heat")
				.save(output, Reference.modLoc("infuser/convert_heat"));

		InfuseRecipeBuilder.infuse(Ingredient.of(ForceRegistry.FORCE_PACK_UPGRADE), Ingredient.of(ForceRegistry.FORCE_PACK),
						new PackUpgrade(), UpgradeBookTier.FOUR, 30, 200)
				.save(output, Reference.modLoc("infuse/pack_upgrade3"));

		InfuseRecipeBuilder.infuse(Ingredient.of(ForceRegistry.GOLDEN_POWER_SOURCE), tag(ForceTags.Items.VALID_HEAT_ITEMS),
						FlagUpgrade.of(ForceComponents.HEAT.get()), UpgradeBookTier.FOUR, 30, 200).group("heat")
				.save(output, Reference.modLoc("infuse/heat"));

		InfuseRecipeBuilder.convert(Ingredient.of(ForceRegistry.GOLDEN_POWER_SOURCE), tag(ForceTags.Items.VALID_INFUSE_TORCHES),
				new ItemStackTemplate(ForceRegistry.HEAT_TORCH.getTorchItem()), UpgradeBookTier.FOUR, 30, 200).group("heat")
				.save(output, Reference.modLoc("infuse/heat_torch"));

		InfuseRecipeBuilder.infuse(Ingredient.of(Items.SPIDER_EYE), tag(ForceTags.Items.VALID_BANE_ITEMS),
						FlagUpgrade.of(ForceComponents.BANE.get()), UpgradeBookTier.FOUR, 30, 200).group("bane")
				.save(output, Reference.modLoc("infuse/bane"));

		InfuseRecipeBuilder.convert(Ingredient.of(Items.SPIDER_EYE), tag(ForceTags.Items.VALID_INFUSE_TORCHES),
						new ItemStackTemplate(ForceRegistry.BANE_TORCH.getTorchItem()), UpgradeBookTier.FOUR, 30, 200).group("bane")
				.save(output, Reference.modLoc("infuse/bane_torch"));

		// Storage Unit upgrade maybe in future?????

		InfuseRecipeBuilder.infuse(Ingredient.of(Items.FURNACE), tag(ForceTags.Items.VALID_FORGE_ITEMS),
						FlagUpgrade.of(ForceComponents.FORGE.get()), UpgradeBookTier.FOUR, 30, 200)
				.save(output, Reference.modLoc("infuse/forge"));

		// Tier 5:
		InfuseRecipeBuilder.infuse(Ingredient.of(ForceRegistry.ROCS_FEATHER), tag(ForceTags.Items.VALID_WING_ITEMS),
						FlagUpgrade.of(ForceComponents.WING.get()), UpgradeBookTier.FIVE, 35, 200)
				.save(output, Reference.modLoc("infuse/wing"));

		InfuseRecipeBuilder.infuse(Ingredient.of(Items.GHAST_TEAR), tag(ForceTags.Items.VALID_HEALING_ITEMS),
						LeveledUpgrade.of(ForceComponents.HEALING.get(), 2), UpgradeBookTier.FIVE, 35, 200).group("healing")
				.save(output, Reference.modLoc("infuse/healing"));

		InfuseRecipeBuilder.convert(Ingredient.of(Items.GHAST_TEAR), tag(ForceTags.Items.VALID_INFUSE_TORCHES),
						new ItemStackTemplate(ForceRegistry.HEALING_TORCH.getTorchItem()), UpgradeBookTier.FIVE, 35, 200).group("healing")
				.save(output, Reference.modLoc("infuser/healing_torch"));

		InfuseRecipeBuilder.infuse(Ingredient.of(ForceRegistry.FORCE_PACK_UPGRADE), Ingredient.of(ForceRegistry.FORCE_PACK),
						new PackUpgrade(), UpgradeBookTier.FIVE, 30, 200)
				.save(output, Reference.modLoc("infuse/pack_upgrade4"));

		ItemStackTemplate invisibilityPotion = new ItemStackTemplate(Items.POTION, DataComponentPatch.builder()
				.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.INVISIBILITY))
				.build());
		InfuseRecipeBuilder.infuse(DataComponentIngredient.of(false, invisibilityPotion), tag(ForceTags.Items.VALID_CAMO_ITEMS),
						FlagUpgrade.of(ForceComponents.CAMO.get()), UpgradeBookTier.FIVE, 35, 200).group("camo")
				.save(output, Reference.modLoc("infuse/camo"));

		InfuseRecipeBuilder.convert(DataComponentIngredient.of(false, invisibilityPotion), tag(ForceTags.Items.VALID_INFUSE_TORCHES),
						new ItemStackTemplate(ForceRegistry.CAMO_TORCH.getTorchItem()), UpgradeBookTier.FIVE, 35, 200).group("camo")
				.save(output, Reference.modLoc("infuser/camo_torch"));

		ItemStackTemplate nightVisionPotion = new ItemStackTemplate(Items.POTION, DataComponentPatch.builder()
				.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.NIGHT_VISION))
				.build());
		InfuseRecipeBuilder.infuse(DataComponentIngredient.of(false, nightVisionPotion), tag(ForceTags.Items.VALID_SIGHT_ITEMS),
						FlagUpgrade.of(ForceComponents.SIGHT.get()), UpgradeBookTier.FIVE, 35, 200)
				.save(output, Reference.modLoc("infuse/sight"));

		// Tier 6:
		InfuseRecipeBuilder.infuse(Ingredient.of(Items.OBSIDIAN), tag(ForceTags.Items.VALID_STURDY_ITEMS),
						LeveledUpgrade.of(ForceComponents.STURDY.get(), 2, ForceTags.Items.LIMITED_STURDY_ITEMS), UpgradeBookTier.SIX, 45, 200)
				.save(output, Reference.modLoc("infuse/sturdy"));

		InfuseRecipeBuilder.infuse(Ingredient.of(Items.ENDER_PEARL), tag(ForceTags.Items.VALID_ENDER_ITEMS),
						FlagUpgrade.of(ForceComponents.ENDER.get()), UpgradeBookTier.SIX, 45, 200)
				.save(output, Reference.modLoc("infuse/ender"));

		InfuseRecipeBuilder.infuse(Ingredient.of(ForceRegistry.SOUL_WAFER), tag(ForceTags.Items.VALID_SOUL_ITEMS),
						FlagUpgrade.of(ForceComponents.SOUL.get()), UpgradeBookTier.SIX, 45, 200)
				.save(output, Reference.modLoc("infuse/soul"));

		// Tier 7:
		InfuseRecipeBuilder.infuse(Ingredient.of(Items.GLOWSTONE_DUST), tag(ForceTags.Items.VALID_LIGHT_ITEMS),
						LeveledUpgrade.of(ForceComponents.LIGHT.get(), 5), UpgradeBookTier.SEVEN, 55, 200)
				.save(output, Reference.modLoc("infuse/light"));

		InfuseRecipeBuilder.infuse(Ingredient.of(ForceRegistry.TREASURE_CORE), tag(ForceTags.Items.VALID_TREASURE_ITEMS),
						FlagUpgrade.of(ForceComponents.TREASURE.get()), UpgradeBookTier.SEVEN, 55, 200)
				.save(output, Reference.modLoc("infuse/treasure"));

	}

	private void addMultiOutputRecipes() {
		MultiOutputRecipeBuilder.freezing(tag(Tags.Items.RODS_BLAZE), 0.1F, 200)
				.result(Items.BONE, 1)
				.unlockedBy("has_core", has(ForceRegistry.FROZEN_CORE))
				.save(output, key("freezing/bone_from_blaze_rod"));
		MultiOutputRecipeBuilder.freezing(Items.BLAZE_POWDER, 0.1F, 200)
				.result(Items.BONE_MEAL, 1)
				.unlockedBy("has_core", has(ForceRegistry.FROZEN_CORE))
				.save(output, key("freezing/bone_meal_from_blaze_powder"));
		MultiOutputRecipeBuilder.freezing(tag(Tags.Items.BRICKS_NORMAL), 0.1F, 200)
				.result(Items.BRICK, 1)
				.unlockedBy("has_core", has(ForceRegistry.FROZEN_CORE))
				.save(output, key("freezing/brick_from_nether_brick"));
		MultiOutputRecipeBuilder.freezing(Items.ROTTEN_FLESH, 0.1F, 200)
				.result(Items.LEATHER, 1)
				.unlockedBy("has_core", has(ForceRegistry.FROZEN_CORE))
				.save(output, key("freezing/leather_from_rotten_flesh"));

		ItemStackTemplate waterBottle = new ItemStackTemplate(Items.POTION, DataComponentPatch.builder()
				.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER))
				.build());
		MultiOutputRecipeBuilder.freezing(DataComponentIngredient.of(false, waterBottle), 0.1F, 200)
				.result(Items.ICE, 1)
				.secondaryResult(Items.GLASS_BOTTLE, 1)
				.unlockedBy("has_core", has(ForceRegistry.FROZEN_CORE))
				.save(output, key("freezing/ice_from_water_bottle"));

		MultiOutputRecipeBuilder.freezing(Items.WATER_BUCKET, 0.1F, 200)
				.result(Items.PACKED_ICE, 1)
				.secondaryResult(Items.BUCKET, 1)
				.unlockedBy("has_core", has(ForceRegistry.FROZEN_CORE))
				.save(output, key("freezing/ice_from_water_bucket"));
		MultiOutputRecipeBuilder.freezing(tag(Tags.Items.NETHERRACKS), 0.1F, 200)
				.result(Items.COBBLESTONE, 1)
				.unlockedBy("has_core", has(ForceRegistry.FROZEN_CORE))
				.save(output, key("freezing/cobblestone_from_netherrack"));
		MultiOutputRecipeBuilder.freezing(Items.LAVA_BUCKET, 0.1F, 200)
				.result(Items.OBSIDIAN, 1)
				.secondaryResult(Items.BUCKET, 1)
				.unlockedBy("has_core", has(ForceRegistry.FROZEN_CORE))
				.save(output, key("freezing/obsidian_from_lava_bucket"));
		MultiOutputRecipeBuilder.freezing(tag(Tags.Items.SANDS_COLORLESS), 0.1F, 200)
				.result(Items.SANDSTONE, 1)
				.unlockedBy("has_core", has(ForceRegistry.FROZEN_CORE))
				.save(output, key("freezing/sandstone_from_sand"));
		MultiOutputRecipeBuilder.freezing(tag(Tags.Items.SANDS_RED), 0.1F, 200)
				.result(Items.RED_SANDSTONE, 1)
				.unlockedBy("has_core", has(ForceRegistry.FROZEN_CORE))
				.save(output, key("freezing/sandstone_from_red_sand"));
		MultiOutputRecipeBuilder.freezing(tag(Tags.Items.SLIME_BALLS), 0.1F, 200)
				.result(Items.SNOWBALL, 1)
				.unlockedBy("has_core", has(ForceRegistry.FROZEN_CORE))
				.save(output, key("freezing/snowball_from_slimeball"));
		MultiOutputRecipeBuilder.freezing(tag(Tags.Items.COBBLESTONES_NORMAL), 0.1F, 200)
				.result(Items.STONE, 1)
				.unlockedBy("has_core", has(ForceRegistry.FROZEN_CORE))
				.save(output, key("freezing/stone_from_cobblestone"));
		MultiOutputRecipeBuilder.freezing(tag(Tags.Items.STONES), 0.1F, 200)
				.result(Items.STONE_BRICKS, 1)
				.unlockedBy("has_core", has(ForceRegistry.FROZEN_CORE))
				.save(output, key("freezing/stone_bricks_from_stone"));

		//Oak
		MultiOutputRecipeBuilder.grinding(tag(Tags.Items.BOOKSHELVES), 1.0F, 0.1F, 400)
				.result(Items.OAK_PLANKS, 6)
				.secondaryResult(Items.PAPER, 9)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/oak_planks_from_bookshelf"));
		MultiOutputRecipeBuilder.grinding(tag(Tags.Items.CHESTS_WOODEN), 1.0F, 0.1F, 400)
				.result(Items.OAK_PLANKS, 8)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/oak_planks_from_chest"));
		MultiOutputRecipeBuilder.grinding(Items.OAK_DOOR, 1.0F, 0.1F, 400)
				.result(Items.OAK_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/oak_planks_from_door"));
		MultiOutputRecipeBuilder.grinding(tag(ItemTags.OAK_LOGS), 1.0F, 0.1F, 400)
				.result(Items.OAK_PLANKS, 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/oak_planks_from_log"));
		MultiOutputRecipeBuilder.grinding(Items.OAK_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.result(Items.OAK_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/oak_planks_from_pressure_plate"));
		MultiOutputRecipeBuilder.grinding(Items.CRAFTING_TABLE, 1.0F, 0.1F, 400)
				.result(Items.OAK_PLANKS, 4)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/oak_planks_from_crafting_table"));
		//Acacia
		MultiOutputRecipeBuilder.grinding(Items.ACACIA_DOOR, 1.0F, 0.1F, 400)
				.result(Items.ACACIA_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/acacia_planks_from_door"));
		MultiOutputRecipeBuilder.grinding(tag(ItemTags.ACACIA_LOGS), 1.0F, 0.1F, 400)
				.result(Items.ACACIA_PLANKS, 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/acacia_planks_from_log"));
		MultiOutputRecipeBuilder.grinding(Items.ACACIA_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.result(Items.ACACIA_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/acacia_planks_from_pressure_plate"));
		//Birch
		MultiOutputRecipeBuilder.grinding(Items.BIRCH_DOOR, 1.0F, 0.1F, 400)
				.result(Items.BIRCH_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/birch_planks_from_door"));
		MultiOutputRecipeBuilder.grinding(tag(ItemTags.BIRCH_LOGS), 1.0F, 0.1F, 400)
				.result(Items.BIRCH_PLANKS, 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/birch_planks_from_log"));
		MultiOutputRecipeBuilder.grinding(Items.BIRCH_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.result(Items.BIRCH_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/birch_planks_from_pressure_plate"));
		//Spruce
		MultiOutputRecipeBuilder.grinding(Items.SPRUCE_DOOR, 1.0F, 0.1F, 400)
				.result(Items.SPRUCE_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/spruce_planks_from_door"));
		MultiOutputRecipeBuilder.grinding(tag(ItemTags.SPRUCE_LOGS), 1.0F, 0.1F, 400)
				.result(Items.SPRUCE_PLANKS, 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/spruce_planks_from_log"));
		MultiOutputRecipeBuilder.grinding(Items.SPRUCE_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.result(Items.SPRUCE_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/spruce_planks_from_pressure_plate"));
		//Jungle
		MultiOutputRecipeBuilder.grinding(Items.JUNGLE_DOOR, 1.0F, 0.1F, 400)
				.result(Items.JUNGLE_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/jungle_planks_from_door"));
		MultiOutputRecipeBuilder.grinding(tag(ItemTags.JUNGLE_LOGS), 1.0F, 0.1F, 400)
				.result(Items.JUNGLE_PLANKS, 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/jungle_planks_from_log"));
		MultiOutputRecipeBuilder.grinding(Items.JUNGLE_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.result(Items.JUNGLE_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/jungle_planks_from_pressure_plate"));
		//Cherry
		MultiOutputRecipeBuilder.grinding(Items.CHERRY_DOOR, 1.0F, 0.1F, 400)
				.result(Items.CHERRY_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/cherry_planks_from_door"));
		MultiOutputRecipeBuilder.grinding(tag(ItemTags.CHERRY_LOGS), 1.0F, 0.1F, 400)
				.result(Items.CHERRY_PLANKS, 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/cherry_planks_from_log"));
		MultiOutputRecipeBuilder.grinding(Items.CHERRY_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.result(Items.CHERRY_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/cherry_planks_from_pressure_plate"));
		//Mangrove
		MultiOutputRecipeBuilder.grinding(Items.MANGROVE_DOOR, 1.0F, 0.1F, 400)
				.result(Items.MANGROVE_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/mangrove_planks_from_door"));
		MultiOutputRecipeBuilder.grinding(tag(ItemTags.MANGROVE_LOGS), 1.0F, 0.1F, 400)
				.result(Items.MANGROVE_PLANKS, 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/mangrove_planks_from_log"));
		MultiOutputRecipeBuilder.grinding(Items.MANGROVE_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.result(Items.MANGROVE_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/mangrove_planks_from_pressure_plate"));
		//Dark_Oak
		MultiOutputRecipeBuilder.grinding(Items.DARK_OAK_DOOR, 1.0F, 0.1F, 400)
				.result(Items.DARK_OAK_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/dark_oak_planks_from_door"));
		MultiOutputRecipeBuilder.grinding(tag(ItemTags.DARK_OAK_LOGS), 1.0F, 0.1F, 400)
				.result(Items.DARK_OAK_PLANKS, 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/dark_oak_planks_from_log"));
		MultiOutputRecipeBuilder.grinding(Items.DARK_OAK_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.result(Items.DARK_OAK_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/dark_oak_planks_from_pressure_plate"));
		//Crimson
		MultiOutputRecipeBuilder.grinding(Items.CRIMSON_DOOR, 1.0F, 0.1F, 400)
				.result(Items.CRIMSON_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/crimson_planks_from_door"));
		MultiOutputRecipeBuilder.grinding(tag(ItemTags.CRIMSON_STEMS), 1.0F, 0.1F, 400)
				.result(Items.CRIMSON_PLANKS, 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/crimson_planks_from_log"));
		MultiOutputRecipeBuilder.grinding(Items.CRIMSON_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.result(Items.CRIMSON_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/crimson_planks_from_pressure_plate"));
		//Warped
		MultiOutputRecipeBuilder.grinding(Items.WARPED_DOOR, 1.0F, 0.1F, 400)
				.result(Items.WARPED_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/warped_planks_from_door"));
		MultiOutputRecipeBuilder.grinding(tag(ItemTags.WARPED_STEMS), 1.0F, 0.1F, 400)
				.result(Items.WARPED_PLANKS, 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/warped_planks_from_log"));
		MultiOutputRecipeBuilder.grinding(Items.WARPED_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.result(Items.WARPED_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/warped_planks_from_pressure_plate"));
		//Force
		MultiOutputRecipeBuilder.grinding(ForceRegistry.FORCE_LOG.get(), 1.0F, 0.1F, 400)
				.result(ForceRegistry.FORCE_PLANKS.getPlanks(), 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/force_planks_from_log"));
		MultiOutputRecipeBuilder.grinding(tag(ForceTags.Items.FORCE_FURNACES), 1.0F, 0.1F, 400)
				.result(ForceRegistry.FORCE_INGOT.get(), 3)
				.secondaryResult(Items.IRON_INGOT, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/ingots_from_force_furnace"));

		//Flint
		MultiOutputRecipeBuilder.grinding(tag(Tags.Items.GRAVELS), 1.0F, 0.1F, 400)
				.result(Items.FLINT, 1)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/flint_from_gravel"));
		//Iron
		MultiOutputRecipeBuilder.grinding(Items.IRON_DOOR, 1.0F, 0.1F, 400)
				.result(Items.IRON_INGOT, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/iron_ingot_from_door"));
		//Paper
		MultiOutputRecipeBuilder.grinding(Items.BOOK, 1.0F, 0.1F, 400)
				.result(Items.PAPER, 3)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/paper_from_book"));

		//String
		MultiOutputRecipeBuilder.grinding(tag(ItemTags.WOOL), 1.0F, 0.1F, 400)
				.result(Items.STRING, 4)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/string_from_wool"));
		MultiOutputRecipeBuilder.grinding(tag(ItemTags.WOOL_CARPETS), 1.0F, 0.1F, 400)
				.result(Items.STRING, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/string_from_wool_carpet"));

		//Pressure plates
		MultiOutputRecipeBuilder.grinding(Items.STONE_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.result(Items.STONE, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/stone_from_pressure_plate"));
		MultiOutputRecipeBuilder.grinding(Items.LIGHT_WEIGHTED_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.result(Items.GOLD_INGOT, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/gold_ingot_from_light_weighted_pressure_plate"));
		MultiOutputRecipeBuilder.grinding(Items.HEAVY_WEIGHTED_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.result(Items.IRON_INGOT, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/iron_ingot_from_heavy_weighted_pressure_plate"));

		//Sand
		MultiOutputRecipeBuilder.grinding(Items.SANDSTONE, 1.0F, 0.1F, 400)
				.result(Items.SAND, 1)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/sand_from_sandstone"));
		MultiOutputRecipeBuilder.grinding(Items.RED_SANDSTONE, 1.0F, 0.1F, 400)
				.result(Items.RED_SAND, 1)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/sand_from_red_sandstone"));
		MultiOutputRecipeBuilder.grinding(tag(Tags.Items.COBBLESTONES_NORMAL), 1.0F, 0.1F, 400)
				.result(Items.COBBLESTONE, 1)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/sand_from_cobblestone"));

		//Cobblestone
		MultiOutputRecipeBuilder.grinding(Items.FURNACE, 1.0F, 0.1F, 400)
				.result(Items.COBBLESTONE, 8)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/cobblestone_from_furnace"));

//		//Bacon
//		MultiOutputRecipeBuilder.grinding(Items.PORKCHOP, 1.0F, 0.1F, 400)
//				.result(ForceRegistry.RAW_BACON.get(), 2)
//				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
//				.save(output, key("grinding/raw_bacon_from_porkchop"));
//		MultiOutputRecipeBuilder.grinding(Items.COOKED_PORKCHOP, 1.0F, 0.1F, 400)
//				.result(ForceRegistry.COOKED_BACON.get(), 2)
//				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
//				.save(output, key("grinding/cooked_bacon_from_cooked_porkchop"));

		//Powder
		MultiOutputRecipeBuilder.grinding(tag(Tags.Items.RODS_BLAZE), 1.0F, 0.1F, 400)
				.result(Items.BLAZE_POWDER, 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/blaze_powder_from_blaze_rod"));
		MultiOutputRecipeBuilder.grinding(tag(Tags.Items.BONES), 1.0F, 0.1F, 400)
				.result(Items.BONE_MEAL, 5)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/bone_meal_from_bone"));
		MultiOutputRecipeBuilder.grinding(tag(Tags.Items.FEATHERS), 1.0F, 0.1F, 400)
				.result(ForceRegistry.FEATHER_DUST, 1)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, key("grinding/feather_dust_from_feathers"));
	}

	private ResourceKey<Recipe<?>> key(String path) {
		return ResourceKey.create(Registries.RECIPE, Reference.modLoc(path));
	}

	/**
	 * Edit to keep the forcecraft namespace
	 */
	@Override
	protected void stonecutterResultFromBase(RecipeCategory category, ItemLike result, ItemLike base, int count) {
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(base), category, result, count)
				.unlockedBy(getHasName(base), this.has(base))
				.save(this.output, "forcecraft:" + getConversionRecipeName(result, base) + "_stonecutting");
	}

	public static class Runner extends RecipeProvider.Runner {
		public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
			super(output, completableFuture);
		}

		@Override
		protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
			return new ForceRecipeProvider(provider, recipeOutput);
		}

		@Override
		public String getName() {
			return "ForceCraft Recipes";
		}
	}
}
