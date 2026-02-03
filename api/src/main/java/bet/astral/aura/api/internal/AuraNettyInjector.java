package bet.astral.aura.api.internal;

import bet.astral.aura.api.user.AuraUserProvider;
import org.bukkit.entity.Player;

public interface AuraNettyInjector {
	void inject(Player player, AuraUserProvider provider);
	void uninject(Player player);
	default String getChannelName() {
		return "aura_glow";
	}
	AuraNettyHandler createInjector(Player player, AuraUserProvider provider);
}
