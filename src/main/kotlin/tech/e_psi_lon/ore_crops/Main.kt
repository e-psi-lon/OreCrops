package tech.e_psi_lon.ore_crops

import io.github.ayfri.kore.arguments.chatcomponents.textComponent
import io.github.ayfri.kore.arguments.chatcomponents.translatedTextComponent
import io.github.ayfri.kore.arguments.colors.Color
import io.github.ayfri.kore.arguments.enums.Relation
import io.github.ayfri.kore.arguments.scores.ScoreboardCriteria
import io.github.ayfri.kore.arguments.scores.score
import io.github.ayfri.kore.arguments.types.literals.allPlayers
import io.github.ayfri.kore.arguments.types.literals.literal
import io.github.ayfri.kore.arguments.types.resources.FunctionArgument
import io.github.ayfri.kore.commands.execute.execute
import io.github.ayfri.kore.commands.scoreboard.scoreboard
import io.github.ayfri.kore.commands.tellraw
import io.github.ayfri.kore.configuration
import io.github.ayfri.kore.dataPack
import io.github.ayfri.kore.functions.load
import io.github.ayfri.kore.functions.tick
import io.github.ayfri.kore.pack.pack
import java.io.File
import kotlin.io.path.Path

const val NAMESPACE = "ore_crops"
val LORE = translatedTextComponent("$NAMESPACE.lore", fallback = "Ore Crops") {
	color = Color.BLUE
	italic = true
}
const val PATH = "/mnt/shared/Minecraft/instances/1.21.4/.minecraft/saves/ore_crops/datapacks/"


fun main() {
	File("$PATH/Ore Crops").deleteRecursively()
	val dataPack = dataPack("Ore Crops") {
		pack {
			format = 55
			description = /* textComponent("Adds mineral seeds.\nCreated by ", color = Color.GRAY) +
					textComponent("HackFight", color = Color.GOLD) +
					textComponent(" (Updated to 1.21.4 by ", color = Color.GRAY) +
					textComponent("e_psi_lon", color = Color.GOLD) + textComponent(")", color = Color.GRAY) */
					translatedTextComponent("$NAMESPACE.description.1", fallback = "Adds mineral seeds.\nCreated by ") { color = Color.GRAY} +
					textComponent("HackFight", color = Color.GOLD) +
					translatedTextComponent("$NAMESPACE.description.2", fallback = " (Updated to 1.21.4 by ") { color = Color.GRAY} +
					textComponent("e_psi_lon", color = Color.GOLD) + textComponent(")", color = Color.GRAY)
		}
		path = Path(PATH)

		configuration {
			prettyPrint = true
			prettyPrintIndent = "\t"
			generatedFunctionsFolder = "generated"
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
			scoreboard {
				objectives {
					add("ore_plants.data", ScoreboardCriteria.DUMMY)
					add("ore_plants.second", ScoreboardCriteria.DUMMY)
				}
			}
			tellraw(allPlayers { tag = "convention.debug" }, textComponent("[",  color = Color.WHITE) +
					textComponent(this@dataPack.name, color = Color.GOLD ) +
					translatedTextComponent("$NAMESPACE.reloaded.", fallback = "] (re)loaded.") { color = Color.WHITE }
			)

		}
		tick("tick", NAMESPACE) {
			scoreboard {
				players {
					add(literal("#tick"), "$NAMESPACE.second", 1)
				}
			}
			execute {
				ifCondition {
					this.score(literal("#tick"), "$NAMESPACE.second", 20, Relation.GREATER_THAN_OR_EQUAL_TO)
				}
				run("second", NAMESPACE) {
					scoreboard {
						players {
							reset(literal("#tick"), "$NAMESPACE.second")
						}
					}
				}
				run(FunctionArgument("second", NAMESPACE, "generated_scopes"))
			}
		}
	}
	dataPack.generate()
}