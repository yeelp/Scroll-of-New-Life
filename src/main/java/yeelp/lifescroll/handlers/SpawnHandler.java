package yeelp.lifescroll.handlers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.lifescroll.capability.IScrollSpawn;
import yeelp.lifescroll.capability.ScrollSpawn;

public final class SpawnHandler extends AbstractHandler
{
	private static HashSet<UUID> scrollUsed = new HashSet<UUID>();
	private static HashMap<UUID, BlockPos> cachedSpawn = new HashMap<UUID, BlockPos>();
	public static void registerScrollUse(EntityPlayer player)
	{
		scrollUsed.add(player.getUniqueID());
	}
	
	@SubscribeEvent
	public void onSetSpawn(PlayerSetSpawnEvent evt)
	{
		EntityPlayer player = evt.getEntityPlayer();
		IScrollSpawn spawn = ScrollSpawn.get(player);
		if(shouldKeepScrollEnabled(player, spawn, evt.getNewSpawn()))
		{
			return;
		}
		else
		{
			spawn.setStatus(false);
			CapabilityHandler.syncSpawn(player);
		}	
	}
	
	@SubscribeEvent
	public void onDeath(LivingDeathEvent evt)
	{
		if(evt.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) evt.getEntityLiving();
			IScrollSpawn spawn = ScrollSpawn.get(player);
			Optional<BlockPos> newPos = IScrollSpawn.getScrollSpawnPos(player);
			if(newPos.isPresent())
			{
				cachedSpawn.put(player.getUniqueID(), newPos.get());
				player.setSpawnPoint(newPos.get(), true);
			}
		}
	}
	
	@SubscribeEvent
	public void onJoin(EntityJoinWorldEvent evt)
	{
		if(evt.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) evt.getEntity();
			IScrollSpawn spawn = ScrollSpawn.get(player);
			if(!cachedSpawn.containsKey(player.getUniqueID()))
			{
				Optional<BlockPos> pos = IScrollSpawn.getScrollSpawnPos(player);
				if(pos.isPresent())
				{
					cachedSpawn.put(player.getUniqueID(), pos.get());
				}
			}
		}
	}
	
	private static boolean shouldKeepScrollEnabled(EntityPlayer player, IScrollSpawn scroll, BlockPos spawn)
	{
		if(!player.world.isRemote)
		{
			return scrollUsed.remove(player.getUniqueID()) || 
				   player.getHealth() <= 0 ||
				   (scroll.enabled() && spawn.equals(cachedSpawn.get(player.getUniqueID())));
		}
		else
		{
			return false;
		}
	}
}
