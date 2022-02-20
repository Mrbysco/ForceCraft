package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.menu.infuser.InfuserMenu;
import com.mrbysco.forcecraft.menu.ForceBeltMenu;
import com.mrbysco.forcecraft.menu.ForcePackMenu;
import com.mrbysco.forcecraft.menu.ItemCardMenu;
import com.mrbysco.forcecraft.menu.SpoilsBagMenu;
import com.mrbysco.forcecraft.menu.engine.ForceEngineMenu;
import com.mrbysco.forcecraft.menu.furnace.ForceFurnaceMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForceContainers {
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Reference.MOD_ID);

	public static final RegistryObject<MenuType<ForceFurnaceMenu>> FORCE_FURNACE = CONTAINERS.register("force_furnace", () ->
			IForgeMenuType.create((windowId, inv, data) -> new ForceFurnaceMenu(windowId, inv, data)));

	public static final RegistryObject<MenuType<InfuserMenu>> INFUSER = CONTAINERS.register("infuser", () ->
			IForgeMenuType.create((windowId, inv, data) -> new InfuserMenu(windowId, inv, data)));

	public static final RegistryObject<MenuType<ForcePackMenu>> FORCE_PACK = CONTAINERS.register("force_pack", () ->
			IForgeMenuType.create((windowId, inv, data) -> new ForcePackMenu(windowId, inv)));

	public static final RegistryObject<MenuType<ForceBeltMenu>> FORCE_BELT = CONTAINERS.register("force_belt", () ->
			IForgeMenuType.create((windowId, inv, data) -> new ForceBeltMenu(windowId, inv)));

	public static final RegistryObject<MenuType<SpoilsBagMenu>> SPOILS_BAG = CONTAINERS.register("spoils_bag", () ->
			IForgeMenuType.create((windowId, inv, data) -> new SpoilsBagMenu(windowId, inv)));

	public static final RegistryObject<MenuType<ItemCardMenu>> ITEM_CARD = CONTAINERS.register("item_card", () ->
			IForgeMenuType.create((windowId, inv, data) -> new ItemCardMenu(windowId, inv)));

	public static final RegistryObject<MenuType<ForceEngineMenu>> FORCE_ENGINE = CONTAINERS.register("force_engine", () ->
			IForgeMenuType.create((windowId, inv, data) -> new ForceEngineMenu(windowId, inv, data)));
}
