package bet.astral.aura;

import bet.astral.aura.api.Aura;
import bet.astral.aura.api.AuraInternal;
import bet.astral.aura.api.color.VanillaGlowColor;
import bet.astral.aura.api.internal.AuraNettyInjector;
import bet.astral.aura.api.multiversion.VersionHandler;
import bet.astral.aura.api.user.AuraUser;
import bet.astral.aura.api.user.AuraUserProvider;
import bet.astral.aura.gui.GlowGUI;
import bet.astral.aura.user.UserProvider;
import bet.astral.guiman.GUIMan;
import bet.astral.guiman.gui.builders.InventoryGUIBuilder;
import bet.astral.multiversion.ClassFetcher;
import bet.astral.multiversion.Version;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

public class AuraPlugin extends JavaPlugin implements Listener {
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(AuraPlugin.class);

	public static void init(JavaPlugin plugin) {
		aura = new CoreAura();
		internal = null;
		injector = null;

		VersionHandler versionHandler = (VersionHandler) ClassFetcher.fetch("bet.astral.aura.hooks");
		if (versionHandler == null) {
			plugin.getLogger().severe("");
			plugin.getLogger().severe("Aura does not support the server version! (" + Bukkit.getMinecraftVersion()+")");
			plugin.getLogger().severe("");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
			return;
		}

		internal = versionHandler.getInternalAura();
		injector = versionHandler.getNettyInjector();
		version = versionHandler.getClass().getAnnotation(Version.class);

		aura.registerAuraInternal(internal);
		aura.registerUserProvider(userProvider);

		plugin.getServer().getPluginManager().registerEvents(new NettyManager(plugin, userProvider, injector), plugin);
		plugin.getServer().getPluginManager().registerEvents(new EntityManager(plugin, internal, aura, userProvider), plugin);
	}
	private static void initGlowPlugin(@NotNull JavaPlugin plugin) {
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
					sendVersion(plugin, sender);
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

		sendVersion(plugin, plugin.getServer().getConsoleSender());
	}

	public static void sendVersion(@NotNull JavaPlugin plugin, @NotNull CommandSender sender) {
		sender.sendMessage("---- Server ----");
		sendInfo(sender, "Minecraft Version", Bukkit.getMinecraftVersion());
		sendInfo(sender, "Bukkit Version", Bukkit.getBukkitVersion());
		sender.sendMessage("---- Aura Provider ----");
		sendInfo(sender, "Minium Version", version.miniumVersion());
		sendInfo(sender, "Maximum Version", version.maximumVersion());
		sendInfo(sender, "Internal Version", version.internalVersion());
		sendInfo(sender, "Legacy", String.valueOf(version.legacy()));
		sendInfo(sender, "Legacy Version", version.legacyVersion());

		sender.sendMessage("---- Author(s) ----");
		plugin.getPluginMeta().getAuthors().forEach(author->{
			sender.sendMessage(" - " + author);
		});
	}
	public static void sendInfo(@NotNull CommandSender sender, String name, String value){
		sender.sendMessage(name+": " + value);
	}
	static Aura aura;
	static AuraInternal internal;
	static Version version;
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
