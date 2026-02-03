package bet.astral.aura.hooks.v1_21;

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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class NettyHandler_v1_21 extends ChannelDuplexHandler implements AuraNettyHandler {
	private static final EntityDataAccessor<Byte> FLAGS =
		new EntityDataAccessor<>(0, EntityDataSerializers.BYTE);
	private final Player viewer;
	private final AuraUserProvider users;

	private static final byte FLAG_GLOWING = 0x40;

	public NettyHandler_v1_21(Player viewer, AuraUserProvider users) {
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
		ClientboundSetEntityDataPacket packet,
		ChannelPromise promise
	) {

		int entityId = packet.id();

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

		List<SynchedEntityData.DataValue<?>> original = packet.packedItems();
		List<SynchedEntityData.DataValue<?>> modified = new ArrayList<>(original);

		boolean found = false;

		for (int i = 0; i < modified.size(); i++) {
			var value = modified.get(i);

			if (value.id() == 0 && value.value() instanceof Byte oldFlags) {
				byte flags = (byte) (oldFlags | FLAG_GLOWING);

				modified.set(i,
					SynchedEntityData.DataValue.create(
						FLAGS,
						flags
					)
				);

				found = true;
				break;
			}
		}

		if (!found) {
			modified.add(
				SynchedEntityData.DataValue.create(
					FLAGS,
					FLAG_GLOWING
				)
			);
		}

		ClientboundSetEntityDataPacket newPacket =
			new ClientboundSetEntityDataPacket(entityId, modified);

		ctx.write(newPacket, promise);
	}
}
