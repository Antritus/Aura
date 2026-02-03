package bet.astral.aura.api;

import bet.astral.aura.api.color.GlowColor;
import bet.astral.aura.api.user.AuraUser;
import bet.astral.aura.api.user.AuraUserProvider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Aura {
	protected static Aura aura = null;

	public static Aura get() {
		return aura;
	}

	protected Map<UUID, GlowColor> globalGlowColors = new HashMap<>();

	abstract public void setGlowing(Player player, Collection<? extends Entity> entities, GlowColor color, int ticks);

	abstract public void setGlowing(Player player, Entity target, GlowColor color, int ticks);

	abstract public void setGlowing(Player player, Collection<? extends Entity> entities, GlowColor color);

	abstract public void setGlowing(Player player, Entity target, GlowColor color);

	abstract public void unsetGlowing(Player player, Entity target);

	abstract public void unsetGlowing(Player player, Collection<? extends Entity> target);

	abstract public void hideGlobalGlow(Player player, Collection<? extends Entity> entities, int ticks);

	abstract public void hideGlobalGlow(Player player, Collection<? extends Entity> entities);

	abstract public void hideGlobalGlow(Player player, Entity entity, int ticks);

	abstract public void hideGlobalGlow(Player player, Entity entity);

	abstract public void revealGlobalGlow(Player player, Collection<? extends Entity> entities);

	abstract public void revealGlobalGlow(Player player, Entity entity);

	public abstract void setGlobalGlow(Entity entity, GlowColor color);

	public abstract void unsetGlobalGlow(Entity entity);

	public abstract GlowColor getGlobalGlow(Entity entity);

	public abstract boolean isPersistentGlobalGlow(Entity entity);

	public abstract void join(Player player, AuraUser user);

	public abstract void registerUserProvider(AuraUserProvider provider);

	public abstract void registerAuraInternal(AuraInternal provider);
}
