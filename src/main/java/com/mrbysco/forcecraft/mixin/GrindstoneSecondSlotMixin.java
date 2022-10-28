package com.mrbysco.forcecraft.mixin;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = {"net.minecraft.inventory.container.GrindstoneContainer$3"})
public class GrindstoneSecondSlotMixin {
	@Inject(at = @At("HEAD"), method = "mayPlace(Lnet/minecraft/item/ItemStack;)Z", cancellable = true)
	private void forcecraftIsItemValid(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.getItem().getRegistryName().getNamespace().equals(Reference.MOD_ID)) {
			cir.setReturnValue(false);
		}
	}
}
