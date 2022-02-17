package com.mrbysco.forcecraft.capablilities;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.capablilities.banemodifier.IBaneModifier;
import com.mrbysco.forcecraft.capablilities.experiencetome.IExperienceTome;
import com.mrbysco.forcecraft.capablilities.forcerod.IForceRodModifier;
import com.mrbysco.forcecraft.capablilities.forcewrench.IForceWrench;
import com.mrbysco.forcecraft.capablilities.magnet.IMagnet;
import com.mrbysco.forcecraft.capablilities.playermodifier.IPlayerModifier;
import com.mrbysco.forcecraft.capablilities.toolmodifier.IToolModifier;
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
