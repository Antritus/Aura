package bet.astral.aura;

public final class VersionDetector {
  public static NmsFeatureSet detect() {
    String v = org.bukkit.Bukkit.getMinecraftVersion();

    String[] parts = v.split("\\.");
    int major = Integer.parseInt(parts[0]);
    int minor = Integer.parseInt(parts[1]);

    if (major == 1 && minor <= 16) {
      return NmsFeatureSet.LEGACY;
    }

    if (major == 1 && minor <= 19) {
      return NmsFeatureSet.MODERN;
    }

    return NmsFeatureSet.MODERN_1_20_DATA;
  }
}
