package dmillerw.bling.creative;

import dmillerw.bling.AlchemicalBling;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @author dmillerw
 */
public class TabAlchemicalBling extends CreativeTabs {

	public TabAlchemicalBling() {
		super(AlchemicalBling.ID);
	}

	@Override
	public Item getTabIconItem() {
		return null;
	}
}
