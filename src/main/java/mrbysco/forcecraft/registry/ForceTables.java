package mrbysco.forcecraft.registry;

import mrbysco.forcecraft.Reference;
import net.minecraft.util.ResourceLocation;

public class ForceTables {
	public static final ResourceLocation TIER_1 = register("spoils/tier1");
	public static final ResourceLocation TIER_2 = register("spoils/tier2");
	public static final ResourceLocation TIER_3 = register("spoils/tier3");

	private static ResourceLocation register(String id) {
		return new ResourceLocation(Reference.MOD_ID, id);
	}
}
