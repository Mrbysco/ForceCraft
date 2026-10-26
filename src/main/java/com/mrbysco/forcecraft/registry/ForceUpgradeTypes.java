package com.mrbysco.forcecraft.registry;

import com.mojang.serialization.MapCodec;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.blockentity.infuser.InfuserUpgrade;
import com.mrbysco.forcecraft.upgrade.ConvertUpgrade;
import com.mrbysco.forcecraft.upgrade.FlagUpgrade;
import com.mrbysco.forcecraft.upgrade.LeveledUpgrade;
import com.mrbysco.forcecraft.upgrade.PackUpgrade;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

@EventBusSubscriber
public class ForceUpgradeTypes {
	public static final ResourceKey<Registry<MapCodec<? extends InfuserUpgrade>>> KEY =
			ResourceKey.createRegistryKey(Reference.modLoc("infuser_upgrade_type"));

	public static final Registry<MapCodec<? extends InfuserUpgrade>> UPGRADE_TYPES =
			new RegistryBuilder<>(KEY).sync(true).create();

	public static final DeferredRegister<MapCodec<? extends InfuserUpgrade>> TYPES =
			DeferredRegister.create(UPGRADE_TYPES, Reference.MOD_ID);

	public static final Supplier<MapCodec<LeveledUpgrade>> LEVELED = TYPES.register("leveled", () -> LeveledUpgrade.CODEC);
	public static final Supplier<MapCodec<FlagUpgrade>> TOGGLE = TYPES.register("flag", () -> FlagUpgrade.CODEC);
	public static final Supplier<MapCodec<ConvertUpgrade>> CONVERT = TYPES.register("convert", () -> ConvertUpgrade.CODEC);
	public static final Supplier<MapCodec<PackUpgrade>> PACK = TYPES.register("pack", () -> PackUpgrade.CODEC);

	@SubscribeEvent
	public static void onNewRegistry(NewRegistryEvent event) {
		event.register(ForceUpgradeTypes.UPGRADE_TYPES);
	}
}
