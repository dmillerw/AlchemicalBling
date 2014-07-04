package dmillerw.bling.item.bauble;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.google.common.collect.HashMultimap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dmillerw.bling.entity.EntityPotionBauble;
import dmillerw.bling.potion.BaublePotionEffect;
import dmillerw.bling.helper.BaublePotionHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author dmillerw
 */
public class BaublePotion extends Item implements IBauble {

	private static final String[] NAMES = new String[] {"amulet", "ring"};

	private static final BaubleType[] TYPES = new BaubleType[] {BaubleType.AMULET, BaubleType.RING};

	private IIcon[] item;
	private IIcon[] gem;

	public BaublePotion() {
		super();

		setMaxDamage(0);
		setMaxStackSize(1);
		setHasSubtypes(true);
		setCreativeTab(CreativeTabs.tabCombat);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {
		PotionEffect potionEffect = BaublePotionHelper.getPotionEffectFromItem(stack);

		if (potionEffect != null) {
			HashMultimap hashmultimap = HashMultimap.create();
			String s1 = StatCollector.translateToLocal(potionEffect.getEffectName()).trim();
			Potion potion = Potion.potionTypes[potionEffect.getPotionID()];
			Map map = potion.func_111186_k();

			if (map != null && map.size() > 0) {
				Iterator iterator = map.entrySet().iterator();

				while (iterator.hasNext()) {
					Map.Entry entry = (Map.Entry) iterator.next();
					AttributeModifier attributemodifier = (AttributeModifier) entry.getValue();
					AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), potion.func_111183_a(potionEffect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
					hashmultimap.put(((IAttribute) entry.getKey()).getAttributeUnlocalizedName(), attributemodifier1);
				}
			}

			if (potionEffect.getAmplifier() > 0) {
				s1 = s1 + " " + StatCollector.translateToLocal("potion.potency." + potionEffect.getAmplifier()).trim();
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
	}

	/* VISUALS */
	@SideOnly(Side.CLIENT)
	@Override
	public boolean hasEffect(ItemStack stack, int pass) {
		return pass == 0 && BaublePotionHelper.getPotionEffectFromItem(stack) != null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamageForRenderPass(int damage, int pass) {
		return pass == 0 ? gem[damage] : item[damage];
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getColorFromItemStack(ItemStack stack, int pass) {
		if (pass == 0) {
			BaublePotionEffect effect = BaublePotionHelper.getPotionEffectFromItem(stack);

			if (effect != null) {
				return PotionHelper.func_77915_a(effect.getPotionID(), false);
			}
		}

		return 0xFFFFFF;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	/* ICON REGISTRATION */
	@Override
	public void registerIcons(IIconRegister register) {
		item = new IIcon[NAMES.length];
		gem = new IIcon[NAMES.length];

		for (int i=0; i<NAMES.length; i++) {
			item[i] = register.registerIcon("alchbling:" + NAMES[i]);
			gem[i] = register.registerIcon("alchbling:" + NAMES[i] + "_gem");
		}
	}

	/* NAME */
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		BaublePotionEffect effect = BaublePotionHelper.getPotionEffectFromItem(stack);
		return super.getUnlocalizedName(stack) + "." + NAMES[stack.getItemDamage() % NAMES.length] + "." + (effect != null ? "infused" : "mundane");
	}

	/* ENTITY */
	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return BaublePotionHelper.getPotionEffectFromItem(stack) == null;
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		return new EntityPotionBauble((EntityItem) location);
	}

	/* MISC */

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i=0; i<NAMES.length; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}

	/* IBAUBLE */
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return TYPES[itemstack.getItemDamage() % TYPES.length];
	}

	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		if (player instanceof EntityPlayer) {
			BaublePotionEffect effect = BaublePotionHelper.getPotionEffectFromItem(itemstack);

			if (effect != null) {
				BaublePotionHelper.addPotionEffect((EntityPlayer) player, effect);
			}
		}
	}

	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
		if (player instanceof EntityPlayer) {
			BaublePotionEffect effect = BaublePotionHelper.getPotionEffectFromItem(itemstack);

			if (effect != null) {
				BaublePotionHelper.addPotionEffect((EntityPlayer) player, effect);
			}
		}
	}

	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
		if (player instanceof EntityPlayer) {
			BaublePotionEffect effect = BaublePotionHelper.getPotionEffectFromItem(itemstack);

			if (effect != null) {
				BaublePotionHelper.removePotionEffect((EntityPlayer) player, effect);
			}
		}
	}

	@Override
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
		return BaublePotionHelper.getPotionEffectFromItem(itemstack) != null;
	}

	@Override
	public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}
}
