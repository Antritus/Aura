package bet.astral.aura;

import bet.astral.aura.api.Aura;
import bet.astral.aura.api.AuraInternal;
import bet.astral.aura.api.color.VanillaGlowColor;
import bet.astral.aura.api.internal.AuraNettyInjector;
import bet.astral.aura.api.user.AuraUser;
import bet.astral.aura.api.user.AuraUserProvider;
import bet.astral.aura.gui.GlowGUI;
import bet.astral.aura.user.UserProvider;
import bet.astral.guiman.GUIMan;
import bet.astral.guiman.gui.builders.InventoryGUIBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Random;

public class AuraPlugin extends JavaPlugin implements Listener {
	public static void init(JavaPlugin plugin) {
		aura = new CoreAura();
		internal = null;
		injector = null;

		Class<? extends AuraInternal> auraClass = findAura();
		Class<? extends AuraNettyInjector> nettyClass = findInjector();
		if (auraClass == null) {
			plugin.getLogger().severe("");
			plugin.getLogger().severe("Aura does not support version: " + Bukkit.getMinecraftVersion());
			plugin.getLogger().severe("Aura does not support version: " + Bukkit.getBukkitVersion());
			plugin.getLogger().severe("");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
//			plugin.setEnabled(false); // Doesnt work in newer versions
			return;
		}

		try {
			internal = auraClass.getConstructor().newInstance();
			internal.registerUserProvider(userProvider);
		} catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		try {
			injector = nettyClass.getConstructor().newInstance();
		} catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		aura.registerAuraInternal(internal);
		aura.registerUserProvider(userProvider);

		plugin.getServer().getPluginManager().registerEvents(new NettyManager(plugin, userProvider, injector), plugin);
		plugin.getServer().getPluginManager().registerEvents(new EntityManager(plugin, internal, aura, userProvider), plugin);
	}
	public static void initGlowPlugin(JavaPlugin plugin) {
		if (!plugin.isEnabled()){
			return;
		}
		GUIMan.init(plugin);
		InventoryGUIBuilder.throwExceptionIfMessengerNull = false;

		glowGUI = new GlowGUI(userProvider, aura);

		plugin.getServer().getCommandMap().register(
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
		plugin.getServer().getCommandMap().register(
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
		plugin.getServer().getCommandMap().register(
			"aura",
			new BukkitCommand("auraversion") {
				@Override
				public boolean execute(CommandSender sender,
									   String label,
									   String[] args) {
					sender.sendMessage("");
					sender.sendMessage("Bukkit Version: " + Bukkit.getBukkitVersion());
					sender.sendMessage("Minecraft Version: " + Bukkit.getMinecraftVersion());
					sender.sendMessage("Aura Internal Version: " + internal.getUsedInternalVersion());
					sender.sendMessage("Aura Provider: " + internal.getClass().getName());
					sender.sendMessage("Aura Version: " + Bukkit.getBukkitVersion());
					sender.sendMessage("Author(s): ");
					sender.sendMessage(" - Laakkonen A.");
					sender.sendMessage("");

					return true;
				}
			}
		);
		plugin.getServer().getCommandMap().register(
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

		plugin.getServer().getCommandMap().register(
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
		plugin.getServer().getCommandMap().register(
			"aura",
			new BukkitCommand("test") {
				@Override
				public boolean execute(CommandSender sender,
									   String label,
									   String[] args) {
					new Test().run(plugin, (Player) sender);
					return true;
				}
			}
		);
	}
	static Aura aura;
	static AuraInternal internal;
	static AuraNettyInjector injector;
	static GlowGUI glowGUI;
	static final UserProvider userProvider = new UserProvider();
	static Random rand = new Random();

	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		init(this);
		initGlowPlugin(this);

		getLogger().info("Aura has enabled!");
	}

	public static @Nullable Class<? extends AuraInternal> findAura() {
		String minecraftVersion = Bukkit.getServer().getMinecraftVersion();
		return switch (minecraftVersion) {
			case "1.20.3", "1.20.4" -> findAuraClass("v1_20_R3");
			case "1.20.5", "1.20.6", "1.21", "1.21.1", "1.21.3", "1.21.4", "1.21.5", "1.21.6", "1.21.7", "1.21.8", "1.21.9", "1.21.10", "1.21.11" -> findAuraClass("v1_20_R4");
			case "1.21.2" -> throw new RuntimeException("1.21.2 minecraft version is not supported!");
			default -> null;
		};
	}

	public static @Nullable Class<? extends AuraNettyInjector> findInjector() {
		String minecraftVersion = Bukkit.getServer().getMinecraftVersion();
		return switch (minecraftVersion) {
			case "1.20.3", "1.20.4" -> findNettyClass("v1_20_R3");
			case "1.20.5", "1.20.6", "1.21", "1.21.1", "1.21.3", "1.21.4", "1.21.5", "1.21.6", "1.21.7", "1.21.8", "1.21.9", "1.21.10", "1.21.11" -> findNettyClass("v1_20_R4");
			case "1.21.2" -> throw new RuntimeException("1.21.2 minecraft version is not supported!");
			default -> null;
		};
	}

	private static @NotNull Class<? extends AuraInternal> findAuraClass(String version) {
		try {
			return (Class<? extends AuraInternal>) Class.forName("bet.astral.aura.hooks." + version + ".Aura_" + version);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private static @NotNull Class<? extends AuraNettyInjector> findNettyClass(String version) {
		try {
			return (Class<? extends AuraNettyInjector>) Class.forName("bet.astral.aura.hooks." + version + ".NettyInjector_" + version);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	public AuraUserProvider getUserProvider() {
		return userProvider;
	}

	public Aura getAura() {
		return aura;
	}

	public AuraInternal getInternal() {
		return internal;
	}

	public AuraNettyInjector getInjector() {
		return injector;
	}
}
