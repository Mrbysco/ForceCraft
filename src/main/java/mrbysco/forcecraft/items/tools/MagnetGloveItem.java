package mrbysco.forcecraft.items.tools;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.capablilities.magnet.IMagnet;
import mrbysco.forcecraft.capablilities.magnet.MagnetProvider;
import mrbysco.forcecraft.items.BaseItem;
import mrbysco.forcecraft.potion.effects.EffectMagnet;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nullable;
import java.util.List;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_MAGNET;

public class MagnetGloveItem extends BaseItem {

    public MagnetGloveItem(Item.Properties properties) {
        super(properties.maxStackSize(1));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if(CAPABILITY_MAGNET == null) {
            return null;
        }
        return new MagnetProvider();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(playerIn.isSneaking()) {
            ItemStack stack = playerIn.getHeldItem(handIn);
            stack.getCapability(CAPABILITY_MAGNET).ifPresent((cap) -> {
                if (this.getDamage(stack) == 1) {
                    cap.deactivate();
                    this.setDamage(stack, 0);
                } else {
                    cap.activate();
                    this.setDamage(stack, 1);
                }
                worldIn.playSound((PlayerEntity)null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            });
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(entityIn instanceof PlayerEntity && !(entityIn instanceof FakePlayer)) {
            if(itemSlot >= 0 && itemSlot <= PlayerInventory.getHotbarSize()) {
                IMagnet magnetCap = stack.getCapability(CAPABILITY_MAGNET).orElse(null);
                if (magnetCap != null && magnetCap.isActivated()) {
                    EffectInstance magnet = new EffectMagnet(20);
                    ((PlayerEntity)entityIn).addPotionEffect(magnet);
                }
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(this.getDamage(stack) == 1) {
            tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".magnet_glove.active").mergeStyle(TextFormatting.GREEN));
        } else {
            tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".magnet_glove.deactivated").mergeStyle(TextFormatting.RED));
        }
        tooltip.add(new StringTextComponent(" "));
        tooltip.add(new TranslationTextComponent("forcecraft.magnet_glove.change").mergeStyle(TextFormatting.BOLD));

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
