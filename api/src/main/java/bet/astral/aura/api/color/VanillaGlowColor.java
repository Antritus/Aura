package bet.astral.aura.api.color;

public enum VanillaGlowColor implements GlowColor {
  /**
   * Represents black
   */
  BLACK("black", 0, 0, 0),
  /**
   * Represents dark blue
   */
  DARK_BLUE("dark_blue", 255, 255, 255),
  /**
   * Represents dark green
   */
  DARK_GREEN("dark_green", 255, 255, 255),
  /**
   * Represents dark blue (aqua)
   */
  DARK_AQUA("dark_aqua", 255, 255, 255),
  /**
   * Represents dark red
   */
  DARK_RED("dark_red", 255, 255, 255),
  /**
   * Represents dark purple
   */
  DARK_PURPLE("dark_purple", 255, 255, 255),
  /**
   * Represents gold
   */
  GOLD("gold", 255, 255, 255),
  /**
   * Represents gray
   */
  GRAY("gray", 255, 255, 255),
  /**
   * Represents dark gray
   */
  DARK_GRAY("dark_gray", 255, 255, 255),
  /**
   * Represents blue
   */
  BLUE("blue", 255, 255, 255),
  /**
   * Represents green
   */
  GREEN("green", 255, 255, 255),
  /**
   * Represents aqua
   */
  AQUA("aqua", 255, 255, 255),
  /**
   * Represents red
   */
  RED("red", 255, 255, 255),
  /**
   * Represents light purple
   */
  LIGHT_PURPLE("light_purple", 255, 255, 255),
  /**
   * Represents yellow
   */
  YELLOW("yellow", 255, 255, 255),
  /**
   * Represents white
   */
  WHITE("white", 255, 255, 255)
  ;

  ;

  private final String name;
  private final int red;
  private final int green;
  private final int blue;

  VanillaGlowColor(String name, int red, int green, int blue) {
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

	@Override
	public VanillaGlowColor asVanillaColor() {
		return this;
	}
}
