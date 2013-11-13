package warlockjk.compressedBuilding.config;

import java.io.File;

import net.minecraftforge.common.Configuration;
import warlockjk.compressedBuilding.lib.BlockInfo;

public class ConfigHandler {
	public static void init(File file) {
		Configuration config = new Configuration(file);
		
		config.load();
		
		BlockInfo.SQCOBBLE_ID = config.getBlock(BlockInfo.SQCOBBLE_KEY, BlockInfo.SQCOBBLE_DEFAULT).getInt();
		
//		ItemInfo.WAND_ID = config.getItem(ItemInfo.WAND_KEY, ItemInfo.WAND_DEFAULT).getInt() - 256;
		
		config.save();
	}
}
