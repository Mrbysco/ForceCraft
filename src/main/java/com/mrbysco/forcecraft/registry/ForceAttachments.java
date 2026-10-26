package com.mrbysco.forcecraft.registry;

import com.mojang.serialization.Codec;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.data.TimeData;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ForceAttachments {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Reference.MOD_ID);

	public static final Supplier<AttachmentType<Boolean>> BANE = ATTACHMENT_TYPES.register(
			"bane", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL.fieldOf("bane")).build());

	public static final Supplier<AttachmentType<TimeData>> TIME_ADJUSTMENT = ATTACHMENT_TYPES.register("time_adjustment",
			() -> AttachmentType.builder(() -> new TimeData(0L, 1.0F)).serialize(TimeData.CODEC.fieldOf("time_adjustment")).build());
}
