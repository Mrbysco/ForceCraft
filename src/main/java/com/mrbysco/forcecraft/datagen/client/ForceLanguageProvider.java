package com.mrbysco.forcecraft.datagen.client;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceEffects;
import com.mrbysco.forcecraft.registry.ForceEntities;
import com.mrbysco.forcecraft.registry.ForceFluids;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceSounds;
import com.mrbysco.forcecraft.registry.ForceTags;
import com.mrbysco.forcecraft.registry.helper.ForceBrickReg;
import com.mrbysco.forcecraft.registry.helper.ForceFurnaceReg;
import com.mrbysco.forcecraft.registry.helper.ForceTorchReg;
import net.minecraft.data.PackOutput;
import net.minecraft.locale.Language;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ForceLanguageProvider extends LanguageProvider {
	public ForceLanguageProvider(PackOutput packOutput) {
		super(packOutput, Reference.MOD_ID, Language.DEFAULT);
	}

	@Override
	protected void addTranslations() {
		add("itemGroup.forcecraft", "ForceCraft");

		addTags();

		addEntityType(ForceEntities.BOMB, "Bomb");
		addEntityType(ForceEntities.FORCE_ARROW, "Ice Arrow");

		addBlock(ForceRegistry.POWER_ORE, "Power Ore");
		addBlock(ForceRegistry.DEEPSLATE_POWER_ORE, "Deepslate Power Ore");
		addBlock(ForceRegistry.NETHER_POWER_ORE, "Nether Power Ore");
		addBlock(ForceRegistry.FORCE_LOG, "Force Log");
		addBlock(ForceRegistry.FORCE_WOOD, "Force Wood");
		addBlock(ForceRegistry.FORCE_LEAVES, "Force Leaves");
		addBlock(ForceRegistry.FORCE_SAPLING, "Force Sapling");
		addBlock(ForceRegistry.FORCE_PLANKS.getPlanks(), "Force Planks");
		addBlock(ForceRegistry.FORCE_PLANKS.getButton(), "Force Button");
		addBlock(ForceRegistry.FORCE_PLANKS.getFence(), "Force Fence");
		addBlock(ForceRegistry.FORCE_PLANKS.getFenceGate(), "Force Fence Gate");
		addBlock(ForceRegistry.FORCE_PLANKS.getPressurePlate(), "Force Pressure Plate");
		addBlock(ForceRegistry.FORCE_PLANKS.getSign(), "Force Sign");
		addBlock(ForceRegistry.FORCE_PLANKS.getHangingSign(), "Force Hanging Sign");
		addBlock(ForceRegistry.FORCE_PLANKS.getStairs(), "Force Stairs");
		addBlock(ForceRegistry.FORCE_PLANKS.getSlab(), "Force Slab");
		addBlock(ForceRegistry.FORCE_PLANKS.getDoor(), "Force Door");
		addBlock(ForceRegistry.FORCE_PLANKS.getTrapdoor(), "Force Trapdoor");
		addBlock(ForceRegistry.FORCE_PLANKS.getShelf(), "Force Shelf");
		addBlock(ForceRegistry.FORCE_ENGINE, "Force Engine");
		addBlock(ForceRegistry.FORCE_INFUSER, "Force Infuser");
		addBlock(ForceRegistry.FORCE_FURNACE, "Force Furnace");
		for (ForceFurnaceReg reg : ForceRegistry.getFurnaces()) {
			String colorPrefix = getColorPrefix(reg.getColor());
			addBlock(reg.getFurnace(), colorPrefix + "Force Furnace");
		}

		addItem(ForceRegistry.FORCE_HELMET, "Force Cap");
		addItem(ForceRegistry.FORCE_CHEST, "Force Tunic");
		addItem(ForceRegistry.FORCE_LEGS, "Force Kilt");
		addItem(ForceRegistry.FORCE_BOOTS, "Force Boots");

		addItem(ForceRegistry.FORCE_GEM, "Force Gem");
		addItem(ForceRegistry.FORCE_NUGGET, "Force Nugget");
		addItem(ForceRegistry.FORCE_INGOT, "Force Ingot");
		addItem(ForceRegistry.FORCE_STICK, "Force Stick");
		addItem(ForceRegistry.FORCE_GEAR, "Force Gear");
		addItem(ForceRegistry.GOLDEN_POWER_SOURCE, "Golden Power Source");
		addItem(ForceRegistry.CLAW, "Claw");
		addItem(ForceRegistry.INERT_CORE, "Inert Core");
		addItem(ForceRegistry.ROCS_FEATHER, "Roc's Feather");
		addItem(ForceRegistry.FEATHER_DUST, "Feather Dust");
		addItem(ForceRegistry.TREASURE_CORE, "Treasure Core");
		addItem(ForceRegistry.SOUL_WAFER, "Soul Wafer");
		addItem(ForceRegistry.UPGRADE_TOME, "Upgrade Tome");
		addItem(ForceRegistry.EXPERIENCE_TOME, "Experience Tome");
		addItem(ForceRegistry.FORTUNE_COOKIE, "Fortune Cookie");
		addItem(ForceRegistry.FORTUNE, "Fortune");
		addItem(ForceRegistry.FORCE_ROD, "Force Rod");
		add("item.forcecraft.force_rod.time", "Rod of Time");
		add("item.forcecraft.force_rod.speed1", "Rod of Speed I");
		add("item.forcecraft.force_rod.speed2", "Rod of Speed II");
		add("item.forcecraft.force_rod.speed3", "Rod of Speed III");
		add("item.forcecraft.force_rod.speed4", "Rod of Speed IV");
		add("item.forcecraft.force_rod.speed5", "Rod of Speed V");
		add("item.forcecraft.force_rod.healing1", "Rod of Restoration MK1");
		add("item.forcecraft.force_rod.healing2", "Rod of Restoration MK2");
		add("item.forcecraft.force_rod.heat", "Rod of Fire Resistance");
		add("item.forcecraft.force_rod.ender", "Rod of Return");
		add("item.forcecraft.force_rod.camo", "Rod of Invisibility");
		add("item.forcecraft.force_rod.sight", "Rod of Vision");
		addItem(ForceRegistry.FORCE_PICKAXE, "Force Pickaxe");
		addItem(ForceRegistry.FORCE_AXE, "Force Axe");
		addItem(ForceRegistry.FORCE_SWORD, "Force Sword");
		addItem(ForceRegistry.FORCE_SHOVEL, "Force Shovel");
		addItem(ForceRegistry.FORCE_SHEARS, "Force Shears");
		addItem(ForceRegistry.FORCE_BOW, "Force Bow");
		addItem(ForceRegistry.FORCE_MITT, "Force Mitt");
		addItem(ForceRegistry.FORCE_BUCKET, "Force Bucket");
		addItem(ForceRegistry.FORCE_ARROW, "Force Arrow");
		addItem(ForceRegistry.SNOW_COOKIE, "Snow Cookie");
		addItem(ForceRegistry.BOMB, "Bomb");
		addItem(ForceRegistry.SUPER_BOMB, "Super Bomb");

		addItem(ForceRegistry.FORCE_PACK, "Force Pack");
		addItem(ForceRegistry.FORCE_PACK_UPGRADE, "Force Pack Upgrade");
		addItem(ForceRegistry.FORCE_BELT, "Force Belt");
		addItem(ForceRegistry.UPGRADE_CORE, "Upgrade Core");
		addItem(ForceRegistry.EXPERIENCE_CORE, "Experience Core");
		addItem(ForceRegistry.FROZEN_CORE, "Frozen Core");
		addItem(ForceRegistry.GRINDING_CORE, "Grinding Core");
		addItem(ForceRegistry.HEAT_CORE, "Heat Core");
		addItem(ForceRegistry.SPEED_CORE, "Speed Core");

		add(ForceRegistry.FORCE_FLUID_BLOCK.get(), "Liquid Force");
		add(ForceFluids.FORCE_TYPE.get().getDescriptionId(), "Liquid Force");
		add("fluid.forcecraft.fluid_force_source", "Liquid Force");

		for (ForceBrickReg brickReg : ForceRegistry.getBricks()) {
			String colorPrefix = getColorPrefix(brickReg.getColor());
			addBlock(brickReg.getBrick(), colorPrefix + "Force Brick");
			addBlock(brickReg.getWall(), colorPrefix + "Force Brick Wall");
			addBlock(brickReg.getStairs(), colorPrefix + "Force Brick Stairs");
			addBlock(brickReg.getSlab(), colorPrefix + "Force Brick Slab");
		}

		addTorch(ForceRegistry.FORCE_TORCH, "Force Torch");
		for (ForceTorchReg torchReg : ForceRegistry.getTorches()) {
			if (torchReg.getColor() != null) {
				String colorPrefix = getColorPrefix(torchReg.getColor());
				addTorch(torchReg, colorPrefix + "Force Torch");
			}
		}
		addTorch(ForceRegistry.HEAT_TORCH, "Heat Torch");
		addTorch(ForceRegistry.HEALING_TORCH, "Healing Torch");
		addTorch(ForceRegistry.BANE_TORCH, "Bane Torch");
		addTorch(ForceRegistry.CAMO_TORCH, "Camo Torch");
		addTorch(ForceRegistry.TIME_TORCH, "Time Torch");

		// Config
		addConfig("general", "General", "General Settings");
		addConfig("forceInfusingTime", "Force Infusing Time", "The max amount of ticks it takes to charge a tool with Force [Default: 120 (6 seconds)]");

		addConfig("rod", "Rod", "Rod Settings");
		addConfig("buffRadius", "Buff Radius", "The radius of the buff effect around the activated position [Default: 5]");
		addConfig("timeRadius", "Time Radius", "The radius of the time effect around the activated position [Default: 5]");
		addConfig("timeCooldown", "Time Cooldown", "The cooldown of the Rod of Time in ticks [Default: 100]");
		addConfig("entitySpeedDuration", "Entity Speed Duration", "The duration of the speed effect for entities in ticks (20 ticks = 1 second) [Default: 200]");
		addConfig("blockSpeedDuration", "Block Speed Duration", "The duration of the speed effect for blocks in ticks (20 ticks = 1 second) [Default: 200]");
		addConfig("slowSpeed", "Slow Speed", "The speed multiplier for slow time mode [Default: 0.25]");
		addConfig("fastSpeed", "Fast Speed", "The speed multiplier for fast time mode [Default: 2.0]");
		addConfig("hyperSpeed", "Hyper Speed", "The speed multiplier for hyper time mode [Default: 4.0]");

		addConfig("worldgen", "World Generation", "World Generation Settings");
		addConfig("generateForceOre", "Generate Force Ore", "Enable Force Ore generation [Default: true]");
		addConfig("generateNetherForceOre", "Generate Nether Force Ore", "Enable Force Ore generation in The Nether [Default: true]");
		addConfig("generateForceTree", "Generate Force Tree", "Enable Force Tree generation [Default: true]");

		//Networking
		add("forcecraft.networking.ender_pos_change.failed", "Failed to change Rod Position: %s");
		add("forcecraft.networking.pack_change.failed", "Failed to change Force Pack: %s");
		add("forcecraft.networking.stop_infuser_sound.failed", "Failed to stop Infuser sound: %s");

		//Subtitles
		addSubtitle(ForceSounds.WHOOSH, "Whooshing");
		addSubtitle(ForceSounds.FAIRY_PICKUP, "Fairy being absorbed");
		addSubtitle(ForceSounds.FAIRY_LISTEN, "Fairy yelling");
		addSubtitle(ForceSounds.FAIRY_LISTEN_SPECIAL, "Fairy wishing a happy birthday");
		addSubtitle(ForceSounds.INFUSER_DONE, "Infuser Completes");
		addSubtitle(ForceSounds.INFUSER_WORKING, "Infuser Processing");
		addSubtitle(ForceSounds.INFUSER_SPECIAL_BEEP, "Infuser Beeps");
		addSubtitle(ForceSounds.INFUSER_SPECIAL_DONE, "Infuser Completes");
		addSubtitle(ForceSounds.INFUSER_SPECIAL, "Infuser Humming");
		addSubtitle(ForceSounds.HEART_PICKUP, "Heart Pickup");
		addSubtitle(ForceSounds.FORCE_PUNCH, "Force Punched");

		//Effects
		addEffect(ForceEffects.BLEEDING, "Bleeding");
		addEffect(ForceEffects.FROZEN, "Frozen");
		addEffect(ForceEffects.TIME_SLOWDOWN, "Time Slowdown");
		addEffect(ForceEffects.TIME_SPEEDUP, "Time Speedup");

		// Components
		add("forcecraft.component.force_stored", "Force Stored: %s");

		add("forcecraft.component.force1", "Force I");
		add("forcecraft.component.force2", "Force II");

		add("forcecraft.component.damage1", "Damage I");
		add("forcecraft.component.damage2", "Damage II");
		add("forcecraft.component.damage3", "Damage III");
		add("forcecraft.component.damage4", "Damage IV");
		add("forcecraft.component.damage5", "Damage V");

		add("forcecraft.component.speed1", "Speed I");
		add("forcecraft.component.speed2", "Speed II");
		add("forcecraft.component.speed3", "Speed III");
		add("forcecraft.component.speed4", "Speed IV");
		add("forcecraft.component.speed5", "Speed V");

		add("forcecraft.component.lumberjack", "Lumberjack");
		add("forcecraft.component.rainbow", "Rainbow");

		add("forcecraft.component.luck1", "Luck I");
		add("forcecraft.component.luck2", "Luck II");
		add("forcecraft.component.luck3", "Luck III");
		add("forcecraft.component.luck4", "Luck IV");

		add("forcecraft.component.grinding", "Grinding");
		add("forcecraft.component.freezing", "Freezing");
		add("forcecraft.component.false", "False");
		add("forcecraft.component.touch", "Touch");

		add("forcecraft.component.bleed1", "Bleeding I");
		add("forcecraft.component.bleed2", "Bleeding II");
		add("forcecraft.component.bleed3", "Bleeding III");

		add("forcecraft.component.time_mode", "Mode: %s");
		add("forcecraft.time_mode.stop", "Stop");
		add("forcecraft.time_mode.slow", "Slow");
		add("forcecraft.time_mode.fast", "Fast");
		add("forcecraft.time_mode.hyper", "Hyper");

		add("forcecraft.component.craft", "Craft");
		add("forcecraft.component.time", "Time");
		add("forcecraft.component.heat", "Heat");
		add("forcecraft.component.bane", "Bane");
		add("forcecraft.component.forge", "Forge");
		add("forcecraft.component.wing", "Wing");

		add("forcecraft.component.healing1", "Healing I");
		add("forcecraft.component.healing2", "Healing II");

		add("forcecraft.component.camo", "Camo");
		add("forcecraft.component.sight", "Sight");

		add("forcecraft.component.sturdy1", "Sturdy I");
		add("forcecraft.component.sturdy2", "Sturdy II");

		add("forcecraft.component.ender", "Ender");
		add("forcecraft.component.soul", "Soul");

		add("forcecraft.component.light1", "Light I");
		add("forcecraft.component.light2", "Light II");
		add("forcecraft.component.light3", "Light III");
		add("forcecraft.component.light4", "Light IV");
		add("forcecraft.component.light5", "Light V");

		add("forcecraft.component.treasure", "Treasure");

		//Item tooltips
		add("forcecraft.component.upgrade_book.tier", "Tier %s");
		add("forcecraft.component.upgrade_book.points", "%s Force Points");
		add("forcecraft.component.upgrade_book.next_tier", "Next Tier: %s");
		add("forcecraft.component.upgrade_book.max_tier", "Max Tier");
		add("forcecraft.serious_bomb.tooltip", "Serious Business.");
		add("forcecraft.experience_tome.tooltip", "EXP: %s");


		//UI
		add("forcecraft.gui.guide_book", "Book of Mudora");
		add("forcecraft.container.belt", "Force Belt");
		add("forcecraft.container.pack", "Force Pack");
		add("forcecraft.container.force_furnace", "Force Furnace");
		add("forcecraft.container.spoils_bag", "Spoils Bag");
		add("forcecraft.container.card", "Item Card");
		add("forcecraft.container.force_engine", "Force Engine");

		//Infuser UI
		add("gui.forcecraft.infuser.nobook", "Upgrade Tome required");
		add("gui.forcecraft.infuser.patchouli", "The info screen requires Patchouli");
		add("gui.forcecraft.infuser.empty.tooltip", "Empty");
		add("gui.forcecraft.infuser.help.tooltip", "Help");
		add("gui.forcecraft.infuser.start.tooltip", "Start Infusion");
		add("gui.forcecraft.infuser.missing.tooltip", "Upgrade Tome or Center Slot empty");
		add("gui.forcecraft.infuser.missing.rf.tooltip", "Need more RF to operate");
		add("gui.forcecraft.infuser.missing.recipe", "Cannot find matching infusion recipe");
		add("gui.forcecraft.infuser.button.guide", "Open Guide GUI");
		add("gui.forcecraft.infuser.button.button", "Start Button");

		//Force Engine UI
		add("gui.forcecraft.force_engine.empty", "Empty");

		//Rename and recolor screen
		add("gui.forcecraft.rename.title", "Rename and Recolor");

		// Interactions
		add("forcecraft.bow.switch_mode", "Changed to %s.");
		add("forcecraft.bow.no_torch", "No torch available.");
		add("forcecraft.bow.mode.normal", "Normal");
		add("forcecraft.bow.mode.torch", "Torch");
		add("forcecraft.bow.mode.ice", "Ice");

		//JEI Compat
		add("forcecraft.gui.jei.category.freezing", "Freezing");
		add("forcecraft.gui.jei.category.grinding", "Grinding");
		add("forcecraft.gui.jei.category.grinding.tooltip", "% Chance");
		add("forcecraft.gui.jei.category.infuser", "Infuser");
		add("forcecraft.gui.jei.category.infuser.tier", "Tier: %s");
	}

	private String getColorPrefix(@Nullable DyeColor dyeColor) {
		String colorPrefix = dyeColor == null ? "" : dyeColor.getName() + " ";
		if (!colorPrefix.isBlank()) {
			colorPrefix = Arrays.stream(colorPrefix.split("_"))
					.map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
					.collect(Collectors.joining(" "));
		}
		return colorPrefix;
	}

	private void addTags() {
		addTag(() -> ForceTags.Blocks.FORCE_LOGS, "Force Logs");
		addTag(() -> ForceTags.Blocks.FORCE_BRICKS, "Force Bricks");
		addTag(() -> ForceTags.Blocks.FORCE_TORCHES, "Force Torches");
		addTag(() -> ForceTags.Blocks.INCORRECT_FOR_FORCE, "Incorrect for Force");
		addTag(() -> ForceTags.Blocks.MINEABLE_WITH_MITTS, "Mineable with Mitts");
		addTag(() -> ForceTags.Blocks.TICKING_NOT_SUPPORTED, "Ticking Not Supported");
		addTag(() -> ForceTags.Blocks.ORES, "Ores");
		addTag(() -> ForceTags.Blocks.ORES_POWER, "Power Ores");

		addTag(() -> ForceTags.Items.FORCE_LOGS, "Force Logs");
		addTag(() -> ForceTags.Items.FORCE_BRICKS, "Force Bricks");
		addTag(() -> ForceTags.Items.FORCE_TORCHES, "Force Torches");
		addTag(() -> ForceTags.Items.CAN_STORE_FORCE, "Can Store Force");
		addTag(() -> ForceTags.Items.FORCE_REPAIR_INGREDIENTS, "Force Repair Ingredients");
		addTag(() -> ForceTags.Items.VALID_INFUSER_MODIFIERS, "Valid Infuser Modifiers");
		addTag(() -> ForceTags.Items.VALID_INFUSER_TOOLS, "Valid Infuser Tools");
		addTag(() -> ForceTags.Items.VALID_INFUSER_CHARGE, "Valid Infuser Charge");
		addTag(() -> ForceTags.Items.VALID_FORCE_BELT, "Valid Force Belt");
		addTag(() -> ForceTags.Items.VALID_FORCE_ITEMS, "Valid Force Items");
		addTag(() -> ForceTags.Items.VALID_DAMAGE_ITEMS, "Valid Damage Items");
		addTag(() -> ForceTags.Items.VALID_SPEED_ITEMS, "Valid Speed Items");
		addTag(() -> ForceTags.Items.VALID_LUMBERJACK_ITEMS, "Valid Lumberjack Items");
		addTag(() -> ForceTags.Items.VALID_RAINBOW_ITEMS, "Valid Rainbow Items");
		addTag(() -> ForceTags.Items.VALID_LUCK_ITEMS, "Valid Luck Items");
		addTag(() -> ForceTags.Items.VALID_GRINDING_ITEMS, "Valid Grinding Items");
		addTag(() -> ForceTags.Items.VALID_FREEZING_ITEMS, "Valid Freezing Items");
		addTag(() -> ForceTags.Items.VALID_EXPERIENCE_ITEMS, "Valid Experience Items");
		addTag(() -> ForceTags.Items.VALID_FALSE_ITEMS, "Valid False Items");
		addTag(() -> ForceTags.Items.VALID_TOUCH_ITEMS, "Valid Touch Items");
		addTag(() -> ForceTags.Items.VALID_BLEED_ITEMS, "Valid Bleed Items");
		addTag(() -> ForceTags.Items.VALID_CRAFT_ITEMS, "Valid Craft Items");
		addTag(() -> ForceTags.Items.VALID_TIME_ITEMS, "Valid Time Items");
		addTag(() -> ForceTags.Items.VALID_HEAT_ITEMS, "Valid Heat Items");
		addTag(() -> ForceTags.Items.VALID_BANE_ITEMS, "Valid Bane Items");
		addTag(() -> ForceTags.Items.VALID_FORGE_ITEMS, "Valid Forge Items");
		addTag(() -> ForceTags.Items.VALID_WING_ITEMS, "Valid Wing Items");
		addTag(() -> ForceTags.Items.VALID_HEALING_ITEMS, "Valid Healing Items");
		addTag(() -> ForceTags.Items.VALID_CAMO_ITEMS, "Valid Camo Items");
		addTag(() -> ForceTags.Items.VALID_SIGHT_ITEMS, "Valid Sight Items");
		addTag(() -> ForceTags.Items.VALID_STURDY_ITEMS, "Valid Sturdy Items");
		addTag(() -> ForceTags.Items.VALID_ENDER_ITEMS, "Valid Ender Items");
		addTag(() -> ForceTags.Items.VALID_SOUL_ITEMS, "Valid Soul Items");
		addTag(() -> ForceTags.Items.VALID_LIGHT_ITEMS, "Valid Light Items");
		addTag(() -> ForceTags.Items.VALID_TREASURE_ITEMS, "Valid Treasure Items");
		addTag(() -> ForceTags.Items.ORES_POWER, "Power Ores");
		addTag(() -> ForceTags.Items.GEMS_FORCE, "Force Gems");
		addTag(() -> ForceTags.Items.INGOTS_FORCE, "Force Ingots");
		addTag(() -> ForceTags.Items.NUGGETS_FORCE, "Force Nuggets");
		addTag(() -> ForceTags.Items.RODS_FORCE, "Force Rods");

		addTag(() -> ForceTags.Fluids.FORCE, "Force");
		addTag(() -> ForceTags.Fluids.FUEL, "Fuel");
		addTag(() -> ForceTags.Fluids.BIOFUEL, "Biofuel");
	}

	/**
	 * Helper method for adding torch keys
	 */
	private void addTorch(ForceTorchReg torchReg, String name) {
		addBlock(torchReg.getTorch(), name);
		addBlock(torchReg.getWallTorch(), name);
	}

	public void addSubtitle(DeferredHolder<SoundEvent, SoundEvent> sound, String name) {
		String path = Reference.MOD_ID + "." + sound.getId().getPath() + ".subtitle";
		this.add(path, name);
	}

	/**
	 * Add the translation for a config entry
	 *
	 * @param path        The path of the config entry
	 * @param name        The name of the config entry
	 * @param description The description of the config entry (optional in case of targeting "title" or similar entries that have no tooltip)
	 */
	private void addConfig(String path, String name, @Nullable String description) {
		this.add(Reference.MOD_ID + ".configuration." + path, name);
		if (description != null && !description.isEmpty())
			this.add(Reference.MOD_ID + ".configuration." + path + ".tooltip", description);
	}
}
