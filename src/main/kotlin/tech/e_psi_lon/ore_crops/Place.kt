package tech.e_psi_lon.ore_crops

import io.github.ayfri.kore.DataPack
import io.github.ayfri.kore.arguments.maths.vec3
import io.github.ayfri.kore.arguments.numbers.ranges.IntRange
import io.github.ayfri.kore.arguments.numbers.relativePos
import io.github.ayfri.kore.arguments.scores.score
import io.github.ayfri.kore.arguments.types.literals.allPlayers
import io.github.ayfri.kore.arguments.types.literals.literal
import io.github.ayfri.kore.arguments.types.literals.self
import io.github.ayfri.kore.arguments.types.resources.FunctionArgument
import io.github.ayfri.kore.commands.*
import io.github.ayfri.kore.commands.execute.execute
import io.github.ayfri.kore.functions.function
import io.github.ayfri.kore.generated.Blocks
import io.github.ayfri.kore.generated.Sounds

fun DataPack.placeWheat() = function("place_wheat", NAMESPACE, "plant") {
	execute {
		storeSuccess {
			score(literal("#is_farmland"), "$NAMESPACE.data")
		}
		ifCondition {
			block(vec3(0.relativePos, (-1).relativePos, 0.0.relativePos), Blocks.FARMLAND)
		}
	}
	execute {
		storeSuccess {
			score(literal("#is_wheat"), "$NAMESPACE.data")
		}
		ifCondition {
			block(vec3(), Blocks.WHEAT)
		}
	}

	execute {
		ifCondition {
			score(literal("#is_farmland"), "$NAMESPACE.data") equalTo 1
			score(literal("#is_wheat"), "$NAMESPACE.data") equalTo 0
		}
		run(namespace = NAMESPACE) {
			playSound(Sounds.Item.Plant.CROP1, PlaySoundMixer.BLOCK, allPlayers { distance = IntRange(start = null, end=50)})
			setBlock(vec3(), Blocks.WHEAT)
		}
	}
}

fun DataPack.placeNether() = function("place_nether", NAMESPACE, "plant") {

}

fun DataPack.placeMain(itemDatabase: MutableMap<String, OreCropItem>, placeWheat: FunctionArgument, placeNether: FunctionArgument) = function("place_main", NAMESPACE, "plant") {
	for (value in itemDatabase.values) {
		execute {
			ifCondition {
				entity(self { this.tag =  "$NAMESPACE.${value.materialName.lowercase()}_${if (value is Seeds) "wheat" else "wart"}" })
			}
			run("place", NAMESPACE, "plant/${value.materialName.lowercase()}_${if (value is Seeds) "seeds" else "wart"}") {
				function(if (value is Seeds) placeWheat else placeNether)

			}
		}
	}

	tag(self()) {
		remove("$NAMESPACE.to_place")
		add("$NAMESPACE.wheat")
	}
}