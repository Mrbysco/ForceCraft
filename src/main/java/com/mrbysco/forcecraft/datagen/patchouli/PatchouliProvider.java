package com.mrbysco.forcecraft.datagen.patchouli;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import xyz.brassgoggledcoders.patchouliprovider.BookBuilder;
import xyz.brassgoggledcoders.patchouliprovider.CategoryBuilder;
import xyz.brassgoggledcoders.patchouliprovider.PatchouliBookProvider;

import java.util.function.Consumer;

public class PatchouliProvider extends PatchouliBookProvider {
	public PatchouliProvider(PackOutput packOutput) {
		super(packOutput, Reference.MOD_ID, "en_us");
	}

	@Override
	protected void addBooks(Consumer<BookBuilder> consumer) {
		BookBuilder bookBuilder = createBookBuilder("force_and_you", "item.forcecraft.book.name", "info.forcecraft.book.landing")
				.setSubtitle("info.forcecraft.book.subtitle")
				.setAdvancementsTab("forcecraft:root")
				.setCreativeTab("forcecraft")
				.setModel("forcecraft:force_and_you")
				.setBookTexture("forcecraft:textures/gui/book.png")
				.setShowProgress(false)
				.setI18n(true)
//                .setUseBlockyFont(true)
				.setFillerTexture("forcecraft:textures/gui/page_filler.png")
				.addMacro("$(item)", "$(#b89232)")
				.setUseResourcePack(true)

				//infuser category
				.addCategory("infuser", "info.forcecraft.book.infuser.name", "info.forcecraft.book.infuser.desc", "forcecraft:infuser")
				//Add leveling altar entry
				.addEntry("infuser/infuser", "info.forcecraft.book.infuser.entry.name", "forcecraft:infuser")
				.addTextPage("info.forcecraft.book.infuser_info.text").build()
				.addCraftingPage(new ResourceLocation(Reference.MOD_ID, "transmutation/infuser")).setText("info.forcecraft.book.infuser_recipe.text").build().build()

				.addEntry("infuser/requirements", "info.forcecraft.book.requirements.entry.name", "forcecraft:infuser")
				.addTextPage("info.forcecraft.book.requirements_info.text").build()
				.addSpotlightPage(new ItemStack(ForceRegistry.FORCE_GEM.get()))
				.setText("info.forcecraft.book.requirements_gem.text").build()
				.addSpotlightPage(new ItemStack(ForceRegistry.UPGRADE_TOME.get()))
				.setText("info.forcecraft.book.requirements_tome.text").build().build()

				.addEntry("infuser/force_engine", "info.forcecraft.book.force_engine.entry.name", "forcecraft:force_engine")
				.addTextPage("info.forcecraft.book.force_engine_info.text").build()
				.addTextPage("info.forcecraft.book.force_engine_info_2.text").build()
				.addTextPage("info.forcecraft.book.force_engine_info_3.text").build()
				.addCraftingPage(new ResourceLocation(Reference.MOD_ID, "force_engine")).setText("info.forcecraft.book.force_engine_recipe.text").build().build()

				.addEntry("infuser/force_rod", "info.forcecraft.book.force_rod.entry.name", "forcecraft:force_rod")
				.addTextPage("info.forcecraft.book.force_rod_info.text").build()
				.addCraftingPage(new ResourceLocation(Reference.MOD_ID, "force_rod"))
				.setText("info.forcecraft.book.force_rod_recipe.text").build().build()

				.addEntry("infuser/force_ingot", "info.forcecraft.book.force_ingot.entry.name", "forcecraft:force_ingot")
				.addTextPage("info.forcecraft.book.force_ingot_info.text").build()
				.addCraftingPage(new ResourceLocation(Reference.MOD_ID, "iron_to_force_ingot"))
				.setText("info.forcecraft.book.force_ingot_recipe.text").build()
				.addCraftingPage(new ResourceLocation(Reference.MOD_ID, "gold_to_force_ingot"))
				.setText("info.forcecraft.book.force_ingot_recipe2.text").build()
				.addCraftingPage(new ResourceLocation(Reference.MOD_ID, "nuggets_to_force_ingot"))
				.setText("info.forcecraft.book.force_ingot_recipe3.text").build().build()

				.addEntry("infuser/force_shears", "info.forcecraft.book.force_shears.entry.name", "forcecraft:force_shears")
				.addTextPage("info.forcecraft.book.force_shears_info.text").build()
				.addTextPage("info.forcecraft.book.force_shears_info2.text").build()
				.addCraftingPage(new ResourceLocation(Reference.MOD_ID, "force_shears"))
				.setText("info.forcecraft.book.force_shears_recipe.text").build().build()

				.addEntry("infuser/baconator", "info.forcecraft.book.baconator.entry.name", "forcecraft:baconator")
				.addTextPage("info.forcecraft.book.baconator_info.text").build()
				.addCraftingPage(new ResourceLocation(Reference.MOD_ID, "baconator"))
				.setText("info.forcecraft.book.baconator_recipe.text").build()
				.build()

				.addEntry("infuser/bacon", "info.forcecraft.book.bacon.entry.name", "forcecraft:raw_bacon")
				.addTextPage("info.forcecraft.book.bacon_info.text").build()
				.addSmeltingPage(new ResourceLocation(Reference.MOD_ID, "cooked_bacon_from_smelting"))
				.setText("info.forcecraft.book.bacon_recipe.text").build()
				.addEntityPage("minecraft:pig").build()
				.addEntityPage("forcecraft:cold_pig").build()
				.build()

				.addEntry("infuser/claw", "info.forcecraft.book.claw.entry.name", "forcecraft:claw")
				.addTextPage("info.forcecraft.book.claw_info.text").build()
				.addEntityPage("minecraft:bat").build().build()

				.addEntry("infuser/bottled_wither", "info.forcecraft.book.bottled_wither.entry.name", "forcecraft:bottled_wither")
				.addTextPage("info.forcecraft.book.bottled_wither_info.text").build()
				.addEntityPage("minecraft:wither").build().build()

				.addEntry("infuser/inert_core", "info.forcecraft.book.inert_core.entry.name", "forcecraft:inert_core")
				.addTextPage("info.forcecraft.book.inert_core_info.text").build()
				.addImagePage(new ResourceLocation(Reference.MOD_ID, "textures/gui/entries/burning_inert.png"))
				.setText("info.forcecraft.book.inert_core_info2.text").build()
				.addSpotlightPage(new ItemStack(ForceRegistry.BOTTLED_WITHER.get())).build()
				.build()

				.addEntry("infuser/force_flask", "info.forcecraft.book.force_flask.entry.name", "forcecraft:force_flask")
				.addTextPage("info.forcecraft.book.force_flask_info.text").build()
				.addCraftingPage(new ResourceLocation(Reference.MOD_ID, "force_flask"))
				.setText("info.forcecraft.book.force_flask_recipe.text").build()
				.addCraftingPage(new ResourceLocation(Reference.MOD_ID, "force_flask_from_entity"))
				.setText("info.forcecraft.book.force_flask_recipe2.text").build()
				.build()

				.addEntry("infuser/furnace", "info.forcecraft.book.force_furnace.entry.name", "forcecraft:force_furnace")
				.addTextPage("info.forcecraft.book.force_furnace_info.text").build()
				.addSpotlightPage(new ItemStack(ForceRegistry.SPEED_CORE.get()))
				.setText("info.forcecraft.book.force_furnace_speed_core.text").build()
				.addSpotlightPage(new ItemStack(ForceRegistry.HEAT_CORE.get()))
				.setText("info.forcecraft.book.force_furnace_heat_core.text").build()
				.addSpotlightPage(new ItemStack(ForceRegistry.GRINDING_CORE.get()))
				.setText("info.forcecraft.book.force_furnace_grinding_core.text").build()
				.addSpotlightPage(new ItemStack(ForceRegistry.FREEZING_CORE.get()))
				.setText("info.forcecraft.book.force_furnace_freezing_core.text").build()
				.addSpotlightPage(new ItemStack(ForceRegistry.EXPERIENCE_CORE.get()))
				.setText("info.forcecraft.book.force_furnace_experience_core.text").build()
				.addCraftingPage(new ResourceLocation(Reference.MOD_ID, "force_furnace")).setText("info.forcecraft.book.force_furnace_recipe.text").build().build()

				.addEntry("infuser/upgrade_core", "info.forcecraft.book.upgrade_core.entry.name", "forcecraft:upgrade_core")
				.addTextPage("info.forcecraft.book.upgrade_core_info.text").build()
				.addCraftingPage(new ResourceLocation(Reference.MOD_ID, "transmutation/upgrade_core")).setText("info.forcecraft.book.upgrade_core_recipe.text").build().build()

				.build(); //Back to the bookbuilder

		bookBuilder = addTier0(bookBuilder);
		bookBuilder = addTier1(bookBuilder);
		bookBuilder = addTier2(bookBuilder);
		bookBuilder = addTier3(bookBuilder);
		bookBuilder = addTier4(bookBuilder);
		bookBuilder = addTier5(bookBuilder);
		bookBuilder = addTier6(bookBuilder);
		bookBuilder = addTier7(bookBuilder);

		//Finish book
		bookBuilder.build(consumer);
	}

