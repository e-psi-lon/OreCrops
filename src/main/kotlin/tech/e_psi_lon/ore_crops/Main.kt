package tech.e_psi_lon.ore_crops

import io.github.ayfri.kore.arguments.chatcomponents.textComponent
import io.github.ayfri.kore.arguments.colors.Color
import io.github.ayfri.kore.arguments.numbers.TimeNumber
import io.github.ayfri.kore.arguments.numbers.TimeType
import io.github.ayfri.kore.arguments.types.literals.literal
import io.github.ayfri.kore.commands.function
import io.github.ayfri.kore.commands.schedule
import io.github.ayfri.kore.commands.scoreboard.scoreboard
import io.github.ayfri.kore.configuration
import io.github.ayfri.kore.dataPack
import io.github.ayfri.kore.functions.function
import io.github.ayfri.kore.functions.load
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
		val itemDatabase = mutableMapOf<String, OreCropItem>()
		orePlantsItems(itemDatabase)
		oreCropsConventionAdvancements(itemDatabase)
		load("load", NAMESPACE) {
			scoreboard {
				players {
					add(literal("#$name.loaded"), "load.status", 1)
				}
			}
			function("1s", NAMESPACE)
		}
	}
	dataPack.generate()
}