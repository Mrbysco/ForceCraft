package mrbysco.forcecraft.potion.effects;

import mrbysco.forcecraft.registry.ForceEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;

public class EffectMagnet extends EffectInstance {

    public EffectMagnet(int duration) {
        super(ForceEffects.MAGNET.get(), duration, 1, true, false);
    }

    @Override
    public void performEffect(LivingEntity entity) {
        //Inspired by Botania Code

        double x = entity.getPosX();
        double y = entity.getPosY() + 0.75;;
        double z = entity.getPosZ();
        double range = 10.0d;

        EffectInstance activePotionEffect = entity.getActivePotionEffect(ForceEffects.MAGNET.get());
        if(activePotionEffect != null) {
            range += activePotionEffect.getAmplifier() * 0.3f;
        }

        List<ItemEntity> items = entity.getEntityWorld().getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range));
        for(ItemEntity item : items) {
            if(item.getItem().isEmpty() || !item.isAlive()) {
                continue;
            }

            // constant force!
            float strength = 0.07F;

            Vector3d entityVector = new Vector3d(item.getPosX(), item.getPosY() - item.getYOffset() + item.getHeight() / 2, item.getPosZ());
            Vector3d finalVector = new Vector3d(x, y, z).subtract(entityVector);

            if (Math.sqrt(finalVector.x * finalVector.x + finalVector.y * finalVector.y + finalVector.z * finalVector.z) > 1) {
                finalVector = finalVector.normalize();
            }

            item.setMotion(finalVector.mul(strength, strength, strength));
        }
    }

}
