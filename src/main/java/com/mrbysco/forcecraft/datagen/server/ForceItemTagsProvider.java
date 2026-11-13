package com.mrbysco.forcecraft.datagen.server;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import com.mrbysco.forcecraft.registry.helper.ForceBrickReg;
import com.mrbysco.forcecraft.registry.helper.ForceTorchReg;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockItemTags;
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
			this.tag(BlockItemTags.WALLS.item()).add(brickReg.getWall().asItem().builtInRegistryHolder().key());
			this.tag(BlockItemTags.STAIRS.item()).add(brickReg.getStairs().asItem().builtInRegistryHolder().key());
			this.tag(BlockItemTags.SLABS.item()).add(brickReg.getSlab().asItem().builtInRegistryHolder().key());
			this.tag(ForceTags.Items.FORCE_BRICKS).add(brickReg.getBrick().asItem().builtInRegistryHolder().key());
		}

		for (ForceTorchReg reg : ForceRegistry.getTorches()) {
			if (!reg.isSpecialTorch()) {
				this.tag(ForceTags.Items.FORCE_TORCHES).add(reg.getTorch().asItem().builtInRegistryHolder().key());
			}
		}

		this.tag(ForceTags.Items.FORCE_LOGS).add(ForceRegistry.FORCE_LOG_ITEM.key(), ForceRegistry.FORCE_WOOD_ITEM.key());
		this.tag(BlockItemTags.LOGS_THAT_BURN.item()).addTag(ForceTags.Items.FORCE_LOGS);
		this.tag(ItemTags.LEAVES).add(ForceRegistry.FORCE_LEAVES_ITEM.key());
		this.tag(ItemTags.SAPLINGS).add(ForceRegistry.FORCE_SAPLING_ITEM.key());
		this.tag(ItemTags.PLANKS).add(ForceRegistry.FORCE_PLANKS.getPlanks().asItem().builtInRegistryHolder().key());
		this.tag(ItemTags.WOODEN_BUTTONS).add(ForceRegistry.FORCE_PLANKS.getButton().asItem().builtInRegistryHolder().key());
		this.tag(ItemTags.WOODEN_DOORS).add(ForceRegistry.FORCE_PLANKS.getDoor().asItem().builtInRegistryHolder().key());
		this.tag(ItemTags.WOODEN_FENCES).add(ForceRegistry.FORCE_PLANKS.getFence().asItem().builtInRegistryHolder().key());
		this.tag(ItemTags.WOODEN_PRESSURE_PLATES).add(ForceRegistry.FORCE_PLANKS.getPressurePlate().asItem().builtInRegistryHolder().key());
		this.tag(ItemTags.WOODEN_SLABS).add(ForceRegistry.FORCE_PLANKS.getSlab().asItem().builtInRegistryHolder().key());
		this.tag(ItemTags.WOODEN_STAIRS).add(ForceRegistry.FORCE_PLANKS.getStairs().asItem().builtInRegistryHolder().key());
		this.tag(ItemTags.WOODEN_TRAPDOORS).add(ForceRegistry.FORCE_PLANKS.getTrapdoor().asItem().builtInRegistryHolder().key());
		this.tag(ItemTags.SIGNS).add(ForceRegistry.FORCE_PLANKS.getSign().asItem().builtInRegistryHolder().key());
		this.tag(ItemTags.HANGING_SIGNS).add(ForceRegistry.FORCE_PLANKS.getHangingSign().asItem().builtInRegistryHolder().key());
		this.tag(ItemTags.WOODEN_SHELVES).add(ForceRegistry.FORCE_PLANKS.getShelf().asItem().builtInRegistryHolder().key());

		this.tag(Tags.BlockItems.ORES_IN_GROUND_STONE.item()).add(ForceRegistry.POWER_ORE_ITEM.key());
		this.tag(Tags.BlockItems.ORES_IN_GROUND_DEEPSLATE.item()).add(ForceRegistry.DEEPSLATE_POWER_ORE_ITEM.key());
		this.tag(Tags.BlockItems.ORE_BEARING_GROUND_NETHERRACK.item()).add(ForceRegistry.NETHER_POWER_ORE_ITEM.key());
		this.tag(ForceTags.Items.ORES_POWER).add(
				ForceRegistry.POWER_ORE_ITEM.key(),
				ForceRegistry.DEEPSLATE_POWER_ORE_ITEM.key(),
				ForceRegistry.NETHER_POWER_ORE_ITEM.key()
		);
		this.tag(Tags.BlockItems.ORES.item()).addTag(ForceTags.Items.ORES_POWER);
		this.tag(ForceTags.Items.GEMS_FORCE).add(ForceRegistry.FORCE_GEM.key());
		this.tag(Tags.Items.GEMS).addTag(ForceTags.Items.GEMS_FORCE);
		this.tag(ForceTags.Items.INGOTS_FORCE).add(ForceRegistry.FORCE_INGOT.key());
		this.tag(Tags.Items.INGOTS).addTag(ForceTags.Items.INGOTS_FORCE);
		this.tag(ForceTags.Items.NUGGETS_FORCE).add(ForceRegistry.FORCE_NUGGET.key());
		this.tag(Tags.Items.NUGGETS).addTag(ForceTags.Items.NUGGETS_FORCE);
		this.tag(ForceTags.Items.RODS_FORCE).add(ForceRegistry.FORCE_STICK.key());

		this.tag(ItemTags.MEAT).add(ForceRegistry.RAW_BACON.key(), ForceRegistry.COOKED_BACON.key());
		this.tag(Tags.Items.FOODS_RAW_MEAT).add(ForceRegistry.RAW_BACON.key());
		this.tag(Tags.Items.FOODS_COOKED_MEAT).add(ForceRegistry.COOKED_BACON.key());

		this.tag(ForceTags.Items.CAN_STORE_FORCE).add(
				ForceRegistry.FORCE_ROD.key()
		);

		this.tag(ForceTags.Items.FORCE_REPAIR_INGREDIENTS).add(ForceRegistry.FORCE_GEM.key());
		this.tag(ForceTags.Items.UPGRADE_CORES).add(
				ForceRegistry.GRINDING_CORE.key(),
				ForceRegistry.FROZEN_CORE.key(),
				ForceRegistry.EXPERIENCE_CORE.key(),
				ForceRegistry.HEAT_CORE.key(),
				ForceRegistry.SPEED_CORE.key()
		);
		this.tag(ForceTags.Items.FORCE_FURNACES).add(ForceRegistry.FORCE_FURNACE.asItem().builtInRegistryHolder().key());
		ForceRegistry.getFurnaces().forEach(reg -> this.tag(ForceTags.Items.FORCE_FURNACES).add(reg.getFurnace().asItem().builtInRegistryHolder().key()));

		this.tag(ForceTags.Items.HOLDS_ITEMS).add(
				ForceRegistry.FORCE_PACK.key()
		);
		this.tag(ForceTags.Items.FORCE_ARMOR).add(
				ForceRegistry.FORCE_BOOTS.key(), ForceRegistry.FORCE_LEGS.key(),
				ForceRegistry.FORCE_CHEST.key(), ForceRegistry.FORCE_HELMET.key()
		);
		this.tag(ItemTags.FOOT_ARMOR).add(ForceRegistry.FORCE_BOOTS.key());
		this.tag(ItemTags.LEG_ARMOR).add(ForceRegistry.FORCE_LEGS.key());
		this.tag(ItemTags.CHEST_ARMOR).add(ForceRegistry.FORCE_CHEST.key());
		this.tag(ItemTags.HEAD_ARMOR).add(ForceRegistry.FORCE_HELMET.key());

		this.tag(ItemTags.PICKAXES).add(ForceRegistry.FORCE_PICKAXE.key());
		this.tag(ItemTags.SHOVELS).add(ForceRegistry.FORCE_SHOVEL.key());
		this.tag(ItemTags.AXES).add(ForceRegistry.FORCE_AXE.key());
		this.tag(ItemTags.SWORDS).add(ForceRegistry.FORCE_SWORD.key());
		this.tag(ItemTags.BOW_ENCHANTABLE).add(ForceRegistry.FORCE_BOW.key());
		this.tag(ItemTags.DURABILITY_ENCHANTABLE).add(
				ForceRegistry.FORCE_ROD.key(), ForceRegistry.FORCE_BOW.key(),
				ForceRegistry.FORCE_SHEARS.key(), ForceRegistry.FORCE_MITT.key()
		);
		this.tag(ItemTags.MELEE_WEAPON_ENCHANTABLE).add(ForceRegistry.FORCE_MITT.key());
		this.tag(ItemTags.MINING_ENCHANTABLE).add(ForceRegistry.FORCE_SHEARS.key(), ForceRegistry.FORCE_MITT.key());
		this.tag(ItemTags.MINING_LOOT_ENCHANTABLE).add(ForceRegistry.FORCE_MITT.key());
		this.tag(Tags.Items.TOOLS_BOW).add(ForceRegistry.FORCE_BOW.key());
		this.tag(Tags.Items.TOOLS_SHEAR).add(ForceRegistry.FORCE_SHEARS.key());

		// Infuser tags
		this.tag(ForceTags.Items.CAN_STORE_FORCE).add(
				ForceRegistry.FORCE_SWORD.key(), ForceRegistry.FORCE_BOW.key(), ForceRegistry.FORCE_SHEARS.key(),
				ForceRegistry.FORCE_PICKAXE.key(), ForceRegistry.FORCE_SHOVEL.key(), ForceRegistry.FORCE_AXE.key()
		).addTag(ForceTags.Items.FORCE_ARMOR);

		this.tag(ForceTags.Items.VALID_INFUSER_CHARGE).addTag(ForceTags.Items.CAN_STORE_FORCE);
		this.tag(ForceTags.Items.VALID_FORCE_BELT).add(Items.POTION.builtInRegistryHolder().key(), Items.MILK_BUCKET.builtInRegistryHolder().key()); //TODO: Add Red potion, Force Filled Flask and Milk Force Flasklask

		this.tag(ForceTags.Items.VALID_FORCE_ITEMS).add(
				ForceRegistry.FORCE_SWORD.key(), ForceRegistry.FORCE_PICKAXE.key(),
				ForceRegistry.FORCE_SHOVEL.key(), ForceRegistry.FORCE_AXE.key());

		this.tag(ForceTags.Items.VALID_DAMAGE_ITEMS).add(
				ForceRegistry.FORCE_SWORD.key(), ForceRegistry.FORCE_BOW.key()
		).addTag(ForceTags.Items.FORCE_ARMOR);

		this.tag(ForceTags.Items.VALID_SPEED_ITEMS)
				.add(
						ForceRegistry.FORCE_PICKAXE.key(), ForceRegistry.FORCE_SHOVEL.key(), ForceRegistry.FORCE_AXE.key(),
						ForceRegistry.FORCE_ROD.key())
				.addTag(ForceTags.Items.FORCE_ARMOR);

		this.tag(ForceTags.Items.VALID_LUMBERJACK_ITEMS).add(ForceRegistry.FORCE_AXE.key());

		this.tag(ForceTags.Items.VALID_RAINBOW_ITEMS).add(ForceRegistry.FORCE_SHEARS.key());

		this.tag(ForceTags.Items.VALID_LUCK_ITEMS).add(
				ForceRegistry.FORCE_SWORD.key(),
				ForceRegistry.FORCE_PICKAXE.key(), ForceRegistry.FORCE_SHOVEL.key(), ForceRegistry.FORCE_AXE.key());

		this.tag(ForceTags.Items.VALID_GRINDING_ITEMS).add(
				ForceRegistry.FORCE_PICKAXE.key(), ForceRegistry.FORCE_SHOVEL.key(), ForceRegistry.FORCE_AXE.key());

		this.tag(ForceTags.Items.VALID_FREEZING_ITEMS).add(ForceRegistry.FORCE_BOW.key(), ForceRegistry.FORCE_SWORD.key());

		this.tag(ForceTags.Items.VALID_EXPERIENCE_ITEMS).add(Items.BOOK.builtInRegistryHolder().key());

		this.tag(ForceTags.Items.VALID_FALSE_ITEMS).add(ForceRegistry.FORCE_SWORD.key());

		this.tag(ForceTags.Items.VALID_TOUCH_ITEMS).add(
				ForceRegistry.FORCE_PICKAXE.key(), ForceRegistry.FORCE_SHOVEL.key(), ForceRegistry.FORCE_AXE.key());

		this.tag(ForceTags.Items.VALID_BLEED_ITEMS).add(
				ForceRegistry.FORCE_SWORD.key(), ForceRegistry.FORCE_BOW.key());

