package tech.e_psi_lon.ore_crops

import io.github.ayfri.kore.DataPack
import io.github.ayfri.kore.arguments.chatcomponents.text
import io.github.ayfri.kore.arguments.chatcomponents.textComponent
import io.github.ayfri.kore.arguments.components.types.profile
import io.github.ayfri.kore.arguments.types.resources.AdvancementArgument
import io.github.ayfri.kore.arguments.types.resources.ItemArgument
import io.github.ayfri.kore.features.advancements.advancement
import io.github.ayfri.kore.features.advancements.criteria
import io.github.ayfri.kore.features.advancements.display
import io.github.ayfri.kore.features.advancements.triggers.tick
import io.github.ayfri.kore.generated.Items

fun DataPack.userAdvancement(username: String, advancementParent: AdvancementArgument) = advancement(username.lowercase()) {
	namespace = "global"
	parent = advancementParent
	display(Items.PLAYER_HEAD {
		profile(username)
	}) {
		title = textComponent(username)
		description = textComponent("")
		showToast = false
		announceToChat = false
	}
	criteria {
		tick("tick")
	}
}

fun DataPack.globalRoot() = advancement("root") {
	namespace = "global"
	display(Items.KNOWLEDGE_BOOK) {
		title = textComponent("Installed Datapacks")
		description = textComponent("")
		background = "minecraft:textures/block/gray_concrete.png"
		showToast = false
		announceToChat = false
	}
	criteria {
		tick("tick")
	}
}

fun DataPack.dataPackAdvancement(dataPackNamespace: String, dataPackIcon: ItemArgument, parentAdvancement: AdvancementArgument) = advancement(dataPackNamespace) {
	namespace = "global"
	parent = parentAdvancement
	display(dataPackIcon) {
		title = textComponent(name)
		description = pack.description
		showToast = false
		announceToChat = false
	}

	criteria {
		tick("tick")
	}
}

fun DataPack.oreCropsConventionAdvancements(itemDatabase: Map<String, OreCropItem>): AdvancementArgument {
	val root = globalRoot()
	val originalCreator = userAdvancement("HackFight", root)
	advancements.last().parent = AdvancementArgument("root", "global")
	advancements.last().display?.description = textComponent("The original creator of the datapack.")
	val currentMaintainer = userAdvancement("e_psi_lon", originalCreator)
	advancements.last().parent = AdvancementArgument("HackFight", "global")
	return dataPackAdvancement("ore_crops", itemDatabase["DIAMOND_SEEDS"]!!, currentMaintainer).also {
		advancements.last().parent = AdvancementArgument("e_psi_lon", "global")
	}
}