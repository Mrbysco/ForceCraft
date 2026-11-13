package com.mrbysco.forcecraft.advancement.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.forcecraft.advancement.ForceCriteriaTriggers;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContextSource;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public class ForceShearEntityTrigger extends SimpleCriterionTrigger<ForceShearEntityTrigger.TriggerInstance> {
	@Override
	public Codec<ForceShearEntityTrigger.TriggerInstance> codec() {
		return ForceShearEntityTrigger.TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, LivingEntity livingEntity) {
		LootContext animalContext = EntityPredicate.createContext(player, livingEntity);
		this.trigger(player, t -> t.matches(animalContext));
	}

	public record TriggerInstance(Optional<Holder<LootItemCondition>> player, Optional<Holder<LootItemCondition>> entity)
			implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<ForceShearEntityTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(
				i -> i.group(
								LootItemCondition.CODEC.optionalFieldOf("player").forGetter(ForceShearEntityTrigger.TriggerInstance::player),
								LootItemCondition.CODEC.optionalFieldOf("entity").forGetter(ForceShearEntityTrigger.TriggerInstance::entity)
						)
						.apply(i, ForceShearEntityTrigger.TriggerInstance::new)
		);

		public static Criterion<ForceShearEntityTrigger.TriggerInstance> shearedEntity() {
			return ForceCriteriaTriggers.FORCE_SHEAR_ENTITY.get().createCriterion(new ForceShearEntityTrigger.TriggerInstance(Optional.empty(), Optional.empty()));
		}

		public static Criterion<TriggerInstance> shearedEntity(EntityPredicate.Builder entity) {
			return ForceCriteriaTriggers.FORCE_SHEAR_ENTITY.get()
					.createCriterion(new ForceShearEntityTrigger.TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(entity))));
		}

		public boolean matches(LootContext animal) {
			return this.entity.isEmpty() || this.entity.get().value().test(animal);
		}

		@Override
		public void validate(ValidationContextSource validator) {
			SimpleCriterionTrigger.SimpleInstance.super.validate(validator);
			Validatable.validateHolder(validator.entityContext(), "entity", this.entity);
		}
	}
}
