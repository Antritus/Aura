package bet.astral.aura.api.user;

import bet.astral.aura.api.Aura;
import bet.astral.aura.api.Tickable;
import bet.astral.aura.api.color.GlowColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface AuraUser extends Tickable {
	UUID getUniqueId();
	String getName();

	void enableGlow(Entity entity, GlowColor color, int ticks);
	void disableGlow(Entity entity);
	void disableGlow(UUID entityId);
	default void update(@NotNull GlowInfo info) {
		Entity entity = Bukkit.getEntity(info.getEntityId());
		if (entity == null) {
			disableGlow(info.getEntityId());
			return;
		}
		if (info.getTicksLeft() == 0) {
			return;
		}

		Player player = Bukkit.getPlayer(getUniqueId());
		if (info.isGlowing()) {
			Aura.get().setGlowing(
				player,
				entity,
				info.getColor(),
				info.getTicksLeft()
				);
		} else {
			Aura.get().unsetGlowing(
				player,
				entity
			);
		}
	}

	default void update(GlobalGlowInfo globalGlowInfo) {
		if (globalGlowInfo.getTicksLeft() == 0) {
			revealGlobalGlow(Bukkit.getEntity(globalGlowInfo.getEntityId()));
			return;
		}
	}

	GlowInfo getGlowInfo(@NotNull Entity entity);
	GlowInfo getGlowInfo(@NotNull UUID entity);

	void setGlobalColor(GlowColor color);
	GlowColor getGlobalColor();
	boolean hasGlobalGlow();
	void enableGlobalGlow();
	void disableGlobalGlow();

	void hideGlobalGlow(Entity entity, int ticks);
	default void hideGlobalGlow(Entity entity) {
		hideGlobalGlow(entity, -1);
	}
	void revealGlobalGlow(Entity entity);

	boolean hasPrivateGlow(UUID entity);
	boolean hasGlobalGlow(UUID viewer);
	boolean hasDisabledGlobalGlow(UUID viewer);
}
