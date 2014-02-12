package com.omegajak.compressedbuilding.blocks;

import javax.swing.Icon;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.omegajak.compressedbuilding.CompressedBuilding;
import com.omegajak.compressedbuilding.lib.BlockInfo;
import com.omegajak.compressedbuilding.tileentities.TileEntityCompactor;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCompactor extends BlockContainer {
	public BlockCompactor(int id) {
		super(Material.rock);
		
		setCreativeTab(com.omegajak.compressedbuilding.CompressedBuilding.tabCompressedBuilding);
		setHardness(2F);
		setStepSound(Block.soundTypeStone);
//		setUnlocalizedName(BlockInfo.COMPACTOR_UNLOCALIZED_NAME);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int par2) {
		return new TileEntityCompactor();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		blockIcon = register.registerIcon(BlockInfo.TEXTURE_LOCATION + ":" + BlockInfo.COMPACTOR_TEXTURE);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return blockIcon;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float clickX, float clickY, float clickZ) {
        if (player.isSneaking()) return false;
        if (!world.isRemote) {
            FMLNetworkHandler.openGui(player, CompressedBuilding.instance, 0, world, x, y, z);
        }
        return true;
	}
}
