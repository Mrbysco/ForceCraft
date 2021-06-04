package mrbysco.forcecraft.util;

import mrbysco.forcecraft.Reference;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;

public class AdvancementUtil {
	public static void unlockTierAdvancements(PlayerEntity player, int tier) {
		if(!player.world.isRemote) {
			ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
			if(tier >= 1) {
				unlockAdvancement(serverPlayer, "tier1/tier");
				unlockAdvancement(serverPlayer, "tier1/heat");
				unlockAdvancement(serverPlayer, "tier1/lumberjack");
				unlockAdvancement(serverPlayer, "tier1/speed");
			}
			if(tier >= 2) {
				unlockAdvancement(serverPlayer, "tier2/tier");
				unlockAdvancement(serverPlayer, "tier2/experience");
				unlockAdvancement(serverPlayer, "tier2/freezing");
				unlockAdvancement(serverPlayer, "tier2/grinding");
				unlockAdvancement(serverPlayer, "tier2/holding");
				unlockAdvancement(serverPlayer, "tier2/luck");
				unlockAdvancement(serverPlayer, "tier2/rainbow");
			}
			if(tier >= 3) {
				unlockAdvancement(serverPlayer, "tier3/tier");
				unlockAdvancement(serverPlayer, "tier3/bleeding");
				unlockAdvancement(serverPlayer, "tier3/camo");
				unlockAdvancement(serverPlayer, "tier3/silk");
			}
			if(tier >= 4) {
				unlockAdvancement(serverPlayer, "tier4/tier");
				unlockAdvancement(serverPlayer, "tier4/bane");
			}
			if(tier >= 5) {
				unlockAdvancement(serverPlayer, "tier5/tier");
				unlockAdvancement(serverPlayer, "tier5/healing");
				unlockAdvancement(serverPlayer, "tier5/wing");
			}
			if(tier >= 6) {
				unlockAdvancement(serverPlayer, "tier6/tier");
				unlockAdvancement(serverPlayer, "tier6/ender");
				unlockAdvancement(serverPlayer, "tier6/sturdy");
				unlockAdvancement(serverPlayer, "tier6/time");
			}
			if(tier >= 7) {
				unlockAdvancement(serverPlayer, "tier7/tier");
				unlockAdvancement(serverPlayer, "tier7/light");
				unlockAdvancement(serverPlayer, "tier7/treasure");
			}
		}
	}

	public static void unlockAdvancement(ServerPlayerEntity player, String name) {
		Advancement advancementIn = player.getServer().getAdvancementManager().getAdvancement(new ResourceLocation(Reference.MOD_ID + ":" + name));
		if(advancementIn != null) {
			AdvancementProgress advancementprogress = player.getAdvancements().getProgress(advancementIn);
			if (!advancementprogress.isDone()) {
				for(String s : advancementprogress.getRemaningCriteria()) {
					player.getAdvancements().grantCriterion(advancementIn, s);
				}
			}
		}
	}
}
