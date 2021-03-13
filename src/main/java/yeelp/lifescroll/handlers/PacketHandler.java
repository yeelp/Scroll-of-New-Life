package yeelp.lifescroll.handlers;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import yeelp.lifescroll.LifeScroll;
import yeelp.lifescroll.network.SpawnMessage;

public final class PacketHandler
{
	public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(LifeScroll.MODID);
	private static int id = 0;
	public static final void init()
	{
		INSTANCE.registerMessage(SpawnMessage.Handler.class, SpawnMessage.class, id++, Side.CLIENT);
	}
}
