package mrbysco.forcecraft.registry;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.lootmodifiers.SmeltingModifier;
import mrbysco.forcecraft.lootmodifiers.SmeltingModifier.Serializer;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ForceLootModifiers {
	public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLM = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, Reference.MOD_ID);

	private static final RegistryObject<Serializer> SMELTING = GLM.register("smelting", SmeltingModifier.Serializer::new);
}
