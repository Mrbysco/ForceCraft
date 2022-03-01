package com.mrbysco.forcecraft.capabilities;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.capabilities.banemodifier.IBaneModifier;
import com.mrbysco.forcecraft.capabilities.experiencetome.IExperienceTome;
import com.mrbysco.forcecraft.capabilities.forcerod.IForceRodModifier;
import com.mrbysco.forcecraft.capabilities.forcewrench.IForceWrench;
import com.mrbysco.forcecraft.capabilities.magnet.IMagnet;
import com.mrbysco.forcecraft.capabilities.playermodifier.IPlayerModifier;
import com.mrbysco.forcecraft.capabilities.toolmodifier.IToolModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CapabilityHandler {

    public static final ResourceLocation BANE_CAP = new ResourceLocation(Reference.MOD_ID, "banemod");
    public static final ResourceLocation PLAYER_CAP = new ResourceLocation(Reference.MOD_ID, "playermod");

    public static final Capability<IToolModifier> CAPABILITY_TOOLMOD = CapabilityManager.get(new CapabilityToken<>() {});

    public static final Capability<IForceRodModifier> CAPABILITY_FORCEROD = CapabilityManager.get(new CapabilityToken<>() {});

    public static final Capability<IExperienceTome> CAPABILITY_EXPTOME = CapabilityManager.get(new CapabilityToken<>() {});

    public static final Capability<IBaneModifier> CAPABILITY_BANE = CapabilityManager.get(new CapabilityToken<>() {});

    public static final Capability<IPlayerModifier> CAPABILITY_PLAYERMOD = CapabilityManager.get(new CapabilityToken<>() {});

    public static final Capability<IForceWrench> CAPABILITY_FORCEWRENCH = CapabilityManager.get(new CapabilityToken<>() {});

    public static final Capability<IMagnet> CAPABILITY_MAGNET = CapabilityManager.get(new CapabilityToken<>() {});

    public static void register(RegisterCapabilitiesEvent event){
        event.register(IToolModifier.class);
        event.register(IForceRodModifier.class);
        event.register(IExperienceTome.class);
        event.register(IBaneModifier.class);
        event.register(IForceWrench.class);
        event.register(IPlayerModifier.class);
        event.register(IMagnet.class);
    }
}
