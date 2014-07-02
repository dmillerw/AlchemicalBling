package dmillerw.potion.item;

import dmillerw.potion.entity.EntityPotionAmulet;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public class ItemMundaneAmulet extends Item {

	private IIcon amulet;

	public ItemMundaneAmulet() {
		super();

		setMaxDamage(0);
		setMaxStackSize(1);
		setTextureName("alchbling:amulet");
		setCreativeTab(CreativeTabs.tabCombat);
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		return new EntityPotionAmulet((EntityItem) location);
	}
}
