package tech.e_psi_lon.ore_crops

import io.github.ayfri.kore.arguments.chatcomponents.textComponent
import io.github.ayfri.kore.arguments.colors.Color
import io.github.ayfri.kore.arguments.components.ComponentsRemovables
import io.github.ayfri.kore.arguments.components.types.customData
import io.github.ayfri.kore.arguments.components.types.itemModel
import io.github.ayfri.kore.arguments.components.types.itemName
import io.github.ayfri.kore.arguments.components.types.lore
import io.github.ayfri.kore.arguments.types.resources.ItemArgument
import io.github.ayfri.kore.generated.Items
import net.benwoodworth.knbt.put
import net.benwoodworth.knbt.putNbtCompound

abstract class OreCropItem : ItemArgument {
	override val name: String
		get() = Items.ITEM_FRAME.name
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
	abstract val material: ItemArgument
	abstract val isExternal: Boolean
}


class Seeds(override val material: ItemArgument, override val isExternal: Boolean = false) : OreCropItem() {
	val isGem: Boolean
		get() = material in arrayOf(Items.COAL, Items.DIAMOND, Items.EMERALD, Items.LAPIS_LAZULI, Items.REDSTONE)

	override var components: ComponentsRemovables? = super.components?.apply {
		val materialName = if (material.name.split("_").size > 1) material.name.split("_")[0] else material.name
		itemName("${materialName.capitalize()} Seeds")
		itemModel("${materialName.lowercase()}_seeds", NAMESPACE)
	}
}

class Wart(override val material: ItemArgument, override val isExternal: Boolean = false) : OreCropItem() {
	override var components: ComponentsRemovables? = super.components?.apply {
		val materialName = if (material.name.split("_").size > 1) material.name.split("_")[0] else material.name
		itemName("${materialName.capitalize()} Wart")
		itemModel("${materialName.lowercase()}_wart", NAMESPACE)
	}
}