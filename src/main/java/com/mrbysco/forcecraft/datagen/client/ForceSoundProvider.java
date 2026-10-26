package com.mrbysco.forcecraft.datagen.client;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceSounds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class ForceSoundProvider extends SoundDefinitionsProvider {

	public ForceSoundProvider(PackOutput output) {
		super(output, Reference.MOD_ID);
	}

	@Override
	public void registerSounds() {
		this.add(ForceSounds.WHOOSH, definition()
				.subtitle(modSubtitle(ForceSounds.WHOOSH.getId()))
				.with(sound(modLoc("whoosh"))));

		this.add(ForceSounds.FAIRY_PICKUP, definition()
				.subtitle(modSubtitle(ForceSounds.FAIRY_PICKUP.getId()))
				.with(sound(modLoc("pickup"))));

		this.add(ForceSounds.FAIRY_LISTEN, definition()
				.subtitle(modSubtitle(ForceSounds.FAIRY_LISTEN.getId()))
				.with(
						sound(modLoc("listen")),
						sound(modLoc("listen2"))
				));

		this.add(ForceSounds.FAIRY_LISTEN_SPECIAL, definition()
				.subtitle(modSubtitle(ForceSounds.FAIRY_LISTEN_SPECIAL.getId()))
				.with(sound(modLoc("listen3"))));

		this.add(ForceSounds.INFUSER_DONE, definition()
				.subtitle(modSubtitle(ForceSounds.INFUSER_DONE.getId()))
				.with(sound(modLoc("infuser_done"))));

		this.add(ForceSounds.INFUSER_WORKING, definition()
				.subtitle(modSubtitle(ForceSounds.INFUSER_WORKING.getId()))
				.with(sound(modLoc("chimes"))));

		this.add(ForceSounds.INFUSER_SPECIAL, definition()
				.subtitle(modSubtitle(ForceSounds.INFUSER_SPECIAL.getId()))
				.with(sound(modLoc("hum"))));

		this.add(ForceSounds.INFUSER_SPECIAL_BEEP, definition()
				.subtitle(modSubtitle(ForceSounds.INFUSER_SPECIAL_BEEP.getId()))
				.with(sound(modLoc("start_beep"))));

		this.add(ForceSounds.INFUSER_SPECIAL_DONE, definition()
				.subtitle(modSubtitle(ForceSounds.INFUSER_SPECIAL_DONE.getId()))
				.with(sound(modLoc("finish"))));

		this.add(ForceSounds.HEART_PICKUP, definition()
				.subtitle(modSubtitle(ForceSounds.HEART_PICKUP.getId()))
				.with(sound(modLoc("pickup"))));

		this.add(ForceSounds.FORCE_PUNCH, definition()
				.subtitle(modSubtitle(ForceSounds.FORCE_PUNCH.getId()))
				.with(sound(modLoc("force_punch"))));

		this.add(ForceSounds.FORCE_MAGIC, definition()
				.subtitle(modSubtitle(ForceSounds.FORCE_MAGIC.getId()))
				.with(sound(modLoc("magic"))));
	}

	public String modSubtitle(Identifier id) {
		return Reference.MOD_ID + ".subtitle." + id.getPath();
	}

	public Identifier modLoc(String name) {
		return Reference.modLoc(name);
	}
}
