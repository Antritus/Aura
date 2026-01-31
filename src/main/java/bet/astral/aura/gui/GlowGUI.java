package bet.astral.aura.gui;

import bet.astral.aura.api.Aura;
import bet.astral.aura.api.color.GlowColor;
import bet.astral.aura.api.color.VanillaGlowColor;
import bet.astral.aura.api.user.AuraUserProvider;
import bet.astral.guiman.background.Background;
import bet.astral.guiman.clickable.ClickAction;
import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.gui.InventoryGUI;
import bet.astral.guiman.gui.builders.InventoryGUIBuilder;
import bet.astral.guiman.utils.ChestRows;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GlowGUI {
	private final AuraUserProvider userProvider;
	private final Aura aura;

	public GlowGUI(AuraUserProvider userProvider, Aura aura) {
		this.userProvider = userProvider;
		this.aura = aura;
	}

	public void open(Player player) {
		InventoryGUIBuilder builder = InventoryGUI
			.builder(ChestRows.SIX)
			.background(Background.border(ChestRows.SIX, Material.BLACK_STAINED_GLASS_PANE, Material.LIGHT_GRAY_STAINED_GLASS_PANE))
			.clickable(10, globalColor(Material.RED_WOOL, VanillaGlowColor.DARK_RED))
			.clickable(11, globalColor(Material.RED_CONCRETE_POWDER, VanillaGlowColor.RED))
			.clickable(12, globalColor(Material.ORANGE_WOOL, VanillaGlowColor.GOLD))
			.clickable(13, globalColor(Material.YELLOW_WOOL, VanillaGlowColor.YELLOW))
			.clickable(14, globalColor(Material.LIME_WOOL, VanillaGlowColor.GREEN))
			.clickable(15, globalColor(Material.GREEN_WOOL, VanillaGlowColor.DARK_GREEN))
			.clickable(16, globalColor(Material.LIGHT_BLUE_WOOL, VanillaGlowColor.AQUA))
			.clickable(19, globalColor(Material.CYAN_WOOL, VanillaGlowColor.DARK_AQUA))
			.clickable(20, globalColor(Material.BLUE_CONCRETE_POWDER, VanillaGlowColor.BLUE))
			.clickable(21, globalColor(Material.BLUE_WOOL, VanillaGlowColor.DARK_BLUE))
			.clickable(22, globalColor(Material.PURPLE_WOOL, VanillaGlowColor.DARK_PURPLE))
			.clickable(23, globalColor(Material.PINK_WOOL, VanillaGlowColor.LIGHT_PURPLE))
			.clickable(24, globalColor(Material.BLACK_WOOL, VanillaGlowColor.BLACK))
			.clickable(25, globalColor(Material.GRAY_WOOL, VanillaGlowColor.DARK_GRAY))
			.clickable(28, globalColor(Material.LIGHT_GRAY_WOOL, VanillaGlowColor.GRAY))
			.clickable(29, globalColor(Material.WHITE_WOOL, VanillaGlowColor.WHITE))
			;
		builder.build().open(player);
	}

	public Clickable globalColor(Material material, GlowColor color) {
		return clickable(material, color, (context)->{
			Player player = context.getWho();
			aura.setGlobalGlow(player, color);
		});
	}
	public Clickable clickable(Material material, GlowColor color, ClickAction action) {
		return Clickable.builder(material, meta -> {
				meta.displayName(Component.text(color.getName()));
			})
			.hideItemFlags()
			.actionGeneral(action)
			.build();
	}
}
