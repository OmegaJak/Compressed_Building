package com.omegajak.compressedbuilding.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.omegajak.compressedbuilding.utils.PlacementUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSquareTemplate extends ItemBlock{
	public ItemSquareTemplate(Block block) {
		super(block);
		super.setHasSubtypes(true);
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		player.swingItem();
		if (stack.getItemDamage() >>> 8 != 0) {
			if(!world.isRemote) {
				PlacementUtil placementUtil = new PlacementUtil();
				placementUtil.placeBlocks(stack, player, world, x, y, z, side, stack.getItemDamage(), 1, 1);
				if (!player.capabilities.isCreativeMode) {
					stack.stackSize--;
				}
			}
		}else{
			if(!world.isRemote) {
				PlacementUtil placementUtil = new PlacementUtil();
				placementUtil.placeBlocks(stack, player, world, x, y, z, side, 4 << 8, 1, 1);
				if (!player.capabilities.isCreativeMode) {
					stack.stackSize--;
				}
			}
		}
		world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, Block.getBlockById(stack.getItemDamage()).stepSound.getBreakSound(), 1.0F, Block.soundTypeStone.getPitch() * 0.8F);
		world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, Block.getBlockById(stack.getItemDamage()).stepSound.getBreakSound(), 1.0F, Block.soundTypeStone.getPitch() * 0.9F);
		return false;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.block;
    }
	
	@Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean useExtraInfo) {
		info.add("This item holds the block with Id " + (itemStack.getItemDamageForDisplay() >>> 8) + " and damage of " + (itemStack.getItemDamageForDisplay() & 0xFF));
	}
}
