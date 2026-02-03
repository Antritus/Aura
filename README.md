# Aura
Aura is a packet based player glow management plugin with functionality to make players and entities glow per player.

```java
import bet.astral.guiman.api.sign.api.Sign;
import bet.astral.guiman.api.sign.api.SignAction;
import bet.astral.guiman.api.sign.api.material.SignMaterial;
import bet.astral.guiman.api.sign.api.material.SmallSignMaterial;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

import java.util.List;

public class Test {
	public void run(Player player) {
		Sign sign = Sign.builder()
			.setMaterial(SignMaterial.CHERRY)
			.setColor(DyeColor.GREEN)
			.setLines(
				Component.text(""),
				Component.text("^^^^^^^^^^^^^^^^"),
				Component.text("Write something"),
				Component.text("Hey I am red", NamedTextColor.RED)
				)
			.setHandler(() -> List.of(
				SignAction.run((p, result) -> {
					p.sendMessage("You wrote: " + result.getFirstPlain());
				}),
				SignAction.openSign(
					Sign.builder()
						.setMaterial(SmallSignMaterial.JUNGLE)
						.setColor(DyeColor.WHITE)
						.setLinePlain(1, "Beep Beep")
						.setLinePlain(2, "Write Something")
						.setLinePlain(3, "VVVVVVVVVVVVVVVV")
						.setLinePlain(4, "")
						.setOpenConsumer(p -> {
							Bukkit.broadcast(p.name().color(NamedTextColor.GREEN)
								.append(Component.text(" has opened the second sign!", NamedTextColor.YELLOW)));
						})
						.setHandler(() ->
							List.of(
								(p2, result2) -> {
									p2.sendMessage("You wrote: " + result2.getFourthPlain());
								}
							)
						).build())
				)
			).build();
		
		sign.open(player);
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


| Version        | Entity Glow | Later? |
|----------------|-------------|--------|
| 1.21–1.21.11   | ✅           | ✅      |
| 1.20.5–1.20.6  | ✅           | ✅      |
| 1.20–1.20.4    | ❌           | ✅      |
| 1.19–1.19.4    | ❌           | ✅      |
| 1.18–1.18.2    | ❌           | ✅      |
| 1.17–1.17.1    | ❌           | ✅      |
