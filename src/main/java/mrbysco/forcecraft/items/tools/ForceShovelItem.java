package mrbysco.forcecraft.items.tools;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.capablilities.toolmodifier.ToolModProvider;
import mrbysco.forcecraft.registry.material.ModToolMaterial;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static mrbysco.forcecraft.Reference.MODIFIERS.MOD_CHARGE;
import static mrbysco.forcecraft.Reference.MODIFIERS.MOD_CHARGEII;
import static mrbysco.forcecraft.Reference.MODIFIERS.MOD_GRINDING;
import static mrbysco.forcecraft.Reference.MODIFIERS.MOD_HEAT;
import static mrbysco.forcecraft.Reference.MODIFIERS.MOD_LUCK;
import static mrbysco.forcecraft.Reference.MODIFIERS.MOD_REPAIR;
import static mrbysco.forcecraft.Reference.MODIFIERS.MOD_SPEED;
import static mrbysco.forcecraft.Reference.MODIFIERS.MOD_STURDY;
import static mrbysco.forcecraft.Reference.MODIFIERS.MOD_TOUCH;
import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class ForceShovelItem extends ShovelItem {

    public List<Reference.MODIFIERS> applicableModifers = new ArrayList<>();

    public ForceShovelItem(Item.Properties properties) {
        super(ModToolMaterial.FORCE, -2.5F, -3.0F, properties);
        setApplicableModifers();
    }

    public void setApplicableModifers() {
        applicableModifers.add(MOD_CHARGE);
        applicableModifers.add(MOD_CHARGEII);
        applicableModifers.add(MOD_HEAT);
        applicableModifers.add(MOD_LUCK);
        applicableModifers.add(MOD_GRINDING);
        applicableModifers.add(MOD_TOUCH);
        applicableModifers.add(MOD_STURDY);
        applicableModifers.add(MOD_REPAIR);
        applicableModifers.add(MOD_SPEED);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if(CAPABILITY_TOOLMOD == null) {
            return null;
        }
        return new ToolModProvider();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> lores, ITooltipFlag flagIn) {
        ForceAxeItem.attachInformation(stack, lores);
        super.addInformation(stack, worldIn, lores, flagIn);
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

}
