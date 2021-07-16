package mrbysco.forcecraft.mixin;

import mrbysco.forcecraft.Reference;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = {"net.minecraft.inventory.container.GrindstoneContainer$2"})
public class GrindstoneSlotMixin {
	@Inject(at = @At("HEAD"), method = "Lnet/minecraft/inventory/container/GrindstoneContainer$2;isItemValid(Lnet/minecraft/item/ItemStack;)Z", cancellable = true)
	private void forcecraftIsItemValid(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if(stack.getItem().getRegistryName().getNamespace().equals(Reference.MOD_ID)) {
			cir.setReturnValue(false);
		}
	}
}
