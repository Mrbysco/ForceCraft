package com.mrbysco.forcecraft.entities;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.registry.ForceEntities;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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
		this.level.broadcastEntityEvent(this, (byte) 17);

		if (!this.level.isClientSide) {
			this.dead = true;
			this.playSound(SoundEvents.GENERIC_EXPLODE, 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F);

			if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT) && this.getRandom().nextInt(4) == 0) {
				spawnAtLocation(new ItemStack(ForceRegistry.PILE_OF_GUNPOWDER.get(), this.getRandom().nextInt(2) + 1));
			}

			this.discard();
		}
	}

	public void summonFireworkParticles(CompoundTag fireworksTag, double yOffset) {
		if (fireworksTag != null) {
			ForceCraft.LOGGER.info(fireworksTag.toString());
			Vec3 vector3d = this.getDeltaMovement();
			this.level.createFireworks(this.getX(), this.getY() + yOffset, this.getZ(), vector3d.x, vector3d.y, vector3d.z, fireworksTag);
		}
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == 17 && this.level.isClientSide) {
			for (int i = 0; i < 4; i++) {
				summonFireworkParticles(getFireworkTag(), 0.5D);
			}

			summonFireworkParticles(getCreeperFireworkTag(), 2.5D);
		}
		super.handleEntityEvent(id);
	}

	public CompoundTag getFireworkTag() {
		CompoundTag nbt = new CompoundTag();
		nbt.putBoolean("Flicker", true);

		int[] colors = new int[16];
		for (int i = 0; i < 16; i++) {
			colors[i] = DyeColor.byId(i).getFireworkColor();
		}
		nbt.putIntArray("Colors", colors);
		nbt.putByte("Type", (byte) 0);

		ListTag explosions = new ListTag();
		explosions.add(nbt);

		CompoundTag fireworkTag = new CompoundTag();
		fireworkTag.put("Explosions", explosions);

		return fireworkTag;
	}

	public CompoundTag getCreeperFireworkTag() {
		CompoundTag nbt = new CompoundTag();
		nbt.putBoolean("Flicker", true);

		int[] colors = new int[1];
		colors[0] = DyeColor.LIME.getFireworkColor();
		nbt.putIntArray("Colors", colors);
		nbt.putByte("Type", (byte) 3);

		ListTag explosions = new ListTag();
		explosions.add(nbt);

		CompoundTag fireworkTag = new CompoundTag();
		fireworkTag.put("Explosions", explosions);

		return fireworkTag;
	}
}