	public BookBuilder addTier0(BookBuilder builder) {
		CategoryBuilder tierCategory = builder.addCategory("tier_0", "info.forcecraft.book.tier_0.name", "info.forcecraft.book.tier_0.desc", "forcecraft:infuser")
				.setSecret(true);

		ItemStack nuggetStack = new ItemStack(ForceRegistry.FORCE_NUGGET.get());
		tierCategory.addEntry("tier_0/force", "info.forcecraft.book.force.entry.name", nuggetStack).setSecret(true)
				.setAdvancement("forcecraft:tier0/tier")
				.addTextPage("info.forcecraft.book.force_info.text").build()
				.addSpotlightPage(nuggetStack).build()
				.build();

		ItemStack clawStack = new ItemStack(ForceRegistry.CLAW.get());
		tierCategory.addEntry("tier_0/damage", "info.forcecraft.book.damage.entry.name", clawStack).setSecret(true)
				.setAdvancement("forcecraft:tier0/tier")
				.addTextPage("info.forcecraft.book.damage_info.text").build()
				.addSpotlightPage(clawStack).build().build();

		builder = tierCategory.build();
		return builder;
	}

	public BookBuilder addTier1(BookBuilder builder) {
		CategoryBuilder tierCategory = builder.addCategory("tier_1", "info.forcecraft.book.tier_1.name", "info.forcecraft.book.tier_1.desc", "forcecraft:infuser")
				.setSecret(true);

		ItemStack sugarStack = new ItemStack(Items.SUGAR);
		tierCategory.addEntry("tier_1/speed", "info.forcecraft.book.speed.entry.name", sugarStack).setSecret(true)
				.setAdvancement("forcecraft:tier1/tier")
				.addTextPage("info.forcecraft.book.speed_info.text").build()
				.addSpotlightPage(sugarStack)
				.setText("info.forcecraft.book.speed_info.text2").build()
				.build();

		ItemStack logStack = new ItemStack(ForceRegistry.FORCE_LOG.get());
		tierCategory.addEntry("tier_1/lumberjack", "info.forcecraft.book.lumberjack.entry.name", logStack).setSecret(true)
				.setAdvancement("forcecraft:tier1/tier")
				.addTextPage("info.forcecraft.book.lumberjack_info.text").build()
				.addSpotlightPage(logStack).build().build();

		ItemStack powerSourceStack = new ItemStack(ForceRegistry.GOLDEN_POWER_SOURCE.get());
		tierCategory.addEntry("tier_1/heat", "info.forcecraft.book.heat.entry.name", powerSourceStack).setSecret(true)
				.setAdvancement("forcecraft:tier1/tier")
				.addTextPage("info.forcecraft.book.heat_info.text").build()
				.addSpotlightPage(powerSourceStack).build().build();

		builder = tierCategory.build();
		return builder;
	}

