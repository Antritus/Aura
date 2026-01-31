package bet.astral.aura;

import bet.astral.aura.api.Aura;
import bet.astral.aura.api.AuraInternal;
import bet.astral.aura.api.color.VanillaGlowColor;
import bet.astral.aura.api.user.AuraUser;
import bet.astral.aura.api.user.GlowInfo;
import bet.astral.aura.gui.GlowGUI;
import bet.astral.aura.user.UserProvider;
import bet.astral.guiman.GUIMan;
import bet.astral.guiman.gui.builders.InventoryGUIBuilder;
import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Random;

public class AuraPlugin extends JavaPlugin implements Listener {
	private GlowGUI glowGUI;
	private static final Logger log = LoggerFactory.getLogger(AuraPlugin.class);
	private final UserProvider userProvider = new UserProvider(this);
	Random rand = new Random();

	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		GUIMan.init(this);
		InventoryGUIBuilder.throwExceptionIfMessengerNull = false;
		Class<? extends Aura> auraClass = findAura();
		if (auraClass == null) {
			getLogger().warning("Aura does not support version: " + Bukkit.getMinecraftVersion());
			setEnabled(false);
			return;
		}

		try {
			Aura aura = auraClass.getConstructor().newInstance();
			AuraInternal internal =  (AuraInternal) aura;
			internal.registerUserProvider(userProvider);
			glowGUI = new GlowGUI(userProvider, aura);
		} catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		getServer().getCommandMap().register(
			"aura",
			new BukkitCommand("glowmenu") {
				@Override
				public boolean execute(CommandSender sender,
									   String label,
									   String[] args) {
					glowGUI.open((Player) sender);
					return true;
				}
			}
		);
		getServer().getCommandMap().register(
			"aura",
			new BukkitCommand("glow") {
				@Override
				public boolean execute(CommandSender sender,
									   String label,
									   String[] args) {

					List<Entity> entities = ((Player) sender).getNearbyEntities(80, 80, 80);

					for (Entity entity : entities) {
						Aura.get().setGlobalGlow(entity, VanillaGlowColor.values()[rand.nextInt(VanillaGlowColor.values().length)]);
					}

					return true;
				}
			}
		);
		getServer().getCommandMap().register(
			"aura",
			new BukkitCommand("unglow") {
				@Override
				public boolean execute(CommandSender sender,
									   String label,
									   String[] args) {

					List<Entity> entities = ((Player) sender).getNearbyEntities(80, 80, 80);
					for (Entity entity : entities) {
						Aura.get().unsetGlobalGlow(entity);
					}
					return true;
				}
			}
		);

		getServer().getCommandMap().register(
			"aura",
			new BukkitCommand("listentities") {
				@Override
				public boolean execute(CommandSender sender,
									   String label,
									   String[] args) {
					List<AuraUser> users = userProvider.getUsers();
					for (AuraUser user : users) {
						Entity entity = Bukkit.getEntity(user.getUniqueId());
						if (entity == null) {
							continue;
						}
						if (user.getGlobalColor() == null) {
							sender.sendMessage(entity.getType().name() +" - NULL");
						} else {
							sender.sendMessage(entity.getType().name() + " - " + user.getGlobalColor().getName());
						}
					}
					return true;
				}
			}
		);

		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("Aura has enabled!");
	}

	public Class<? extends Aura> findAura() {
		String minecraftVersion = Bukkit.getServer().getMinecraftVersion();
		return switch (minecraftVersion) {
			case "1.21.10", "1.21.11" -> findAuraClass("v1_21_10");
			default -> null;
		};
	}

	private @NotNull Class<? extends Aura> findAuraClass(String version) {
		try {
			return (Class<? extends Aura>) Class.forName("bet.astral.aura.hooks." + version + ".Aura_" + version);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
		// Load user data
		userProvider.loadUser(event.getPlayer());
		AuraUser user = userProvider.getUser(event.getPlayer());

		// Load glowing stuff
		AuraInternal auraInternal = (AuraInternal) Aura.get();
		auraInternal.join(event.getPlayer(), user);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
		userProvider.unloadUser(userProvider.getUser(event.getPlayer()));
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityAddToWorld(@NotNull PlayerChunkLoadEvent event) {
		Player player = event.getPlayer();

		Bukkit.getScheduler().runTaskLater(this, ()->{
			Chunk chunk = event.getChunk();
			for (Entity entity : chunk.getEntities()) {
				fixEntity(player, entity);
			}
		}, 10);
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityAddToWorld(@NotNull EntityAddToWorldEvent event) {
		Bukkit.getScheduler().runTaskLater(this, ()->{
			for (Player player : event.getEntity().getWorld().getPlayers()) {
				fixEntity(player, event.getEntity());
			}
		}, 2);
	}

	public void fixEntity(Player player, Entity entity) {
		AuraInternal internal =  (AuraInternal) Aura.get();
		AuraUser user = userProvider.getUser(player);

		GlowInfo glowInfo = user.getGlowInfo(entity);
		if (glowInfo != null && glowInfo.isGlowing()) {
			internal.setGlowPacket(player, entity);
			if (glowInfo.getColor() != null) {
				internal.setGlobalTeamPacket(player, entity, glowInfo.getColor());
			}
		} else {
			AuraUser entityUser = userProvider.getUser(entity);
			if (entityUser != null && entityUser.hasGlobalGlow()) {
				internal.setGlowPacket(player, entity);
				if (entityUser.getGlobalColor() != null) {
					internal.setGlobalTeamPacket(player, entity, entityUser.getGlobalColor());
				}
			}
		}
	}

}
