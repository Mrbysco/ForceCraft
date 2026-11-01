package com.mrbysco.forcecraft.mixin.client;

import com.mrbysco.forcecraft.client.ForceRenderStateHolder;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemStackRenderState.class)
public abstract class ItemStackRenderStateMixin implements ForceRenderStateHolder {
	@Unique
	private boolean forcecraft$silhouette = false;

	@Override
	@Unique
	public void forcecraft$setSilhouette(boolean silhouette) {
		this.forcecraft$silhouette = silhouette;
	}

	@Override
	@Unique
	public boolean forcecraft$isSilhouette() {
		return this.forcecraft$silhouette;
	}

	@Inject(method = "clear", at = @At("HEAD"))
	private void forcecraft$clear(CallbackInfo ci) {
		this.forcecraft$silhouette = false;
	}
}
