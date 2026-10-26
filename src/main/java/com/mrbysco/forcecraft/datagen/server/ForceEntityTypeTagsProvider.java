package com.mrbysco.forcecraft.datagen.server;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class ForceEntityTypeTagsProvider extends EntityTypeTagsProvider {
	public ForceEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, Reference.MOD_ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		tag(ForceTags.EntityTypes.VALID_BANE).add(EntityType.CREEPER, EntityType.ENDERMAN);
		tag(ForceTags.EntityTypes.TICKING_NOT_SUPPORTED);
	}
}
