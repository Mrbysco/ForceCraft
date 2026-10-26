package com.mrbysco.forcecraft.datagen.server;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import com.mrbysco.forcecraft.registry.helper.ForceBrickReg;
import com.mrbysco.forcecraft.registry.helper.ForceTorchReg;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ForceItemTagsProvider extends ItemTagsProvider {
	public ForceItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, Reference.MOD_ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		for (ForceBrickReg brickReg : ForceRegistry.getBricks()) {
			this.tag(ItemTags.WALLS).add(brickReg.getWall().asItem());
			this.tag(ItemTags.STAIRS).add(brickReg.getStairs().asItem());
			this.tag(ItemTags.SLABS).add(brickReg.getSlab().asItem());
			this.tag(ForceTags.Items.FORCE_BRICKS).add(brickReg.getBrick().asItem());
		}

		for (ForceTorchReg reg : ForceRegistry.getTorches()) {
			if (!reg.isSpecialTorch()) {
				ForceCraft.LOGGER.info("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA {}", reg.getTorch().getId());
				this.tag(ForceTags.Items.FORCE_TORCHES).add(reg.getTorch().asItem());
			}
		}

		this.tag(ForceTags.Items.FORCE_LOGS).add(ForceRegistry.FORCE_LOG.asItem(), ForceRegistry.FORCE_WOOD.asItem());
		this.tag(ItemTags.LOGS_THAT_BURN).addTag(ForceTags.Items.FORCE_LOGS);
		this.tag(ItemTags.LEAVES).add(ForceRegistry.FORCE_LEAVES.asItem());
		this.tag(ItemTags.SAPLINGS).add(ForceRegistry.FORCE_SAPLING.asItem());
		this.tag(ItemTags.PLANKS).add(ForceRegistry.FORCE_PLANKS.getPlanks().asItem());
		this.tag(ItemTags.WOODEN_BUTTONS).add(ForceRegistry.FORCE_PLANKS.getButton().asItem());
		this.tag(ItemTags.WOODEN_DOORS).add(ForceRegistry.FORCE_PLANKS.getDoor().asItem());
		this.tag(ItemTags.WOODEN_FENCES).add(ForceRegistry.FORCE_PLANKS.getFence().asItem());
		this.tag(ItemTags.WOODEN_PRESSURE_PLATES).add(ForceRegistry.FORCE_PLANKS.getPressurePlate().asItem());
		this.tag(ItemTags.WOODEN_SLABS).add(ForceRegistry.FORCE_PLANKS.getSlab().asItem());
		this.tag(ItemTags.WOODEN_STAIRS).add(ForceRegistry.FORCE_PLANKS.getStairs().asItem());
		this.tag(ItemTags.WOODEN_TRAPDOORS).add(ForceRegistry.FORCE_PLANKS.getTrapdoor().asItem());
		this.tag(ItemTags.SIGNS).add(ForceRegistry.FORCE_PLANKS.getSign().asItem());
		this.tag(ItemTags.HANGING_SIGNS).add(ForceRegistry.FORCE_PLANKS.getHangingSign().asItem());
		this.tag(ItemTags.WOODEN_SHELVES).add(ForceRegistry.FORCE_PLANKS.getShelf().asItem());

		this.tag(Tags.Items.ORES_IN_GROUND_STONE).add(ForceRegistry.POWER_ORE.asItem());
		this.tag(Tags.Items.ORES_IN_GROUND_DEEPSLATE).add(ForceRegistry.DEEPSLATE_POWER_ORE.asItem());
		this.tag(Tags.Items.ORE_BEARING_GROUND_NETHERRACK).add(ForceRegistry.NETHER_POWER_ORE.asItem());
		this.tag(ForceTags.Items.ORES_POWER).add(ForceRegistry.POWER_ORE.asItem(), ForceRegistry.DEEPSLATE_POWER_ORE.asItem(), ForceRegistry.NETHER_POWER_ORE.asItem());
		this.tag(Tags.Items.ORES).addTag(ForceTags.Items.ORES_POWER);
		this.tag(ForceTags.Items.GEMS_FORCE).add(ForceRegistry.FORCE_GEM.get());
		this.tag(Tags.Items.GEMS).addTag(ForceTags.Items.GEMS_FORCE);
		this.tag(ForceTags.Items.INGOTS_FORCE).add(ForceRegistry.FORCE_INGOT.get());
		this.tag(Tags.Items.INGOTS).addTag(ForceTags.Items.INGOTS_FORCE);
		this.tag(ForceTags.Items.NUGGETS_FORCE).add(ForceRegistry.FORCE_NUGGET.get());
		this.tag(Tags.Items.NUGGETS).addTag(ForceTags.Items.NUGGETS_FORCE);
		this.tag(ForceTags.Items.RODS_FORCE).add(ForceRegistry.FORCE_STICK.get());

		this.tag(ForceTags.Items.CAN_STORE_FORCE).add(
				ForceRegistry.FORCE_ROD.get()
		);

		this.tag(ForceTags.Items.FORCE_REPAIR_INGREDIENTS).add(ForceRegistry.FORCE_GEM.get());
		this.tag(ForceTags.Items.UPGRADE_CORES).add(
				ForceRegistry.GRINDING_CORE.get(),
				ForceRegistry.FROZEN_CORE.get(),
				ForceRegistry.EXPERIENCE_CORE.get(),
				ForceRegistry.HEAT_CORE.get(),
				ForceRegistry.SPEED_CORE.get()
		);
		this.tag(ForceTags.Items.FORCE_FURNACES).add(ForceRegistry.FORCE_FURNACE.asItem());
		ForceRegistry.getFurnaces().forEach(reg -> this.tag(ForceTags.Items.FORCE_FURNACES).add(reg.getFurnace().asItem()));

		this.tag(ForceTags.Items.HOLDS_ITEMS).add(
				ForceRegistry.FORCE_PACK.get()
		);
		this.tag(ForceTags.Items.FORCE_ARMOR).add(
				ForceRegistry.FORCE_BOOTS.get(), ForceRegistry.FORCE_LEGS.get(),
				ForceRegistry.FORCE_CHEST.get(), ForceRegistry.FORCE_HELMET.get()
		);
		this.tag(ItemTags.FOOT_ARMOR).add(ForceRegistry.FORCE_BOOTS.get());
		this.tag(ItemTags.LEG_ARMOR).add(ForceRegistry.FORCE_LEGS.get());
		this.tag(ItemTags.CHEST_ARMOR).add(ForceRegistry.FORCE_CHEST.get());
		this.tag(ItemTags.HEAD_ARMOR).add(ForceRegistry.FORCE_HELMET.get());

		this.tag(ItemTags.PICKAXES).add(ForceRegistry.FORCE_PICKAXE.get());
		this.tag(ItemTags.SHOVELS).add(ForceRegistry.FORCE_SHOVEL.get());
		this.tag(ItemTags.AXES).add(ForceRegistry.FORCE_AXE.get());
		this.tag(ItemTags.SWORDS).add(ForceRegistry.FORCE_SWORD.get());
		this.tag(Tags.Items.TOOLS_BOW).add(ForceRegistry.FORCE_BOW.get());
		this.tag(Tags.Items.TOOLS_SHEAR).add(ForceRegistry.FORCE_SHEARS.get());

		// Infuser tags
		this.tag(ForceTags.Items.CAN_STORE_FORCE).add(
				ForceRegistry.FORCE_SWORD.get(), ForceRegistry.FORCE_BOW.get(), ForceRegistry.FORCE_SHEARS.get(),
				ForceRegistry.FORCE_PICKAXE.get(), ForceRegistry.FORCE_SHOVEL.get(), ForceRegistry.FORCE_AXE.get()
		).addTag(ForceTags.Items.FORCE_ARMOR);

		this.tag(ForceTags.Items.VALID_INFUSER_CHARGE).addTag(ForceTags.Items.CAN_STORE_FORCE);
		this.tag(ForceTags.Items.VALID_FORCE_BELT).add(Items.POTION, Items.MILK_BUCKET); //TODO: Add Red potion, Force Filled Flask and Milk Force Flask

		this.tag(ForceTags.Items.VALID_FORCE_ITEMS).add(
				ForceRegistry.FORCE_SWORD.get(), ForceRegistry.FORCE_PICKAXE.get(),
				ForceRegistry.FORCE_SHOVEL.get(), ForceRegistry.FORCE_AXE.get());

		this.tag(ForceTags.Items.VALID_DAMAGE_ITEMS).add(
				ForceRegistry.FORCE_SWORD.get(), ForceRegistry.FORCE_BOW.get()
		).addTag(ForceTags.Items.FORCE_ARMOR);

		this.tag(ForceTags.Items.VALID_SPEED_ITEMS)
				.add(
						ForceRegistry.FORCE_PICKAXE.get(), ForceRegistry.FORCE_SHOVEL.get(), ForceRegistry.FORCE_AXE.get(),
						ForceRegistry.FORCE_ROD.get())
				.addTag(ForceTags.Items.FORCE_ARMOR);

		this.tag(ForceTags.Items.VALID_LUMBERJACK_ITEMS).add(ForceRegistry.FORCE_AXE.get());

		this.tag(ForceTags.Items.VALID_RAINBOW_ITEMS).add(ForceRegistry.FORCE_SHEARS.get());

		this.tag(ForceTags.Items.VALID_LUCK_ITEMS).add(
				ForceRegistry.FORCE_SWORD.get(),
				ForceRegistry.FORCE_PICKAXE.get(), ForceRegistry.FORCE_SHOVEL.get(), ForceRegistry.FORCE_AXE.get());

		this.tag(ForceTags.Items.VALID_GRINDING_ITEMS).add(
				ForceRegistry.FORCE_PICKAXE.get(), ForceRegistry.FORCE_SHOVEL.get(), ForceRegistry.FORCE_AXE.get());

		this.tag(ForceTags.Items.VALID_FREEZING_ITEMS).add(ForceRegistry.FORCE_BOW.get(), ForceRegistry.FORCE_SWORD.get());

		this.tag(ForceTags.Items.VALID_EXPERIENCE_ITEMS).add(net.minecraft.world.item.Items.BOOK);

		this.tag(ForceTags.Items.VALID_FALSE_ITEMS).add(ForceRegistry.FORCE_SWORD.get());

		this.tag(ForceTags.Items.VALID_TOUCH_ITEMS).add(
				ForceRegistry.FORCE_PICKAXE.get(), ForceRegistry.FORCE_SHOVEL.get(), ForceRegistry.FORCE_AXE.get());

		this.tag(ForceTags.Items.VALID_BLEED_ITEMS).add(
				ForceRegistry.FORCE_SWORD.get(), ForceRegistry.FORCE_BOW.get());

//		this.tag(ForceTags.Items.VALID_CRAFT_ITEMS).add(ForceRegistry.ITEM_CARD.get());

		this.tag(ForceTags.Items.VALID_TIME_ITEMS)
				.add(ForceRegistry.FORCE_SWORD.get(), ForceRegistry.FORCE_ROD.get());

		this.tag(ForceTags.Items.VALID_INFUSE_TORCHES)
				.addTag(ForceTags.Items.FORCE_TORCHES);

		this.tag(ForceTags.Items.VALID_HEAT_ITEMS)
				.add(
						ForceRegistry.FORCE_SWORD.get(), ForceRegistry.FORCE_BOW.get(),
						ForceRegistry.FORCE_PICKAXE.get(), ForceRegistry.FORCE_SHOVEL.get(), ForceRegistry.FORCE_AXE.get(),
						ForceRegistry.FORCE_ROD.get() // Item_card?
				)
				.addTag(ForceTags.Items.FORCE_ARMOR);

		this.tag(ForceTags.Items.VALID_BANE_ITEMS).add(ForceRegistry.FORCE_SWORD.get());

//		this.tag(ForceTags.Items.VALID_FORGE_ITEMS).add(ForceRegistry.ITEM_CARD.get());

		this.tag(ForceTags.Items.VALID_WING_ITEMS)
				.add(ForceRegistry.FORCE_SWORD.get())
				.addTag(ForceTags.Items.FORCE_ARMOR);

		this.tag(ForceTags.Items.VALID_HEALING_ITEMS).add(
				ForceRegistry.FORCE_SWORD.get(), ForceRegistry.FORCE_BOW.get(), ForceRegistry.FORCE_ROD.get());

		this.tag(ForceTags.Items.VALID_CAMO_ITEMS)
				.add(ForceRegistry.FORCE_ROD.get())
				.addTag(ForceTags.Items.FORCE_ARMOR);

		this.tag(ForceTags.Items.VALID_SIGHT_ITEMS).add(ForceRegistry.FORCE_ROD.get());

		this.tag(ForceTags.Items.VALID_STURDY_ITEMS)
				.add(
						ForceRegistry.FORCE_PICKAXE.get(), ForceRegistry.FORCE_SHOVEL.get(), ForceRegistry.FORCE_AXE.get(),
						ForceRegistry.FORCE_BOW.get())
				.addTag(ForceTags.Items.FORCE_ARMOR);

		this.tag(ForceTags.Items.VALID_ENDER_ITEMS).add(
				ForceRegistry.FORCE_SWORD.get(), ForceRegistry.FORCE_ROD.get());

		this.tag(ForceTags.Items.VALID_SOUL_ITEMS).add(ForceRegistry.FORCE_SWORD.get());

		this.tag(ForceTags.Items.VALID_LIGHT_ITEMS).add(ForceRegistry.FORCE_SWORD.get());

		this.tag(ForceTags.Items.VALID_TREASURE_ITEMS).add(
				ForceRegistry.FORCE_SWORD.get(), ForceRegistry.FORCE_BOW.get());

		this.tag(ForceTags.Items.LIMITED_STURDY_ITEMS)
				.addTag(ForceTags.Items.FORCE_ARMOR);

		this.tag(ForceTags.Items.LIMITED_DAMAGE_ITEMS)
				.addTag(ForceTags.Items.FORCE_ARMOR);

		this.tag(ForceTags.Items.FORCE_TOOLS).add(
				ForceRegistry.FORCE_PICKAXE.get(),
				ForceRegistry.FORCE_AXE.get(),
				ForceRegistry.FORCE_SHOVEL.get(),
				ForceRegistry.FORCE_SHEARS.get()
		);
		this.tag(ForceTags.Items.FORCE_WEAPONS).add(
				ForceRegistry.FORCE_SWORD.get(),
				ForceRegistry.FORCE_MITT.get(),
				ForceRegistry.FORCE_BOW.get()
		);
	}
}
