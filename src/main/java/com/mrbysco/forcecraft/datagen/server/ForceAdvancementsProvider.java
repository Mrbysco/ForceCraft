package com.mrbysco.forcecraft.datagen.server;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.advancement.ForceDataComponentPredicates;
import com.mrbysco.forcecraft.advancement.predicate.ForcePackTierPredicate;
import com.mrbysco.forcecraft.advancement.predicate.UpgradeTomeTierPredicate;
import com.mrbysco.forcecraft.advancement.trigger.ForceShearEntityTrigger;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.BlockPredicate;
import net.minecraft.advancements.criterion.DataComponentMatchers;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.ItemUsedOnLocationTrigger;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ForceAdvancementsProvider extends AdvancementProvider {
	public ForceAdvancementsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, List.of(new ForceAdvancementsSubProvider()));
	}

	public static class ForceAdvancementsSubProvider implements AdvancementSubProvider {

		@Override
		public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {
			HolderLookup.RegistryLookup<Item> items = registries.lookupOrThrow(Registries.ITEM);
			HolderLookup.RegistryLookup<Block> blocks = registries.lookupOrThrow(Registries.BLOCK);

			AdvancementHolder root = Advancement.Builder.advancement()
					.display(
							new ItemStackTemplate(ForceRegistry.FORCE_GEM),
							Component.translatable(advancementPrefix("root.title")),
							Component.translatable(advancementPrefix("root.desc")),
							Identifier.withDefaultNamespace("gui/advancements/backgrounds/stone"),
							AdvancementType.TASK, false, true, false
					).addCriterion("force_gem", InventoryChangeTrigger.TriggerInstance.hasItems(ForceRegistry.FORCE_GEM))
					.save(output, rootID("root"));

			AdvancementHolder ingot = Advancement.Builder.advancement()
					.display(
							new ItemStackTemplate(ForceRegistry.FORCE_INGOT),
							Component.translatable(advancementPrefix("force_ingot.title")),
							Component.translatable(advancementPrefix("force_ingot.desc")),
							null,
							AdvancementType.TASK, true, true, false
					).addCriterion("force_gem", InventoryChangeTrigger.TriggerInstance.hasItems(ForceRegistry.FORCE_INGOT))
					.parent(root)
					.save(output, rootID("force_ingot"));

			AdvancementHolder shear = Advancement.Builder.advancement()
					.display(
							new ItemStackTemplate(ForceRegistry.FORCE_SHEARS),
							Component.translatable(advancementPrefix("force_shears.title")),
							Component.translatable(advancementPrefix("force_shears.desc")),
							null,
							AdvancementType.TASK, true, true, false
					)
					.addCriterion("shear", InventoryChangeTrigger.TriggerInstance.hasItems(ForceRegistry.FORCE_SHEARS))
					.addCriterion("shear_entity", ForceShearEntityTrigger.TriggerInstance.shearedEntity())
					.parent(ingot)
					.save(output, rootID("force_shears"));

			AdvancementHolder rod = Advancement.Builder.advancement()
					.display(
							new ItemStackTemplate(ForceRegistry.FORCE_ROD),
							Component.translatable(advancementPrefix("force_rod.title")),
							Component.translatable(advancementPrefix("force_rod.desc")),
							null,
							AdvancementType.TASK, true, true, false
					)
					.addCriterion("rod", InventoryChangeTrigger.TriggerInstance.hasItems(ForceRegistry.FORCE_ROD))
					.parent(ingot)
					.save(output, rootID("force_rod"));

			AdvancementHolder force_sapling = Advancement.Builder.advancement()
					.display(
							new ItemStackTemplate(ForceRegistry.FORCE_SAPLING.asItem()),
							Component.translatable(advancementPrefix("force_sapling.title")),
							Component.translatable(advancementPrefix("force_sapling.desc")),
							null,
							AdvancementType.TASK, true, true, false
					)
					.addCriterion("transmute_sapling",
							ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
									LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(blocks, ForceRegistry.FORCE_SAPLING.get())),
									ItemPredicate.Builder.item().of(items, ForceRegistry.FORCE_ROD)
							)
					)
					.parent(rod)
					.save(output, rootID("force_sapling"));

			AdvancementHolder force_stick = Advancement.Builder.advancement()
					.display(
							new ItemStackTemplate(ForceRegistry.FORCE_STICK),
							Component.translatable(advancementPrefix("force_stick.title")),
							Component.translatable(advancementPrefix("force_stick.desc")),
							null,
							AdvancementType.TASK, true, true, false
					)
					.addCriterion("force_stick", InventoryChangeTrigger.TriggerInstance.hasItems(ForceRegistry.FORCE_STICK))
					.parent(force_sapling)
					.save(output, rootID("force_stick"));

			AdvancementHolder force_infuser = Advancement.Builder.advancement()
					.display(
							new ItemStackTemplate(ForceRegistry.FORCE_INFUSER.asItem()),
							Component.translatable(advancementPrefix("force_infuser.title")),
							Component.translatable(advancementPrefix("force_infuser.desc")),
							null,
							AdvancementType.TASK, true, true, false
					)
					.addCriterion("force_infuser", InventoryChangeTrigger.TriggerInstance.hasItems(ForceRegistry.FORCE_INFUSER))
					.parent(rod)
					.save(output, rootID("force_infuser"));

			AdvancementHolder upgrade_tome = Advancement.Builder.advancement()
					.display(
							new ItemStackTemplate(ForceRegistry.UPGRADE_TOME.asItem()),
							Component.translatable(advancementPrefix("upgrade_tome.title")),
							Component.translatable(advancementPrefix("upgrade_tome.desc")),
							null,
							AdvancementType.TASK, true, true, false
					)
					.addCriterion("upgrade_tome", InventoryChangeTrigger.TriggerInstance.hasItems(ForceRegistry.UPGRADE_TOME))
					.parent(force_infuser)
					.save(output, rootID("upgrade_tome"));

			AdvancementHolder bane = Advancement.Builder.advancement()
					.display(
							new ItemStackTemplate(ForceRegistry.FORCE_SWORD.asItem()),
							Component.translatable(advancementPrefix("bane.title")),
							Component.translatable(advancementPrefix("bane.desc")),
							null,
							AdvancementType.TASK, true, true, false
					)
					.addCriterion("bane", InventoryChangeTrigger.TriggerInstance.hasItems(
							ItemPredicate.Builder.item().of(items, ForceTags.Items.VALID_BANE_ITEMS)
									.withComponents(DataComponentMatchers.Builder.components().any(
													ForceComponents.BANE.get()
											).build()
									)
					))
					.parent(upgrade_tome)
					.save(output, rootID("bane"));

			AdvancementHolder tome_mastery = Advancement.Builder.advancement()
					.display(
							new ItemStackTemplate(ForceRegistry.UPGRADE_TOME.asItem()),
							Component.translatable(advancementPrefix("tome_mastery.title")),
							Component.translatable(advancementPrefix("tome_mastery.desc")),
							null,
							AdvancementType.TASK, true, true, false
					)
					.addCriterion("tome_mastery", InventoryChangeTrigger.TriggerInstance.hasItems(
							ItemPredicate.Builder.item().of(items, ForceRegistry.UPGRADE_TOME)
									.withComponents(DataComponentMatchers.Builder.components().partial(
													ForceDataComponentPredicates.TOME_TIER.get(), new UpgradeTomeTierPredicate(7)
											).build()
									)
					))
					.parent(bane)
					.save(output, rootID("tome_mastery"));

			AdvancementHolder force_pack = Advancement.Builder.advancement()
					.display(
							new ItemStackTemplate(ForceRegistry.FORCE_PACK),
							Component.translatable(advancementPrefix("force_pack.title")),
							Component.translatable(advancementPrefix("force_pack.desc")),
							null,
							AdvancementType.TASK, true, true, false
					)
					.addCriterion("force_pack", InventoryChangeTrigger.TriggerInstance.hasItems(ForceRegistry.FORCE_PACK))
					.parent(ingot)
					.save(output, rootID("force_pack"));

			AdvancementHolder force_pack_upgrade = Advancement.Builder.advancement()
					.display(
							new ItemStackTemplate(ForceRegistry.FORCE_PACK_UPGRADE),
							Component.translatable(advancementPrefix("force_pack_upgrade.title")),
							Component.translatable(advancementPrefix("force_pack_upgrade.desc")),
							null,
							AdvancementType.TASK, true, true, false
					)
					.addCriterion("force_pack_upgrade", InventoryChangeTrigger.TriggerInstance.hasItems(
							ItemPredicate.Builder.item().of(items, ForceRegistry.FORCE_PACK)
									.withComponents(DataComponentMatchers.Builder.components().partial(
													ForceDataComponentPredicates.FORCE_PACK_TIER.get(), new ForcePackTierPredicate(1)
											).build()
									)
					))
					.parent(force_pack)
					.save(output, rootID("force_pack_upgrade"));

		}

		private static Identifier modLoc(String path) {
			return Reference.modLoc(path);
		}

		private static String advancementPrefix(String name) {
			return "advancement." + Reference.MOD_ID + "." + name;
		}

		private static String rootID(String name) {
			return modLoc(name).toString();
		}
	}
}
