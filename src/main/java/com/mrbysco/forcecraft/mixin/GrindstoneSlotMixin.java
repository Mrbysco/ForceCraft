package com.mrbysco.forcecraft.mixin;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(targets = {"net/minecraft/world/inventory/GrindstoneMenu$2"})
public class GrindstoneSlotMixin {

	@Inject(at = @At("HEAD"), method = "mayPlace(Lnet/minecraft/world/item/ItemStack;)Z", locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private void forcecraftMayPlace(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (ForgeRegistries.ITEMS.getKey(stack.getItem()).getNamespace().equals(Reference.MOD_ID))
			cir.setReturnValue(false);
	}
}