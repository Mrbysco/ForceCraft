package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.entity.Bomb;
import com.mrbysco.forcecraft.entity.ForceArrow;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ForceEntities {
	public static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(Reference.MOD_ID);

	public static final Supplier<EntityType<Bomb>> BOMB = ENTITY_TYPES.registerEntityType("bomb",
			Bomb::new, MobCategory.MISC,
			builder -> builder
					.noLootTable()
					.fireImmune()
					.sized(0.5F, 0.5F)
					.eyeHeight(0.15F)
					.clientTrackingRange(10)
					.updateInterval(10)
	);

	public static final Supplier<EntityType<ForceArrow>> FORCE_ARROW = ENTITY_TYPES.registerEntityType("force_arrow",
			ForceArrow::new, MobCategory.MISC,
			builder -> builder
					.noLootTable()
					.sized(0.5F, 0.5F)
					.eyeHeight(0.13F)
					.clientTrackingRange(4)
					.updateInterval(20)
	);
}
