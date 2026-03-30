package com.bonfiremc.xaerosbridge;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

final class XaeroConfigEnforcer {
	private XaeroConfigEnforcer() {
	}

	static void enforce(Path gameDir) {
		List<PatchTarget> targets = List.of(
			new PatchTarget(gameDir.resolve("config/xaero/minimap/client.cfg"), "differentiate_by_server_address", " = false"),
			new PatchTarget(gameDir.resolve("config/xaero/world-map/client.cfg"), "differentiate_by_server_address", " = false"),
			new PatchTarget(gameDir.resolve("config/xaerominimap.txt"), "differentiateByServerAddress", ":false"),
			new PatchTarget(gameDir.resolve("config/xaeroworldmap.txt"), "differentiateByServerAddress", ":false")
		);

		for (PatchTarget target : targets) {
			patch(target);
		}

		forceRuntimeConfig(
			"xaero.minimap.XaeroMinimap",
			"instance",
			"getHudConfigs",
			"xaero.hud.minimap.config.primary.option.MinimapPrimaryClientConfigOptions",
			"DIFFERENTIATE_BY_SERVER_ADDRESS"
		);

		forceRuntimeConfig(
			"xaero.map.WorldMap",
			"INSTANCE",
			"getConfigs",
			"xaero.map.config.primary.option.WorldMapPrimaryClientConfigOptions",
			"DIFFERENTIATE_BY_SERVER_ADDRESS"
		);
	}

	private static void forceRuntimeConfig(String ownerClassName, String instanceFieldName, String configAccessorName, String optionClassName, String optionFieldName) {
		try {
			Class<?> ownerClass = Class.forName(ownerClassName);
			Field instanceField = ownerClass.getField(instanceFieldName);
			Object ownerInstance = instanceField.get(null);

			if (ownerInstance == null) {
				BonfireXaerosMapBridgeClient.LOGGER.info("Xaero runtime config owner {} is not ready yet.", ownerClassName);
				return;
			}

			Method configAccessor = ownerClass.getMethod(configAccessorName);
			Object configChannel = configAccessor.invoke(ownerInstance);
			Object clientConfigManager = configChannel.getClass().getMethod("getClientConfigManager").invoke(configChannel);
			Object primaryConfigManager = clientConfigManager.getClass().getMethod("getPrimaryConfigManager").invoke(clientConfigManager);
			Object config = primaryConfigManager.getClass().getMethod("getConfig").invoke(primaryConfigManager);

			Class<?> optionClass = Class.forName(optionClassName);
			Field optionField = optionClass.getField(optionFieldName);
			Object option = optionField.get(null);

			Method setMethod = findConfigSetMethod(config.getClass());
			setMethod.invoke(config, option, Boolean.FALSE);
			BonfireXaerosMapBridgeClient.LOGGER.info("Forced runtime Xaero option {}.{} to false", optionClassName, optionFieldName);
		} catch (ReflectiveOperationException exception) {
			BonfireXaerosMapBridgeClient.LOGGER.warn("Failed to force runtime Xaero option {}.{}", optionClassName, optionFieldName, exception);
		}
	}

	private static Method findConfigSetMethod(Class<?> configClass) throws NoSuchMethodException {
		for (Method method : configClass.getMethods()) {
			if (method.getName().equals("set") && method.getParameterCount() == 2) {
				return method;
			}
		}

		throw new NoSuchMethodException("Could not find config set method on " + configClass.getName());
	}

	private static void patch(PatchTarget target) {
		if (!Files.exists(target.path())) {
			return;
		}

		try {
			List<String> lines = Files.readAllLines(target.path(), StandardCharsets.UTF_8);
			List<String> updated = new ArrayList<>(lines.size() + 1);
			boolean found = false;

			for (String line : lines) {
				String trimmed = line.trim();
				if (trimmed.startsWith(target.key() + " ") || trimmed.startsWith(target.key() + "=") || trimmed.startsWith(target.key() + ":")) {
					updated.add(target.key() + target.suffix());
					found = true;
				} else {
					updated.add(line);
				}
			}

			if (!found) {
				updated.add(target.key() + target.suffix());
			}

			if (!updated.equals(lines)) {
				Files.createDirectories(target.path().getParent());
				Files.write(target.path(), updated, StandardCharsets.UTF_8);
				BonfireXaerosMapBridgeClient.LOGGER.info("Forced {} in {}", target.key(), target.path());
			}
		} catch (IOException exception) {
			BonfireXaerosMapBridgeClient.LOGGER.warn("Failed to patch Xaero config {}", target.path(), exception);
		}
	}

	private record PatchTarget(Path path, String key, String suffix) {
	}
}
