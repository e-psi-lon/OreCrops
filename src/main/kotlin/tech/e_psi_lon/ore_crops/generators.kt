package tech.e_psi_lon.ore_crops

import io.github.ayfri.kore.DataPack
import io.github.ayfri.kore.arguments.components.types.container
import io.github.ayfri.kore.arguments.components.types.itemName
import io.github.ayfri.kore.arguments.components.types.lore
import io.github.ayfri.kore.arguments.components.types.slot
import io.github.ayfri.kore.arguments.types.literals.self
import io.github.ayfri.kore.commands.give
import io.github.ayfri.kore.data.item.ItemStack
import io.github.ayfri.kore.features.itemmodifiers.functions.setComponents
import io.github.ayfri.kore.features.itemmodifiers.functions.setItem
import io.github.ayfri.kore.features.loottables.*
import io.github.ayfri.kore.functions.function
import io.github.ayfri.kore.generated.Items

fun DataPack.orePlantsItems() {
	val itemDatabase = mutableMapOf<String, OreCropItem>()
	for (seedMaterial in arrayOf(Items.COAL, Items.IRON_INGOT, Items.COPPER_INGOT, Items.GOLD_INGOT, Items.DIAMOND, Items.EMERALD, Items.LAPIS_LAZULI, Items.REDSTONE)) {
		val seeds = Seeds(seedMaterial)
		itemDatabase[seedMaterial.name] = seeds
	}
	for (wartMaterial in arrayOf(Items.NETHERITE_SCRAP, Items.QUARTZ)) {
		Wart(wartMaterial)
		itemDatabase[wartMaterial.name] = Wart(wartMaterial)
	}
	function("_give_all", NAMESPACE) {
		val chests = itemDatabase.values.filter { !it.isExternal }.chunked(27).mapIndexed { chestIndex, chunk ->
			Items.CHEST {
				itemName("Chest ${chestIndex + 1}/${itemDatabase.values.filter { !it.isExternal }.chunked(27).size}")
				container {
					chunk.forEachIndexed { index, oreCropItem ->
						slot(index,
							ItemStack(oreCropItem.name.lowercase(),
								count = 1,
								components = oreCropItem.components?.toComponents()
							)
						)
					}
				}
			}
		}
		chests.forEach { chest ->
			give(self(), chest, 1)
		}
	}
	itemDatabase.filter { !it.value.isExternal }.forEach { (key, value) ->
		val keyName = if (key.split("_").size > 1) key.split("_")[0].lowercase() else key.lowercase()
		lootTable("i/$keyName") {
			namespace = NAMESPACE
			pool {
				type = LootTableType.GENERIC
				functions {
					setItem(Items.entries.first { it.name == value.name })
					value.components?.let { defaultComponents ->
						setComponents {
							for (component in defaultComponents.components) {
								components[component.key] = component.value
							}
 						}
					}
				}
			}
		}

		lootTable("crop/$keyName") {
			namespace = NAMESPACE
			pool {
				type = LootTableType.GENERIC
				functions {
					setItem(value.material)
				}
			}
		}
	}
}