package dmillerw.potion;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import dmillerw.potion.client.render.EffectRenderer;
import dmillerw.potion.entity.EntityPotionAmulet;
import dmillerw.potion.entity.EntitySplashPotion;
import dmillerw.potion.handler.BlacklistHandler;
import dmillerw.potion.handler.EntityEventHandler;
import dmillerw.potion.item.ItemInfusedAmulet;
import dmillerw.potion.item.ItemIronBottle;
import dmillerw.potion.item.ItemMundaneAmulet;
import dmillerw.potion.item.ItemSilverIngot;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.io.File;

/**
 * @author dmillerw
 */
@Mod(modid = AlchemicalBling.ID, name= AlchemicalBling.NAME, version="%VERSION%", dependencies="required-after:Forge@[%FORGE_VERSION%,);required-after:Baubles")
public class AlchemicalBling {

	public static final String ID = "AlchBling";
	public static final String NAME = "Alchemical Bling";

	@Mod.Instance(AlchemicalBling.ID)
	public static AlchemicalBling instance;

	public static File configFile;

	public static Item mundaneAmulet;
	public static Item infusedAmulet;
	public static Item ironBottle;
	public static Item silverIngot;

	public static boolean registerSilverWithOreDictionary = true;
	public static boolean allowOreDictionarySilver = true;

	//TODO Broken amulet?

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		AlchemicalBling.configFile = event.getSuggestedConfigurationFile();

		/* CONFIG */
		Configuration configuration = new Configuration(configFile);
		try {
			registerSilverWithOreDictionary = configuration.get(Configuration.CATEGORY_GENERAL, "_registerSilverWithOreDictionary", true).getBoolean(true);
			allowOreDictionarySilver = configuration.get(Configuration.CATEGORY_GENERAL, "_allowOreDictionarySilver", true).getBoolean(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (configuration.hasChanged()) {
				configuration.save();
			}
		}

		/* REGISTRATION */
		mundaneAmulet = new ItemMundaneAmulet().setUnlocalizedName("amulet.mundane");
		infusedAmulet = new ItemInfusedAmulet().setUnlocalizedName("amulet.infused");
		ironBottle = new ItemIronBottle().setUnlocalizedName("iron_bottle").setTextureName("potion");
		silverIngot = new ItemSilverIngot().setUnlocalizedName("ingot.silver");

		GameRegistry.registerItem(mundaneAmulet, mundaneAmulet.getUnlocalizedName());
		GameRegistry.registerItem(infusedAmulet, infusedAmulet.getUnlocalizedName());
		GameRegistry.registerItem(ironBottle, ironBottle.getUnlocalizedName());
		GameRegistry.registerItem(silverIngot, silverIngot.getUnlocalizedName());

		if (registerSilverWithOreDictionary) {
			OreDictionary.registerOre("ingotSilver", silverIngot);
		}

		if (event.getSide().isClient()) {
			MinecraftForge.EVENT_BUS.register(new EffectRenderer());
		}
		MinecraftForge.EVENT_BUS.register(new EntityEventHandler());

		EntityRegistry.registerModEntity(EntitySplashPotion.class, "splashPotion", 1, AlchemicalBling.instance, 64, 64, true);
		EntityRegistry.registerModEntity(EntityPotionAmulet.class, "potionAmulet", 2, AlchemicalBling.instance, 64, 64, true);

		/* RECIPES */
		GameRegistry.addShapelessRecipe(
			new ItemStack(ironBottle, 1, 0),
			Items.iron_ingot
		);

		GameRegistry.addSmelting(
			new ItemStack(ironBottle, 1, 0),
			new ItemStack(ironBottle, 1, 1),
			0F
		);

		GameRegistry.addShapelessRecipe(
			new ItemStack(silverIngot),
			new ItemStack(ironBottle, 1, 2)
		);

		GameRegistry.addShapelessRecipe(
			new ItemStack(ironBottle, 1, 2),
			new ItemStack(ironBottle, 1, 1),
			Items.ghast_tear,
			Items.glowstone_dust
		);

		GameRegistry.addShapedRecipe(
			new ItemStack(mundaneAmulet),
			" I ",
			"IBI",
			" D ",
			'I', silverIngot,
			'B', Items.glass_bottle,
			'D', Items.diamond
		);

		if (allowOreDictionarySilver) {
			GameRegistry.addRecipe(new ShapedOreRecipe(
				new ItemStack(mundaneAmulet),
				" I ",
				"IBI",
				" D ",
				'I', "ingotSilver",
				'B', Items.glass_bottle,
				'D', Items.diamond
			));
		}
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Configuration configuration = new Configuration(configFile);
		try {
			for (Potion potion : Potion.potionTypes) {
				if (potion != null) {
					if (!configuration.get(Configuration.CATEGORY_GENERAL, potion.getName(), true).getBoolean(true)) {
						BlacklistHandler.blacklist(potion.getId());
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (configuration.hasChanged()) {
				configuration.save();
			}
		}
	}
}
