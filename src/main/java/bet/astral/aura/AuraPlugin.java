package bet.astral.aura;

import bet.astral.api.Aura;
import bet.astral.api.color.VanillaGlowColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class AuraPlugin extends JavaPlugin {
  @Override
  public void onDisable() {
  }

  @Override
  public void onEnable() {
    Class<? extends Aura> auraClass = findAura();
    if (auraClass == null) {
      getLogger().warning("Aura does not support version: " + Bukkit.getMinecraftVersion());
      setEnabled(false);
      return;
    }

    try {
      auraClass.getConstructor().newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }

    getServer().getCommandMap().register(
      "glow",
      new BukkitCommand("glow") {
        @Override
        public boolean execute(CommandSender sender,
                               String label,
                               String[] args) {

          List<Entity> entities = ((Player) sender).getNearbyEntities(10, 10, 10);
          Aura.get().setGlowing(((Player) sender), entities, VanillaGlowColor.AQUA);

          return true;
        }
      }
    );

    getLogger().info("Aura has enabled!");
  }

  public Class<? extends Aura> findAura() {
    String minecraftVersion = Bukkit.getServer().getMinecraftVersion();
    return switch (minecraftVersion) {
      case "1.21.10", "1.21.11" -> findAuraClass("v1_21_10");
      default -> null;
    };
  }

  private Class<? extends Aura> findAuraClass(String version) {
    try {
      return (Class<? extends Aura>) Class.forName("bet.astral.aura.hooks."+version+".Aura_"+version);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
