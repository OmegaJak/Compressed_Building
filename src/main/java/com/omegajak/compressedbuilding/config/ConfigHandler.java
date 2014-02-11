package warlockjk.compressedBuilding.config;

import java.io.File;

import net.minecraftforge.common.Configuration;
import warlockjk.compressedBuilding.lib.BlockInfo;

public class ConfigHandler {
	public static void init(File file) {
		Configuration config = new Configuration(file);
		
		config.load();
		
		BlockInfo.SQTEMPLATE_ID = config.getBlock(BlockInfo.SQTEMPLATE_KEY, BlockInfo.SQTEMPLATE_DEFAULT).getInt();
		BlockInfo.COMPACTOR_ID = config.getBlock(BlockInfo.COMPACTOR_KEY, BlockInfo.COMPACTOR_DEFAULT).getInt();
		
//		ItemInfo.WAND_ID = config.getItem(ItemInfo.WAND_KEY, ItemInfo.WAND_DEFAULT).getInt() - 256;
		
		config.save();
	}
}
