package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.entity.Bomb;
import com.mrbysco.forcecraft.entity.CreeperTot;
import com.mrbysco.forcecraft.entity.ForceArrow;
import com.mrbysco.forcecraft.entity.cold.ColdChicken;
import com.mrbysco.forcecraft.entity.cold.ColdCow;
import com.mrbysco.forcecraft.entity.cold.ColdPig;
import com.mrbysco.forcecraft.entity.ender.AbstractForceEnderman;
import com.mrbysco.forcecraft.entity.ender.AngryEnderman;
import com.mrbysco.forcecraft.entity.ender.EnderTot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@EventBusSubscriber
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

	public static final Supplier<EntityType<ColdChicken>> COLD_CHICKEN = ENTITY_TYPES.registerEntityType("cold_chicken",
			ColdChicken::new, MobCategory.CREATURE,
			builder -> builder
					.sized(0.4F, 0.7F)
					.eyeHeight(0.644F)
					.passengerAttachments(new Vec3(0.0, 0.7, -0.1))
					.clientTrackingRange(10)
	);

	public static final Supplier<EntityType<ColdCow>> COLD_COW = ENTITY_TYPES.registerEntityType("cold_cow",
			ColdCow::new, MobCategory.CREATURE,
			builder -> builder
					.sized(0.9F, 1.4F)
					.eyeHeight(1.3F)
					.passengerAttachments(1.36875F)
					.clientTrackingRange(10)
	);

	public static final Supplier<EntityType<ColdPig>> COLD_PIG = ENTITY_TYPES.registerEntityType("cold_pig",
			ColdPig::new, MobCategory.CREATURE,
			builder -> builder
					.sized(0.9F, 0.9F)
					.passengerAttachments(0.86875F)
					.clientTrackingRange(10)
	);

	public static final Supplier<EntityType<CreeperTot>> CREEPER_TOT = ENTITY_TYPES.registerEntityType("creeper_tot",
			CreeperTot::new, MobCategory.MONSTER,
			builder -> builder
					.sized(0.6F, 1.1F)
					.passengerAttachments(0.86875F)
					.clientTrackingRange(8)
	);

	public static final Supplier<EntityType<EnderTot>> ENDER_TOT = ENTITY_TYPES.registerEntityType("ender_tot",
			EnderTot::new, MobCategory.MONSTER,
			builder -> builder
					.sized(0.6F, 1.6F)
					.eyeHeight(1.35F)
					.passengerAttachments(1.50625F)
					.clientTrackingRange(8)
					.notInPeaceful()

	);

	public static final Supplier<EntityType<AngryEnderman>> ANGRY_ENDERMAN = ENTITY_TYPES.registerEntityType("angry_enderman",
			AngryEnderman::new, MobCategory.MONSTER,
			builder -> builder
					.sized(0.6F, 2.9F)
					.eyeHeight(2.55F)
					.passengerAttachments(2.80625F)
					.clientTrackingRange(8)
					.notInPeaceful()
	);

	@SubscribeEvent
	public static void registerSpawnPlacement(RegisterSpawnPlacementsEvent event) {
		event.register(CREEPER_TOT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
		event.register(ENDER_TOT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
		event.register(ANGRY_ENDERMAN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
	}

	@SubscribeEvent
	public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(ForceEntities.COLD_CHICKEN.get(), ColdChicken.generateAttributes().build());
		event.put(ForceEntities.COLD_COW.get(), ColdCow.generateAttributes().build());
		event.put(ForceEntities.COLD_PIG.get(), ColdPig.generateAttributes().build());
		event.put(ForceEntities.CREEPER_TOT.get(), CreeperTot.generateAttributes().build());
		event.put(ForceEntities.ENDER_TOT.get(), EnderTot.generateAttributes().build());
		event.put(ForceEntities.ANGRY_ENDERMAN.get(), AbstractForceEnderman.generateAttributes().build());
	}
}