	public BookBuilder addTier2(BookBuilder builder) {
		CategoryBuilder tierCategory = builder.addCategory("tier_2", "info.forcecraft.book.tier_2.name", "info.forcecraft.book.tier_2.desc", "forcecraft:infuser")
				.setSecret(true);

		ItemStack fortuneStack = new ItemStack(ForceRegistry.FORTUNE.get());
		ItemStack fortuneStack2 = new ItemStack(ForceRegistry.FORTUNE_COOKIE.get());
		tierCategory.addEntry("tier_2/luck", "info.forcecraft.book.luck.entry.name", fortuneStack).setSecret(true)
				.setAdvancement("forcecraft:tier2/tier")
				.addTextPage("info.forcecraft.book.luck_info.text").build()
				.addTextPage("info.forcecraft.book.luck_info.text2").build()
				.addSpotlightPage(fortuneStack).build()
				.addSpotlightPage(fortuneStack2).build()
				.build();

		ItemStack dyeStack = new ItemStack(Items.BLUE_DYE);
		tierCategory.addEntry("tier_2/rainbow", "info.forcecraft.book.rainbow.entry.name", dyeStack).setSecret(true)
				.setAdvancement("forcecraft:tier2/tier")
				.addTextPage("info.forcecraft.book.rainbow_info.text").build()
				.addSpotlightPage(dyeStack).build().build();

		ItemStack experienceStack = new ItemStack(Items.EXPERIENCE_BOTTLE);
		tierCategory.addEntry("tier_2/experience", "info.forcecraft.book.experience.entry.name", experienceStack).setSecret(true)
				.setAdvancement("forcecraft:tier2/tier")
				.addTextPage("info.forcecraft.book.experience_info.text").build()
				.addSpotlightPage(experienceStack).build().build();

		ItemStack upgradeStack = new ItemStack(ForceRegistry.FORCE_PACK_UPGRADE.get());
		tierCategory.addEntry("tier_2/holding", "info.forcecraft.book.holding.entry.name", upgradeStack).setSecret(true)
				.setAdvancement("forcecraft:tier2/tier")
				.addTextPage("info.forcecraft.book.holding_info.text").build()
				.addSpotlightPage(upgradeStack).build().build();

		ItemStack snowStack = new ItemStack(ForceRegistry.SNOW_COOKIE.get());
		tierCategory.addEntry("tier_2/freezing", "info.forcecraft.book.freezing.entry.name", snowStack).setSecret(true)
				.setAdvancement("forcecraft:tier2/tier")
				.addTextPage("info.forcecraft.book.freezing_info.text").build()
				.addSpotlightPage(snowStack).build().build();

		tierCategory.addEntry("tier_2/experience_core", "info.forcecraft.book.experience_core.entry.name", experienceStack).setSecret(true)
				.setAdvancement("forcecraft:tier2/tier")
				.addTextPage("info.forcecraft.book.experience_core_info.text").build()
				.addSpotlightPage(experienceStack)
				.setText("info.forcecraft.book.upgrade_core_link.text").build().build();

		tierCategory.addEntry("tier_2/freezing_core", "info.forcecraft.book.freezing_core.entry.name", snowStack).setSecret(true)
				.setAdvancement("forcecraft:tier2/tier")
				.addTextPage("info.forcecraft.book.freezing_core_info.text").build()
				.addSpotlightPage(snowStack)
				.setText("info.forcecraft.book.upgrade_core_link.text").build().build();

		ItemStack flintStack = new ItemStack(Items.FLINT);
		tierCategory.addEntry("tier_2/grinding_core", "info.forcecraft.book.grinding_core.entry.name", flintStack).setSecret(true)
				.setAdvancement("forcecraft:tier2/tier")
				.addTextPage("info.forcecraft.book.grinding_core_info.text").build()
				.addSpotlightPage(flintStack)
				.setText("info.forcecraft.book.upgrade_core_link.text").build().build();

		ItemStack sugarStack = new ItemStack(Items.SUGAR);
		tierCategory.addEntry("tier_2/speed_core", "info.forcecraft.book.speed_core.entry.name", sugarStack).setSecret(true)
				.setAdvancement("forcecraft:tier2/tier")
				.addTextPage("info.forcecraft.book.speed_core_info.text").build()
				.addSpotlightPage(sugarStack)
				.setText("info.forcecraft.book.upgrade_core_link.text").build()
				.build();


		builder = tierCategory.build();
		return builder;
	}

