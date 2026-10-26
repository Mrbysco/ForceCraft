package com.mrbysco.forcecraft.util;

import com.mrbysco.forcecraft.ForceCraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Code for users that asked for their Infuser to be able to explode with a 5% chance
 */
public class SpecialUsers {
	private static final String EXPLODY_URL = "https://raw.githubusercontent.com/Mrbysco/ForceCraft/refs/heads/26.1/explody.md";
	private static final Set<UUID> EXPLODY_USERS = ConcurrentHashMap.newKeySet();

	public static final float EXPLODE_CHANCE = 0.05F;

	public static void loadExplodyUsers() {
		new Thread(() -> {
			ForceCraft.LOGGER.info("Loading ForceCraft explody users...");
			try {
				URLConnection connection = URI.create(EXPLODY_URL).toURL().openConnection();
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
					String line;
					while ((line = reader.readLine()) != null) {
						line = line.replace("`", "").trim();
						if (line.isEmpty()) continue;
						String[] split = line.split("\\s+", 2);
						try {
							EXPLODY_USERS.add(UUID.fromString(split[0]));
						} catch (IllegalArgumentException e) {
							ForceCraft.LOGGER.error("Invalid entry {} will be ignored.", line);
						}
					}
				}
			} catch (IOException ex) {
				ForceCraft.LOGGER.error("Exception loading explody users!");
				ForceCraft.LOGGER.trace("Trace", ex);
			}
			ForceCraft.LOGGER.info("Loaded {} explody users.", EXPLODY_USERS.size());
		}, "ForceCraft Explody Data Loader").start();
	}

	public static boolean isExplodyUser(UUID uuid) {
		return EXPLODY_USERS.contains(uuid);
	}
}
