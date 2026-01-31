package bet.astral.aura.api.user;

import bet.astral.aura.api.color.GlowColor;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class GlowInfo {
	private final AuraUser user;
	private final UUID entityId;
	private GlowColor color;
	private boolean isGlowing;
	private int ticksLeft = 0;

	public GlowInfo(AuraUser user, Entity entity, GlowColor color, boolean isGlowing, int ticksLeft) {
		this.user = user;
		this.entityId = entity.getUniqueId();
		this.color = color;
		this.isGlowing = isGlowing;
		this.ticksLeft = ticksLeft;
	}
	public GlowInfo(AuraUser user, Entity entity, GlowColor color, boolean isGlowing) {
		this.user = user;
		this.entityId = entity.getUniqueId();
		this.color = color;
		this.isGlowing = isGlowing;
		this.ticksLeft = -1;
	}

	public UUID getEntityId() {
		return entityId;
	}

	public GlowColor getColor() {
		return color;
	}

	public boolean isGlowing() {
		return isGlowing;
	}

	public int getTicksLeft() {
		return ticksLeft;
	}

	public GlowInfo setColor(GlowColor color) {
		this.color = color;
		return this;
	}

	public GlowInfo setGlowing(boolean glowing) {
		isGlowing = glowing;
		return this;
	}

	public GlowInfo setTicksLeft(int ticksLeft) {
		this.ticksLeft = ticksLeft;
		return this;
	}

	public void update() {
		user.update(this);
	}

	public void tick() {
		if (ticksLeft == -1) {
			return;
		}
		ticksLeft--;
		if (ticksLeft <= 0) {
			isGlowing = false;
			update();
		}
	}
}