	public BookBuilder addTier3(BookBuilder builder) {
		CategoryBuilder tierCategory = builder.addCategory("tier_3", "info.forcecraft.book.tier_3.name", "info.forcecraft.book.tier_3.desc", "forcecraft:infuser")
				.setSecret(true);

		ItemStack upgradeStack = new ItemStack(ForceRegistry.FORCE_PACK_UPGRADE.get());
		tierCategory.addEntry("tier_3/holding_2", "info.forcecraft.book.holding_2.entry.name", upgradeStack).setSecret(true)
				.setAdvancement("forcecraft:tier3/tier")
				.addTextPage("info.forcecraft.book.holding_2_info.text").build()
				.addSpotlightPage(upgradeStack).build().build();

		ItemStack arrowStack = new ItemStack(Items.ARROW);
		tierCategory.addEntry("tier_3/bleeding", "info.forcecraft.book.bleeding.entry.name", arrowStack).setSecret(true)
				.setAdvancement("forcecraft:tier3/tier")
				.addTextPage("info.forcecraft.book.bleeding_info.text").build()
				.addSpotlightPage(arrowStack)
				.setText("info.forcecraft.book.bleeding_info.text2").build()
				.build();

		ItemStack webStack = new ItemStack(Items.COBWEB);
		tierCategory.addEntry("tier_3/silk", "info.forcecraft.book.silk.entry.name", webStack).setSecret(true)
				.setAdvancement("forcecraft:tier3/tier")
				.addTextPage("info.forcecraft.book.silk_info.text").build()
				.addSpotlightPage(webStack).build()
				.build();

		ItemStack potionStack = new ItemStack(Items.POTION);
		PotionUtils.setPotion(potionStack, Potions.INVISIBILITY);
		tierCategory.addEntry("tier_3/camo", "info.forcecraft.book.camo.entry.name", potionStack).setSecret(true)
				.setAdvancement("forcecraft:tier3/tier")
				.addTextPage("info.forcecraft.book.camo_info.text").build()
				.addSpotlightPage(potionStack).build()
				.build();

		builder = tierCategory.build();
		return builder;
	}

