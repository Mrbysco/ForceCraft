package com.mrbysco.forcecraft.registry.helper;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.data.BlockFamily;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.ShelfBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ForcePlankReg {
	protected final BlockSetType setType;
	protected final WoodType woodType;

	protected final DeferredBlock<Block> planks;
	protected final DeferredBlock<ButtonBlock> button;
	protected final DeferredBlock<FenceBlock> fence;
	protected final DeferredBlock<FenceGateBlock> fenceGate;
	protected final DeferredBlock<PressurePlateBlock> pressurePlate;
	protected final DeferredBlock<StandingSignBlock> sign;
	protected final DeferredBlock<WallSignBlock> wallSign;
	protected final DeferredBlock<CeilingHangingSignBlock> hangingSign;
	protected final DeferredBlock<WallHangingSignBlock> wallHangingSign;
	protected final DeferredBlock<StairBlock> stairs;
	protected final DeferredBlock<SlabBlock> slab;
	protected final DeferredBlock<DoorBlock> door;
	protected final DeferredBlock<TrapDoorBlock> trapdoor;
	protected final DeferredBlock<ShelfBlock> shelf;

	public DeferredBlock<Block> getPlanks() {
		return planks;
	}

	public DeferredBlock<ButtonBlock> getButton() {
		return button;
	}

	public DeferredBlock<FenceBlock> getFence() {
		return fence;
	}

	public DeferredBlock<FenceGateBlock> getFenceGate() {
		return fenceGate;
	}

	public DeferredBlock<PressurePlateBlock> getPressurePlate() {
		return pressurePlate;
	}

	public DeferredBlock<StandingSignBlock> getSign() {
		return sign;
	}

	public DeferredBlock<WallSignBlock> getWallSign() {
		return wallSign;
	}

	public DeferredBlock<CeilingHangingSignBlock> getHangingSign() {
		return hangingSign;
	}

	public DeferredBlock<WallHangingSignBlock> getWallHangingSign() {
		return wallHangingSign;
	}

	public DeferredBlock<StairBlock> getStairs() {
		return stairs;
	}

	public DeferredBlock<SlabBlock> getSlab() {
		return slab;
	}

	public DeferredBlock<DoorBlock> getDoor() {
		return door;
	}

	public DeferredBlock<TrapDoorBlock> getTrapdoor() {
		return trapdoor;
	}

	public DeferredBlock<ShelfBlock> getShelf() {
		return shelf;
	}

	public BlockFamily getBlockFamily() {
		return new BlockFamily.Builder(planks.get())
				.button(button.get())
				.fence(fence.get())
				.fenceGate(fenceGate.get())
				.pressurePlate(pressurePlate.get())
				.sign(sign.get(), wallSign.get())
				.slab(slab.get())
				.stairs(stairs.get())
				.door(door.get())
				.trapdoor(trapdoor.get())
				.recipeGroupPrefix("wooden")
				.getFamily();
	}

	public ForcePlankReg() {
		setType = BlockSetType.register(new BlockSetType(Reference.modLoc("force").toString(),
				true,
				true,
				true,
				BlockSetType.PressurePlateSensitivity.EVERYTHING,
				SoundType.WOOD,
				SoundEvents.WOODEN_DOOR_CLOSE,
				SoundEvents.WOODEN_DOOR_OPEN,
				SoundEvents.WOODEN_TRAPDOOR_CLOSE,
				SoundEvents.WOODEN_TRAPDOOR_OPEN,
				SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF,
				SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON,
				SoundEvents.WOODEN_BUTTON_CLICK_OFF,
				SoundEvents.WOODEN_BUTTON_CLICK_ON));
		woodType = WoodType.register(new WoodType(Reference.modLoc("force").toString(), setType));

		planks = ForceRegistry.BLOCKS.registerSimpleBlock("force_planks", () -> BlockBehaviour.Properties.of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava());
		ForceRegistry.ITEMS.registerSimpleBlockItem(planks);
		button = ForceRegistry.BLOCKS.registerBlock("force_button", (properties) -> new ButtonBlock(setType, 30, properties), () -> BlockBehaviour.Properties.of().noCollision().strength(0.5F).pushReaction(PushReaction.DESTROY));
		ForceRegistry.ITEMS.registerSimpleBlockItem(button);
		fence = ForceRegistry.BLOCKS.registerBlock("force_fence", FenceBlock::new, () -> BlockBehaviour.Properties.ofLegacyCopy(planks.get()));
		ForceRegistry.ITEMS.registerSimpleBlockItem(fence);
		fenceGate = ForceRegistry.BLOCKS.registerBlock("force_fence_gate", (properties) -> new FenceGateBlock(woodType, properties), () ->
				BlockBehaviour.Properties.of()
						.mapColor(planks.get().defaultMapColor())
						.forceSolidOn()
						.instrument(NoteBlockInstrument.BASS)
						.strength(2.0F, 3.0F)
						.ignitedByLava()
		);
		ForceRegistry.ITEMS.registerSimpleBlockItem(fenceGate);
		pressurePlate = ForceRegistry.BLOCKS.registerBlock("force_pressure_plate", (properties) -> new PressurePlateBlock(setType, properties), () ->
				BlockBehaviour.Properties.of()
						.mapColor(planks.get().defaultMapColor())
						.forceSolidOn()
						.instrument(NoteBlockInstrument.BASS)
						.noCollision()
						.strength(0.5F)
						.ignitedByLava()
						.pushReaction(PushReaction.DESTROY)
		);
		ForceRegistry.ITEMS.registerSimpleBlockItem(pressurePlate);
		sign = ForceRegistry.BLOCKS.registerBlock("force_sign", (properties) -> new StandingSignBlock(woodType, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.SAND).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).ignitedByLava());
		wallSign = ForceRegistry.BLOCKS.registerBlock("force_wall_sign", (properties) -> new WallSignBlock(woodType, properties), () -> {
			BlockBehaviour.Properties wallProperties = BlockBehaviour.Properties.of().overrideLootTable(sign.get().getLootTable());
			return wallProperties.overrideDescription(sign.get().getDescriptionId())
					.mapColor(MapColor.SAND).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).ignitedByLava();

		});
		ForceRegistry.ITEMS.registerItem("force_sign", (properties) -> new SignItem(sign.get(), wallSign.get(), properties.useBlockDescriptionPrefix().stacksTo(16)));
		hangingSign = ForceRegistry.BLOCKS.registerBlock("force_hanging_sign", (properties) -> new CeilingHangingSignBlock(woodType, properties), () ->
				BlockBehaviour.Properties.of()
						.mapColor(MapColor.SAND)
						.forceSolidOn()
						.instrument(NoteBlockInstrument.BASS)
						.noCollision()
						.strength(1.0F)
						.ignitedByLava()
		);
		wallHangingSign = ForceRegistry.BLOCKS.registerBlock("force_wall_hanging_sign", (properties) -> new WallHangingSignBlock(woodType, properties), () -> {
					BlockBehaviour.Properties wallProperties = BlockBehaviour.Properties.of().overrideLootTable(hangingSign.get().getLootTable());
					return wallProperties.overrideDescription(hangingSign.get().getDescriptionId()).mapColor(MapColor.SAND).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).ignitedByLava();
				}
		);
		ForceRegistry.ITEMS.registerItem("force_hanging_sign", (properties) -> new HangingSignItem(hangingSign.get(), wallHangingSign.get(), properties.useBlockDescriptionPrefix().stacksTo(16)));
		stairs = ForceRegistry.BLOCKS.registerBlock("force_stairs", (properties) -> new StairBlock(planks.get().defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofLegacyCopy(planks.get()));
		ForceRegistry.ITEMS.registerSimpleBlockItem(stairs);
		slab = ForceRegistry.BLOCKS.registerBlock("force_slab", SlabBlock::new, () -> BlockBehaviour.Properties.ofLegacyCopy(planks.get()));
		ForceRegistry.ITEMS.registerSimpleBlockItem(slab);
		door = ForceRegistry.BLOCKS.registerBlock("force_door", (properties) -> new DoorBlock(setType, properties), () ->
				BlockBehaviour.Properties.of()
						.mapColor(planks.get().defaultMapColor())
						.instrument(NoteBlockInstrument.BASS)
						.strength(3.0F)
						.noOcclusion()
						.ignitedByLava()
						.pushReaction(PushReaction.DESTROY));
		ForceRegistry.ITEMS.registerItem("force_door", (properties) -> new DoubleHighBlockItem(door.get(), properties.useBlockDescriptionPrefix()));
		trapdoor = ForceRegistry.BLOCKS.registerBlock("force_trapdoor", (properties) -> new TrapDoorBlock(setType, properties), () ->
				BlockBehaviour.Properties.of()
						.mapColor(MapColor.SAND)
						.instrument(NoteBlockInstrument.BASS)
						.strength(3.0F)
						.noOcclusion()
						.isValidSpawn(Blocks::never)
						.ignitedByLava());
		ForceRegistry.ITEMS.registerSimpleBlockItem(trapdoor, () -> new Item.Properties().useBlockDescriptionPrefix());

		shelf = ForceRegistry.BLOCKS.registerBlock("force_shelf", ShelfBlock::new, () ->
				BlockBehaviour.Properties.of()
						.mapColor(planks.get().defaultMapColor())
						.instrument(NoteBlockInstrument.BASS)
						.sound(SoundType.SHELF)
						.ignitedByLava()
						.strength(2.0F, 3.0F)
		);
		ForceRegistry.ITEMS.registerSimpleBlockItem(shelf, () -> new Item.Properties().useBlockDescriptionPrefix());
	}
}
