package bet.astral.aura;

import bet.astral.aura.api.Aura;
import bet.astral.aura.api.color.VanillaGlowColor;
import bet.astral.aura.testing.TestWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Test {
	public void run(JavaPlugin plugin, @NotNull Player player){
		player.sendMessage(Component.text("Starting world generation...", NamedTextColor.YELLOW));
		TestWorld.generate();
		player.sendMessage(Component.text("World has generated!", NamedTextColor.GREEN));
		TestWorld.teleport(player);
	}
}
