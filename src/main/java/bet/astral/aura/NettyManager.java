package bet.astral.aura;

import bet.astral.aura.api.internal.AuraNettyInjector;
import bet.astral.aura.api.user.AuraUserProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class NettyManager implements Listener {
	private final JavaPlugin plugin;
	private final AuraUserProvider provider;
	private final AuraNettyInjector injector;

	public NettyManager(JavaPlugin plugin, AuraUserProvider provider, AuraNettyInjector injector) {
		this.plugin = plugin;
		this.provider = provider;
		this.injector = injector;
	}

	@EventHandler
	public void onJoin(@NotNull PlayerJoinEvent event) {
		Player player = event.getPlayer();

		Bukkit.getScheduler().runTask(plugin, () -> {
			injector.inject(player, provider);
		});
	}

	@EventHandler
	public void onRespawn(@NotNull PlayerRespawnEvent event) {
		Player player = event.getPlayer();

		Bukkit.getScheduler().runTask(plugin, () -> {
			injector.inject(player, provider);
		});
	}

	@EventHandler
	public void onWorldChange(@NotNull PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();

		Bukkit.getScheduler().runTask(plugin, () -> {
			injector.inject(player,  provider);
		});
	}

	@EventHandler
	public void onQuit(@NotNull PlayerQuitEvent event) {
		Player player = event.getPlayer();
		injector.uninject(player);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(@NotNull PlayerKickEvent event) {
		if (event.isCancelled()) {
			return;
		}
		Player player = event.getPlayer();
		injector.uninject(player);
	}
}
