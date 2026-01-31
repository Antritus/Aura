package bet.astral.aura.hooks.v1_21_10;

import bet.astral.aura.api.Aura;
import bet.astral.aura.api.AuraInternal;
import bet.astral.aura.api.color.GlowColor;
import bet.astral.aura.api.color.VanillaGlowColor;
import bet.astral.aura.api.user.AuraUser;
import bet.astral.aura.api.user.AuraUserProvider;
import bet.astral.aura.api.user.GlowInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Aura_v1_21_10 extends Aura implements AuraInternal {
	private static final Logger log = LoggerFactory.getLogger(Aura_v1_21_10.class);
	private AuraUserProvider users = null;
	private final Map<GlowColor, PlayerTeam> globalTeams = new HashMap<>();

	public Aura_v1_21_10() {
		aura = this;
		for (VanillaGlowColor color : VanillaGlowColor.values()) {
			Scoreboard scoreboard = new Scoreboard();
			PlayerTeam team = new PlayerTeam(scoreboard, "g_" + color.getTeamName());
			team.setColor(ChatFormatting.getById(color.asVanillaColor().ordinal()));
			globalTeams.put(color, team);
		}
	}

	public void sendPacket(Player player, Packet<?> packet) {
		((CraftPlayer) player).getHandle().connection.send(packet);
	}

	@Override
	public String getUsedInternalVersion() {
		return "1.21.8";
	}

	@Override
	public void setGlowing(Player player, @NotNull Collection<? extends Entity> entities, GlowColor color, int ticks) {
		for (Entity entity : entities) {
			setGlowPacket(player, entity);
			if (color != null) {
				setTeamPacket(player, entity, color);
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
			unsetGlowPacket(player, entity);
			users.getUser(player).disableGlow(entity);
		}
	}

	@Override
	public void setGlobalGlow(Entity entity, GlowColor color) {
		users.getUser(entity).setGlobalColor(color);
		users.getUser(entity).enableGlobalGlow();
		entity.getWorld().getPlayers()
			.forEach(ent -> {
				setGlowPacket(ent, entity);
				setGlobalTeamPacket(ent, entity, color);
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
					unsetGlowPacket(ent, entity);
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
	public void createTeamGlobalPacket(Player player, GlowColor color) {

		PlayerTeam team = globalTeams.get(color);

		ClientboundSetPlayerTeamPacket packet =
			ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(
				team,
				true
			);

		sendPacket(player, packet);
	}

	@Override
	public void createTeamPacket(Player player, @NotNull Entity entity, @NotNull GlowColor color) {
		createTeamGlobalPacket(player, color);
	}

	@Override
	public void setGlobalTeamPacket(Player player, @NotNull Entity entity, GlowColor color) {
		if (color == null) {
			throw new NullPointerException("Couldn't find team for color " + "NULL");
		}
		PlayerTeam team = globalTeams.get(color);
		if (team == null) {
			log.error("Couldn't find team for color " + color);
			throw new NullPointerException("Couldn't find team for color " + color);
		}
		ClientboundSetPlayerTeamPacket addEntity =
			ClientboundSetPlayerTeamPacket.createPlayerPacket(
				team,
				entity.getUniqueId().toString(),
				ClientboundSetPlayerTeamPacket.Action.ADD
			);

		sendPacket(player, addEntity);
	}

	@Override
	public void setTeamPacket(Player player, @NotNull Entity entity, @NotNull GlowColor color) {
		setGlobalTeamPacket(player, entity, color);
	}

	@Override
	public void setGlowPacket(Player player, Entity entity) {
		var nmsEntity = ((CraftEntity) entity).getHandle();
		SynchedEntityData entityData = nmsEntity.getEntityData();

		EntityDataAccessor<Byte> FLAGS =
			new EntityDataAccessor<>(0, EntityDataSerializers.BYTE);

		byte oldFlags = entityData.get(FLAGS);
		byte newFlags = (byte) (oldFlags | 0x40);

		SynchedEntityData.DataValue<Byte> packed =
			SynchedEntityData.DataValue.create(FLAGS, newFlags);

		ClientboundSetEntityDataPacket packet =
			new ClientboundSetEntityDataPacket(
				nmsEntity.getId(),
				List.of(packed)
			);

		sendPacket(player, packet);
	}

	@Override
	public void unsetGlowPacket(Player player, Entity entity) {
		var nmsEntity = ((CraftEntity) entity).getHandle();
		SynchedEntityData entityData = nmsEntity.getEntityData();

		EntityDataAccessor<Byte> FLAGS =
			new EntityDataAccessor<>(0, EntityDataSerializers.BYTE);

		byte oldFlags = entityData.get(FLAGS);
		byte newFlags = (byte) (oldFlags & 0x40);

		SynchedEntityData.DataValue<Byte> packed =
			SynchedEntityData.DataValue.create(FLAGS, newFlags);

		ClientboundSetEntityDataPacket packet =
			new ClientboundSetEntityDataPacket(
				nmsEntity.getId(),
				List.of(packed)
			);

		sendPacket(player, packet);
	}

	@Override
	public void join(Player player, AuraUser user) {
		for (VanillaGlowColor color : VanillaGlowColor.values()) {
			createTeamGlobalPacket(player, color);
		}
	}

	@Override
	public void registerUserProvider(AuraUserProvider provider) {
		this.users = provider;
	}
}
