package bet.astral.aura.api;

import bet.astral.aura.api.color.GlowColor;
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

	abstract public String getUsedInternalVersion();

	abstract public void setGlowing(Player player, Collection<? extends Entity> entities, GlowColor color, int ticks);

	abstract public void setGlowing(Player player, Entity target, GlowColor color, int ticks);

	abstract public void setGlowing(Player player, Collection<? extends Entity> entities, GlowColor color);

	abstract public void setGlowing(Player player, Entity target, GlowColor color);

	abstract public void unsetGlowing(Player player, Entity target);

	abstract public void unsetGlowing(Player player, Collection<? extends Entity> target);

	public abstract void setGlobalGlow(Entity entity, GlowColor color);

	public abstract void unsetGlobalGlow(Entity entity);

	public abstract GlowColor getGlobalGlow(Entity entity);

	public abstract boolean isPersistentGlobalGlow(Entity entity);
}
