package com.omegajak.compressedbuilding.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import com.omegajak.compressedbuilding.CompressedBuilding;
import com.omegajak.compressedbuilding.lib.BlockInfo;
import com.omegajak.compressedbuilding.proxies.ClientProxy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSquareTemplate extends Block{

	public BlockSquareTemplate(int id) {
		super(id, Material.rock);
		setCreativeTab(CompressedBuilding.tabCompressedBuilding);
		setUnlocalizedName(BlockInfo.SQTEMPLATE_UNLOCALIZED_NAME);
		setBlockBounds(0F, .33F, 0F, 1F, .66F, 1F);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		blockIcon = register.registerIcon(BlockInfo.TEXTURE_LOCATION + ":" + BlockInfo.SQTEMPLATE_TEXTURE);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta) {
		return blockIcon;
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		if(!world.isRemote){
			world.setBlock(x, y, z, Block.cobblestone.blockID);
		}
	}
	
	@Override
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
	}
	
	@Override
	public boolean canRenderInPass(int pass) {
		//set the static variable in the clientProxy
		ClientProxy.renderPass = pass;
		
		//the block can render in both passes, so always return true
		return true;
	}
	
	@Override
	public int getRenderBlockPass() {
		return 1;
	}
}
