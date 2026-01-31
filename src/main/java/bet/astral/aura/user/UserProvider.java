package bet.astral.aura.user;

import bet.astral.aura.api.user.AuraUser;
import bet.astral.aura.api.user.AuraUserProvider;
import bet.astral.aura.AuraPlugin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class UserProvider implements AuraUserProvider {
	private final Map<UUID, AuraUser> users = new HashMap<UUID, AuraUser>();

	public UserProvider(AuraPlugin auraPlugin) {

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
