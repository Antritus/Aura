package bet.astral.aura.api.color;

public interface GlowColor {
  String getName();
  default String getTeamName() {
	  return "glow_"+getName();
  }
  int getRed();
  int getGreen();
  int getBlue();
  VanillaGlowColor asVanillaColor();
}
