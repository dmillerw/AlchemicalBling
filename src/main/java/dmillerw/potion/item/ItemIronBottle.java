package dmillerw.potion.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dmillerw
 */
public class ItemIronBottle extends ItemPotion {

	private static final Color IRON = new Color(122, 122, 122);
	private static final Color MOLTEN_IRON = new Color(155, 122, 122);
	private static final Color SILVER = new Color(200, 200, 200);

	private IIcon bottle;
	private IIcon overlay;

	@Override
	public List getEffects(ItemStack stack) {
		return new ArrayList();
	}

	@Override
	public List getEffects(int id) {
		return new ArrayList();
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		return stack;
	}

	@Override
	public int getColorFromDamage(int damage) {
		switch (damage) {
			case 0: return IRON.getRGB();
			case 1: return MOLTEN_IRON.getRGB();
			case 2: return SILVER.getRGB();
			default: return IRON.getRGB();
		}
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {

	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		if (stack.getItemDamage() == 0) {
			return StatCollector.translateToLocal("item.iron_bottle.name").trim();
		} else if (stack.getItemDamage() == 1) {
			return StatCollector.translateToLocal("item.iron_bottle.molten.name").trim();
		} else {
			return StatCollector.translateToLocal("item.silver_bottle.name").trim();
		}
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		list.add(new ItemStack(this, 1, 0));
		list.add(new ItemStack(this, 1, 1));
		list.add(new ItemStack(this, 1, 2));
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(int par1, int par2) {
		return par2 == 0 ? overlay : bottle;
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		bottle = register.registerIcon(this.getIconString() + "_" + "bottle_drinkable");
		overlay = register.registerIcon(this.getIconString() + "_" + "overlay");
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return stack.getItemDamage() == 2;
	}

	@Override
	public Item getContainerItem() {
		return Items.glass_bottle;
	}
}
