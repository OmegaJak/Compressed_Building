package compressedBuilding.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import compressedBuilding.blocks.Blocks;

import cpw.mods.fml.common.registry.GameRegistry;

public class Items {
	
	public static void init() {
		
	}
	
	public static void registerRecipes() {
		GameRegistry.addRecipe(new ItemStack(Blocks.squareCobble), new Object[] { "XXX", "XXX", "XXX", 'X', Block.cobblestone});
	}
	
}
