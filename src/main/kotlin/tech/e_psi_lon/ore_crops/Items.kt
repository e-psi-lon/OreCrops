package tech.e_psi_lon.ore_crops

import io.github.ayfri.kore.arguments.chatcomponents.translatedTextComponent
import io.github.ayfri.kore.arguments.components.ComponentsRemovables
import io.github.ayfri.kore.arguments.components.types.*
import io.github.ayfri.kore.arguments.types.resources.ItemArgument
import io.github.ayfri.kore.generated.Items
import io.github.ayfri.kore.nbt.mutableNbt
import io.github.ayfri.kore.nbt.toMutableNbtCompound
import io.github.ayfri.kore.utils.nbtListOf
import net.benwoodworth.knbt.*

abstract class OreCropItem : ItemArgument {
	override val name: String
		get() = Items.ITEM_FRAME.name.lowercase()
	override var components: ComponentsRemovables? = ComponentsRemovables().apply {
		lore(LORE)
		entityData {
			put("id", Items.ITEM_FRAME.asId())
			put("Invisible", true)
			put("Invulnerable", true)
			put("Fixed", true)
			put("Silent", true)
			putNbtList("Tags") {
				add("ore_crops.to_place")
			}

		}
		customData {
			putNbtCompound("ore_crops") {
				put("seed", true)
			}
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
	val materialName: String
		get() = if (material.name.split("_").size > 1) material.name.split("_")[0] else material.name
}


class Seeds(override val material: ItemArgument, override val isExternal: Boolean = false) : OreCropItem() {
	val isGem: Boolean
		get() = material in arrayOf(Items.COAL, Items.DIAMOND, Items.EMERALD, Items.LAPIS_LAZULI, Items.REDSTONE)

	@OptIn(ExperimentalNbtApi::class)
	override var components: ComponentsRemovables? = super.components?.apply {
		itemName(translatedTextComponent("$NAMESPACE.items.${materialName.lowercase()}_seed", fallback = "${materialName.capitalize()} Seed"))
		itemModel("${materialName.lowercase()}_seeds", NAMESPACE)
		val superEntityData = super.components?.components?.values?.filterIsInstance<EntityDataComponent>()?.first()?.data?.toMutableNbtCompound() ?: mutableNbt()
		val newEntityData = superEntityData.apply {
			val temp = this["Tags"]?.nbtList<NbtString>()?.toMutableList()?.apply {
				add(NbtString("ore_crops.${materialName.lowercase()}_wheat"))
			}?.toTypedArray()?.map { it.value }?.let { nbtListOf(*it.toTypedArray()) } ?: nbtListOf("ore_crops.${materialName.lowercase()}_wheat")
			this["Tags"] = temp
		}
		entityData(newEntityData.toNbtCompound())
	}
}

class Wart(override val material: ItemArgument, override val isExternal: Boolean = false) : OreCropItem() {
	@OptIn(ExperimentalNbtApi::class)
	override var components: ComponentsRemovables? = super.components?.apply {
		itemName(translatedTextComponent("$NAMESPACE.items.${materialName.lowercase()}_wart", fallback = "${materialName.capitalize()} Wart"))
		itemModel("${materialName.lowercase()}_wart", NAMESPACE)
		val superEntityData = super.components?.components?.values?.filterIsInstance<EntityDataComponent>()?.first()?.data?.toMutableNbtCompound() ?: mutableNbt()
		val newEntityData = superEntityData.apply {
			val temp = this["Tags"]?.nbtList<NbtString>()?.toMutableList()?.apply {
				add(NbtString("ore_crops.${materialName.lowercase()}_wart"))
			}?.toTypedArray()?.map { it.value }?.let { nbtListOf(*it.toTypedArray()) } ?: nbtListOf("ore_crops.${materialName.lowercase()}_wart")
			this["Tags"] = temp
		}
		entityData(newEntityData.toNbtCompound())
	}
}