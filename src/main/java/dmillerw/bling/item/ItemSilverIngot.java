package dmillerw.bling.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @author dmillerw
 */
public class ItemSilverIngot extends Item {

	public ItemSilverIngot() {
		super();

		setTextureName("alchbling:ingot_silver");
		setCreativeTab(CreativeTabs.tabMaterials);
	}
}
