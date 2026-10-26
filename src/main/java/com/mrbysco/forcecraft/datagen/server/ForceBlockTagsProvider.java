package com.mrbysco.forcecraft.datagen.server;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import com.mrbysco.forcecraft.registry.helper.ForceBrickReg;
import com.mrbysco.forcecraft.registry.helper.ForceTorchReg;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
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
				ForceRegistry.FORCE_INFUSER.get(),
				ForceRegistry.FORCE_ENGINE.get(),
				ForceRegistry.FORCE_FURNACE.get()
		);
		for (ForceBrickReg brickReg : ForceRegistry.getBricks()) {
			this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
					brickReg.getBrick().get(),
					brickReg.getWall().get(),
					brickReg.getStairs().get(),
					brickReg.getSlab().get()
			);
			this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(
					brickReg.getBrick().get(),
					brickReg.getWall().get(),
					brickReg.getStairs().get(),
					brickReg.getSlab().get()
			);
			this.tag(BlockTags.WALLS).add(brickReg.getWall().get());
			this.tag(BlockTags.STAIRS).add(brickReg.getStairs().get());
			this.tag(BlockTags.SLABS).add(brickReg.getSlab().get());
			this.tag(ForceTags.Blocks.FORCE_BRICKS).add(brickReg.getBrick().get());
		}
		this.tag(ForceTags.Blocks.INCORRECT_FOR_FORCE).addTag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL);
		this.tag(ForceTags.Blocks.TICKING_NOT_SUPPORTED);

		this.tag(BlockTags.NEEDS_IRON_TOOL).add(
				ForceRegistry.POWER_ORE.get(),
				ForceRegistry.DEEPSLATE_POWER_ORE.get(),
				ForceRegistry.NETHER_POWER_ORE.get()
		);

		for (ForceTorchReg reg : ForceRegistry.getTorches()) {
			if (!reg.isSpecialTorch()) {
				this.tag(ForceTags.Blocks.FORCE_TORCHES).add(reg.getTorch().get());
			}
		}

		this.tag(BlockTags.MINEABLE_WITH_AXE).add(
				ForceRegistry.FORCE_LOG.get(),
				ForceRegistry.FORCE_WOOD.get(),
				ForceRegistry.FORCE_PLANKS.getPlanks().get(),
				ForceRegistry.FORCE_PLANKS.getButton().get(),
				ForceRegistry.FORCE_PLANKS.getDoor().get(),
				ForceRegistry.FORCE_PLANKS.getFence().get(),
				ForceRegistry.FORCE_PLANKS.getFenceGate().get(),
				ForceRegistry.FORCE_PLANKS.getPressurePlate().get(),
				ForceRegistry.FORCE_PLANKS.getSlab().get(),
				ForceRegistry.FORCE_PLANKS.getStairs().get(),
				ForceRegistry.FORCE_PLANKS.getTrapdoor().get(),
				ForceRegistry.FORCE_PLANKS.getSign().get(),
				ForceRegistry.FORCE_PLANKS.getWallSign().get(),
				ForceRegistry.FORCE_PLANKS.getHangingSign().get(),
				ForceRegistry.FORCE_PLANKS.getWallHangingSign().get(),
				ForceRegistry.FORCE_PLANKS.getShelf().get()
		);
		this.tag(BlockTags.MINEABLE_WITH_HOE).add(ForceRegistry.FORCE_LEAVES.get());

		this.tag(ForceTags.Blocks.FORCE_LOGS).add(ForceRegistry.FORCE_LOG.get(), ForceRegistry.FORCE_WOOD.get());
		this.tag(BlockTags.LOGS_THAT_BURN).addTag(ForceTags.Blocks.FORCE_LOGS);
		this.tag(BlockTags.LEAVES).add(ForceRegistry.FORCE_LEAVES.get());
		this.tag(BlockTags.SAPLINGS).add(ForceRegistry.FORCE_SAPLING.get());
		this.tag(BlockTags.FLOWER_POTS).add(ForceRegistry.POTTED_FORCE_SAPLING.get());
		this.tag(BlockTags.PLANKS).add(ForceRegistry.FORCE_PLANKS.getPlanks().get());
		this.tag(BlockTags.WOODEN_BUTTONS).add(ForceRegistry.FORCE_PLANKS.getButton().get());
		this.tag(BlockTags.WOODEN_DOORS).add(ForceRegistry.FORCE_PLANKS.getDoor().get());
		this.tag(BlockTags.WOODEN_FENCES).add(ForceRegistry.FORCE_PLANKS.getFence().get());
		this.tag(BlockTags.FENCE_GATES).add(ForceRegistry.FORCE_PLANKS.getFenceGate().get());
		this.tag(Tags.Blocks.FENCE_GATES_WOODEN).add(ForceRegistry.FORCE_PLANKS.getFenceGate().get());
		this.tag(BlockTags.WOODEN_PRESSURE_PLATES).add(ForceRegistry.FORCE_PLANKS.getPressurePlate().get());
		this.tag(BlockTags.WOODEN_SLABS).add(ForceRegistry.FORCE_PLANKS.getSlab().get());
		this.tag(BlockTags.WOODEN_STAIRS).add(ForceRegistry.FORCE_PLANKS.getStairs().get());
		this.tag(BlockTags.WOODEN_TRAPDOORS).add(ForceRegistry.FORCE_PLANKS.getTrapdoor().get());
		this.tag(BlockTags.STANDING_SIGNS).add(ForceRegistry.FORCE_PLANKS.getSign().get());
		this.tag(BlockTags.WALL_SIGNS).add(ForceRegistry.FORCE_PLANKS.getWallSign().get());
		this.tag(BlockTags.CEILING_HANGING_SIGNS).add(ForceRegistry.FORCE_PLANKS.getHangingSign().get());
		this.tag(BlockTags.WALL_HANGING_SIGNS).add(ForceRegistry.FORCE_PLANKS.getWallHangingSign().get());
		this.tag(BlockTags.WOODEN_SHELVES).add(ForceRegistry.FORCE_PLANKS.getShelf().get());

		this.tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(ForceRegistry.POWER_ORE.get());
		this.tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(ForceRegistry.DEEPSLATE_POWER_ORE.get());
		this.tag(Tags.Blocks.ORE_BEARING_GROUND_NETHERRACK).add(ForceRegistry.NETHER_POWER_ORE.get());
		this.tag(ForceTags.Blocks.ORES_POWER).add(ForceRegistry.POWER_ORE.get(), ForceRegistry.DEEPSLATE_POWER_ORE.get(), ForceRegistry.NETHER_POWER_ORE.get());
		this.tag(Tags.Blocks.ORES).addTag(ForceTags.Blocks.ORES_POWER);


	}
}
