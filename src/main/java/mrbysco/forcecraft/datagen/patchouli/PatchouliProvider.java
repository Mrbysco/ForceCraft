package mrbysco.forcecraft.datagen.patchouli;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.patchouliprovider.BookBuilder;
import xyz.brassgoggledcoders.patchouliprovider.CategoryBuilder;
import xyz.brassgoggledcoders.patchouliprovider.PatchouliBookProvider;

import java.util.function.Consumer;

public class PatchouliProvider extends PatchouliBookProvider {
	public PatchouliProvider(DataGenerator gen) {
		super(gen, Reference.MOD_ID, "en_us");
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
					.setText("info.forcecraft.book.requirements_tome.text").build()
				.build().build(); //Back to the bookbuilder

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
		CategoryBuilder tierCategory = builder.addCategory("tier_0", "info.forcecraft.book.tier_0.name", "info.forcecraft.book.tier_0.desc", "forcecraft:infuser");

		ItemStack nuggetStack = new ItemStack(ForceRegistry.FORCE_NUGGET.get());
		tierCategory.addEntry("tier_0/force", "info.forcecraft.book.force.entry.name", nuggetStack).setSecret(true)
				.addTextPage("info.forcecraft.book.force_info.text").build()
				.addSpotlightPage(nuggetStack).build()
				.build();

		ItemStack clawStack = new ItemStack(ForceRegistry.CLAW.get());
		tierCategory.addEntry("tier_0/damage", "info.forcecraft.book.damage.entry.name", clawStack).setSecret(true)
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
				.setAdvancement("forcecraft:tier1/root")
				.addTextPage("info.forcecraft.book.speed_info.text").build()
				.addSpotlightPage(sugarStack)
				.setText("info.forcecraft.book.speed_info.text2").build()
				.build();

		ItemStack logStack = new ItemStack(ForceRegistry.FORCE_LOG.get());
		tierCategory.addEntry("tier_1/lumberjack", "info.forcecraft.book.lumberjack.entry.name", logStack).setSecret(true)
				.setAdvancement("forcecraft:tier1/root")
				.addTextPage("info.forcecraft.book.lumberjack_info.text").build()
				.addSpotlightPage(logStack).build().build();

		ItemStack powerSourceStack = new ItemStack(ForceRegistry.GOLDEN_POWER_SOURCE.get());
		tierCategory.addEntry("tier_1/heat", "info.forcecraft.book.heat.entry.name", powerSourceStack).setSecret(true)
				.setAdvancement("forcecraft:tier1/root")
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
				.setAdvancement("forcecraft:tier2/root")
				.addTextPage("info.forcecraft.book.luck_info.text").build()
				.addTextPage("info.forcecraft.book.luck_info.text2").build()
				.addSpotlightPage(fortuneStack).build()
				.addSpotlightPage(fortuneStack2).build()
				.build();

		ItemStack dyeStack = new ItemStack(Items.BLUE_DYE);
		tierCategory.addEntry("tier_2/rainbow", "info.forcecraft.book.rainbow.entry.name", dyeStack).setSecret(true)
				.setAdvancement("forcecraft:tier2/root")
				.addTextPage("info.forcecraft.book.rainbow_info.text").build()
				.addSpotlightPage(dyeStack).build().build();

		ItemStack experienceStack = new ItemStack(Items.EXPERIENCE_BOTTLE);
		tierCategory.addEntry("tier_2/experience", "info.forcecraft.book.experience.entry.name", experienceStack).setSecret(true)
				.setAdvancement("forcecraft:tier2/root")
				.addTextPage("info.forcecraft.book.experience_info.text").build()
				.addSpotlightPage(experienceStack).build().build();

		ItemStack upgradeStack = new ItemStack(ForceRegistry.FORCE_PACK_UPGRADE.get());
		tierCategory.addEntry("tier_2/holding", "info.forcecraft.book.holding.entry.name", upgradeStack).setSecret(true)
				.setAdvancement("forcecraft:tier2/root")
				.addTextPage("info.forcecraft.book.holding_info.text").build()
				.addSpotlightPage(upgradeStack).build().build();

		ItemStack snowStack = new ItemStack(ForceRegistry.SNOW_COOKIE.get());
		tierCategory.addEntry("tier_2/freezing", "info.forcecraft.book.freezing.entry.name", snowStack).setSecret(true)
				.setAdvancement("forcecraft:tier2/root")
				.addTextPage("info.forcecraft.book.freezing_info.text").build()
				.addSpotlightPage(snowStack).build().build();

		ItemStack flintStack = new ItemStack(Items.FLINT);
		tierCategory.addEntry("tier_2/grinding", "info.forcecraft.book.grinding.entry.name", flintStack).setSecret(true)
				.setAdvancement("forcecraft:tier2/root")
				.addTextPage("info.forcecraft.book.grinding_info.text").build()
				.addSpotlightPage(flintStack).build().build();


		builder = tierCategory.build();
		return builder;
	}

