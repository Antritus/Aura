package bet.astral.api;

import bet.astral.api.color.GlowColor;
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

  abstract public void setGlowing(Player player, Collection<? extends Entity> entities, GlowColor color);
  abstract public void setGlowing(Player player, Entity target, GlowColor color);

  abstract public void unsetGlowing(Player player, Entity target);
  abstract public void unsetGlowing(Player player, Collection<? extends Entity> target);

  abstract public void setGlobalGlow(Entity player, GlowColor color, boolean persistent);
  public void setGlobalGlow(Entity player, GlowColor color) {
    setGlobalGlow(player, color, false);
  }
  public abstract void unsetGlobalGlow(Entity player);
  public abstract GlowColor getGlobalGlow(Entity player);
  public abstract boolean isPersistentGlobalGlow(Entity player);
}
