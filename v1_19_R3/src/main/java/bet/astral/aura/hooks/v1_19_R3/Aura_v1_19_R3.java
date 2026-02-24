package bet.astral.aura.hooks.v1_19_R3;

import bet.astral.aura.api.AuraInternal;
import bet.astral.aura.api.color.GlowColor;
import bet.astral.aura.api.color.VanillaGlowColor;
import bet.astral.aura.api.user.AuraUser;
import bet.astral.aura.api.user.AuraUserProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Aura_v1_19_R3 implements AuraInternal {
	private static final Logger log = LoggerFactory.getLogger(Aura_v1_19_R3.class);
	private AuraUserProvider users = null;
	private final Map<GlowColor, PlayerTeam> globalTeams = new HashMap<>();

	public Aura_v1_19_R3() {
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
				entity instanceof Player ?
					entity.getName() : entity.getUniqueId().toString(),
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
		byte newFlags = (byte) (oldFlags & ~0x40);

		SynchedEntityData.DataValue<Byte> packed = SynchedEntityData.DataValue.create(FLAGS, newFlags);

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
