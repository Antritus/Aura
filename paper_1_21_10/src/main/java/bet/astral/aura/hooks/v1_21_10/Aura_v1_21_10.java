package bet.astral.aura.hooks.v1_21_10;

import bet.astral.api.Aura;
import bet.astral.api.color.GlowColor;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public class Aura_v1_21_10 extends Aura {
  public Aura_v1_21_10() {
    aura = this;
  }

  public void sendPacket(Player player, Packet<?> packet) {
    ((CraftPlayer) player).getHandle().connection.send(packet);
  }

  @Override
  public String getUsedInternalVersion() {
    return "1.21.8";
  }

  @Override
  public void setGlowing(Player player, Collection<? extends Entity> entities, GlowColor color) {
    for (Entity entity : entities) {
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
  public void unsetGlowing(Player player, Collection<? extends Entity> target) {

  }

  @Override
  public void setGlobalGlow(Entity player, GlowColor color, boolean persistent) {

  }

  @Override
  public void unsetGlobalGlow(Entity player) {

  }

  @Override
  public GlowColor getGlobalGlow(Entity player) {
    return null;
  }

  @Override
  public boolean isPersistentGlobalGlow(Entity player) {
    return false;
  }
}
