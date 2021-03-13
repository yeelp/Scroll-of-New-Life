package yeelp.lifescroll.network;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.lifescroll.capability.IScrollSpawn;
import yeelp.lifescroll.capability.ScrollSpawn;

public class SpawnMessage implements IMessage
{
	private NBTTagCompound nbt;
	public SpawnMessage()
	{
		this.nbt = new NBTTagCompound();
	}
	
	public SpawnMessage(IScrollSpawn spawn)
	{
		this.nbt = spawn.serializeNBT().copy();
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		try
		{
			nbt = new PacketBuffer(buf).readCompoundTag();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		new PacketBuffer(buf).writeCompoundTag(nbt);
	}
	
	public static final class Handler implements IMessageHandler<SpawnMessage, IMessage>
	{
		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(SpawnMessage message, MessageContext ctx) 
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}
		
		@SideOnly(Side.CLIENT)
		public void handle(SpawnMessage msg, MessageContext ctx)
		{
			EntityPlayer player = NetworkHelper.getSidedPlayer(ctx);
			ScrollSpawn.get(player).deserializeNBT(msg.nbt);
		}
	}
}
