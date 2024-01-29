package com.mrbysco.forcecraft.datagen.data.tags;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.mrbysco.forcecraft.registry.ForceRegistry.DEEPSLATE_POWER_ORE_ITEM;
import static com.mrbysco.forcecraft.registry.ForceRegistry.POWER_ORE_ITEM;

public class ForceItemTagProvider extends ItemTagsProvider {
	public ForceItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
								TagsProvider<Block> blockTagProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, blockTagProvider.contentsGetter(), Reference.MOD_ID, existingFileHelper);
	}

	public static final TagKey<Item> ORES_IN_GROUND_DEEPSLATE = forgeTag("ores_in_ground/deepslate");
	public static final TagKey<Item> ORES_IN_GROUND_STONE = forgeTag("ores_in_ground/stone");
	public static final TagKey<Item> ORES = forgeTag("ores");
	public static final TagKey<Item> ORES_POWER = forgeTag("ores/power");

	private static TagKey<Item> forgeTag(String name) {
		return ItemTags.create(new ResourceLocation("forge", name));
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(ORES_IN_GROUND_DEEPSLATE).add(DEEPSLATE_POWER_ORE_ITEM.get());
		this.tag(ORES_IN_GROUND_STONE).add(POWER_ORE_ITEM.get());
		this.tag(ORES_POWER).add(POWER_ORE_ITEM.get(), DEEPSLATE_POWER_ORE_ITEM.get());
		this.tag(ORES).addTag(ORES_POWER);
		this.tag(ForceTags.FORCE_LOGS).add(ForceRegistry.FORCE_LOG_ITEM.get(), ForceRegistry.FORCE_WOOD_ITEM.get());
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
	}
}