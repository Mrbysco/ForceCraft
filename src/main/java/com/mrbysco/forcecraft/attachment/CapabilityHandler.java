package com.mrbysco.forcecraft.attachment;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.attachment.banemodifier.BaneModifierAttachment;
import com.mrbysco.forcecraft.attachment.experiencetome.ExperienceTomeAttachment;
import com.mrbysco.forcecraft.attachment.forcerod.ForceRodAttachment;
import com.mrbysco.forcecraft.attachment.forcewrench.ForceWrenchAttachment;
import com.mrbysco.forcecraft.attachment.magnet.MagnetAttachment;
import com.mrbysco.forcecraft.attachment.playermodifier.PlayerModifierAttachment;
import com.mrbysco.forcecraft.attachment.storage.StorageManager;
import com.mrbysco.forcecraft.attachment.toolmodifier.ToolModifierAttachment;
import com.mrbysco.forcecraft.blockentities.ForceEngineBlockEntity;
import com.mrbysco.forcecraft.blockentities.InfuserBlockEntity;
import com.mrbysco.forcecraft.items.flask.FlaskFluidHandler;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class CapabilityHandler {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Reference.MOD_ID);

	public static final Supplier<AttachmentType<ToolModifierAttachment>> TOOL_MODIFIER = ATTACHMENT_TYPES.register("tool_modifier", () -> AttachmentType.serializable(ToolModifierAttachment::new).build());
	public static final Supplier<AttachmentType<ForceRodAttachment>> FORCE_ROD = ATTACHMENT_TYPES.register("force_rod", () -> AttachmentType.serializable(ForceRodAttachment::new).build());
	public static final Supplier<AttachmentType<ExperienceTomeAttachment>> EXP_TOME = ATTACHMENT_TYPES.register("exp_tome", () -> AttachmentType.serializable(ExperienceTomeAttachment::new).build());
	public static final Supplier<AttachmentType<BaneModifierAttachment>> BANE_MODIFIER = ATTACHMENT_TYPES.register("bane_modifier", () -> AttachmentType.serializable(BaneModifierAttachment::new).build());
	public static final Supplier<AttachmentType<PlayerModifierAttachment>> PLAYER_MOD = ATTACHMENT_TYPES.register("player_mod", () -> AttachmentType.serializable(PlayerModifierAttachment::new).build());
	public static final Supplier<AttachmentType<ForceWrenchAttachment>> FORCE_WRENCH = ATTACHMENT_TYPES.register("force_wrench", () -> AttachmentType.serializable(ForceWrenchAttachment::new).build());
	public static final Supplier<AttachmentType<MagnetAttachment>> MAGNET = ATTACHMENT_TYPES.register("magnet", () -> AttachmentType.serializable(MagnetAttachment::new).build());

	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ForceRegistry.INFUSER_BLOCK_ENTITY.get(), InfuserBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ForceRegistry.INFUSER_BLOCK_ENTITY.get(), InfuserBlockEntity::getFluidTank);
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ForceRegistry.INFUSER_BLOCK_ENTITY.get(), InfuserBlockEntity::getEnergyStorage);

		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ForceRegistry.FURNACE_BLOCK_ENTITY.get(), (sidedContainer, side) ->
				side == null ? new InvWrapper(sidedContainer) : new SidedInvWrapper(sidedContainer, side));

		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ForceRegistry.FORCE_ENGINE_BLOCK_ENTITY.get(), ForceEngineBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ForceRegistry.FORCE_ENGINE_BLOCK_ENTITY.get(), ForceEngineBlockEntity::getFluidTank);

		event.registerItem(Capabilities.FluidHandler.ITEM, (stack, unused) -> new FlaskFluidHandler(stack),
				ForceRegistry.FORCE_FLASK.get(),
				ForceRegistry.FORCE_FILLED_FORCE_FLASK.get(),
				ForceRegistry.MILK_FORCE_FLASK.get());
		event.registerItem(Capabilities.FluidHandler.ITEM, (stack, unused) -> new FluidBucketWrapper(stack),
				ForceRegistry.BUCKET_FLUID_FORCE.get());
		event.registerItem(Capabilities.ItemHandler.ITEM, (stack, unused) -> new ItemStackHandler(4), ForceRegistry.BACONATOR.get());
		event.registerItem(Capabilities.ItemHandler.ITEM, (stack, unused) -> new ItemStackHandler(8),
				ForceRegistry.SPOILS_BAG.get(), ForceRegistry.SPOILS_BAG_T2.get(), ForceRegistry.SPOILS_BAG_T3.get());

		event.registerItem(Capabilities.ItemHandler.ITEM, (stack, unused) -> StorageManager.getCapability(stack), ForceRegistry.FORCE_BELT.get(), ForceRegistry.FORCE_PACK.get());
	}
}
