package com.mrbysco.forcecraft.datagen.data.tags;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

import static com.mrbysco.forcecraft.registry.ForceRegistry.DEEPSLATE_POWER_ORE_ITEM;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BLACK_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BLACK_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BLUE_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BLUE_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BROWN_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BROWN_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_CYAN_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_CYAN_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_GRAY_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_GRAY_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_GREEN_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_GREEN_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIGHT_BLUE_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIGHT_BLUE_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIGHT_GRAY_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIGHT_GRAY_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIME_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIME_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_MAGENTA_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_MAGENTA_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_ORANGE_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_ORANGE_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_PINK_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_PINK_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_PURPLE_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_PURPLE_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_RED_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_RED_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_WHITE_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_WHITE_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_YELLOW_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_YELLOW_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_GEM;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_LEAVES;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_PLANKS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_PLANK_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_SAPLING_ITEM;
import static com.mrbysco.forcecraft.registry.ForceRegistry.POWER_ORE_ITEM;

public class ForceItemTagProvider extends ItemTagsProvider {
	public ForceItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, Reference.MOD_ID);
	}

	public static final TagKey<Item> ORES_IN_GROUND_DEEPSLATE = commonTag("ores_in_ground/deepslate");
	public static final TagKey<Item> ORES_IN_GROUND_STONE = commonTag("ores_in_ground/stone");
	public static final TagKey<Item> ORES = commonTag("ores");
	public static final TagKey<Item> ORES_POWER = commonTag("ores/power");
	public static final TagKey<Item> MEAT = mcTag("meat");

	private static TagKey<Item> commonTag(String name) {
		return ItemTags.create(Identifier.fromNamespaceAndPath("c", name));
	}

	private static TagKey<Item> mcTag(String name) {
		return ItemTags.create(Identifier.withDefaultNamespace(name));
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(ORES_IN_GROUND_DEEPSLATE).add(DEEPSLATE_POWER_ORE_ITEM.get());
		this.tag(ORES_IN_GROUND_STONE).add(POWER_ORE_ITEM.get());
		this.tag(ORES_POWER).add(POWER_ORE_ITEM.get(), DEEPSLATE_POWER_ORE_ITEM.get());
		this.tag(ORES).addTag(ORES_POWER);
		this.tag(ForceTags.FORTUNE).add(ForceRegistry.FORTUNE.get(), ForceRegistry.FORTUNE_COOKIE.get());
		this.tag(ForceTags.BACONATOR_FOOD).add(ForceRegistry.COOKED_BACON.get());
		this.tag(ForceTags.ENDER).add(Items.ENDER_EYE, Items.ENDER_PEARL);
		this.tag(ForceTags.CHU_JELLY).add(
				ForceRegistry.RED_CHU_JELLY.get(),
				ForceRegistry.GREEN_CHU_JELLY.get(),
				ForceRegistry.BLUE_CHU_JELLY.get(),
				ForceRegistry.GOLD_CHU_JELLY.get()
		);
		this.tag(ForceTags.ENTITY_FLASKS).add(
				ForceRegistry.ENTITY_FLASK.get(), ForceRegistry.BAT_FLASK.get(), ForceRegistry.BEE_FLASK.get(),
				ForceRegistry.CAT_FLASK.get(), ForceRegistry.CAVE_SPIDER_FLASK.get(), ForceRegistry.CHICKEN_FLASK.get(),
				ForceRegistry.COD_FLASK.get(), ForceRegistry.COW_FLASK.get(), ForceRegistry.DOLPHIN_FLASK.get(),
				ForceRegistry.DONKEY_FLASK.get(), ForceRegistry.ENDERMAN_FLASK.get(), ForceRegistry.FOX_FLASK.get(),
				ForceRegistry.HORSE_FLASK.get(), ForceRegistry.IRON_GOLEM_FLASK.get(), ForceRegistry.LLAMA_FLASK.get(),
				ForceRegistry.MOOSHROOM_FLASK.get(), ForceRegistry.MULE_FLASK.get(), ForceRegistry.PANDA_FLASK.get(),
				ForceRegistry.PARROT_FLASK.get(), ForceRegistry.PIG_FLASK.get(), ForceRegistry.PIGLIN_FLASK.get(),
				ForceRegistry.POLAR_BEAR_FLASK.get(), ForceRegistry.PUFFERFISH_FLASK.get(), ForceRegistry.RABBIT_FLASK.get(),
				ForceRegistry.SALMON_FLASK.get(), ForceRegistry.SHEEP_FLASK.get(), ForceRegistry.SKELETON_FLASK.get(),
				ForceRegistry.SNOW_GOLEM_FLASK.get(), ForceRegistry.SPIDER_FLASK.get(), ForceRegistry.SQUID_FLASK.get(),
				ForceRegistry.STRIDER_FLASK.get(), ForceRegistry.TROPICAL_FISH_FLASK.get(), ForceRegistry.TURTLE_FLASK.get(),
				ForceRegistry.VILLAGER_FLASK.get(), ForceRegistry.WANDERING_TRADER_FLASK.get(), ForceRegistry.WOLF_FLASK.get(),
				ForceRegistry.ZOMBIFIED_PIGLIN_FLASK.get()
		);

		this.tag(ForceTags.FORCE_FURNACES)
				.add(ForceRegistry.FORCE_FURNACE.asItem(), ForceRegistry.BLACK_FORCE_FURNACE.asItem(),
						ForceRegistry.BLUE_FORCE_FURNACE.asItem(), ForceRegistry.BROWN_FORCE_FURNACE.asItem(),
						ForceRegistry.CYAN_FORCE_FURNACE.asItem(), ForceRegistry.GRAY_FORCE_FURNACE.asItem(),
						ForceRegistry.GREEN_FORCE_FURNACE.asItem(), ForceRegistry.LIGHT_BLUE_FORCE_FURNACE.asItem(),
						ForceRegistry.LIGHT_GRAY_FORCE_FURNACE.asItem(), ForceRegistry.LIME_FORCE_FURNACE.asItem(),
						ForceRegistry.MAGENTA_FORCE_FURNACE.asItem(), ForceRegistry.ORANGE_FORCE_FURNACE.asItem(),
						ForceRegistry.PINK_FORCE_FURNACE.asItem(), ForceRegistry.PURPLE_FORCE_FURNACE.asItem(),
						ForceRegistry.RED_FORCE_FURNACE.asItem(), ForceRegistry.WHITE_FORCE_FURNACE.asItem());

		this.tag(ItemTags.MEAT).add(ForceRegistry.RAW_BACON.get(), ForceRegistry.COOKED_BACON.get());
		this.tag(MEAT).add(ForceRegistry.RAW_BACON.get(), ForceRegistry.COOKED_BACON.get());

		this.tag(ItemTags.FOOT_ARMOR).add(ForceRegistry.FORCE_BOOTS.get());
		this.tag(ItemTags.LEG_ARMOR).add(ForceRegistry.FORCE_LEGS.get());
		this.tag(ItemTags.CHEST_ARMOR).add(ForceRegistry.FORCE_CHEST.get());
		this.tag(ItemTags.HEAD_ARMOR).add(ForceRegistry.FORCE_HELMET.get());

		this.tag(ItemTags.PICKAXES)
				.add(ForceRegistry.FORCE_PICKAXE.get());

		this.tag(ItemTags.SHOVELS)
				.add(ForceRegistry.FORCE_SHOVEL.get());

		this.tag(ItemTags.AXES)
				.add(ForceRegistry.FORCE_AXE.get());

		this.tag(Tags.Items.TOOLS_BOW)
				.add(ForceRegistry.FORCE_BOW.get());

		this.tag(ItemTags.SWORDS)
				.add(ForceRegistry.FORCE_SWORD.get());

		this.tag(Tags.Items.TOOLS_SHEAR)
				.add(ForceRegistry.FORCE_SHEARS.get());

		this.tag(Tags.Items.DRINKS_MILK)
				.add(ForceRegistry.MILK_FORCE_FLASK.get());
		this.tag(Tags.Items.DRINKS_MAGIC)
				.add(ForceRegistry.FORCE_FLASK.get());

		this.tag(ItemTags.WOODEN_STAIRS).add(FORCE_PLANK_STAIRS.asItem());
		this.tag(ItemTags.STAIRS).add(FORCE_BRICK_RED_STAIRS.asItem(), FORCE_BRICK_YELLOW_STAIRS.asItem(),
				FORCE_BRICK_GREEN_STAIRS.asItem(), FORCE_BRICK_BLUE_STAIRS.asItem(), FORCE_BRICK_WHITE_STAIRS.asItem(),
				FORCE_BRICK_BLACK_STAIRS.asItem(), FORCE_BRICK_BROWN_STAIRS.asItem(), FORCE_BRICK_ORANGE_STAIRS.asItem(),
				FORCE_BRICK_LIGHT_BLUE_STAIRS.asItem(), FORCE_BRICK_MAGENTA_STAIRS.asItem(), FORCE_BRICK_PINK_STAIRS.asItem(),
				FORCE_BRICK_LIGHT_GRAY_STAIRS.asItem(), FORCE_BRICK_LIME_STAIRS.asItem(), FORCE_BRICK_CYAN_STAIRS.asItem(),
				FORCE_BRICK_PURPLE_STAIRS.asItem(), FORCE_BRICK_GRAY_STAIRS.asItem(), FORCE_BRICK_STAIRS.asItem());
		this.tag(ItemTags.WOODEN_STAIRS).add(FORCE_PLANK_STAIRS.asItem());
		this.tag(ItemTags.SLABS).add(FORCE_BRICK_RED_SLAB.asItem(), FORCE_BRICK_YELLOW_SLAB.asItem(),
				FORCE_BRICK_GREEN_SLAB.asItem(), FORCE_BRICK_BLUE_SLAB.asItem(), FORCE_BRICK_WHITE_SLAB.asItem(),
				FORCE_BRICK_BLACK_SLAB.asItem(), FORCE_BRICK_BROWN_SLAB.asItem(), FORCE_BRICK_ORANGE_SLAB.asItem(),
				FORCE_BRICK_LIGHT_BLUE_SLAB.asItem(), FORCE_BRICK_MAGENTA_SLAB.asItem(), FORCE_BRICK_PINK_SLAB.asItem(),
				FORCE_BRICK_LIGHT_GRAY_SLAB.asItem(), FORCE_BRICK_LIME_SLAB.asItem(), FORCE_BRICK_CYAN_SLAB.asItem(),
				FORCE_BRICK_PURPLE_SLAB.asItem(), FORCE_BRICK_GRAY_SLAB.asItem(), FORCE_BRICK_SLAB.asItem());
		this.tag(ItemTags.LEAVES).add(FORCE_LEAVES.asItem());
		this.tag(ItemTags.PLANKS).add(FORCE_PLANKS.asItem());

		this.tag(ForceTags.FORCE_LOGS_ITEM).add(ForceRegistry.FORCE_LOG_ITEM.get(), ForceRegistry.FORCE_WOOD_ITEM.get());
		this.tag(ItemTags.LOGS_THAT_BURN).addTag(ForceTags.FORCE_LOGS_ITEM);

		this.tag(ItemTags.SAPLINGS).add(FORCE_SAPLING_ITEM.get());

		this.tag(ForceTags.FORCE_REPAIR_INGREDIENTS).add(FORCE_GEM.get());

	}
}