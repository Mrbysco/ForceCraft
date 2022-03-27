package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.blockentities.ForceEngineBlockEntity;
import com.mrbysco.forcecraft.blockentities.ForceFurnaceBlockEntity;
import com.mrbysco.forcecraft.blockentities.AbstractForceFurnaceBlockEntity;
import com.mrbysco.forcecraft.blockentities.InfuserBlockEntity;
import com.mrbysco.forcecraft.blockentities.TimeTorchBlockEntity;
import com.mrbysco.forcecraft.blocks.ForceFluidBlock;
import com.mrbysco.forcecraft.blocks.ForceFurnaceBlock;
import com.mrbysco.forcecraft.blocks.ForceLeavesBlock;
import com.mrbysco.forcecraft.blocks.ForceLogBlock;
import com.mrbysco.forcecraft.blocks.engine.ForceEngineBlock;
import com.mrbysco.forcecraft.blocks.flammable.FlammableBlock;
import com.mrbysco.forcecraft.blocks.flammable.FlammableSlab;
import com.mrbysco.forcecraft.blocks.flammable.FlammableStairs;
import com.mrbysco.forcecraft.blocks.infuser.InfuserBlock;
import com.mrbysco.forcecraft.blocks.torch.TimeTorchBlock;
import com.mrbysco.forcecraft.blocks.torch.WallTimeTorchBlock;
import com.mrbysco.forcecraft.blocks.tree.ForceTree;
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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForceRegistry {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Reference.MOD_ID);

	/**
	 * Blocks
	 */
	public static final RegistryObject<Block> POWER_ORE = BLOCKS.register("power_ore", () ->
			new OreBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
	public static final RegistryObject<Block> DEEPSLATE_POWER_ORE = BLOCKS.register("deepslate_power_ore", () ->
			new OreBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
	public static final RegistryObject<Block> FORCE_SAPLING = BLOCKS.register("force_sapling", () ->
			new SaplingBlock(new ForceTree(), BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));
	public static final RegistryObject<Block> FORCE_LOG = BLOCKS.register("force_log", () ->
			new ForceLogBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.SAND)
					.strength(1.0F).sound(SoundType.WOOD).lightLevel((state) -> 2)));
	public static final RegistryObject<Block> FORCE_WOOD = BLOCKS.register("force_wood", () ->
			new ForceLogBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(1.0F).sound(SoundType.WOOD).lightLevel((state) -> 1)));
	public static final RegistryObject<Block> FORCE_PLANKS = BLOCKS.register("force_planks", () ->
			new FlammableBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.SAND).strength(2.0F, 3.0F).sound(SoundType.WOOD), 5, 20));
	public static final RegistryObject<Block> FORCE_PLANK_STAIRS = BLOCKS.register("force_plank_stairs", () ->
			new FlammableStairs(() -> FORCE_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(FORCE_PLANKS.get()), 5, 20));
	public static final RegistryObject<Block> FORCE_PLANK_SLAB = BLOCKS.register("force_plank_slab",
			() -> new FlammableSlab(BlockBehaviour.Properties.copy(FORCE_PLANKS.get()), 5, 20));


	public static final RegistryObject<Block> FORCE_LEAVES = BLOCKS.register("force_leaves", () ->
			new ForceLeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.3f).randomTicks().sound(SoundType.GRASS).noOcclusion()
					.isValidSpawn(Blocks::ocelotOrParrot).isSuffocating(Blocks::never).isViewBlocking(Blocks::never)));

	public static final RegistryObject<Block> INFUSER = BLOCKS.register("infuser", () ->
			new InfuserBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(2.0F)));

	public static final RegistryObject<Block> FORCE_FLUID_BLOCK = BLOCKS.register("force_fluid", () ->
			new ForceFluidBlock(ForceFluids.FORCE_FLUID_SOURCE, BlockBehaviour.Properties.of(Material.WATER).noCollission().lightLevel(state -> 15).noDrops()));

	public static final RegistryObject<Block> FORCE_FURNACE = BLOCKS.register("force_furnace", () -> new ForceFurnaceBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final RegistryObject<Block> BLACK_FORCE_FURNACE = BLOCKS.register("black_force_furnace", () -> new ForceFurnaceBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final RegistryObject<Block> BLUE_FORCE_FURNACE = BLOCKS.register("blue_force_furnace", () -> new ForceFurnaceBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final RegistryObject<Block> BROWN_FORCE_FURNACE = BLOCKS.register("brown_force_furnace", () -> new ForceFurnaceBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final RegistryObject<Block> CYAN_FORCE_FURNACE = BLOCKS.register("cyan_force_furnace", () -> new ForceFurnaceBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final RegistryObject<Block> GRAY_FORCE_FURNACE = BLOCKS.register("gray_force_furnace", () -> new ForceFurnaceBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final RegistryObject<Block> GREEN_FORCE_FURNACE = BLOCKS.register("green_force_furnace", () -> new ForceFurnaceBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final RegistryObject<Block> LIGHT_BLUE_FORCE_FURNACE = BLOCKS.register("light_blue_force_furnace", () -> new ForceFurnaceBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final RegistryObject<Block> LIGHT_GRAY_FORCE_FURNACE = BLOCKS.register("light_gray_force_furnace", () -> new ForceFurnaceBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final RegistryObject<Block> LIME_FORCE_FURNACE = BLOCKS.register("lime_force_furnace", () -> new ForceFurnaceBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final RegistryObject<Block> MAGENTA_FORCE_FURNACE = BLOCKS.register("magenta_force_furnace", () -> new ForceFurnaceBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final RegistryObject<Block> ORANGE_FORCE_FURNACE = BLOCKS.register("orange_force_furnace", () -> new ForceFurnaceBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final RegistryObject<Block> PINK_FORCE_FURNACE = BLOCKS.register("pink_force_furnace", () -> new ForceFurnaceBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final RegistryObject<Block> PURPLE_FORCE_FURNACE = BLOCKS.register("purple_force_furnace", () -> new ForceFurnaceBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final RegistryObject<Block> RED_FORCE_FURNACE = BLOCKS.register("red_force_furnace", () -> new ForceFurnaceBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final RegistryObject<Block> WHITE_FORCE_FURNACE = BLOCKS.register("white_force_furnace", () -> new ForceFurnaceBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13))));
	public static final RegistryObject<Block> FORCE_ENGINE = BLOCKS.register("force_engine", () ->
			new ForceEngineBlock(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(4.0F, 18.0F)
					.lightLevel(ForceEngineBlock.getLightValueActive(8))));

	//Bricks
	public static final RegistryObject<Block> FORCE_BRICK_RED = BLOCKS.register("force_brick_red", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, DyeColor.RED).strength(2.0F, 12.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> FORCE_BRICK_YELLOW = BLOCKS.register("force_brick_yellow", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, DyeColor.YELLOW).strength(2.0F, 12.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> FORCE_BRICK_GREEN = BLOCKS.register("force_brick_green", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, DyeColor.GREEN).strength(2.0F, 12.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> FORCE_BRICK_BLUE = BLOCKS.register("force_brick_blue", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, DyeColor.BLUE).strength(2.0F, 12.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> FORCE_BRICK_WHITE = BLOCKS.register("force_brick_white", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, DyeColor.WHITE).strength(2.0F, 12.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> FORCE_BRICK_BLACK = BLOCKS.register("force_brick_black", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, DyeColor.BLACK).strength(2.0F, 12.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> FORCE_BRICK_BROWN = BLOCKS.register("force_brick_brown", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, DyeColor.BROWN).strength(2.0F, 12.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> FORCE_BRICK_ORANGE = BLOCKS.register("force_brick_orange", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, DyeColor.ORANGE).strength(2.0F, 12.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> FORCE_BRICK_LIGHT_BLUE = BLOCKS.register("force_brick_light_blue", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, DyeColor.LIGHT_BLUE).strength(2.0F, 12.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> FORCE_BRICK_MAGENTA = BLOCKS.register("force_brick_magenta", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, DyeColor.MAGENTA).strength(2.0F, 12.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> FORCE_BRICK_PINK = BLOCKS.register("force_brick_pink", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, DyeColor.PINK).strength(2.0F, 12.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> FORCE_BRICK_LIGHT_GRAY = BLOCKS.register("force_brick_light_gray", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, DyeColor.LIGHT_GRAY).strength(2.0F, 12.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> FORCE_BRICK_LIME = BLOCKS.register("force_brick_lime", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, DyeColor.LIME).strength(2.0F, 12.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> FORCE_BRICK_CYAN = BLOCKS.register("force_brick_cyan", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, DyeColor.CYAN).strength(2.0F, 12.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> FORCE_BRICK_PURPLE = BLOCKS.register("force_brick_purple", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, DyeColor.PURPLE).strength(2.0F, 12.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> FORCE_BRICK_GRAY = BLOCKS.register("force_brick_gray", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, DyeColor.GRAY).strength(2.0F, 12.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> FORCE_BRICK = BLOCKS.register("force_brick", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, DyeColor.YELLOW).strength(2.0F, 12.0F).requiresCorrectToolForDrops()));
	//Stairs
	public static final RegistryObject<Block> FORCE_BRICK_RED_STAIRS = BLOCKS.register("force_brick_red_stairs", () -> new StairBlock(() -> FORCE_BRICK_RED.get().defaultBlockState(), BlockBehaviour.Properties.copy(FORCE_BRICK_RED.get())));
	public static final RegistryObject<Block> FORCE_BRICK_YELLOW_STAIRS = BLOCKS.register("force_brick_yellow_stairs", () -> new StairBlock(() -> FORCE_BRICK_YELLOW.get().defaultBlockState(), BlockBehaviour.Properties.copy(FORCE_BRICK_YELLOW.get())));
	public static final RegistryObject<Block> FORCE_BRICK_GREEN_STAIRS = BLOCKS.register("force_brick_green_stairs", () -> new StairBlock(() -> FORCE_BRICK_GREEN.get().defaultBlockState(), BlockBehaviour.Properties.copy(FORCE_BRICK_GREEN.get())));
	public static final RegistryObject<Block> FORCE_BRICK_BLUE_STAIRS = BLOCKS.register("force_brick_blue_stairs", () -> new StairBlock(() -> FORCE_BRICK_BLUE.get().defaultBlockState(), BlockBehaviour.Properties.copy(FORCE_BRICK_BLUE.get())));
	public static final RegistryObject<Block> FORCE_BRICK_WHITE_STAIRS = BLOCKS.register("force_brick_white_stairs", () -> new StairBlock(() -> FORCE_BRICK_WHITE.get().defaultBlockState(), BlockBehaviour.Properties.copy(FORCE_BRICK_WHITE.get())));
	public static final RegistryObject<Block> FORCE_BRICK_BLACK_STAIRS = BLOCKS.register("force_brick_black_stairs", () -> new StairBlock(() -> FORCE_BRICK_BLACK.get().defaultBlockState(), BlockBehaviour.Properties.copy(FORCE_BRICK_BLACK.get())));
	public static final RegistryObject<Block> FORCE_BRICK_BROWN_STAIRS = BLOCKS.register("force_brick_brown_stairs", () -> new StairBlock(() -> FORCE_BRICK_BROWN.get().defaultBlockState(), BlockBehaviour.Properties.copy(FORCE_BRICK_BROWN.get())));
	public static final RegistryObject<Block> FORCE_BRICK_ORANGE_STAIRS = BLOCKS.register("force_brick_orange_stairs", () -> new StairBlock(() -> FORCE_BRICK_ORANGE.get().defaultBlockState(), BlockBehaviour.Properties.copy(FORCE_BRICK_ORANGE.get())));
	public static final RegistryObject<Block> FORCE_BRICK_LIGHT_BLUE_STAIRS = BLOCKS.register("force_brick_light_blue_stairs", () -> new StairBlock(() -> FORCE_BRICK_LIGHT_BLUE.get().defaultBlockState(), BlockBehaviour.Properties.copy(FORCE_BRICK_LIGHT_BLUE.get())));
	public static final RegistryObject<Block> FORCE_BRICK_MAGENTA_STAIRS = BLOCKS.register("force_brick_magenta_stairs", () -> new StairBlock(() -> FORCE_BRICK_MAGENTA.get().defaultBlockState(), BlockBehaviour.Properties.copy(FORCE_BRICK_MAGENTA.get())));
	public static final RegistryObject<Block> FORCE_BRICK_PINK_STAIRS = BLOCKS.register("force_brick_pink_stairs", () -> new StairBlock(() -> FORCE_BRICK_PINK.get().defaultBlockState(), BlockBehaviour.Properties.copy(FORCE_BRICK_PINK.get())));
	public static final RegistryObject<Block> FORCE_BRICK_LIGHT_GRAY_STAIRS = BLOCKS.register("force_brick_light_gray_stairs", () -> new StairBlock(() -> FORCE_BRICK_LIGHT_GRAY.get().defaultBlockState(), BlockBehaviour.Properties.copy(FORCE_BRICK_LIGHT_GRAY.get())));
	public static final RegistryObject<Block> FORCE_BRICK_LIME_STAIRS = BLOCKS.register("force_brick_lime_stairs", () -> new StairBlock(() -> FORCE_BRICK_LIME.get().defaultBlockState(), BlockBehaviour.Properties.copy(FORCE_BRICK_LIME.get())));
	public static final RegistryObject<Block> FORCE_BRICK_CYAN_STAIRS = BLOCKS.register("force_brick_cyan_stairs", () -> new StairBlock(() -> FORCE_BRICK_CYAN.get().defaultBlockState(), BlockBehaviour.Properties.copy(FORCE_BRICK_CYAN.get())));
	public static final RegistryObject<Block> FORCE_BRICK_PURPLE_STAIRS = BLOCKS.register("force_brick_purple_stairs", () -> new StairBlock(() -> FORCE_BRICK_PURPLE.get().defaultBlockState(), BlockBehaviour.Properties.copy(FORCE_BRICK_PURPLE.get())));
	public static final RegistryObject<Block> FORCE_BRICK_GRAY_STAIRS = BLOCKS.register("force_brick_gray_stairs", () -> new StairBlock(() -> FORCE_BRICK_GRAY.get().defaultBlockState(), BlockBehaviour.Properties.copy(FORCE_BRICK_GRAY.get())));
	public static final RegistryObject<Block> FORCE_BRICK_STAIRS = BLOCKS.register("force_brick_stairs", () -> new StairBlock(() -> FORCE_BRICK.get().defaultBlockState(), BlockBehaviour.Properties.copy(FORCE_BRICK.get())));

	//Slabs
	public static final RegistryObject<Block> FORCE_BRICK_RED_SLAB = BLOCKS.register("force_brick_red_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(FORCE_BRICK_RED.get())));
	public static final RegistryObject<Block> FORCE_BRICK_YELLOW_SLAB = BLOCKS.register("force_brick_yellow_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(FORCE_BRICK_YELLOW.get())));
	public static final RegistryObject<Block> FORCE_BRICK_GREEN_SLAB = BLOCKS.register("force_brick_green_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(FORCE_BRICK_GREEN.get())));
	public static final RegistryObject<Block> FORCE_BRICK_BLUE_SLAB = BLOCKS.register("force_brick_blue_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(FORCE_BRICK_BLUE.get())));
	public static final RegistryObject<Block> FORCE_BRICK_WHITE_SLAB = BLOCKS.register("force_brick_white_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(FORCE_BRICK_WHITE.get())));
	public static final RegistryObject<Block> FORCE_BRICK_BLACK_SLAB = BLOCKS.register("force_brick_black_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(FORCE_BRICK_BLACK.get())));
	public static final RegistryObject<Block> FORCE_BRICK_BROWN_SLAB = BLOCKS.register("force_brick_brown_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(FORCE_BRICK_BROWN.get())));
	public static final RegistryObject<Block> FORCE_BRICK_ORANGE_SLAB = BLOCKS.register("force_brick_orange_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(FORCE_BRICK_ORANGE.get())));
	public static final RegistryObject<Block> FORCE_BRICK_LIGHT_BLUE_SLAB = BLOCKS.register("force_brick_light_blue_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(FORCE_BRICK_LIGHT_BLUE.get())));
	public static final RegistryObject<Block> FORCE_BRICK_MAGENTA_SLAB = BLOCKS.register("force_brick_magenta_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(FORCE_BRICK_MAGENTA.get())));
	public static final RegistryObject<Block> FORCE_BRICK_PINK_SLAB = BLOCKS.register("force_brick_pink_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(FORCE_BRICK_PINK.get())));
	public static final RegistryObject<Block> FORCE_BRICK_LIGHT_GRAY_SLAB = BLOCKS.register("force_brick_light_gray_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(FORCE_BRICK_LIGHT_GRAY.get())));
	public static final RegistryObject<Block> FORCE_BRICK_LIME_SLAB = BLOCKS.register("force_brick_lime_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(FORCE_BRICK_LIME.get())));
	public static final RegistryObject<Block> FORCE_BRICK_CYAN_SLAB = BLOCKS.register("force_brick_cyan_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(FORCE_BRICK_CYAN.get())));
	public static final RegistryObject<Block> FORCE_BRICK_PURPLE_SLAB = BLOCKS.register("force_brick_purple_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(FORCE_BRICK_PURPLE.get())));
	public static final RegistryObject<Block> FORCE_BRICK_GRAY_SLAB = BLOCKS.register("force_brick_gray_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(FORCE_BRICK_GRAY.get())));
	public static final RegistryObject<Block> FORCE_BRICK_SLAB = BLOCKS.register("force_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(FORCE_BRICK.get())));

	//Torches
	public static final RegistryObject<Block> FORCE_TORCH = BLOCKS.register("force_torch", () -> new TorchBlock(BlockBehaviour.Properties.of(Material.DECORATION, DyeColor.ORANGE).noCollission().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
	public static final RegistryObject<Block> FORCE_RED_TORCH = BLOCKS.register("force_red_torch", () -> new TorchBlock(BlockBehaviour.Properties.of(Material.DECORATION, DyeColor.RED).noCollission().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
	public static final RegistryObject<Block> FORCE_ORANGE_TORCH = BLOCKS.register("force_orange_torch", () -> new TorchBlock(BlockBehaviour.Properties.of(Material.DECORATION, DyeColor.YELLOW).noCollission().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
	public static final RegistryObject<Block> FORCE_GREEN_TORCH = BLOCKS.register("force_green_torch", () -> new TorchBlock(BlockBehaviour.Properties.of(Material.DECORATION, DyeColor.GREEN).noCollission().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
	public static final RegistryObject<Block> FORCE_BLUE_TORCH = BLOCKS.register("force_blue_torch", () -> new TorchBlock(BlockBehaviour.Properties.of(Material.DECORATION, DyeColor.BLUE).noCollission().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
	public static final RegistryObject<Block> FORCE_WHITE_TORCH = BLOCKS.register("force_white_torch", () -> new TorchBlock(BlockBehaviour.Properties.of(Material.DECORATION, DyeColor.WHITE).noCollission().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
	public static final RegistryObject<Block> FORCE_BLACK_TORCH = BLOCKS.register("force_black_torch", () -> new TorchBlock(BlockBehaviour.Properties.of(Material.DECORATION, DyeColor.BLACK).noCollission().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
	public static final RegistryObject<Block> FORCE_BROWN_TORCH = BLOCKS.register("force_brown_torch", () -> new TorchBlock(BlockBehaviour.Properties.of(Material.DECORATION, DyeColor.BROWN).noCollission().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
	public static final RegistryObject<Block> FORCE_LIGHT_BLUE_TORCH = BLOCKS.register("force_light_blue_torch", () -> new TorchBlock(BlockBehaviour.Properties.of(Material.DECORATION, DyeColor.LIGHT_BLUE).noCollission().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
	public static final RegistryObject<Block> FORCE_MAGENTA_TORCH = BLOCKS.register("force_magenta_torch", () -> new TorchBlock(BlockBehaviour.Properties.of(Material.DECORATION, DyeColor.MAGENTA).noCollission().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
	public static final RegistryObject<Block> FORCE_PINK_TORCH = BLOCKS.register("force_pink_torch", () -> new TorchBlock(BlockBehaviour.Properties.of(Material.DECORATION, DyeColor.PINK).noCollission().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
	public static final RegistryObject<Block> FORCE_LIGHT_GRAY_TORCH = BLOCKS.register("force_light_gray_torch", () -> new TorchBlock(BlockBehaviour.Properties.of(Material.DECORATION, DyeColor.LIGHT_GRAY).noCollission().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
	public static final RegistryObject<Block> FORCE_LIME_TORCH = BLOCKS.register("force_lime_torch", () -> new TorchBlock(BlockBehaviour.Properties.of(Material.DECORATION, DyeColor.LIME).noCollission().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
	public static final RegistryObject<Block> FORCE_CYAN_TORCH = BLOCKS.register("force_cyan_torch", () -> new TorchBlock(BlockBehaviour.Properties.of(Material.DECORATION, DyeColor.CYAN).noCollission().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
	public static final RegistryObject<Block> FORCE_PURPLE_TORCH = BLOCKS.register("force_purple_torch", () -> new TorchBlock(BlockBehaviour.Properties.of(Material.DECORATION, DyeColor.PURPLE).noCollission().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
	public static final RegistryObject<Block> FORCE_GRAY_TORCH = BLOCKS.register("force_gray_torch", () -> new TorchBlock(BlockBehaviour.Properties.of(Material.DECORATION, DyeColor.GRAY).noCollission().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));

	public static final RegistryObject<Block> WALL_FORCE_TORCH = BLOCKS.register("wall_force_torch", () -> new WallTorchBlock(BlockBehaviour.Properties.copy(FORCE_TORCH.get()), ParticleTypes.FLAME));
	public static final RegistryObject<Block> WALL_FORCE_RED_TORCH = BLOCKS.register("wall_force_red_torch", () -> new WallTorchBlock(BlockBehaviour.Properties.copy(FORCE_RED_TORCH.get()), ParticleTypes.FLAME));
	public static final RegistryObject<Block> WALL_FORCE_ORANGE_TORCH = BLOCKS.register("wall_force_orange_torch", () -> new WallTorchBlock(BlockBehaviour.Properties.copy(FORCE_ORANGE_TORCH.get()), ParticleTypes.FLAME));
	public static final RegistryObject<Block> WALL_FORCE_GREEN_TORCH = BLOCKS.register("wall_force_green_torch", () -> new WallTorchBlock(BlockBehaviour.Properties.copy(FORCE_GREEN_TORCH.get()), ParticleTypes.FLAME));
	public static final RegistryObject<Block> WALL_FORCE_BLUE_TORCH = BLOCKS.register("wall_force_blue_torch", () -> new WallTorchBlock(BlockBehaviour.Properties.copy(FORCE_BLUE_TORCH.get()), ParticleTypes.FLAME));
	public static final RegistryObject<Block> WALL_FORCE_WHITE_TORCH = BLOCKS.register("wall_force_white_torch", () -> new WallTorchBlock(BlockBehaviour.Properties.copy(FORCE_WHITE_TORCH.get()), ParticleTypes.FLAME));
	public static final RegistryObject<Block> WALL_FORCE_BLACK_TORCH = BLOCKS.register("wall_force_black_torch", () -> new WallTorchBlock(BlockBehaviour.Properties.copy(FORCE_BLACK_TORCH.get()), ParticleTypes.FLAME));
	public static final RegistryObject<Block> WALL_FORCE_BROWN_TORCH = BLOCKS.register("wall_force_brown_torch", () -> new WallTorchBlock(BlockBehaviour.Properties.copy(FORCE_BROWN_TORCH.get()), ParticleTypes.FLAME));
	public static final RegistryObject<Block> WALL_FORCE_LIGHT_BLUE_TORCH = BLOCKS.register("wall_force_light_blue_torch", () -> new WallTorchBlock(BlockBehaviour.Properties.copy(FORCE_LIGHT_BLUE_TORCH.get()), ParticleTypes.FLAME));
	public static final RegistryObject<Block> WALL_FORCE_MAGENTA_TORCH = BLOCKS.register("wall_force_magenta_torch", () -> new WallTorchBlock(BlockBehaviour.Properties.copy(FORCE_MAGENTA_TORCH.get()), ParticleTypes.FLAME));
	public static final RegistryObject<Block> WALL_FORCE_PINK_TORCH = BLOCKS.register("wall_force_pink_torch", () -> new WallTorchBlock(BlockBehaviour.Properties.copy(FORCE_PINK_TORCH.get()), ParticleTypes.FLAME));
	public static final RegistryObject<Block> WALL_FORCE_LIGHT_GRAY_TORCH = BLOCKS.register("wall_force_light_gray_torch", () -> new WallTorchBlock(BlockBehaviour.Properties.copy(FORCE_LIGHT_GRAY_TORCH.get()), ParticleTypes.FLAME));
	public static final RegistryObject<Block> WALL_FORCE_LIME_TORCH = BLOCKS.register("wall_force_lime_torch", () -> new WallTorchBlock(BlockBehaviour.Properties.copy(FORCE_LIME_TORCH.get()), ParticleTypes.FLAME));
	public static final RegistryObject<Block> WALL_FORCE_CYAN_TORCH = BLOCKS.register("wall_force_cyan_torch", () -> new WallTorchBlock(BlockBehaviour.Properties.copy(FORCE_CYAN_TORCH.get()), ParticleTypes.FLAME));
	public static final RegistryObject<Block> WALL_FORCE_PURPLE_TORCH = BLOCKS.register("wall_force_purple_torch", () -> new WallTorchBlock(BlockBehaviour.Properties.copy(FORCE_PURPLE_TORCH.get()), ParticleTypes.FLAME));
	public static final RegistryObject<Block> WALL_FORCE_GRAY_TORCH = BLOCKS.register("wall_force_gray_torch", () -> new WallTorchBlock(BlockBehaviour.Properties.copy(FORCE_GRAY_TORCH.get()), ParticleTypes.FLAME));

	public static final RegistryObject<Block> TIME_TORCH = BLOCKS.register("time_torch", () ->
			new TimeTorchBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().instabreak().lightLevel((state) -> 15)
					.sound(SoundType.WOOD), ParticleTypes.FLAME));
	public static final RegistryObject<Block> WALL_TIME_TORCH = BLOCKS.register("wall_time_torch", () ->
			new WallTimeTorchBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().instabreak().lightLevel((state) -> 15)
					.sound(SoundType.WOOD), ParticleTypes.FLAME));

	/**
	 * Items
	 */
	public static final RegistryObject<Item> RECOVERY_HEART = ITEMS.register("recovery_heart", () -> new RecoveryHeartItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_GEM = ITEMS.register("force_gem", () -> new BaseItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_INGOT = ITEMS.register("force_ingot", () -> new BaseItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_NUGGET = ITEMS.register("force_nugget", () -> new BaseItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_STICK = ITEMS.register("force_stick", () -> new BaseItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> UPGRADE_TOME = ITEMS.register("upgrade_tome", () -> new UpgradeTomeItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_PACK_UPGRADE = ITEMS.register("force_pack_upgrade", () -> new BaseItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> UPGRADE_CORE = ITEMS.register("upgrade_core", () -> new BaseItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> EXPERIENCE_CORE = ITEMS.register("experience_core", () -> new UpgradeCoreItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FREEZING_CORE = ITEMS.register("freezing_core", () -> new UpgradeCoreItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> GRINDING_CORE = ITEMS.register("grinding_core", () -> new UpgradeCoreItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> HEAT_CORE = ITEMS.register("heat_core", () -> new UpgradeCoreItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> SPEED_CORE = ITEMS.register("speed_core", () -> new UpgradeCoreItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> RED_CHU_JELLY = ITEMS.register("red_chu_jelly", () -> new BaseItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> GREEN_CHU_JELLY = ITEMS.register("green_chu_jelly", () -> new BaseItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> BLUE_CHU_JELLY = ITEMS.register("blue_chu_jelly", () -> new BaseItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> GOLD_CHU_JELLY = ITEMS.register("gold_chu_jelly", () -> new BaseItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> PILE_OF_GUNPOWDER = ITEMS.register("pile_of_gunpowder", () -> new BaseItem(itemBuilder().tab(ForceCraft.creativeTab)));

	public static final RegistryObject<Item> FORTUNE_COOKIE = ITEMS.register("fortune_cookie", () ->
			new CustomFoodItem(itemBuilder().food(ForceFoods.FORTUNE_COOKIE).stacksTo(1).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> SOUL_WAFER = ITEMS.register("soul_wafer", () ->
			new CustomFoodItem(itemBuilder().food(ForceFoods.SOUL_WAFER).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> RAW_BACON = ITEMS.register("raw_bacon", () ->
			new CustomFoodItem(itemBuilder().food(ForceFoods.BACON).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> COOKED_BACON = ITEMS.register("cooked_bacon", () ->
			new CustomFoodItem(itemBuilder().food(ForceFoods.COOKED_BACON).tab(ForceCraft.creativeTab)));

	public static final RegistryObject<Item> FORCE_HELMET = ITEMS.register("force_helmet", () ->
			new ForceArmorItem(ModArmor.FORCE_ARMOR, EquipmentSlot.HEAD, itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_CHEST = ITEMS.register("force_chest", () ->
			new ForceArmorItem(ModArmor.FORCE_ARMOR, EquipmentSlot.CHEST, itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_LEGS = ITEMS.register("force_legs", () ->
			new ForceArmorItem(ModArmor.FORCE_ARMOR, EquipmentSlot.LEGS, itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BOOTS = ITEMS.register("force_boots", () ->
			new ForceArmorItem(ModArmor.FORCE_ARMOR, EquipmentSlot.FEET, itemBuilder().tab(ForceCraft.creativeTab)));

	public static final RegistryObject<Item> FORCE_ROD = ITEMS.register("force_rod", () -> new ForceRodItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_WRENCH = ITEMS.register("force_wrench", () -> new ForceWrenchItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> GOLDEN_POWER_SOURCE = ITEMS.register("golden_power_source", () -> new GoldenPowerSourceItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> CLAW = ITEMS.register("claw", () -> new BaseItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORTUNE = ITEMS.register("fortune", () -> new FortuneItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_GEAR = ITEMS.register("force_gear", () -> new BaseItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> SNOW_COOKIE = ITEMS.register("snow_cookie", () -> new BaseItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_PACK = ITEMS.register("force_pack", () -> new ForcePackItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BELT = ITEMS.register("force_belt", () -> new ForceBeltItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> BOTTLED_WITHER = ITEMS.register("bottled_wither", () -> new BottledWitherItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> INERT_CORE = ITEMS.register("inert_core", () -> new InertCoreItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> BACONATOR = ITEMS.register("baconator", () -> new BaconatorItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> SPOILS_BAG = ITEMS.register("spoils_bag", () -> new SpoilsBagItem(itemBuilder().tab(ForceCraft.creativeTab), 1));
	public static final RegistryObject<Item> SPOILS_BAG_T2 = ITEMS.register("spoils_bag_t2", () -> new SpoilsBagItem(itemBuilder().tab(ForceCraft.creativeTab), 2));
	public static final RegistryObject<Item> SPOILS_BAG_T3 = ITEMS.register("spoils_bag_t3", () -> new SpoilsBagItem(itemBuilder().tab(ForceCraft.creativeTab), 3));
	public static final RegistryObject<Item> LIFE_CARD = ITEMS.register("life_card", () -> new BaseItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> DARKNESS_CARD = ITEMS.register("darkness_card", () -> new BaseItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> UNDEATH_CARD = ITEMS.register("undeath_card", () -> new BaseItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> TREASURE_CORE = ITEMS.register("treasure_core", () -> new BaseItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_ARROW = ITEMS.register("force_arrow", () -> new ForceArrowItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_FLASK = ITEMS.register("force_flask", () -> new ForceFlaskItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> MILK_FORCE_FLASK = ITEMS.register("milk_force_flask", () -> new MilkFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_FILLED_FORCE_FLASK = ITEMS.register("force_filled_force_flask", () -> new ForceFilledForceFlask(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> RED_POTION = ITEMS.register("red_potion", () -> new RedPotionItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> ENTITY_FLASK = ITEMS.register("entity_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));

	public static final RegistryObject<Item> BAT_FLASK = ITEMS.register("bat_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> BEE_FLASK = ITEMS.register("bee_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> CAT_FLASK = ITEMS.register("cat_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> CAVE_SPIDER_FLASK = ITEMS.register("cave_spider_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> CHICKEN_FLASK = ITEMS.register("chicken_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> COD_FLASK = ITEMS.register("cod_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> COW_FLASK = ITEMS.register("cow_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> DOLPHIN_FLASK = ITEMS.register("dolphin_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> DONKEY_FLASK = ITEMS.register("donkey_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> ENDERMAN_FLASK = ITEMS.register("enderman_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FOX_FLASK = ITEMS.register("fox_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> HORSE_FLASK = ITEMS.register("horse_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> IRON_GOLEM_FLASK = ITEMS.register("iron_golem_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> LLAMA_FLASK = ITEMS.register("llama_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> MOOSHROOM_FLASK = ITEMS.register("mooshroom_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> MULE_FLASK = ITEMS.register("mule_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> PANDA_FLASK = ITEMS.register("panda_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> PARROT_FLASK = ITEMS.register("parrot_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> PIG_FLASK = ITEMS.register("pig_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> PIGLIN_FLASK = ITEMS.register("piglin_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> POLAR_BEAR_FLASK = ITEMS.register("polar_bear_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> PUFFERFISH_FLASK = ITEMS.register("pufferfish_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> RABBIT_FLASK = ITEMS.register("rabbit_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> SALMON_FLASK = ITEMS.register("salmon_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> SHEEP_FLASK = ITEMS.register("sheep_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> SKELETON_FLASK = ITEMS.register("skeleton_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> SNOW_GOLEM_FLASK = ITEMS.register("snow_golem_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> SPIDER_FLASK = ITEMS.register("spider_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> SQUID_FLASK = ITEMS.register("squid_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> STRIDER_FLASK = ITEMS.register("strider_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> TROPICAL_FISH_FLASK = ITEMS.register("tropical_fish_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> TURTLE_FLASK = ITEMS.register("turtle_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> VILLAGER_FLASK = ITEMS.register("villager_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> WANDERING_TRADER_FLASK = ITEMS.register("wandering_trader_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> WOLF_FLASK = ITEMS.register("wolf_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> ZOMBIFIED_PIGLIN_FLASK = ITEMS.register("zombified_piglin_flask", () -> new EntityFlaskItem(itemBuilder().craftRemainder(FORCE_FLASK.get()).tab(ForceCraft.creativeTab)));

	public static final RegistryObject<Item> ITEM_CARD = ITEMS.register("item_card", () -> new ItemCardItem(itemBuilder().tab(ForceCraft.creativeTab)));

	//Tools
	public static final RegistryObject<Item> FORCE_PICKAXE = ITEMS.register("force_pickaxe", () -> new ForcePickaxeItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_AXE = ITEMS.register("force_axe", () -> new ForceAxeItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_SWORD = ITEMS.register("force_sword", () -> new ForceSwordItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_SHOVEL = ITEMS.register("force_shovel", () -> new ForceShovelItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_SHEARS = ITEMS.register("force_shears", () -> new ForceShearsItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BOW = ITEMS.register("force_bow", () -> new ForceBowItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_MITT = ITEMS.register("force_mitt", () -> new ForceMittItem(itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> MAGNET_GLOVE = ITEMS.register("magnet_glove", () -> new MagnetGloveItem(itemBuilder().tab(ForceCraft.creativeTab)));

	//Bucket
	public static final RegistryObject<Item> BUCKET_FLUID_FORCE = ITEMS.register("force_bucket", () ->
			new ForceFluidBucketItem(itemBuilder().craftRemainder(Items.BUCKET).stacksTo(1).tab(ForceCraft.creativeTab), () -> ForceFluids.FORCE_FLUID_SOURCE.get()));

	//Experience Tome
	public static final RegistryObject<Item> EXPERIENCE_TOME = ITEMS.register("experience_tome", () -> new ExperienceTomeItem(itemBuilder().tab(ForceCraft.creativeTab).stacksTo(1)));

	//Spawn eggs
	public static final RegistryObject<Item> RED_CHU_CHU_SPAWN_EGG = ITEMS.register("red_chu_chu_spawn_egg", () -> new ForgeSpawnEggItem(() -> ForceEntities.RED_CHU_CHU.get(), 15674931, 14483465, itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> GREEN_CHU_CHU_SPAWN_EGG = ITEMS.register("green_chu_chu_spawn_egg", () -> new ForgeSpawnEggItem(() -> ForceEntities.GREEN_CHU_CHU.get(), 6539935, 3190138, itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> BLUE_CHU_CHU_SPAWN_EGG = ITEMS.register("blue_chu_chu_spawn_egg", () -> new ForgeSpawnEggItem(() -> ForceEntities.BLUE_CHU_CHU.get(), 2450423, 16606, itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> GOLD_CHU_CHU_SPAWN_EGG = ITEMS.register("gold_chu_chu_spawn_egg", () -> new ForgeSpawnEggItem(() -> ForceEntities.GOLD_CHU_CHU.get(), 14921786, 13670157, itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> COLD_CHICKEN_SPAWN_EGG = ITEMS.register("cold_chicken_spawn_egg", () -> new ForgeSpawnEggItem(() -> ForceEntities.COLD_CHICKEN.get(), 10592673, 16711680, itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> COLD_COW_SPAWN_EGG = ITEMS.register("cold_cow_spawn_egg", () -> new ForgeSpawnEggItem(() -> ForceEntities.COLD_COW.get(), 4470310, 10592673, itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> COLD_PIG_SPAWN_EGG = ITEMS.register("cold_pig_spawn_egg", () -> new ForgeSpawnEggItem(() -> ForceEntities.COLD_PIG.get(), 15771042, 14377823, itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FAIRY_SPAWN_EGG = ITEMS.register("fairy_spawn_egg", () -> new ForgeSpawnEggItem(() -> ForceEntities.FAIRY.get(), 11395062, 12970232, itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> CREEPER_TOT_SPAWN_EGG = ITEMS.register("creeper_tot_spawn_egg", () -> new ForgeSpawnEggItem(() -> ForceEntities.CREEPER_TOT.get(), 894731, 0, itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> ENDER_TOT_SPAWN_EGG = ITEMS.register("ender_tot_spawn_egg", () -> new ForgeSpawnEggItem(() -> ForceEntities.ENDER_TOT.get(), 1447446, 0, itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> ANGRY_ENDERMAN_SPAWN_EGG = ITEMS.register("angry_enderman_spawn_egg", () -> new ForgeSpawnEggItem(() -> ForceEntities.ANGRY_ENDERMAN.get(), 1447446, 0, itemBuilder().tab(ForceCraft.creativeTab)));


	/**
	 * Block Entities
	 */
	public static final RegistryObject<BlockEntityType<InfuserBlockEntity>> INFUSER_BLOCK_ENTITY = BLOCK_ENTITIES.register("infuser", () -> BlockEntityType.Builder.of(
			InfuserBlockEntity::new, ForceRegistry.INFUSER.get()).build(null));

	public static final RegistryObject<BlockEntityType<ForceFurnaceBlockEntity>> FURNACE_BLOCK_ENTITY = BLOCK_ENTITIES.register("furnace", () -> BlockEntityType.Builder.of(
			ForceFurnaceBlockEntity::new, ForceRegistry.FORCE_FURNACE.get(), ForceRegistry.BLACK_FORCE_FURNACE.get(), ForceRegistry.BLUE_FORCE_FURNACE.get(),
			ForceRegistry.BROWN_FORCE_FURNACE.get(), ForceRegistry.CYAN_FORCE_FURNACE.get(), ForceRegistry.GRAY_FORCE_FURNACE.get(), ForceRegistry.GREEN_FORCE_FURNACE.get(),
			ForceRegistry.LIGHT_BLUE_FORCE_FURNACE.get(), ForceRegistry.LIGHT_GRAY_FORCE_FURNACE.get(), ForceRegistry.LIME_FORCE_FURNACE.get(), ForceRegistry.MAGENTA_FORCE_FURNACE.get(),
			ForceRegistry.ORANGE_FORCE_FURNACE.get(), ForceRegistry.PINK_FORCE_FURNACE.get(), ForceRegistry.PURPLE_FORCE_FURNACE.get(), ForceRegistry.RED_FORCE_FURNACE.get(),
			ForceRegistry.WHITE_FORCE_FURNACE.get()).build(null));

	public static final RegistryObject<BlockEntityType<TimeTorchBlockEntity>> TIME_TORCH_BLOCK_ENTITY = BLOCK_ENTITIES.register("time_torch", () -> BlockEntityType.Builder.of(
			TimeTorchBlockEntity::new, ForceRegistry.TIME_TORCH.get(), ForceRegistry.WALL_TIME_TORCH.get()).build(null));

	public static final RegistryObject<BlockEntityType<ForceEngineBlockEntity>> FORCE_ENGINE_BLOCK_ENTITY = BLOCK_ENTITIES.register("force_engine", () -> BlockEntityType.Builder.of(
			ForceEngineBlockEntity::new, ForceRegistry.FORCE_ENGINE.get()).build(null));


	/**
	 * Block items
	 */
	public static final RegistryObject<Item> POWER_ORE_ITEM = ITEMS.register("power_ore", () -> new BlockItem(ForceRegistry.POWER_ORE.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> DEEPSLATE_POWER_ORE_ITEM = ITEMS.register("deepslate_power_ore", () -> new BlockItem(ForceRegistry.DEEPSLATE_POWER_ORE.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_SAPLING_ITEM = ITEMS.register("force_sapling", () -> new BlockItem(ForceRegistry.FORCE_SAPLING.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_LOG_ITEM = ITEMS.register("force_log", () -> new BlockItem(ForceRegistry.FORCE_LOG.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_WOOD_ITEM = ITEMS.register("force_wood", () -> new BlockItem(ForceRegistry.FORCE_WOOD.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_LEAVES_ITEM = ITEMS.register("force_leaves", () -> new BlockItem(ForceRegistry.FORCE_LEAVES.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> INFUSER_ITEM = ITEMS.register("infuser", () -> new BlockItem(ForceRegistry.INFUSER.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_PLANKS_ITEM = ITEMS.register("force_planks", () -> new BlockItem(ForceRegistry.FORCE_PLANKS.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_PLANK_STAIRS_ITEM = ITEMS.register("force_plank_stairs", () -> new BlockItem(ForceRegistry.FORCE_PLANK_STAIRS.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_PLANK_SLAB_ITEM = ITEMS.register("force_plank_slab", () -> new BlockItem(ForceRegistry.FORCE_PLANK_SLAB.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_FURNACE_ITEM = ITEMS.register("force_furnace", () -> new BlockItem(ForceRegistry.FORCE_FURNACE.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> BLACK_FORCE_FURNACE_ITEM = ITEMS.register("black_force_furnace", () -> new BlockItem(ForceRegistry.BLACK_FORCE_FURNACE.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> BLUE_FORCE_FURNACE_ITEM = ITEMS.register("blue_force_furnace", () -> new BlockItem(ForceRegistry.BLUE_FORCE_FURNACE.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> BROWN_FORCE_FURNACE_ITEM = ITEMS.register("brown_force_furnace", () -> new BlockItem(ForceRegistry.BROWN_FORCE_FURNACE.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> CYAN_FORCE_FURNACE_ITEM = ITEMS.register("cyan_force_furnace", () -> new BlockItem(ForceRegistry.CYAN_FORCE_FURNACE.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> GRAY_FORCE_FURNACE_ITEM = ITEMS.register("gray_force_furnace", () -> new BlockItem(ForceRegistry.GRAY_FORCE_FURNACE.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> GREEN_FORCE_FURNACE_ITEM = ITEMS.register("green_force_furnace", () -> new BlockItem(ForceRegistry.GREEN_FORCE_FURNACE.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> LIGHT_BLUE_FORCE_FURNACE_ITEM = ITEMS.register("light_blue_force_furnace", () -> new BlockItem(ForceRegistry.LIGHT_BLUE_FORCE_FURNACE.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> LIGHT_GRAY_FORCE_FURNACE_ITEM = ITEMS.register("light_gray_force_furnace", () -> new BlockItem(ForceRegistry.LIGHT_GRAY_FORCE_FURNACE.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> LIME_FORCE_FURNACE_ITEM = ITEMS.register("lime_force_furnace", () -> new BlockItem(ForceRegistry.LIME_FORCE_FURNACE.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> MAGENTA_FORCE_FURNACE_ITEM = ITEMS.register("magenta_force_furnace", () -> new BlockItem(ForceRegistry.MAGENTA_FORCE_FURNACE.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> ORANGE_FORCE_FURNACE_ITEM = ITEMS.register("orange_force_furnace", () -> new BlockItem(ForceRegistry.ORANGE_FORCE_FURNACE.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> PINK_FORCE_FURNACE_ITEM = ITEMS.register("pink_force_furnace", () -> new BlockItem(ForceRegistry.PINK_FORCE_FURNACE.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> PURPLE_FORCE_FURNACE_ITEM = ITEMS.register("purple_force_furnace", () -> new BlockItem(ForceRegistry.PURPLE_FORCE_FURNACE.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> RED_FORCE_FURNACE_ITEM = ITEMS.register("red_force_furnace", () -> new BlockItem(ForceRegistry.RED_FORCE_FURNACE.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> WHITE_FORCE_FURNACE_ITEM = ITEMS.register("white_force_furnace", () -> new BlockItem(ForceRegistry.WHITE_FORCE_FURNACE.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_ENGINE_ITEM = ITEMS.register("force_engine", () -> new BlockItem(ForceRegistry.FORCE_ENGINE.get(), itemBuilder().tab(ForceCraft.creativeTab)));

	public static final RegistryObject<Item> FORCE_BRICK_RED_ITEM = ITEMS.register("force_brick_red", () -> new BlockItem(FORCE_BRICK_RED.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_YELLOW_ITEM = ITEMS.register("force_brick_yellow", () -> new BlockItem(FORCE_BRICK_YELLOW.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_GREEN_ITEM = ITEMS.register("force_brick_green", () -> new BlockItem(FORCE_BRICK_GREEN.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_BLUE_ITEM = ITEMS.register("force_brick_blue", () -> new BlockItem(FORCE_BRICK_BLUE.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_WHITE_ITEM = ITEMS.register("force_brick_white", () -> new BlockItem(FORCE_BRICK_WHITE.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_BLACK_ITEM = ITEMS.register("force_brick_black", () -> new BlockItem(FORCE_BRICK_BLACK.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_BROWN_ITEM = ITEMS.register("force_brick_brown", () -> new BlockItem(FORCE_BRICK_BROWN.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_ORANGE_ITEM = ITEMS.register("force_brick_orange", () -> new BlockItem(FORCE_BRICK_ORANGE.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_LIGHT_BLUE_ITEM = ITEMS.register("force_brick_light_blue", () -> new BlockItem(FORCE_BRICK_LIGHT_BLUE.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_MAGENTA_ITEM = ITEMS.register("force_brick_magenta", () -> new BlockItem(FORCE_BRICK_MAGENTA.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_PINK_ITEM = ITEMS.register("force_brick_pink", () -> new BlockItem(FORCE_BRICK_PINK.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_LIGHT_GRAY_ITEM = ITEMS.register("force_brick_light_gray", () -> new BlockItem(FORCE_BRICK_LIGHT_GRAY.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_LIME_ITEM = ITEMS.register("force_brick_lime", () -> new BlockItem(FORCE_BRICK_LIME.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_CYAN_ITEM = ITEMS.register("force_brick_cyan", () -> new BlockItem(FORCE_BRICK_CYAN.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_PURPLE_ITEM = ITEMS.register("force_brick_purple", () -> new BlockItem(FORCE_BRICK_PURPLE.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_GRAY_ITEM = ITEMS.register("force_brick_gray", () -> new BlockItem(FORCE_BRICK_GRAY.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_ITEM = ITEMS.register("force_brick", () -> new BlockItem(FORCE_BRICK.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_RED_STAIRS_ITEM = ITEMS.register("force_brick_red_stairs", () -> new BlockItem(FORCE_BRICK_RED_STAIRS.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_YELLOW_STAIRS_ITEM = ITEMS.register("force_brick_yellow_stairs", () -> new BlockItem(FORCE_BRICK_YELLOW_STAIRS.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_GREEN_STAIRS_ITEM = ITEMS.register("force_brick_green_stairs", () -> new BlockItem(FORCE_BRICK_GREEN_STAIRS.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_BLUE_STAIRS_ITEM = ITEMS.register("force_brick_blue_stairs", () -> new BlockItem(FORCE_BRICK_BLUE_STAIRS.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_WHITE_STAIRS_ITEM = ITEMS.register("force_brick_white_stairs", () -> new BlockItem(FORCE_BRICK_WHITE_STAIRS.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_BLACK_STAIRS_ITEM = ITEMS.register("force_brick_black_stairs", () -> new BlockItem(FORCE_BRICK_BLACK_STAIRS.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_BROWN_STAIRS_ITEM = ITEMS.register("force_brick_brown_stairs", () -> new BlockItem(FORCE_BRICK_BROWN_STAIRS.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_ORANGE_STAIRS_ITEM = ITEMS.register("force_brick_orange_stairs", () -> new BlockItem(FORCE_BRICK_ORANGE_STAIRS.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_LIGHT_BLUE_STAIRS_ITEM = ITEMS.register("force_brick_light_blue_stairs", () -> new BlockItem(FORCE_BRICK_LIGHT_BLUE_STAIRS.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_MAGENTA_STAIRS_ITEM = ITEMS.register("force_brick_magenta_stairs", () -> new BlockItem(FORCE_BRICK_MAGENTA_STAIRS.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_PINK_STAIRS_ITEM = ITEMS.register("force_brick_pink_stairs", () -> new BlockItem(FORCE_BRICK_PINK_STAIRS.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_LIGHT_GRAY_STAIRS_ITEM = ITEMS.register("force_brick_light_gray_stairs", () -> new BlockItem(FORCE_BRICK_LIGHT_GRAY_STAIRS.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_LIME_STAIRS_ITEM = ITEMS.register("force_brick_lime_stairs", () -> new BlockItem(FORCE_BRICK_LIME_STAIRS.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_CYAN_STAIRS_ITEM = ITEMS.register("force_brick_cyan_stairs", () -> new BlockItem(FORCE_BRICK_CYAN_STAIRS.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_PURPLE_STAIRS_ITEM = ITEMS.register("force_brick_purple_stairs", () -> new BlockItem(FORCE_BRICK_PURPLE_STAIRS.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_GRAY_STAIRS_ITEM = ITEMS.register("force_brick_gray_stairs", () -> new BlockItem(FORCE_BRICK_GRAY_STAIRS.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_STAIRS_ITEM = ITEMS.register("force_brick_stairs", () -> new BlockItem(FORCE_BRICK_STAIRS.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_RED_SLAB_ITEM = ITEMS.register("force_brick_red_slab", () -> new BlockItem(FORCE_BRICK_RED_SLAB.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_YELLOW_SLAB_ITEM = ITEMS.register("force_brick_yellow_slab", () -> new BlockItem(FORCE_BRICK_YELLOW_SLAB.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_GREEN_SLAB_ITEM = ITEMS.register("force_brick_green_slab", () -> new BlockItem(FORCE_BRICK_GREEN_SLAB.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_BLUE_SLAB_ITEM = ITEMS.register("force_brick_blue_slab", () -> new BlockItem(FORCE_BRICK_BLUE_SLAB.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_WHITE_SLAB_ITEM = ITEMS.register("force_brick_white_slab", () -> new BlockItem(FORCE_BRICK_WHITE_SLAB.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_BLACK_SLAB_ITEM = ITEMS.register("force_brick_black_slab", () -> new BlockItem(FORCE_BRICK_BLACK_SLAB.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_BROWN_SLAB_ITEM = ITEMS.register("force_brick_brown_slab", () -> new BlockItem(FORCE_BRICK_BROWN_SLAB.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_ORANGE_SLAB_ITEM = ITEMS.register("force_brick_orange_slab", () -> new BlockItem(FORCE_BRICK_ORANGE_SLAB.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_LIGHT_BLUE_SLAB_ITEM = ITEMS.register("force_brick_light_blue_slab", () -> new BlockItem(FORCE_BRICK_LIGHT_BLUE_SLAB.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_MAGENTA_SLAB_ITEM = ITEMS.register("force_brick_magenta_slab", () -> new BlockItem(FORCE_BRICK_MAGENTA_SLAB.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_PINK_SLAB_ITEM = ITEMS.register("force_brick_pink_slab", () -> new BlockItem(FORCE_BRICK_PINK_SLAB.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_LIGHT_GRAY_SLAB_ITEM = ITEMS.register("force_brick_light_gray_slab", () -> new BlockItem(FORCE_BRICK_LIGHT_GRAY_SLAB.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_LIME_SLAB_ITEM = ITEMS.register("force_brick_lime_slab", () -> new BlockItem(FORCE_BRICK_LIME_SLAB.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_CYAN_SLAB_ITEM = ITEMS.register("force_brick_cyan_slab", () -> new BlockItem(FORCE_BRICK_CYAN_SLAB.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_PURPLE_SLAB_ITEM = ITEMS.register("force_brick_purple_slab", () -> new BlockItem(FORCE_BRICK_PURPLE_SLAB.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_GRAY_SLAB_ITEM = ITEMS.register("force_brick_gray_slab", () -> new BlockItem(FORCE_BRICK_GRAY_SLAB.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BRICK_SLAB_ITEM = ITEMS.register("force_brick_slab", () -> new BlockItem(FORCE_BRICK_SLAB.get(), itemBuilder().tab(ForceCraft.creativeTab)));

	public static final RegistryObject<Item> FORCE_TORCH_ITEM = ITEMS.register("force_torch", () -> new StandingAndWallBlockItem(FORCE_TORCH.get(), WALL_FORCE_TORCH.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_RED_TORCH_ITEM = ITEMS.register("force_red_torch", () -> new StandingAndWallBlockItem(FORCE_RED_TORCH.get(), WALL_FORCE_RED_TORCH.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_ORANGE_TORCH_ITEM = ITEMS.register("force_orange_torch", () -> new StandingAndWallBlockItem(FORCE_ORANGE_TORCH.get(), WALL_FORCE_ORANGE_TORCH.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_GREEN_TORCH_ITEM = ITEMS.register("force_green_torch", () -> new StandingAndWallBlockItem(FORCE_GREEN_TORCH.get(), WALL_FORCE_GREEN_TORCH.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BLUE_TORCH_ITEM = ITEMS.register("force_blue_torch", () -> new StandingAndWallBlockItem(FORCE_BLUE_TORCH.get(), WALL_FORCE_BLUE_TORCH.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_WHITE_TORCH_ITEM = ITEMS.register("force_white_torch", () -> new StandingAndWallBlockItem(FORCE_WHITE_TORCH.get(), WALL_FORCE_WHITE_TORCH.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BLACK_TORCH_ITEM = ITEMS.register("force_black_torch", () -> new StandingAndWallBlockItem(FORCE_BLACK_TORCH.get(), WALL_FORCE_BLACK_TORCH.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_BROWN_TORCH_ITEM = ITEMS.register("force_brown_torch", () -> new StandingAndWallBlockItem(FORCE_BROWN_TORCH.get(), WALL_FORCE_BROWN_TORCH.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_LIGHT_BLUE_TORCH_ITEM = ITEMS.register("force_light_blue_torch", () -> new StandingAndWallBlockItem(FORCE_LIGHT_BLUE_TORCH.get(), WALL_FORCE_LIGHT_BLUE_TORCH.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_MAGENTA_TORCH_ITEM = ITEMS.register("force_magenta_torch", () -> new StandingAndWallBlockItem(FORCE_MAGENTA_TORCH.get(), WALL_FORCE_MAGENTA_TORCH.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_PINK_TORCH_ITEM = ITEMS.register("force_pink_torch", () -> new StandingAndWallBlockItem(FORCE_PINK_TORCH.get(), WALL_FORCE_PINK_TORCH.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_LIGHT_GRAY_TORCH_ITEM = ITEMS.register("force_light_gray_torch", () -> new StandingAndWallBlockItem(FORCE_LIGHT_GRAY_TORCH.get(), WALL_FORCE_LIGHT_GRAY_TORCH.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_LIME_TORCH_ITEM = ITEMS.register("force_lime_torch", () -> new StandingAndWallBlockItem(FORCE_LIME_TORCH.get(), WALL_FORCE_LIME_TORCH.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_CYAN_TORCH_ITEM = ITEMS.register("force_cyan_torch", () -> new StandingAndWallBlockItem(FORCE_CYAN_TORCH.get(), WALL_FORCE_CYAN_TORCH.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_PURPLE_TORCH_ITEM = ITEMS.register("force_purple_torch", () -> new StandingAndWallBlockItem(FORCE_PURPLE_TORCH.get(), WALL_FORCE_PURPLE_TORCH.get(), itemBuilder().tab(ForceCraft.creativeTab)));
	public static final RegistryObject<Item> FORCE_GRAY_TORCH_ITEM = ITEMS.register("force_gray_torch", () -> new StandingAndWallBlockItem(FORCE_GRAY_TORCH.get(), WALL_FORCE_GRAY_TORCH.get(), itemBuilder().tab(ForceCraft.creativeTab)));

	public static final RegistryObject<Item> TIME_TORCH_ITEM = ITEMS.register("time_torch", () -> new StandingAndWallBlockItem(TIME_TORCH.get(), WALL_TIME_TORCH.get(), itemBuilder().tab(ForceCraft.creativeTab)));

	private static Item.Properties itemBuilder() {
		return new Item.Properties();
	}
}