//		this.tag(ForceTags.Items.VALID_CRAFT_ITEMS).add(ForceRegistry.ITEM_CARD.key());

		this.tag(ForceTags.Items.VALID_TIME_ITEMS)
				.add(ForceRegistry.FORCE_SWORD.key());

		this.tag(ForceTags.Items.VALID_INFUSE_TORCHES)
				.addTag(ForceTags.Items.FORCE_TORCHES);

		this.tag(ForceTags.Items.VALID_HEAT_ITEMS)
				.add(
						ForceRegistry.FORCE_SWORD.key(), ForceRegistry.FORCE_BOW.key(),
						ForceRegistry.FORCE_PICKAXE.key(), ForceRegistry.FORCE_SHOVEL.key(), ForceRegistry.FORCE_AXE.key(),
						ForceRegistry.FORCE_ROD.key() // Item_card?
				)
				.addTag(ForceTags.Items.FORCE_ARMOR);

		this.tag(ForceTags.Items.VALID_BANE_ITEMS).add(ForceRegistry.FORCE_SWORD.key());

//		this.tag(ForceTags.Items.VALID_FORGE_ITEMS).add(ForceRegistry.ITEM_CARD.key());

		this.tag(ForceTags.Items.VALID_WING_ITEMS)
				.add(ForceRegistry.FORCE_SWORD.key())
				.addTag(ForceTags.Items.FORCE_ARMOR);

		this.tag(ForceTags.Items.VALID_HEALING_ITEMS).add(
				ForceRegistry.FORCE_SWORD.key(), ForceRegistry.FORCE_BOW.key(), ForceRegistry.FORCE_ROD.key());

		this.tag(ForceTags.Items.VALID_CAMO_ITEMS)
				.add(ForceRegistry.FORCE_ROD.key())
				.addTag(ForceTags.Items.FORCE_ARMOR);

		this.tag(ForceTags.Items.VALID_SIGHT_ITEMS).add(ForceRegistry.FORCE_ROD.key());

		this.tag(ForceTags.Items.VALID_STURDY_ITEMS)
				.add(
						ForceRegistry.FORCE_PICKAXE.key(), ForceRegistry.FORCE_SHOVEL.key(), ForceRegistry.FORCE_AXE.key()
				)
				.addTag(ForceTags.Items.FORCE_ARMOR);

		this.tag(ForceTags.Items.VALID_ENDER_ITEMS).add(
				ForceRegistry.FORCE_SWORD.key(), ForceRegistry.FORCE_BOW.key(), ForceRegistry.FORCE_ROD.key());

		this.tag(ForceTags.Items.VALID_SOUL_ITEMS).add(ForceRegistry.FORCE_SWORD.key());

		this.tag(ForceTags.Items.VALID_LIGHT_ITEMS).add(ForceRegistry.FORCE_SWORD.key());

		this.tag(ForceTags.Items.VALID_TREASURE_ITEMS).add(
				ForceRegistry.FORCE_SWORD.key(), ForceRegistry.FORCE_BOW.key());

		this.tag(ForceTags.Items.LIMITED_STURDY_ITEMS)
				.addTag(ForceTags.Items.FORCE_ARMOR);

		this.tag(ForceTags.Items.LIMITED_DAMAGE_ITEMS)
				.addTag(ForceTags.Items.FORCE_ARMOR);

		this.tag(ForceTags.Items.FORCE_TOOLS).add(
				ForceRegistry.FORCE_PICKAXE.key(),
				ForceRegistry.FORCE_AXE.key(),
				ForceRegistry.FORCE_SHOVEL.key(),
				ForceRegistry.FORCE_SHEARS.key()
		);
		this.tag(ForceTags.Items.FORCE_WEAPONS).add(
				ForceRegistry.FORCE_SWORD.key(),
				ForceRegistry.FORCE_MITT.key(),
				ForceRegistry.FORCE_BOW.key()
		);
		ForceRegistry.getMobChunks().forEach((entry, item) -> this.tag(ForceTags.Items.MOB_CHUNKS).add(item.key()));
	}
}
