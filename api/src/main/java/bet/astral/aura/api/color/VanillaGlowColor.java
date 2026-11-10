package bet.astral.aura.api.color;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public enum VanillaGlowColor implements GlowColor {
	/**
	 * Represents black
	 */
	BLACK("black", '0', 0, 0, 0),
	/**
	 * Represents dark blue
	 */
	DARK_BLUE("dark_blue", '1', 0, 0, 170),
	/**
	 * Represents dark green
	 */
	DARK_GREEN("dark_green", '2', 0, 170, 0),
	/**
	 * Represents dark blue (aqua)
	 */
	DARK_AQUA("dark_aqua", '3', 0, 170, 170),
	/**
	 * Represents dark red
	 */
	DARK_RED("dark_red", '4', 170, 0, 0),
	/**
	 * Represents dark purple
	 */
	DARK_PURPLE("dark_purple", '5', 170, 0, 170),
	/**
	 * Represents gold
	 */
	GOLD("gold", '6', 255, 170, 0),
	/**
	 * Represents gray
	 */
	GRAY("gray", '7', 170, 170, 170),
	/**
	 * Represents dark gray
	 */
	DARK_GRAY("dark_gray", '8', 85, 85, 85),
	/**
	 * Represents blue
	 */
	BLUE("blue", '9', 85, 85, 255),
	/**
	 * Represents green
	 */
	GREEN("green", 'a', 85, 255, 85),
	/**
	 * Represents aqua
	 */
	AQUA("aqua", 'b', 85, 255, 255),
	/**
	 * Represents red
	 */
	RED("red", 'c', 255, 85, 85),
	/**
	 * Represents light purple
	 */
	LIGHT_PURPLE("light_purple", 'd', 255, 85, 255),
	/**
	 * Represents yellow
	 */
	YELLOW("yellow", 'e', 255, 255, 85),
	/**
	 * Represents white
	 */
	WHITE("white", 'f', 255, 255, 255);;

	private final String name;
	private final char code;
	private final int red;
	private final int green;
	private final int blue;

	VanillaGlowColor(String name, char code, int red, int green, int blue) {
		this.name = name;
		this.code = code;
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

	public char getCode() {
		return code;
	}

	public NamedTextColor asNamedTextColor() {
		return NamedTextColor.nearestTo(TextColor.color(red, green, blue));
	}

	@Override
	public VanillaGlowColor asVanillaColor() {
		return this;
	}
}
