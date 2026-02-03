package bet.astral.aura.user;

import bet.astral.aura.api.color.GlowColor;
import bet.astral.aura.api.user.AuraUser;
import bet.astral.aura.api.user.GlobalGlowInfo;
import bet.astral.aura.api.user.GlowInfo;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class User implements AuraUser {
	public final UUID uniqueId;
	private final String name;
	private boolean hasGlobalGlow;
	private GlowColor globalColor;
	private Map<UUID, GlowInfo> entityGlowInfo = new HashMap<>();
	private Map<UUID, GlobalGlowInfo> globalGlowInfo = new HashMap<>();


	public User(@NotNull Entity player) {
		this.uniqueId = player.getUniqueId();
		this.name = player.getName();
	}
	@Override
	public UUID getUniqueId() {
		return uniqueId;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void enableGlow(Entity entity, GlowColor color, int ticks) {
		GlowInfo info = new GlowInfo(
			this,
			entity,
			color,
			true,
			ticks
		);
		this.entityGlowInfo.put(entity.getUniqueId(), info);
	}

	@Override
	public void disableGlow(@NotNull Entity entity) {
		this.entityGlowInfo.remove(entity.getUniqueId());
	}

	@Override
	public void disableGlow(UUID entityId) {
		this.entityGlowInfo.remove(entityId);
	}

	@Override
	public void update(GlobalGlowInfo globalGlowInfo) {

	}

	@Override
	public GlowInfo getGlowInfo(@NotNull Entity entity) {
		return entityGlowInfo.get(entity.getUniqueId());
	}

	@Override
	public GlowInfo getGlowInfo(@NotNull UUID entity) {
		return entityGlowInfo.get(entity);
	}

	@Override
	public void setGlobalColor(GlowColor color) {
		globalColor = color;
	}

	@Override
	public GlowColor getGlobalColor() {
		return globalColor;
	}

	@Override
	public boolean hasGlobalGlow() {
		return hasGlobalGlow;
	}

	@Override
	public void enableGlobalGlow() {
		hasGlobalGlow = true;
	}

	@Override
	public void disableGlobalGlow() {
		hasGlobalGlow = false;
	}

	@Override
	public void hideGlobalGlow(@NotNull Entity entity, int ticks) {
		globalGlowInfo.put(entity.getUniqueId(), new GlobalGlowInfo(this, entity, true, ticks));
	}

	@Override
	public void revealGlobalGlow(@NotNull Entity entity) {
		globalGlowInfo.remove(entity.getUniqueId());
	}

	@Override
	public boolean hasPrivateGlow(UUID viewer) {
		return getGlowInfo(viewer) != null;
	}

	@Override
	public boolean hasGlobalGlow(UUID viewer) {
		GlobalGlowInfo info = globalGlowInfo.get(viewer);
		if (info != null) {

			return !info.isDisabled();
		}

		return hasGlobalGlow();
	}

	@Override
	public boolean hasDisabledGlobalGlow(UUID viewer) {
		GlobalGlowInfo info = globalGlowInfo.get(viewer);
		if (info != null) {

			return info.isDisabled();
		}
		return false;
	}

	@Override
	public void tick() {
		entityGlowInfo.values().forEach(GlowInfo::tick);
	}
}
