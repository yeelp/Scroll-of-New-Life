package yeelp.lifescroll.handlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.lifescroll.LifeScroll;
import yeelp.lifescroll.capability.IScrollSpawn;
import yeelp.lifescroll.capability.ScrollSpawn;
import yeelp.lifescroll.network.SpawnMessage;
import yeelp.lifescroll.util.InventoryUtil;

public class CapabilityHandler extends AbstractHandler
{
	@SubscribeEvent 
	public void onAddCaps(AttachCapabilitiesEvent<Entity> evt)
	{
		if (evt.getObject() instanceof EntityPlayer)
		{
			evt.addCapability(new ResourceLocation(LifeScroll.MODID,  "scrollspawn"), new ScrollSpawn());
		}
	}
	
	@SubscribeEvent 
	public void onDeath(Clone evt)
	{
		EntityPlayer oldPlayer = evt.getOriginal();
		EntityPlayer newPlayer = evt.getEntityPlayer();
		IScrollSpawn oldSpawn = ScrollSpawn.get(oldPlayer);
		IScrollSpawn newSpawn = ScrollSpawn.get(newPlayer);
		newSpawn.deserializeNBT(oldSpawn.serializeNBT());
		syncSpawn(newPlayer);
		if(evt.isWasDeath())
		{
			if(newPlayer.world.getGameRules().getBoolean("keepInventory"))
			{
				return;
			}
			else if(newPlayer instanceof FakePlayer)
			{
				return;
			}
			else
			{
				addScrolls(newPlayer, oldPlayer.inventory.mainInventory, false);
				addScrolls(newPlayer, oldPlayer.inventory.offHandInventory, true);
			}
		}
	}
	
	public static void syncSpawn(EntityPlayer player)
	{
		if(!player.world.isRemote)
		{
			PacketHandler.INSTANCE.sendTo(new SpawnMessage(ScrollSpawn.get(player)), (EntityPlayerMP) player);
		}
	}
	
	private static void addScrolls(EntityPlayer player, NonNullList<ItemStack> inv, boolean offHand)
	{
		for(Tuple<Integer, ItemStack> t : InventoryUtil.getScrollsFromInventory(inv))
		{
			InventoryUtil.addToInventory(player, t.getSecond(), t.getFirst(), offHand);
		}
	}
}
