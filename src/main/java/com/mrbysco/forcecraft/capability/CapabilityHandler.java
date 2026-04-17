package com.mrbysco.forcecraft.capability;

import com.mrbysco.forcecraft.blockentities.ForceEngineBlockEntity;
import com.mrbysco.forcecraft.blockentities.InfuserBlockEntity;
import com.mrbysco.forcecraft.components.ForceComponents;
import com.mrbysco.forcecraft.components.storage.StorageManager;
import com.mrbysco.forcecraft.items.flask.FlaskFluidHandler;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;

public class CapabilityHandler {

	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ForceRegistry.INFUSER_BLOCK_ENTITY.get(), InfuserBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.Fluid.BLOCK, ForceRegistry.INFUSER_BLOCK_ENTITY.get(), InfuserBlockEntity::getFluidTank);
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ForceRegistry.INFUSER_BLOCK_ENTITY.get(), InfuserBlockEntity::getEnergyStorage);

		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ForceRegistry.FURNACE_BLOCK_ENTITY.get(), (sidedContainer, side) ->
				side == null ? new InvWrapper(sidedContainer) : new SidedInvWrapper(sidedContainer, side));

		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ForceRegistry.FORCE_ENGINE_BLOCK_ENTITY.get(), ForceEngineBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.Fluid.BLOCK, ForceRegistry.FORCE_ENGINE_BLOCK_ENTITY.get(), ForceEngineBlockEntity::getFluidTank);

		event.registerItem(Capabilities.Fluid.ITEM, (stack, unused) -> new FlaskFluidHandler(ForceComponents.FLASK_FLUID, stack),
				ForceRegistry.FORCE_FLASK,
				ForceRegistry.FORCE_FILLED_FORCE_FLASK,
				ForceRegistry.MILK_FORCE_FLASK);
		event.registerItem(Capabilities.Fluid.ITEM, (stack, unused) -> new FluidBucketWrapper(stack),
				ForceRegistry.BUCKET_FLUID_FORCE);
		event.registerItem(Capabilities.ItemHandler.ITEM, (stack, context) ->
						new ComponentItemHandler(stack, ForceComponents.STORED_FOOD.get(), 4),
				ForceRegistry.BACONATOR
		);
		event.registerItem(Capabilities.ItemHandler.ITEM, (stack, context) ->
						new ComponentItemHandler(stack, ForceComponents.SPOILS_CONTENT.get(), 8),
				ForceRegistry.SPOILS_BAG, ForceRegistry.SPOILS_BAG_T2, ForceRegistry.SPOILS_BAG_T3);

		event.registerItem(Capabilities.ItemHandler.ITEM, (stack, unused) -> StorageManager.getCapability(stack),
				ForceRegistry.FORCE_BELT, ForceRegistry.FORCE_PACK);
	}
}
