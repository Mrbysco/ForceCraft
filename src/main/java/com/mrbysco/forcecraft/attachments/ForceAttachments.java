package com.mrbysco.forcecraft.attachments;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.attachments.banemodifier.BaneModifierAttachment;
import com.mrbysco.forcecraft.attachments.playermodifier.PlayerModifierAttachment;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ForceAttachments {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Reference.MOD_ID);

	public static final Supplier<AttachmentType<BaneModifierAttachment>> BANE_MODIFIER = ATTACHMENT_TYPES.register("bane_modifier", () ->
			AttachmentType.serializable(BaneModifierAttachment::new).build());
	public static final Supplier<AttachmentType<PlayerModifierAttachment>> PLAYER_MOD = ATTACHMENT_TYPES.register("player_mod", () ->
			AttachmentType.serializable(PlayerModifierAttachment::new).build());
}
