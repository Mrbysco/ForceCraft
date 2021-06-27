package mrbysco.forcecraft.entities.projectile;

import mrbysco.forcecraft.ForceCraft;
import mrbysco.forcecraft.registry.ForceEntities;
import mrbysco.forcecraft.registry.ForceRegistry;
import mrbysco.forcecraft.util.ForceUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.CreeperSwellGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potions;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_BANE;

public class ForceArrowEntity extends ArrowEntity {
	private static final DataParameter<Boolean> ENDER = EntityDataManager.createKey(ForceArrowEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> BANE = EntityDataManager.createKey(ForceArrowEntity.class, DataSerializers.BOOLEAN);
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
		this.dataManager.register(BANE, false);
		this.dataManager.register(LUCK, 0);
	}

	public boolean isBane() {
		return this.dataManager.get(BANE);
	}

	public void setBane() {
		this.dataManager.set(BANE, true);
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
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);

		if(compound.getBoolean("Bane")) {
			setBane();
		}
		if(compound.getBoolean("Ender")) {
			setEnder();
		}
		this.setLuck(compound.getInt("Luck"));
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);

		if(isBane()) {
			compound.putBoolean("Bane", true);
		}
		if(isEnder()) {
			compound.putBoolean("Ender", true);
		}
		compound.putInt("Luck", getLuck());
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
			ForceUtils.teleportRandomly(living);
		}

		if(isBane()) {
			if(living instanceof CreeperEntity){
				CreeperEntity creeper = ((CreeperEntity) living);
				creeper.getCapability(CAPABILITY_BANE).ifPresent((entityCap) -> {
					if(entityCap.canExplode()){
						creeper.setCreeperState(-1);
						creeper.getDataManager().set(CreeperEntity.IGNITED, false);
						entityCap.setExplodeAbility(false);
						creeper.goalSelector.goals.removeIf(goal -> goal.getGoal() instanceof CreeperSwellGoal);
						ForceCraft.LOGGER.info("Added Bane to " + living.getName());
					}
				});
			}
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
