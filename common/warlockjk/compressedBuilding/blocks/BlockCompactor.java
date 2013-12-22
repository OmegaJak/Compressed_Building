package warlockjk.compressedBuilding.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import warlockjk.compressedBuilding.CompressedBuilding;
import warlockjk.compressedBuilding.lib.BlockInfo;
import warlockjk.compressedBuilding.tileentities.TileEntityCompactor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCompactor extends BlockContainer {
	public BlockCompactor(int id) {
		super(id, Material.rock);
		
		setCreativeTab(CompressedBuilding.tabCompressedBuilding);
		setStepSound(Block.soundStoneFootstep);
		setUnlocalizedName(BlockInfo.COMPACTOR_UNLOCALIZED_NAME);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityCompactor();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		blockIcon = register.registerIcon(BlockInfo.TEXTURE_LOCATION + ":" + BlockInfo.COMPACTOR_TEXTURE);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta) {
		return blockIcon;
	}
}
