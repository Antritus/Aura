package bet.astral.aura.hooks.v1_19_R3;

import bet.astral.aura.api.internal.AuraNettyInjector;
import bet.astral.aura.api.user.AuraUserProvider;
import io.netty.channel.Channel;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NettyInjector_v1_19_R3 implements AuraNettyInjector {
	@Override
	public void inject(Player player, AuraUserProvider provider) {
		CraftPlayer craft = (CraftPlayer) player;

		Channel channel =
			craft.getHandle().connection.connection.channel;

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
			craft.getHandle().connection.connection.channel;

		if (channel.pipeline().get(getChannelName()) != null) {
			channel.pipeline().remove(getChannelName());
		}
	}
	@Override
	public NettyHandler_v1_19_R3 createInjector(Player player, AuraUserProvider provider) {
		return new NettyHandler_v1_19_R3(player, provider);
	}
}
