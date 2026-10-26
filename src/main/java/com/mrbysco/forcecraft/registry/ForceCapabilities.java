package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.blockentity.ForceEngineBlockEntity;
import com.mrbysco.forcecraft.blockentity.infuser.InfuserBlockEntity;
import com.mrbysco.forcecraft.storage.StorageManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.fluid.BucketResourceHandler;
import net.neoforged.neoforge.transfer.item.WorldlyContainerWrapper;

@EventBusSubscriber
public class ForceCapabilities {
	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.Item.BLOCK, ForceRegistry.INFUSER_BLOCK_ENTITY.get(), InfuserBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.Fluid.BLOCK, ForceRegistry.INFUSER_BLOCK_ENTITY.get(), InfuserBlockEntity::getFluidTank);
		event.registerBlockEntity(Capabilities.Energy.BLOCK, ForceRegistry.INFUSER_BLOCK_ENTITY.get(), InfuserBlockEntity::getEnergyStorage);

		event.registerBlockEntity(Capabilities.Item.BLOCK, ForceRegistry.FORCE_ENGINE_BLOCK_ENTITY.get(), ForceEngineBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.Fluid.BLOCK, ForceRegistry.FORCE_ENGINE_BLOCK_ENTITY.get(), ForceEngineBlockEntity::getFluidTank);
		event.registerItem(Capabilities.Fluid.ITEM, (stack, access) -> new BucketResourceHandler(access),
				ForceRegistry.FORCE_BUCKET);

		event.registerBlockEntity(Capabilities.Item.BLOCK, ForceRegistry.FURNACE_BLOCK_ENTITY.get(), WorldlyContainerWrapper::new);

		event.registerItem(Capabilities.Item.ITEM, (stack, access) -> StorageManager.getCapability(stack),
				ForceRegistry.FORCE_BELT, ForceRegistry.FORCE_PACK);
	}
}