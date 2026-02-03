package bet.astral.aura;

import bet.astral.aura.api.Aura;
import bet.astral.aura.api.AuraInternal;
import bet.astral.aura.api.user.AuraUser;
import bet.astral.aura.api.user.AuraUserProvider;
import bet.astral.aura.api.user.GlowInfo;
import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class EntityManager implements Listener {
	private final JavaPlugin plugin;
	private final Aura aura;
	private final AuraInternal auraInternal;
	private final AuraUserProvider userProvider;

	public EntityManager(JavaPlugin plugin, AuraInternal auraInternal, Aura aura, AuraUserProvider userProvider) {
		this.plugin = plugin;
		this.aura = aura;
		this.auraInternal = auraInternal;
		this.userProvider = userProvider;
	}

	private void cache(@NotNull EntityEvent event){
		userProvider.getEntityIdMap().put(event.getEntity().getEntityId(), event.getEntity().getUniqueId());
	}
	private void cache(@NotNull PlayerEvent event){
		userProvider.getEntityIdMap().put(event.getPlayer().getEntityId(), event.getPlayer().getUniqueId());
	}

	private void decache(@NotNull EntityEvent event){
		userProvider.getEntityIdMap().put(event.getEntity().getEntityId(), event.getEntity().getUniqueId());
	}
	private void decache(@NotNull PlayerEvent event){
		userProvider.getEntityIdMap().put(event.getPlayer().getEntityId(), event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onSpawn(EntityAddToWorldEvent event) {
		cache(event);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		cache(event);
	}

	@EventHandler
	public void onRemove(EntityRemoveFromWorldEvent event) {
		decache(event);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		decache(event);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
		// Get user data
		userProvider.loadUser(event.getPlayer());
		AuraUser user = userProvider.getUser(event.getPlayer());

		// Load glowing stuff
		auraInternal.join(event.getPlayer(), user);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
		userProvider.unloadUser(userProvider.getUser(event.getPlayer()));
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityAddToWorld(@NotNull PlayerChunkLoadEvent event) {
		Player player = event.getPlayer();

		Bukkit.getScheduler().runTaskLater(plugin, ()->{
			Chunk chunk = event.getChunk();
			for (Entity entity : chunk.getEntities()) {
				fixEntity(player, entity);
			}
		}, 10);
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityAddToWorld(@NotNull EntityAddToWorldEvent event) {
		Bukkit.getScheduler().runTaskLater(plugin, ()->{
			for (Player player : event.getEntity().getWorld().getPlayers()) {
				fixEntity(player, event.getEntity());
			}
		}, 10);
	}

	public void fixEntity(Player player, Entity entity) {
		AuraUser user = userProvider.getUser(player);

		if (user == null){
			Bukkit.getScheduler().runTaskLater(plugin, ()-> fixEntity(player, entity), 5);
			return;
		}

		GlowInfo glowInfo = user.getGlowInfo(entity);
		if (glowInfo != null && glowInfo.isGlowing()) {
			auraInternal.setGlowPacket(player, entity);
			if (glowInfo.getColor() != null) {
				auraInternal.setGlobalTeamPacket(player, entity, glowInfo.getColor());
			}
		} else {
			AuraUser entityUser = userProvider.getUser(entity);
			if (entityUser != null && entityUser.hasGlobalGlow() && entityUser.hasGlobalGlow(player.getUniqueId())) {
				auraInternal.setGlowPacket(player, entity);
				if (entityUser.getGlobalColor() != null) {
					auraInternal.setGlobalTeamPacket(player, entity, entityUser.getGlobalColor());
				}
			}
		}
	}
}
