package warlockjk.compressedBuilding.blocks;

import net.minecraft.block.Block;
import warlockjk.compressedBuilding.items.ItemSquareCobblestone;
import warlockjk.compressedBuilding.lib.BlockInfo;
import warlockjk.compressedBuilding.tileentities.TileEntityCompactor;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Blocks {
	public static Block squareCobble;
	public static Block compactor;
	
	public static void init() {
		squareCobble = new BlockSquareCobblestone(BlockInfo.SQCOBBLE_ID);
		GameRegistry.registerBlock(squareCobble, ItemSquareCobblestone.class, BlockInfo.SQCOBBLE_KEY);
		compactor = new BlockCompactor(BlockInfo.COMPACTOR_ID);
		GameRegistry.registerBlock(compactor, BlockInfo.COMPACTOR_KEY);
	}
	
	public static void addNames() {
		LanguageRegistry.addName(squareCobble, BlockInfo.SQCOBBLE_NAME);
		LanguageRegistry.addName(compactor, BlockInfo.COMPACTOR_NAME);
	}
	
	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityCompactor.class, BlockInfo.COMPACTOR_TE_KEY);
	}
}
