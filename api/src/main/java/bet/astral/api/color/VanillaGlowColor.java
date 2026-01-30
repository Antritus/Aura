package bet.astral.api.color;

public enum VanillaGlowColor implements GlowColor {
  /**
   * Represents black
   */
  BLACK(0x00, "black", 0, 0, 0),
  /**
   * Represents dark blue
   */
  DARK_BLUE(0x1, "dark blue", 255, 255, 255),
  /**
   * Represents dark green
   */
  DARK_GREEN(0x2, "dark green", 255, 255, 255),
  /**
   * Represents dark blue (aqua)
   */
  DARK_AQUA(0x3, "dark aqua", 255, 255, 255),
  /**
   * Represents dark red
   */
  DARK_RED(0x4, "dark red", 255, 255, 255),
  /**
   * Represents dark purple
   */
  DARK_PURPLE(0x5, "dark purple", 255, 255, 255),
  /**
   * Represents gold
   */
  GOLD(0x6, "gold", 255, 255, 255),
  /**
   * Represents gray
   */
  GRAY(0x7, "gray", 255, 255, 255),
  /**
   * Represents dark gray
   */
  DARK_GRAY(0x8, "dark gray", 255, 255, 255),
  /**
   * Represents blue
   */
  BLUE(0x9, "blue", 255, 255, 255),
  /**
   * Represents green
   */
  GREEN(0xA, "green", 255, 255, 255),
  /**
   * Represents aqua
   */
  AQUA(0xB, "aqua", 255, 255, 255),
  /**
   * Represents red
   */
  RED(0xC, "red", 255, 255, 255),
  /**
   * Represents light purple
   */
  LIGHT_PURPLE(0xD, "light purple", 255, 255, 255),
  /**
   * Represents yellow
   */
  YELLOW(0xE, "yellow", 255, 255, 255),
  /**
   * Represents white
   */
  WHITE(0xF, "white", 255, 255, 255)
  ;

  ;

  private final int vanillaId;
  private final String name;
  private final int red;
  private final int green;
  private final int blue;

  VanillaGlowColor(int vanillaId, String name, int red, int green, int blue) {
    this.vanillaId = vanillaId;
    this.name = name;
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getVanillaId() {
    return vanillaId;
  }

  @Override
  public int getRed() {
    return red;
  }

  @Override
  public int getGreen() {
    return green;
  }

  @Override
  public int getBlue() {
    return blue;
  }
}
