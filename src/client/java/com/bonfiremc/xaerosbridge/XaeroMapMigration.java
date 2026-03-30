package com.bonfiremc.xaerosbridge;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

final class XaeroMapMigration {
	private XaeroMapMigration() {
	}

	static void migrate(Path gameDir) {
		migrateBranch(gameDir.resolve("xaero/minimap"));
		migrateBranch(gameDir.resolve("xaero/world-map"));
	}

	private static void migrateBranch(Path baseDir) {
		if (!Files.isDirectory(baseDir)) {
			return;
		}

		Path fixedRoot = baseDir.resolve(BonfireXaerosMapBridgeClient.FIXED_MULTIPLAYER_ROOT);
		List<Path> legacyRoots = findLegacyRoots(baseDir, fixedRoot.getFileName().toString());

		if (legacyRoots.isEmpty()) {
			return;
		}

		try {
			Files.createDirectories(fixedRoot);
		} catch (IOException exception) {
			BonfireXaerosMapBridgeClient.LOGGER.warn("Failed to create fixed Xaero root {}", fixedRoot, exception);
			return;
		}

		MigrationStats total = new MigrationStats();
		for (Path legacyRoot : legacyRoots) {
			MigrationStats stats = copyIntoFixedRoot(legacyRoot, fixedRoot);
			total.add(stats);
			BonfireXaerosMapBridgeClient.LOGGER.info(
				"Migrated Xaero root {} -> {} (copied={}, replaced={}, skipped={})",
				legacyRoot.getFileName(),
				fixedRoot.getFileName(),
				stats.copied,
				stats.replaced,
				stats.skipped
			);
		}
	}

	private static List<Path> findLegacyRoots(Path baseDir, String fixedRootName) {
		List<Path> roots = new ArrayList<>();
		try (Stream<Path> stream = Files.list(baseDir)) {
			stream
				.filter(Files::isDirectory)
				.filter(path -> path.getFileName().toString().startsWith("Multiplayer_"))
				.filter(path -> !path.getFileName().toString().equals(fixedRootName))
				.sorted(Comparator.comparing(path -> path.getFileName().toString()))
				.forEach(roots::add);
		} catch (IOException exception) {
			BonfireXaerosMapBridgeClient.LOGGER.warn("Failed to list Xaero roots in {}", baseDir, exception);
		}
		return roots;
	}

	private static MigrationStats copyIntoFixedRoot(Path sourceRoot, Path targetRoot) {
		MigrationStats stats = new MigrationStats();
		try {
			Files.walkFileTree(sourceRoot, new SimpleFileVisitor<>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					Path relative = sourceRoot.relativize(dir);
					Files.createDirectories(targetRoot.resolve(relative));
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Path relative = sourceRoot.relativize(file);
					Path targetFile = targetRoot.resolve(relative);

					if (!Files.exists(targetFile)) {
						Files.copy(file, targetFile, StandardCopyOption.COPY_ATTRIBUTES);
						stats.copied++;
						return FileVisitResult.CONTINUE;
					}

					if (Files.getLastModifiedTime(file).compareTo(Files.getLastModifiedTime(targetFile)) > 0) {
						Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
						stats.replaced++;
					} else {
						stats.skipped++;
					}

					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException exception) {
			BonfireXaerosMapBridgeClient.LOGGER.warn("Failed to migrate Xaero root {} into {}", sourceRoot, targetRoot, exception);
		}
		return stats;
	}

	private static final class MigrationStats {
		private int copied;
		private int replaced;
		private int skipped;

		private void add(MigrationStats other) {
			this.copied += other.copied;
			this.replaced += other.replaced;
			this.skipped += other.skipped;
		}
	}
}
