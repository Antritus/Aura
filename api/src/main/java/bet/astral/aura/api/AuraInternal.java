package bet.astral.aura.api;

import bet.astral.aura.api.color.GlowColor;
import bet.astral.aura.api.user.AuraUser;
import bet.astral.aura.api.user.AuraUserProvider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface AuraInternal {
	void createTeamGlobalPacket(Player player, GlowColor color);
	void createTeamPacket(Player player, Entity entity, GlowColor color);
	void setGlobalTeamPacket(Player player, Entity entity, GlowColor color);
	void setTeamPacket(Player player, Entity entity, GlowColor color);
	void setGlowPacket(Player player, Entity entity);
	void unsetGlowPacket(Player player, Entity entity);

	void join(Player player, AuraUser user);

	/**
	 * Registers a new user provider
	 * @param provider provider
	 */
	void registerUserProvider(AuraUserProvider provider);
}
