package bet.astral.aura.hooks.v1_13_R2;

import bet.astral.aura.api.internal.AuraNettyInjector;
import bet.astral.aura.api.user.AuraUserProvider;
import io.netty.channel.Channel;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NettyInjector_v1_13_R2 implements AuraNettyInjector {
	@Override
	public void inject(Player player, AuraUserProvider provider) {
		CraftPlayer craft = (CraftPlayer) player;

		Channel channel =
			craft.getHandle().playerConnection.networkManager.channel;

		if (channel.pipeline().get(getChannelName()) == null) {
			channel.pipeline().addBefore(
				"packet_handler",
				getChannelName(),
				createInjector(player, provider)
			);
		}
	}
	@Override
	public void uninject(Player player) {
		CraftPlayer craft = (CraftPlayer) player;

		Channel channel =
			craft.getHandle().playerConnection.networkManager.channel;

		if (channel.pipeline().get(getChannelName()) != null) {
			channel.pipeline().remove(getChannelName());
		}
	}
	@Override
	public NettyHandler_v1_13_R2 createInjector(Player player, AuraUserProvider provider) {
		return new NettyHandler_v1_13_R2(player, provider);
	}
}
