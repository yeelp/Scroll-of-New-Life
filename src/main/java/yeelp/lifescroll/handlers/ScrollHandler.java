package yeelp.lifescroll.handlers;

import java.util.ListIterator;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import yeelp.lifescroll.item.ItemSpawnScroll;
import yeelp.lifescroll.item.LifeScrollItems;
import yeelp.lifescroll.util.InventoryUtil;

public class ScrollHandler extends AbstractHandler
{
	
	private static final String KEY = "lifescroll.firstjoin";
	
	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent evt)
	{
		EntityPlayer player = evt.player;
		NBTTagCompound data = player.getEntityData();
		NBTTagCompound tag;
		if(data.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
		{
			tag = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		}
		else
		{
			data.setTag(EntityPlayer.PERSISTED_NBT_TAG, tag = new NBTTagCompound());
		}
		if(!tag.hasKey(KEY))
		{
			tag.setBoolean(KEY, true);
			ItemStack stack = new ItemStack(LifeScrollItems.spawnScroll);
			if(!player.addItemStackToInventory(stack))
			{
				EntityItem entityItem = new EntityItem(player.world, player.posX + 0.5f, player.posY + 0.5f, player.posZ + 0.5f, stack);
				player.world.spawnEntity(entityItem);
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerDrops(PlayerDropsEvent evt)
	{
		EntityPlayer player = evt.getEntityPlayer();
		if(player instanceof FakePlayer)
		{
			return;
		}
		else if(player.world.getGameRules().getBoolean("keepInventory"))
		{
			return;
		}
		else
		{
			ListIterator<EntityItem> it = evt.getDrops().listIterator();
			while(it.hasNext())
			{
				ItemStack stack = it.next().getItem();
				if(stack.getItem() instanceof ItemSpawnScroll)
				{
					if(InventoryUtil.addToInventory(player, stack, 0, false))
					{
						it.remove();
					}
				}
			}
		}
	}
}
