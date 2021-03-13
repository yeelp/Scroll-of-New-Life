package yeelp.lifescroll;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yeelp.lifescroll.capability.ScrollSpawn;
import yeelp.lifescroll.handlers.CapabilityHandler;
import yeelp.lifescroll.handlers.PacketHandler;
import yeelp.lifescroll.handlers.ScrollHandler;
import yeelp.lifescroll.handlers.SpawnHandler;
import yeelp.lifescroll.item.LifeScrollItems;
import yeelp.lifescroll.proxy.Proxy;

@Mod(modid = LifeScroll.MODID, name = LifeScroll.NAME, version = LifeScroll.VERSION)
public class LifeScroll
{
    public static final String MODID = "lifescroll";
    public static final String NAME = "Scroll of New Life";
    public static final String VERSION = "${version}";
    public static final String CLIENT_PROXY = "yeelp.lifescroll.proxy.ClientProxy";
    public static final String SERVER_PROXY = "yeelp.lifescroll.proxy.Proxy";

    private static Logger logger;
    
    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
    public static Proxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        ScrollSpawn.register();
        proxy.preInit();
        PacketHandler.init();
        info("Scroll of New Life pre-initialization complete!");
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        new CapabilityHandler().register();
        new SpawnHandler().register();
        new ScrollHandler().register();
        info("Scroll of New Life initialization complete!");
    }
    
    public static void info(String msg)
    {
    	logger.info("[SoNL] "+msg);
    }
}
