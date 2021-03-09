package mrbysco.forcecraft.handlers;

import mrbysco.forcecraft.capablilities.magnet.IMagnet;
import mrbysco.forcecraft.capablilities.toolmodifier.IToolModifier;
import mrbysco.forcecraft.items.CustomArmorItem;
import mrbysco.forcecraft.items.tools.MagnetGloveItem;
import mrbysco.forcecraft.potion.effects.EffectMagnet;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_MAGNET;
import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_PLAYERMOD;
import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_SHEARABLE;
import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class LivingUpdateHandler {

    public static List<PlayerEntity> flightList = new ArrayList<PlayerEntity>();

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        event.getEntityLiving().getCapability(CAPABILITY_SHEARABLE).ifPresent((cap) -> {
            cap.update();
        });

        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = ((PlayerEntity) event.getEntityLiving());
            Iterable<ItemStack> armor = player.getArmorInventoryList();
            int wings = 0;
            boolean camo = false;
            int speed = 0;
            int damage = 0;
            int heat = 0;
            int luck = 0;
            int bane = 0;
            int bleed = 0;
            for (ItemStack slotSelected : armor) {
                if (slotSelected.getItem() instanceof CustomArmorItem) {
                    IToolModifier modifierCap = slotSelected.getCapability(CAPABILITY_TOOLMOD).orElse(null);
                    if(modifierCap != null) {
                        //Camo
                        if(modifierCap.hasCamo()) {
                            camo = true;
                        }
                        //Speed
                        speed += modifierCap.getSpeedLevel();
                        //Wing
                        if(modifierCap.hasWing()) {
                            wings++;
                        }
                        //Damage
                        damage += modifierCap.getSharpLevel();
                        //Heat
                        if(modifierCap.hasHeat()) {
                            heat++;
                        }
                    }
                }
            }
            //Checks Hotbar
            Iterable<ItemStack> hotBar = player.inventory.mainInventory.subList(0, player.inventory.getHotbarSize());
            for(ItemStack slotSelected : hotBar) {
                if(slotSelected.getItem() instanceof MagnetGloveItem) {
                    IMagnet magnetCap = slotSelected.getCapability(CAPABILITY_MAGNET).orElse(null);
                    if(magnetCap != null && magnetCap.isActivated()) {
                        EffectInstance magnet = new EffectMagnet(20);
                        player.addPotionEffect(magnet);
                    }
                }
            }
            if (!player.isCreative()) {
                if (!player.world.isRemote) {
                    if (wings == 4) {
                        if (!player.isSpectator() && !player.abilities.allowFlying) {
                            player.abilities.allowFlying = true;
                            player.sendPlayerAbilities();
                            flightList.add(player);
                        }
                    } else {
                        if (flightList.contains(player)) {
                            player.abilities.allowFlying = false;
                            player.abilities.isFlying = false;
                            player.sendPlayerAbilities();
                            flightList.remove(player);
                        }
                    }
                    if (camo) {
                        EffectInstance camoEffect = new EffectInstance(Effects.INVISIBILITY, 200, 1);
                        player.addPotionEffect(camoEffect);
                    }
                    if (speed != 0) {
                        EffectInstance speedEffect = new EffectInstance(Effects.SPEED, 200, speed);
                    }
                    if (damage != 0) {
                        int finalDamage = damage;
                        player.getCapability(CAPABILITY_PLAYERMOD).ifPresent((cap) -> {
                            cap.addAttackDamage((float)(.5 * finalDamage));
                        });
                    }
                    if (heat != 0) {
                        int finalHeat = heat;
                        player.getCapability(CAPABILITY_PLAYERMOD).ifPresent((cap) -> {
                            cap.addHeatDamage((float)(.5 * finalHeat));
                        });
                    }
                }
            }
        }
    }
}
