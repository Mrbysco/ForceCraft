package mrbysco.forcecraft.entities.projectile;

import mrbysco.forcecraft.registry.ForceEntities;
import mrbysco.forcecraft.registry.ForceRegistry;
import mrbysco.forcecraft.util.DartUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potions;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class ForceArrowEntity extends ArrowEntity {
	private static final DataParameter<Boolean> ENDER = EntityDataManager.createKey(ForceArrowEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> LUCK = EntityDataManager.createKey(ForceArrowEntity.class, DataSerializers.VARINT);

	public ForceArrowEntity(EntityType<? extends ArrowEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public ForceArrowEntity(World worldIn, LivingEntity shooter) {
		super(worldIn, shooter);
		this.setShooter(shooter);
	}

	protected void registerData() {
		super.registerData();
		this.dataManager.register(ENDER, false);
	}

	public boolean isEnder() {
		return this.dataManager.get(ENDER);
	}

	public void setEnder() {
		this.dataManager.set(ENDER, true);
	}

	public int getLuck() {
		return this.dataManager.get(LUCK);
	}

	public void setLuck(int luck) {
		this.dataManager.set(LUCK, luck);
	}

	@Override
	public void setPotionEffect(ItemStack stack) {
		if (stack.getItem() == Items.ARROW) {
			this.potion = Potions.EMPTY;
			this.customPotionEffects.clear();
			this.dataManager.set(COLOR, -1);
		}
	}

	@Override
	protected void arrowHit(LivingEntity living) {
		super.arrowHit(living);

		if(isEnder()) {
			DartUtils.teleportRandomly(living);
		}
	}

	@Override
	protected ItemStack getArrowStack() {
		return new ItemStack(ForceRegistry.FORCE_ARROW.get());
	}

	@Override
	public EntityType<?> getType() {
		return ForceEntities.FORCE_ARROW.get();
	}

	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
