package jaymanh.fsts.client.compat.jei;

import jaymanh.fsts.FromStoneToStars;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;

import net.minecraft.resources.Identifier;

@JeiPlugin
public final class FstsJeiPlugin implements IModPlugin {
	@Override
	public Identifier getPluginUid() {
		return FromStoneToStars.id("jei");
	}
}
