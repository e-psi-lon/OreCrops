package tech.e_psi_lon.ore_crops

import io.github.ayfri.kore.arguments.chatcomponents.textComponent
import io.github.ayfri.kore.arguments.colors.Color
import io.github.ayfri.kore.configuration
import io.github.ayfri.kore.dataPack
import io.github.ayfri.kore.pack.pack
import kotlin.io.path.Path

const val NAMESPACE = "ore_crops"
val LORE = textComponent("Ore Crops", color = Color.BLUE) { italic = true }

fun main() {
	val dataPack = dataPack("OreCrops") {
		pack {
			format = 55
			description = textComponent("Makes ores growable!\n") +
					textComponent("Created by e_psi_lon ", color = Color.GOLD) +
					textComponent("(based on the HackFight data pack)", color = Color.GRAY)
		}
		path = Path("out")

		configuration {
			prettyPrint = true
			prettyPrintIndent = "\t"
		}
		orePlantsItems()
	}
	dataPack.generate()
}