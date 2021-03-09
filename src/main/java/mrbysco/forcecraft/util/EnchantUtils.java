package mrbysco.forcecraft.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

import java.util.Map;

public final class EnchantUtils {

    public static boolean removeEnchant(ItemStack stack, Enchantment enchantment) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        enchantments.remove(enchantment);
        EnchantmentHelper.setEnchantments(enchantments, stack);
        return false;
    }

    public static void incrementLevel(ItemStack stack, Enchantment enchantment) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        if(enchantments.containsKey(enchantment)) {
            int oldLevel = enchantments.get(enchantment);
            enchantments.remove(enchantment);
            enchantments.put(enchantment, oldLevel + 1);
            EnchantmentHelper.setEnchantments(enchantments, stack);
        }
    }
}
