package mrbysco.forcecraft.items.tools;

import mrbysco.forcecraft.entities.projectile.ForceArrowEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ForceArrowItem extends ArrowItem {
	public ForceArrowItem(Properties builder) {
		super(builder);
	}

	@Override
	public AbstractArrowEntity createArrow(World worldIn, ItemStack stack, LivingEntity shooter) {
		ForceArrowEntity forceArrow = new ForceArrowEntity(worldIn, shooter);
		return forceArrow;
	}
}
