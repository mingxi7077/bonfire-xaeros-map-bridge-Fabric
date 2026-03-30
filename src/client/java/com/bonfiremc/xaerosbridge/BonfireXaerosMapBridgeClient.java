package com.bonfiremc.xaerosbridge;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public final class BonfireXaerosMapBridgeClient implements ClientModInitializer {
	public static final String MOD_ID = "bonfire_xaeros_map_bridge";
	public static final String FIXED_MULTIPLAYER_ROOT = "Multiplayer_Any Address";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		boolean hasMinimap = FabricLoader.getInstance().isModLoaded("xaerominimap");
		boolean hasWorldMap = FabricLoader.getInstance().isModLoaded("xaeroworldmap");

		if (!hasMinimap && !hasWorldMap) {
			LOGGER.info("Xaero mods not detected. Bridge stays idle.");
			return;
		}

		Path gameDir = FabricLoader.getInstance().getGameDir().toAbsolutePath().normalize();
		LOGGER.info("Bonfire Xaero's Map Bridge loaded. Fixed multiplayer root is [{}].", FIXED_MULTIPLAYER_ROOT);
		LOGGER.info("Game directory resolved to {}", gameDir);

		XaeroConfigEnforcer.enforce(gameDir);
		XaeroMapMigration.migrate(gameDir);
	}
}
