package com.mrbysco.forcecraft.datagen.server;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceFluids;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ForceFluidTagsProvider extends FluidTagsProvider {
	public ForceFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, Reference.MOD_ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		this.tag(ForceTags.Fluids.BIOFUEL);
		this.tag(ForceTags.Fluids.FORCE).add(
				ForceFluids.FORCE_FLUID_SOURCE.get(),
				ForceFluids.FORCE_FLUID_FLOWING.get()
		);
		this.tag(ForceTags.Fluids.FUEL);
	}
}
