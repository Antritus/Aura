package bet.astral.aura.user;

import bet.astral.aura.api.user.AuraUser;
import bet.astral.aura.api.user.AuraUserProvider;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class UserProvider implements AuraUserProvider {
	private final Int2ObjectMap<UUID> entityIdToUuid = new Int2ObjectOpenHashMap<>();
	private final Map<UUID, AuraUser> users = new HashMap<UUID, AuraUser>();


	@Override
	public Int2ObjectMap<UUID> getEntityIdMap() {
		return entityIdToUuid;
	}

	@Override
	public AuraUser getUser(Entity entity) {
		if (!(entity instanceof Player)) {
			users.putIfAbsent(entity.getUniqueId(),
				new User(entity));
		}
		return users.get(entity.getUniqueId());
	}

	@Override
	public AuraUser getUser(UUID id) {
		return users.get(id);
	}

	@Override
	public void loadUser(@NotNull Player player) {
		users.put(player.getUniqueId(),
			new User(player));
		// TODO -> Load global glow data..?
	}

	@Override
	public void unloadUser(AuraUser player) {
		if (player == null) {
			return;
		}
		users.remove(player.getUniqueId());
	}

	@Override
	public void saveUser(AuraUser user) {
		// TODO -> Save global glow data
	}

	@Override
	public List<AuraUser> getUsers() {
		return new ArrayList<>(users.values());
	}

	@Override
	public boolean hasAccount(Entity entity) {
		return users.containsKey(entity.getUniqueId());
	}
}
