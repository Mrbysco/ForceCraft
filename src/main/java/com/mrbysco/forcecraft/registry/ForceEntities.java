package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.entities.AngryEndermanEntity;
import com.mrbysco.forcecraft.entities.ChuChuEntity;
import com.mrbysco.forcecraft.entities.ColdChickenEntity;
import com.mrbysco.forcecraft.entities.ColdCowEntity;
import com.mrbysco.forcecraft.entities.ColdPigEntity;
import com.mrbysco.forcecraft.entities.CreeperTotEntity;
import com.mrbysco.forcecraft.entities.EnderTotEntity;
import com.mrbysco.forcecraft.entities.FairyEntity;
import com.mrbysco.forcecraft.entities.projectile.FlaskEntity;
import com.mrbysco.forcecraft.entities.projectile.ForceArrowEntity;
import com.mrbysco.forcecraft.items.nonburnable.NonBurnableItemEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ForceEntities {
	public static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(Reference.MOD_ID);


	public static final Supplier<EntityType<NonBurnableItemEntity>> NON_BURNABLE_ITEM = ENTITY_TYPES.registerEntityType("non_burnable_item",
			NonBurnableItemEntity::new, MobCategory.MISC,
			builder -> builder
					.sized(0.25F, 0.25F).clientTrackingRange(6).updateInterval(20).fireImmune()
	);

	public static final Supplier<EntityType<ColdChickenEntity>> COLD_CHICKEN = ENTITY_TYPES.registerEntityType("cold_chicken",
			ColdChickenEntity::new, MobCategory.CREATURE,
			builder -> builder
					.sized(0.4F, 0.7F).clientTrackingRange(10));

	public static final Supplier<EntityType<ColdCowEntity>> COLD_COW = ENTITY_TYPES.registerEntityType("cold_cow",
			ColdCowEntity::new, MobCategory.CREATURE,
			builder -> builder
					.sized(0.9F, 1.4F).clientTrackingRange(10));

	public static final Supplier<EntityType<ColdPigEntity>> COLD_PIG = ENTITY_TYPES.registerEntityType("cold_pig",
			ColdPigEntity::new, MobCategory.CREATURE,
			builder -> builder
					.sized(0.9F, 0.9F).clientTrackingRange(10));

	public static final Supplier<EntityType<FairyEntity>> FAIRY = ENTITY_TYPES.registerEntityType("fairy",
			FairyEntity::new, MobCategory.CREATURE,
			builder -> builder
					.sized(0.5F, 0.5F).clientTrackingRange(10));

	public static final Supplier<EntityType<ChuChuEntity>> RED_CHU_CHU = ENTITY_TYPES.registerEntityType("red_chu_chu",
			ChuChuEntity::new, MobCategory.MONSTER,
			builder -> builder
					.sized(0.52F, 0.52F).eyeHeight(0.325F).spawnDimensionsScale(4.0F).clientTrackingRange(10));
	public static final Supplier<EntityType<ChuChuEntity>> GREEN_CHU_CHU = ENTITY_TYPES.registerEntityType("green_chu_chu",
			ChuChuEntity::new, MobCategory.MONSTER,
			builder -> builder
					.sized(0.52F, 0.52F).eyeHeight(0.325F).spawnDimensionsScale(4.0F).clientTrackingRange(10));
	public static final Supplier<EntityType<ChuChuEntity>> BLUE_CHU_CHU = ENTITY_TYPES.registerEntityType("blue_chu_chu",
			ChuChuEntity::new, MobCategory.MONSTER,
			builder -> builder
					.sized(0.52F, 0.52F).eyeHeight(0.325F).spawnDimensionsScale(4.0F).clientTrackingRange(10));
	public static final Supplier<EntityType<ChuChuEntity>> GOLD_CHU_CHU = ENTITY_TYPES.registerEntityType("gold_chu_chu",
			ChuChuEntity::new, MobCategory.MONSTER,
			builder -> builder
					.sized(0.52F, 0.52F).eyeHeight(0.325F).spawnDimensionsScale(4.0F).clientTrackingRange(10));

	public static final Supplier<EntityType<CreeperTotEntity>> CREEPER_TOT = ENTITY_TYPES.registerEntityType("creeper_tot",
			CreeperTotEntity::new, MobCategory.MONSTER,
			builder -> builder
					.sized(0.6F, 1.1F).clientTrackingRange(8));

	public static final Supplier<EntityType<EnderTotEntity>> ENDER_TOT = ENTITY_TYPES.registerEntityType("ender_tot",
			EnderTotEntity::new, MobCategory.MONSTER,
			builder -> builder
					.sized(0.6F, 1.6F).clientTrackingRange(8));

	public static final Supplier<EntityType<AngryEndermanEntity>> ANGRY_ENDERMAN = ENTITY_TYPES.registerEntityType("angry_enderman",
			AngryEndermanEntity::new, MobCategory.MONSTER,
			builder -> builder
					.sized(0.6F, 2.9F).clientTrackingRange(8));

	public static final Supplier<EntityType<ForceArrowEntity>> FORCE_ARROW = ENTITY_TYPES.registerEntityType("force_arrow",
			ForceArrowEntity::new, MobCategory.MISC,
			builder -> builder
					.sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));

	public static final Supplier<EntityType<FlaskEntity>> FORCE_FLASK = ENTITY_TYPES.registerEntityType("force_flask",
			FlaskEntity::new, MobCategory.MISC,
			builder -> builder
					.sized(0.25F, 0.25F)
					.clientTrackingRange(4).updateInterval(10));

	public static void registerSpawnPlacement(RegisterSpawnPlacementsEvent event) {
		event.register(RED_CHU_CHU.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ChuChuEntity::canSpawnHere, RegisterSpawnPlacementsEvent.Operation.AND);
		event.register(GREEN_CHU_CHU.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ChuChuEntity::canSpawnHere, RegisterSpawnPlacementsEvent.Operation.AND);
		event.register(BLUE_CHU_CHU.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ChuChuEntity::canSpawnHere, RegisterSpawnPlacementsEvent.Operation.AND);
		event.register(GOLD_CHU_CHU.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ChuChuEntity::canSpawnHere, RegisterSpawnPlacementsEvent.Operation.AND);
		event.register(CREEPER_TOT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
		event.register(ENDER_TOT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
		event.register(ANGRY_ENDERMAN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
		event.register(FAIRY.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, FairyEntity::canSpawnOn, RegisterSpawnPlacementsEvent.Operation.AND);
	}

	public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(ForceEntities.COLD_CHICKEN.get(), ColdChickenEntity.generateAttributes().build());
		event.put(ForceEntities.COLD_COW.get(), ColdCowEntity.generateAttributes().build());
		event.put(ForceEntities.COLD_PIG.get(), ColdPigEntity.generateAttributes().build());

		event.put(ForceEntities.RED_CHU_CHU.get(), Monster.createMonsterAttributes().build());
		event.put(ForceEntities.GREEN_CHU_CHU.get(), Monster.createMonsterAttributes().build());
		event.put(ForceEntities.BLUE_CHU_CHU.get(), Monster.createMonsterAttributes().build());
		event.put(ForceEntities.GOLD_CHU_CHU.get(), Monster.createMonsterAttributes().build());

		event.put(ForceEntities.CREEPER_TOT.get(), CreeperTotEntity.generateAttributes().build());
		event.put(ForceEntities.ENDER_TOT.get(), EnderTotEntity.generateAttributes().build());
		event.put(ForceEntities.ANGRY_ENDERMAN.get(), AngryEndermanEntity.generateAttributes().build());

		event.put(ForceEntities.FAIRY.get(), FairyEntity.generateAttributes().build());
	}
}
