package com.omegajak.compressedbuilding.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.omegajak.compressedbuilding.utils.PlacementUtil;

public class ItemSquareTemplate extends ItemBlock{
	Block correspondingBlock;
	public ItemSquareTemplate(Block block) {
		super(block);
		this.correspondingBlock = block;
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		player.swingItem();
		if(!world.isRemote) {
			PlacementUtil placementUtil = new PlacementUtil();
			placementUtil.placeBlocks(player, world, x, y, z, side, correspondingBlock, 1, 1);
			if (!player.capabilities.isCreativeMode) {
				stack.stackSize--;
			}
		}
		world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, Block.soundTypeStone.getBreakSound(), 1.0F, Block.soundTypeStone.getPitch() * 0.8F);
		world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, Block.soundTypeStone.getBreakSound(), 1.0F, Block.soundTypeStone.getPitch() * 0.9F);
		return false;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.block;
    }
	
	@Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean useExtraInfo) {
		info.add("This places the block with ID " + itemStack.getItemDamage());
	}
}
