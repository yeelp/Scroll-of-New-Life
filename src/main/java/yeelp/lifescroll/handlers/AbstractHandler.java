package yeelp.lifescroll.handlers;

import net.minecraftforge.common.MinecraftForge;

/**
 * Skeleton implementation of a Forge EventHandler
 * @author Yeelp
 *
 */
public abstract class AbstractHandler
{
	public void register()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
}
