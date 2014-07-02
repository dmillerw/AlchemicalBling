package dmillerw.bling.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.google.common.collect.HashMultimap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dmillerw.bling.helper.AmuletHelper;
import dmillerw.bling.helper.BaublePotionEffect;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author dmillerw
 */
public class ItemInfusedAmulet extends Item implements IBauble {

	public static PotionEffect getPotionEffect(EntityPlayer player, ItemStack stack) {
		int id = stack.getItemDamage();
		int amplifier = 0;

		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("amp")) {
			amplifier = stack.getTagCompound().getInteger("amp");
		}

		return new BaublePotionEffect(id, amplifier);
	}

	public static void addPotionEffect(EntityPlayer player, ItemStack stack) {
		int id = stack.getItemDamage();
		int amplifier = 0;

		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("amp")) {
			amplifier = stack.getTagCompound().getInteger("amp");
		}

		AmuletHelper.addPotionEffect(player, id, amplifier);
	}

	private IIcon amulet;
	private IIcon gem;

	public ItemInfusedAmulet() {
		super();

		setMaxDamage(0);
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.tabCombat);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {
		HashMultimap hashmultimap = HashMultimap.create();
		PotionEffect potioneffect = getPotionEffect(player, stack);
		String s1 = StatCollector.translateToLocal(potioneffect.getEffectName()).trim();
		Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
		Map map = potion.func_111186_k();

		if (map != null && map.size() > 0) {
			Iterator iterator = map.entrySet().iterator();

			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				AttributeModifier attributemodifier = (AttributeModifier) entry.getValue();
				AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), potion.func_111183_a(potioneffect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
				hashmultimap.put(((IAttribute) entry.getKey()).getAttributeUnlocalizedName(), attributemodifier1);
			}
		}

		if (potioneffect.getAmplifier() > 0) {
			s1 = s1 + " " + StatCollector.translateToLocal("potion.potency." + potioneffect.getAmplifier()).trim();
		}

		if (potion.isBadEffect()) {
			list.add(EnumChatFormatting.RED + s1);
		} else {
			list.add(EnumChatFormatting.GRAY + s1);
		}

		if (!hashmultimap.isEmpty()) {
			list.add("");
			list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("potion.effects.whenDrank"));
			Iterator iterator1 = hashmultimap.entries().iterator();

			while (iterator1.hasNext()) {
				Map.Entry entry1 = (Map.Entry) iterator1.next();
				AttributeModifier attributemodifier2 = (AttributeModifier) entry1.getValue();
				double d0 = attributemodifier2.getAmount();
				double d1;

				if (attributemodifier2.getOperation() != 1 && attributemodifier2.getOperation() != 2) {
					d1 = attributemodifier2.getAmount();
				} else {
					d1 = attributemodifier2.getAmount() * 100.0D;
				}

				if (d0 > 0.0D) {
					list.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier2.getOperation(), new Object[]{ItemStack.field_111284_a.format(d1), StatCollector.translateToLocal("attribute.name." + (String) entry1.getKey())}));
				} else if (d0 < 0.0D) {
					d1 *= -1.0D;
					list.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("attribute.modifier.take." + attributemodifier2.getOperation(), new Object[]{ItemStack.field_111284_a.format(d1), StatCollector.translateToLocal("attribute.name." + (String) entry1.getKey())}));
				}
			}
		}
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack, int pass) {
		return pass == 0;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {

	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamageForRenderPass(int damage, int pass) {
		return pass == 0 ? gem : amulet;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getColorFromItemStack(ItemStack stack, int pass) {
		return pass == 0 ? PotionHelper.func_77915_a(stack.getItemDamage(), false) : 0xFFFFFF;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister register) {
		amulet = register.registerIcon("alchbling:amulet");
		gem = register.registerIcon("alchbling:amulet_gem");
	}

	/* IBAUBLE */
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.AMULET;
	}

	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		if (player instanceof EntityPlayer) {
			addPotionEffect((EntityPlayer) player, itemstack);
		}
	}

	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
		if (player instanceof EntityPlayer) {
			addPotionEffect((EntityPlayer) player, itemstack);
		}
	}

	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
		if (player instanceof EntityPlayer) {
			AmuletHelper.removePotionEffect((EntityPlayer) player, itemstack.getItemDamage());
		}
	}

	@Override
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	@Override
	public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}
}
