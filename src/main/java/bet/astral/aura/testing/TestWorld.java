package bet.astral.aura.testing;

import bet.astral.aura.AuraPlugin;
import bet.astral.aura.api.Aura;
import bet.astral.aura.api.color.VanillaGlowColor;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TestWorld {
	private static World world;
	private static Random random = new Random(500000);
	private static final List<EntityType> SPAWNABLE_TYPES = Arrays.stream(EntityType.values())
		.filter(type -> type.isSpawnable() && type != EntityType.PLAYER)
		.toList();

	// Then in generateEntity:
	public static void teleport(@NotNull Player player) {
		player.teleport(new Location(world, 0, 101, 0));
		player.setAllowFlight(true);
	}

	public static void generate(){
		World world = Bukkit.getWorld("aura_test");
		if (world == null){
			File file = new File(Bukkit.getWorldContainer(), "aura_test");
			if (file != null){
				file.delete();
			}
			actuallyGenerate();
		}
	}

	private static void actuallyGenerate() {
		World world = new WorldCreator("aura_test", new NamespacedKey("aura", "test"))
			.generator(new TestWorldGenerator())
			.biomeProvider(new BiomeProvider() {
				@Override
				public @NotNull Biome getBiome(@NotNull WorldInfo worldInfo, int i, int i1, int i2) {
					return Biome.PLAINS;
				}

				@Override
				public @NotNull List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {
					return List.of(Biome.PLAINS);
				}
			})
			.environment(World.Environment.NORMAL)
			.createWorld();

		TestWorld.world	= world;

		world.setDifficulty(Difficulty.EASY);

		Bukkit.getScheduler().runTaskLater(AuraPlugin.getPlugin(AuraPlugin.class), () -> {
			generateEntities();
		}, 5);
	}

	public static void generateEntities() {
		for (int i = 0; i < 50; i++) {
			generateEntity();
		}
	}
	public static void generateEntity() {
		// 1. Get a safe location
		double x = (random.nextDouble() * 30) - 15;
		double z = (random.nextDouble() * 30) - 15;
		Location spawnLoc = new Location(world, x, 101, z);

		// 2. Force the chunk to load so the entity has "ground" to stand on
		if (!spawnLoc.isChunkLoaded()) {
			spawnLoc.getChunk().load();
		}

		// 3. Filter for spawnable entities only
		EntityType type = SPAWNABLE_TYPES.get(random.nextInt(SPAWNABLE_TYPES.size()));
		// 4. Spawn on the main thread
		EntityType finalType = type;
		Entity entity = world.spawnEntity(spawnLoc, finalType);
		if (entity != null) {
			//entity.setNoPhysics(true);
			if (entity instanceof LivingEntity livingEntity) {
				//livingEntity.setAI(false);
				livingEntity.setInvulnerable(true);
				if (random.nextBoolean()){
					livingEntity.setInvisible(true);
				}
			}

//			entity.setGlowing(true);

			randomColorGlow(entity);
		}
	}

	public static void randomColorGlow(Entity entity) {
		if (random.nextBoolean()) {
			Aura.get().setGlobalGlow(entity, VanillaGlowColor.values()[random.nextInt(VanillaGlowColor.values().length)]);
		} else {
			rainbowColorGlow(entity);
		}
	}

	public static void rainbowColorGlow(@NotNull Entity entity) {
		final int[] colorIndex = {0};
		VanillaGlowColor[] colors = VanillaGlowColor.values();

		Bukkit.getScheduler().runTaskTimer(AuraPlugin.getPlugin(AuraPlugin.class), task -> {
			// If entity is dead or world is gone, stop the task immediately
			if (!entity.isValid() || entity.isDead()) {
				task.cancel();
				return;
			}

			Aura.get().setGlobalGlow(entity, colors[colorIndex[0]]);
			colorIndex[0] = (colorIndex[0] + 1) % colors.length;
		}, 20L, 10L);
	}
}
