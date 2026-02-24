package bet.astral.aura.hooks.v1_16_R2;

import bet.astral.aura.api.internal.AuraNettyHandler;
import bet.astral.aura.api.user.AuraUserProvider;
import bet.astral.aura.legacy.packet.MetadataPacket;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_16_R2.DataWatcher;
import net.minecraft.server.v1_16_R2.DataWatcherObject;
import net.minecraft.server.v1_16_R2.DataWatcherRegistry;
import net.minecraft.server.v1_16_R2.PacketPlayOutEntityMetadata;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class NettyHandler_v1_16_R2 extends ChannelDuplexHandler implements AuraNettyHandler {
	public static final DataWatcherObject<Byte> FLAGS =
		new DataWatcherObject<>(0, DataWatcherRegistry.a);
	private final Player viewer;
	private final AuraUserProvider users;

	private static final byte FLAG_GLOWING = 0x40;

	public NettyHandler_v1_16_R2(Player viewer, AuraUserProvider users) {
		this.viewer = viewer;
		this.users = users;
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
		throws Exception {

		if (msg instanceof PacketPlayOutEntityMetadata packet) {
			handleMetadata(ctx, packet, promise);
			return;
		}

		super.write(ctx, msg, promise);
	}
	private void handleMetadata(
		ChannelHandlerContext ctx,
		@NotNull PacketPlayOutEntityMetadata packet,
		ChannelPromise promise
	)  {

		int entityId = 0;
		try {
			entityId = (int) MetadataPacket.entityId.get(packet);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		UUID uuid = users.getEntityIdMap().get(entityId);
		if (uuid == null) {
			ctx.write(packet, promise);
			return;
		}

		boolean shouldGlow =
			users.getUser(viewer).hasPrivateGlow(uuid)
				|| users.getUser(uuid) != null && users.getUser(uuid).hasGlobalGlow(viewer.getUniqueId());

		if (!shouldGlow) {
			ctx.write(packet, promise);
			return;
		}


		List<DataWatcher.Item<?>> original = null;
		try {
			original = (List<DataWatcher.Item<?>>) MetadataPacket.watcherItems.get(packet);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		List<DataWatcher.Item<?>> modified = new ArrayList<>(original);

		boolean found = false;

		for (int i = 0; i < modified.size(); i++) {
			var value = modified.get(i);
			if (value.a().a() == 0 &&  value.b() instanceof Byte oldFlags) {
				byte flags = (byte) (oldFlags | FLAG_GLOWING);

				modified.set(i,
					new DataWatcher.Item<>(
						FLAGS,
						flags
					)
				);

				found = true;
				break;
			}
		}

		DataWatcher data = Aura_v1_16_R2.createWatcher(packet);
		if (!found) {
			data.register(FLAGS, FLAG_GLOWING);
		}

		PacketPlayOutEntityMetadata newPacket =
			new PacketPlayOutEntityMetadata(
				entityId,
				data,
				true
			);

		ctx.write(newPacket, promise);
	}

}
