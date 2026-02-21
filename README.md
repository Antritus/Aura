# Aura
Aura is a packet based player glow management plugin with functionality to make players and entities glow per player.

```java
package bet.astral.aura;

import bet.astral.aura.api.Aura;
import bet.astral.aura.api.color.VanillaGlowColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Test {
	public void run(JavaPlugin plugin, @NotNull Player player){
		Aura aura = Aura.get();
		Random random = new Random();

		player.getScheduler().runAtFixedRate(plugin,
			task->{
				player.getWorld().getNearbyEntities(player.getLocation(), 50, 50, 50)
					.forEach(entity -> {
						aura.setGlowing(player, entity, VanillaGlowColor.values()[random.nextInt(VanillaGlowColor.values().length)]);
					});
			},
			null,
			1,
			25);
	}
}

```

## Gradle

```groovy
repositories {
	maven("https://jitpack.io")
}

dependencies {
	implementation("com.github.AstralLiteratureClub:Aura:-")
}
```



## Minecraft Support


| Version       | Entity Glow | Untested? | In Development? |
|---------------|-----------|--------|-------------|
| 1.21–1.21.11  | ✅         | ❌       | ✅           |
| 1.20.5–1.20.6 | ✅         | ❌       | ✅           |
| 1.20–1.20.4   | ✅          | ✅      | ✅           |
| 1.19–1.19.4   | ✅          | ✅      | ✅           |
| 1.18–1.18.2   | ❌         | ✅      | ✅           |
| 1.17–1.17.1   | ❌         | ❌       | ❌            |
