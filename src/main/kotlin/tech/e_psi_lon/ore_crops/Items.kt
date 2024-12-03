package tech.e_psi_lon.ore_crops

import io.github.ayfri.kore.arguments.chatcomponents.textComponent
import io.github.ayfri.kore.arguments.colors.Color
import io.github.ayfri.kore.arguments.components.ComponentsRemovables
import io.github.ayfri.kore.arguments.components.types.customData
import io.github.ayfri.kore.arguments.components.types.itemModel
import io.github.ayfri.kore.arguments.components.types.itemName
import io.github.ayfri.kore.arguments.components.types.lore
import io.github.ayfri.kore.arguments.types.resources.ItemArgument
import net.benwoodworth.knbt.put
import net.benwoodworth.knbt.putNbtCompound

open class OreCropItem : ItemArgument {
	override val name: String
		get() = "item_frame"
	override var components: ComponentsRemovables? = ComponentsRemovables().apply {
		lore(textComponent("Ore Crops", color= Color.BLUE) { italic=true})
		customData {
			putNbtCompound("smithed") {
				putNbtCompound("ignore") {
					put("functionality", true)
					put("crafting", true)
				}
			}
		}
	}
	override val namespace: String
		get() = "minecraft"
	open val material: String
		get() = "material"
}


class Seeds(override val material: String) : OreCropItem() {
	override var components: ComponentsRemovables? = super.components?.apply {
		itemName("${material.capitalize()} Seeds")
		itemModel("${material}_seeds", "ore_crops")
	}
}

class Wart(override val material: String) : OreCropItem() {
	override var components: ComponentsRemovables? =
		ComponentsRemovables().apply {
			itemName("${material.capitalize()} Wart")
			itemModel("${material}_wart", "ore_crops")
			lore(textComponent("Ore Crops", color= Color.BLUE) { italic=true})

		}
	override val namespace: String
		get() = "minecraft"

}