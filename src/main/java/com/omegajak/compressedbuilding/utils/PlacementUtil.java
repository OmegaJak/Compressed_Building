package com.omegajak.compressedbuilding.utils;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.omegajak.compressedbuilding.blocks.Blocks;
import com.omegajak.compressedbuilding.config.ConfigSettings;

public class PlacementUtil {
	
	private boolean sneaking = false;
	private byte[] orientationArr = new byte[7];
	private byte originalCount = 0;
	private byte newCount = 0;
//	private byte radius = 1;
//	private byte upCount = 0;
//	private byte downCount = 0;
	
	public PlacementUtil() {
	}
	
	/**
	 * @param id The id of the block to be placed
	 * @param sizeFactor1 Modifies the size of what's placed, 1 would keep it at the default 3 TODO Implement this
	 * @param sizeFactor2 Modifies the size of what's placed, 1 would keep it at the default 3 TODO Implement this
	 */
	public void placeBlocks(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, int id, double sizeFactor1, double sizeFactor2) {
		if (!world.isRemote) {
			sneaking = player.isSneaking();
			if (!ConfigSettings.SHIFT_VERTICAL) {//so it pays attention to the config setting
				sneaking = !sneaking;
			}
			orientationArr[0] = -1;//Starting of i
			orientationArr[1] = 2;//One greater than the ending of i
			orientationArr[2] = -1;//Starting of k
			orientationArr[3] = 2;//One greater than the ending of k
			orientationArr[4] = 1;//The offset of various directions depending on where it is used below
			
			/**
			 * Used to determine which direction to orient it when the player is sneaking and placing on the top or bottom
			 * 1 means East-West
			 * 2 means North-South
			 */
			orientationArr[5] = 1;
			
			/**
			 * This is used similarly to the 5th index, except it's for when you're placing on the side
			 * It's determined by orientationLogic
			 * 1 means vertical
			 * 2 means horizontal
			 */
			orientationArr[6] = 1;
			
			orientationLogic(world, x, y, z, side);//Changes the east-west vs north-south orientation
			sideBasedLogic(side);//Manipulates where the loops below start and end for proper orientation based on the side
			orientationLogic(world, x, y, z, side);//Changes the east-west vs north-south orientation
			if(!world.getBlock(x, y, z).getMaterial().isSolid()) {
				//nonSolidLogic(side);//Makes things work as you would think they would with grass and such
				orientationArr[0] = -1;
				orientationArr[1] = 2;
				orientationArr[2] = -1;
				orientationArr[3] = 2;
				orientationArr[4] = 0;
			}
/**			if(sneaking) {
				verticalDisplacementLogic(world, x, y, z);
			}*/
			for (int i = orientationArr[0]; i < orientationArr[1]; i++) {
				for(int j = orientationArr[2]; j < orientationArr[3]; j++) {
					if ((sneaking || side > 1) && orientationArr[6] == 1) {
						if (orientationArr[5] == 1) {
							if (world.isAirBlock(x + i, y + j, z + orientationArr[4]) || !world.getBlock(x + i, y + j, z + orientationArr[4]).getMaterial().isSolid() || Block.getIdFromBlock(world.getBlock(x + i, y + j, z + orientationArr[4])) == Block.getIdFromBlock(Blocks.squareTemplate)) {
								world.setBlock(x + i, y + j, z + orientationArr[4], Block.getBlockById(id >>> 8));
								world.setBlockMetadataWithNotify(x + i, y + j, z + orientationArr[4], 0xFF & id, 2);
							}else{
								spawnCompensation(world, id, 1, x, y, z, player);
							}
						}else{
							if (world.isAirBlock(x + orientationArr[4], y + j, z + i) || !world.getBlock(x + orientationArr[4], y + j, z + i).getMaterial().isSolid() || Block.getIdFromBlock(world.getBlock(x + orientationArr[4], y + j, z + i)) == Block.getIdFromBlock(Blocks.squareTemplate)) {
								world.setBlock(x + orientationArr[4], y + j, z + i, Block.getBlockById(id >>> 8));
								world.setBlockMetadataWithNotify(x + orientationArr[4], y + j, z + i, 0xFF & id, 2);
							}else{
								spawnCompensation(world, id, 1, x, y, z, player);
							}
						}
					}else{
						if (world.isAirBlock(x + i, y + orientationArr[4], z + j) || !world.getBlock(x + i, y + orientationArr[4], z + j).getMaterial().isSolid() || Block.getIdFromBlock(world.getBlock(x + i, y + orientationArr[4], z + j)) == Block.getIdFromBlock(Blocks.squareTemplate)) {
							world.setBlock(x + i, y + orientationArr[4], z + j, Block.getBlockById(id >>> 8));
							world.setBlockMetadataWithNotify(x + i, y + orientationArr[4], z + j, 0xFF & id, 2);
						}else{
							spawnCompensation(world, id, 1, x, y, z, player);
						}
					}
				}
			}
		}
	}
	
