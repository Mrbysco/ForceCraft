package mrbysco.forcecraft.items;

import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
        if (entityLiving instanceof PlayerEntity && !worldIn.isRemote) {
            PlayerEntity player = (PlayerEntity)entityLiving;
            if(this == ForceRegistry.FORTUNE_COOKIE.get()){
                player.addItemStackToInventory(new ItemStack(ForceRegistry.FORTUNE.get()));
            }
            if(this == ForceRegistry.SOUL_WAFER.get()){
                this.randPotionEffect(player);
            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    public CustomFoodItem randPotionEffect(LivingEntity entityLiving){
        Random rnd = new Random();
        int rand = rnd.nextInt(16);
        PlayerEntity entityplayer = (PlayerEntity)entityLiving;

        switch(rand){
            case 0:
                entityplayer.addPotionEffect(new EffectInstance(Effects.SPEED, 1000));
                break;
            case 1:
                entityplayer.addPotionEffect(new EffectInstance(Effects.HASTE, 1000));
                break;
            case 2:
                entityplayer.addPotionEffect(new EffectInstance(Effects.STRENGTH, 1000));
                break;
            case 3:
                entityplayer.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 1000));
                break;
            case 4:
                entityplayer.addPotionEffect(new EffectInstance(Effects.REGENERATION, 1000));
                break;
            case 5:
                entityplayer.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 1000));
                break;
            case 6:
                entityplayer.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 1000));
                break;
            case 7:
                entityplayer.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 1000));
                break;
            case 8:
                entityplayer.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, 1000));
                break;
            case 9:
                entityplayer.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 1000));
                break;
            case 10:
                entityplayer.addPotionEffect(new EffectInstance(Effects.HEALTH_BOOST, 1000));
                break;
            case 11:
                entityplayer.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 1000));
                break;
            case 12:
                entityplayer.addPotionEffect(new EffectInstance(Effects.SATURATION, 1000));
                break;
            case 13:
                entityplayer.addPotionEffect(new EffectInstance(Effects.GLOWING, 1000));
                break;
            case 14:
                entityplayer.addPotionEffect(new EffectInstance(Effects.LEVITATION, 1000));
                break;
            case 15:
                entityplayer.addPotionEffect(new EffectInstance(Effects.LUCK, 1000));
                break;
        }
        return this;
    }

}
