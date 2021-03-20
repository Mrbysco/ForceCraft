package mrbysco.forcecraft.registry;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.entities.ColdChickenEntity;
import mrbysco.forcecraft.entities.ColdCowEntity;
import mrbysco.forcecraft.entities.ColdPigEntity;
import mrbysco.forcecraft.entities.projectile.ForceArrowEntity;
import mrbysco.forcecraft.items.nonburnable.NonBurnableItemEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
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

	public static final RegistryObject<EntityType<ForceArrowEntity>> FORCE_ARROW = ENTITIES.register("force_arrow", () ->
			register("force_arrow", EntityType.Builder.<ForceArrowEntity>create(ForceArrowEntity::new, EntityClassification.MISC)
					.size(0.5F, 0.5F).trackingRange(4).updateInterval(20)));

	public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(ForceEntities.COLD_CHICKEN.get(), ColdChickenEntity.generateAttributes().create());
		event.put(ForceEntities.COLD_COW.get(), ColdCowEntity.generateAttributes().create());
		event.put(ForceEntities.COLD_PIG.get(), ColdPigEntity.generateAttributes().create());
	}

	public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
		return builder.build(id);
	}
}
