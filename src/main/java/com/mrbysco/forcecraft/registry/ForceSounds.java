package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForceSounds {
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Reference.MOD_ID);

	public static final RegistryObject<SoundEvent> WHOOSH = SOUND_EVENTS.register("whoosh", () ->
			SoundEvent.createVariableRangeEvent(new ResourceLocation(Reference.MOD_ID, "whoosh")));

	public static final RegistryObject<SoundEvent> FAIRY_PICKUP = SOUND_EVENTS.register("fairy.pickup", () ->
			SoundEvent.createVariableRangeEvent(new ResourceLocation(Reference.MOD_ID, "fairy.pickup")));

	public static final RegistryObject<SoundEvent> FAIRY_LISTEN = SOUND_EVENTS.register("fairy.listen", () ->
			SoundEvent.createVariableRangeEvent(new ResourceLocation(Reference.MOD_ID, "fairy.listen")));

	public static final RegistryObject<SoundEvent> FAIRY_LISTEN_SPECIAL = SOUND_EVENTS.register("fairy.listen.special", () ->
			SoundEvent.createVariableRangeEvent(new ResourceLocation(Reference.MOD_ID, "fairy.listen.special")));

	public static final RegistryObject<SoundEvent> INFUSER_DONE = SOUND_EVENTS.register("infuser.done", () ->
			SoundEvent.createVariableRangeEvent(new ResourceLocation(Reference.MOD_ID, "infuser.done")));

	public static final RegistryObject<SoundEvent> INFUSER_WORKING = SOUND_EVENTS.register("infuser.working", () ->
			SoundEvent.createVariableRangeEvent(new ResourceLocation(Reference.MOD_ID, "infuser.working")));

	public static final RegistryObject<SoundEvent> INFUSER_SPECIAL = SOUND_EVENTS.register("infuser.special", () ->
			SoundEvent.createVariableRangeEvent(new ResourceLocation(Reference.MOD_ID, "infuser.special")));

	public static final RegistryObject<SoundEvent> INFUSER_SPECIAL_BEEP = SOUND_EVENTS.register("infuser.special.beep", () ->
			SoundEvent.createVariableRangeEvent(new ResourceLocation(Reference.MOD_ID, "infuser.special.beep")));

	public static final RegistryObject<SoundEvent> INFUSER_SPECIAL_DONE = SOUND_EVENTS.register("infuser.special.done", () ->
			SoundEvent.createVariableRangeEvent(new ResourceLocation(Reference.MOD_ID, "infuser.special.done")));

	public static final RegistryObject<SoundEvent> HEART_PICKUP = SOUND_EVENTS.register("heart.pickup", () ->
			SoundEvent.createVariableRangeEvent(new ResourceLocation(Reference.MOD_ID, "heart.pickup")));

	public static final RegistryObject<SoundEvent> FORCE_PUNCH = SOUND_EVENTS.register("force.punch", () ->
			SoundEvent.createVariableRangeEvent(new ResourceLocation(Reference.MOD_ID, "force.punch")));


}