	private void sideBasedLogic(int side) {
		if (side == 1) {//Top
			if (sneaking) {
				orientationArr[2] = 1;
				orientationArr[3] = 4;
				orientationArr[4] = 0;
				orientationArr[5] = 1;//This is the default and shouldn't need to be changed, but it's good to be safe if it doesn't make any significant impact on resources
				orientationArr[6] = 1;
			}else{
				orientationArr[4] = 1;
			}
		}else if (side == 0) {//Bottom
			if (sneaking) {
				orientationArr[2] = -3;
				orientationArr[3] = 0;
				orientationArr[4] = 0;
				orientationArr[5] = 1;
				orientationArr[6] = 1;
			}else{
				orientationArr[4] = -1;
			}
		}else if (side == 5) {//The more positive x side
			if (sneaking) {
				orientationArr[0] = 1;
				orientationArr[1] = 4;
				orientationArr[4] = 0;
				orientationArr[5] = 1;
			}else{
				orientationArr[4] = 1;
				orientationArr[5] = 2;
				orientationArr[6] = 1;
			}
		}else if (side == 2) {//The more negative z side
			if (sneaking) {
				if (orientationArr[6] == 1) {
					orientationArr[0] = -3;
					orientationArr[1] = 0;
				}else{
					orientationArr[2] = -3;
					orientationArr[3] = 0;
				}
				orientationArr[4] = 0;
				orientationArr[5] = 2;
				
			}else{
				orientationArr[4] = -1;
				orientationArr[5] = 1;
				orientationArr[6] = 1;
			}
		}else if (side == 3) {//The more positive z side
			if (sneaking) {
				if (orientationArr[6] == 1) {
					orientationArr[0] = 1;
					orientationArr[1] = 4;
				}else{
					orientationArr[2] = 1;
					orientationArr[3] = 4;
				}
				orientationArr[4] = 0;
				orientationArr[5] = 2;
			}else{
				orientationArr[4] = 1;
				orientationArr[5] = 1;
				orientationArr[6] = 1;
			}
		}else if (side == 4) {//The more negative x side
			if (sneaking) {
				orientationArr[0] = -3;
				orientationArr[1] = 0;
				orientationArr[4] = 0;
				orientationArr[5] = 1;
			}else{
				orientationArr[4] = -1;
				orientationArr[5] = 2;
				orientationArr[6] = 1;
			}
		}
	}
	
	public void spawnCompensation(World world, int id, int count, double x, double y, double z, EntityPlayer player) {
		EntityItem entityItem = new EntityItem(world, x, y, z, new ItemStack(Item.getItemById(id >>> 8), count, 0xFF & id));
		double distance = getDistance(x, y, z, player.posX, player.posY, player.posZ);
		entityItem.motionX = (double)((player.posX - x) * distance * 0.01D);
		entityItem.motionY = (double)((player.posY - y) * distance * 0.01D);
		entityItem.motionZ = (double)((player.posZ - z) * distance * 0.01D);
		world.spawnEntityInWorld(entityItem);
	}
	
	public double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
		return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1);
	}
	
	private void orientationLogic(World world,int x,int y,int z, int side) {
		if (side == 0 || side == 1) {
			originalCount = 0;
			for (int n = orientationArr[0]; n < orientationArr[1]; n++) {
				if(orientationArr[5] == 1) {
					if(!(world.isAirBlock(x + n, y, z + orientationArr[4]) || !world.getBlock(x + n, y, z + orientationArr[4]).getMaterial().isSolid())){
						originalCount++;
					}
				}else{
					if(!(world.isAirBlock(x + orientationArr[4], y, z + n) || !world.getBlock(x + orientationArr[4], y, z + n).getMaterial().isSolid())) {
						originalCount++;
					}
				}
			}
			newCount = 0;
			for (int n = orientationArr[0]; n < orientationArr[1]; n++) {
				if(orientationArr[5] == 1) {//It might look like this is the same as above, but I switched the if statements so it does the opposite
					if(!world.isAirBlock(x + orientationArr[4], y, z + n)) {
						newCount++;
					}
				}else{
					if(!world.isAirBlock(x + n, y, z + orientationArr[4])){
						newCount++;
					}
				}
			}
			if (newCount > originalCount) {
				if (orientationArr[5] == 1) {
					orientationArr[5] = 2;
				}else{
					orientationArr[5] = 1;
				}
			}
		}
	}	
}