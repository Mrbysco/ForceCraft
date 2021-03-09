package mrbysco.forcecraft.registry;

import mrbysco.forcecraft.ForceCraft;
import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.blocks.BurnableBlock;
import mrbysco.forcecraft.blocks.ForceFluidBlock;
import mrbysco.forcecraft.blocks.ForceFurnaceBlock;
import mrbysco.forcecraft.blocks.ForceLeavesBlock;
import mrbysco.forcecraft.blocks.ForceLogBlock;
import mrbysco.forcecraft.blocks.InfuserBlock;
import mrbysco.forcecraft.blocks.torch.TimeTorchBlock;
import mrbysco.forcecraft.blocks.torch.WallTimeTorchBlock;
import mrbysco.forcecraft.blocks.tree.ForceTree;
import mrbysco.forcecraft.items.BaseItem;
import mrbysco.forcecraft.items.BottledWitherItem;
import mrbysco.forcecraft.items.CustomArmorItem;
import mrbysco.forcecraft.items.CustomFoodItem;
import mrbysco.forcecraft.items.ExperienceTomeItem;
import mrbysco.forcecraft.items.ForceBeltItem;
import mrbysco.forcecraft.items.ForceFluidBucketItem;
import mrbysco.forcecraft.items.ForcePackItem;
import mrbysco.forcecraft.items.FortuneItem;
import mrbysco.forcecraft.items.GoldenPowerSourceItem;
import mrbysco.forcecraft.items.nonburnable.InertCoreItem;
import mrbysco.forcecraft.items.tools.ForceAxeItem;
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
import mrbysco.forcecraft.tiles.InfuserTileEntity;
import mrbysco.forcecraft.tiles.TimeTorchTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SoundType;
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
            new BurnableBlock(AbstractBlock.Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));


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
    public static final RegistryObject<Block> FORCE_BRICK_RED = BLOCKS.register("force_brick_red", () ->
            new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.RED).hardnessAndResistance(50.0F, 200.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_YELLOW = BLOCKS.register("force_brick_yellow", () ->
            new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.YELLOW).hardnessAndResistance(50.0F, 200.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_GREEN = BLOCKS.register("force_brick_green", () ->
            new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.GREEN).hardnessAndResistance(50.0F, 200.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_BLUE = BLOCKS.register("force_brick_blue", () ->
            new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.BLUE).hardnessAndResistance(50.0F, 200.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_WHITE = BLOCKS.register("force_brick_white", () ->
            new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.WHITE).hardnessAndResistance(50.0F, 200.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_BLACK = BLOCKS.register("force_brick_black", () ->
            new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.BLACK).hardnessAndResistance(50.0F, 200.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_BROWN = BLOCKS.register("force_brick_brown", () ->
            new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.BROWN).hardnessAndResistance(50.0F, 200.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_ORANGE = BLOCKS.register("force_brick_orange", () ->
            new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.ORANGE).hardnessAndResistance(50.0F, 200.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_LIGHT_BLUE = BLOCKS.register("force_brick_light_blue", () ->
            new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.LIGHT_BLUE).hardnessAndResistance(50.0F, 200.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_MAGENTA = BLOCKS.register("force_brick_magenta", () ->
            new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.MAGENTA).hardnessAndResistance(50.0F, 200.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_PINK = BLOCKS.register("force_brick_pink", () ->
            new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.PINK).hardnessAndResistance(50.0F, 200.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_LIGHT_GRAY = BLOCKS.register("force_brick_light_gray", () ->
            new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.LIGHT_GRAY).hardnessAndResistance(50.0F, 200.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_LIME = BLOCKS.register("force_brick_lime", () ->
            new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.LIME).hardnessAndResistance(50.0F, 200.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_CYAN = BLOCKS.register("force_brick_cyan", () ->
            new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.CYAN).hardnessAndResistance(50.0F, 200.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_PURPLE = BLOCKS.register("force_brick_purple", () ->
            new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.PURPLE).hardnessAndResistance(50.0F, 200.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK_GRAY = BLOCKS.register("force_brick_gray", () ->
            new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.GRAY).hardnessAndResistance(50.0F, 200.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> FORCE_BRICK = BLOCKS.register("force_brick", () ->
            new Block(AbstractBlock.Properties.create(Material.ROCK, DyeColor.YELLOW).hardnessAndResistance(50.0F, 200.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));

    //Torches
    public static final RegistryObject<Block> FORCE_TORCH = BLOCKS.register("force_torch", () ->
            new TorchBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel((state) -> 15)
                    .sound(SoundType.WOOD), ParticleTypes.FLAME));
    public static final RegistryObject<Block> WALL_FORCE_TORCH = BLOCKS.register("wall_force_torch", () ->
            new WallTorchBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel((state) -> 15)
                    .sound(SoundType.WOOD), ParticleTypes.FLAME));

    public static final RegistryObject<Block> TIME_TORCH = BLOCKS.register("time_torch", () ->
            new TimeTorchBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel((state) -> 15)
                    .sound(SoundType.WOOD), ParticleTypes.FLAME));
    public static final RegistryObject<Block> WALL_TIME_TORCH = BLOCKS.register("wall_time_torch", () ->
            new WallTimeTorchBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel((state) -> 15)
                    .sound(SoundType.WOOD), ParticleTypes.FLAME));


    /**
     * Items
     */
    public static final RegistryObject<Item> FORCE_GEM = ITEMS.register("force_gem", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_INGOT = ITEMS.register("force_ingot", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_NUGGET = ITEMS.register("force_nugget", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_STICK = ITEMS.register("force_stick", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));

    public static final RegistryObject<Item> FORTUNE_COOKIE = ITEMS.register("fortune_cookie", () ->
            new CustomFoodItem(itemBuilder().food(ForceFoods.FORTUNE_COOKIE).maxStackSize(1).group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> SOUL_WAFER  = ITEMS.register("soul_wafer", () ->
            new CustomFoodItem(itemBuilder().food(ForceFoods.SOUL_WAFER).group(ForceCraft.creativeTab)));

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
    public static final RegistryObject<Item> GOLDEN_POWER_SOURCE = ITEMS.register("golden_power_source", () ->
            new GoldenPowerSourceItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> CLAW = ITEMS.register("claw", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORTUNE =ITEMS.register("fortune", () ->  new FortuneItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_GEAR = ITEMS.register("force_gear", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> SNOW_COOKIE = ITEMS.register("snow_cookie", () -> new BaseItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_PACK = ITEMS.register("force_pack", () -> new ForcePackItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_BELT = ITEMS.register("force_belt", () -> new ForceBeltItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> BOTTLED_WITH = ITEMS.register("bottled_wither", () -> new BottledWitherItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> INERT_CORE = ITEMS.register("inert_core", () -> new InertCoreItem(itemBuilder().group(ForceCraft.creativeTab)));

    //Tools
    public static final RegistryObject<Item> FORCE_PICKAXE = ITEMS.register("force_pickaxe", () -> new ForcePickaxeItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_AXE = ITEMS.register("force_axe", () -> new ForceAxeItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_SWORD = ITEMS.register("force_sword", () -> new ForceSwordItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_SHOVEL = ITEMS.register("force_shovel", () -> new ForceShovelItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_SHEARS = ITEMS.register("force_shears", () -> new ForceShearsItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> FORCE_MITT = ITEMS.register("force_mitt", () -> new ForceMittItem(itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> MAGNET_GLOVE = ITEMS.register("magnet_glove", () -> new MagnetGloveItem(itemBuilder().group(ForceCraft.creativeTab)));

    //Bucket
    public static final RegistryObject<Item> BUCKET_FLUID_FORCE = ITEMS.register("force_bucket", () ->
            new ForceFluidBucketItem(itemBuilder().containerItem(Items.BUCKET).maxStackSize(1).group(ForceCraft.creativeTab), () -> ForceFluids.FORCE_FLUID_SOURCE.get()));

    //Experience Tome
    public static final RegistryObject<Item> EXPERIENCE_TOME = ITEMS.register("experience_tome", () -> new ExperienceTomeItem(itemBuilder().group(ForceCraft.creativeTab)));


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

    public static final RegistryObject<Item> FORCE_TORCH_ITEM = ITEMS.register("force_torch", () -> new WallOrFloorItem(FORCE_TORCH.get(), ForceRegistry.WALL_FORCE_TORCH.get(), itemBuilder().group(ForceCraft.creativeTab)));
    public static final RegistryObject<Item> TIME_TORCH_ITEM = ITEMS.register("time_torch", () -> new WallOrFloorItem(TIME_TORCH.get(), ForceRegistry.WALL_TIME_TORCH.get(), itemBuilder().group(ForceCraft.creativeTab)));


    private static Item.Properties itemBuilder() {
        return new Item.Properties();
    }
}
