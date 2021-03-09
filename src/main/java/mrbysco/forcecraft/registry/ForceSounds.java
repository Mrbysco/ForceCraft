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
}
