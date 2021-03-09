package mrbysco.forcecraft.items;

import mrbysco.forcecraft.capablilities.experiencetome.ExperienceTomeProvider;
import mrbysco.forcecraft.util.DartUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_EXPTOME;

public class ExperienceTomeItem extends Item {

    public static final int CAPACITY = Integer.MAX_VALUE;

    public ExperienceTomeItem(Item.Properties properties){
        super(properties);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(!Screen.hasShiftDown()){
            tooltip.add(new StringTextComponent("Press Shift for Details"));
            return;
        }
        tooltip.add(new StringTextComponent(Float.toString(getExperience(stack)) + " / " + Float.toString(getMaxExperience(stack))));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if(CAPABILITY_EXPTOME == null) {
            return null;
        }
        return new ExperienceTomeProvider();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (DartUtils.isFakePlayer(player) || hand != Hand.MAIN_HAND || world.isRemote) {
            return new ActionResult<>(ActionResultType.FAIL, stack);
        }
        int exp;
        int curLevel = player.experienceLevel;

        if (player.isSneaking()) {
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
        return new ActionResult<>(ActionResultType.FAIL, stack);
    }

    public static int getPlayerExperience(PlayerEntity player) {
        return getTotalExpForLevel(player.experienceLevel) + getExtraPlayerExperience(player);
    }

    public static int getLevelPlayerExperience(PlayerEntity player) {
        return getTotalExpForLevel(player.experienceLevel);
    }

    public static int getExtraPlayerExperience(PlayerEntity player) {

        return Math.round(player.experience * player.xpBarCap());
    }

    public static void setPlayerExperience(PlayerEntity player, int exp) {
        player.experienceLevel = 0;
        player.experience = 0.0F;
        player.experienceTotal = 0;

        addExperienceToPlayer(player, exp);
    }

    public static void setPlayerLevel(PlayerEntity player, int level) {
        player.experienceLevel = level;
        player.experience = 0.0F;
    }

    public static void addExperienceToPlayer(PlayerEntity player, int exp) {

        int i = Integer.MAX_VALUE - player.experienceTotal;

        if (exp > i) {
            exp = i;
        }
        player.experience += (float) exp / (float) player.xpBarCap();
        for (player.experienceTotal += exp; player.experience >= 1.0F; player.experience /= (float) player.xpBarCap()) {
            player.experience = (player.experience - 1.0F) * (float) player.xpBarCap();
            addExperienceLevelToPlayer(player, 1);
        }
    }

    public static void addExperienceLevelToPlayer(PlayerEntity player, int levels) {
        player.experienceLevel += levels;

        if (player.experienceLevel < 0) {
            player.experienceLevel = 0;
            player.experience = 0.0F;
            player.experienceTotal = 0;
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
        CompoundNBT tag = stack.hasTag() ? stack.getTag() : new CompoundNBT();
        tag.putInt("Experience", storedExp);
        stack.setTag(tag);
        return storedExp;
    }

    public static int getExperience(ItemStack stack) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundNBT());
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
