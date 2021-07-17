package mrbysco.forcecraft.registry;

import mrbysco.forcecraft.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ForceSounds {
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Reference.MOD_ID);

	public static final RegistryObject<SoundEvent> WHOOSH = SOUND_EVENTS.register("whoosh", () ->
			new SoundEvent(new ResourceLocation(Reference.MOD_ID, "whoosh")));

	public static final RegistryObject<SoundEvent> FAIRY_PICKUP = SOUND_EVENTS.register("fairy.pickup", () ->
			new SoundEvent(new ResourceLocation(Reference.MOD_ID, "fairy.pickup")));

	public static final RegistryObject<SoundEvent> FAIRY_LISTEN = SOUND_EVENTS.register("fairy.listen", () ->
			new SoundEvent(new ResourceLocation(Reference.MOD_ID, "fairy.listen")));

	public static final RegistryObject<SoundEvent> FAIRY_LISTEN_SPECIAL = SOUND_EVENTS.register("fairy.listen.special", () ->
			new SoundEvent(new ResourceLocation(Reference.MOD_ID, "fairy.listen.special")));

	public static final RegistryObject<SoundEvent> INFUSER_DONE = SOUND_EVENTS.register("infuser.done", () ->
			new SoundEvent(new ResourceLocation(Reference.MOD_ID, "infuser.done")));

	public static final RegistryObject<SoundEvent> INFUSER_WORKING = SOUND_EVENTS.register("infuser.working", () ->
			new SoundEvent(new ResourceLocation(Reference.MOD_ID, "infuser.working")));

	public static final RegistryObject<SoundEvent> INFUSER_SPECIAL = SOUND_EVENTS.register("infuser.special", () ->
			new SoundEvent(new ResourceLocation(Reference.MOD_ID, "infuser.special")));

	public static final RegistryObject<SoundEvent> INFUSER_SPECIAL_BEEP = SOUND_EVENTS.register("infuser.special.beep", () ->
			new SoundEvent(new ResourceLocation(Reference.MOD_ID, "infuser.special.beep")));

	public static final RegistryObject<SoundEvent> INFUSER_SPECIAL_DONE = SOUND_EVENTS.register("infuser.special.done", () ->
			new SoundEvent(new ResourceLocation(Reference.MOD_ID, "infuser.special.done")));

	public static final RegistryObject<SoundEvent> HEART_PICKUP = SOUND_EVENTS.register("heart.pickup", () ->
			new SoundEvent(new ResourceLocation(Reference.MOD_ID, "heart.pickup")));

	public static final RegistryObject<SoundEvent> FORCE_PUNCH = SOUND_EVENTS.register("force.punch", () ->
			new SoundEvent(new ResourceLocation(Reference.MOD_ID, "force.punch")));


}
