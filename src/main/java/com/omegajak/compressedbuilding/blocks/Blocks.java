package warlockjk.compressedBuilding.blocks;

import net.minecraft.block.Block;
import warlockjk.compressedBuilding.items.ItemSquareTemplate;
import warlockjk.compressedBuilding.lib.BlockInfo;
import warlockjk.compressedBuilding.tileentities.TileEntityCompactor;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Blocks {
	public static Block squareTemplate;
	public static Block compactor;
	
	public static void init() {
		squareTemplate = new BlockSquareTemplate(BlockInfo.SQTEMPLATE_ID);
		GameRegistry.registerBlock(squareTemplate, ItemSquareTemplate.class, BlockInfo.SQTEMPLATE_KEY);
		compactor = new BlockCompactor(BlockInfo.COMPACTOR_ID);
		GameRegistry.registerBlock(compactor, BlockInfo.COMPACTOR_KEY);
	}
	
	public static void addNames() {
		LanguageRegistry.addName(squareTemplate, BlockInfo.SQTEMPLATE_NAME);
		LanguageRegistry.addName(compactor, BlockInfo.COMPACTOR_NAME);
	}
	
	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityCompactor.class, BlockInfo.COMPACTOR_TE_KEY);
	}
}