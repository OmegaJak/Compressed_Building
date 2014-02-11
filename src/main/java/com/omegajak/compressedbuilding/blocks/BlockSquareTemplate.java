package warlockjk.compressedBuilding.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import warlockjk.compressedBuilding.CompressedBuilding;
import warlockjk.compressedBuilding.lib.BlockInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSquareTemplate extends Block{

	public BlockSquareTemplate(int id) {
		super(id, Material.rock);
		setCreativeTab(CompressedBuilding.tabCompressedBuilding);
		setUnlocalizedName(BlockInfo.SQTEMPLATE_UNLOCALIZED_NAME);
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
	
	

}
