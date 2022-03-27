package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForceFluids {
	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Reference.MOD_ID);

	public static RegistryObject<FlowingFluid> FORCE_FLUID_SOURCE;
	public static RegistryObject<FlowingFluid> FORCE_FLUID_FLOWING;

	public static ForgeFlowingFluid.Properties FLUID_FORCE_PROPERTIES = new ForgeFlowingFluid.Properties(() -> FORCE_FLUID_SOURCE.get(), () -> FORCE_FLUID_FLOWING.get(), FluidAttributes.builder(new ResourceLocation(Reference.MOD_ID, "fluid/force_fluid_source"), new ResourceLocation(Reference.MOD_ID, "fluid/force_fluid_flowing")).rarity(Rarity.COMMON).luminosity(0).density(2000).viscosity(1000).temperature(120).color(0xFFFFFFFF)).bucket(ForceRegistry.BUCKET_FLUID_FORCE).block(() -> (LiquidBlock) ForceRegistry.FORCE_FLUID_BLOCK.get());

	public static void registerFluids() {
		FORCE_FLUID_SOURCE = FLUIDS.register("fluid_force_source", () -> new ForgeFlowingFluid.Source(FLUID_FORCE_PROPERTIES));
		FORCE_FLUID_FLOWING = FLUIDS.register("fluid_force_flowing", () -> new ForgeFlowingFluid.Flowing(FLUID_FORCE_PROPERTIES));
	}
}
