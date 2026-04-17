package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.blockentities.ForceEngineBlockEntity;
import com.mrbysco.forcecraft.blockentities.InfuserBlockEntity;
import com.mrbysco.forcecraft.blockentities.TimeTorchBlockEntity;
import com.mrbysco.forcecraft.blockentities.furnace.ForceFurnaceBlockEntity;
import com.mrbysco.forcecraft.blocks.ForceFluidBlock;
import com.mrbysco.forcecraft.blocks.ForceFurnaceBlock;
import com.mrbysco.forcecraft.blocks.engine.ForceEngineBlock;
import com.mrbysco.forcecraft.blocks.flammable.FlammableBlock;
import com.mrbysco.forcecraft.blocks.flammable.FlammableSlab;
import com.mrbysco.forcecraft.blocks.flammable.FlammableStairs;
import com.mrbysco.forcecraft.blocks.flammable.ForceLeavesBlock;
import com.mrbysco.forcecraft.blocks.flammable.ForceLogBlock;
import com.mrbysco.forcecraft.blocks.infuser.InfuserBlock;
import com.mrbysco.forcecraft.blocks.torch.TimeTorchBlock;
import com.mrbysco.forcecraft.blocks.torch.WallTimeTorchBlock;
import com.mrbysco.forcecraft.blocks.tree.ForceTreeGrower;
import com.mrbysco.forcecraft.items.BaconatorItem;
import com.mrbysco.forcecraft.items.BaseItem;
import com.mrbysco.forcecraft.items.BottledWitherItem;
import com.mrbysco.forcecraft.items.CustomFoodItem;
import com.mrbysco.forcecraft.items.ExperienceTomeItem;
import com.mrbysco.forcecraft.items.ForceArmorItem;
import com.mrbysco.forcecraft.items.ForceBeltItem;
import com.mrbysco.forcecraft.items.ForceFluidBucketItem;
import com.mrbysco.forcecraft.items.ForcePackItem;
import com.mrbysco.forcecraft.items.FortuneItem;
import com.mrbysco.forcecraft.items.GoldenPowerSourceItem;
import com.mrbysco.forcecraft.items.ItemCardItem;
import com.mrbysco.forcecraft.items.SpoilsBagItem;
import com.mrbysco.forcecraft.items.UpgradeCoreItem;
import com.mrbysco.forcecraft.items.flask.EntityFlaskItem;
import com.mrbysco.forcecraft.items.flask.ForceFilledForceFlask;
import com.mrbysco.forcecraft.items.flask.ForceFlaskItem;
import com.mrbysco.forcecraft.items.flask.MilkFlaskItem;
import com.mrbysco.forcecraft.items.flask.RedPotionItem;
import com.mrbysco.forcecraft.items.heart.RecoveryHeartItem;
import com.mrbysco.forcecraft.items.infuser.UpgradeTomeItem;
import com.mrbysco.forcecraft.items.nonburnable.InertCoreItem;
import com.mrbysco.forcecraft.items.tools.ForceArrowItem;
import com.mrbysco.forcecraft.items.tools.ForceAxeItem;
import com.mrbysco.forcecraft.items.tools.ForceBowItem;
import com.mrbysco.forcecraft.items.tools.ForceMittItem;
import com.mrbysco.forcecraft.items.tools.ForcePickaxeItem;
import com.mrbysco.forcecraft.items.tools.ForceRodItem;
import com.mrbysco.forcecraft.items.tools.ForceShearsItem;
import com.mrbysco.forcecraft.items.tools.ForceShovelItem;
import com.mrbysco.forcecraft.items.tools.ForceSwordItem;
import com.mrbysco.forcecraft.items.tools.ForceWrenchItem;
import com.mrbysco.forcecraft.items.tools.MagnetGloveItem;
import com.mrbysco.forcecraft.registry.material.ModArmor;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class ForceRegistry {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MOD_ID);
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MOD_ID);
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Reference.MOD_ID);

	/**
	 * Blocks
	 */
	public static final DeferredBlock<DropExperienceBlock> POWER_ORE = BLOCKS.register("power_ore", () ->
			new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
	public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_POWER_ORE = BLOCKS.register("deepslate_power_ore", () ->
			new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).requiresCorrectToolForDrops().strength(4.5F, 3.0F)));
	public static final DeferredBlock<SaplingBlock> FORCE_SAPLING = BLOCKS.register("force_sapling", () ->
			new SaplingBlock(ForceTreeGrower.FORCE, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollision().randomTicks().instabreak().sound(SoundType.GRASS)));
	public static final DeferredBlock<ForceLogBlock> FORCE_LOG = BLOCKS.register("force_log", () ->
			new ForceLogBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASS)
					.strength(1.0F).sound(SoundType.WOOD).lightLevel((state) -> 2)));
	public static final DeferredBlock<ForceLogBlock> FORCE_WOOD = BLOCKS.register("force_wood", () ->
			new ForceLogBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS)
					.strength(1.0F).sound(SoundType.WOOD).lightLevel((state) -> 1)));
	public static final DeferredBlock<FlammableBlock> FORCE_PLANKS = BLOCKS.register("force_planks", () ->
			new FlammableBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD), 5, 20));
	public static final DeferredBlock<FlammableStairs> FORCE_PLANK_STAIRS = BLOCKS.register("force_plank_stairs", () ->
			new FlammableStairs(FORCE_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FORCE_PLANKS.get()), 5, 20));
	public static final DeferredBlock<FlammableSlab> FORCE_PLANK_SLAB = BLOCKS.register("force_plank_slab",
			(properties) -> new FlammableSlab(BlockBehaviour.Properties.ofFullCopy(FORCE_PLANKS.get()), 5, 20));


	public static final DeferredBlock<ForceLeavesBlock> FORCE_LEAVES = BLOCKS.register("force_leaves", () ->
			new ForceLeavesBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).strength(0.3f).pushReaction(PushReaction.DESTROY).randomTicks().sound(SoundType.GRASS).noOcclusion()
					.isValidSpawn(Blocks::ocelotOrParrot).isSuffocating((state, level, pos) -> false).isViewBlocking((state, level, pos) -> false)));

	public static final DeferredBlock<InfuserBlock> INFUSER = BLOCKS.register("infuser", () ->
			new InfuserBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(2.0F)));

	public static final DeferredBlock<ForceFluidBlock> FORCE_FLUID_BLOCK = BLOCKS.register("force_fluid", () ->
			new ForceFluidBlock(ForceFluids.FORCE_FLUID_SOURCE.get(), BlockBehaviour.Properties.of().mapColor(MapColor.WATER).instrument(NoteBlockInstrument.BASEDRUM).noCollision().lightLevel(state -> 15).noLootTable()));

	public static final DeferredBlock<ForceFurnaceBlock> FORCE_FURNACE = BLOCKS.register("force_furnace", (properties) -> new ForceFurnaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final DeferredBlock<ForceFurnaceBlock> BLACK_FORCE_FURNACE = BLOCKS.register("black_force_furnace", (properties) -> new ForceFurnaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final DeferredBlock<ForceFurnaceBlock> BLUE_FORCE_FURNACE = BLOCKS.register("blue_force_furnace", (properties) -> new ForceFurnaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final DeferredBlock<ForceFurnaceBlock> BROWN_FORCE_FURNACE = BLOCKS.register("brown_force_furnace", (properties) -> new ForceFurnaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final DeferredBlock<ForceFurnaceBlock> CYAN_FORCE_FURNACE = BLOCKS.register("cyan_force_furnace", (properties) -> new ForceFurnaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final DeferredBlock<ForceFurnaceBlock> GRAY_FORCE_FURNACE = BLOCKS.register("gray_force_furnace", (properties) -> new ForceFurnaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final DeferredBlock<ForceFurnaceBlock> GREEN_FORCE_FURNACE = BLOCKS.register("green_force_furnace", (properties) -> new ForceFurnaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final DeferredBlock<ForceFurnaceBlock> LIGHT_BLUE_FORCE_FURNACE = BLOCKS.register("light_blue_force_furnace", (properties) -> new ForceFurnaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final DeferredBlock<ForceFurnaceBlock> LIGHT_GRAY_FORCE_FURNACE = BLOCKS.register("light_gray_force_furnace", (properties) -> new ForceFurnaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final DeferredBlock<ForceFurnaceBlock> LIME_FORCE_FURNACE = BLOCKS.register("lime_force_furnace", (properties) -> new ForceFurnaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final DeferredBlock<ForceFurnaceBlock> MAGENTA_FORCE_FURNACE = BLOCKS.register("magenta_force_furnace", (properties) -> new ForceFurnaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final DeferredBlock<ForceFurnaceBlock> ORANGE_FORCE_FURNACE = BLOCKS.register("orange_force_furnace", (properties) -> new ForceFurnaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final DeferredBlock<ForceFurnaceBlock> PINK_FORCE_FURNACE = BLOCKS.register("pink_force_furnace", (properties) -> new ForceFurnaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final DeferredBlock<ForceFurnaceBlock> PURPLE_FORCE_FURNACE = BLOCKS.register("purple_force_furnace", (properties) -> new ForceFurnaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final DeferredBlock<ForceFurnaceBlock> RED_FORCE_FURNACE = BLOCKS.register("red_force_furnace", (properties) -> new ForceFurnaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final DeferredBlock<ForceFurnaceBlock> WHITE_FORCE_FURNACE = BLOCKS.register("white_force_furnace", (properties) -> new ForceFurnaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final DeferredBlock<ForceEngineBlock> FORCE_ENGINE = BLOCKS.register("force_engine", () ->
			new ForceEngineBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(4.0F, 18.0F)
					.lightLevel(ForceEngineBlock.getLightValueActive(8))));

	//Bricks
	public static final DeferredBlock<Block> FORCE_BRICK_RED = BLOCKS.registerSimpleBlock("force_brick_red", () -> BlockBehaviour.Properties.of().mapColor(DyeColor.RED).strength(2.0F, 12.0F).requiresCorrectToolForDrops());
	public static final DeferredBlock<Block> FORCE_BRICK_YELLOW = BLOCKS.registerSimpleBlock("force_brick_yellow", () -> BlockBehaviour.Properties.of().mapColor(DyeColor.YELLOW).strength(2.0F, 12.0F).requiresCorrectToolForDrops());
	public static final DeferredBlock<Block> FORCE_BRICK_GREEN = BLOCKS.registerSimpleBlock("force_brick_green", () -> BlockBehaviour.Properties.of().mapColor(DyeColor.GREEN).strength(2.0F, 12.0F).requiresCorrectToolForDrops());
	public static final DeferredBlock<Block> FORCE_BRICK_BLUE = BLOCKS.registerSimpleBlock("force_brick_blue", () -> BlockBehaviour.Properties.of().mapColor(DyeColor.BLUE).strength(2.0F, 12.0F).requiresCorrectToolForDrops());
	public static final DeferredBlock<Block> FORCE_BRICK_WHITE = BLOCKS.registerSimpleBlock("force_brick_white", () -> BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE).strength(2.0F, 12.0F).requiresCorrectToolForDrops());
	public static final DeferredBlock<Block> FORCE_BRICK_BLACK = BLOCKS.registerSimpleBlock("force_brick_black", () -> BlockBehaviour.Properties.of().mapColor(DyeColor.BLACK).strength(2.0F, 12.0F).requiresCorrectToolForDrops());
	public static final DeferredBlock<Block> FORCE_BRICK_BROWN = BLOCKS.registerSimpleBlock("force_brick_brown", () -> BlockBehaviour.Properties.of().mapColor(DyeColor.BROWN).strength(2.0F, 12.0F).requiresCorrectToolForDrops());
	public static final DeferredBlock<Block> FORCE_BRICK_ORANGE = BLOCKS.registerSimpleBlock("force_brick_orange", () -> BlockBehaviour.Properties.of().mapColor(DyeColor.ORANGE).strength(2.0F, 12.0F).requiresCorrectToolForDrops());
	public static final DeferredBlock<Block> FORCE_BRICK_LIGHT_BLUE = BLOCKS.registerSimpleBlock("force_brick_light_blue", () -> BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_BLUE).strength(2.0F, 12.0F).requiresCorrectToolForDrops());
	public static final DeferredBlock<Block> FORCE_BRICK_MAGENTA = BLOCKS.registerSimpleBlock("force_brick_magenta", () -> BlockBehaviour.Properties.of().mapColor(DyeColor.MAGENTA).strength(2.0F, 12.0F).requiresCorrectToolForDrops());
	public static final DeferredBlock<Block> FORCE_BRICK_PINK = BLOCKS.registerSimpleBlock("force_brick_pink", () -> BlockBehaviour.Properties.of().mapColor(DyeColor.PINK).strength(2.0F, 12.0F).requiresCorrectToolForDrops());
	public static final DeferredBlock<Block> FORCE_BRICK_LIGHT_GRAY = BLOCKS.registerSimpleBlock("force_brick_light_gray", () -> BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_GRAY).strength(2.0F, 12.0F).requiresCorrectToolForDrops());
	public static final DeferredBlock<Block> FORCE_BRICK_LIME = BLOCKS.registerSimpleBlock("force_brick_lime", () -> BlockBehaviour.Properties.of().mapColor(DyeColor.LIME).strength(2.0F, 12.0F).requiresCorrectToolForDrops());
	public static final DeferredBlock<Block> FORCE_BRICK_CYAN = BLOCKS.registerSimpleBlock("force_brick_cyan", () -> BlockBehaviour.Properties.of().mapColor(DyeColor.CYAN).strength(2.0F, 12.0F).requiresCorrectToolForDrops());
	public static final DeferredBlock<Block> FORCE_BRICK_PURPLE = BLOCKS.registerSimpleBlock("force_brick_purple", () -> BlockBehaviour.Properties.of().mapColor(DyeColor.PURPLE).strength(2.0F, 12.0F).requiresCorrectToolForDrops());
	public static final DeferredBlock<Block> FORCE_BRICK_GRAY = BLOCKS.registerSimpleBlock("force_brick_gray", () -> BlockBehaviour.Properties.of().mapColor(DyeColor.GRAY).strength(2.0F, 12.0F).requiresCorrectToolForDrops());
	public static final DeferredBlock<Block> FORCE_BRICK = BLOCKS.registerSimpleBlock("force_brick", () -> BlockBehaviour.Properties.of().mapColor(DyeColor.YELLOW).strength(2.0F, 12.0F).requiresCorrectToolForDrops());
	//Stairs
	public static final DeferredBlock<StairBlock> FORCE_BRICK_RED_STAIRS = BLOCKS.register("force_brick_red_stairs", (properties) -> new StairBlock(FORCE_BRICK_RED.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_RED.get())));
	public static final DeferredBlock<StairBlock> FORCE_BRICK_YELLOW_STAIRS = BLOCKS.register("force_brick_yellow_stairs", (properties) -> new StairBlock(FORCE_BRICK_YELLOW.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_YELLOW.get())));
	public static final DeferredBlock<StairBlock> FORCE_BRICK_GREEN_STAIRS = BLOCKS.register("force_brick_green_stairs", (properties) -> new StairBlock(FORCE_BRICK_GREEN.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_GREEN.get())));
	public static final DeferredBlock<StairBlock> FORCE_BRICK_BLUE_STAIRS = BLOCKS.register("force_brick_blue_stairs", (properties) -> new StairBlock(FORCE_BRICK_BLUE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_BLUE.get())));
	public static final DeferredBlock<StairBlock> FORCE_BRICK_WHITE_STAIRS = BLOCKS.register("force_brick_white_stairs", (properties) -> new StairBlock(FORCE_BRICK_WHITE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_WHITE.get())));
	public static final DeferredBlock<StairBlock> FORCE_BRICK_BLACK_STAIRS = BLOCKS.register("force_brick_black_stairs", (properties) -> new StairBlock(FORCE_BRICK_BLACK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_BLACK.get())));
	public static final DeferredBlock<StairBlock> FORCE_BRICK_BROWN_STAIRS = BLOCKS.register("force_brick_brown_stairs", (properties) -> new StairBlock(FORCE_BRICK_BROWN.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_BROWN.get())));
	public static final DeferredBlock<StairBlock> FORCE_BRICK_ORANGE_STAIRS = BLOCKS.register("force_brick_orange_stairs", (properties) -> new StairBlock(FORCE_BRICK_ORANGE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_ORANGE.get())));
	public static final DeferredBlock<StairBlock> FORCE_BRICK_LIGHT_BLUE_STAIRS = BLOCKS.register("force_brick_light_blue_stairs", (properties) -> new StairBlock(FORCE_BRICK_LIGHT_BLUE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_LIGHT_BLUE.get())));
	public static final DeferredBlock<StairBlock> FORCE_BRICK_MAGENTA_STAIRS = BLOCKS.register("force_brick_magenta_stairs", (properties) -> new StairBlock(FORCE_BRICK_MAGENTA.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_MAGENTA.get())));
	public static final DeferredBlock<StairBlock> FORCE_BRICK_PINK_STAIRS = BLOCKS.register("force_brick_pink_stairs", (properties) -> new StairBlock(FORCE_BRICK_PINK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_PINK.get())));
	public static final DeferredBlock<StairBlock> FORCE_BRICK_LIGHT_GRAY_STAIRS = BLOCKS.register("force_brick_light_gray_stairs", (properties) -> new StairBlock(FORCE_BRICK_LIGHT_GRAY.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_LIGHT_GRAY.get())));
	public static final DeferredBlock<StairBlock> FORCE_BRICK_LIME_STAIRS = BLOCKS.register("force_brick_lime_stairs", (properties) -> new StairBlock(FORCE_BRICK_LIME.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_LIME.get())));
	public static final DeferredBlock<StairBlock> FORCE_BRICK_CYAN_STAIRS = BLOCKS.register("force_brick_cyan_stairs", (properties) -> new StairBlock(FORCE_BRICK_CYAN.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_CYAN.get())));
	public static final DeferredBlock<StairBlock> FORCE_BRICK_PURPLE_STAIRS = BLOCKS.register("force_brick_purple_stairs", (properties) -> new StairBlock(FORCE_BRICK_PURPLE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_PURPLE.get())));
	public static final DeferredBlock<StairBlock> FORCE_BRICK_GRAY_STAIRS = BLOCKS.register("force_brick_gray_stairs", (properties) -> new StairBlock(FORCE_BRICK_GRAY.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_GRAY.get())));
	public static final DeferredBlock<StairBlock> FORCE_BRICK_STAIRS = BLOCKS.register("force_brick_stairs", (properties) -> new StairBlock(FORCE_BRICK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK.get())));

	//Slabs
	public static final DeferredBlock<SlabBlock> FORCE_BRICK_RED_SLAB = BLOCKS.register("force_brick_red_slab", (properties) -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_RED.get())));
	public static final DeferredBlock<SlabBlock> FORCE_BRICK_YELLOW_SLAB = BLOCKS.register("force_brick_yellow_slab", (properties) -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_YELLOW.get())));
	public static final DeferredBlock<SlabBlock> FORCE_BRICK_GREEN_SLAB = BLOCKS.register("force_brick_green_slab", (properties) -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_GREEN.get())));
	public static final DeferredBlock<SlabBlock> FORCE_BRICK_BLUE_SLAB = BLOCKS.register("force_brick_blue_slab", (properties) -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_BLUE.get())));
	public static final DeferredBlock<SlabBlock> FORCE_BRICK_WHITE_SLAB = BLOCKS.register("force_brick_white_slab", (properties) -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_WHITE.get())));
	public static final DeferredBlock<SlabBlock> FORCE_BRICK_BLACK_SLAB = BLOCKS.register("force_brick_black_slab", (properties) -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_BLACK.get())));
	public static final DeferredBlock<SlabBlock> FORCE_BRICK_BROWN_SLAB = BLOCKS.register("force_brick_brown_slab", (properties) -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_BROWN.get())));
	public static final DeferredBlock<SlabBlock> FORCE_BRICK_ORANGE_SLAB = BLOCKS.register("force_brick_orange_slab", (properties) -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_ORANGE.get())));
	public static final DeferredBlock<SlabBlock> FORCE_BRICK_LIGHT_BLUE_SLAB = BLOCKS.register("force_brick_light_blue_slab", (properties) -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_LIGHT_BLUE.get())));
	public static final DeferredBlock<SlabBlock> FORCE_BRICK_MAGENTA_SLAB = BLOCKS.register("force_brick_magenta_slab", (properties) -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_MAGENTA.get())));
	public static final DeferredBlock<SlabBlock> FORCE_BRICK_PINK_SLAB = BLOCKS.register("force_brick_pink_slab", (properties) -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_PINK.get())));
	public static final DeferredBlock<SlabBlock> FORCE_BRICK_LIGHT_GRAY_SLAB = BLOCKS.register("force_brick_light_gray_slab", (properties) -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_LIGHT_GRAY.get())));
	public static final DeferredBlock<SlabBlock> FORCE_BRICK_LIME_SLAB = BLOCKS.register("force_brick_lime_slab", (properties) -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_LIME.get())));
	public static final DeferredBlock<SlabBlock> FORCE_BRICK_CYAN_SLAB = BLOCKS.register("force_brick_cyan_slab", (properties) -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_CYAN.get())));
	public static final DeferredBlock<SlabBlock> FORCE_BRICK_PURPLE_SLAB = BLOCKS.register("force_brick_purple_slab", (properties) -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_PURPLE.get())));
	public static final DeferredBlock<SlabBlock> FORCE_BRICK_GRAY_SLAB = BLOCKS.register("force_brick_gray_slab", (properties) -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK_GRAY.get())));
	public static final DeferredBlock<SlabBlock> FORCE_BRICK_SLAB = BLOCKS.register("force_brick_slab", (properties) -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(FORCE_BRICK.get())));

	//Torches
	public static final DeferredBlock<TorchBlock> FORCE_TORCH = BLOCKS.register("force_torch", (properties) -> new TorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.of().mapColor(DyeColor.ORANGE).noCollision().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD)));
	public static final DeferredBlock<TorchBlock> FORCE_RED_TORCH = BLOCKS.register("force_red_torch", (properties) -> new TorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.of().mapColor(DyeColor.RED).noCollision().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD)));
	public static final DeferredBlock<TorchBlock> FORCE_ORANGE_TORCH = BLOCKS.register("force_orange_torch", (properties) -> new TorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.of().mapColor(DyeColor.YELLOW).noCollision().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD)));
	public static final DeferredBlock<TorchBlock> FORCE_GREEN_TORCH = BLOCKS.register("force_green_torch", (properties) -> new TorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.of().mapColor(DyeColor.GREEN).noCollision().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD)));
	public static final DeferredBlock<TorchBlock> FORCE_BLUE_TORCH = BLOCKS.register("force_blue_torch", (properties) -> new TorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.of().mapColor(DyeColor.BLUE).noCollision().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD)));
	public static final DeferredBlock<TorchBlock> FORCE_WHITE_TORCH = BLOCKS.register("force_white_torch", (properties) -> new TorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE).noCollision().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD)));
	public static final DeferredBlock<TorchBlock> FORCE_BLACK_TORCH = BLOCKS.register("force_black_torch", (properties) -> new TorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.of().mapColor(DyeColor.BLACK).noCollision().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD)));
	public static final DeferredBlock<TorchBlock> FORCE_BROWN_TORCH = BLOCKS.register("force_brown_torch", (properties) -> new TorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.of().mapColor(DyeColor.BROWN).noCollision().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD)));
	public static final DeferredBlock<TorchBlock> FORCE_LIGHT_BLUE_TORCH = BLOCKS.register("force_light_blue_torch", (properties) -> new TorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_BLUE).noCollision().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD)));
	public static final DeferredBlock<TorchBlock> FORCE_MAGENTA_TORCH = BLOCKS.register("force_magenta_torch", (properties) -> new TorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.of().mapColor(DyeColor.MAGENTA).noCollision().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD)));
	public static final DeferredBlock<TorchBlock> FORCE_PINK_TORCH = BLOCKS.register("force_pink_torch", (properties) -> new TorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.of().mapColor(DyeColor.PINK).noCollision().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD)));
	public static final DeferredBlock<TorchBlock> FORCE_LIGHT_GRAY_TORCH = BLOCKS.register("force_light_gray_torch", (properties) -> new TorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_GRAY).noCollision().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD)));
	public static final DeferredBlock<TorchBlock> FORCE_LIME_TORCH = BLOCKS.register("force_lime_torch", (properties) -> new TorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.of().mapColor(DyeColor.LIME).noCollision().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD)));
	public static final DeferredBlock<TorchBlock> FORCE_CYAN_TORCH = BLOCKS.register("force_cyan_torch", (properties) -> new TorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.of().mapColor(DyeColor.CYAN).noCollision().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD)));
	public static final DeferredBlock<TorchBlock> FORCE_PURPLE_TORCH = BLOCKS.register("force_purple_torch", (properties) -> new TorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.of().mapColor(DyeColor.PURPLE).noCollision().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD)));
	public static final DeferredBlock<TorchBlock> FORCE_GRAY_TORCH = BLOCKS.register("force_gray_torch", (properties) -> new TorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.of().mapColor(DyeColor.GRAY).noCollision().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD)));

	public static final DeferredBlock<WallTorchBlock> WALL_FORCE_TORCH = BLOCKS.register("wall_force_torch", (properties) -> new WallTorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.ofFullCopy(FORCE_TORCH.get())));
	public static final DeferredBlock<WallTorchBlock> WALL_FORCE_RED_TORCH = BLOCKS.register("wall_force_red_torch", (properties) -> new WallTorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.ofFullCopy(FORCE_RED_TORCH.get())));
	public static final DeferredBlock<WallTorchBlock> WALL_FORCE_ORANGE_TORCH = BLOCKS.register("wall_force_orange_torch", (properties) -> new WallTorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.ofFullCopy(FORCE_ORANGE_TORCH.get())));
	public static final DeferredBlock<WallTorchBlock> WALL_FORCE_GREEN_TORCH = BLOCKS.register("wall_force_green_torch", (properties) -> new WallTorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.ofFullCopy(FORCE_GREEN_TORCH.get())));
	public static final DeferredBlock<WallTorchBlock> WALL_FORCE_BLUE_TORCH = BLOCKS.register("wall_force_blue_torch", (properties) -> new WallTorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.ofFullCopy(FORCE_BLUE_TORCH.get())));
	public static final DeferredBlock<WallTorchBlock> WALL_FORCE_WHITE_TORCH = BLOCKS.register("wall_force_white_torch", (properties) -> new WallTorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.ofFullCopy(FORCE_WHITE_TORCH.get())));
	public static final DeferredBlock<WallTorchBlock> WALL_FORCE_BLACK_TORCH = BLOCKS.register("wall_force_black_torch", (properties) -> new WallTorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.ofFullCopy(FORCE_BLACK_TORCH.get())));
	public static final DeferredBlock<WallTorchBlock> WALL_FORCE_BROWN_TORCH = BLOCKS.register("wall_force_brown_torch", (properties) -> new WallTorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.ofFullCopy(FORCE_BROWN_TORCH.get())));
	public static final DeferredBlock<WallTorchBlock> WALL_FORCE_LIGHT_BLUE_TORCH = BLOCKS.register("wall_force_light_blue_torch", (properties) -> new WallTorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.ofFullCopy(FORCE_LIGHT_BLUE_TORCH.get())));
	public static final DeferredBlock<WallTorchBlock> WALL_FORCE_MAGENTA_TORCH = BLOCKS.register("wall_force_magenta_torch", (properties) -> new WallTorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.ofFullCopy(FORCE_MAGENTA_TORCH.get())));
	public static final DeferredBlock<WallTorchBlock> WALL_FORCE_PINK_TORCH = BLOCKS.register("wall_force_pink_torch", (properties) -> new WallTorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.ofFullCopy(FORCE_PINK_TORCH.get())));
	public static final DeferredBlock<WallTorchBlock> WALL_FORCE_LIGHT_GRAY_TORCH = BLOCKS.register("wall_force_light_gray_torch", (properties) -> new WallTorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.ofFullCopy(FORCE_LIGHT_GRAY_TORCH.get())));
	public static final DeferredBlock<WallTorchBlock> WALL_FORCE_LIME_TORCH = BLOCKS.register("wall_force_lime_torch", (properties) -> new WallTorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.ofFullCopy(FORCE_LIME_TORCH.get())));
	public static final DeferredBlock<WallTorchBlock> WALL_FORCE_CYAN_TORCH = BLOCKS.register("wall_force_cyan_torch", (properties) -> new WallTorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.ofFullCopy(FORCE_CYAN_TORCH.get())));
	public static final DeferredBlock<WallTorchBlock> WALL_FORCE_PURPLE_TORCH = BLOCKS.register("wall_force_purple_torch", (properties) -> new WallTorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.ofFullCopy(FORCE_PURPLE_TORCH.get())));
	public static final DeferredBlock<WallTorchBlock> WALL_FORCE_GRAY_TORCH = BLOCKS.register("wall_force_gray_torch", (properties) -> new WallTorchBlock(ParticleTypes.FLAME, BlockBehaviour.Properties.ofFullCopy(FORCE_GRAY_TORCH.get())));

	public static final DeferredBlock<TimeTorchBlock> TIME_TORCH = BLOCKS.register("time_torch", () ->
			new TimeTorchBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_RED).pushReaction(PushReaction.DESTROY).noCollision().instabreak().lightLevel((state) -> 15)
					.sound(SoundType.WOOD), ParticleTypes.FLAME));
	public static final DeferredBlock<WallTimeTorchBlock> WALL_TIME_TORCH = BLOCKS.register("wall_time_torch", () ->
			new WallTimeTorchBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_RED).pushReaction(PushReaction.DESTROY).noCollision().instabreak().lightLevel((state) -> 15)
					.sound(SoundType.WOOD), ParticleTypes.FLAME));

	/**
	 * Items
	 */
	public static final DeferredItem<RecoveryHeartItem> RECOVERY_HEART = ITEMS.registerItem("recovery_heart", RecoveryHeartItem::new);
	public static final DeferredItem<BaseItem> FORCE_GEM = ITEMS.registerItem("force_gem", BaseItem::new);
	public static final DeferredItem<BaseItem> FORCE_INGOT = ITEMS.registerItem("force_ingot", BaseItem::new);
	public static final DeferredItem<BaseItem> FORCE_NUGGET = ITEMS.registerItem("force_nugget", BaseItem::new);
	public static final DeferredItem<BaseItem> FORCE_STICK = ITEMS.registerItem("force_stick", BaseItem::new);
	public static final DeferredItem<UpgradeTomeItem> UPGRADE_TOME = ITEMS.registerItem("upgrade_tome", UpgradeTomeItem::new);
	public static final DeferredItem<BaseItem> FORCE_PACK_UPGRADE = ITEMS.registerItem("force_pack_upgrade", BaseItem::new);
	public static final DeferredItem<BaseItem> UPGRADE_CORE = ITEMS.registerItem("upgrade_core", BaseItem::new);
	public static final DeferredItem<UpgradeCoreItem> EXPERIENCE_CORE = ITEMS.registerItem("experience_core", UpgradeCoreItem::new);
	public static final DeferredItem<UpgradeCoreItem> FREEZING_CORE = ITEMS.registerItem("freezing_core", UpgradeCoreItem::new);
	public static final DeferredItem<UpgradeCoreItem> GRINDING_CORE = ITEMS.registerItem("grinding_core", UpgradeCoreItem::new);
	public static final DeferredItem<UpgradeCoreItem> HEAT_CORE = ITEMS.registerItem("heat_core", UpgradeCoreItem::new);
	public static final DeferredItem<UpgradeCoreItem> SPEED_CORE = ITEMS.registerItem("speed_core", UpgradeCoreItem::new);
	public static final DeferredItem<BaseItem> RED_CHU_JELLY = ITEMS.registerItem("red_chu_jelly", BaseItem::new);
	public static final DeferredItem<BaseItem> GREEN_CHU_JELLY = ITEMS.registerItem("green_chu_jelly", BaseItem::new);
	public static final DeferredItem<BaseItem> BLUE_CHU_JELLY = ITEMS.registerItem("blue_chu_jelly", BaseItem::new);
	public static final DeferredItem<BaseItem> GOLD_CHU_JELLY = ITEMS.registerItem("gold_chu_jelly", BaseItem::new);
	public static final DeferredItem<BaseItem> PILE_OF_GUNPOWDER = ITEMS.registerItem("pile_of_gunpowder", BaseItem::new);

	public static final DeferredItem<CustomFoodItem> FORTUNE_COOKIE = ITEMS.registerItem("fortune_cookie", (properties) ->
			new CustomFoodItem(properties.food(ForceFoods.FORTUNE_COOKIE).stacksTo(1)));
	public static final DeferredItem<CustomFoodItem> SOUL_WAFER = ITEMS.registerItem("soul_wafer", (properties) ->
			new CustomFoodItem(properties.food(ForceFoods.SOUL_WAFER)));
	public static final DeferredItem<CustomFoodItem> RAW_BACON = ITEMS.registerItem("raw_bacon", (properties) ->
			new CustomFoodItem(properties.food(ForceFoods.BACON)));
	public static final DeferredItem<CustomFoodItem> COOKED_BACON = ITEMS.registerItem("cooked_bacon", (properties) ->
			new CustomFoodItem(properties.food(ForceFoods.COOKED_BACON)));

	public static final DeferredItem<ForceArmorItem> FORCE_HELMET = ITEMS.registerItem("force_helmet", (properties) ->
			new ForceArmorItem(ModArmor.FORCE, ArmorType.HELMET, properties.durability(ArmorType.HELMET.getDurability(31))));
	public static final DeferredItem<ForceArmorItem> FORCE_CHEST = ITEMS.registerItem("force_chest", (properties) ->
			new ForceArmorItem(ModArmor.FORCE, ArmorType.CHESTPLATE, properties.durability(ArmorType.CHESTPLATE.getDurability(31))));
	public static final DeferredItem<ForceArmorItem> FORCE_LEGS = ITEMS.registerItem("force_legs", (properties) ->
			new ForceArmorItem(ModArmor.FORCE, ArmorType.LEGGINGS, properties.durability(ArmorType.LEGGINGS.getDurability(31))));
	public static final DeferredItem<ForceArmorItem> FORCE_BOOTS = ITEMS.registerItem("force_boots", (properties) ->
			new ForceArmorItem(ModArmor.FORCE, ArmorType.BOOTS, properties.durability(ArmorType.BOOTS.getDurability(31))));

	public static final DeferredItem<ForceRodItem> FORCE_ROD = ITEMS.registerItem("force_rod", (properties) -> new ForceRodItem(properties));
	public static final DeferredItem<ForceWrenchItem> FORCE_WRENCH = ITEMS.registerItem("force_wrench", (properties) -> new ForceWrenchItem(properties));
	public static final DeferredItem<GoldenPowerSourceItem> GOLDEN_POWER_SOURCE = ITEMS.registerItem("golden_power_source", (properties) -> new GoldenPowerSourceItem(properties));
	public static final DeferredItem<BaseItem> CLAW = ITEMS.registerItem("claw", BaseItem::new);
	public static final DeferredItem<FortuneItem> FORTUNE = ITEMS.registerItem("fortune", (properties) -> new FortuneItem(properties));
	public static final DeferredItem<BaseItem> FORCE_GEAR = ITEMS.registerItem("force_gear", BaseItem::new);
	public static final DeferredItem<BaseItem> SNOW_COOKIE = ITEMS.registerItem("snow_cookie", BaseItem::new);
	public static final DeferredItem<ForcePackItem> FORCE_PACK = ITEMS.registerItem("force_pack", (properties) -> new ForcePackItem(properties));
	public static final DeferredItem<ForceBeltItem> FORCE_BELT = ITEMS.registerItem("force_belt", (properties) -> new ForceBeltItem(properties));
	public static final DeferredItem<BottledWitherItem> BOTTLED_WITHER = ITEMS.registerItem("bottled_wither", (properties) -> new BottledWitherItem(properties));
	public static final DeferredItem<InertCoreItem> INERT_CORE = ITEMS.registerItem("inert_core", (properties) -> new InertCoreItem(properties));
	public static final DeferredItem<BaconatorItem> BACONATOR = ITEMS.registerItem("baconator", (properties) -> new BaconatorItem(properties));
	public static final DeferredItem<SpoilsBagItem> SPOILS_BAG = ITEMS.registerItem("spoils_bag", (properties) -> new SpoilsBagItem(properties, 1));
	public static final DeferredItem<SpoilsBagItem> SPOILS_BAG_T2 = ITEMS.registerItem("spoils_bag_t2", (properties) -> new SpoilsBagItem(properties, 2));
	public static final DeferredItem<SpoilsBagItem> SPOILS_BAG_T3 = ITEMS.registerItem("spoils_bag_t3", (properties) -> new SpoilsBagItem(properties, 3));
	public static final DeferredItem<BaseItem> LIFE_CARD = ITEMS.registerItem("life_card", BaseItem::new);
	public static final DeferredItem<BaseItem> DARKNESS_CARD = ITEMS.registerItem("darkness_card", BaseItem::new);
	public static final DeferredItem<BaseItem> UNDEATH_CARD = ITEMS.registerItem("undeath_card", BaseItem::new);
	public static final DeferredItem<BaseItem> TREASURE_CORE = ITEMS.registerItem("treasure_core", BaseItem::new);
	public static final DeferredItem<ForceArrowItem> FORCE_ARROW = ITEMS.registerItem("force_arrow", (properties) -> new ForceArrowItem(properties));
	public static final DeferredItem<ForceFlaskItem> FORCE_FLASK = ITEMS.registerItem("force_flask", (properties) -> new ForceFlaskItem(properties));
	public static final DeferredItem<MilkFlaskItem> MILK_FORCE_FLASK = ITEMS.registerItem("milk_force_flask", (properties) -> new MilkFlaskItem(properties.craftRemainder(FORCE_FLASK.get())));
	public static final DeferredItem<ForceFilledForceFlask> FORCE_FILLED_FORCE_FLASK = ITEMS.registerItem("force_filled_force_flask", (properties) -> new ForceFilledForceFlask(properties.craftRemainder(FORCE_FLASK.get())));
	public static final DeferredItem<RedPotionItem> RED_POTION = ITEMS.registerItem("red_potion", (properties) -> new RedPotionItem(properties.craftRemainder(FORCE_FLASK.get())));
	public static final DeferredItem<EntityFlaskItem> ENTITY_FLASK = ITEMS.registerItem("entity_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get())));

	public static final DeferredItem<EntityFlaskItem> BAT_FLASK = ITEMS.registerItem("bat_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.BAT));
	public static final DeferredItem<EntityFlaskItem> BEE_FLASK = ITEMS.registerItem("bee_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.BEE));
	public static final DeferredItem<EntityFlaskItem> CAT_FLASK = ITEMS.registerItem("cat_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.CAT));
	public static final DeferredItem<EntityFlaskItem> CAVE_SPIDER_FLASK = ITEMS.registerItem("cave_spider_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.CAVE_SPIDER));
	public static final DeferredItem<EntityFlaskItem> CHICKEN_FLASK = ITEMS.registerItem("chicken_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.CHICKEN));
	public static final DeferredItem<EntityFlaskItem> COD_FLASK = ITEMS.registerItem("cod_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.COD));
	public static final DeferredItem<EntityFlaskItem> COW_FLASK = ITEMS.registerItem("cow_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.COW));
	public static final DeferredItem<EntityFlaskItem> DOLPHIN_FLASK = ITEMS.registerItem("dolphin_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.DOLPHIN));
	public static final DeferredItem<EntityFlaskItem> DONKEY_FLASK = ITEMS.registerItem("donkey_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.DONKEY));
	public static final DeferredItem<EntityFlaskItem> ENDERMAN_FLASK = ITEMS.registerItem("enderman_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.ENDERMAN));
	public static final DeferredItem<EntityFlaskItem> FOX_FLASK = ITEMS.registerItem("fox_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.FOX));
	public static final DeferredItem<EntityFlaskItem> HORSE_FLASK = ITEMS.registerItem("horse_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.HORSE));
	public static final DeferredItem<EntityFlaskItem> IRON_GOLEM_FLASK = ITEMS.registerItem("iron_golem_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.IRON_GOLEM));
	public static final DeferredItem<EntityFlaskItem> LLAMA_FLASK = ITEMS.registerItem("llama_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.LLAMA));
	public static final DeferredItem<EntityFlaskItem> MOOSHROOM_FLASK = ITEMS.registerItem("mooshroom_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.MOOSHROOM));
	public static final DeferredItem<EntityFlaskItem> MULE_FLASK = ITEMS.registerItem("mule_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.MULE));
	public static final DeferredItem<EntityFlaskItem> PANDA_FLASK = ITEMS.registerItem("panda_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.PANDA));
	public static final DeferredItem<EntityFlaskItem> PARROT_FLASK = ITEMS.registerItem("parrot_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.PARROT));
	public static final DeferredItem<EntityFlaskItem> PIG_FLASK = ITEMS.registerItem("pig_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.PIG));
	public static final DeferredItem<EntityFlaskItem> PIGLIN_FLASK = ITEMS.registerItem("piglin_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.PIGLIN));
	public static final DeferredItem<EntityFlaskItem> POLAR_BEAR_FLASK = ITEMS.registerItem("polar_bear_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.POLAR_BEAR));
	public static final DeferredItem<EntityFlaskItem> PUFFERFISH_FLASK = ITEMS.registerItem("pufferfish_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.PUFFERFISH));
	public static final DeferredItem<EntityFlaskItem> RABBIT_FLASK = ITEMS.registerItem("rabbit_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.RABBIT));
	public static final DeferredItem<EntityFlaskItem> SALMON_FLASK = ITEMS.registerItem("salmon_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.SALMON));
	public static final DeferredItem<EntityFlaskItem> SHEEP_FLASK = ITEMS.registerItem("sheep_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.SHEEP));
	public static final DeferredItem<EntityFlaskItem> SKELETON_FLASK = ITEMS.registerItem("skeleton_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.SKELETON));
	public static final DeferredItem<EntityFlaskItem> SNOW_GOLEM_FLASK = ITEMS.registerItem("snow_golem_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.SNOW_GOLEM));
	public static final DeferredItem<EntityFlaskItem> SPIDER_FLASK = ITEMS.registerItem("spider_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.SPIDER));
	public static final DeferredItem<EntityFlaskItem> SQUID_FLASK = ITEMS.registerItem("squid_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.SQUID));
	public static final DeferredItem<EntityFlaskItem> STRIDER_FLASK = ITEMS.registerItem("strider_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.STRIDER));
	public static final DeferredItem<EntityFlaskItem> TROPICAL_FISH_FLASK = ITEMS.registerItem("tropical_fish_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.TROPICAL_FISH));
	public static final DeferredItem<EntityFlaskItem> TURTLE_FLASK = ITEMS.registerItem("turtle_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.TURTLE));
	public static final DeferredItem<EntityFlaskItem> VILLAGER_FLASK = ITEMS.registerItem("villager_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.VILLAGER));
	public static final DeferredItem<EntityFlaskItem> WANDERING_TRADER_FLASK = ITEMS.registerItem("wandering_trader_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.WANDERING_TRADER));
	public static final DeferredItem<EntityFlaskItem> WOLF_FLASK = ITEMS.registerItem("wolf_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.WOLF));
	public static final DeferredItem<EntityFlaskItem> ZOMBIFIED_PIGLIN_FLASK = ITEMS.registerItem("zombified_piglin_flask", (properties) -> new EntityFlaskItem(properties.craftRemainder(FORCE_FLASK.get()), EntityType.ZOMBIFIED_PIGLIN));

	public static final DeferredItem<ItemCardItem> ITEM_CARD = ITEMS.registerItem("item_card", (properties) -> new ItemCardItem(properties));

	//Tools
	public static final DeferredItem<ForcePickaxeItem> FORCE_PICKAXE = ITEMS.registerItem("force_pickaxe", (properties) -> new ForcePickaxeItem(properties));
	public static final DeferredItem<ForceAxeItem> FORCE_AXE = ITEMS.registerItem("force_axe", (properties) -> new ForceAxeItem(properties));
	public static final DeferredItem<ForceSwordItem> FORCE_SWORD = ITEMS.registerItem("force_sword", (properties) -> new ForceSwordItem(properties));
	public static final DeferredItem<ForceShovelItem> FORCE_SHOVEL = ITEMS.registerItem("force_shovel", (properties) -> new ForceShovelItem(properties));
	public static final DeferredItem<ForceShearsItem> FORCE_SHEARS = ITEMS.registerItem("force_shears", (properties) -> new ForceShearsItem(properties));
	public static final DeferredItem<ForceBowItem> FORCE_BOW = ITEMS.registerItem("force_bow", (properties) -> new ForceBowItem(properties));
	public static final DeferredItem<ForceMittItem> FORCE_MITT = ITEMS.registerItem("force_mitt", (properties) -> new ForceMittItem(properties));
	public static final DeferredItem<MagnetGloveItem> MAGNET_GLOVE = ITEMS.registerItem("magnet_glove", (properties) -> new MagnetGloveItem(properties));

	//Bucket
	public static final DeferredItem<ForceFluidBucketItem> BUCKET_FLUID_FORCE = ITEMS.registerItem("force_bucket", (properties) ->
			new ForceFluidBucketItem(properties.craftRemainder(Items.BUCKET).stacksTo(1), ForceFluids.FORCE_FLUID_SOURCE.get()));

	//Experience Tome
	public static final DeferredItem<ExperienceTomeItem> EXPERIENCE_TOME = ITEMS.registerItem("experience_tome", (properties) -> new ExperienceTomeItem(properties.stacksTo(1)));

	//Spawn eggs
	public static final DeferredItem<SpawnEggItem> RED_CHU_CHU_SPAWN_EGG = ITEMS.registerItem("red_chu_chu_spawn_egg", SpawnEggItem::new, (properties) -> properties.spawnEgg(ForceEntities.RED_CHU_CHU.get()));
	public static final DeferredItem<SpawnEggItem> GREEN_CHU_CHU_SPAWN_EGG = ITEMS.registerItem("green_chu_chu_spawn_egg", SpawnEggItem::new, (properties) -> properties.spawnEgg(ForceEntities.GREEN_CHU_CHU.get()));
	public static final DeferredItem<SpawnEggItem> BLUE_CHU_CHU_SPAWN_EGG = ITEMS.registerItem("blue_chu_chu_spawn_egg", SpawnEggItem::new, (properties) -> properties.spawnEgg(ForceEntities.BLUE_CHU_CHU.get()));
	public static final DeferredItem<SpawnEggItem> GOLD_CHU_CHU_SPAWN_EGG = ITEMS.registerItem("gold_chu_chu_spawn_egg", SpawnEggItem::new, (properties) -> properties.spawnEgg(ForceEntities.GOLD_CHU_CHU.get()));
	public static final DeferredItem<SpawnEggItem> COLD_CHICKEN_SPAWN_EGG = ITEMS.registerItem("cold_chicken_spawn_egg", SpawnEggItem::new, (properties) -> properties.spawnEgg(ForceEntities.COLD_CHICKEN.get()));
	public static final DeferredItem<SpawnEggItem> COLD_COW_SPAWN_EGG = ITEMS.registerItem("cold_cow_spawn_egg", SpawnEggItem::new, (properties) -> properties.spawnEgg(ForceEntities.COLD_COW.get()));
	public static final DeferredItem<SpawnEggItem> COLD_PIG_SPAWN_EGG = ITEMS.registerItem("cold_pig_spawn_egg", SpawnEggItem::new, (properties) -> properties.spawnEgg(ForceEntities.COLD_PIG.get()));
	public static final DeferredItem<SpawnEggItem> FAIRY_SPAWN_EGG = ITEMS.registerItem("fairy_spawn_egg", SpawnEggItem::new, (properties) -> properties.spawnEgg(ForceEntities.FAIRY.get()));
	public static final DeferredItem<SpawnEggItem> CREEPER_TOT_SPAWN_EGG = ITEMS.registerItem("creeper_tot_spawn_egg", SpawnEggItem::new, (properties) -> properties.spawnEgg(ForceEntities.CREEPER_TOT.get()));
	public static final DeferredItem<SpawnEggItem> ENDER_TOT_SPAWN_EGG = ITEMS.registerItem("ender_tot_spawn_egg", SpawnEggItem::new, (properties) -> properties.spawnEgg(ForceEntities.ENDER_TOT.get()));
	public static final DeferredItem<SpawnEggItem> ANGRY_ENDERMAN_SPAWN_EGG = ITEMS.registerItem("angry_enderman_spawn_egg", SpawnEggItem::new, (properties) -> properties.spawnEgg(ForceEntities.ANGRY_ENDERMAN.get()));


	/**
	 * Block Entities
	 */
	public static final Supplier<BlockEntityType<InfuserBlockEntity>> INFUSER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("infuser", () -> new BlockEntityType<>(
			InfuserBlockEntity::new, ForceRegistry.INFUSER.get()));

	public static final Supplier<BlockEntityType<ForceFurnaceBlockEntity>> FURNACE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("furnace", () -> new BlockEntityType<>(
			ForceFurnaceBlockEntity::new, ForceRegistry.FORCE_FURNACE.get(), ForceRegistry.BLACK_FORCE_FURNACE.get(), ForceRegistry.BLUE_FORCE_FURNACE.get(),
			ForceRegistry.BROWN_FORCE_FURNACE.get(), ForceRegistry.CYAN_FORCE_FURNACE.get(), ForceRegistry.GRAY_FORCE_FURNACE.get(), ForceRegistry.GREEN_FORCE_FURNACE.get(),
			ForceRegistry.LIGHT_BLUE_FORCE_FURNACE.get(), ForceRegistry.LIGHT_GRAY_FORCE_FURNACE.get(), ForceRegistry.LIME_FORCE_FURNACE.get(), ForceRegistry.MAGENTA_FORCE_FURNACE.get(),
			ForceRegistry.ORANGE_FORCE_FURNACE.get(), ForceRegistry.PINK_FORCE_FURNACE.get(), ForceRegistry.PURPLE_FORCE_FURNACE.get(), ForceRegistry.RED_FORCE_FURNACE.get(),
			ForceRegistry.WHITE_FORCE_FURNACE.get()));

	public static final Supplier<BlockEntityType<TimeTorchBlockEntity>> TIME_TORCH_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("time_torch", () -> new BlockEntityType<>(
			TimeTorchBlockEntity::new, ForceRegistry.TIME_TORCH.get(), ForceRegistry.WALL_TIME_TORCH.get()));

	public static final Supplier<BlockEntityType<ForceEngineBlockEntity>> FORCE_ENGINE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("force_engine", () -> new BlockEntityType<>(
			ForceEngineBlockEntity::new, ForceRegistry.FORCE_ENGINE.get()));


	/**
	 * Block items
	 */
	public static final DeferredItem<BlockItem> POWER_ORE_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.POWER_ORE);
	public static final DeferredItem<BlockItem> DEEPSLATE_POWER_ORE_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.DEEPSLATE_POWER_ORE);
	public static final DeferredItem<BlockItem> FORCE_SAPLING_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.FORCE_SAPLING);
	public static final DeferredItem<BlockItem> FORCE_LOG_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.FORCE_LOG);
	public static final DeferredItem<BlockItem> FORCE_WOOD_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.FORCE_WOOD);
	public static final DeferredItem<BlockItem> FORCE_LEAVES_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.FORCE_LEAVES);
	public static final DeferredItem<BlockItem> INFUSER_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.INFUSER);
	public static final DeferredItem<BlockItem> FORCE_PLANKS_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.FORCE_PLANKS);
	public static final DeferredItem<BlockItem> FORCE_PLANK_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.FORCE_PLANK_STAIRS);
	public static final DeferredItem<BlockItem> FORCE_PLANK_SLAB_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.FORCE_PLANK_SLAB);
	public static final DeferredItem<BlockItem> FORCE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.FORCE_FURNACE);
	public static final DeferredItem<BlockItem> BLACK_FORCE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.BLACK_FORCE_FURNACE);
	public static final DeferredItem<BlockItem> BLUE_FORCE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.BLUE_FORCE_FURNACE);
	public static final DeferredItem<BlockItem> BROWN_FORCE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.BROWN_FORCE_FURNACE);
	public static final DeferredItem<BlockItem> CYAN_FORCE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.CYAN_FORCE_FURNACE);
	public static final DeferredItem<BlockItem> GRAY_FORCE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.GRAY_FORCE_FURNACE);
	public static final DeferredItem<BlockItem> GREEN_FORCE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.GREEN_FORCE_FURNACE);
	public static final DeferredItem<BlockItem> LIGHT_BLUE_FORCE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.LIGHT_BLUE_FORCE_FURNACE);
	public static final DeferredItem<BlockItem> LIGHT_GRAY_FORCE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.LIGHT_GRAY_FORCE_FURNACE);
	public static final DeferredItem<BlockItem> LIME_FORCE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.LIME_FORCE_FURNACE);
	public static final DeferredItem<BlockItem> MAGENTA_FORCE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.MAGENTA_FORCE_FURNACE);
	public static final DeferredItem<BlockItem> ORANGE_FORCE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.ORANGE_FORCE_FURNACE);
	public static final DeferredItem<BlockItem> PINK_FORCE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.PINK_FORCE_FURNACE);
	public static final DeferredItem<BlockItem> PURPLE_FORCE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.PURPLE_FORCE_FURNACE);
	public static final DeferredItem<BlockItem> RED_FORCE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.RED_FORCE_FURNACE);
	public static final DeferredItem<BlockItem> WHITE_FORCE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.WHITE_FORCE_FURNACE);
	public static final DeferredItem<BlockItem> FORCE_ENGINE_ITEM = ITEMS.registerSimpleBlockItem(ForceRegistry.FORCE_ENGINE);

	public static final DeferredItem<BlockItem> FORCE_BRICK_RED_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_RED);
	public static final DeferredItem<BlockItem> FORCE_BRICK_YELLOW_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_YELLOW);
	public static final DeferredItem<BlockItem> FORCE_BRICK_GREEN_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_GREEN);
	public static final DeferredItem<BlockItem> FORCE_BRICK_BLUE_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_BLUE);
	public static final DeferredItem<BlockItem> FORCE_BRICK_WHITE_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_WHITE);
	public static final DeferredItem<BlockItem> FORCE_BRICK_BLACK_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_BLACK);
	public static final DeferredItem<BlockItem> FORCE_BRICK_BROWN_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_BROWN);
	public static final DeferredItem<BlockItem> FORCE_BRICK_ORANGE_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_ORANGE);
	public static final DeferredItem<BlockItem> FORCE_BRICK_LIGHT_BLUE_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_LIGHT_BLUE);
	public static final DeferredItem<BlockItem> FORCE_BRICK_MAGENTA_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_MAGENTA);
	public static final DeferredItem<BlockItem> FORCE_BRICK_PINK_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_PINK);
	public static final DeferredItem<BlockItem> FORCE_BRICK_LIGHT_GRAY_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_LIGHT_GRAY);
	public static final DeferredItem<BlockItem> FORCE_BRICK_LIME_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_LIME);
	public static final DeferredItem<BlockItem> FORCE_BRICK_CYAN_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_CYAN);
	public static final DeferredItem<BlockItem> FORCE_BRICK_PURPLE_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_PURPLE);
	public static final DeferredItem<BlockItem> FORCE_BRICK_GRAY_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_GRAY);
	public static final DeferredItem<BlockItem> FORCE_BRICK_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK);
	public static final DeferredItem<BlockItem> FORCE_BRICK_RED_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_RED_STAIRS);
	public static final DeferredItem<BlockItem> FORCE_BRICK_YELLOW_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_YELLOW_STAIRS);
	public static final DeferredItem<BlockItem> FORCE_BRICK_GREEN_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_GREEN_STAIRS);
	public static final DeferredItem<BlockItem> FORCE_BRICK_BLUE_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_BLUE_STAIRS);
	public static final DeferredItem<BlockItem> FORCE_BRICK_WHITE_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_WHITE_STAIRS);
	public static final DeferredItem<BlockItem> FORCE_BRICK_BLACK_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_BLACK_STAIRS);
	public static final DeferredItem<BlockItem> FORCE_BRICK_BROWN_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_BROWN_STAIRS);
	public static final DeferredItem<BlockItem> FORCE_BRICK_ORANGE_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_ORANGE_STAIRS);
	public static final DeferredItem<BlockItem> FORCE_BRICK_LIGHT_BLUE_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_LIGHT_BLUE_STAIRS);
	public static final DeferredItem<BlockItem> FORCE_BRICK_MAGENTA_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_MAGENTA_STAIRS);
	public static final DeferredItem<BlockItem> FORCE_BRICK_PINK_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_PINK_STAIRS);
	public static final DeferredItem<BlockItem> FORCE_BRICK_LIGHT_GRAY_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_LIGHT_GRAY_STAIRS);
	public static final DeferredItem<BlockItem> FORCE_BRICK_LIME_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_LIME_STAIRS);
	public static final DeferredItem<BlockItem> FORCE_BRICK_CYAN_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_CYAN_STAIRS);
	public static final DeferredItem<BlockItem> FORCE_BRICK_PURPLE_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_PURPLE_STAIRS);
	public static final DeferredItem<BlockItem> FORCE_BRICK_GRAY_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_GRAY_STAIRS);
	public static final DeferredItem<BlockItem> FORCE_BRICK_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_STAIRS);
	public static final DeferredItem<BlockItem> FORCE_BRICK_RED_SLAB_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_RED_SLAB);
	public static final DeferredItem<BlockItem> FORCE_BRICK_YELLOW_SLAB_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_YELLOW_SLAB);
	public static final DeferredItem<BlockItem> FORCE_BRICK_GREEN_SLAB_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_GREEN_SLAB);
	public static final DeferredItem<BlockItem> FORCE_BRICK_BLUE_SLAB_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_BLUE_SLAB);
	public static final DeferredItem<BlockItem> FORCE_BRICK_WHITE_SLAB_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_WHITE_SLAB);
	public static final DeferredItem<BlockItem> FORCE_BRICK_BLACK_SLAB_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_BLACK_SLAB);
	public static final DeferredItem<BlockItem> FORCE_BRICK_BROWN_SLAB_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_BROWN_SLAB);
	public static final DeferredItem<BlockItem> FORCE_BRICK_ORANGE_SLAB_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_ORANGE_SLAB);
	public static final DeferredItem<BlockItem> FORCE_BRICK_LIGHT_BLUE_SLAB_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_LIGHT_BLUE_SLAB);
	public static final DeferredItem<BlockItem> FORCE_BRICK_MAGENTA_SLAB_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_MAGENTA_SLAB);
	public static final DeferredItem<BlockItem> FORCE_BRICK_PINK_SLAB_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_PINK_SLAB);
	public static final DeferredItem<BlockItem> FORCE_BRICK_LIGHT_GRAY_SLAB_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_LIGHT_GRAY_SLAB);
	public static final DeferredItem<BlockItem> FORCE_BRICK_LIME_SLAB_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_LIME_SLAB);
	public static final DeferredItem<BlockItem> FORCE_BRICK_CYAN_SLAB_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_CYAN_SLAB);
	public static final DeferredItem<BlockItem> FORCE_BRICK_PURPLE_SLAB_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_PURPLE_SLAB);
	public static final DeferredItem<BlockItem> FORCE_BRICK_GRAY_SLAB_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_GRAY_SLAB);
	public static final DeferredItem<BlockItem> FORCE_BRICK_SLAB_ITEM = ITEMS.registerSimpleBlockItem(FORCE_BRICK_SLAB);

	public static final DeferredItem<StandingAndWallBlockItem> FORCE_TORCH_ITEM = ITEMS.registerItem("force_torch", (properties) -> new StandingAndWallBlockItem(FORCE_TORCH.get(), WALL_FORCE_TORCH.get(), Direction.DOWN, properties));
	public static final DeferredItem<StandingAndWallBlockItem> FORCE_RED_TORCH_ITEM = ITEMS.registerItem("force_red_torch", (properties) -> new StandingAndWallBlockItem(FORCE_RED_TORCH.get(), WALL_FORCE_RED_TORCH.get(), Direction.DOWN, properties));
	public static final DeferredItem<StandingAndWallBlockItem> FORCE_ORANGE_TORCH_ITEM = ITEMS.registerItem("force_orange_torch", (properties) -> new StandingAndWallBlockItem(FORCE_ORANGE_TORCH.get(), WALL_FORCE_ORANGE_TORCH.get(), Direction.DOWN, properties));
	public static final DeferredItem<StandingAndWallBlockItem> FORCE_GREEN_TORCH_ITEM = ITEMS.registerItem("force_green_torch", (properties) -> new StandingAndWallBlockItem(FORCE_GREEN_TORCH.get(), WALL_FORCE_GREEN_TORCH.get(), Direction.DOWN, properties));
	public static final DeferredItem<StandingAndWallBlockItem> FORCE_BLUE_TORCH_ITEM = ITEMS.registerItem("force_blue_torch", (properties) -> new StandingAndWallBlockItem(FORCE_BLUE_TORCH.get(), WALL_FORCE_BLUE_TORCH.get(), Direction.DOWN, properties));
	public static final DeferredItem<StandingAndWallBlockItem> FORCE_WHITE_TORCH_ITEM = ITEMS.registerItem("force_white_torch", (properties) -> new StandingAndWallBlockItem(FORCE_WHITE_TORCH.get(), WALL_FORCE_WHITE_TORCH.get(), Direction.DOWN, properties));
	public static final DeferredItem<StandingAndWallBlockItem> FORCE_BLACK_TORCH_ITEM = ITEMS.registerItem("force_black_torch", (properties) -> new StandingAndWallBlockItem(FORCE_BLACK_TORCH.get(), WALL_FORCE_BLACK_TORCH.get(), Direction.DOWN, properties));
	public static final DeferredItem<StandingAndWallBlockItem> FORCE_BROWN_TORCH_ITEM = ITEMS.registerItem("force_brown_torch", (properties) -> new StandingAndWallBlockItem(FORCE_BROWN_TORCH.get(), WALL_FORCE_BROWN_TORCH.get(), Direction.DOWN, properties));
	public static final DeferredItem<StandingAndWallBlockItem> FORCE_LIGHT_BLUE_TORCH_ITEM = ITEMS.registerItem("force_light_blue_torch", (properties) -> new StandingAndWallBlockItem(FORCE_LIGHT_BLUE_TORCH.get(), WALL_FORCE_LIGHT_BLUE_TORCH.get(), Direction.DOWN, properties));
	public static final DeferredItem<StandingAndWallBlockItem> FORCE_MAGENTA_TORCH_ITEM = ITEMS.registerItem("force_magenta_torch", (properties) -> new StandingAndWallBlockItem(FORCE_MAGENTA_TORCH.get(), WALL_FORCE_MAGENTA_TORCH.get(), Direction.DOWN, properties));
	public static final DeferredItem<StandingAndWallBlockItem> FORCE_PINK_TORCH_ITEM = ITEMS.registerItem("force_pink_torch", (properties) -> new StandingAndWallBlockItem(FORCE_PINK_TORCH.get(), WALL_FORCE_PINK_TORCH.get(), Direction.DOWN, properties));
	public static final DeferredItem<StandingAndWallBlockItem> FORCE_LIGHT_GRAY_TORCH_ITEM = ITEMS.registerItem("force_light_gray_torch", (properties) -> new StandingAndWallBlockItem(FORCE_LIGHT_GRAY_TORCH.get(), WALL_FORCE_LIGHT_GRAY_TORCH.get(), Direction.DOWN, properties));
	public static final DeferredItem<StandingAndWallBlockItem> FORCE_LIME_TORCH_ITEM = ITEMS.registerItem("force_lime_torch", (properties) -> new StandingAndWallBlockItem(FORCE_LIME_TORCH.get(), WALL_FORCE_LIME_TORCH.get(), Direction.DOWN, properties));
	public static final DeferredItem<StandingAndWallBlockItem> FORCE_CYAN_TORCH_ITEM = ITEMS.registerItem("force_cyan_torch", (properties) -> new StandingAndWallBlockItem(FORCE_CYAN_TORCH.get(), WALL_FORCE_CYAN_TORCH.get(), Direction.DOWN, properties));
	public static final DeferredItem<StandingAndWallBlockItem> FORCE_PURPLE_TORCH_ITEM = ITEMS.registerItem("force_purple_torch", (properties) -> new StandingAndWallBlockItem(FORCE_PURPLE_TORCH.get(), WALL_FORCE_PURPLE_TORCH.get(), Direction.DOWN, properties));
	public static final DeferredItem<StandingAndWallBlockItem> FORCE_GRAY_TORCH_ITEM = ITEMS.registerItem("force_gray_torch", (properties) -> new StandingAndWallBlockItem(FORCE_GRAY_TORCH.get(), WALL_FORCE_GRAY_TORCH.get(), Direction.DOWN, properties));

	public static final DeferredItem<StandingAndWallBlockItem> TIME_TORCH_ITEM = ITEMS.registerItem("time_torch", (properties) -> new StandingAndWallBlockItem(TIME_TORCH.get(), WALL_TIME_TORCH.get(), Direction.DOWN, properties));

	public static final Supplier<CreativeModeTab> FORCE_TAB = CREATIVE_MODE_TABS.register("tab", (properties) -> CreativeModeTab.builder()
			.icon(() -> new ItemStack(ForceRegistry.FORCE_GEM.get()))
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.title(Component.translatable("itemGroup.forcecraft"))
			.displayItems((features, output) -> {
				List<ItemStack> stacks = ForceRegistry.ITEMS.getEntries().stream().map(reg -> new ItemStack(reg.get())).toList();
				output.acceptAll(stacks);
			}).build());
}
