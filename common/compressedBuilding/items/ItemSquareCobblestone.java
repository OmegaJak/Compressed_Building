package compressedBuilding.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import compressedBuilding.blocks.Blocks;

public class ItemSquareCobblestone extends ItemBlock{
	public ItemSquareCobblestone(int id) {
		super(id);
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			boolean sneaking = player.isSneaking();
			byte[] orientationArr = new byte[6];
			orientationArr[0] = -1;
			orientationArr[1] = 2;
			orientationArr[2] = -1;
			orientationArr[3] = 2;
			orientationArr[4] = 1;
			
			/**
			 * Used to determine which direction to orient it when the player is sneaking
			 * 1 means East-West
			 * 2 means North-South
			 */
			orientationArr[5] = 1;
			byte originalCount = 0;
			byte newCount = 0;
			
			if (side == 1) {//Top
				if (sneaking) {
					orientationArr[2] = 1;
					orientationArr[3] = 4;
					orientationArr[4] = 0;
					orientationArr[5] = 1;//This is the default and shouldn't need to be changed, but it's good to be safe if it doesn't make any significant impact on resources
				}else{
					orientationArr[4] = 1;
				}
			}else if (side == 0) {//Bottom
				if (sneaking) {
					orientationArr[2] = -3;
					orientationArr[3] = 0;
					orientationArr[4] = 0;
					orientationArr[5] = 1;
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
					orientationArr[0] = 1;
					orientationArr[1] = 4;
					orientationArr[4] = 0;
				}
			}else if (side == 2) {//The more negative z side
				if (sneaking) {
					orientationArr[0] = -3;
					orientationArr[1] = 0;
					orientationArr[4] = 0;
					orientationArr[5] = 2;
				}else{
					orientationArr[2] = -3;
					orientationArr[3] = 0;
					orientationArr[4] = 0;
				}
			}else if (side == 3) {//The more positive z side
				if (sneaking) {
					orientationArr[0] = 1;
					orientationArr[1] = 4;
					orientationArr[4] = 0;
					orientationArr[5] = 2;
				}else{
					orientationArr[2] = 1;
					orientationArr[3] = 4;
					orientationArr[4] = 0;
				}
			}else if (side == 4) {//The more negative x side
				if (sneaking) {
					orientationArr[0] = -3;
					orientationArr[1] = 0;
					orientationArr[4] = 0;
					orientationArr[5] = 1;
				}else{
					orientationArr[0] = -3;
					orientationArr[1] = 0;
					orientationArr[4] = 0;
				}
			}

			if (side == 0 || side == 1) {
				originalCount = 0;		
				for (int n = orientationArr[0]; n < orientationArr[1]; n++) {
					if(orientationArr[5] == 1) {
						if(!world.isAirBlock(x + n, y, z + orientationArr[4])){
							originalCount++;
						}
					}else{
						if(!world.isAirBlock(x + orientationArr[4], y, z + n)) {
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
			for (int i = orientationArr[0]; i < orientationArr[1]; i++) {
				for(int j = orientationArr[2]; j < orientationArr[3]; j++) {
					if (sneaking) {
						if (orientationArr[5] == 1) {
							if (world.isAirBlock(x + i, y + j, z + orientationArr[4]) || world.getBlockId(x + i, y + j, z + orientationArr[4]) == Blocks.squareCobble.blockID) {
								world.setBlock(x + i, y + j, z + orientationArr[4], Block.cobblestone.blockID);
							}
						}else{
							if (world.isAirBlock(x + orientationArr[4], y + j, z + i ) || world.getBlockId(x + orientationArr[4], y + j, z + i) == Blocks.squareCobble.blockID) {
								world.setBlock(x + orientationArr[4], y + j, z + i, Block.cobblestone.blockID);
							}
						}
					}else{
						if (world.isAirBlock(x + i, y + orientationArr[4], z + j) || world.getBlockId(x + i, y + orientationArr[4], z + j) == Blocks.squareCobble.blockID) {
							world.setBlock(x + i, y + orientationArr[4], z + j, Block.cobblestone.blockID);
						}
					}
				}
			}

		}	
		return false;
	}
}
