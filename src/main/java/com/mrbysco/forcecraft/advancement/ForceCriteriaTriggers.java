package com.mrbysco.forcecraft.advancement;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.advancement.trigger.ForceShearEntityTrigger;
import net.minecraft.advancements.triggers.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ForceCriteriaTriggers {
	public static final DeferredRegister<CriterionTrigger<?>> CRITERION_TRIGGERS = DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES, Reference.MOD_ID);

	public static final Supplier<ForceShearEntityTrigger> FORCE_SHEAR_ENTITY = CRITERION_TRIGGERS.register("force_shear_entity", ForceShearEntityTrigger::new);
}