	public BookBuilder addTier3(BookBuilder builder) {
		CategoryBuilder tierCategory = builder.addCategory("tier_3", "info.forcecraft.book.tier_3.name", "info.forcecraft.book.tier_3.desc", "forcecraft:infuser");

		ItemStack arrowStack = new ItemStack(Items.ARROW);
		tierCategory.addEntry("tier_3/bleeding", "info.forcecraft.book.bleeding.entry.name", arrowStack).setSecret(true)
				.addTextPage("info.forcecraft.book.bleeding_info.text").build()
				.addSpotlightPage(arrowStack)
				.setText("info.forcecraft.book.bleeding_info.text2").build()
				.build();

		ItemStack webStack = new ItemStack(Items.COBWEB);
		tierCategory.addEntry("tier_3/silk", "info.forcecraft.book.silk.entry.name", webStack).setSecret(true)
				.addTextPage("info.forcecraft.book.silk_info.text").build()
				.addSpotlightPage(webStack).build()
				.build();

		ItemStack potionStack = new ItemStack(Items.POTION);
		PotionUtils.addPotionToItemStack(potionStack, Potions.INVISIBILITY);
		tierCategory.addEntry("tier_3/camo", "info.forcecraft.book.camo.entry.name", potionStack).setSecret(true)
				.addTextPage("info.forcecraft.book.camo_info.text").build()
				.addSpotlightPage(potionStack).build()
				.build();

		builder = tierCategory.build();
		return builder;
	}

	public BookBuilder addTier4(BookBuilder builder) {
		CategoryBuilder tierCategory = builder.addCategory("tier_4", "info.forcecraft.book.tier_4.name", "info.forcecraft.book.tier_4.desc", "forcecraft:infuser");

		ItemStack spiderStack = new ItemStack(Items.SPIDER_EYE);
		tierCategory.addEntry("tier_4/bane", "info.forcecraft.book.bane.entry.name", spiderStack).setSecret(true)
				.addTextPage("info.forcecraft.book.bane_info.text").build()
				.addSpotlightPage(spiderStack)
				.setText("info.forcecraft.book.bane_info.text2").build()
				.build();

		ItemStack powerSourceStack = new ItemStack(ForceRegistry.GOLDEN_POWER_SOURCE.get());
		tierCategory.addEntry("tier_4/heat_upgrade", "info.forcecraft.book.heat_upgrade.entry.name", powerSourceStack).setSecret(true)
				.addTextPage("info.forcecraft.book.heat_upgrade_info.text").build()
				.addSpotlightPage(powerSourceStack).build()
				.build();

		builder = tierCategory.build();
		return builder;
	}

	public BookBuilder addTier5(BookBuilder builder) {
		CategoryBuilder tierCategory = builder.addCategory("tier_5", "info.forcecraft.book.tier_5.name", "info.forcecraft.book.tier_5.desc", "forcecraft:infuser");

		ItemStack ghastStack = new ItemStack(Items.GHAST_TEAR);
		tierCategory.addEntry("tier_5/healing", "info.forcecraft.book.healing.entry.name", ghastStack).setSecret(true)
				.addTextPage("info.forcecraft.book.healing_info.text").build()
				.addSpotlightPage(ghastStack).build()
				.build();

		ItemStack featherStack = new ItemStack(Items.FEATHER);
		tierCategory.addEntry("tier_5/wing", "info.forcecraft.book.wing.entry.name", featherStack).setSecret(true)
				.addTextPage("info.forcecraft.book.wing_info.text").build()
				.addSpotlightPage(featherStack).build()
				.build();

		builder = tierCategory.build();
		return builder;
	}

	public BookBuilder addTier6(BookBuilder builder) {
		CategoryBuilder tierCategory = builder.addCategory("tier_6", "info.forcecraft.book.tier_6.name", "info.forcecraft.book.tier_6.desc", "forcecraft:infuser");

		ItemStack clockStack = new ItemStack(Items.CLOCK);
		tierCategory.addEntry("tier_6/time", "info.forcecraft.book.time.entry.name", clockStack).setSecret(true)
				.addTextPage("info.forcecraft.book.time_info.text").build()
				.addSpotlightPage(clockStack).build()
				.build();

		ItemStack pearlStack = new ItemStack(Items.ENDER_PEARL);
		tierCategory.addEntry("tier_6/ender", "info.forcecraft.book.ender.entry.name", pearlStack).setSecret(true)
				.addTextPage("info.forcecraft.book.ender_info.text").build()
				.addSpotlightPage(pearlStack)
				.setText("info.forcecraft.book.ender_info.text2").build()
				.addSpotlightPage(new ItemStack(Items.ENDER_EYE)).build()
				.build();

		ItemStack obsidianStack = new ItemStack(Items.OBSIDIAN);
		tierCategory.addEntry("tier_6/sturdy", "info.forcecraft.book.sturdy.entry.name", obsidianStack).setSecret(true)
				.addTextPage("info.forcecraft.book.sturdy_info.text").build()
				.addSpotlightPage(obsidianStack).build()
				.build();

		builder = tierCategory.build();
		return builder;
	}

	public BookBuilder addTier7(BookBuilder builder) {
		CategoryBuilder tierCategory = builder.addCategory("tier_7", "info.forcecraft.book.tier_7.name", "info.forcecraft.book.tier_7.desc", "forcecraft:infuser");

		ItemStack glowStack = new ItemStack(Items.GLOWSTONE);
		tierCategory.addEntry("tier_7/light", "info.forcecraft.book.light.entry.name", glowStack).setSecret(true)
				.addTextPage("info.forcecraft.book.light_info.text").build()
				.addSpotlightPage(glowStack).build()
				.build();

		ItemStack treasureStack = new ItemStack(ForceRegistry.TREASURE_CORE.get());
		tierCategory.addEntry("tier_7/treasure", "info.forcecraft.book.treasure.entry.name", treasureStack).setSecret(true)
				.addTextPage("info.forcecraft.book.treasure_info.text").build()
				.addSpotlightPage(treasureStack).build()
				.build();

		builder = tierCategory.build();
		return builder;
	}
}
