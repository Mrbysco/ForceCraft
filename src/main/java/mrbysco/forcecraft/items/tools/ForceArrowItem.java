package mrbysco.forcecraft.items.tools;

import mrbysco.forcecraft.entities.projectile.ForceArrowEntity;
import mrbysco.forcecraft.registry.ForceEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class ForceArrowItem extends ArrowItem {
	public ForceArrowItem(Properties builder) {
		super(builder);
	}

	@Override
	public AbstractArrowEntity createArrow(World worldIn, ItemStack stack, LivingEntity shooter) {
		ForceArrowEntity forceArrow = new ForceArrowEntity(worldIn, shooter);
		if(shooter instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) shooter;
			ItemStack heldItem = player.getActiveItemStack();
			if(heldItem.getItem() instanceof ForceBowItem) {
				heldItem.getCapability(CAPABILITY_TOOLMOD).ifPresent(cap -> {
					if(cap.hasFreezing()) {
						forceArrow.addEffect(new EffectInstance(Effects.SLOWNESS, 60, 2, false, false));
					}
					if(cap.hasEnder()) {
						forceArrow.setEnder();
					}
					if(cap.hasBane()) {
						forceArrow.setBane();
					}
					if(cap.hasBleed()) {
						EffectInstance bleedingEffect = new EffectInstance(ForceEffects.BLEEDING.get(), 20 * cap.getBleedLevel(), 0, false, false);
						forceArrow.addEffect(bleedingEffect);
					}
					if(cap.hasLuck()) {
						int luckValue = cap.getLuckLevel();
						forceArrow.setLuck(luckValue);
					}
					if(cap.getSpeedLevel() > 0) {
						forceArrow.setSpeedy();
					}
				});
			}
		}
		forceArrow.setPotionEffect(stack);
		return forceArrow;
	}
}
