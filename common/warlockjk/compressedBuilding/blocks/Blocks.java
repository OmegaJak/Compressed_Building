package warlockjk.compressedBuilding.blocks;

import net.minecraft.block.Block;
import warlockjk.compressedBuilding.items.ItemSquareCobblestone;
import warlockjk.compressedBuilding.lib.BlockInfo;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Blocks {
	public static Block squareCobble;
	
	public static void init() {
		squareCobble = new BlockSquareCobblestone(BlockInfo.SQCOBBLE_ID);
		GameRegistry.registerBlock(squareCobble, ItemSquareCobblestone.class, BlockInfo.SQCOBBLE_KEY);
	}
	
	public static void addNames() {
		LanguageRegistry.addName(squareCobble, BlockInfo.SQCOBBLE_NAME);
	}
}
