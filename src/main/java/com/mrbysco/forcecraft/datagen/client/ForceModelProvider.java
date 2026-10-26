package com.mrbysco.forcecraft.datagen.client;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.block.ForceEngineBlock;
import com.mrbysco.forcecraft.client.tint.RodColor;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceFluids;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.helper.ForceBrickReg;
import com.mrbysco.forcecraft.registry.helper.ForceFurnaceReg;
import com.mrbysco.forcecraft.registry.helper.ForceTorchReg;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.item.DynamicFluidContainerModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ForceModelProvider extends ModelProvider {
	public ForceModelProvider(PackOutput packOutput) {
		super(packOutput, Reference.MOD_ID);
	}

	private static final TextureSlot STONE = TextureSlot.create("stone");
	private static final TextureSlot ORE = TextureSlot.create("ore");
	private static final ModelTemplate ORE_TEMPLATE = ModelTemplates.create("forcecraft:template_ore", STONE, ORE, TextureSlot.PARTICLE);

	@Override
	protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
		generateOre(blockModels, ForceRegistry.POWER_ORE.get(), Blocks.STONE);
		generateOre(blockModels, ForceRegistry.DEEPSLATE_POWER_ORE.get(), Blocks.DEEPSLATE);
		generateOre(blockModels, ForceRegistry.NETHER_POWER_ORE.get(), Blocks.NETHERRACK);
		generateForceEngine(blockModels, ForceRegistry.FORCE_ENGINE.get());
		blockModels.createNonTemplateModelBlock(ForceRegistry.FORCE_INFUSER.get());
		blockModels.createFurnace(ForceRegistry.FORCE_FURNACE.get(), TexturedModel.ORIENTABLE_ONLY_TOP);
		for (ForceFurnaceReg reg : ForceRegistry.getFurnaces()) {
			blockModels.createFurnace(reg.getFurnace().get(), TexturedModel.ORIENTABLE_ONLY_TOP);
		}

		blockModels.woodProvider(ForceRegistry.FORCE_LOG.get()).logWithHorizontal(ForceRegistry.FORCE_LOG.get()).wood(ForceRegistry.FORCE_WOOD.get());
		blockModels.createTrivialBlock(ForceRegistry.FORCE_LEAVES.get(), TexturedModel.LEAVES);
		blockModels.createPlantWithDefaultItem(ForceRegistry.FORCE_SAPLING.get(), ForceRegistry.POTTED_FORCE_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);

		blockModels.family(ForceRegistry.FORCE_PLANKS.getPlanks().get()).generateFor(ForceRegistry.FORCE_PLANKS.getBlockFamily());
		blockModels.createHangingSign(
				ForceRegistry.FORCE_PLANKS.getPlanks().get(),
				ForceRegistry.FORCE_PLANKS.getHangingSign().get(),
				ForceRegistry.FORCE_PLANKS.getWallHangingSign().get()
		);
		blockModels.createShelf(ForceRegistry.FORCE_PLANKS.getShelf().get(), ForceRegistry.FORCE_PLANKS.getPlanks().get());
		itemModels.generateFlatItem(ForceRegistry.FORCE_STICK.get(), ModelTemplates.FLAT_ITEM);
		itemModels.generateFlatItem(ForceRegistry.FORCE_GEAR.get(), ModelTemplates.FLAT_ITEM);
		for (ForceBrickReg reg : ForceRegistry.getBricks()) {
			blockModels.family(reg.getBrick().get()).generateFor(reg.getBlockFamily());
		}

		for (ForceTorchReg reg : ForceRegistry.getTorches()) {
			blockModels.createNormalTorch(reg.getTorch().get(), reg.getWallTorch().get());
		}

		itemModels.generateFlatItem(ForceRegistry.FORCE_HELMET.get(), ModelTemplates.FLAT_ITEM);
		itemModels.generateFlatItem(ForceRegistry.FORCE_CHEST.get(), ModelTemplates.FLAT_ITEM);
		itemModels.generateFlatItem(ForceRegistry.FORCE_LEGS.get(), ModelTemplates.FLAT_ITEM);
		itemModels.generateFlatItem(ForceRegistry.FORCE_BOOTS.get(), ModelTemplates.FLAT_ITEM);

		itemModels.generateFlatItem(ForceRegistry.FORCE_GEM.get(), ModelTemplates.FLAT_ITEM);
		itemModels.generateFlatItem(ForceRegistry.FORCE_INGOT.get(), ModelTemplates.FLAT_ITEM);
		itemModels.generateFlatItem(ForceRegistry.FORCE_NUGGET.get(), ModelTemplates.FLAT_ITEM);
		itemModels.generateFlatItem(ForceRegistry.GOLDEN_POWER_SOURCE.get(), ModelTemplates.FLAT_ITEM);
		itemModels.generateFlatItem(ForceRegistry.CLAW.get(), ModelTemplates.FLAT_ITEM);
		itemModels.generateFlatItem(ForceRegistry.INERT_CORE.get(), ModelTemplates.FLAT_ITEM);
		itemModels.generateFlatItem(ForceRegistry.SOUL_WAFER.get(), ModelTemplates.FLAT_ITEM);
		itemModels.generateFlatItem(ForceRegistry.ROCS_FEATHER.get(), ModelTemplates.FLAT_ITEM);
		itemModels.generateFlatItem(ForceRegistry.FEATHER_DUST.get(), ModelTemplates.FLAT_ITEM);
		itemModels.generateFlatItem(ForceRegistry.TREASURE_CORE.get(), ModelTemplates.FLAT_ITEM);
		itemModels.generateItemWithTintedOverlay(ForceRegistry.FORCE_ROD.get(), "_layer", new RodColor());
		itemModels.generateFlatItem(ForceRegistry.EXPERIENCE_TOME.get(), ModelTemplates.FLAT_ITEM);
		itemModels.generateFlatItem(ForceRegistry.UPGRADE_TOME.get(), ModelTemplates.FLAT_ITEM);
		itemModels.generateFlatItem(ForceRegistry.FORTUNE_COOKIE.get(), ModelTemplates.FLAT_ITEM);
		itemModels.generateFlatItem(ForceRegistry.FORTUNE.get(), ModelTemplates.FLAT_ITEM);
		itemModels.generateFlatItem(ForceRegistry.SNOW_COOKIE.get(), ModelTemplates.FLAT_ITEM);

		itemModels.generateFlatItem(ForceRegistry.FORCE_ARROW.get(), ModelTemplates.FLAT_ITEM);
		itemModels.generateFlatItem(ForceRegistry.FORCE_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		itemModels.generateFlatItem(ForceRegistry.FORCE_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		itemModels.generateFlatItem(ForceRegistry.FORCE_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		itemModels.generateFlatItem(ForceRegistry.FORCE_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		itemModels.generateFlatItem(ForceRegistry.FORCE_SHEARS.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		itemModels.generateFlatItem(ForceRegistry.FORCE_BOW.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		itemModels.generateFlatItem(ForceRegistry.FORCE_MITT.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

		generatePack(itemModels);
		itemModels.generateFlatItem(ForceRegistry.FORCE_PACK_UPGRADE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		itemModels.generateFlatItem(ForceRegistry.FORCE_BELT.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		itemModels.generateFlatItem(ForceRegistry.UPGRADE_CORE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		itemModels.generateFlatItem(ForceRegistry.EXPERIENCE_CORE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		itemModels.generateFlatItem(ForceRegistry.FROZEN_CORE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		itemModels.generateFlatItem(ForceRegistry.GRINDING_CORE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		itemModels.generateFlatItem(ForceRegistry.HEAT_CORE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		itemModels.generateFlatItem(ForceRegistry.SPEED_CORE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

		// Bombs
		itemModels.declareCustomModelItem(ForceRegistry.BOMB.get());
		itemModels.declareCustomModelItem(ForceRegistry.SUPER_BOMB.get());

		generateBucket(blockModels);
	}

	private void generatePack(ItemModelGenerators itemModels) {
		Item item = ForceRegistry.FORCE_PACK.get();
		ItemModel.Unbaked defaultModel = ItemModelUtils.plainModel(itemModels.createFlatItemModel(item, ModelTemplates.FLAT_ITEM));
		List<SelectItemModel.SwitchCase<DyeColor>> cases = new ArrayList<>();

		for (DyeColor color : DyeColor.values()) {
			if (color == DyeColor.YELLOW) continue; // Regular force pack is yellow :)

			ItemModel.Unbaked colorModel = ItemModelUtils.plainModel(
					itemModels.createFlatItemModel(item, "_" + color.getName(), ModelTemplates.FLAT_ITEM)
			);
			cases.add(ItemModelUtils.when(color, colorModel));
		}

		itemModels.itemModelOutput.accept(
				item,
				ItemModelUtils.select(
						new net.minecraft.client.renderer.item.properties.select.ComponentContents<>(ForceComponents.PACK_COLOR.get()),
						defaultModel,
						cases
				)
		);
	}

	private void generateOre(BlockModelGenerators blockModels, Block block, Block stone) {
		TextureMapping oreTextures = new TextureMapping()
				.put(ORE, TextureMapping.getBlockTexture(ForceRegistry.POWER_ORE.get()))
				.put(STONE, TextureMapping.getBlockTexture(stone))
				.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(stone));
		MultiVariant oreModel = BlockModelGenerators.plainVariant(
				ORE_TEMPLATE.create(block, oreTextures, blockModels.modelOutput));
		blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, oreModel));
	}

	private void generateBucket(BlockModelGenerators blockModels) {
		blockModels.createNonTemplateModelBlock(ForceRegistry.FORCE_FLUID_BLOCK.get());

		blockModels.itemModelOutput.accept(ForceRegistry.FORCE_BUCKET.get(), new DynamicFluidContainerModel.Unbaked(
				new DynamicFluidContainerModel.Textures(
						Optional.of(new Material(Identifier.withDefaultNamespace("item/bucket"))),
						Optional.of(new Material(Identifier.withDefaultNamespace("item/bucket"))),
						Optional.of(new Material(Identifier.fromNamespaceAndPath("neoforge", "item/mask/bucket_fluid"))),
						Optional.empty()
				), ForceFluids.FORCE_FLUID_SOURCE.get(), false, true, false
		));
	}

	private void generateForceEngine(BlockModelGenerators blockModels, Block block) {
		MultiVariant inactive = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(block));
		MultiVariant active = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(block, "_active"));

		blockModels.blockStateOutput.accept(
				MultiVariantGenerator.dispatch(block)
						.with(PropertyDispatch.initial(ForceEngineBlock.GENERATING, ForceEngineBlock.POWERED)
								.generate((generating, powered) -> generating ? active : inactive))
						.with(BlockModelGenerators.ROTATIONS_COLUMN_WITH_FACING)
		);
		blockModels.registerSimpleItemModel(block, ModelLocationUtils.getModelLocation(block));
	}
}
