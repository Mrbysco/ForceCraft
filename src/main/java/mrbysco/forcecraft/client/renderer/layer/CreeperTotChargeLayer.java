package mrbysco.forcecraft.client.renderer.layer;

import mrbysco.forcecraft.client.model.CreeperTotModel;
import mrbysco.forcecraft.entities.CreeperTotEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EnergyLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

public class CreeperTotChargeLayer extends EnergyLayer<CreeperTotEntity, CreeperTotModel<CreeperTotEntity>> {
	private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
	private final CreeperTotModel<CreeperTotEntity> creeperModel = new CreeperTotModel<>(2.0F);

	public CreeperTotChargeLayer(IEntityRenderer<CreeperTotEntity, CreeperTotModel<CreeperTotEntity>> p_i50947_1_) {
		super(p_i50947_1_);
	}

	protected float func_225634_a_(float p_225634_1_) {
		return p_225634_1_ * 0.01F;
	}

	protected ResourceLocation func_225633_a_() {
		return LIGHTNING_TEXTURE;
	}

	protected EntityModel<CreeperTotEntity> func_225635_b_() {
		return this.creeperModel;
	}
}