	public BookBuilder addTier4(BookBuilder builder) {
		CategoryBuilder tierCategory = builder.addCategory("tier_4", "info.forcecraft.book.tier_4.name", "info.forcecraft.book.tier_4.desc", "forcecraft:infuser")
				.setSecret(true);

		ItemStack upgradeStack = new ItemStack(ForceRegistry.FORCE_PACK_UPGRADE.get());
		tierCategory.addEntry("tier_4/holding_3", "info.forcecraft.book.holding_3.entry.name", upgradeStack).setSecret(true)
				.setAdvancement("forcecraft:tier4/tier")
				.addTextPage("info.forcecraft.book.holding_3_info.text").build()
				.addSpotlightPage(upgradeStack).build().build();

		ItemStack spiderStack = new ItemStack(Items.SPIDER_EYE);
		tierCategory.addEntry("tier_4/bane", "info.forcecraft.book.bane.entry.name", spiderStack).setSecret(true)
				.setAdvancement("forcecraft:tier4/tier")
				.addTextPage("info.forcecraft.book.bane_info.text").build()
				.addSpotlightPage(spiderStack)
				.setText("info.forcecraft.book.bane_info.text2").build()
				.build();

		ItemStack powerSourceStack = new ItemStack(ForceRegistry.HEAT_CORE.get());
		tierCategory.addEntry("tier_4/heat_upgrade", "info.forcecraft.book.heat_upgrade.entry.name", powerSourceStack).setSecret(true)
				.setAdvancement("forcecraft:tier4/tier")
				.addTextPage("info.forcecraft.book.heat_upgrade_info.text").build()
				.addSpotlightPage(powerSourceStack).build()
				.build();

		builder = tierCategory.build();
		return builder;
	}

