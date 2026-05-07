package bet.astral.aura.testing;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class TestWorldGenerator extends ChunkGenerator {
	@Override
	public void generateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random,
							  int chunkX, int chunkZ, @NotNull ChunkData chunkData) {

		int minX = -50;
		int maxX = 50;
		int minZ = -50;
		int maxZ = 50;
		int y = 100;

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int worldX = (chunkX << 4) + x;
				int worldZ = (chunkZ << 4) + z;

				if (worldX >= minX && worldX <= maxX &&
					worldZ >= minZ && worldZ <= maxZ) {

					chunkData.setBlock(x, y, z, Material.GREEN_CONCRETE);
				}
			}
		}
	}

		@Override
		public void generateSurface(@NotNull WorldInfo worldInfo, @NotNull Random random,
									int chunkX, int chunkZ, @NotNull ChunkData chunkData) {

			int minX = -50;
			int maxX = 50;
			int minZ = -50;
			int maxZ = 50;
			int y = 100;

			// Loop through all blocks in this chunk (16x16)
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {

					// Convert local chunk coords to world coords
					int worldX = (chunkX << 4) + x;
					int worldZ = (chunkZ << 4) + z;

					// Check if inside the square
					if (worldX >= minX && worldX <= maxX &&
						worldZ >= minZ && worldZ <= maxZ) {

						chunkData.setBlock(x, y, z, Material.GREEN_CONCRETE);
					}
				}
			}
		}

		@Override
		public void generateBedrock(@NotNull WorldInfo worldInfo, @NotNull Random random,
									int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
			// No bedrock generation needed for this test
		}
	}

