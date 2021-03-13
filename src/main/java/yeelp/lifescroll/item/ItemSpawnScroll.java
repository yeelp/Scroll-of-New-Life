package yeelp.lifescroll.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import yeelp.lifescroll.LifeScroll;
import yeelp.lifescroll.capability.IScrollSpawn;
import yeelp.lifescroll.capability.ScrollSpawn;
import yeelp.lifescroll.handlers.CapabilityHandler;
import yeelp.lifescroll.handlers.SpawnHandler;

public class ItemSpawnScroll extends Item
{
	private static final ITextComponent usable = new TextComponentTranslation("tooltips.lifescroll.spawnscroll.usable").setStyle(new Style().setColor(TextFormatting.DARK_GREEN));
	private static final ITextComponent unusable = new TextComponentTranslation("tooltips.lifescroll.spawnscroll.unusable").setStyle(new Style().setColor(TextFormatting.RED));
	private static final ITextComponent fail = new TextComponentTranslation("messages.lifescroll.spawnscroll.fail").setStyle(new Style().setColor(TextFormatting.RED));
	public ItemSpawnScroll()
	{
		super();
		this.setRegistryName("spawnscroll");
		this.setUnlocalizedName(LifeScroll.MODID + ".spawnscroll");
		this.setCreativeTab(CreativeTabs.MISC);
		this.setMaxStackSize(1);
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
	{
		if(world == null)
		{
			return;
		}
		if(world.provider.getDimensionType() == DimensionType.OVERWORLD)
		{
			tooltip.add(usable.getFormattedText());
		}
		else
		{
			tooltip.add(unusable.getFormattedText());
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		if(!world.isRemote)
		{
			if(player.dimension == 0)
			{
				SpawnHandler.registerScrollUse(player);
				BlockPos pos = player.getPosition();
				player.setSpawnPoint(pos, true);	
				IScrollSpawn spawn = ScrollSpawn.get(player);
				spawn.setSpawn(pos);
				spawn.setStatus(true);
				CapabilityHandler.syncSpawn(player);
				if(!player.capabilities.isCreativeMode)
				{
					stack.setCount(Math.max(0, stack.getCount() - 1));
				}
				player.sendMessage(new TextComponentTranslation("messages.lifescroll.spawnscroll.success", pos.getX(), pos.getY(), pos.getZ()).setStyle(new Style().setColor(TextFormatting.GREEN)));
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
			else
			{
				player.sendMessage(fail);
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
	}
}
