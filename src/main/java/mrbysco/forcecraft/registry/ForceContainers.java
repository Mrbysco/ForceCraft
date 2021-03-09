package mrbysco.forcecraft.registry;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.container.ForceBeltContainer;
import mrbysco.forcecraft.container.ForceFurnaceContainer;
import mrbysco.forcecraft.container.ForcePackContainer;
import mrbysco.forcecraft.container.InfuserContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ForceContainers {
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Reference.MOD_ID);

	public static final RegistryObject<ContainerType<ForceFurnaceContainer>> FORCE_FURNACE = CONTAINERS.register("force_furnace", () ->
			IForgeContainerType.create((windowId, inv, data) -> new ForceFurnaceContainer(windowId, inv)));

	public static final RegistryObject<ContainerType<InfuserContainer>> INFUSER = CONTAINERS.register("infuser", () ->
			IForgeContainerType.create((windowId, inv, data) -> new InfuserContainer(windowId, inv, data)));

	public static final RegistryObject<ContainerType<ForcePackContainer>> FORCE_PACK = CONTAINERS.register("force_pack", () ->
			IForgeContainerType.create((windowId, inv, data) -> new ForcePackContainer(windowId, inv)));

	public static final RegistryObject<ContainerType<ForceBeltContainer>> FORCE_BELT = CONTAINERS.register("force_belt", () ->
			IForgeContainerType.create((windowId, inv, data) -> new ForceBeltContainer(windowId, inv)));
}
