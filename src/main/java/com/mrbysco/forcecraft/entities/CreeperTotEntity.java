package com.mrbysco.forcecraft.entities;

import com.mrbysco.forcecraft.registry.ForceEntities;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class CreeperTotEntity extends Creeper {

	public CreeperTotEntity(EntityType<? extends Creeper> type, Level level) {
		super(type, level);
	}

	public static AttributeSupplier.Builder generateAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.25D)
				.add(Attributes.MAX_HEALTH, 4.0D);
	}

	@Override
	public EntityType<? extends Creeper> getType() {
		return ForceEntities.CREEPER_TOT.get();
	}

	@Override
	public void explodeCreeper() {
		this.level().broadcastEntityEvent(this, (byte) 17);

		if (!this.level().isClientSide()) {
			this.dead = true;
			this.playSound(SoundEvents.GENERIC_EXPLODE.value(), 4.0F, (1.0F + (this.level().getRandom().nextFloat() - this.level().getRandom().nextFloat()) * 0.2F) * 0.7F);

			if (((ServerLevel)this.level()).getGameRules().get(GameRules.MOB_DROPS) && this.getRandom().nextInt(4) == 0) {
				spawnAtLocation(((ServerLevel)this.level()), new ItemStack(ForceRegistry.PILE_OF_GUNPOWDER.get(), this.getRandom().nextInt(2) + 1));
			}

			this.discard();
		}
	}

	public void summonFireworkParticles(List<FireworkExplosion> fireworksTag, double yOffset) {
		if (fireworksTag != null) {
			Vec3 vector3d = this.getDeltaMovement();
			this.level().createFireworks(this.getX(), this.getY() + yOffset, this.getZ(), vector3d.x, vector3d.y, vector3d.z, fireworksTag);
		}
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == 17 && this.level().isClientSide()) {
			for (int i = 0; i < 4; i++) {
				summonFireworkParticles(getFireworkTag(), 0.5D);
			}

			summonFireworkParticles(getCreeperFireworkTag(), 2.5D);
		}
		super.handleEntityEvent(id);
	}

	public List<FireworkExplosion> getFireworkTag() {
		List<FireworkExplosion> explosions = new ArrayList<>();

		int[] colors = new int[16];
		for (int i = 0; i < 16; i++) {
			colors[i] = DyeColor.byId(i).getFireworkColor();
		}
		FireworkExplosion explosion = new FireworkExplosion(
				FireworkExplosion.Shape.SMALL_BALL,
				IntList.of(colors),
				IntList.of(colors),
				false,
				true
		);
		explosions.add(explosion);

		return explosions;
	}

	public List<FireworkExplosion> getCreeperFireworkTag() {
		List<FireworkExplosion> explosions = new ArrayList<>();

		FireworkExplosion explosion = new FireworkExplosion(
				FireworkExplosion.Shape.CREEPER,
				IntList.of(DyeColor.LIME.getFireworkColor()),
				IntList.of(DyeColor.LIME.getFireworkColor()),
				false,
				true
		);
		explosions.add(explosion);

		return explosions;
	}
}
