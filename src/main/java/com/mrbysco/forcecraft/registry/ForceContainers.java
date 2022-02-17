package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.container.infuser.InfuserContainer;
import com.mrbysco.forcecraft.container.ForceBeltContainer;
import com.mrbysco.forcecraft.container.ForcePackContainer;
import com.mrbysco.forcecraft.container.ItemCardContainer;
import com.mrbysco.forcecraft.container.SpoilsBagContainer;
import com.mrbysco.forcecraft.container.engine.ForceEngineContainer;
import com.mrbysco.forcecraft.container.furnace.ForceFurnaceContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForceContainers {
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Reference.MOD_ID);

	public static final RegistryObject<MenuType<ForceFurnaceContainer>> FORCE_FURNACE = CONTAINERS.register("force_furnace", () ->
			IForgeMenuType.create((windowId, inv, data) -> new ForceFurnaceContainer(windowId, inv, data)));

	public static final RegistryObject<MenuType<InfuserContainer>> INFUSER = CONTAINERS.register("infuser", () ->
			IForgeMenuType.create((windowId, inv, data) -> new InfuserContainer(windowId, inv, data)));

	public static final RegistryObject<MenuType<ForcePackContainer>> FORCE_PACK = CONTAINERS.register("force_pack", () ->
			IForgeMenuType.create((windowId, inv, data) -> new ForcePackContainer(windowId, inv)));

	public static final RegistryObject<MenuType<ForceBeltContainer>> FORCE_BELT = CONTAINERS.register("force_belt", () ->
			IForgeMenuType.create((windowId, inv, data) -> new ForceBeltContainer(windowId, inv)));

	public static final RegistryObject<MenuType<SpoilsBagContainer>> SPOILS_BAG = CONTAINERS.register("spoils_bag", () ->
			IForgeMenuType.create((windowId, inv, data) -> new SpoilsBagContainer(windowId, inv)));

	public static final RegistryObject<MenuType<ItemCardContainer>> ITEM_CARD = CONTAINERS.register("item_card", () ->
			IForgeMenuType.create((windowId, inv, data) -> new ItemCardContainer(windowId, inv)));

	public static final RegistryObject<MenuType<ForceEngineContainer>> FORCE_ENGINE = CONTAINERS.register("force_engine", () ->
			IForgeMenuType.create((windowId, inv, data) -> new ForceEngineContainer(windowId, inv, data)));
}
