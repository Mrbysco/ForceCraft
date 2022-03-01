package com.mrbysco.forcecraft.items;

import com.mrbysco.forcecraft.capabilities.experiencetome.ExperienceTomeCapability;
import com.mrbysco.forcecraft.util.ForceUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;

import static com.mrbysco.forcecraft.capabilities.CapabilityHandler.CAPABILITY_EXPTOME;

public class ExperienceTomeItem extends Item {

    public static final int CAPACITY = Integer.MAX_VALUE;

    public ExperienceTomeItem(Item.Properties properties){
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        if(!Screen.hasShiftDown()){
  		  tooltip.add(new TranslatableComponent("forcecraft.tooltip.press_shift"));
            return;
        }
        tooltip.add(new TextComponent(Float.toString(getExperience(stack)) + " / " + Float.toString(getMaxExperience(stack))));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if(CAPABILITY_EXPTOME == null) {
            return null;
        }
        return new ExperienceTomeCapability();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (ForceUtils.isFakePlayer(player) || hand != InteractionHand.MAIN_HAND || world.isClientSide) {
            return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
        }
        int exp;
        int curLevel = player.experienceLevel;

        if (player.isShiftKeyDown()) {
            if (getExtraPlayerExperience(player) > 0) {
                exp = Math.min(getTotalExpForLevel(player.experienceLevel + 1) - getTotalExpForLevel(player.experienceLevel) - getExtraPlayerExperience(player), getExperience(stack));
            } else {
                exp = Math.min(getTotalExpForLevel(player.experienceLevel + 1) - getTotalExpForLevel(player.experienceLevel), getExperience(stack));
            }
            setPlayerExperience(player, getPlayerExperience(player) + exp);
            if (player.experienceLevel < curLevel + 1 && getPlayerExperience(player) >= getTotalExpForLevel(curLevel + 1)) {
                setPlayerLevel(player, curLevel + 1);
            }
            modifyExperience(stack, -exp);
        } else {
            if (getExtraPlayerExperience(player) > 0) {
                exp = Math.min(getExtraPlayerExperience(player), getSpace(stack));
                setPlayerExperience(player, getPlayerExperience(player) - exp);
                if (player.experienceLevel < curLevel) {
                    setPlayerLevel(player, curLevel);
                }
                modifyExperience(stack, exp);
            } else if (player.experienceLevel > 0) {
                exp = Math.min(getTotalExpForLevel(player.experienceLevel) - getTotalExpForLevel(player.experienceLevel - 1), getSpace(stack));
                setPlayerExperience(player, getPlayerExperience(player) - exp);
                if (player.experienceLevel < curLevel - 1) {
                    setPlayerLevel(player, curLevel - 1);
                }
                modifyExperience(stack, exp);
            }
        }
        return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
    }

    public static int getPlayerExperience(Player player) {
        return getTotalExpForLevel(player.experienceLevel) + getExtraPlayerExperience(player);
    }

    public static int getLevelPlayerExperience(Player player) {
        return getTotalExpForLevel(player.experienceLevel);
    }

    public static int getExtraPlayerExperience(Player player) {
        return Math.round(player.experienceProgress * player.getXpNeededForNextLevel());
    }

    public static void setPlayerExperience(Player player, int exp) {
        player.experienceLevel = 0;
        player.experienceProgress = 0.0F;
        player.totalExperience = 0;

        addExperienceToPlayer(player, exp);
    }

    public static void setPlayerLevel(Player player, int level) {
        player.experienceLevel = level;
        player.experienceProgress = 0.0F;
    }

    public static void addExperienceToPlayer(Player player, int exp) {
        int i = Integer.MAX_VALUE - player.totalExperience;

        if (exp > i) {
            exp = i;
        }
        player.experienceProgress += (float) exp / (float) player.getXpNeededForNextLevel();
        for (player.totalExperience += exp; player.experienceProgress >= 1.0F; player.experienceProgress /= (float) player.getXpNeededForNextLevel()) {
            player.experienceProgress = (player.experienceProgress - 1.0F) * (float) player.getXpNeededForNextLevel();
            addExperienceLevelToPlayer(player, 1);
        }
    }

    public static void addExperienceLevelToPlayer(Player player, int levels) {
        player.experienceLevel += levels;

        if (player.experienceLevel < 0) {
            player.experienceLevel = 0;
            player.experienceProgress = 0.0F;
            player.totalExperience = 0;
        }
    }

    public static int getTotalExpForLevel(int level) {
        return level >= 32 ? (9 * level * level - 325 * level + 4440) / 2 : level >= 17 ? (5 * level * level - 81 * level + 720) / 2 : (level * level + 6 * level);
    }

    public static int modifyExperience(ItemStack stack, int exp) {
        int storedExp = getExperience(stack) + exp;

        if (storedExp > getMaxExperience(stack)) {
            storedExp = getMaxExperience(stack);
        } else if (storedExp < 0) {
            storedExp = 0;
        }
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("Experience", storedExp);
        stack.setTag(tag);
        return storedExp;
    }

    public static int getExperience(ItemStack stack) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundTag());
        }
        return stack.getTag().getInt("Experience");
    }

    public static int getMaxExperience(ItemStack stack) {
        return CAPACITY;
    }

    public static int getSpace(ItemStack stack) {
        return getMaxExperience(stack) - getExperience(stack);
    }
}
