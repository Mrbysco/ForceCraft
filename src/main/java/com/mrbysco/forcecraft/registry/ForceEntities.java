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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.SpawnPlacementRegisterEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ForceEntities {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Reference.MOD_ID);

	public static final Supplier<EntityType<NonBurnableItemEntity>> NON_BURNABLE_ITEM = ENTITY_TYPES.register("non_burnable_item", () ->
			register("non_burnable_item", EntityType.Builder.<NonBurnableItemEntity>of(NonBurnableItemEntity::new, MobCategory.MISC)
					.sized(0.25F, 0.25F).clientTrackingRange(6).updateInterval(20)));

	public static final Supplier<EntityType<ColdChickenEntity>> COLD_CHICKEN = ENTITY_TYPES.register("cold_chicken", () ->
			register("cold_chicken", EntityType.Builder.<ColdChickenEntity>of(ColdChickenEntity::new, MobCategory.CREATURE)
					.sized(0.4F, 0.7F).clientTrackingRange(10)));

	public static final Supplier<EntityType<ColdCowEntity>> COLD_COW = ENTITY_TYPES.register("cold_cow", () ->
			register("cold_cow", EntityType.Builder.<ColdCowEntity>of(ColdCowEntity::new, MobCategory.CREATURE)
					.sized(0.9F, 1.4F).clientTrackingRange(10)));

	public static final Supplier<EntityType<ColdPigEntity>> COLD_PIG = ENTITY_TYPES.register("cold_pig", () ->
			register("cold_pig", EntityType.Builder.<ColdPigEntity>of(ColdPigEntity::new, MobCategory.CREATURE)
					.sized(0.9F, 0.9F).clientTrackingRange(10)));

	public static final Supplier<EntityType<FairyEntity>> FAIRY = ENTITY_TYPES.register("fairy", () ->
			register("fairy", EntityType.Builder.<FairyEntity>of(FairyEntity::new, MobCategory.CREATURE)
					.sized(0.5F, 0.5F).clientTrackingRange(10)));

	public static final Supplier<EntityType<ChuChuEntity>> RED_CHU_CHU = ENTITY_TYPES.register("red_chu_chu", () ->
			register("red_chu_chu", EntityType.Builder.<ChuChuEntity>of(ChuChuEntity::new, MobCategory.MONSTER)
					.sized(2.04F, 2.04F).clientTrackingRange(10)));
	public static final Supplier<EntityType<ChuChuEntity>> GREEN_CHU_CHU = ENTITY_TYPES.register("green_chu_chu", () ->
			register("green_chu_chu", EntityType.Builder.<ChuChuEntity>of(ChuChuEntity::new, MobCategory.MONSTER)
					.sized(2.04F, 2.04F).clientTrackingRange(10)));
	public static final Supplier<EntityType<ChuChuEntity>> BLUE_CHU_CHU = ENTITY_TYPES.register("blue_chu_chu", () ->
			register("blue_chu_chu", EntityType.Builder.<ChuChuEntity>of(ChuChuEntity::new, MobCategory.MONSTER)
					.sized(2.04F, 2.04F).clientTrackingRange(10)));
	public static final Supplier<EntityType<ChuChuEntity>> GOLD_CHU_CHU = ENTITY_TYPES.register("gold_chu_chu", () ->
			register("gold_chu_chu", EntityType.Builder.<ChuChuEntity>of(ChuChuEntity::new, MobCategory.MONSTER)
					.sized(2.04F, 2.04F).clientTrackingRange(10)));

	public static final Supplier<EntityType<CreeperTotEntity>> CREEPER_TOT = ENTITY_TYPES.register("creeper_tot", () ->
			register("creeper_tot", EntityType.Builder.<CreeperTotEntity>of(CreeperTotEntity::new, MobCategory.MONSTER)
					.sized(0.6F, 1.1F).clientTrackingRange(8)));

	public static final Supplier<EntityType<EnderTotEntity>> ENDER_TOT = ENTITY_TYPES.register("ender_tot", () ->
			register("ender_tot", EntityType.Builder.<EnderTotEntity>of(EnderTotEntity::new, MobCategory.MONSTER)
					.sized(0.6F, 1.6F).clientTrackingRange(8)));

	public static final Supplier<EntityType<AngryEndermanEntity>> ANGRY_ENDERMAN = ENTITY_TYPES.register("angry_enderman", () ->
			register("angry_enderman", EntityType.Builder.<AngryEndermanEntity>of(AngryEndermanEntity::new, MobCategory.MONSTER)
					.sized(0.6F, 2.9F).clientTrackingRange(8)));

	public static final Supplier<EntityType<ForceArrowEntity>> FORCE_ARROW = ENTITY_TYPES.register("force_arrow", () ->
			register("force_arrow", EntityType.Builder.<ForceArrowEntity>of(ForceArrowEntity::new, MobCategory.MISC)
					.sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20)));

	public static final Supplier<EntityType<FlaskEntity>> FORCE_FLASK = ENTITY_TYPES.register("force_flask", () ->
			register("force_flask", EntityType.Builder.<FlaskEntity>of(FlaskEntity::new, MobCategory.MISC)
					.sized(0.25F, 0.25F)
					.clientTrackingRange(4).updateInterval(10)));

	public static void registerSpawnPlacement(SpawnPlacementRegisterEvent event) {
		event.register(RED_CHU_CHU.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ChuChuEntity::canSpawnHere, SpawnPlacementRegisterEvent.Operation.AND);
		event.register(GREEN_CHU_CHU.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ChuChuEntity::canSpawnHere, SpawnPlacementRegisterEvent.Operation.AND);
		event.register(BLUE_CHU_CHU.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ChuChuEntity::canSpawnHere, SpawnPlacementRegisterEvent.Operation.AND);
		event.register(GOLD_CHU_CHU.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ChuChuEntity::canSpawnHere, SpawnPlacementRegisterEvent.Operation.AND);
		event.register(CREEPER_TOT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
		event.register(ENDER_TOT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
		event.register(FAIRY.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, FairyEntity::canSpawnOn, SpawnPlacementRegisterEvent.Operation.AND);
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

	public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
		return builder.build(id);
	}
}
