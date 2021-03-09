package mrbysco.forcecraft.registry;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.potion.potions.PotionBleeding;
import mrbysco.forcecraft.potion.potions.PotionMagnet;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ForceEffects {
	public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Reference.MOD_ID);

	public static final RegistryObject<Effect> BLEEDING = EFFECTS.register("bleeding", () -> new PotionBleeding());
	public static final RegistryObject<Effect> MAGNET = EFFECTS.register("magnet", () -> new PotionMagnet());
}
