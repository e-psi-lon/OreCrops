package tech.e_psi_lon

import io.github.ayfri.kore.arguments.chatcomponents.textComponent
import io.github.ayfri.kore.arguments.colors.Color
import io.github.ayfri.kore.configuration
import io.github.ayfri.kore.dataPack
import io.github.ayfri.kore.pack.pack
import kotlin.io.path.Path

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
	println(Seeds("diamond").asString())
	dataPack.generate()
}