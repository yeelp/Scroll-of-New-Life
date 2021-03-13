package yeelp.lifescroll.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public final class LifeScrollItems
{
	public static Item spawnScroll;
	
	public static void init()
	{
		spawnScroll = new ItemSpawnScroll();
		ForgeRegistries.ITEMS.register(spawnScroll);
	}

	public static void registerRenders()
	{
		ModelLoader.setCustomModelResourceLocation(spawnScroll, 0, new ModelResourceLocation(spawnScroll.getRegistryName(), "inventory"));
	}
}
