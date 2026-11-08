package com.mrbysco.forcecraft.datagen.server;

import com.mrbysco.forcecraft.registry.ForceEntities;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.helper.ForceBrickReg;
import com.mrbysco.forcecraft.registry.helper.ForceFurnaceReg;
import com.mrbysco.forcecraft.registry.helper.ForceTorchReg;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_FURNACE;


public class ForceLootProvider extends LootTableProvider {
	public ForceLootProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, Set.of(), List.of(
				new SubProviderEntry(ForceBlockLoot::new, LootContextParamSets.BLOCK),
				new SubProviderEntry(ForceEntityLoot::new, LootContextParamSets.ENTITY)//,
//				new SubProviderEntry(SpoilsBagLootTableProvider::new, LootContextParamSets.GIFT)
		), lookupProvider);
	}


	private static class ForceBlockLoot extends BlockLootSubProvider {
		private final HolderLookup.RegistryLookup<Enchantment> enchantments;

		protected ForceBlockLoot(HolderLookup.Provider provider) {
			super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
			this.enchantments = provider.lookupOrThrow(Registries.ENCHANTMENT);
		}

		@Override
		protected void generate() {
			add(ForceRegistry.POWER_ORE.get(), (ore) -> createSilkTouchDispatchTable(ore, applyExplosionDecay(ForceRegistry.POWER_ORE_ITEM.get(), LootItem.lootTableItem(ForceRegistry.FORCE_GEM.get())
					.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))).apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE))))));
			add(ForceRegistry.DEEPSLATE_POWER_ORE.get(), (ore) -> createSilkTouchDispatchTable(ore, applyExplosionDecay(ForceRegistry.POWER_ORE_ITEM.get(), LootItem.lootTableItem(ForceRegistry.FORCE_GEM.get())
					.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))).apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE))))));
			add(ForceRegistry.NETHER_POWER_ORE.get(), (ore) -> createSilkTouchDispatchTable(ore, applyExplosionDecay(ForceRegistry.POWER_ORE_ITEM.get(), LootItem.lootTableItem(ForceRegistry.FORCE_GEM.get())
					.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))).apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE))))));
			add(ForceRegistry.FORCE_LEAVES.get(), (leaves) -> {
				var pool = createOakLeavesDrops(leaves, ForceRegistry.FORCE_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES);
				//add a loot pool for nuggets, base chance 0.025 (as in og dartcraft), + 0.005 per level of fortune
				pool.withPool(
						LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.when(doesNotHaveShearsOrSilkTouch())
								.add(
										((LootPoolSingletonContainer.Builder) applyExplosionCondition(leaves, LootItem.lootTableItem(ForceRegistry.FORCE_NUGGET)))
												.when(
														BonusLevelTableCondition.bonusLevelFlatChance(
																enchantments.getOrThrow(Enchantments.FORTUNE), 0.025f, 0.03f, 0.035f, 0.04f, 0.045f
														)
												)
								)

				);
				return pool;
			});
			dropSelf(ForceRegistry.FORCE_SAPLING.get());
			dropPottedContents(ForceRegistry.POTTED_FORCE_SAPLING.get());

			dropSelf(ForceRegistry.FORCE_LOG.get());
			dropSelf(ForceRegistry.FORCE_WOOD.get());
			dropSelf(ForceRegistry.FORCE_PLANKS.getPlanks().get());
			dropSelf(ForceRegistry.FORCE_PLANKS.getButton().get());
			dropSelf(ForceRegistry.FORCE_PLANKS.getFence().get());
			dropSelf(ForceRegistry.FORCE_PLANKS.getFenceGate().get());
			dropSelf(ForceRegistry.FORCE_PLANKS.getPressurePlate().get());
			dropSelf(ForceRegistry.FORCE_PLANKS.getSign().get());
			dropSelf(ForceRegistry.FORCE_PLANKS.getHangingSign().get());
			dropSelf(ForceRegistry.FORCE_PLANKS.getStairs().get());
			dropSelf(ForceRegistry.FORCE_PLANKS.getSlab().get());
			dropSelf(ForceRegistry.FORCE_PLANKS.getDoor().get());
			dropSelf(ForceRegistry.FORCE_PLANKS.getTrapdoor().get());
			dropSelf(ForceRegistry.FORCE_PLANKS.getShelf().get());

			for (ForceTorchReg reg : ForceRegistry.getTorches()) {
				dropTorch(reg);
			}

			for (ForceBrickReg reg : ForceRegistry.getBricks()) {
				dropSelf(reg.getBrick().get());
				dropSelf(reg.getStairs().get());
				dropSelf(reg.getSlab().get());
				dropSelf(reg.getWall().get());
			}

			add(ForceRegistry.FORCE_ENGINE.get(), createNameableBlockEntityTable(ForceRegistry.FORCE_ENGINE.get()));
			dropSelf(ForceRegistry.FORCE_INFUSER.get());
			add(FORCE_FURNACE.get(), createNameableBlockEntityTable(FORCE_FURNACE.get()));
			for (ForceFurnaceReg reg : ForceRegistry.getFurnaces()) {
				add(reg.getFurnace().get(), createNameableBlockEntityTable(reg.getFurnace().get()));
			}
		}

		private void dropTorch(ForceTorchReg torchReg) {
			dropSelf(torchReg.getTorch().get());
			dropOther(torchReg.getWallTorch().get(), torchReg.getTorch().get());
		}

		@Override
		protected Iterable<Block> getKnownBlocks() {
			return ForceRegistry.BLOCKS.getEntries().stream().map(holder -> (Block) holder.get())::iterator;
		}
	}

	private static class ForceEntityLoot extends EntityLootSubProvider {

		protected ForceEntityLoot(HolderLookup.Provider provider) {
			super(FeatureFlags.REGISTRY.allFlags(), provider);
		}

		@Override
		public void generate() {
			// Cold mobs
			this.add(
					ForceEntities.COLD_CHICKEN.get(),
					LootTable.lootTable()
							.withPool(
									LootPool.lootPool()
											.name("main")
											.setRolls(ConstantValue.exactly(1.0F))
											.add(
													LootItem.lootTableItem(Items.CHICKEN)
															.apply(SmeltItemFunction.smelted().when(this.shouldSmeltLoot()))
															.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
											)
							)
			);
			this.add(
					ForceEntities.COLD_COW.get(),
					LootTable.lootTable()
							.withPool(
									LootPool.lootPool()
											.name("main")
											.setRolls(ConstantValue.exactly(1.0F))
											.add(
													LootItem.lootTableItem(Items.BEEF)
															.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
															.apply(SmeltItemFunction.smelted().when(this.shouldSmeltLoot()))
															.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
											)
							)
			);
			this.add(
					ForceEntities.COLD_PIG.get(),
					LootTable.lootTable()
							.withPool(
									LootPool.lootPool()
											.name("main")
											.setRolls(ConstantValue.exactly(1.0F))
											.add(
													LootItem.lootTableItem(Items.PORKCHOP)
															.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
															.apply(SmeltItemFunction.smelted().when(this.shouldSmeltLoot()))
															.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
											)
							)
			);

			//Ender mobs
			this.add(
					ForceEntities.ANGRY_ENDERMAN.get(),
					LootTable.lootTable()
							.withPool(
									LootPool.lootPool()
											.name("main")
											.setRolls(ConstantValue.exactly(1.0F))
											.add(
													LootItem.lootTableItem(Items.ENDER_PEARL)
															.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
															.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
											)
							)
			);

			this.add(
					ForceEntities.ENDER_TOT.get(),
					LootTable.lootTable()
							.withPool(
									LootPool.lootPool()
											.name("main")
											.setRolls(ConstantValue.exactly(1.0F))
											.add(
													LootItem.lootTableItem(Items.ENDER_PEARL)
															.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
															.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
											)
							)
			);

			//Creeper tot
			this.add(
					ForceEntities.CREEPER_TOT.get(),
					LootTable.lootTable()
							.withPool(
									LootPool.lootPool()
											.name("main")
											.setRolls(ConstantValue.exactly(1.0F))
											.add(
													LootItem.lootTableItem(ForceRegistry.PILE_OF_GUNPOWDER.get())
															.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
															.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
											)
							)
							.withPool(
									LootPool.lootPool()
											.name("head")
											.setRolls(ConstantValue.exactly(1.0F))
											.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.025F, 0.01F))
											.add(
													LootItem.lootTableItem(Items.CREEPER_HEAD)
											)
							)
			);

		}

		@Override
		protected Stream<EntityType<?>> getKnownEntityTypes() {
			return ForceEntities.ENTITY_TYPES.getEntries().stream().map(DeferredHolder::get);
		}
	}
}
