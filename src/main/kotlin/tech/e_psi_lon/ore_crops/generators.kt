package tech.e_psi_lon.ore_crops

import io.github.ayfri.kore.DataPack
import io.github.ayfri.kore.arguments.components.types.container
import io.github.ayfri.kore.arguments.components.types.slot
import io.github.ayfri.kore.arguments.types.literals.self
import io.github.ayfri.kore.commands.give
import io.github.ayfri.kore.data.item.ItemStack
import io.github.ayfri.kore.features.itemmodifiers.functions.setComponents
import io.github.ayfri.kore.features.loottables.*
import io.github.ayfri.kore.functions.function
import io.github.ayfri.kore.generated.Items

fun DataPack.orePlantsItems() {
	val itemDatabase = mutableMapOf<String, OreCropItem>()
	for (seedMaterial in arrayOf("coal", "iron", "copper", "gold", "diamond", "emerald", "lapis", "redstone")) {
		val seeds = Seeds(seedMaterial)
		itemDatabase[seedMaterial] = seeds
	}
	for (wartMaterial in arrayOf("quartz", "netherite")) {
		Wart(wartMaterial)
		itemDatabase[wartMaterial] = Wart(wartMaterial)
	}
	function("_give_all", "ore_crops") {
		val chest = Items.CHEST {
			container {
				itemDatabase.values.forEachIndexed { index, oreCropItem ->
					slot(index, ItemStack(itemDatabase.keys.first { it == oreCropItem.material }, count = 1, components = oreCropItem.components?.toComponents()))
				}
			}
		}
		give(self(), chest,1)
	}
	itemDatabase.forEach { (key, value) ->
		lootTable("i/$key") {
			namespace = "ore_crops"
			pool {
				type = LootTableType.GENERIC
				functions {
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
	}
}