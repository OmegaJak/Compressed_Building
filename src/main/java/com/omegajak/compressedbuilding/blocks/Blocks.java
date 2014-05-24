package com.omegajak.compressedbuilding.blocks;

import net.minecraft.block.Block;

import com.omegajak.compressedbuilding.items.ItemSquareTemplate;
import com.omegajak.compressedbuilding.lib.BlockInfo;
import com.omegajak.compressedbuilding.tileentities.TileEntityCompactor;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Blocks {
	public static final Block squareTemplate = new BlockSquareTemplate();
	public static final Block compactor = new BlockCompactor();
	
	public static void init() {
		GameRegistry.registerBlock(squareTemplate, ItemSquareTemplate.class, BlockInfo.SQTEMPLATE_KEY);
		GameRegistry.registerBlock(compactor, BlockInfo.COMPACTOR_KEY);
	}
	
	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityCompactor.class, BlockInfo.COMPACTOR_TE_KEY);
	}
}
