package com.mrbysco.forcecraft.items;

import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Random;

public class CustomFoodItem extends Item {

    public CustomFoodItem(Item.Properties properties){
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        if(this.isEdible()) {
            Item item = stack.getItem();
            ItemStack returnStack = entityLiving.eat(worldIn, stack);
            if(!worldIn.isClientSide) {
                if (entityLiving instanceof Player player) {
                    if(item == ForceRegistry.FORTUNE_COOKIE.get()){
                        ItemStack fortuneItem = new ItemStack(ForceRegistry.FORTUNE.get());
                        FortuneItem.addMessage(fortuneItem, new CompoundTag());
                        return fortuneItem;
                    } else if(item == ForceRegistry.SOUL_WAFER.get()){
                        this.randPotionEffect(player);
                    }
                }
            }

            return returnStack;
        }
        return stack;
    }

    public CustomFoodItem randPotionEffect(LivingEntity entityLiving){
        Random rnd = new Random();
        int rand = rnd.nextInt(16);
        Player player = (Player)entityLiving;

        switch (rand) {
            default -> player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1000, 0, false, false));
            case 1 -> player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 1000, 0, false, false));
            case 2 -> player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1000, 0, false, false));
            case 3 -> player.addEffect(new MobEffectInstance(MobEffects.JUMP, 1000, 0, false, false));
            case 4 -> player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 1000, 0, false, false));
            case 5 -> player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1000, 0, false, false));
            case 6 -> player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1000, 0, false, false));
            case 7 -> player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 1000, 0, false, false));
            case 8 -> player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 1000, 0, false, false));
            case 9 -> player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1000, 0, false, false));
            case 10 -> player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 1000, 0, false, false));
            case 11 -> player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 1000, 0, false, false));
            case 12 -> player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 1000, 0, false, false));
            case 13 -> player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 1000, 0, false, false));
            case 14 -> player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 1000, 0, false, false));
            case 15 -> player.addEffect(new MobEffectInstance(MobEffects.LUCK, 1000, 0, false, false));
        }
        return this;
    }

}
