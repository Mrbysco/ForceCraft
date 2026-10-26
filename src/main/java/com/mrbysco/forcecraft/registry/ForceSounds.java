package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ForceSounds {
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Reference.MOD_ID);

	public static final DeferredHolder<SoundEvent, SoundEvent> WHOOSH = SOUND_EVENTS.register("whoosh", () ->
			SoundEvent.createVariableRangeEvent(Reference.modLoc("whoosh")));

	public static final DeferredHolder<SoundEvent, SoundEvent> FAIRY_PICKUP = SOUND_EVENTS.register("fairy.pickup", () ->
			SoundEvent.createVariableRangeEvent(Reference.modLoc("fairy.pickup")));

	public static final DeferredHolder<SoundEvent, SoundEvent> FAIRY_LISTEN = SOUND_EVENTS.register("fairy.listen", () ->
			SoundEvent.createVariableRangeEvent(Reference.modLoc("fairy.listen")));

	public static final DeferredHolder<SoundEvent, SoundEvent> FAIRY_LISTEN_SPECIAL = SOUND_EVENTS.register("fairy.listen.special", () ->
			SoundEvent.createVariableRangeEvent(Reference.modLoc("fairy.listen.special")));

	public static final DeferredHolder<SoundEvent, SoundEvent> INFUSER_DONE = SOUND_EVENTS.register("infuser.done", () ->
			SoundEvent.createVariableRangeEvent(Reference.modLoc("infuser.done")));

	public static final DeferredHolder<SoundEvent, SoundEvent> INFUSER_WORKING = SOUND_EVENTS.register("infuser.working", () ->
			SoundEvent.createVariableRangeEvent(Reference.modLoc("infuser.working")));

	public static final DeferredHolder<SoundEvent, SoundEvent> INFUSER_SPECIAL = SOUND_EVENTS.register("infuser.special", () ->
			SoundEvent.createVariableRangeEvent(Reference.modLoc("infuser.special")));

	public static final DeferredHolder<SoundEvent, SoundEvent> INFUSER_SPECIAL_BEEP = SOUND_EVENTS.register("infuser.special.beep", () ->
			SoundEvent.createVariableRangeEvent(Reference.modLoc("infuser.special.beep")));

	public static final DeferredHolder<SoundEvent, SoundEvent> INFUSER_SPECIAL_DONE = SOUND_EVENTS.register("infuser.special.done", () ->
			SoundEvent.createVariableRangeEvent(Reference.modLoc("infuser.special.done")));

	public static final DeferredHolder<SoundEvent, SoundEvent> HEART_PICKUP = SOUND_EVENTS.register("heart.pickup", () ->
			SoundEvent.createVariableRangeEvent(Reference.modLoc("heart.pickup")));

	public static final DeferredHolder<SoundEvent, SoundEvent> FORCE_PUNCH = SOUND_EVENTS.register("force.punch", () ->
			SoundEvent.createVariableRangeEvent(Reference.modLoc("force.punch")));

	public static final DeferredHolder<SoundEvent, SoundEvent> FORCE_MAGIC = SOUND_EVENTS.register("force.magic", () ->
			SoundEvent.createVariableRangeEvent(Reference.modLoc("force.magic")));


}
