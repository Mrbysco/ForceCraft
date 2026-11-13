package com.mrbysco.forcecraft.datagen.server;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import com.mrbysco.forcecraft.registry.helper.ForceBrickReg;
import com.mrbysco.forcecraft.registry.helper.ForceFurnaceReg;
import com.mrbysco.forcecraft.registry.helper.ForceTorchReg;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ForceBlockTagsProvider extends BlockTagsProvider {
	public ForceBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, Reference.MOD_ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
				ForceRegistry.FORCE_INFUSER.key(),
				ForceRegistry.FORCE_ENGINE.key(),
				ForceRegistry.FORCE_FURNACE.key(),
				ForceRegistry.POWER_ORE.key(),
				ForceRegistry.DEEPSLATE_POWER_ORE.key(),
				ForceRegistry.NETHER_POWER_ORE.key()
		);
		for (ForceFurnaceReg reg : ForceRegistry.getFurnaces()) {
			this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(reg.getFurnace().key());
		}
		for (ForceBrickReg brickReg : ForceRegistry.getBricks()) {
			this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
					brickReg.getBrick().key(),
					brickReg.getWall().key(),
					brickReg.getStairs().key(),
					brickReg.getSlab().key()
			);
			this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(
					brickReg.getBrick().key(),
					brickReg.getWall().key(),
					brickReg.getStairs().key(),
					brickReg.getSlab().key()
			);
			this.tag(BlockTags.WALLS).add(brickReg.getWall().key());
			this.tag(BlockTags.STAIRS).add(brickReg.getStairs().key());
			this.tag(BlockTags.SLABS).add(brickReg.getSlab().key());
			this.tag(ForceTags.Blocks.FORCE_BRICKS).add(brickReg.getBrick().key());
		}
		this.tag(ForceTags.Blocks.INCORRECT_FOR_FORCE).addTag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL);
		this.tag(ForceTags.Blocks.TICKING_NOT_SUPPORTED);
		this.tag(ForceTags.Blocks.COLD_EDIBLE_GRASS_BLOCKS).add(
				Blocks.GRASS_BLOCK.builtInRegistryHolder().key(),
				Blocks.PODZOL.builtInRegistryHolder().key()
		);
		this.tag(ForceTags.Blocks.ENDERTOT_HOLDABLE).addTags(BlockTags.STAIRS, BlockTags.SLABS);

		this.tag(BlockTags.NEEDS_IRON_TOOL).add(
				ForceRegistry.POWER_ORE.key(),
				ForceRegistry.DEEPSLATE_POWER_ORE.key(),
				ForceRegistry.NETHER_POWER_ORE.key()
		);

		for (ForceTorchReg reg : ForceRegistry.getTorches()) {
			if (!reg.isSpecialTorch()) {
				this.tag(ForceTags.Blocks.FORCE_TORCHES).add(reg.getTorch().key());
			}
		}

		this.tag(BlockTags.MINEABLE_WITH_AXE).add(
				ForceRegistry.FORCE_LOG.key(),
				ForceRegistry.FORCE_WOOD.key(),
				ForceRegistry.FORCE_PLANKS.getPlanks().key(),
				ForceRegistry.FORCE_PLANKS.getButton().key(),
				ForceRegistry.FORCE_PLANKS.getDoor().key(),
				ForceRegistry.FORCE_PLANKS.getFence().key(),
				ForceRegistry.FORCE_PLANKS.getFenceGate().key(),
				ForceRegistry.FORCE_PLANKS.getPressurePlate().key(),
				ForceRegistry.FORCE_PLANKS.getSlab().key(),
				ForceRegistry.FORCE_PLANKS.getStairs().key(),
				ForceRegistry.FORCE_PLANKS.getTrapdoor().key(),
				ForceRegistry.FORCE_PLANKS.getSign().key(),
				ForceRegistry.FORCE_PLANKS.getWallSign().key(),
				ForceRegistry.FORCE_PLANKS.getHangingSign().key(),
				ForceRegistry.FORCE_PLANKS.getWallHangingSign().key(),
				ForceRegistry.FORCE_PLANKS.getShelf().key()
		);
		this.tag(BlockTags.MINEABLE_WITH_HOE).add(ForceRegistry.FORCE_LEAVES.key());

		this.tag(ForceTags.Blocks.FORCE_LOGS).add(ForceRegistry.FORCE_LOG.key(), ForceRegistry.FORCE_WOOD.key());
		this.tag(BlockItemTags.LOGS_THAT_BURN.block()).addTag(ForceTags.Blocks.FORCE_LOGS);
		this.tag(BlockTags.LEAVES).add(ForceRegistry.FORCE_LEAVES.key());
		this.tag(BlockTags.SAPLINGS).add(ForceRegistry.FORCE_SAPLING.key());
		this.tag(BlockTags.FLOWER_POTS).add(ForceRegistry.POTTED_FORCE_SAPLING.key());
		this.tag(BlockTags.PLANKS).add(ForceRegistry.FORCE_PLANKS.getPlanks().key());
		this.tag(BlockTags.WOODEN_BUTTONS).add(ForceRegistry.FORCE_PLANKS.getButton().key());
		this.tag(BlockTags.WOODEN_DOORS).add(ForceRegistry.FORCE_PLANKS.getDoor().key());
		this.tag(BlockTags.WOODEN_FENCES).add(ForceRegistry.FORCE_PLANKS.getFence().key());
		this.tag(BlockTags.FENCE_GATES).add(ForceRegistry.FORCE_PLANKS.getFenceGate().key());
		this.tag(Tags.BlockItems.FENCE_GATES_WOODEN.block()).add(ForceRegistry.FORCE_PLANKS.getFenceGate().key());
		this.tag(BlockTags.WOODEN_PRESSURE_PLATES).add(ForceRegistry.FORCE_PLANKS.getPressurePlate().key());
		this.tag(BlockTags.WOODEN_SLABS).add(ForceRegistry.FORCE_PLANKS.getSlab().key());
		this.tag(BlockTags.WOODEN_STAIRS).add(ForceRegistry.FORCE_PLANKS.getStairs().key());
		this.tag(BlockTags.WOODEN_TRAPDOORS).add(ForceRegistry.FORCE_PLANKS.getTrapdoor().key());
		this.tag(BlockTags.STANDING_SIGNS).add(ForceRegistry.FORCE_PLANKS.getSign().key());
		this.tag(BlockTags.WALL_SIGNS).add(ForceRegistry.FORCE_PLANKS.getWallSign().key());
		this.tag(BlockTags.CEILING_HANGING_SIGNS).add(ForceRegistry.FORCE_PLANKS.getHangingSign().key());
		this.tag(BlockTags.WALL_HANGING_SIGNS).add(ForceRegistry.FORCE_PLANKS.getWallHangingSign().key());
		this.tag(BlockTags.WOODEN_SHELVES).add(ForceRegistry.FORCE_PLANKS.getShelf().key());

		this.tag(Tags.BlockItems.ORES_IN_GROUND_STONE.block()).add(ForceRegistry.POWER_ORE.key());
		this.tag(Tags.BlockItems.ORES_IN_GROUND_DEEPSLATE.block()).add(ForceRegistry.DEEPSLATE_POWER_ORE.key());
		this.tag(Tags.BlockItems.ORE_BEARING_GROUND_NETHERRACK.block()).add(ForceRegistry.NETHER_POWER_ORE.key());
		this.tag(ForceTags.Blocks.ORES_POWER).add(ForceRegistry.POWER_ORE.key(), ForceRegistry.DEEPSLATE_POWER_ORE.key(), ForceRegistry.NETHER_POWER_ORE.key());
		this.tag(Tags.BlockItems.ORES.block()).addTag(ForceTags.Blocks.ORES_POWER);


	}
}
