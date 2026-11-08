package com.mrbysco.forcecraft.advancement;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.advancement.predicate.ForcePackTierPredicate;
import com.mrbysco.forcecraft.advancement.predicate.UpgradeTomeTierPredicate;
import net.minecraft.core.component.predicates.DataComponentPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ForceDataComponentPredicates {
	public static final DeferredRegister<DataComponentPredicate.Type<?>> COMPONENT_TYPE_PREDICATE = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_PREDICATE_TYPE, Reference.MOD_ID);

	public static final Supplier<DataComponentPredicate.Type<ForcePackTierPredicate>> FORCE_PACK_TIER = COMPONENT_TYPE_PREDICATE.register("force_pack_tier", () -> new DataComponentPredicate.ConcreteType<>(ForcePackTierPredicate.CODEC));
	public static final Supplier<DataComponentPredicate.Type<UpgradeTomeTierPredicate>> TOME_TIER = COMPONENT_TYPE_PREDICATE.register("tome_tier", () -> new DataComponentPredicate.ConcreteType<>(UpgradeTomeTierPredicate.CODEC));

}
