package com.mrbysco.forcecraft.mixin;

import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.util.ForceUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

	@Inject(method = "processDurabilityChange(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;)I",
			at = @At("RETURN"),
			cancellable = true
	)
	private void forcecraft$applyForce(int amount, ServerLevel level, @Nullable LivingEntity player,
	                                   CallbackInfoReturnable<Integer> cir) {
		ItemStack self = (ItemStack) (Object) this;
		if (self.has(ForceComponents.FORCE_STORED)) {
			cir.setReturnValue(ForceUtil.damageForceItem(self, cir.getReturnValueI()));
		}
	}
}
