package com.mrbysco.forcecraft.client.renderer.cold;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.model.ColdPigModel;
import com.mrbysco.forcecraft.client.state.ColdPigRenderState;
import com.mrbysco.forcecraft.entity.cold.ColdPig;
import net.minecraft.client.model.animal.pig.PigModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;

public class ColdPigRenderer extends MobRenderer<ColdPig, ColdPigRenderState, ColdPigModel> {
	private static final Identifier PIG_TEXTURES = Reference.modLoc("textures/entity/cold_pig.png");

	public ColdPigRenderer(EntityRendererProvider.Context context) {
		super(context, new ColdPigModel(context.bakeLayer(ModelLayers.PIG)), 0.7F);
		this.addLayer(
				new SimpleEquipmentLayer<>(
						this,
						context.getEquipmentRenderer(),
						EquipmentClientInfo.LayerType.PIG_SADDLE,
						state -> state.saddle,
						new PigModel(context.bakeLayer(ModelLayers.PIG_SADDLE)),
						null
				)
		);
	}

	@Override
	public Identifier getTextureLocation(ColdPigRenderState state) {
		return PIG_TEXTURES;
	}

	@Override
	public ColdPigRenderState createRenderState() {
		return new ColdPigRenderState();
	}

	@Override
	public void extractRenderState(ColdPig entity, ColdPigRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.saddle = entity.getItemBySlot(EquipmentSlot.SADDLE).copy();
		state.headEatAngleScale = entity.getHeadEatAngleScale(entity.getXRot(partialTicks), partialTicks);
		state.headEatPositionScale = entity.getHeadEatPositionScale(partialTicks);
	}
}
