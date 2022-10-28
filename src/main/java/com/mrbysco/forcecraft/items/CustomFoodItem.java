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

	public CustomFoodItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		if (this.isEdible()) {
			Item item = stack.getItem();
			ItemStack returnStack = entityLiving.eat(worldIn, stack);
			if (!worldIn.isClientSide) {
				if (entityLiving instanceof PlayerEntity) {
					PlayerEntity player = (PlayerEntity) entityLiving;
					if (item.getItem() == ForceRegistry.FORTUNE_COOKIE.get()) {
						ItemStack fortuneItem = new ItemStack(ForceRegistry.FORTUNE.get());
						FortuneItem.addMessage(fortuneItem, new CompoundNBT());
						return fortuneItem;
					} else if (item.getItem() == ForceRegistry.SOUL_WAFER.get()) {
						this.randPotionEffect(player);
					}
				}
			}

			return returnStack;
		}
		return stack;
	}

	public CustomFoodItem randPotionEffect(LivingEntity entityLiving) {
		Random rnd = new Random();
		int rand = rnd.nextInt(16);
		PlayerEntity player = (PlayerEntity) entityLiving;

		switch (rand) {
			default:
				player.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 1000, 0, false, false));
				break;
			case 1:
				player.addEffect(new EffectInstance(Effects.DIG_SPEED, 1000, 0, false, false));
				break;
			case 2:
				player.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 1000, 0, false, false));
				break;
			case 3:
				player.addEffect(new EffectInstance(Effects.JUMP, 1000, 0, false, false));
				break;
			case 4:
				player.addEffect(new EffectInstance(Effects.REGENERATION, 1000, 0, false, false));
				break;
			case 5:
				player.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 1000, 0, false, false));
				break;
			case 6:
				player.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 1000, 0, false, false));
				break;
			case 7:
				player.addEffect(new EffectInstance(Effects.WATER_BREATHING, 1000, 0, false, false));
				break;
			case 8:
				player.addEffect(new EffectInstance(Effects.INVISIBILITY, 1000, 0, false, false));
				break;
			case 9:
				player.addEffect(new EffectInstance(Effects.NIGHT_VISION, 1000, 0, false, false));
				break;
			case 10:
				player.addEffect(new EffectInstance(Effects.HEALTH_BOOST, 1000, 0, false, false));
				break;
			case 11:
				player.addEffect(new EffectInstance(Effects.ABSORPTION, 1000, 0, false, false));
				break;
			case 12:
				player.addEffect(new EffectInstance(Effects.SATURATION, 1000, 0, false, false));
				break;
			case 13:
				player.addEffect(new EffectInstance(Effects.GLOWING, 1000, 0, false, false));
				break;
			case 14:
				player.addEffect(new EffectInstance(Effects.LEVITATION, 1000, 0, false, false));
				break;
			case 15:
				player.addEffect(new EffectInstance(Effects.LUCK, 1000, 0, false, false));
				break;
		}
		return this;
	}

}
