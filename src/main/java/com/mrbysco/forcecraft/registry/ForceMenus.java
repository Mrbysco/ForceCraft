package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.menu.ForceBeltMenu;
import com.mrbysco.forcecraft.menu.ForcePackMenu;
import com.mrbysco.forcecraft.menu.ItemCardMenu;
import com.mrbysco.forcecraft.menu.SpoilsBagMenu;
import com.mrbysco.forcecraft.menu.engine.ForceEngineMenu;
import com.mrbysco.forcecraft.menu.furnace.ForceFurnaceMenu;
import com.mrbysco.forcecraft.menu.infuser.InfuserMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ForceMenus {
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, Reference.MOD_ID);

	public static final Supplier<MenuType<ForceFurnaceMenu>> FORCE_FURNACE = MENU_TYPES.register("force_furnace", () ->
			IMenuTypeExtension.create(ForceFurnaceMenu::new));

	public static final Supplier<MenuType<InfuserMenu>> INFUSER = MENU_TYPES.register("infuser", () ->
			IMenuTypeExtension.create(InfuserMenu::new));

	public static final Supplier<MenuType<ForcePackMenu>> FORCE_PACK = MENU_TYPES.register("force_pack", () ->
			IMenuTypeExtension.create(ForcePackMenu::fromNetwork));

	public static final Supplier<MenuType<ForceBeltMenu>> FORCE_BELT = MENU_TYPES.register("force_belt", () ->
			IMenuTypeExtension.create(ForceBeltMenu::fromNetwork));

	public static final Supplier<MenuType<SpoilsBagMenu>> SPOILS_BAG = MENU_TYPES.register("spoils_bag", () ->
			IMenuTypeExtension.create((windowId, inv, data) -> new SpoilsBagMenu(windowId, inv)));

	public static final Supplier<MenuType<ItemCardMenu>> ITEM_CARD = MENU_TYPES.register("item_card", () ->
			IMenuTypeExtension.create((windowId, inv, data) -> new ItemCardMenu(windowId, inv)));

	public static final Supplier<MenuType<ForceEngineMenu>> FORCE_ENGINE = MENU_TYPES.register("force_engine", () ->
			IMenuTypeExtension.create(ForceEngineMenu::new));
}
