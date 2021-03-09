package mrbysco.forcecraft.capablilities;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.capablilities.banemodifier.BaneFactory;
import mrbysco.forcecraft.capablilities.banemodifier.BaneModifierStorage;
import mrbysco.forcecraft.capablilities.banemodifier.IBaneModifier;
import mrbysco.forcecraft.capablilities.experiencetome.ExperienceTomeFactory;
import mrbysco.forcecraft.capablilities.experiencetome.ExperienceTomeStorage;
import mrbysco.forcecraft.capablilities.experiencetome.IExperienceTome;
import mrbysco.forcecraft.capablilities.forcerod.ForceRodFactory;
import mrbysco.forcecraft.capablilities.forcerod.ForceRodStorage;
import mrbysco.forcecraft.capablilities.forcerod.IForceRodModifier;
import mrbysco.forcecraft.capablilities.forcewrench.ForceWrenchFactory;
import mrbysco.forcecraft.capablilities.forcewrench.ForceWrenchStorage;
import mrbysco.forcecraft.capablilities.forcewrench.IForceWrench;
import mrbysco.forcecraft.capablilities.magnet.IMagnet;
import mrbysco.forcecraft.capablilities.magnet.MagnetFactory;
import mrbysco.forcecraft.capablilities.magnet.MagnetStorage;
import mrbysco.forcecraft.capablilities.playermodifier.IPlayerModifier;
import mrbysco.forcecraft.capablilities.playermodifier.PlayerModifierFactory;
import mrbysco.forcecraft.capablilities.playermodifier.PlayerModifierStorage;
import mrbysco.forcecraft.capablilities.shearable.IShearableMob;
import mrbysco.forcecraft.capablilities.shearable.ShearableFactory;
import mrbysco.forcecraft.capablilities.shearable.ShearableStorage;
import mrbysco.forcecraft.capablilities.toolmodifier.IToolModifier;
import mrbysco.forcecraft.capablilities.toolmodifier.ToolFactory;
import mrbysco.forcecraft.capablilities.toolmodifier.ToolModStorage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityHandler {

    public static final ResourceLocation BANE_CAP = new ResourceLocation(Reference.MOD_ID, "banemod");
    public static final ResourceLocation PLAYER_CAP = new ResourceLocation(Reference.MOD_ID, "playermod");
    public static final ResourceLocation SHEAR_CAP = new ResourceLocation(Reference.MOD_ID, "shearable");

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

    @CapabilityInject(IShearableMob.class)
    public static Capability<IShearableMob> CAPABILITY_SHEARABLE = null;

    @CapabilityInject(IForceWrench.class)
    public static Capability<IForceWrench> CAPABILITY_FORCEWRENCH = null;

    @CapabilityInject(IMagnet.class)
    public static Capability<IMagnet> CAPABILITY_MAGNET = null;

    public static void register(){
        CapabilityManager.INSTANCE.register(IToolModifier.class, new ToolModStorage(), new ToolFactory());
        CapabilityManager.INSTANCE.register(IForceRodModifier.class, new ForceRodStorage(), new ForceRodFactory());
        CapabilityManager.INSTANCE.register(IExperienceTome.class, new ExperienceTomeStorage(), new ExperienceTomeFactory());
        CapabilityManager.INSTANCE.register(IBaneModifier.class, new BaneModifierStorage(), new BaneFactory());
        CapabilityManager.INSTANCE.register(IShearableMob.class, new ShearableStorage(), new ShearableFactory());
        CapabilityManager.INSTANCE.register(IForceWrench.class, new ForceWrenchStorage(), new ForceWrenchFactory());
        CapabilityManager.INSTANCE.register(IPlayerModifier.class, new PlayerModifierStorage(), new PlayerModifierFactory());
        CapabilityManager.INSTANCE.register(IMagnet.class, new MagnetStorage(), new MagnetFactory());

        MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
    }
}
