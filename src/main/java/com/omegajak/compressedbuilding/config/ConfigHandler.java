package com.omegajak.compressedbuilding.config;

import java.io.File;

import net.minecraftforge.common.Configuration;

import com.omegajak.compressedbuilding.lib.BlockInfo;

public class ConfigHandler {
	public static void init(File file) {
		Configuration config = new Configuration(file);
		
		config.load();
		
		BlockInfo.SQTEMPLATE_ID = config.getBlock(BlockInfo.SQTEMPLATE_KEY, BlockInfo.SQTEMPLATE_DEFAULT).getInt();
		BlockInfo.COMPACTOR_ID = config.getBlock(BlockInfo.COMPACTOR_KEY, BlockInfo.COMPACTOR_DEFAULT).getInt();
		
		config.get(ConfigSettings.GENERAL_SETTINGS_CATEGORY, ConfigSettings.SHIFT_VERTICAL_KEY, ConfigSettings.SHIFT_VERTICAL_DEFAULT).comment = ConfigSettings.SHIFT_VERTICAL_COMMENT;
		ConfigSettings.SHIFT_VERTICAL = config.get(ConfigSettings.GENERAL_SETTINGS_CATEGORY, ConfigSettings.SHIFT_VERTICAL_KEY, ConfigSettings.SHIFT_VERTICAL_DEFAULT).getBoolean(ConfigSettings.SHIFT_VERTICAL_DEFAULT);
		
//		ItemInfo.WAND_ID = config.getItem(ItemInfo.WAND_KEY, ItemInfo.WAND_DEFAULT).getInt() - 256;
		
		config.save();
	}
}
