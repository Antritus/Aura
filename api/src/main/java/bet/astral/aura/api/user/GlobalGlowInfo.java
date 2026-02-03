package bet.astral.aura.api.user;

import bet.astral.aura.api.color.GlowColor;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class GlobalGlowInfo {
	private final AuraUser user;
	private final UUID entityId;
	private boolean isDisabled;
	private int ticksLeft = 0;

	public GlobalGlowInfo(AuraUser user, Entity entity, boolean isDisabled, int ticksLeft) {
		this.user = user;
		this.entityId = entity.getUniqueId();
		this.isDisabled = isDisabled;
		this.ticksLeft = ticksLeft;
	}
	public GlobalGlowInfo(AuraUser user, Entity entity, boolean isDisabled) {
		this.user = user;
		this.entityId = entity.getUniqueId();
		this.isDisabled = isDisabled;
		this.ticksLeft = -1;
	}

	public UUID getEntityId() {
		return entityId;
	}

	public int getTicksLeft() {
		return ticksLeft;
	}

	public AuraUser getUser() {
		return user;
	}

	public boolean isDisabled() {
		return isDisabled;
	}

	public GlobalGlowInfo setTicksLeft(int ticksLeft) {
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
			isDisabled = false;
			update();
		}
	}
}
