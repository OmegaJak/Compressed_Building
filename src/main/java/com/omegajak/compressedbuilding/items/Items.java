package com.omegajak.compressedbuilding.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.omegajak.compressedbuilding.blocks.Blocks;

import cpw.mods.fml.common.registry.GameRegistry;

public class Items {
	
	public static void init() {
		
	}
	
	public static void registerRecipes() {
		GameRegistry.addRecipe(new ItemStack(Blocks.compactor), new Object[] { "XYX", "Y Y", "XYX", 'X', Block.cobblestone, 'Y', Block.pistonBase});
		GameRegistry.addShapelessRecipe(new ItemStack(Block.cobblestone, 9), new ItemStack(Blocks.squareTemplate));
	}
	
}
