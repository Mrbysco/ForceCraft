package mrbysco.forcecraft.registry;

import mrbysco.forcecraft.Reference;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ForceFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Reference.MOD_ID);

    public static RegistryObject<FlowingFluid> FORCE_FLUID_SOURCE;
    public static RegistryObject<FlowingFluid> FORCE_FLUID_FLOWING;

    public static ForgeFlowingFluid.Properties FLUID_FORCE_PROPERTIES = new ForgeFlowingFluid.Properties(
            () -> FORCE_FLUID_SOURCE.get(),
            () -> FORCE_FLUID_FLOWING.get(),
            FluidAttributes.builder(
                    new ResourceLocation(Reference.MOD_ID, "fluid/force_fluid_source"),
                    new ResourceLocation(Reference.MOD_ID, "fluid/force_fluid_flowing")
            )
            .rarity(Rarity.COMMON)
            .luminosity(0)
            .density(2000)
            .viscosity(1000)
            .temperature(120)
            .color(0xFFFFFFFF)
        ).bucket(ForceRegistry.BUCKET_FLUID_FORCE).block(() -> (FlowingFluidBlock) ForceRegistry.FORCE_FLUID_BLOCK.get());

    public static void registerFluids(){
        FORCE_FLUID_SOURCE = FLUIDS.register("fluid_force_source", () -> new ForgeFlowingFluid.Source(FLUID_FORCE_PROPERTIES));
        FORCE_FLUID_FLOWING = FLUIDS.register("fluid_force_flowing", () -> new ForgeFlowingFluid.Flowing(FLUID_FORCE_PROPERTIES));
    }
}
