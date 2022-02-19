package com.mrbysco.forcecraft.mixin;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(targets = { "net.minecraft.world.inventory.GrindstoneMenu$3" })
public class GrindstoneSecondSlotMixin {

	@Inject(at = @At("HEAD"), method = "mayPlace(Lnet/minecraft/world/item/ItemStack;)Z", locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
	private void forcecraftMayPlace(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if(stack.getItem().getRegistryName().getNamespace().equals(Reference.MOD_ID))
			cir.setReturnValue(false);
	}
}