package com.mrbysco.forcecraft.capablilities;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.capablilities.banemodifier.BaneFactory;
import com.mrbysco.forcecraft.capablilities.banemodifier.BaneModifierStorage;
import com.mrbysco.forcecraft.capablilities.banemodifier.IBaneModifier;
import com.mrbysco.forcecraft.capablilities.experiencetome.ExperienceTomeFactory;
import com.mrbysco.forcecraft.capablilities.experiencetome.ExperienceTomeStorage;
import com.mrbysco.forcecraft.capablilities.experiencetome.IExperienceTome;
import com.mrbysco.forcecraft.capablilities.forcerod.ForceRodFactory;
import com.mrbysco.forcecraft.capablilities.forcerod.ForceRodStorage;
import com.mrbysco.forcecraft.capablilities.forcerod.IForceRodModifier;
import com.mrbysco.forcecraft.capablilities.forcewrench.ForceWrenchFactory;
import com.mrbysco.forcecraft.capablilities.forcewrench.ForceWrenchStorage;
import com.mrbysco.forcecraft.capablilities.forcewrench.IForceWrench;
import com.mrbysco.forcecraft.capablilities.magnet.IMagnet;
import com.mrbysco.forcecraft.capablilities.magnet.MagnetFactory;
import com.mrbysco.forcecraft.capablilities.magnet.MagnetStorage;
import com.mrbysco.forcecraft.capablilities.playermodifier.IPlayerModifier;
import com.mrbysco.forcecraft.capablilities.playermodifier.PlayerModifierFactory;
import com.mrbysco.forcecraft.capablilities.playermodifier.PlayerModifierStorage;
import com.mrbysco.forcecraft.capablilities.toolmodifier.IToolModifier;
import com.mrbysco.forcecraft.capablilities.toolmodifier.ToolFactory;
import com.mrbysco.forcecraft.capablilities.toolmodifier.ToolModStorage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityHandler {

    public static final ResourceLocation BANE_CAP = new ResourceLocation(Reference.MOD_ID, "banemod");
    public static final ResourceLocation PLAYER_CAP = new ResourceLocation(Reference.MOD_ID, "playermod");

    @CapabilityInject(IToolModifier.class)
    public static Capability<IToolModifier> CAPABILITY_TOOLMOD = null;

    @CapabilityInject(IForceRodModifier.class)
    public static Capability<IForceRodModifier> CAPABILITY_FORCEROD = null;

    @CapabilityInject(IExperienceTome.class)
    public static Capability<IExperienceTome> CAPABILITY_EXPTOME = null;

    @CapabilityInject(IBaneModifier.class)
    public static Capability<IBaneModifier> CAPABILITY_BANE = null;

    @CapabilityInject(IPlayerModifier.class)
    public static Capability<IPlayerModifier> CAPABILITY_PLAYERMOD = null;

    @CapabilityInject(IForceWrench.class)
    public static Capability<IForceWrench> CAPABILITY_FORCEWRENCH = null;

    @CapabilityInject(IMagnet.class)
    public static Capability<IMagnet> CAPABILITY_MAGNET = null;

    public static void register(){
        CapabilityManager.INSTANCE.register(IToolModifier.class, new ToolModStorage(), new ToolFactory());
        CapabilityManager.INSTANCE.register(IForceRodModifier.class, new ForceRodStorage(), new ForceRodFactory());
        CapabilityManager.INSTANCE.register(IExperienceTome.class, new ExperienceTomeStorage(), new ExperienceTomeFactory());
        CapabilityManager.INSTANCE.register(IBaneModifier.class, new BaneModifierStorage(), new BaneFactory());
        CapabilityManager.INSTANCE.register(IForceWrench.class, new ForceWrenchStorage(), new ForceWrenchFactory());
        CapabilityManager.INSTANCE.register(IPlayerModifier.class, new PlayerModifierStorage(), new PlayerModifierFactory());
        CapabilityManager.INSTANCE.register(IMagnet.class, new MagnetStorage(), new MagnetFactory());

        MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
    }
}
