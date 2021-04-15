package mrbysco.forcecraft.entities;

import mrbysco.forcecraft.registry.ForceEntities;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class CreeperTotEntity extends CreeperEntity {

	public CreeperTotEntity(EntityType<? extends CreeperEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public static AttributeModifierMap.MutableAttribute generateAttributes() {
		return MonsterEntity.func_234295_eP_()
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D)
				.createMutableAttribute(Attributes.MAX_HEALTH, 4.0D);
	}

	@Override
	public EntityType<? extends CreeperEntity> getType() {
		return ForceEntities.CREEPER_TOT.get();
	}

	@Override
	public void explode() {
		if(this.world.isRemote) {
			for(int i = 0; i < 4; i++) {
				summonFireworkParticles(getFirework(), 0.5D);
			}

			summonFireworkParticles(getCreeperFirework(), 2.5D);
		}

		if (!this.world.isRemote) {
			this.dead = true;
			this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);

			if(this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT) && this.getRNG().nextInt(4) == 0) {
				entityDropItem(new ItemStack(ForceRegistry.PILE_OF_GUNPOWDER.get(), this.getRNG().nextInt(2) + 1));
			}

			this.remove();
		}
	}

	public void summonFireworkParticles(ItemStack fireworkRocket, double yOffset) {
		CompoundNBT compoundnbt = fireworkRocket.isEmpty() ? null : fireworkRocket.getChildTag("Fireworks");
		Vector3d vector3d = this.getMotion();
		this.world.makeFireworks(this.getPosX(), this.getPosY() + yOffset, this.getPosZ(), vector3d.x, vector3d.y, vector3d.z, compoundnbt);
	}

	public ItemStack getFirework() {
		ItemStack firework = new ItemStack(Items.FIREWORK_ROCKET);
		firework.setTag(new CompoundNBT());
		CompoundNBT nbt = new CompoundNBT();
		nbt.putBoolean("Flicker", true);

		int[] colors = new int[16];
		for (int i = 0; i < 16; i++) {
			colors[i] = DyeColor.byId(i).getColorValue();
		}
		nbt.putIntArray("Colors", colors);
		nbt.putByte("Type", (byte) 0);

		ListNBT explosions = new ListNBT();
		explosions.add(nbt);

		CompoundNBT fireworkTag = new CompoundNBT();
		fireworkTag.put("Explosions", explosions);
		firework.getOrCreateTag().put("Fireworks", fireworkTag);

		return firework;
	}

	public ItemStack getCreeperFirework() {
		ItemStack firework = new ItemStack(Items.FIREWORK_ROCKET);
		firework.setTag(new CompoundNBT());
		CompoundNBT nbt = new CompoundNBT();
		nbt.putBoolean("Flicker", true);

		int[] colors = new int[1];
		colors[0] = DyeColor.LIME.getColorValue();
		nbt.putIntArray("Colors", colors);
		nbt.putByte("Type", (byte) 3);

		ListNBT explosions = new ListNBT();
		explosions.add(nbt);

		CompoundNBT fireworkTag = new CompoundNBT();
		fireworkTag.put("Explosions", explosions);
		firework.getOrCreateTag().put("Fireworks", fireworkTag);

		return firework;
	}
}
