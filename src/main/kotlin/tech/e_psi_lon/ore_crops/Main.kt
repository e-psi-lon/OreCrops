package tech.e_psi_lon.ore_crops

import io.github.ayfri.kore.arguments.chatcomponents.textComponent
import io.github.ayfri.kore.arguments.colors.Color
import io.github.ayfri.kore.arguments.colors.color
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
val LORE = textComponent("Ore Crops", color = Color.BLUE) { italic = true }
const val PATH = "out"

fun main() {
	File("$PATH/Ore Crops").deleteRecursively()
	val dataPack = dataPack("Ore Crops") {
		pack {
			format = 55
			description = textComponent("Adds mineral seeds.\n", color = Color.GRAY) +
					textComponent("Created by e_psi_lon ", color = Color.GOLD) +
					textComponent("(based on the HackFight data pack)", color = Color.GRAY)
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
					textComponent("] (re)loaded.", color = Color.WHITE)
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