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
