package mrbysco.forcecraft.registry;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.config.ConfigHandler;
import mrbysco.forcecraft.entities.AngryEndermanEntity;
import mrbysco.forcecraft.entities.ChuChuEntity;
import mrbysco.forcecraft.entities.ColdChickenEntity;
import mrbysco.forcecraft.entities.ColdCowEntity;
import mrbysco.forcecraft.entities.ColdPigEntity;
import mrbysco.forcecraft.entities.CreeperTotEntity;
import mrbysco.forcecraft.entities.EnderTotEntity;
import mrbysco.forcecraft.entities.FairyEntity;
import mrbysco.forcecraft.entities.projectile.FlaskEntity;
import mrbysco.forcecraft.entities.projectile.ForceArrowEntity;
import mrbysco.forcecraft.items.nonburnable.NonBurnableItemEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ForceEntities {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Reference.MOD_ID);

	public static final RegistryObject<EntityType<NonBurnableItemEntity>> NON_BURNABLE_ITEM = ENTITIES.register("non_burnable_item", () ->
			register("non_burnable_item", EntityType.Builder.<NonBurnableItemEntity>create(NonBurnableItemEntity::new, EntityClassification.MISC)
					.size(0.25F, 0.25F).trackingRange(6).updateInterval(20)));

	public static final RegistryObject<EntityType<ColdChickenEntity>> COLD_CHICKEN = ENTITIES.register("cold_chicken", () ->
			register("cold_chicken", EntityType.Builder.<ColdChickenEntity>create(ColdChickenEntity::new, EntityClassification.CREATURE)
					.size(0.4F, 0.7F).trackingRange(10)));

	public static final RegistryObject<EntityType<ColdCowEntity>> COLD_COW = ENTITIES.register("cold_cow", () ->
			register("cold_cow", EntityType.Builder.<ColdCowEntity>create(ColdCowEntity::new, EntityClassification.CREATURE)
					.size(0.9F, 1.4F).trackingRange(10)));

	public static final RegistryObject<EntityType<ColdPigEntity>> COLD_PIG = ENTITIES.register("cold_pig", () ->
			register("cold_pig", EntityType.Builder.<ColdPigEntity>create(ColdPigEntity::new, EntityClassification.CREATURE)
					.size(0.9F, 0.9F).trackingRange(10)));

	public static final RegistryObject<EntityType<FairyEntity>> FAIRY = ENTITIES.register("fairy", () ->
			register("fairy", EntityType.Builder.<FairyEntity>create(FairyEntity::new, EntityClassification.CREATURE)
					.size(0.5F, 0.5F).trackingRange(10)));

	public static final RegistryObject<EntityType<ChuChuEntity>> RED_CHU_CHU = ENTITIES.register("red_chu_chu", () ->
			register("red_chu_chu", EntityType.Builder.<ChuChuEntity>create(ChuChuEntity::new, EntityClassification.MONSTER)
					.size(2.04F, 2.04F).trackingRange(10)));
	public static final RegistryObject<EntityType<ChuChuEntity>> GREEN_CHU_CHU = ENTITIES.register("green_chu_chu", () ->
			register("green_chu_chu", EntityType.Builder.<ChuChuEntity>create(ChuChuEntity::new, EntityClassification.MONSTER)
					.size(2.04F, 2.04F).trackingRange(10)));
	public static final RegistryObject<EntityType<ChuChuEntity>> BLUE_CHU_CHU = ENTITIES.register("blue_chu_chu", () ->
			register("blue_chu_chu", EntityType.Builder.<ChuChuEntity>create(ChuChuEntity::new, EntityClassification.MONSTER)
					.size(2.04F, 2.04F).trackingRange(10)));
	public static final RegistryObject<EntityType<ChuChuEntity>> GOLD_CHU_CHU = ENTITIES.register("gold_chu_chu", () ->
			register("gold_chu_chu", EntityType.Builder.<ChuChuEntity>create(ChuChuEntity::new, EntityClassification.MONSTER)
					.size(2.04F, 2.04F).trackingRange(10)));

	public static final RegistryObject<EntityType<CreeperTotEntity>> CREEPER_TOT = ENTITIES.register("creeper_tot", () ->
			register("creeper_tot", EntityType.Builder.<CreeperTotEntity>create(CreeperTotEntity::new, EntityClassification.MONSTER)
					.size(0.6F, 1.1F).trackingRange(8)));

	public static final RegistryObject<EntityType<EnderTotEntity>> ENDER_TOT = ENTITIES.register("ender_tot", () ->
			register("ender_tot", EntityType.Builder.<EnderTotEntity>create(EnderTotEntity::new, EntityClassification.MONSTER)
					.size(0.6F, 1.6F).trackingRange(8)));

	public static final RegistryObject<EntityType<AngryEndermanEntity>> ANGRY_ENDERMAN = ENTITIES.register("angry_enderman", () ->
			register("angry_enderman", EntityType.Builder.<AngryEndermanEntity>create(AngryEndermanEntity::new, EntityClassification.MONSTER)
					.size(0.6F, 2.9F).trackingRange(8)));

	public static final RegistryObject<EntityType<ForceArrowEntity>> FORCE_ARROW = ENTITIES.register("force_arrow", () ->
			register("force_arrow", EntityType.Builder.<ForceArrowEntity>create(ForceArrowEntity::new, EntityClassification.MISC)
					.size(0.5F, 0.5F).trackingRange(4).updateInterval(20)));

	public static final RegistryObject<EntityType<FlaskEntity>> FORCE_FLASK = ENTITIES.register("force_flask", () ->
			register("force_flask", EntityType.Builder.<FlaskEntity>create(FlaskEntity::new, EntityClassification.MISC)
					.size(0.25F, 0.25F)
					.setCustomClientFactory(FlaskEntity::new)
					.trackingRange(4).updateInterval(10)));

	public static void registerSpawnPlacement() {
		EntitySpawnPlacementRegistry.register(RED_CHU_CHU.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ChuChuEntity::canSpawnHere);
		EntitySpawnPlacementRegistry.register(GREEN_CHU_CHU.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ChuChuEntity::canSpawnHere);
		EntitySpawnPlacementRegistry.register(BLUE_CHU_CHU.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ChuChuEntity::canSpawnHere);
		EntitySpawnPlacementRegistry.register(GOLD_CHU_CHU.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ChuChuEntity::canSpawnHere);
		EntitySpawnPlacementRegistry.register(CREEPER_TOT.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(ENDER_TOT.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);

		EntitySpawnPlacementRegistry.register(FAIRY.get(), EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING, FairyEntity::canSpawnOn);
	}

	public static void addSpawns(BiomeLoadingEvent event) {
		RegistryKey<Biome> biomeKey = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, event.getName());
		if(BiomeDictionary.hasType(biomeKey, Type.OVERWORLD)) {
			if(ConfigHandler.COMMON.ChuChuSpawning.get()) {
				if(BiomeDictionary.hasType(biomeKey, Type.SWAMP)) {
					event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(RED_CHU_CHU.get(), 1, 1, 1));
					event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(GREEN_CHU_CHU.get(), 1, 1, 1));
					event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(BLUE_CHU_CHU.get(), 1, 1, 1));
					event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(GOLD_CHU_CHU.get(), 1, 1, 1));
				}
				event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(RED_CHU_CHU.get(), ConfigHandler.COMMON.ChuChuWeight.get(), ConfigHandler.COMMON.ChuChuMinGroup.get(), ConfigHandler.COMMON.ChuChuMaxGroup.get()));
				event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(GREEN_CHU_CHU.get(), ConfigHandler.COMMON.ChuChuWeight.get(), ConfigHandler.COMMON.ChuChuMinGroup.get(), ConfigHandler.COMMON.ChuChuMaxGroup.get()));
				event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(BLUE_CHU_CHU.get(), ConfigHandler.COMMON.ChuChuWeight.get(), ConfigHandler.COMMON.ChuChuMinGroup.get(), ConfigHandler.COMMON.ChuChuMaxGroup.get()));
				event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(GOLD_CHU_CHU.get(), ConfigHandler.COMMON.ChuChuWeight.get(), ConfigHandler.COMMON.ChuChuMinGroup.get(), ConfigHandler.COMMON.ChuChuMaxGroup.get()));
			}
			if(ConfigHandler.COMMON.FairySpawning.get()) {
				event.getSpawns().getSpawner(EntityClassification.CREATURE).add(new MobSpawnInfo.Spawners(FAIRY.get(), ConfigHandler.COMMON.FairyWeight.get(), ConfigHandler.COMMON.FairyMinGroup.get(), ConfigHandler.COMMON.FairyMaxGroup.get()));
			}
			if(ConfigHandler.COMMON.CreeperTotSpawning.get()) {
				event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(CREEPER_TOT.get(), ConfigHandler.COMMON.CreeperTotWeight.get(), ConfigHandler.COMMON.CreeperTotMinGroup.get(), ConfigHandler.COMMON.CreeperTotMaxGroup.get()));
			}
			if(ConfigHandler.COMMON.EnderTotSpawning.get()) {
				event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(ENDER_TOT.get(), ConfigHandler.COMMON.EnderTotWeight.get(), ConfigHandler.COMMON.EnderTotMinGroup.get(), ConfigHandler.COMMON.EnderTotMaxGroup.get()));
			}
		}
	}

	public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(ForceEntities.COLD_CHICKEN.get(), ColdChickenEntity.generateAttributes().create());
		event.put(ForceEntities.COLD_COW.get(), ColdCowEntity.generateAttributes().create());
		event.put(ForceEntities.COLD_PIG.get(), ColdPigEntity.generateAttributes().create());

		event.put(ForceEntities.RED_CHU_CHU.get(), MonsterEntity.func_234295_eP_().create());
		event.put(ForceEntities.GREEN_CHU_CHU.get(), MonsterEntity.func_234295_eP_().create());
		event.put(ForceEntities.BLUE_CHU_CHU.get(), MonsterEntity.func_234295_eP_().create());
		event.put(ForceEntities.GOLD_CHU_CHU.get(), MonsterEntity.func_234295_eP_().create());

		event.put(ForceEntities.CREEPER_TOT.get(), CreeperTotEntity.generateAttributes().create());
		event.put(ForceEntities.ENDER_TOT.get(), EnderTotEntity.generateAttributes().create());
		event.put(ForceEntities.ANGRY_ENDERMAN.get(), AngryEndermanEntity.generateAttributes().create());

		event.put(ForceEntities.FAIRY.get(), FairyEntity.generateAttributes().create());
	}

	public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
		return builder.build(id);
	}
}
