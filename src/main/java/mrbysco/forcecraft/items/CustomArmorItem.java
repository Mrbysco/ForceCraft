package mrbysco.forcecraft.items;

import mrbysco.forcecraft.ForceCraft;
import mrbysco.forcecraft.capablilities.toolmodifier.IToolModifier;
import mrbysco.forcecraft.capablilities.toolmodifier.ToolModProvider;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class CustomArmorItem extends ArmorItem {

    public CustomArmorItem(IArmorMaterial materialIn, EquipmentSlotType slot, Item.Properties builderIn) {
        super(materialIn, slot, builderIn);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if(CAPABILITY_TOOLMOD == null) {
            return null;
        }
        return new ToolModProvider();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> lores, ITooltipFlag flagIn) {
        attachInformation(stack, lores);
        super.addInformation(stack, worldIn, lores, flagIn);
    }

    static void attachInformation(ItemStack stack, List<ITextComponent> toolTip) {
    	IToolModifier stuff = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
	
ForceCraft.LOGGER.info("WHY IS THIS NULL {}",stuff);
	
        stack.getCapability(CAPABILITY_TOOLMOD).ifPresent((cap) -> {
        	// TODO: language file
            if(cap.getSpeedLevel() > 0) {
                toolTip.add(new StringTextComponent("force punch " + cap.getSpeedLevel()));
            }
        });
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }
}
