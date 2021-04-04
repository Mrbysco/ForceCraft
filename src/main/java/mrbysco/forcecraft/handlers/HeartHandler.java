package mrbysco.forcecraft.handlers;

import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HeartHandler {

	private static final int HEAL_AMT = 2; // 2 health == 1 heart
	static final double CHANCE = 0.05; // 5%

	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		World world = event.getEntity().world;
		if (world.isRemote || event.getSource() == null || world.rand.nextDouble() >= CHANCE) {
			return;
		}
		if (event.getSource().getTrueSource() instanceof PlayerEntity && !(event.getSource().getTrueSource() instanceof FakePlayer)) {
			// killed by a real player
			EntityClassification eclass = event.getEntityLiving().getType().getClassification();
			if (eclass == EntityClassification.MONSTER) {
				// and its a monster, not sheep or squid or something
				BlockPos pos = event.getEntity().getPosition();
				world.addEntity(new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, new ItemStack(ForceRegistry.RECOVERY_HEART.get())));
			}
		}
	}

	@SubscribeEvent
	public void onPickup(EntityItemPickupEvent event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			ItemEntity itemEntity = event.getItem();
			ItemStack resultStack = itemEntity.getItem();
			if (!resultStack.isEmpty() && resultStack.getItem() == ForceRegistry.RECOVERY_HEART.get()) {

				// item entities can hold multiple

				while (!resultStack.isEmpty() && player.shouldHeal()) {
					player.heal(HEAL_AMT);
					resultStack.shrink(1);
					itemEntity.setItem(resultStack);
				}

				// remove the whole thing and cancel item pickup
				//even if nothing was healed.
				itemEntity.remove();
				event.setCanceled(true);
			}
		}
	}
}
