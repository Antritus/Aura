package bet.astral.aura;

public enum NmsFeatureSet {
    LEGACY,          // 1.8 – 1.16 (old NMS, watchers, R versions)
    MODERN,          // 1.17 – 1.19 (mojang mappings, old packets)
    MODERN_1_20_DATA // 1.20+ (DataValue packet change)
}