	public BookBuilder addTier5(BookBuilder builder) {
		CategoryBuilder tierCategory = builder.addCategory("tier_5", "info.forcecraft.book.tier_5.name", "info.forcecraft.book.tier_5.desc", "forcecraft:infuser")
				.setSecret(true);

		ItemStack upgradeStack = new ItemStack(ForceRegistry.FORCE_PACK_UPGRADE.get());
		tierCategory.addEntry("tier_5/holding_4", "info.forcecraft.book.holding_4.entry.name", upgradeStack).setSecret(true)
				.setAdvancement("forcecraft:tier5/tier")
				.addTextPage("info.forcecraft.book.holding_4_info.text").build()
				.addSpotlightPage(upgradeStack).build().build();

		ItemStack ghastStack = new ItemStack(Items.GHAST_TEAR);
		tierCategory.addEntry("tier_5/healing", "info.forcecraft.book.healing.entry.name", ghastStack).setSecret(true)
				.setAdvancement("forcecraft:tier5/tier")
				.addTextPage("info.forcecraft.book.healing_info.text").build()
				.addSpotlightPage(ghastStack).build()
				.build();

		ItemStack featherStack = new ItemStack(Items.FEATHER);
		tierCategory.addEntry("tier_5/wing", "info.forcecraft.book.wing.entry.name", featherStack).setSecret(true)
				.setAdvancement("forcecraft:tier5/tier")
				.addTextPage("info.forcecraft.book.wing_info.text").build()
				.addSpotlightPage(featherStack).build()
				.build();

		builder = tierCategory.build();
		return builder;
	}

	public BookBuilder addTier6(BookBuilder builder) {
		CategoryBuilder tierCategory = builder.addCategory("tier_6", "info.forcecraft.book.tier_6.name", "info.forcecraft.book.tier_6.desc", "forcecraft:infuser")
				.setSecret(true);

		ItemStack clockStack = new ItemStack(Items.CLOCK);
		tierCategory.addEntry("tier_6/time", "info.forcecraft.book.time.entry.name", clockStack).setSecret(true)
				.setAdvancement("forcecraft:tier6/tier")
				.addTextPage("info.forcecraft.book.time_info.text").build()
				.addSpotlightPage(clockStack).build()
				.build();

		ItemStack pearlStack = new ItemStack(Items.ENDER_PEARL);
		tierCategory.addEntry("tier_6/ender", "info.forcecraft.book.ender.entry.name", pearlStack).setSecret(true)
				.setAdvancement("forcecraft:tier6/tier")
				.addTextPage("info.forcecraft.book.ender_info.text").build()
				.addSpotlightPage(pearlStack)
				.setText("info.forcecraft.book.ender_info.text2").build()
				.addSpotlightPage(new ItemStack(Items.ENDER_EYE)).build()
				.build();

		ItemStack obsidianStack = new ItemStack(Items.OBSIDIAN);
		tierCategory.addEntry("tier_6/sturdy", "info.forcecraft.book.sturdy.entry.name", obsidianStack).setSecret(true)
				.setAdvancement("forcecraft:tier6/tier")
				.addTextPage("info.forcecraft.book.sturdy_info.text").build()
				.addSpotlightPage(obsidianStack).build()
				.build();

		builder = tierCategory.build();
		return builder;
	}

	public BookBuilder addTier7(BookBuilder builder) {
		CategoryBuilder tierCategory = builder.addCategory("tier_7", "info.forcecraft.book.tier_7.name", "info.forcecraft.book.tier_7.desc", "forcecraft:infuser")
				.setSecret(true);

		ItemStack glowStack = new ItemStack(Items.GLOWSTONE);
		tierCategory.addEntry("tier_7/light", "info.forcecraft.book.light.entry.name", glowStack).setSecret(true)
				.setAdvancement("forcecraft:tier7/tier")
				.addTextPage("info.forcecraft.book.light_info.text").build()
				.addSpotlightPage(glowStack).build()
				.build();

		ItemStack treasureStack = new ItemStack(ForceRegistry.TREASURE_CORE.get());
		tierCategory.addEntry("tier_7/treasure", "info.forcecraft.book.treasure.entry.name", treasureStack).setSecret(true)
				.setAdvancement("forcecraft:tier7/tier")
				.addTextPage("info.forcecraft.book.treasure_info.text").build()
				.addSpotlightPage(treasureStack).build()
				.build();

		builder = tierCategory.build();
		return builder;
	}
}
