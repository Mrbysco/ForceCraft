package mrbysco.forcecraft.registry;

import mrbysco.forcecraft.ForceCraft;
import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.blocks.ForceFluidBlock;
import mrbysco.forcecraft.blocks.ForceFurnaceBlock;
import mrbysco.forcecraft.blocks.ForceLeavesBlock;
import mrbysco.forcecraft.blocks.ForceLogBlock;
import mrbysco.forcecraft.blocks.flammable.FlammableBlock;
import mrbysco.forcecraft.blocks.flammable.FlammableSlab;
import mrbysco.forcecraft.blocks.flammable.FlammableStairs;
import mrbysco.forcecraft.blocks.infuser.InfuserBlock;
import mrbysco.forcecraft.blocks.infuser.InfuserTileEntity;
import mrbysco.forcecraft.blocks.torch.TimeTorchBlock;
import mrbysco.forcecraft.blocks.torch.WallTimeTorchBlock;
import mrbysco.forcecraft.blocks.tree.ForceTree;
import mrbysco.forcecraft.items.BaconatorItem;
import mrbysco.forcecraft.items.BaseItem;
import mrbysco.forcecraft.items.BottledWitherItem;
import mrbysco.forcecraft.items.CustomArmorItem;
import mrbysco.forcecraft.items.CustomFoodItem;
import mrbysco.forcecraft.items.CustomSpawnEggItem;
import mrbysco.forcecraft.items.ExperienceTomeItem;
import mrbysco.forcecraft.items.ForceBeltItem;
import mrbysco.forcecraft.items.ForceFluidBucketItem;
import mrbysco.forcecraft.items.ForcePackItem;
import mrbysco.forcecraft.items.FortuneItem;
import mrbysco.forcecraft.items.GoldenPowerSourceItem;
import mrbysco.forcecraft.items.SpoilsBagItem;
import mrbysco.forcecraft.items.UpgradeCoreItem;
import mrbysco.forcecraft.items.flask.ForceFilledForceFlask;
import mrbysco.forcecraft.items.flask.ForceFlaskItem;
import mrbysco.forcecraft.items.flask.MilkFlaskItem;
import mrbysco.forcecraft.items.flask.RedPotionItem;
import mrbysco.forcecraft.items.infuser.UpgradeTomeItem;
import mrbysco.forcecraft.items.nonburnable.InertCoreItem;
import mrbysco.forcecraft.items.tools.ForceArrowItem;
import mrbysco.forcecraft.items.tools.ForceAxeItem;
import mrbysco.forcecraft.items.tools.ForceBowItem;
import mrbysco.forcecraft.items.tools.ForceMittItem;
import mrbysco.forcecraft.items.tools.ForcePickaxeItem;
import mrbysco.forcecraft.items.tools.ForceRodItem;
import mrbysco.forcecraft.items.tools.ForceShearsItem;
import mrbysco.forcecraft.items.tools.ForceShovelItem;
import mrbysco.forcecraft.items.tools.ForceSwordItem;
import mrbysco.forcecraft.items.tools.ForceWrenchItem;
import mrbysco.forcecraft.items.tools.MagnetGloveItem;
import mrbysco.forcecraft.registry.material.ModArmor;
import mrbysco.forcecraft.tiles.ForceFurnaceTileEntity;
import mrbysco.forcecraft.tiles.TimeTorchTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.WallOrFloorItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ForceRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Reference.MOD_ID);

    /**
     * Blocks
     */
    public static final RegistryObject<Block> POWER_ORE = BLOCKS.register("power_ore", () ->
            new OreBlock(AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<Block> FORCE_SAPLING = BLOCKS.register("force_sapling", () ->
        new SaplingBlock(new ForceTree(), AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().zeroHardnessAndResistance().sound(SoundType.PLANT)));
    public static final RegistryObject<Block> FORCE_LOG = BLOCKS.register("force_log", () ->
        new ForceLogBlock(AbstractBlock.Properties.create(Material.WOOD, MaterialColor.SAND)
                .hardnessAndResistance(1.0F).sound(SoundType.WOOD).setLightLevel((state) -> 1)));
    public static final RegistryObject<Block> FORCE_WOOD = BLOCKS.register("force_wood", () ->
        new ForceLogBlock(AbstractBlock.Properties.create(Material.WOOD, MaterialColor.WOOD)
                .hardnessAndResistance(1.0F).sound(SoundType.WOOD).setLightLevel((state) -> 1)));
    public static final RegistryObject<Block> FORCE_PLANKS = BLOCKS.register("force_planks", () ->
            new FlammableBlock(AbstractBlock.Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD), 5, 20));
    public static final RegistryObject<Block> FORCE_PLANK_STAIRS = BLOCKS.register("force_plank_stairs", () ->
            new FlammableStairs(() -> FORCE_PLANKS.get().getDefaultState(), AbstractBlock.Properties.from(FORCE_PLANKS.get()), 5, 20));
    public static final RegistryObject<Block> FORCE_PLANK_SLAB = BLOCKS.register("force_plank_slab",
            () -> new FlammableSlab(AbstractBlock.Properties.from(FORCE_PLANKS.get()), 5, 20));


    public static final RegistryObject<Block> FORCE_LEAVES = BLOCKS.register("force_leaves", () ->
            new ForceLeavesBlock(AbstractBlock.Properties.create(Material.LEAVES).hardnessAndResistance(0.3f).tickRandomly().sound(SoundType.PLANT).notSolid()
                    .setAllowsSpawn(Blocks::allowsSpawnOnLeaves).setSuffocates(Blocks::isntSolid).setBlocksVision(Blocks::isntSolid)));

    public static final RegistryObject<Block> INFUSER = BLOCKS.register("infuser", () ->
            new InfuserBlock(AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(2.0F)));

    public static final RegistryObject<Block> FORCE_FLUID_BLOCK = BLOCKS.register("force_fluid", () ->
            new ForceFluidBlock(ForceFluids.FORCE_FLUID_SOURCE, AbstractBlock.Properties.create(Material.WATER).doesNotBlockMovement().setLightLevel(state -> 15).noDrops()));

    public static final RegistryObject<Block> FORCE_FURNACE = BLOCKS.register("force_furnace", () ->
            new ForceFurnaceBlock(AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(4.0F, 18.0F)
                    .setLightLevel(ForceFurnaceBlock.getLightValueLit(13))));

    //Bricks
    public static final RegistryObject<Block> FORCE_BRICK_RED = BLOCKS.register("force_brick_red", () -> new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.RED).hardnessAndResistance(2.0F, 12.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_YELLOW = BLOCKS.register("force_brick_yellow", () -> new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.YELLOW).hardnessAndResistance(2.0F, 12.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_GREEN = BLOCKS.register("force_brick_green", () -> new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.GREEN).hardnessAndResistance(2.0F, 12.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_BLUE = BLOCKS.register("force_brick_blue", () -> new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.BLUE).hardnessAndResistance(2.0F, 12.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_WHITE = BLOCKS.register("force_brick_white", () -> new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.WHITE).hardnessAndResistance(2.0F, 12.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_BLACK = BLOCKS.register("force_brick_black", () -> new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.BLACK).hardnessAndResistance(2.0F, 12.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_BROWN = BLOCKS.register("force_brick_brown", () -> new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.BROWN).hardnessAndResistance(2.0F, 12.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_ORANGE = BLOCKS.register("force_brick_orange", () -> new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.ORANGE).hardnessAndResistance(2.0F, 12.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_LIGHT_BLUE = BLOCKS.register("force_brick_light_blue", () -> new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.LIGHT_BLUE).hardnessAndResistance(2.0F, 12.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_MAGENTA = BLOCKS.register("force_brick_magenta", () -> new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.MAGENTA).hardnessAndResistance(2.0F, 12.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_PINK = BLOCKS.register("force_brick_pink", () -> new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.PINK).hardnessAndResistance(2.0F, 12.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_LIGHT_GRAY = BLOCKS.register("force_brick_light_gray", () -> new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.LIGHT_GRAY).hardnessAndResistance(2.0F, 12.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_LIME = BLOCKS.register("force_brick_lime", () -> new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.LIME).hardnessAndResistance(2.0F, 12.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_CYAN = BLOCKS.register("force_brick_cyan", () -> new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.CYAN).hardnessAndResistance(2.0F, 12.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_PURPLE = BLOCKS.register("force_brick_purple", () -> new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.PURPLE).hardnessAndResistance(2.0F, 12.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_GRAY = BLOCKS.register("force_brick_gray", () -> new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.GRAY).hardnessAndResistance(2.0F, 12.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK = BLOCKS.register("force_brick", () -> new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.YELLOW).hardnessAndResistance(2.0F, 12.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    //Stairs
    public static final RegistryObject<Block> FORCE_BRICK_RED_STAIRS = BLOCKS.register("force_brick_red_stairs", () -> new StairsBlock(() -> FORCE_BRICK_RED.get().getDefaultState(), AbstractBlock.Properties.from(FORCE_BRICK_RED.get())));
    public static final RegistryObject<Block> FORCE_BRICK_YELLOW_STAIRS = BLOCKS.register("force_brick_yellow_stairs", () -> new StairsBlock(() -> FORCE_BRICK_YELLOW.get().getDefaultState(), AbstractBlock.Properties.from(FORCE_BRICK_YELLOW.get())));
    public static final RegistryObject<Block> FORCE_BRICK_GREEN_STAIRS = BLOCKS.register("force_brick_green_stairs", () -> new StairsBlock(() -> FORCE_BRICK_GREEN.get().getDefaultState(), AbstractBlock.Properties.from(FORCE_BRICK_GREEN.get())));
    public static final RegistryObject<Block> FORCE_BRICK_BLUE_STAIRS = BLOCKS.register("force_brick_blue_stairs", () -> new StairsBlock(() -> FORCE_BRICK_BLUE.get().getDefaultState(), AbstractBlock.Properties.from(FORCE_BRICK_BLUE.get())));
    public static final RegistryObject<Block> FORCE_BRICK_WHITE_STAIRS = BLOCKS.register("force_brick_white_stairs", () -> new StairsBlock(() -> FORCE_BRICK_WHITE.get().getDefaultState(), AbstractBlock.Properties.from(FORCE_BRICK_WHITE.get())));
    public static final RegistryObject<Block> FORCE_BRICK_BLACK_STAIRS = BLOCKS.register("force_brick_black_stairs", () -> new StairsBlock(() -> FORCE_BRICK_BLACK.get().getDefaultState(), AbstractBlock.Properties.from(FORCE_BRICK_BLACK.get())));
    public static final RegistryObject<Block> FORCE_BRICK_BROWN_STAIRS = BLOCKS.register("force_brick_brown_stairs", () -> new StairsBlock(() -> FORCE_BRICK_BROWN.get().getDefaultState(), AbstractBlock.Properties.from(FORCE_BRICK_BROWN.get())));
    public static final RegistryObject<Block> FORCE_BRICK_ORANGE_STAIRS = BLOCKS.register("force_brick_orange_stairs", () -> new StairsBlock(() -> FORCE_BRICK_ORANGE.get().getDefaultState(), AbstractBlock.Properties.from(FORCE_BRICK_ORANGE.get())));
    public static final RegistryObject<Block> FORCE_BRICK_LIGHT_BLUE_STAIRS = BLOCKS.register("force_brick_light_blue_stairs", () -> new StairsBlock(() -> FORCE_BRICK_LIGHT_BLUE.get().getDefaultState(), AbstractBlock.Properties.from(FORCE_BRICK_LIGHT_BLUE.get())));
    public static final RegistryObject<Block> FORCE_BRICK_MAGENTA_STAIRS = BLOCKS.register("force_brick_magenta_stairs", () -> new StairsBlock(() -> FORCE_BRICK_MAGENTA.get().getDefaultState(), AbstractBlock.Properties.from(FORCE_BRICK_MAGENTA.get())));
    public static final RegistryObject<Block> FORCE_BRICK_PINK_STAIRS = BLOCKS.register("force_brick_pink_stairs", () -> new StairsBlock(() -> FORCE_BRICK_PINK.get().getDefaultState(), AbstractBlock.Properties.from(FORCE_BRICK_PINK.get())));
    public static final RegistryObject<Block> FORCE_BRICK_LIGHT_GRAY_STAIRS = BLOCKS.register("force_brick_light_gray_stairs", () -> new StairsBlock(() -> FORCE_BRICK_LIGHT_GRAY.get().getDefaultState(), AbstractBlock.Properties.from(FORCE_BRICK_LIGHT_GRAY.get())));
    public static final RegistryObject<Block> FORCE_BRICK_LIME_STAIRS = BLOCKS.register("force_brick_lime_stairs", () -> new StairsBlock(() -> FORCE_BRICK_LIME.get().getDefaultState(), AbstractBlock.Properties.from(FORCE_BRICK_LIME.get())));
    public static final RegistryObject<Block> FORCE_BRICK_CYAN_STAIRS = BLOCKS.register("force_brick_cyan_stairs", () -> new StairsBlock(() -> FORCE_BRICK_CYAN.get().getDefaultState(), AbstractBlock.Properties.from(FORCE_BRICK_CYAN.get())));
    public static final RegistryObject<Block> FORCE_BRICK_PURPLE_STAIRS = BLOCKS.register("force_brick_purple_stairs", () -> new StairsBlock(() -> FORCE_BRICK_PURPLE.get().getDefaultState(), AbstractBlock.Properties.from(FORCE_BRICK_PURPLE.get())));
    public static final RegistryObject<Block> FORCE_BRICK_GRAY_STAIRS = BLOCKS.register("force_brick_gray_stairs", () -> new StairsBlock(() -> FORCE_BRICK_GRAY.get().getDefaultState(), AbstractBlock.Properties.from(FORCE_BRICK_GRAY.get())));
    public static final RegistryObject<Block> FORCE_BRICK_STAIRS = BLOCKS.register("force_brick_stairs", () -> new StairsBlock(() -> FORCE_BRICK.get().getDefaultState(), AbstractBlock.Properties.from(FORCE_BRICK.get())));

    //Slabs
    public static final RegistryObject<Block> FORCE_BRICK_RED_SLAB = BLOCKS.register("force_brick_red_slab", () -> new SlabBlock(AbstractBlock.Properties.from(FORCE_BRICK_RED.get())));
    public static final RegistryObject<Block> FORCE_BRICK_YELLOW_SLAB = BLOCKS.register("force_brick_yellow_slab", () -> new SlabBlock(AbstractBlock.Properties.from(FORCE_BRICK_YELLOW.get())));
    public static final RegistryObject<Block> FORCE_BRICK_GREEN_SLAB = BLOCKS.register("force_brick_green_slab", () -> new SlabBlock(AbstractBlock.Properties.from(FORCE_BRICK_GREEN.get())));
    public static final RegistryObject<Block> FORCE_BRICK_BLUE_SLAB = BLOCKS.register("force_brick_blue_slab", () -> new SlabBlock(AbstractBlock.Properties.from(FORCE_BRICK_BLUE.get())));
    public static final RegistryObject<Block> FORCE_BRICK_WHITE_SLAB = BLOCKS.register("force_brick_white_slab", () -> new SlabBlock(AbstractBlock.Properties.from(FORCE_BRICK_WHITE.get())));
    public static final RegistryObject<Block> FORCE_BRICK_BLACK_SLAB = BLOCKS.register("force_brick_black_slab", () -> new SlabBlock(AbstractBlock.Properties.from(FORCE_BRICK_BLACK.get())));
    public static final RegistryObject<Block> FORCE_BRICK_BROWN_SLAB = BLOCKS.register("force_brick_brown_slab", () -> new SlabBlock(AbstractBlock.Properties.from(FORCE_BRICK_BROWN.get())));
    public static final RegistryObject<Block> FORCE_BRICK_ORANGE_SLAB = BLOCKS.register("force_brick_orange_slab", () -> new SlabBlock(AbstractBlock.Properties.from(FORCE_BRICK_ORANGE.get())));
    public static final RegistryObject<Block> FORCE_BRICK_LIGHT_BLUE_SLAB = BLOCKS.register("force_brick_light_blue_slab", () -> new SlabBlock(AbstractBlock.Properties.from(FORCE_BRICK_LIGHT_BLUE.get())));
    public static final RegistryObject<Block> FORCE_BRICK_MAGENTA_SLAB = BLOCKS.register("force_brick_magenta_slab", () -> new SlabBlock(AbstractBlock.Properties.from(FORCE_BRICK_MAGENTA.get())));
    public static final RegistryObject<Block> FORCE_BRICK_PINK_SLAB = BLOCKS.register("force_brick_pink_slab", () -> new SlabBlock(AbstractBlock.Properties.from(FORCE_BRICK_PINK.get())));
    public static final RegistryObject<Block> FORCE_BRICK_LIGHT_GRAY_SLAB = BLOCKS.register("force_brick_light_gray_slab", () -> new SlabBlock(AbstractBlock.Properties.from(FORCE_BRICK_LIGHT_GRAY.get())));
    public static final RegistryObject<Block> FORCE_BRICK_LIME_SLAB = BLOCKS.register("force_brick_lime_slab", () -> new SlabBlock(AbstractBlock.Properties.from(FORCE_BRICK_LIME.get())));
    public static final RegistryObject<Block> FORCE_BRICK_CYAN_SLAB = BLOCKS.register("force_brick_cyan_slab", () -> new SlabBlock(AbstractBlock.Properties.from(FORCE_BRICK_CYAN.get())));
    public static final RegistryObject<Block> FORCE_BRICK_PURPLE_SLAB = BLOCKS.register("force_brick_purple_slab", () -> new SlabBlock(AbstractBlock.Properties.from(FORCE_BRICK_PURPLE.get())));
    public static final RegistryObject<Block> FORCE_BRICK_GRAY_SLAB = BLOCKS.register("force_brick_gray_slab", () -> new SlabBlock(AbstractBlock.Properties.from(FORCE_BRICK_GRAY.get())));
    public static final RegistryObject<Block> FORCE_BRICK_SLAB = BLOCKS.register("force_brick_slab", () -> new SlabBlock(AbstractBlock.Properties.from(FORCE_BRICK.get())));

    //Torches
    public static final RegistryObject<Block> FORCE_TORCH = BLOCKS.register("force_torch", () -> new TorchBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS, DyeColor.ORANGE).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
    public static final RegistryObject<Block> FORCE_RED_TORCH = BLOCKS.register("force_red_torch", () -> new TorchBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS, DyeColor.RED).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
    public static final RegistryObject<Block> FORCE_YELLOW_TORCH = BLOCKS.register("force_yellow_torch", () -> new TorchBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS, DyeColor.YELLOW).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
    public static final RegistryObject<Block> FORCE_GREEN_TORCH = BLOCKS.register("force_green_torch", () -> new TorchBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS, DyeColor.GREEN).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
    public static final RegistryObject<Block> FORCE_BLUE_TORCH = BLOCKS.register("force_blue_torch", () -> new TorchBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS, DyeColor.BLUE).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
    public static final RegistryObject<Block> FORCE_WHITE_TORCH = BLOCKS.register("force_white_torch", () -> new TorchBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS, DyeColor.WHITE).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
    public static final RegistryObject<Block> FORCE_BLACK_TORCH = BLOCKS.register("force_black_torch", () -> new TorchBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS, DyeColor.BLACK).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
    public static final RegistryObject<Block> FORCE_BROWN_TORCH = BLOCKS.register("force_brown_torch", () -> new TorchBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS, DyeColor.BROWN).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
    public static final RegistryObject<Block> FORCE_LIGHT_BLUE_TORCH = BLOCKS.register("force_light_blue_torch", () -> new TorchBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS, DyeColor.LIGHT_BLUE).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
    public static final RegistryObject<Block> FORCE_MAGENTA_TORCH = BLOCKS.register("force_magenta_torch", () -> new TorchBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS, DyeColor.MAGENTA).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
    public static final RegistryObject<Block> FORCE_PINK_TORCH = BLOCKS.register("force_pink_torch", () -> new TorchBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS, DyeColor.PINK).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
    public static final RegistryObject<Block> FORCE_LIGHT_GRAY_TORCH = BLOCKS.register("force_light_gray_torch", () -> new TorchBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS, DyeColor.LIGHT_GRAY).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
    public static final RegistryObject<Block> FORCE_LIME_TORCH = BLOCKS.register("force_lime_torch", () -> new TorchBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS, DyeColor.LIME).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
    public static final RegistryObject<Block> FORCE_CYAN_TORCH = BLOCKS.register("force_cyan_torch", () -> new TorchBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS, DyeColor.CYAN).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
    public static final RegistryObject<Block> FORCE_PURPLE_TORCH = BLOCKS.register("force_purple_torch", () -> new TorchBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS, DyeColor.PURPLE).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));
    public static final RegistryObject<Block> FORCE_GRAY_TORCH = BLOCKS.register("force_gray_torch", () -> new TorchBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS, DyeColor.GRAY).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel((state) -> 15).sound(SoundType.WOOD), ParticleTypes.FLAME));

    public static final RegistryObject<Block> WALL_FORCE_TORCH = BLOCKS.register("wall_force_torch", () -> new WallTorchBlock(AbstractBlock.Properties.from(FORCE_TORCH.get()), ParticleTypes.FLAME));
    public static final RegistryObject<Block> WALL_FORCE_RED_TORCH = BLOCKS.register("wall_force_red_torch", () -> new WallTorchBlock(AbstractBlock.Properties.from(FORCE_RED_TORCH.get()), ParticleTypes.FLAME));
    public static final RegistryObject<Block> WALL_FORCE_YELLOW_TORCH = BLOCKS.register("wall_force_yellow_torch", () -> new WallTorchBlock(AbstractBlock.Properties.from(FORCE_YELLOW_TORCH.get()), ParticleTypes.FLAME));
    public static final RegistryObject<Block> WALL_FORCE_GREEN_TORCH = BLOCKS.register("wall_force_green_torch", () -> new WallTorchBlock(AbstractBlock.Properties.from(FORCE_GREEN_TORCH.get()), ParticleTypes.FLAME));
    public static final RegistryObject<Block> WALL_FORCE_BLUE_TORCH = BLOCKS.register("wall_force_blue_torch", () -> new WallTorchBlock(AbstractBlock.Properties.from(FORCE_BLUE_TORCH.get()), ParticleTypes.FLAME));
    public static final RegistryObject<Block> WALL_FORCE_WHITE_TORCH = BLOCKS.register("wall_force_white_torch", () -> new WallTorchBlock(AbstractBlock.Properties.from(FORCE_WHITE_TORCH.get()), ParticleTypes.FLAME));
    public static final RegistryObject<Block> WALL_FORCE_BLACK_TORCH = BLOCKS.register("wall_force_black_torch", () -> new WallTorchBlock(AbstractBlock.Properties.from(FORCE_BLACK_TORCH.get()), ParticleTypes.FLAME));
    public static final RegistryObject<Block> WALL_FORCE_BROWN_TORCH = BLOCKS.register("wall_force_brown_torch", () -> new WallTorchBlock(AbstractBlock.Properties.from(FORCE_BROWN_TORCH.get()), ParticleTypes.FLAME));
    public static final RegistryObject<Block> WALL_FORCE_LIGHT_BLUE_TORCH = BLOCKS.register("wall_force_light_blue_torch", () -> new WallTorchBlock(AbstractBlock.Properties.from(FORCE_LIGHT_BLUE_TORCH.get()), ParticleTypes.FLAME));
    public static final RegistryObject<Block> WALL_FORCE_MAGENTA_TORCH = BLOCKS.register("wall_force_magenta_torch", () -> new WallTorchBlock(AbstractBlock.Properties.from(FORCE_MAGENTA_TORCH.get()), ParticleTypes.FLAME));
    public static final RegistryObject<Block> WALL_FORCE_PINK_TORCH = BLOCKS.register("wall_force_pink_torch", () -> new WallTorchBlock(AbstractBlock.Properties.from(FORCE_PINK_TORCH.get()), ParticleTypes.FLAME));
    public static final RegistryObject<Block> WALL_FORCE_LIGHT_GRAY_TORCH = BLOCKS.register("wall_force_light_gray_torch", () -> new WallTorchBlock(AbstractBlock.Properties.from(FORCE_LIGHT_GRAY_TORCH.get()), ParticleTypes.FLAME));
    public static final RegistryObject<Block> WALL_FORCE_LIME_TORCH = BLOCKS.register("wall_force_lime_torch", () -> new WallTorchBlock(AbstractBlock.Properties.from(FORCE_LIME_TORCH.get()), ParticleTypes.FLAME));
    public static final RegistryObject<Block> WALL_FORCE_CYAN_TORCH = BLOCKS.register("wall_force_cyan_torch", () -> new WallTorchBlock(AbstractBlock.Properties.from(FORCE_CYAN_TORCH.get()), ParticleTypes.FLAME));
    public static final RegistryObject<Block> WALL_FORCE_PURPLE_TORCH = BLOCKS.register("wall_force_purple_torch", () -> new WallTorchBlock(AbstractBlock.Properties.from(FORCE_PURPLE_TORCH.get()), ParticleTypes.FLAME));
    public static final RegistryObject<Block> WALL_FORCE_GRAY_TORCH = BLOCKS.register("wall_force_gray_torch", () -> new WallTorchBlock(AbstractBlock.Properties.from(FORCE_GRAY_TORCH.get()), ParticleTypes.FLAME));

    public static final RegistryObject<Block> TIME_TORCH = BLOCKS.register("time_torch", () ->
            new TimeTorchBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel((state) -> 15)
                    .sound(SoundType.WOOD), ParticleTypes.FLAME));
    public static final RegistryObject<Block> WALL_TIME_TORCH = BLOCKS.register("wall_time_torch", () ->
            new WallTimeTorchBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel((state) -> 15)
                    .sound(SoundType.WOOD), ParticleTypes.FLAME));


    /**
     * Items
     */
    public static final RegistryObject<Item> RECOVERY_HEART = ITEMS.register("recovery_heart", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_GEM = ITEMS.register("force_gem", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_INGOT = ITEMS.register("force_ingot", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_NUGGET = ITEMS.register("force_nugget", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_STICK = ITEMS.register("force_stick", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> UPGRADE_TOME = ITEMS.register("upgrade_tome", () -> new UpgradeTomeItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_PACK_UPGRADE = ITEMS.register("force_pack_upgrade", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> UPGRADE_CORE = ITEMS.register("upgrade_core", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> EXPERIENCE_CORE = ITEMS.register("experience_core", () -> new UpgradeCoreItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FREEZING_CORE = ITEMS.register("freezing_core", () -> new UpgradeCoreItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> GRINDING_CORE = ITEMS.register("grinding_core", () -> new UpgradeCoreItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> HEAT_CORE = ITEMS.register("heat_core", () -> new UpgradeCoreItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> SPEED_CORE = ITEMS.register("speed_core", () -> new UpgradeCoreItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> RED_CHU_JELLY = ITEMS.register("red_chu_jelly", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> GREEN_CHU_JELLY = ITEMS.register("green_chu_jelly", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> BLUE_CHU_JELLY = ITEMS.register("blue_chu_jelly", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> GOLD_CHU_JELLY = ITEMS.register("gold_chu_jelly", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> PILE_OF_GUNPOWDER = ITEMS.register("pile_of_gunpowder", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));

    public static final RegistryObject<Item> FORTUNE_COOKIE = ITEMS.register("fortune_cookie", () ->
            new CustomFoodItem(itemBuilder().food(ForceFoods.FORTUNE_COOKIE).maxStackSize(1).group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> SOUL_WAFER  = ITEMS.register("soul_wafer", () ->
            new CustomFoodItem(itemBuilder().food(ForceFoods.SOUL_WAFER).group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> RAW_BACON  = ITEMS.register("raw_bacon", () ->
            new CustomFoodItem(itemBuilder().food(ForceFoods.BACON).group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> COOKED_BACON  = ITEMS.register("cooked_bacon", () ->
            new CustomFoodItem(itemBuilder().food(ForceFoods.COOKED_BACON).group(ForceCraft.creativeTab)));

    public static final RegistryObject<Item> FORCE_HELMET = ITEMS.register("force_helmet", () ->
            new CustomArmorItem(ModArmor.FORCE_ARMOR, EquipmentSlotType.HEAD, itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_CHEST = ITEMS.register("force_chest", () ->
            new CustomArmorItem(ModArmor.FORCE_ARMOR, EquipmentSlotType.CHEST, itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_LEGS = ITEMS.register("force_legs", () ->
            new CustomArmorItem(ModArmor.FORCE_ARMOR, EquipmentSlotType.LEGS, itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BOOTS = ITEMS.register("force_boots", () ->
            new CustomArmorItem(ModArmor.FORCE_ARMOR, EquipmentSlotType.FEET, itemBuilder().group(ForceCraft.creativeTab)));

    public static final RegistryObject<Item> FORCE_ROD = ITEMS.register("force_rod", () -> new ForceRodItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_WRENCH = ITEMS.register("force_wrench", () -> new ForceWrenchItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> GOLDEN_POWER_SOURCE = ITEMS.register("golden_power_source", () -> new GoldenPowerSourceItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> CLAW = ITEMS.register("claw", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORTUNE =ITEMS.register("fortune", () ->  new FortuneItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_GEAR = ITEMS.register("force_gear", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> SNOW_COOKIE = ITEMS.register("snow_cookie", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_PACK = ITEMS.register("force_pack", () -> new ForcePackItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BELT = ITEMS.register("force_belt", () -> new ForceBeltItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> BOTTLED_WITHER = ITEMS.register("bottled_wither", () -> new BottledWitherItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> INERT_CORE = ITEMS.register("inert_core", () -> new InertCoreItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> BACONATOR = ITEMS.register("baconator", () -> new BaconatorItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> SPOILS_BAG = ITEMS.register("spoils_bag", () -> new SpoilsBagItem(itemBuilder().group(ForceCraft.creativeTab), 1));
    public static final RegistryObject<Item> SPOILS_BAG_T2 = ITEMS.register("spoils_bag_t2", () -> new SpoilsBagItem(itemBuilder().group(ForceCraft.creativeTab), 2));
    public static final RegistryObject<Item> SPOILS_BAG_T3 = ITEMS.register("spoils_bag_t3", () -> new SpoilsBagItem(itemBuilder().group(ForceCraft.creativeTab), 3));
    public static final RegistryObject<Item> LIFE_CARD = ITEMS.register("life_card", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> DARKNESS_CARD = ITEMS.register("darkness_card", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> UNDEATH_CARD = ITEMS.register("undeath_card", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> TREASURE_CORE = ITEMS.register("treasure_core", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_ARROW = ITEMS.register("force_arrow", () -> new ForceArrowItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_FLASK = ITEMS.register("force_flask", () -> new ForceFlaskItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> MILK_FORCE_FLASK = ITEMS.register("milk_force_flask", () -> new MilkFlaskItem(itemBuilder().containerItem(FORCE_FLASK.get()).group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_FILLED_FORCE_FLASK = ITEMS.register("force_filled_force_flask", () -> new ForceFilledForceFlask(itemBuilder().containerItem(FORCE_FLASK.get()).group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> RED_POTION = ITEMS.register("red_potion", () -> new RedPotionItem(itemBuilder().containerItem(FORCE_FLASK.get()).group(ForceCraft.creativeTab)));

    //Tools
    public static final RegistryObject<Item> FORCE_PICKAXE = ITEMS.register("force_pickaxe", () -> new ForcePickaxeItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_AXE = ITEMS.register("force_axe", () -> new ForceAxeItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_SWORD = ITEMS.register("force_sword", () -> new ForceSwordItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_SHOVEL = ITEMS.register("force_shovel", () -> new ForceShovelItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_SHEARS = ITEMS.register("force_shears", () -> new ForceShearsItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BOW = ITEMS.register("force_bow", () -> new ForceBowItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_MITT = ITEMS.register("force_mitt", () -> new ForceMittItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> MAGNET_GLOVE = ITEMS.register("magnet_glove", () -> new MagnetGloveItem(itemBuilder().group(ForceCraft.creativeTab)));

    //Bucket
    public static final RegistryObject<Item> BUCKET_FLUID_FORCE = ITEMS.register("force_bucket", () ->
            new ForceFluidBucketItem(itemBuilder().containerItem(Items.BUCKET).maxStackSize(1).group(ForceCraft.creativeTab), () -> ForceFluids.FORCE_FLUID_SOURCE.get()));

    //Experience Tome
    public static final RegistryObject<Item> EXPERIENCE_TOME = ITEMS.register("experience_tome", () -> new ExperienceTomeItem(itemBuilder().group(ForceCraft.creativeTab)));

    //Spawn eggs
    public static final RegistryObject<Item> RED_CHU_CHU_SPAWN_EGG = ITEMS.register("red_chu_chu_spawn_egg", () -> new CustomSpawnEggItem(() -> ForceEntities.RED_CHU_CHU.get(), 15674931, 14483465, itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> GREEN_CHU_CHU_SPAWN_EGG = ITEMS.register("green_chu_chu_spawn_egg", () -> new CustomSpawnEggItem(() -> ForceEntities.GREEN_CHU_CHU.get(), 6539935, 3190138, itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> BLUE_CHU_CHU_SPAWN_EGG = ITEMS.register("blue_chu_chu_spawn_egg", () -> new CustomSpawnEggItem(() -> ForceEntities.BLUE_CHU_CHU.get(), 2450423, 16606, itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> GOLD_CHU_CHU_SPAWN_EGG = ITEMS.register("gold_chu_chu_spawn_egg", () -> new CustomSpawnEggItem(() -> ForceEntities.GOLD_CHU_CHU.get(), 14921786, 13670157, itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> COLD_CHICKEN_SPAWN_EGG = ITEMS.register("cold_chicken_spawn_egg", () -> new CustomSpawnEggItem(() -> ForceEntities.COLD_CHICKEN.get(), 10592673, 16711680, itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> COLD_COW_SPAWN_EGG = ITEMS.register("cold_cow_spawn_egg", () -> new CustomSpawnEggItem(() -> ForceEntities.COLD_COW.get(), 4470310, 10592673, itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> COLD_PIG_SPAWN_EGG = ITEMS.register("cold_pig_spawn_egg", () -> new CustomSpawnEggItem(() -> ForceEntities.COLD_PIG.get(), 15771042, 14377823, itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FAIRY_SPAWN_EGG = ITEMS.register("fairy_spawn_egg", () -> new CustomSpawnEggItem(() -> ForceEntities.FAIRY.get(), 11395062, 12970232, itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> CREEPER_TOT_SPAWN_EGG = ITEMS.register("creeper_tot_spawn_egg", () -> new CustomSpawnEggItem(() -> ForceEntities.CREEPER_TOT.get(), 894731, 0, itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> ENDER_TOT_SPAWN_EGG = ITEMS.register("ender_tot_spawn_egg", () -> new CustomSpawnEggItem(() -> ForceEntities.ENDER_TOT.get(), 1447446, 0, itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> ANGRY_ENDERMAN_SPAWN_EGG = ITEMS.register("angry_enderman_spawn_egg", () -> new CustomSpawnEggItem(() -> ForceEntities.ANGRY_ENDERMAN.get(), 1447446, 0, itemBuilder().group(ForceCraft.creativeTab)));


    /**
     * Tile Entities
     */
    public static final RegistryObject<TileEntityType<InfuserTileEntity>> INFUSER_TILE = TILES.register("infuser", () -> TileEntityType.Builder.create(() ->
            new InfuserTileEntity(), ForceRegistry.INFUSER.get()).build(null));

    public static final RegistryObject<TileEntityType<ForceFurnaceTileEntity>> FURNACE_TILE = TILES.register("furnace", () -> TileEntityType.Builder.create(() ->
            new ForceFurnaceTileEntity(), ForceRegistry.FORCE_FURNACE.get()).build(null));

    public static final RegistryObject<TileEntityType<TimeTorchTileEntity>> TIME_TORCH_TILE = TILES.register("time_torch", () -> TileEntityType.Builder.create(() ->
            new TimeTorchTileEntity(), ForceRegistry.TIME_TORCH.get(), ForceRegistry.WALL_TIME_TORCH.get()).build(null));

    /**
     * Block items
     */
    public static final RegistryObject<Item> POWER_ORE_ITEM = ITEMS.register("power_ore", () -> new BlockItem(ForceRegistry.POWER_ORE.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_SAPLING_ITEM = ITEMS.register("force_sapling", () -> new BlockItem(ForceRegistry.FORCE_SAPLING.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_LOG_ITEM = ITEMS.register("force_log", () -> new BlockItem(ForceRegistry.FORCE_LOG.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_WOOD_ITEM = ITEMS.register("force_wood", () -> new BlockItem(ForceRegistry.FORCE_WOOD.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_LEAVES_ITEM = ITEMS.register("force_leaves", () -> new BlockItem(ForceRegistry.FORCE_LEAVES.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> INFUSER_ITEM = ITEMS.register("infuser", () -> new BlockItem(ForceRegistry.INFUSER.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_PLANKS_ITEM = ITEMS.register("force_planks", () -> new BlockItem(ForceRegistry.FORCE_PLANKS.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_PLANK_STAIRS_ITEM = ITEMS.register("force_plank_stairs", () -> new BlockItem(ForceRegistry.FORCE_PLANK_STAIRS.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_PLANK_SLAB_ITEM = ITEMS.register("force_plank_slab", () -> new BlockItem(ForceRegistry.FORCE_PLANK_SLAB.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_FURNACE_ITEM = ITEMS.register("force_furnace", () -> new BlockItem(ForceRegistry.FORCE_FURNACE.get(), itemBuilder().group(ForceCraft.creativeTab)));

    public static final RegistryObject<Item> FORCE_BRICK_RED_ITEM = ITEMS.register("force_brick_red", () -> new BlockItem(FORCE_BRICK_RED.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_YELLOW_ITEM = ITEMS.register("force_brick_yellow", () -> new BlockItem(FORCE_BRICK_YELLOW.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_GREEN_ITEM = ITEMS.register("force_brick_green", () -> new BlockItem(FORCE_BRICK_GREEN.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_BLUE_ITEM = ITEMS.register("force_brick_blue", () -> new BlockItem(FORCE_BRICK_BLUE.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_WHITE_ITEM = ITEMS.register("force_brick_white", () -> new BlockItem(FORCE_BRICK_WHITE.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_BLACK_ITEM = ITEMS.register("force_brick_black", () -> new BlockItem(FORCE_BRICK_BLACK.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_BROWN_ITEM = ITEMS.register("force_brick_brown", () -> new BlockItem(FORCE_BRICK_BROWN.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_ORANGE_ITEM = ITEMS.register("force_brick_orange", () -> new BlockItem(FORCE_BRICK_ORANGE.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_LIGHT_BLUE_ITEM = ITEMS.register("force_brick_light_blue", () -> new BlockItem(FORCE_BRICK_LIGHT_BLUE.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_MAGENTA_ITEM = ITEMS.register("force_brick_magenta", () -> new BlockItem(FORCE_BRICK_MAGENTA.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_PINK_ITEM = ITEMS.register("force_brick_pink", () -> new BlockItem(FORCE_BRICK_PINK.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_LIGHT_GRAY_ITEM = ITEMS.register("force_brick_light_gray", () -> new BlockItem(FORCE_BRICK_LIGHT_GRAY.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_LIME_ITEM = ITEMS.register("force_brick_lime", () -> new BlockItem(FORCE_BRICK_LIME.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_CYAN_ITEM = ITEMS.register("force_brick_cyan", () -> new BlockItem(FORCE_BRICK_CYAN.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_PURPLE_ITEM = ITEMS.register("force_brick_purple", () -> new BlockItem(FORCE_BRICK_PURPLE.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_GRAY_ITEM = ITEMS.register("force_brick_gray", () -> new BlockItem(FORCE_BRICK_GRAY.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_ITEM = ITEMS.register("force_brick", () -> new BlockItem(FORCE_BRICK.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_RED_STAIRS_ITEM = ITEMS.register("force_brick_red_stairs", () -> new BlockItem(FORCE_BRICK_RED_STAIRS.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_YELLOW_STAIRS_ITEM = ITEMS.register("force_brick_yellow_stairs", () -> new BlockItem(FORCE_BRICK_YELLOW_STAIRS.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_GREEN_STAIRS_ITEM = ITEMS.register("force_brick_green_stairs", () -> new BlockItem(FORCE_BRICK_GREEN_STAIRS.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_BLUE_STAIRS_ITEM = ITEMS.register("force_brick_blue_stairs", () -> new BlockItem(FORCE_BRICK_BLUE_STAIRS.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_WHITE_STAIRS_ITEM = ITEMS.register("force_brick_white_stairs", () -> new BlockItem(FORCE_BRICK_WHITE_STAIRS.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_BLACK_STAIRS_ITEM = ITEMS.register("force_brick_black_stairs", () -> new BlockItem(FORCE_BRICK_BLACK_STAIRS.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_BROWN_STAIRS_ITEM = ITEMS.register("force_brick_brown_stairs", () -> new BlockItem(FORCE_BRICK_BROWN_STAIRS.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_ORANGE_STAIRS_ITEM = ITEMS.register("force_brick_orange_stairs", () -> new BlockItem(FORCE_BRICK_ORANGE_STAIRS.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_LIGHT_BLUE_STAIRS_ITEM = ITEMS.register("force_brick_light_blue_stairs", () -> new BlockItem(FORCE_BRICK_LIGHT_BLUE_STAIRS.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_MAGENTA_STAIRS_ITEM = ITEMS.register("force_brick_magenta_stairs", () -> new BlockItem(FORCE_BRICK_MAGENTA_STAIRS.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_PINK_STAIRS_ITEM = ITEMS.register("force_brick_pink_stairs", () -> new BlockItem(FORCE_BRICK_PINK_STAIRS.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_LIGHT_GRAY_STAIRS_ITEM = ITEMS.register("force_brick_light_gray_stairs", () -> new BlockItem(FORCE_BRICK_LIGHT_GRAY_STAIRS.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_LIME_STAIRS_ITEM = ITEMS.register("force_brick_lime_stairs", () -> new BlockItem(FORCE_BRICK_LIME_STAIRS.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_CYAN_STAIRS_ITEM = ITEMS.register("force_brick_cyan_stairs", () -> new BlockItem(FORCE_BRICK_CYAN_STAIRS.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_PURPLE_STAIRS_ITEM = ITEMS.register("force_brick_purple_stairs", () -> new BlockItem(FORCE_BRICK_PURPLE_STAIRS.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_GRAY_STAIRS_ITEM = ITEMS.register("force_brick_gray_stairs", () -> new BlockItem(FORCE_BRICK_GRAY_STAIRS.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_STAIRS_ITEM = ITEMS.register("force_brick_stairs", () -> new BlockItem(FORCE_BRICK_STAIRS.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_RED_SLAB_ITEM = ITEMS.register("force_brick_red_slab", () -> new BlockItem(FORCE_BRICK_RED_SLAB.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_YELLOW_SLAB_ITEM = ITEMS.register("force_brick_yellow_slab", () -> new BlockItem(FORCE_BRICK_YELLOW_SLAB.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_GREEN_SLAB_ITEM = ITEMS.register("force_brick_green_slab", () -> new BlockItem(FORCE_BRICK_GREEN_SLAB.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_BLUE_SLAB_ITEM = ITEMS.register("force_brick_blue_slab", () -> new BlockItem(FORCE_BRICK_BLUE_SLAB.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_WHITE_SLAB_ITEM = ITEMS.register("force_brick_white_slab", () -> new BlockItem(FORCE_BRICK_WHITE_SLAB.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_BLACK_SLAB_ITEM = ITEMS.register("force_brick_black_slab", () -> new BlockItem(FORCE_BRICK_BLACK_SLAB.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_BROWN_SLAB_ITEM = ITEMS.register("force_brick_brown_slab", () -> new BlockItem(FORCE_BRICK_BROWN_SLAB.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_ORANGE_SLAB_ITEM = ITEMS.register("force_brick_orange_slab", () -> new BlockItem(FORCE_BRICK_ORANGE_SLAB.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_LIGHT_BLUE_SLAB_ITEM = ITEMS.register("force_brick_light_blue_slab", () -> new BlockItem(FORCE_BRICK_LIGHT_BLUE_SLAB.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_MAGENTA_SLAB_ITEM = ITEMS.register("force_brick_magenta_slab", () -> new BlockItem(FORCE_BRICK_MAGENTA_SLAB.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_PINK_SLAB_ITEM = ITEMS.register("force_brick_pink_slab", () -> new BlockItem(FORCE_BRICK_PINK_SLAB.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_LIGHT_GRAY_SLAB_ITEM = ITEMS.register("force_brick_light_gray_slab", () -> new BlockItem(FORCE_BRICK_LIGHT_GRAY_SLAB.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_LIME_SLAB_ITEM = ITEMS.register("force_brick_lime_slab", () -> new BlockItem(FORCE_BRICK_LIME_SLAB.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_CYAN_SLAB_ITEM = ITEMS.register("force_brick_cyan_slab", () -> new BlockItem(FORCE_BRICK_CYAN_SLAB.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_PURPLE_SLAB_ITEM = ITEMS.register("force_brick_purple_slab", () -> new BlockItem(FORCE_BRICK_PURPLE_SLAB.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_GRAY_SLAB_ITEM = ITEMS.register("force_brick_gray_slab", () -> new BlockItem(FORCE_BRICK_GRAY_SLAB.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BRICK_SLAB_ITEM = ITEMS.register("force_brick_slab", () -> new BlockItem(FORCE_BRICK_SLAB.get(), itemBuilder().group(ForceCraft.creativeTab)));

    public static final RegistryObject<Item> FORCE_TORCH_ITEM = ITEMS.register("force_torch", () -> new WallOrFloorItem(FORCE_TORCH.get(), WALL_FORCE_TORCH.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_RED_TORCH_ITEM = ITEMS.register("force_red_torch", () -> new WallOrFloorItem(FORCE_RED_TORCH.get(), WALL_FORCE_RED_TORCH.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_YELLOW_TORCH_ITEM = ITEMS.register("force_yellow_torch", () -> new WallOrFloorItem(FORCE_YELLOW_TORCH.get(), WALL_FORCE_YELLOW_TORCH.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_GREEN_TORCH_ITEM = ITEMS.register("force_green_torch", () -> new WallOrFloorItem(FORCE_GREEN_TORCH.get(), WALL_FORCE_GREEN_TORCH.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BLUE_TORCH_ITEM = ITEMS.register("force_blue_torch", () -> new WallOrFloorItem(FORCE_BLUE_TORCH.get(), WALL_FORCE_BLUE_TORCH.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_WHITE_TORCH_ITEM = ITEMS.register("force_white_torch", () -> new WallOrFloorItem(FORCE_WHITE_TORCH.get(), WALL_FORCE_WHITE_TORCH.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BLACK_TORCH_ITEM = ITEMS.register("force_black_torch", () -> new WallOrFloorItem(FORCE_BLACK_TORCH.get(), WALL_FORCE_BLACK_TORCH.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BROWN_TORCH_ITEM = ITEMS.register("force_brown_torch", () -> new WallOrFloorItem(FORCE_BROWN_TORCH.get(), WALL_FORCE_BROWN_TORCH.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_LIGHT_BLUE_TORCH_ITEM = ITEMS.register("force_light_blue_torch", () -> new WallOrFloorItem(FORCE_LIGHT_BLUE_TORCH.get(), WALL_FORCE_LIGHT_BLUE_TORCH.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_MAGENTA_TORCH_ITEM = ITEMS.register("force_magenta_torch", () -> new WallOrFloorItem(FORCE_MAGENTA_TORCH.get(), WALL_FORCE_MAGENTA_TORCH.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_PINK_TORCH_ITEM = ITEMS.register("force_pink_torch", () -> new WallOrFloorItem(FORCE_PINK_TORCH.get(), WALL_FORCE_PINK_TORCH.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_LIGHT_GRAY_TORCH_ITEM = ITEMS.register("force_light_gray_torch", () -> new WallOrFloorItem(FORCE_LIGHT_GRAY_TORCH.get(), WALL_FORCE_LIGHT_GRAY_TORCH.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_LIME_TORCH_ITEM = ITEMS.register("force_lime_torch", () -> new WallOrFloorItem(FORCE_LIME_TORCH.get(), WALL_FORCE_LIME_TORCH.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_CYAN_TORCH_ITEM = ITEMS.register("force_cyan_torch", () -> new WallOrFloorItem(FORCE_CYAN_TORCH.get(), WALL_FORCE_CYAN_TORCH.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_PURPLE_TORCH_ITEM = ITEMS.register("force_purple_torch", () -> new WallOrFloorItem(FORCE_PURPLE_TORCH.get(), WALL_FORCE_PURPLE_TORCH.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_GRAY_TORCH_ITEM = ITEMS.register("force_gray_torch", () -> new WallOrFloorItem(FORCE_GRAY_TORCH.get(), WALL_FORCE_GRAY_TORCH.get(), itemBuilder().group(ForceCraft.creativeTab)));

    public static final RegistryObject<Item> TIME_TORCH_ITEM = ITEMS.register("time_torch", () -> new WallOrFloorItem(TIME_TORCH.get(), WALL_TIME_TORCH.get(), itemBuilder().group(ForceCraft.creativeTab)));

    private static Item.Properties itemBuilder() {
        return new Item.Properties();
    }
}
