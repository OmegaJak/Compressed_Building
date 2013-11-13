package warlockjk.compressedBuilding.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import warlockjk.compressedBuilding.utils.PlacementUtil;

public class ItemSquareCobblestone extends ItemBlock{
	public ItemSquareCobblestone(int id) {
		super(id);
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if(!world.isRemote) {
			PlacementUtil placementUtil = new PlacementUtil();	
			placementUtil.placeBlocks(stack, player, world, x, y, z, side, Block.cobblestone.blockID, 1, 1);
			if (!player.capabilities.isCreativeMode) {
				stack.stackSize--;
			}
		}
		return false;
	}
}
