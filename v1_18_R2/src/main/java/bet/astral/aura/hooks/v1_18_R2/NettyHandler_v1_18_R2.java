package bet.astral.aura.hooks.v1_18_R2;

import bet.astral.aura.api.internal.AuraNettyHandler;
import bet.astral.aura.api.user.AuraUserProvider;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class NettyHandler_v1_18_R2 extends ChannelDuplexHandler implements AuraNettyHandler {
	public static final EntityDataAccessor<Byte> FLAGS =
		new EntityDataAccessor<>(0, EntityDataSerializers.BYTE);
	private final Player viewer;
	private final AuraUserProvider users;

	private static final byte FLAG_GLOWING = 0x40;

	public NettyHandler_v1_18_R2(Player viewer, AuraUserProvider users) {
		this.viewer = viewer;
		this.users = users;
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
		throws Exception {

		if (msg instanceof ClientboundSetEntityDataPacket packet) {
			handleMetadata(ctx, packet, promise);
			return;
		}

		super.write(ctx, msg, promise);
	}
	private void handleMetadata(
		ChannelHandlerContext ctx,
		@NotNull ClientboundSetEntityDataPacket packet,
		ChannelPromise promise
	) {

		int entityId = packet.getId();

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


		List<SynchedEntityData.DataItem<?>> original = packet.getUnpackedData();
		List<SynchedEntityData.DataItem<?>> modified = new ArrayList<>(original);

		boolean found = false;

		for (int i = 0; i < modified.size(); i++) {
			var value = modified.get(i);
			if (value.getAccessor().getId() == 0 &&  value.getValue() instanceof Byte oldFlags) {
				byte flags = (byte) (oldFlags | FLAG_GLOWING);

				modified.set(i,
					new SynchedEntityData.DataItem<>(
						FLAGS,
						flags
					)
				);

				found = true;
				break;
			}
		}

		SynchedEntityData data = Aura_v1_18_R2.createSyncedData(packet);

		if (!found) {
			data.define(FLAGS, FLAG_GLOWING);
		}

		ClientboundSetEntityDataPacket newPacket =
			new ClientboundSetEntityDataPacket(entityId, data, true);

		ctx.write(newPacket, promise);
	}

}
