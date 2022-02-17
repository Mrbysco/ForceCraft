package com.mrbysco.forcecraft.items.flask;

import com.mrbysco.forcecraft.items.BaseItem;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ForceFilledForceFlask extends BaseItem {

    public ForceFilledForceFlask(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        if (!level.isClientSide) entityLiving.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 2, false, false));

        if (entityLiving instanceof ServerPlayer serverplayerentity) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayerentity, stack);
            serverplayerentity.awardStat(Stats.ITEM_USED.get(this));
        }

        ItemStack flaskStack = ForceRegistry.FORCE_FLASK.get().getDefaultInstance();
        if (entityLiving instanceof Player playerIn) {
            if(!playerIn.getAbilities().instabuild) {
                stack.shrink(1);
            }

            if(!playerIn.getInventory().add(flaskStack)) {
                playerIn.spawnAtLocation(flaskStack, 0F);
            }
        }

        return stack.isEmpty() ? flaskStack : stack;
    }

    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
        return ItemUtils.startUsingInstantly(level, playerIn, handIn);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("item.force_filled_force_flask.tooltip").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, level, tooltip, flagIn);
    }

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @javax.annotation.Nullable net.minecraft.nbt.CompoundTag nbt) {
        return new FlaskFluidHandler(stack);
    }
}
