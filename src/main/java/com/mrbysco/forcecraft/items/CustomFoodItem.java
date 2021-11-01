package com.mrbysco.forcecraft.items;

import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

import java.util.Random;

public class CustomFoodItem extends Item {

    public CustomFoodItem(Item.Properties properties){
        super(properties);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if(this.isFood()) {
            Item item = stack.getItem();
            ItemStack returnStack = entityLiving.onFoodEaten(worldIn, stack);
            if(!worldIn.isRemote) {
                if (entityLiving instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity)entityLiving;
                    if(item.getItem() == ForceRegistry.FORTUNE_COOKIE.get()){
                        ItemStack fortuneItem = new ItemStack(ForceRegistry.FORTUNE.get());
                        FortuneItem.addMessage(fortuneItem, new CompoundNBT());
                        return fortuneItem;
                    } else if(item.getItem() == ForceRegistry.SOUL_WAFER.get()){
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
        PlayerEntity player = (PlayerEntity)entityLiving;

        switch(rand){
            default:
                player.addPotionEffect(new EffectInstance(Effects.SPEED, 1000, 0, false, false));
                break;
            case 1:
                player.addPotionEffect(new EffectInstance(Effects.HASTE, 1000, 0, false, false));
                break;
            case 2:
                player.addPotionEffect(new EffectInstance(Effects.STRENGTH, 1000, 0, false, false));
                break;
            case 3:
                player.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 1000, 0, false, false));
                break;
            case 4:
                player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 1000, 0, false, false));
                break;
            case 5:
                player.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 1000, 0, false, false));
                break;
            case 6:
                player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 1000, 0, false, false));
                break;
            case 7:
                player.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 1000, 0, false, false));
                break;
            case 8:
                player.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, 1000, 0, false, false));
                break;
            case 9:
                player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 1000, 0, false, false));
                break;
            case 10:
                player.addPotionEffect(new EffectInstance(Effects.HEALTH_BOOST, 1000, 0, false, false));
                break;
            case 11:
                player.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 1000, 0, false, false));
                break;
            case 12:
                player.addPotionEffect(new EffectInstance(Effects.SATURATION, 1000, 0, false, false));
                break;
            case 13:
                player.addPotionEffect(new EffectInstance(Effects.GLOWING, 1000, 0, false, false));
                break;
            case 14:
                player.addPotionEffect(new EffectInstance(Effects.LEVITATION, 1000, 0, false, false));
                break;
            case 15:
                player.addPotionEffect(new EffectInstance(Effects.LUCK, 1000, 0, false, false));
                break;
        }
        return this;
    }

}
