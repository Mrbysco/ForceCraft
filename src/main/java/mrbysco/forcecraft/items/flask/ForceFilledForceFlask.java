package mrbysco.forcecraft.items.flask;

import mrbysco.forcecraft.items.BaseItem;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ForceFilledForceFlask extends BaseItem {

    public ForceFilledForceFlask(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) entityLiving.addPotionEffect(new EffectInstance(Effects.SPEED, 600, 2));

        if (entityLiving instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)entityLiving;
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayerentity, stack);
            serverplayerentity.addStat(Stats.ITEM_USED.get(this));
        }

        ItemStack flaskStack = ForceRegistry.FORCE_FLASK.get().getDefaultInstance();
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerIn = (PlayerEntity)entityLiving;
            if(!playerIn.abilities.isCreativeMode) {
                stack.shrink(1);
            }

            if(!playerIn.inventory.addItemStackToInventory(flaskStack)) {
                playerIn.entityDropItem(flaskStack, 0F);
            }
        }

        return stack.isEmpty() ? flaskStack : stack;
    }

    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        return DrinkHelper.startDrinking(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.force_filled_force_flask.tooltip").mergeStyle(TextFormatting.GRAY));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @javax.annotation.Nullable net.minecraft.nbt.CompoundNBT nbt) {
        return new FlaskFluidHandler(stack);
    }
}
