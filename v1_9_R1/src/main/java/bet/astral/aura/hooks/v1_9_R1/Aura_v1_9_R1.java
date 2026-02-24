package bet.astral.aura.hooks.v1_9_R1;

import bet.astral.aura.api.AuraInternal;
import bet.astral.aura.api.color.GlowColor;
import bet.astral.aura.api.color.VanillaGlowColor;
import bet.astral.aura.api.user.AuraUser;
import bet.astral.aura.api.user.AuraUserProvider;
import bet.astral.aura.legacy.packet.MetadataPacket;
import bet.astral.aura.legacy.scoreboard.ScoreboardAction;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Aura_v1_9_R1 implements AuraInternal {
	private static Logger log = Logger.getLogger(Aura_v1_9_R1.class.getName());
	private AuraUserProvider users = null;
	private final Map<GlowColor, ScoreboardTeam> globalTeams = new HashMap<>();
	static {
		try {
			MetadataPacket.entityId = PacketPlayOutEntityMetadata.class.getDeclaredField("a");
			MetadataPacket.watcherItems = PacketPlayOutEntityMetadata.class.getDeclaredField("b");
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public Aura_v1_9_R1() {
		for (VanillaGlowColor color : VanillaGlowColor.values()) {
			Scoreboard scoreboard = new Scoreboard();
			ScoreboardTeam team = new ScoreboardTeam(scoreboard, "g_" + color.getTeamName());
			team.a(EnumChatFormat.a(color.asVanillaColor().ordinal()));
			globalTeams.put(color, team);
		}
	}

	public void sendPacket(Player player, Packet<?> packet) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	@Override
	public void createTeamGlobalPacket(Player player, GlowColor color) {

		ScoreboardTeam team = globalTeams.get(color);

		PacketPlayOutScoreboardTeam packet =
			new PacketPlayOutScoreboardTeam(team, ScoreboardAction.CREATE);

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
		ScoreboardTeam team = globalTeams.get(color);
		if (team == null) {
			log.severe("Couldn't find team for color " + color);
			throw new NullPointerException("Couldn't find team for color " + color);
		}

		PacketPlayOutScoreboardTeam addEntity =
			new PacketPlayOutScoreboardTeam(
				team,
				List.of(entity instanceof Player ?
					entity.getName() : entity.getUniqueId().toString()),
				ScoreboardAction.ADD_ENTITY
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
		DataWatcher dataWatcher = nmsEntity.getDataWatcher();
		byte oldFlags = dataWatcher.get(NettyHandler_v1_9_R1.FLAGS);
		byte newFlags = (byte) (oldFlags | 0x40);

		DataWatcher watcher = createWatcher(dataWatcher);
		watcher.register(NettyHandler_v1_9_R1.FLAGS, newFlags);

		PacketPlayOutEntityMetadata packet =  new PacketPlayOutEntityMetadata(
			nmsEntity.getId(), watcher, true
		);
		sendPacket(player, packet);
	}

	@Override
	public void unsetGlowPacket(Player player, Entity entity) {
		var nmsEntity = ((CraftEntity) entity).getHandle();
		DataWatcher dataWatcher = nmsEntity.getDataWatcher();

		byte oldFlags = dataWatcher.get(NettyHandler_v1_9_R1.FLAGS);
		byte newFlags = (byte) (oldFlags & ~0x40);

		DataWatcher watcher =  createWatcher(dataWatcher);
		dataWatcher.register(NettyHandler_v1_9_R1.FLAGS, newFlags);

		PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(
			nmsEntity.getId(),
			watcher,
			true
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

	public static @NotNull DataWatcher createWatcher(@NotNull DataWatcher data) {
		return new DataWatcher(null);
	}
	public static @NotNull DataWatcher createWatcher(@NotNull PacketPlayOutEntityMetadata packet) {
		DataWatcher watcher = new DataWatcher(null);

		try {
			List<DataWatcher.Item<?>> items = (List<DataWatcher.Item<?>>) MetadataPacket.watcherItems.get(packet);
			for (DataWatcher.Item<?> item : items) {
				copyItem(watcher, item);
			}
			return watcher;
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> void copyItem(
		@NotNull DataWatcher data,
		DataWatcher.Item<?> item
	) {
		DataWatcherObject<T> accessor = (DataWatcherObject<T>) item.a();
		T value = (T) item.b();
		data.register(accessor, value);
	}
}
