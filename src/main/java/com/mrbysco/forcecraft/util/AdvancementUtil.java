package com.mrbysco.forcecraft.util;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class AdvancementUtil {
	public static void unlockTierAdvancements(Player player, int tier) {
		if (!player.level().isClientSide) {
			ServerPlayer serverPlayer = (ServerPlayer) player;
			if (tier >= 1) {
				unlockAdvancement(serverPlayer, "tier1/tier");
				unlockAdvancement(serverPlayer, "tier1/heat");
				unlockAdvancement(serverPlayer, "tier1/lumberjack");
				unlockAdvancement(serverPlayer, "tier1/speed");
			}
			if (tier >= 2) {
				unlockAdvancement(serverPlayer, "tier2/tier");
				unlockAdvancement(serverPlayer, "tier2/experience");
				unlockAdvancement(serverPlayer, "tier2/freezing");
				unlockAdvancement(serverPlayer, "tier2/grinding");
				unlockAdvancement(serverPlayer, "tier2/holding");
				unlockAdvancement(serverPlayer, "tier2/luck");
				unlockAdvancement(serverPlayer, "tier2/rainbow");
			}
			if (tier >= 3) {
				unlockAdvancement(serverPlayer, "tier3/tier");
				unlockAdvancement(serverPlayer, "tier3/bleeding");
				unlockAdvancement(serverPlayer, "tier3/camo");
				unlockAdvancement(serverPlayer, "tier3/silk");
			}
			if (tier >= 4) {
				unlockAdvancement(serverPlayer, "tier4/tier");
				unlockAdvancement(serverPlayer, "tier4/bane");
			}
			if (tier >= 5) {
				unlockAdvancement(serverPlayer, "tier5/tier");
				unlockAdvancement(serverPlayer, "tier5/healing");
				unlockAdvancement(serverPlayer, "tier5/wing");
			}
			if (tier >= 6) {
				unlockAdvancement(serverPlayer, "tier6/tier");
				unlockAdvancement(serverPlayer, "tier6/ender");
				unlockAdvancement(serverPlayer, "tier6/sturdy");
				unlockAdvancement(serverPlayer, "tier6/time");
			}
			if (tier >= 7) {
				unlockAdvancement(serverPlayer, "tier7/tier");
				unlockAdvancement(serverPlayer, "tier7/light");
				unlockAdvancement(serverPlayer, "tier7/treasure");
			}
		}
	}

	public static void unlockAdvancement(ServerPlayer serverPlayer, String name) {
		AdvancementHolder holder = serverPlayer.getServer().getAdvancements().get(new ResourceLocation(Reference.MOD_ID + ":" + name));
		if (holder != null) {
			AdvancementProgress advancementprogress = serverPlayer.getAdvancements().getOrStartProgress(holder);
			if (!advancementprogress.isDone()) {
				for (String s : advancementprogress.getRemainingCriteria()) {
					serverPlayer.getAdvancements().award(holder, s);
				}
			}
		}
	}
}
