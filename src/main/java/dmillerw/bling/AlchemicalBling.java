package dmillerw.bling;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import dmillerw.bling.client.render.PotionEffectRenderer;
import dmillerw.bling.entity.EntityBaublePotion;
import dmillerw.bling.entity.EntitySplashPotion;
import dmillerw.bling.handler.BlacklistHandler;
import dmillerw.bling.handler.BrewingHandler;
import dmillerw.bling.handler.EntityEventHandler;
import dmillerw.bling.handler.EventHelper;
import dmillerw.bling.item.ItemBrewableBottle;
import dmillerw.bling.item.ItemSilverIngot;
import dmillerw.bling.item.bauble.BaublePotion;
import dmillerw.bling.recipe.RecipeIronBottle;
import dmillerw.bling.recipe.RecipeSilver;
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

	public static Item baublePotion;
	public static Item ingotSilver;

	//TODO Broken amulet?

	/* Ugly, but unfortunately necessary */
	public static Item bottleIron;
	public static Item bottleMoltenIron;
	public static Item bottleSilver;
	/* End the ugliness! */

	public static boolean registerSilverWithOreDictionary = true;
	public static boolean allowOreDictionarySilver = true;

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
		baublePotion = new BaublePotion().setUnlocalizedName("bauble.potion");
		ingotSilver = new ItemSilverIngot().setUnlocalizedName("ingot.silver");

		bottleIron = new ItemBrewableBottle(ItemBrewableBottle.IRON.getRGB()).setUnlocalizedName("bottle.iron");
		bottleMoltenIron = new ItemBrewableBottle(ItemBrewableBottle.MOLTEN_IRON.getRGB()).setUnlocalizedName("bottle.molten_iron");
		bottleSilver = new ItemBrewableBottle(ItemBrewableBottle.SILVER.getRGB()).setUnlocalizedName("bottle.silver");

		GameRegistry.registerItem(baublePotion, baublePotion.getUnlocalizedName());
		GameRegistry.registerItem(ingotSilver, ingotSilver.getUnlocalizedName());

		GameRegistry.registerItem(bottleIron, bottleIron.getUnlocalizedName());
		GameRegistry.registerItem(bottleMoltenIron, bottleMoltenIron.getUnlocalizedName());
		GameRegistry.registerItem(bottleSilver, bottleSilver.getUnlocalizedName());

		/* FILL STATIC REFERENCES */


		if (registerSilverWithOreDictionary) {
			OreDictionary.registerOre("ingotSilver", ingotSilver);
		}

		if (event.getSide().isClient()) {
			MinecraftForge.EVENT_BUS.register(new PotionEffectRenderer());
		}
		EventHelper.batchRegister(new EntityEventHandler(), new BrewingHandler());

		EntityRegistry.registerModEntity(EntitySplashPotion.class, "splashPotion", 1, AlchemicalBling.instance, 64, 64, true);
		EntityRegistry.registerModEntity(EntityBaublePotion.class, "potionAmulet", 2, AlchemicalBling.instance, 64, 64, true);

		/* RECIPES */
		GameRegistry.addRecipe(new RecipeIronBottle());

		GameRegistry.addSmelting(
			new ItemStack(bottleIron),
			new ItemStack(bottleMoltenIron),
			0F
		);

		GameRegistry.addRecipe(new RecipeSilver());

		/* AMULET */
		GameRegistry.addShapedRecipe(
			new ItemStack(baublePotion, 1, 0),
			" I ",
			"IBI",
			" D ",
			'I', ingotSilver,
			'B', new ItemStack(Items.potionitem, 1, OreDictionary.WILDCARD_VALUE),
			'D', Items.diamond
		);

		/* RING */
		GameRegistry.addShapedRecipe(
			new ItemStack(baublePotion, 1, 1),
			"DI ",
			"IBI",
			" I ",
			'I', ingotSilver,
			'B', new ItemStack(Items.potionitem, 1, OreDictionary.WILDCARD_VALUE),
			'D', Items.diamond
		);


		if (allowOreDictionarySilver) {
			/* AMULET */
			GameRegistry.addRecipe(new ShapedOreRecipe(
				new ItemStack(baublePotion, 1, 0),
				" I ",
				"IBI",
				" D ",
				'I', "ingotSilver",
				'B', new ItemStack(Items.potionitem, 1, OreDictionary.WILDCARD_VALUE),
				'D', Items.diamond
			));

			/* RING */
			GameRegistry.addRecipe(new ShapedOreRecipe(
				new ItemStack(baublePotion, 1, 1),
				"DI ",
				"IBI",
				" I ",
				'I', "ingotSilver",
				'B', new ItemStack(Items.potionitem, 1, OreDictionary.WILDCARD_VALUE),
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
					boolean enabled = (potion != Potion.regeneration && potion != Potion.heal);
					if (!configuration.get(Configuration.CATEGORY_GENERAL, potion.getName(), enabled).getBoolean(enabled)) {
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
