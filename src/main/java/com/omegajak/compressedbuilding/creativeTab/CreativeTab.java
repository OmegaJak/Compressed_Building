package warlockjk.compressedBuilding.creativeTab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import warlockjk.compressedBuilding.blocks.Blocks;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class CreativeTab extends CreativeTabs {
	
	public static ItemStack stack;
	
	public CreativeTab(String name) {
		super(name);
		LanguageRegistry.instance().addStringLocalization("itemGroup." + name, "en_US", name);
	}
	
	@Override
	public ItemStack getIconItemStack() {
		if (stack == null) {
			stack = new ItemStack(Blocks.squareTemplate);
		}
		return stack;
	}
	
}
