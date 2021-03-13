package yeelp.lifescroll.capability;

import java.util.Optional;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import yeelp.lifescroll.ModConfig;

public interface IScrollSpawn extends ICapabilitySerializable<NBTTagCompound>
{
	BlockPos getSpawnLocation(World world, int tries);
	
	BlockPos getExactPos();
	
	void setSpawn(BlockPos pos);
	
	boolean enabled();
	
	void setStatus(boolean status);
	
	@Nonnull
	public static Optional<BlockPos> getScrollSpawnPos(EntityPlayer player)
	{
		IScrollSpawn instance = ScrollSpawn.get(player);
		if(instance.enabled())
		{
			return Optional.ofNullable(instance.getSpawnLocation(player.world, ModConfig.tries));
		}
		else
		{
			return Optional.empty();
		}
	}
}
