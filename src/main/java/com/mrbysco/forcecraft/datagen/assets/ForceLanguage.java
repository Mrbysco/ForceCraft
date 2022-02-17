package com.mrbysco.forcecraft.datagen.assets;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ForceLanguage extends LanguageProvider {
	public ForceLanguage(DataGenerator gen) {
		super(gen, Reference.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		//TODO: Make translations generate
	}
}