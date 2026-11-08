package com.mrbysco.forcecraft.datagen.server;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ForceAdvancementsProvider extends AdvancementProvider {
	public ForceAdvancementsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, List.of(new ForceAdvancementsSubProvider()));
	}

	public static class ForceAdvancementsSubProvider implements AdvancementSubProvider {

		@Override
		public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {
			AdvancementHolder root = Advancement.Builder.advancement()
					.display(
							new ItemStackTemplate(ForceRegistry.FORCE_GEM),
							Component.translatable(advancementPrefix("root.title")),
							Component.translatable(advancementPrefix("root.desc")),
							Identifier.withDefaultNamespace("gui/advancements/backgrounds/stone"),
							AdvancementType.TASK, false, true, false
					).addCriterion("force_gem", InventoryChangeTrigger.TriggerInstance.hasItems(ForceRegistry.FORCE_GEM))
					.save(output, rootID("root"));
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
