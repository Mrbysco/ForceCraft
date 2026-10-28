package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.block.ForceEngineBlock;
import com.mrbysco.forcecraft.block.ForceFluidBlock;
import com.mrbysco.forcecraft.block.ForceFurnaceBlock;
import com.mrbysco.forcecraft.block.InfuserBlock;
import com.mrbysco.forcecraft.block.tree.ForceTreeGrower;
import com.mrbysco.forcecraft.blockentity.ForceEngineBlockEntity;
import com.mrbysco.forcecraft.blockentity.furnace.ForceFurnaceBlockEntity;
import com.mrbysco.forcecraft.blockentity.infuser.InfuserBlockEntity;
import com.mrbysco.forcecraft.blockentity.torch.AbstractTorchBlockEntity;
import com.mrbysco.forcecraft.blockentity.torch.BaneTorchBlockEntity;
import com.mrbysco.forcecraft.blockentity.torch.HealingTorchBlockEntity;
import com.mrbysco.forcecraft.blockentity.torch.HeatTorchBlockEntity;
import com.mrbysco.forcecraft.blockentity.torch.TimeTorchBlockEntity;
import com.mrbysco.forcecraft.item.BombItem;
import com.mrbysco.forcecraft.item.ExperienceTomeItem;
import com.mrbysco.forcecraft.item.ForceArrowItem;
import com.mrbysco.forcecraft.item.ForceBeltItem;
import com.mrbysco.forcecraft.item.ForcePackItem;
import com.mrbysco.forcecraft.item.MobChunkItem;
import com.mrbysco.forcecraft.item.tool.ForceBowItem;
import com.mrbysco.forcecraft.item.tool.ForceMittItem;
import com.mrbysco.forcecraft.item.tool.ForceRodItem;
import com.mrbysco.forcecraft.item.tool.ForceShearsItem;
import com.mrbysco.forcecraft.registry.helper.ForceBrickReg;
import com.mrbysco.forcecraft.registry.helper.ForceFurnaceReg;
import com.mrbysco.forcecraft.registry.helper.ForcePlankReg;
import com.mrbysco.forcecraft.registry.helper.ForceTorchReg;
import com.mrbysco.forcecraft.upgrade.UpgradeBookData;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.UntintedParticleLeavesBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@EventBusSubscriber
public class ForceRegistry {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MOD_ID);
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MOD_ID);
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Reference.MOD_ID);

	public static final DeferredBlock<DropExperienceBlock> POWER_ORE = BLOCKS.registerBlock("power_ore", (properties) -> new DropExperienceBlock(ConstantInt.of(0), properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F));
	public static final DeferredItem<BlockItem> POWER_ORE_ITEM = ITEMS.registerSimpleBlockItem(POWER_ORE, (properties) -> properties.rarity(Rarity.UNCOMMON));
	public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_POWER_ORE = BLOCKS.registerBlock("deepslate_power_ore", (properties) -> new DropExperienceBlock(ConstantInt.of(0), properties), () -> BlockBehaviour.Properties.ofLegacyCopy(POWER_ORE.get()).mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE));
	public static final DeferredItem<BlockItem> DEEPSLATE_POWER_ORE_ITEM = ITEMS.registerSimpleBlockItem(DEEPSLATE_POWER_ORE, (properties) -> properties.rarity(Rarity.UNCOMMON));
	public static final DeferredBlock<DropExperienceBlock> NETHER_POWER_ORE = BLOCKS.registerBlock("nether_power_ore", (properties) -> new DropExperienceBlock(ConstantInt.of(0), properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.NETHER).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.NETHER_ORE));
	public static final DeferredItem<BlockItem> NETHER_POWER_ORE_ITEM = ITEMS.registerSimpleBlockItem(NETHER_POWER_ORE, (properties) -> properties.rarity(Rarity.UNCOMMON));

	public static final DeferredBlock<RotatedPillarBlock> FORCE_LOG = BLOCKS.registerBlock("force_log", RotatedPillarBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).lightLevel((state) -> 2).ignitedByLava());
	public static final DeferredItem<BlockItem> FORCE_LOG_ITEM = ITEMS.registerSimpleBlockItem(FORCE_LOG);
	public static final DeferredBlock<RotatedPillarBlock> FORCE_WOOD = BLOCKS.registerBlock("force_wood", RotatedPillarBlock::new, () -> BlockBehaviour.Properties.ofLegacyCopy(FORCE_LOG.get()));
	public static final DeferredItem<BlockItem> FORCE_WOOD_ITEM = ITEMS.registerSimpleBlockItem(FORCE_WOOD);
	public static final ForcePlankReg FORCE_PLANKS = new ForcePlankReg();
	public static final DeferredBlock<LeavesBlock> FORCE_LEAVES = BLOCKS.registerBlock("force_leaves", (properties) ->
			new UntintedParticleLeavesBlock(0.01F, ColorParticleOption.create(ParticleTypes.TINTED_LEAVES, ARGB.opaque(0xafbc63)), properties), () ->
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.PLANT).strength(0.3f).pushReaction(PushReaction.DESTROY).randomTicks().sound(SoundType.GRASS).noOcclusion()
					.isValidSpawn(Blocks::ocelotOrParrot)
					.isSuffocating((state, level, pos) -> false)
					.isViewBlocking((state, level, pos) -> false));
	public static final DeferredItem<BlockItem> FORCE_LEAVES_ITEM = ITEMS.registerSimpleBlockItem(FORCE_LEAVES);
	public static final DeferredBlock<SaplingBlock> FORCE_SAPLING = BLOCKS.registerBlock("force_sapling", (properties) ->
			new SaplingBlock(ForceTreeGrower.FORCE, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollision().randomTicks().instabreak().sound(SoundType.GRASS));
	public static final DeferredItem<BlockItem> FORCE_SAPLING_ITEM = ITEMS.registerSimpleBlockItem(FORCE_SAPLING);
	public static final DeferredBlock<FlowerPotBlock> POTTED_FORCE_SAPLING = BLOCKS.registerBlock("potted_force_sapling", (properties) ->
			new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, FORCE_SAPLING, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));

	public static final DeferredBlock<ForceEngineBlock> FORCE_ENGINE = BLOCKS.registerBlock("force_engine", ForceEngineBlock::new, () ->
			BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(4.0F, 18.0F)
					.lightLevel(ForceEngineBlock.getLightValueActive(8)));
	public static final DeferredItem<BlockItem> FORCE_ENGINE_ITEM = ITEMS.registerSimpleBlockItem(FORCE_ENGINE);

	public static final Supplier<BlockEntityType<ForceEngineBlockEntity>> FORCE_ENGINE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("force_engine", () -> new BlockEntityType<>(
			ForceEngineBlockEntity::new, ForceRegistry.FORCE_ENGINE.get()));

	public static final DeferredBlock<InfuserBlock> FORCE_INFUSER = BLOCKS.registerBlock("force_infuser", InfuserBlock::new, () ->
			BlockBehaviour.Properties.of().mapColor(MapColor.STONE).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().strength(2.0F));
	public static final DeferredItem<BlockItem> INFUSER_ITEM = ITEMS.registerSimpleBlockItem(FORCE_INFUSER);

	public static final Supplier<BlockEntityType<InfuserBlockEntity>> INFUSER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("force_infuser", () -> new BlockEntityType<>(
			InfuserBlockEntity::new, ForceRegistry.FORCE_INFUSER.get()));

	public static final DeferredBlock<ForceFurnaceBlock> FORCE_FURNACE = BLOCKS.registerBlock("force_furnace", ForceFurnaceBlock::new, () ->
			BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(4.0F, 18.0F).lightLevel(ForceFurnaceBlock.getLightValueLit(13)));
	public static final DeferredItem<BlockItem> FORCE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem(FORCE_FURNACE);

	// Force Torches
	private static final List<ForceFurnaceReg> FURNACES = new ArrayList<>();

	public static final ForceFurnaceReg FORCE_FURNACE_RED = createFurnace("force_furnace_red", DyeColor.RED);
	public static final ForceFurnaceReg FORCE_FURNACE_ORANGE = createFurnace("force_furnace_orange", DyeColor.ORANGE);
	public static final ForceFurnaceReg FORCE_FURNACE_GREEN = createFurnace("force_furnace_green", DyeColor.GREEN);
	public static final ForceFurnaceReg FORCE_FURNACE_BLUE = createFurnace("force_furnace_blue", DyeColor.BLUE);
	public static final ForceFurnaceReg FORCE_FURNACE_WHITE = createFurnace("force_furnace_white", DyeColor.WHITE);
	public static final ForceFurnaceReg FORCE_FURNACE_BLACK = createFurnace("force_furnace_black", DyeColor.BLACK);
	public static final ForceFurnaceReg FORCE_FURNACE_BROWN = createFurnace("force_furnace_brown", DyeColor.BROWN);
	public static final ForceFurnaceReg FORCE_FURNACE_LIGHT_BLUE = createFurnace("force_furnace_light_blue", DyeColor.LIGHT_BLUE);
	public static final ForceFurnaceReg FORCE_FURNACE_MAGENTA = createFurnace("force_furnace_magenta", DyeColor.MAGENTA);
	public static final ForceFurnaceReg FORCE_FURNACE_PINK = createFurnace("force_furnace_pink", DyeColor.PINK);
	public static final ForceFurnaceReg FORCE_FURNACE_LIGHT_GRAY = createFurnace("force_furnace_light_gray", DyeColor.LIGHT_GRAY);
	public static final ForceFurnaceReg FORCE_FURNACE_LIME = createFurnace("force_furnace_lime", DyeColor.LIME);
	public static final ForceFurnaceReg FORCE_FURNACE_CYAN = createFurnace("force_furnace_cyan", DyeColor.CYAN);
	public static final ForceFurnaceReg FORCE_FURNACE_PURPLE = createFurnace("force_furnace_purple", DyeColor.PURPLE);
	public static final ForceFurnaceReg FORCE_FURNACE_GRAY = createFurnace("force_furnace_gray", DyeColor.GRAY);

	public static List<ForceFurnaceReg> getFurnaces() {
		return Collections.unmodifiableList(FURNACES);
	}

	private static ForceFurnaceReg createFurnace(String name, @Nullable DyeColor color) {
		ForceFurnaceReg furnaceReg = new ForceFurnaceReg(color);
		FURNACES.add(furnaceReg);
		return furnaceReg;
	}

	private static Block[] getAllFurnaces() {
		List<Block> furnaces = new ArrayList<>();
		furnaces.add(FORCE_FURNACE.get());
		getFurnaces().forEach(reg -> furnaces.add(reg.getFurnace().get()));
		return furnaces.toArray(new Block[0]);
	}

	public static final Supplier<BlockEntityType<ForceFurnaceBlockEntity>> FURNACE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("force_furnace", () -> new BlockEntityType<>(
			ForceFurnaceBlockEntity::new, getAllFurnaces()));

	// Force Brick
	private static final List<ForceBrickReg> BRICKS = new ArrayList<>();

	public static final ForceBrickReg FORCE_BRICK_RED = createBrick(DyeColor.RED);
	public static final ForceBrickReg FORCE_BRICK_YELLOW = createBrick(DyeColor.YELLOW);
	public static final ForceBrickReg FORCE_BRICK_GREEN = createBrick(DyeColor.GREEN);
	public static final ForceBrickReg FORCE_BRICK_BLUE = createBrick(DyeColor.BLUE);
	public static final ForceBrickReg FORCE_BRICK_WHITE = createBrick(DyeColor.WHITE);
	public static final ForceBrickReg FORCE_BRICK_BLACK = createBrick(DyeColor.BLACK);
	public static final ForceBrickReg FORCE_BRICK_BROWN = createBrick(DyeColor.BROWN);
	public static final ForceBrickReg FORCE_BRICK_ORANGE = createBrick(DyeColor.ORANGE);
	public static final ForceBrickReg FORCE_BRICK_LIGHT_BLUE = createBrick(DyeColor.LIGHT_BLUE);
	public static final ForceBrickReg FORCE_BRICK_MAGENTA = createBrick(DyeColor.MAGENTA);
	public static final ForceBrickReg FORCE_BRICK_PINK = createBrick(DyeColor.PINK);
	public static final ForceBrickReg FORCE_BRICK_LIGHT_GRAY = createBrick(DyeColor.LIGHT_GRAY);
	public static final ForceBrickReg FORCE_BRICK_LIME = createBrick(DyeColor.LIME);
	public static final ForceBrickReg FORCE_BRICK_CYAN = createBrick(DyeColor.CYAN);
	public static final ForceBrickReg FORCE_BRICK_PURPLE = createBrick(DyeColor.PURPLE);
	public static final ForceBrickReg FORCE_BRICK_GRAY = createBrick(DyeColor.GRAY);
	public static final ForceBrickReg FORCE_BRICK = createBrick(null);

	public static List<ForceBrickReg> getBricks() {
		return Collections.unmodifiableList(BRICKS);
	}

	private static ForceBrickReg createBrick(@Nullable DyeColor color) {
		ForceBrickReg brickReg = new ForceBrickReg(color);
		BRICKS.add(brickReg);
		return brickReg;
	}

	// Force Torches
	private static final List<ForceTorchReg> TORCHES = new ArrayList<>();

	public static final ForceTorchReg FORCE_TORCH = createTorch("force_torch", null);
	public static final ForceTorchReg FORCE_TORCH_RED = createTorch("force_torch_red", DyeColor.RED);
	public static final ForceTorchReg FORCE_TORCH_ORANGE = createTorch("force_torch_orange", DyeColor.ORANGE);
	public static final ForceTorchReg FORCE_TORCH_GREEN = createTorch("force_torch_green", DyeColor.GREEN);
	public static final ForceTorchReg FORCE_TORCH_BLUE = createTorch("force_torch_blue", DyeColor.BLUE);
	public static final ForceTorchReg FORCE_TORCH_WHITE = createTorch("force_torch_white", DyeColor.WHITE);
	public static final ForceTorchReg FORCE_TORCH_BLACK = createTorch("force_torch_black", DyeColor.BLACK);
	public static final ForceTorchReg FORCE_TORCH_BROWN = createTorch("force_torch_brown", DyeColor.BROWN);
	public static final ForceTorchReg FORCE_TORCH_LIGHT_BLUE = createTorch("force_torch_light_blue", DyeColor.LIGHT_BLUE);
	public static final ForceTorchReg FORCE_TORCH_MAGENTA = createTorch("force_torch_magenta", DyeColor.MAGENTA);
	public static final ForceTorchReg FORCE_TORCH_PINK = createTorch("force_torch_pink", DyeColor.PINK);
	public static final ForceTorchReg FORCE_TORCH_LIGHT_GRAY = createTorch("force_torch_light_gray", DyeColor.LIGHT_GRAY);
	public static final ForceTorchReg FORCE_TORCH_LIME = createTorch("force_torch_lime", DyeColor.LIME);
	public static final ForceTorchReg FORCE_TORCH_CYAN = createTorch("force_torch_cyan", DyeColor.CYAN);
	public static final ForceTorchReg FORCE_TORCH_PURPLE = createTorch("force_torch_purple", DyeColor.PURPLE);
	public static final ForceTorchReg FORCE_TORCH_GRAY = createTorch("force_torch_gray", DyeColor.GRAY);
	public static final ForceTorchReg HEAT_TORCH = createTorch("heat_torch", null, () -> ForceRegistry.HEAT_TORCH_ENTITY.get()); // DO NOT REMOVE LAMBDA...
	public static final ForceTorchReg HEALING_TORCH = createTorch("healing_torch", null, () -> ForceRegistry.HEALING_TORCH_ENTITY.get()); // DO NOT REMOVE LAMBDA...
	public static final ForceTorchReg BANE_TORCH = createTorch("bane_torch", null, () -> ForceRegistry.BANE_TORCH_ENTITY.get()); // DO NOT REMOVE LAMBDA...
	public static final ForceTorchReg CAMO_TORCH = createTorch("camo_torch", null);
	public static final ForceTorchReg TIME_TORCH = createTorch("time_torch", null, () -> ForceRegistry.TIME_TORCH_ENTITY.get()); // DO NOT REMOVE LAMBDA...

	public static List<ForceTorchReg> getTorches() {
		return Collections.unmodifiableList(TORCHES);
	}

	private static ForceTorchReg createTorch(String name, @Nullable DyeColor color, Optional<Supplier<BlockEntityType<AbstractTorchBlockEntity>>> blockEntityTypeSupplier) {
		ForceTorchReg torchReg = new ForceTorchReg(name, color, blockEntityTypeSupplier);
		TORCHES.add(torchReg);
		return torchReg;
	}

	private static ForceTorchReg createTorch(String name, @Nullable DyeColor color, Supplier<BlockEntityType<AbstractTorchBlockEntity>> blockEntityTypeSupplier) {
		ForceTorchReg torchReg = new ForceTorchReg(name, color, Optional.of(blockEntityTypeSupplier));
		TORCHES.add(torchReg);
		return torchReg;
	}

	private static ForceTorchReg createTorch(String name, @Nullable DyeColor color) {
		return createTorch(name, color, Optional.empty());
	}

	public static final Supplier<BlockEntityType<AbstractTorchBlockEntity>> HEAT_TORCH_ENTITY = BLOCK_ENTITY_TYPES.register("heat_torch", () -> new BlockEntityType<>(
			HeatTorchBlockEntity::new, HEAT_TORCH.getTorch().get(), HEAT_TORCH.getWallTorch().get()));

	public static final Supplier<BlockEntityType<AbstractTorchBlockEntity>> HEALING_TORCH_ENTITY = BLOCK_ENTITY_TYPES.register("healing_torch", () -> new BlockEntityType<>(
			HealingTorchBlockEntity::new, HEALING_TORCH.getTorch().get(), HEALING_TORCH.getWallTorch().get()));

	public static final Supplier<BlockEntityType<AbstractTorchBlockEntity>> BANE_TORCH_ENTITY = BLOCK_ENTITY_TYPES.register("bane_torch", () -> new BlockEntityType<>(
			BaneTorchBlockEntity::new, BANE_TORCH.getTorch().get(), BANE_TORCH.getWallTorch().get()));

	public static final Supplier<BlockEntityType<AbstractTorchBlockEntity>> TIME_TORCH_ENTITY = BLOCK_ENTITY_TYPES.register("time_torch", () -> new BlockEntityType<>(
			TimeTorchBlockEntity::new, TIME_TORCH.getTorch().get(), TIME_TORCH.getWallTorch().get()));

	//Fluid
	public static final DeferredBlock<ForceFluidBlock> FORCE_FLUID_BLOCK = BLOCKS.registerBlock("force_fluid", (properties) ->
			new ForceFluidBlock(ForceFluids.FORCE_FLUID_SOURCE.get(), properties), () ->
			BlockBehaviour.Properties.of().mapColor(MapColor.SAND).replaceable().noCollision()
					.strength(100.0F).pushReaction(PushReaction.DESTROY).liquid()
					.lightLevel(state -> 15).noLootTable()
	);

	public static final DeferredItem<Item> FORCE_GEM = ITEMS.registerSimpleItem("force_gem", (properties) -> properties.rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> FORCE_INGOT = ITEMS.registerSimpleItem("force_ingot");
	public static final DeferredItem<Item> FORCE_NUGGET = ITEMS.registerSimpleItem("force_nugget");
	public static final DeferredItem<Item> GOLDEN_POWER_SOURCE = ITEMS.registerSimpleItem("golden_power_source", (properties) -> properties.rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> EXPERIENCE_TOME = ITEMS.registerItem("experience_tome", ExperienceTomeItem::new, (properties) -> properties.stacksTo(1).rarity(Rarity.UNCOMMON).component(ForceComponents.TOME_EXPERIENCE, 0));
	public static final DeferredItem<Item> UPGRADE_TOME = ITEMS.registerSimpleItem("upgrade_tome", (properties) -> properties.stacksTo(1).rarity(Rarity.UNCOMMON).component(ForceComponents.UPGRADE_BOOK, UpgradeBookData.DEFAULT));
	public static final DeferredItem<ForceRodItem> FORCE_ROD = ITEMS.registerItem("force_rod", ForceRodItem::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON).durability(75));
	public static final DeferredItem<ForcePackItem> FORCE_PACK = ITEMS.registerItem("force_pack", ForcePackItem::new, properties -> properties.rarity(Rarity.UNCOMMON).stacksTo(1));
	public static final DeferredItem<ForceBeltItem> FORCE_BELT = ITEMS.registerItem("force_belt", ForceBeltItem::new, properties -> properties.rarity(Rarity.UNCOMMON).stacksTo(1));
	public static final DeferredItem<Item> FORCE_PACK_UPGRADE = ITEMS.registerSimpleItem("force_pack_upgrade");
	public static final DeferredItem<Item> UPGRADE_CORE = ITEMS.registerSimpleItem("upgrade_core");
	public static final DeferredItem<Item> EXPERIENCE_CORE = ITEMS.registerSimpleItem("experience_core");
	public static final DeferredItem<Item> FROZEN_CORE = ITEMS.registerSimpleItem("frozen_core");
	public static final DeferredItem<Item> GRINDING_CORE = ITEMS.registerSimpleItem("grinding_core");
	public static final DeferredItem<Item> HEAT_CORE = ITEMS.registerSimpleItem("heat_core");
	public static final DeferredItem<Item> SPEED_CORE = ITEMS.registerSimpleItem("speed_core");
	//Armor
	public static final DeferredItem<Item> FORCE_HELMET = ITEMS.registerSimpleItem("force_helmet", (properties) ->
			properties.humanoidArmor(ModArmor.FORCE, ArmorType.HELMET).durability(ArmorType.HELMET.getDurability(31)));
	public static final DeferredItem<Item> FORCE_CHEST = ITEMS.registerSimpleItem("force_chest", (properties) ->
			properties.humanoidArmor(ModArmor.FORCE, ArmorType.CHESTPLATE).durability(ArmorType.CHESTPLATE.getDurability(31)));
	public static final DeferredItem<Item> FORCE_LEGS = ITEMS.registerSimpleItem("force_legs", (properties) ->
			properties.humanoidArmor(ModArmor.FORCE, ArmorType.LEGGINGS).durability(ArmorType.LEGGINGS.getDurability(31)));
	public static final DeferredItem<Item> FORCE_BOOTS = ITEMS.registerSimpleItem("force_boots", (properties) ->
			properties.humanoidArmor(ModArmor.FORCE, ArmorType.BOOTS).durability(ArmorType.BOOTS.getDurability(31)));

	//Tools
	public static final DeferredItem<Item> FORCE_PICKAXE = ITEMS.registerSimpleItem("force_pickaxe", (properties) -> properties.pickaxe(ModToolTiers.FORCE, -6, -2.8F));
	public static final DeferredItem<AxeItem> FORCE_AXE = ITEMS.registerItem("force_axe", (properties) -> new AxeItem(ModToolTiers.FORCE, 0F, -3.1F, properties));
	public static final DeferredItem<Item> FORCE_SWORD = ITEMS.registerSimpleItem("force_sword", (properties) -> properties.sword(ModToolTiers.FORCE, -2, -2.4F));
	public static final DeferredItem<ShovelItem> FORCE_SHOVEL = ITEMS.registerItem("force_shovel", (properties) -> new ShovelItem(ModToolTiers.FORCE, -7F, -3.0F, properties));
	public static final DeferredItem<ForceShearsItem> FORCE_SHEARS = ITEMS.registerItem("force_shears", ForceShearsItem::new, (properties) -> properties.stacksTo(1).durability(952));
	public static final DeferredItem<ForceBowItem> FORCE_BOW = ITEMS.registerItem("force_bow", ForceBowItem::new, (properties) -> properties.stacksTo(1).durability(332));
	public static final DeferredItem<Item> FORCE_MITT = ITEMS.registerItem("force_mitt", ForceMittItem::new, (properties) -> properties.durability(1000));
//	public static final DeferredItem<MagnetGloveItem> MAGNET_GLOVE = ITEMS.registerItem("magnet_glove", MagnetGloveItem::new);

	public static final DeferredItem<Item> FORCE_ARROW = ITEMS.registerItem("force_arrow", ForceArrowItem::new);
	public static final DeferredItem<Item> FORTUNE_COOKIE = ITEMS.registerSimpleItem("fortune_cookie");
	public static final DeferredItem<Item> FORTUNE = ITEMS.registerSimpleItem("fortune");
	public static final DeferredItem<Item> FORCE_STICK = ITEMS.registerSimpleItem("force_stick");
	public static final DeferredItem<Item> FORCE_GEAR = ITEMS.registerSimpleItem("force_gear");
	public static final DeferredItem<BucketItem> FORCE_BUCKET = ITEMS.registerItem("force_bucket", (properties) ->
			new BucketItem(ForceFluids.FORCE_FLUID_SOURCE.get(), properties), () -> new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));
	public static final DeferredItem<Item> CLAW = ITEMS.registerSimpleItem("claw");
	public static final DeferredItem<Item> INERT_CORE = ITEMS.registerSimpleItem("inert_core");
	public static final DeferredItem<Item> ROCS_FEATHER = ITEMS.registerSimpleItem("rocs_feather");
	public static final DeferredItem<Item> FEATHER_DUST = ITEMS.registerSimpleItem("feather_dust");
	public static final DeferredItem<Item> TREASURE_CORE = ITEMS.registerSimpleItem("treasure_core");
	public static final DeferredItem<Item> SOUL_WAFER = ITEMS.registerSimpleItem("soul_wafer", (properties) -> properties.rarity(Rarity.RARE));
	public static final DeferredItem<Item> SNOW_COOKIE = ITEMS.registerSimpleItem("snow_cookie");
	public static final DeferredItem<Item> BOMB = ITEMS.registerItem("bomb", (properties) -> new BombItem(properties, false));
	public static final DeferredItem<Item> SUPER_BOMB = ITEMS.registerItem("super_bomb", (properties) -> new BombItem(properties, true));


	// Mob Chunks
	private static final Map<DyeColor, DeferredItem<Item>> MOB_CHUNKS = new HashMap<>();

	public static final DeferredItem<Item> MOB_CHUNK_RED = createChunk(DyeColor.RED);
	public static final DeferredItem<Item> MOB_CHUNK_YELLOW = createChunk(DyeColor.YELLOW);
	public static final DeferredItem<Item> MOB_CHUNK_GREEN = createChunk(DyeColor.GREEN);
	public static final DeferredItem<Item> MOB_CHUNK_BLUE = createChunk(DyeColor.BLUE);
	public static final DeferredItem<Item> MOB_CHUNK_WHITE = createChunk(DyeColor.WHITE);
	public static final DeferredItem<Item> MOB_CHUNK_BLACK = createChunk(DyeColor.BLACK);
	public static final DeferredItem<Item> MOB_CHUNK_BROWN = createChunk(DyeColor.BROWN);
	public static final DeferredItem<Item> MOB_CHUNK_ORANGE = createChunk(DyeColor.ORANGE);
	public static final DeferredItem<Item> MOB_CHUNK_LIGHT_BLUE = createChunk(DyeColor.LIGHT_BLUE);
	public static final DeferredItem<Item> MOB_CHUNK_MAGENTA = createChunk(DyeColor.MAGENTA);
	public static final DeferredItem<Item> MOB_CHUNK_PINK = createChunk(DyeColor.PINK);
	public static final DeferredItem<Item> MOB_CHUNK_LIGHT_GRAY = createChunk(DyeColor.LIGHT_GRAY);
	public static final DeferredItem<Item> MOB_CHUNK_LIME = createChunk(DyeColor.LIME);
	public static final DeferredItem<Item> MOB_CHUNK_CYAN = createChunk(DyeColor.CYAN);
	public static final DeferredItem<Item> MOB_CHUNK_PURPLE = createChunk(DyeColor.PURPLE);
	public static final DeferredItem<Item> MOB_CHUNK_GRAY = createChunk(DyeColor.GRAY);

	public static Map<DyeColor, DeferredItem<Item>> getMobChunks() {
		return Collections.unmodifiableMap(MOB_CHUNKS);
	}

	private static DeferredItem<Item> createChunk(@NotNull DyeColor color) {
		String colorName = color.getName();
		DeferredItem<Item> chunk = ITEMS.registerItem("mob_chunk_" + colorName, MobChunkItem::new);
		MOB_CHUNKS.put(color, chunk);
		return chunk;
	}

	// Mob Ingots
	private static final Map<DyeColor, DeferredItem<Item>> MOB_INGOTS = new HashMap<>();

	public static final DeferredItem<Item> MOB_INGOT_RED = createMobIngot(DyeColor.RED);
	public static final DeferredItem<Item> MOB_INGOT_YELLOW = createMobIngot(DyeColor.YELLOW);
	public static final DeferredItem<Item> MOB_INGOT_GREEN = createMobIngot(DyeColor.GREEN);
	public static final DeferredItem<Item> MOB_INGOT_BLUE = createMobIngot(DyeColor.BLUE);
	public static final DeferredItem<Item> MOB_INGOT_WHITE = createMobIngot(DyeColor.WHITE);
	public static final DeferredItem<Item> MOB_INGOT_BLACK = createMobIngot(DyeColor.BLACK);
	public static final DeferredItem<Item> MOB_INGOT_BROWN = createMobIngot(DyeColor.BROWN);
	public static final DeferredItem<Item> MOB_INGOT_ORANGE = createMobIngot(DyeColor.ORANGE);
	public static final DeferredItem<Item> MOB_INGOT_LIGHT_BLUE = createMobIngot(DyeColor.LIGHT_BLUE);
	public static final DeferredItem<Item> MOB_INGOT_MAGENTA = createMobIngot(DyeColor.MAGENTA);
	public static final DeferredItem<Item> MOB_INGOT_PINK = createMobIngot(DyeColor.PINK);
	public static final DeferredItem<Item> MOB_INGOT_LIGHT_GRAY = createMobIngot(DyeColor.LIGHT_GRAY);
	public static final DeferredItem<Item> MOB_INGOT_LIME = createMobIngot(DyeColor.LIME);
	public static final DeferredItem<Item> MOB_INGOT_CYAN = createMobIngot(DyeColor.CYAN);
	public static final DeferredItem<Item> MOB_INGOT_PURPLE = createMobIngot(DyeColor.PURPLE);
	public static final DeferredItem<Item> MOB_INGOT_GRAY = createMobIngot(DyeColor.GRAY);

	public static Map<DyeColor, DeferredItem<Item>> getMobIngots() {
		return Collections.unmodifiableMap(MOB_INGOTS);
	}

	private static DeferredItem<Item> createMobIngot(@NotNull DyeColor color) {
		String colorName = color.getName();
		DeferredItem<Item> chunk = ITEMS.registerSimpleItem("mob_ingot_" + colorName);
		MOB_INGOTS.put(color, chunk);
		return chunk;
	}

	public static final Supplier<CreativeModeTab> FORCE_TAB = CREATIVE_MODE_TABS.register("tab", (properties) -> CreativeModeTab.builder()
			.icon(() -> new ItemStack(ForceRegistry.FORCE_GEM.get()))
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.title(Component.translatable("itemGroup.forcecraft"))
			.displayItems((features, output) -> {
				List<ItemStack> stacks = ForceRegistry.ITEMS.getEntries().stream().map(reg -> new ItemStack(reg.get())).toList();
				output.acceptAll(stacks);

				Item rodItem = ForceRegistry.FORCE_ROD.get();
				for (Supplier<DataComponentType<Boolean>> flag : List.of(
						ForceComponents.SIGHT,
						ForceComponents.CAMO,
						ForceComponents.TIME,
						ForceComponents.ENDER,
						ForceComponents.HEAT)) {
					ItemStack rod = new ItemStack(rodItem);
					rod.set(flag.get(), true);
					output.accept(rod);
				}

				for (int level = 1; level <= 2; level++) {
					ItemStack rod = new ItemStack(rodItem);
					rod.set(ForceComponents.HEALING.get(), level);
					output.accept(rod);
				}

				for (int level = 1; level <= 5; level++) {
					ItemStack rod = new ItemStack(rodItem);
					rod.set(ForceComponents.SPEED.get(), level);
					output.accept(rod);
				}
			}).build());

	@SubscribeEvent
	public static void addBlockEntityTypes(BlockEntityTypeAddBlocksEvent event) {
		event.modify(BlockEntityType.HANGING_SIGN,
				FORCE_PLANKS.getHangingSign().get(),
				FORCE_PLANKS.getWallHangingSign().get());

		event.modify(BlockEntityType.SIGN,
				FORCE_PLANKS.getSign().get(),
				FORCE_PLANKS.getWallSign().get());

		event.modify(BlockEntityType.SHELF,
				FORCE_PLANKS.getShelf().get());
	}
}
