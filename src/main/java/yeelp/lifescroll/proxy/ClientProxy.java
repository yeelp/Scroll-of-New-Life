package yeelp.lifescroll.proxy;

import yeelp.lifescroll.item.LifeScrollItems;

public class ClientProxy extends Proxy
{
	@Override
	public void preInit()
	{
		super.preInit();
		LifeScrollItems.registerRenders();
	}
}
