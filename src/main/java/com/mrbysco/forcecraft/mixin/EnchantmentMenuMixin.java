package com.mrbysco.forcecraft.mixin;

import com.mrbysco.forcecraft.util.ForceUtil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin extends AbstractContainerMenu {
	protected EnchantmentMenuMixin(@Nullable MenuType<?> menuType, int containerId) {
		super(menuType, containerId);
	}


	@Inject(method = "getEnchantmentList(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/world/item/ItemStack;II)Ljava/util/List;",
			at = @At("HEAD"),
			cancellable = true
	)
	private void forcecraft$getEnchantmentList(RegistryAccess access, ItemStack itemStack, int slot,
	                                           int enchantmentCost,
	                                           CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
		if (ForceUtil.isForceItem(itemStack)) {
			cir.setReturnValue(List.of());
		}
	}
}
