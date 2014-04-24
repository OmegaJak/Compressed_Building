package com.omegajak.compressedbuilding.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.omegajak.compressedbuilding.blocks.Blocks;

import cpw.mods.fml.common.registry.GameRegistry;

public class Items {
	
	public static void init() {
		
	}
	
	public static void registerRecipes() {
		GameRegistry.addRecipe(new ItemStack(Blocks.compactor), new Object[] { "XYX", "Y Y", "XYX", 'X', Block.getBlockFromName("cobblestone"), 'Y', Block.getBlockFromName("piston")});
		
		ItemStack squareTemplate = new ItemStack(Blocks.squareTemplate);
//		GameRegistry.addShapelessRecipe(new ItemStack(Block.getBlockById(squareTemplate.getItemDamage() >>> 8), 9), squareTemplate);//no way this is going to work
	}
	
}
