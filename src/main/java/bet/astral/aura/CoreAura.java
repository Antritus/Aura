package bet.astral.aura;

import bet.astral.aura.api.Aura;
import bet.astral.aura.api.AuraInternal;
import bet.astral.aura.api.color.GlowColor;
import bet.astral.aura.api.color.VanillaGlowColor;
import bet.astral.aura.api.user.AuraUser;
import bet.astral.aura.api.user.AuraUserProvider;
import bet.astral.aura.api.user.GlowInfo;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class CoreAura extends Aura {
	public CoreAura() {
		aura = this;
	}
	private AuraUserProvider users = null;
	private AuraInternal auraInternal = null;

	@Override
	public void setGlowing(Player player, @NotNull Collection<? extends Entity> entities, GlowColor color, int ticks) {
		for (Entity entity : entities) {
			auraInternal.setGlowPacket(player, entity);
			if (color != null) {
				auraInternal.setTeamPacket(player, entity, color);
			}
			users.getUser(player).enableGlow(entity, color, ticks);
		}
	}

	@Override
	public void setGlowing(Player player, Entity target, GlowColor color, int ticks) {
		setGlowing(player, List.of(target), color, ticks);
	}

	@Override
	public void setGlowing(Player player, Collection<? extends Entity> entities, GlowColor color) {
		setGlowing(player, entities, color, -1);
	}

	@Override
	public void setGlowing(Player player, Entity target, GlowColor color) {
		setGlowing(player, List.of(target), color);
	}

	@Override
	public void unsetGlowing(Player player, Entity target) {
		unsetGlowing(player, List.of(target));
	}

	@Override
	public void unsetGlowing(Player player, @NotNull Collection<? extends Entity> entities) {
		for (Entity entity : entities) {
			auraInternal.unsetGlowPacket(player, entity);
			users.getUser(player).disableGlow(entity);
		}
	}

	@Override
	public void hideGlobalGlow(Player player, Collection<? extends Entity> entities, int ticks) {
		for (Entity entity : entities) {
			users.getUser(player).hideGlobalGlow(entity, ticks);
			if (!users.getUser(player).hasPrivateGlow(entity.getUniqueId())) {
				auraInternal.unsetGlowPacket(player, entity);
			}
		}
	}

	@Override
	public void hideGlobalGlow(Player player, Collection<? extends Entity> entities) {
		hideGlobalGlow(player, entities, -1);
	}

	@Override
	public void hideGlobalGlow(Player player, Entity entity, int ticks) {
		hideGlobalGlow(player, List.of(entity), ticks);
	}

	@Override
	public void hideGlobalGlow(Player player, Entity entity) {
		hideGlobalGlow(player, List.of(entity));
	}

	@Override
	public void revealGlobalGlow(Player player, @NotNull Collection<? extends Entity> entities) {
		AuraUser playerUser = users.getUser(player);
		for (Entity entity : entities) {
			playerUser.revealGlobalGlow(entity);
			if (!playerUser.hasPrivateGlow(entity.getUniqueId())
				&& playerUser.hasGlobalGlow(entity.getUniqueId())) {
				auraInternal.setGlowPacket(player, entity);
			}
		}
	}

	@Override
	public void revealGlobalGlow(Player player, Entity entity) {
		revealGlobalGlow(player, List.of(entity));
	}

	@Override
	public void setGlobalGlow(Entity entity, GlowColor color) {
		AuraUser entityUser = users.getUser(entity);
		entityUser.setGlobalColor(color);
		entityUser.enableGlobalGlow();

		entity.getWorld().getPlayers()
			.stream()
			.filter(
				ent -> !users.getUser(ent).hasPrivateGlow(entity.getUniqueId()))
			.filter(ent -> entityUser.hasGlobalGlow(ent.getUniqueId()))
			.forEach(ent -> {
				auraInternal.setGlowPacket(ent, entity);
				auraInternal.setGlobalTeamPacket(ent, entity, color);
			});
	}

	@Override
	public void unsetGlobalGlow(Entity entity) {
		users.getUser(entity).disableGlobalGlow();
		entity.getWorld().getPlayers()
			.forEach(ent -> {
				AuraUser user = users.getUser(ent);
				GlowInfo info = user.getGlowInfo(entity);
				if (info == null) {
					auraInternal.unsetGlowPacket(ent, entity);
				}
			});
	}

	@Override
	public GlowColor getGlobalGlow(Entity entity) {
		return users.getUser(entity).getGlobalColor();
	}

	@Override
	public boolean isPersistentGlobalGlow(Entity entity) {
		if (!(entity instanceof Player)) {
			return false;
		}
		return true;
	}

	@Override
	public void join(Player player, AuraUser user) {
		for (VanillaGlowColor color : VanillaGlowColor.values()) {
			auraInternal.createTeamGlobalPacket(player, color);
		}
	}

	@Override
	public void registerUserProvider(AuraUserProvider provider) {
		this.users = provider;
	}

	@Override
	public void registerAuraInternal(AuraInternal provider) {
		this.auraInternal = provider;
	}
}
