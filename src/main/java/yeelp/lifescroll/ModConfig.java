package yeelp.lifescroll;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.SlidingOption;

@Config(modid = LifeScroll.MODID)
public final class ModConfig
{
	@Name("Attempts")
	@Comment({"The amount of attempts Scroll of New Life should make when trying to spawn a player near their scroll spawn point.",
		      "Note, Scroll of New Life checks blocks above and below its search area so the amount of positions checks will be three times this value.",
		      "This value actually represents the number of XZ coordinate pairs Scroll of New Life should check when trying to find a valid spawn."})
	@RangeInt(min = 1, max = Integer.MAX_VALUE)
	@SlidingOption
	public static int tries = 20;
}
