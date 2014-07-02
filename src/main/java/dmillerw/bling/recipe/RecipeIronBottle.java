package dmillerw.bling.recipe;

import dmillerw.bling.AlchemicalBling;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public class RecipeIronBottle implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting inventory, World world) {
		int foundGlassBottle = 0;
		int foundIronIngot = 0;
		boolean foundOther = false;

		for (int i=0; i<inventory.getSizeInventory(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);

			if (stack != null) {
				if (stack.getItem() == Items.glass_bottle) {
					foundGlassBottle++;
				} else if (stack.getItem() == Items.iron_ingot) {
					foundIronIngot++;
				} else {
					foundOther = true;
				}
			}
		}

		return foundGlassBottle > 0 && (foundGlassBottle == foundIronIngot) && !foundOther;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventory) {
		int foundGlassBottle = 0;
		int foundIronIngot = 0;

		for (int i=0; i<inventory.getSizeInventory(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);

			if (stack != null) {
				if (stack.getItem() == Items.glass_bottle) {
					foundGlassBottle++;
				} else if (stack.getItem() == Items.iron_ingot) {
					foundIronIngot++;
				}
			}
		}

		return foundGlassBottle > 0 && (foundGlassBottle == foundIronIngot) ? new ItemStack(AlchemicalBling.bottleIron, foundGlassBottle, 0) : null;
	}

	@Override
	public int getRecipeSize() {
		return 9;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(AlchemicalBling.bottleIron);
	}
}
