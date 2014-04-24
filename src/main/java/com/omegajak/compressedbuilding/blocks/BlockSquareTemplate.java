package com.omegajak.compressedbuilding.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.omegajak.compressedbuilding.CompressedBuilding;
import com.omegajak.compressedbuilding.lib.BlockInfo;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSquareTemplate extends Block{

	public BlockSquareTemplate() {
		super(Material.rock);
		setCreativeTab(CompressedBuilding.tabCompressedBuilding);
//		setUnlocalizedName(BlockInfo.SQTEMPLATE_UNLOCALIZED_NAME);
		setBlockBounds(0F, .33F, 0F, 1F, .66F, 1F);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		blockIcon = register.registerIcon(BlockInfo.TEXTURE_LOCATION + ":" + BlockInfo.SQTEMPLATE_TEXTURE);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return blockIcon;
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		if(!world.isRemote){
			world.setBlock(x, y, z, Block.getBlockFromName("cobblestone"));
		}
	}
	
/**	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean isBlockNormalCube(World world, int x, int y, int z) {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return BlockInfo.SQTEMPLATE_RENDER_ID;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}*/
}
