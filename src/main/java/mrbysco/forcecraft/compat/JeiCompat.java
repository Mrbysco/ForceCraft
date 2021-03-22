package mrbysco.forcecraft.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JeiCompat implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(Reference.MOD_ID, "main");
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
	}
}
