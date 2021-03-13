package yeelp.lifescroll.capability;

import java.util.HashSet;
import java.util.concurrent.Callable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ScrollSpawn implements IScrollSpawn
{
	private BlockPos pos;
	private boolean status;
	
	@CapabilityInject(IScrollSpawn.class)
	public static Capability<IScrollSpawn> cap = null;
	
	//private static IScrollSpawn instance = cap.getDefaultInstance();
	
	public static IScrollSpawn get(EntityPlayer player)
	{
		return player.getCapability(cap, null);
	}
	
	public ScrollSpawn()
	{
		this(0, 0, 0);
	}
	
	public ScrollSpawn(int x, int y, int z)
	{
		this.pos = new BlockPos(x, y, z);
		this.status = false;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == cap;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == cap ? cap.<T> cast(this) : null;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setIntArray("pos", new int[] {this.pos.getX(), this.pos.getY(), this.pos.getZ()});
		tag.setBoolean("status", status);
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		int[] pos = nbt.getIntArray("pos");
		this.pos = new BlockPos(pos[0], pos[1], pos[2]);
		this.status = nbt.getBoolean("status");
	}

	@Override
	public BlockPos getSpawnLocation(World world, int tries)
	{
		HashSet<Long> checked = new HashSet<Long>();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        
        //start from the spawn location and spiral around to check points along a spiral a specified number of times. We check  y +/- 1 from the y position of the spawn
        for (int i = 0; i <= tries; i++)
        {
        	Point p = spiral(i);
            for(int l = 0; l <= 6; l+=3)
            {
            	int yPos = (int) (y + Math.signum(l*(l-4))); //cheeky way to check the BlockPos above and below without using if statements!
            	BlockPos blockpos = new BlockPos(x+p.getX(), yPos, z+p.getZ());
            	if (hasRoomForPlayer(world, blockpos))
		        {
		            return blockpos;
		        }
            }
        }
        return null;
	}
	
	@Override
	public BlockPos getExactPos()
	{
		return this.pos;
	}

	@Override
	public void setSpawn(BlockPos pos)
	{
		this.pos = pos;
	}


	@Override
	public boolean enabled()
	{
		return this.status;
	}


	@Override
	public void setStatus(boolean status)
	{
		this.status = status;
	}
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IScrollSpawn.class, new SpawnStorage(), new SpawnFactory());
	}
	
	//Adapted from BlockBed
	private static boolean hasRoomForPlayer(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, EnumFacing.UP) && !worldIn.getBlockState(pos).getMaterial().isSolid() && !worldIn.getBlockState(pos.up()).getMaterial().isSolid();
    }
	
	/*
	 * Adapted from an answer from https://math.stackexchange.com/questions/163080/on-a-two-dimensional-grid-is-there-a-formula-i-can-use-to-spiral-coordinates-in
	 * This efficiently enumerates points outwards from the origin.
	 * This is actually a bijection from Z (The integers) to Z x Z (2D integer coordinates!), however the inverse function isn't needed, although it does exist.
	 * Thus, it takes some integer and returns a unique Tuple of integers for x and z coordinates.
	 * I was trying to find a linear transformation using trig functions and polar coordinates but that turned out to be unnecessary
	 */
	private static Point spiral(int n)
	{
		n++;
		int k = (int) Math.ceil((Math.sqrt(n) - 1)/2.0);
		int t = 2*k + 1;
		int m = t*t;
		t -= 1;
		if(n >= m-t)
		{
			return new Point(k-(m-n), -k);
		}
		m -= t;
		if(n >= m-t)
		{
			return new Point(-k, -k+(m-n));
		}
		m -= t;
		if(n >= m-t)
		{
			return new Point(-k+(m-n), k);
		}
		else
		{
			return new Point(k, k-(m-n-t));
		}
	}
	
	private static class SpawnStorage implements IStorage<IScrollSpawn>
	{
		@Override
		public NBTBase writeNBT(Capability<IScrollSpawn> capability, IScrollSpawn instance, EnumFacing side)
		{
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<IScrollSpawn> capability, IScrollSpawn instance, EnumFacing side, NBTBase nbt)
		{
			instance.deserializeNBT((NBTTagCompound) nbt);
		}	
	}
	
	private static class SpawnFactory implements Callable<IScrollSpawn>
	{
		@Override
		public IScrollSpawn call() throws Exception
		{
			return new ScrollSpawn();
		}	
	}
	
	private static class Point extends Tuple<Integer, Integer>
	{
		public Point(int aIn, int bIn)
		{
			super(aIn, bIn);
		}
		
		public int getX()
		{
			return super.getFirst();
		}
		
		public int getZ()
		{
			return super.getSecond();
		}
	}

}
