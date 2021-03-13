package yeelp.lifescroll.util;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import yeelp.lifescroll.item.ItemSpawnScroll;

public class InventoryUtil
{
	public static boolean addToInventory(EntityPlayer player, ItemStack stack, int preferredSlot, boolean offHand)
	{
		if(stack.isEmpty() || player == null)
		{
			return false;
		}
		else if(stack.getItem() instanceof ItemArmor)
		{
			ItemArmor armor = (ItemArmor) stack.getItem();
			int slot = armor.armorType.getIndex();
			NonNullList<ItemStack> armorInv = player.inventory.armorInventory;
			return placeIfEmpty(armorInv, stack, slot);
		}
		else if(offHand)
		{
			return placeIfEmpty(player.inventory.offHandInventory, stack, 0);
		}
		else
		{
			
			NonNullList<ItemStack> inv = player.inventory.mainInventory;
			if(!placeIfEmpty(inv, stack, preferredSlot))
			{
				int res, i;
				for(res = 0, i = 0; i < inv.size() && res == 0; res = toInt(placeIfEmpty(inv, stack, i++)));
				return res > 0;
			}
			else
			{
				return true;
			}
		}
	}
	
	public static List<Tuple<Integer, ItemStack>> getScrollsFromInventory(List<ItemStack> lst)
	{
		return getFromListIf(lst, (stack) -> stack.getItem() instanceof ItemSpawnScroll);
	}
	
	public static <T> List<Tuple<Integer, T>> getFromListIf(List<T> lst, Predicate<T> pred)
	{
		LinkedList<Tuple<Integer, T>> results = new LinkedList<Tuple<Integer, T>>();
		for(int i = 0; i < lst.size(); i++)
		{
			T t = lst.get(i);
			if(pred.test(t))
			{
				results.add(new Tuple<Integer, T>(i, t));
			}
		}
		return results;
	}
	
	private static boolean placeIfEmpty(List<ItemStack> lst, ItemStack stack, int index)
	{
		return placeIf(lst, stack, index, (s) -> s.isEmpty());
	}
	
	private static <T> boolean placeIf(List<T> lst, T item, int index, Predicate<T> pred)
	{
		if(pred.test(lst.get(index)))
		{
			lst.set(index, item);
			return true;
		}
		return false;
	}
	
	private static int toInt(boolean b)
	{
		return b ? 1 : 0;
	}
}
