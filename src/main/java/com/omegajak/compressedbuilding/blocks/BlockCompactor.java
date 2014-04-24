package com.omegajak.compressedbuilding.blocks;

import java.util.Random;

import javax.swing.Icon;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
	public BlockCompactor() {
		super(Material.rock);
		
		setCreativeTab(CompressedBuilding.tabCompressedBuilding);
		setHardness(2F);
		setStepSound(Block.soundTypeStone);
//		setUnlocalizedName(BlockInfo.COMPACTOR_UNLOCALIZED_NAME);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int par2) {
		return new TileEntityCompactor();
	}
	
	@SideOnly(Side.CLIENT)
	private IIcon topIcon;
	@SideOnly(Side.CLIENT)
	private IIcon otherIcons;
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		topIcon = register.registerIcon(BlockInfo.TEXTURE_LOCATION + ":" + BlockInfo.COMPACTOR_TEXTURE_TOP);
		otherIcons = register.registerIcon(BlockInfo.TEXTURE_LOCATION + ":" + BlockInfo.COMPACTOR_TEXTURE_SIDES);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (side == 1) {
			return topIcon;
		}else{
			return otherIcons;
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float clickX, float clickY, float clickZ) {
        if (player.isSneaking()) return false;
        if (!world.isRemote) {
            FMLNetworkHandler.openGui(player, CompressedBuilding.instance, 0, world, x, y, z);
        }
        return true;
	}
	
	//This method comes straight from the BlockChest class in vanilla minecraft
	@Override
    public void breakBlock(World world, int x, int y, int z, Block oldBlock, int oldMeta)
    {
        TileEntityCompactor tileEntityCompactor = (TileEntityCompactor)world.getTileEntity(x, y, z);
        final Random random = new Random();
        if (tileEntityCompactor != null)
        {
            for (int j1 = 0; j1 < tileEntityCompactor.getSizeInventory(); j1++)
            {
                ItemStack itemstack = tileEntityCompactor.getStackInSlot(j1);

                if (itemstack != null)
                {
                    float f = random.nextFloat() * 0.8F + 0.1F;
                    float f1 = random.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for (float f2 = random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world.spawnEntityInWorld(entityitem))
                    {
                        int k1 = random.nextInt(21) + 10;

                        if (k1 > itemstack.stackSize)
                        {
                            k1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= k1;
                        entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)random.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)random.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)random.nextGaussian() * f3);

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }
                    }
                }
            }

            world.func_147453_f(x, y, z, oldBlock);
        }

        super.breakBlock(world, x, y, z, oldBlock, oldMeta);
    }
}
