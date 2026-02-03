package bet.astral.aura.api.user;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface AuraUserProvider {
	Int2ObjectMap<UUID> getEntityIdMap();


	AuraUser getUser(Entity entity);
	AuraUser getUser(UUID entityId);

	void loadUser(Player player);
	void unloadUser(AuraUser player);
	void saveUser(AuraUser user);

	List<AuraUser> getUsers();

	boolean hasAccount(Entity entity);
}
