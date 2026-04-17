package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ForceFluids {
	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, Reference.MOD_ID);
	public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, Reference.MOD_ID);

	private static DeferredHolder<FluidType, FluidType> FORCE_TYPE = FLUID_TYPES.register("force", () -> new FluidType(createTypeProperties()) {
		@Override
		public double motionScale(Entity entity) {
			return entity.level().environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, entity.blockPosition()) ? 0.007D : 0.0023333333333333335D;
		}

		@Override
		public void setItemMovement(ItemEntity entity) {
			Vec3 vec3 = entity.getDeltaMovement();
			entity.setDeltaMovement(vec3.x * (double) 0.95F, vec3.y + (double) (vec3.y < (double) 0.06F ? 5.0E-4F : 0.0F), vec3.z * (double) 0.95F);
		}
	});

	public static DeferredHolder<Fluid, BaseFlowingFluid> FORCE_FLUID_SOURCE = FLUIDS.register("fluid_force_source", () -> new BaseFlowingFluid.Source(ForceFluids.FLUID_FORCE_PROPERTIES));
	public static DeferredHolder<Fluid, BaseFlowingFluid> FORCE_FLUID_FLOWING = FLUIDS.register("fluid_force_flowing", () -> new BaseFlowingFluid.Flowing(ForceFluids.FLUID_FORCE_PROPERTIES));

	private static BaseFlowingFluid.Properties FLUID_FORCE_PROPERTIES = new BaseFlowingFluid.Properties(
			() -> ForceFluids.FORCE_TYPE.get(), () -> ForceFluids.FORCE_FLUID_SOURCE.get(), () -> ForceFluids.FORCE_FLUID_FLOWING.get())
			.bucket(ForceRegistry.BUCKET_FLUID_FORCE).block(() -> (LiquidBlock) ForceRegistry.FORCE_FLUID_BLOCK.get());


	private static FluidType.Properties createTypeProperties() {
		return FluidType.Properties.create()
				.canSwim(false)
				.canDrown(false)
				.pathType(PathType.LAVA)
				.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
				.sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
				.rarity(Rarity.COMMON)
				.lightLevel(0).density(2000).viscosity(1000).temperature(120);
	}
}
