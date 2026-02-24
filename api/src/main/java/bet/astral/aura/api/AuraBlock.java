package bet.astral.aura.api;

import bet.astral.aura.api.color.GlowColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Collection;

public abstract class AuraBlock extends Aura {
	abstract public void setGlowingLocation(Player player, Collection<? extends Location> locations, GlowColor color, int ticks);
	abstract public void setGlowingBlock(Player player, Collection<? extends Block> blocks, GlowColor color, int ticks);

	abstract public void setGlowingLocation(Player player, Location target, GlowColor color, int ticks);

	abstract public void setGlowingBlock(Player player, Block target, GlowColor color, int ticks);

	abstract public void setGlowingLocation(Player player, Collection<? extends Location> locations, GlowColor color);

	abstract public void setGlowingBlock(Player player, Collection<? extends Block> blocks, GlowColor color);

	abstract public void setGlowingLocation(Player player, Location target, GlowColor color);

	abstract public void setGlowingBlock(Player player, Block target, GlowColor color);

	abstract public void unsetGlowingBlock(Player player, Block target);
	abstract public void unsetGlowingLocation(Player player, Location target);

	abstract public void unsetGlowingLocation(Player player, Collection<? extends Block> target);

	abstract public void unsetGlowingBlock(Player player, Collection<? extends Location> target);
}
