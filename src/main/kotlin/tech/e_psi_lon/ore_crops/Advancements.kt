package tech.e_psi_lon.ore_crops

import io.github.ayfri.kore.DataPack
import io.github.ayfri.kore.arguments.chatcomponents.translatedTextComponent
import io.github.ayfri.kore.arguments.components.matchers.customData
import io.github.ayfri.kore.arguments.types.resources.AdvancementArgument
import io.github.ayfri.kore.arguments.types.resources.FunctionArgument
import io.github.ayfri.kore.features.advancements.*
import io.github.ayfri.kore.features.advancements.triggers.impossible
import io.github.ayfri.kore.features.advancements.triggers.itemUsedOnBlock
import io.github.ayfri.kore.features.advancements.triggers.location
import io.github.ayfri.kore.features.predicates.conditions.matchTool
import io.github.ayfri.kore.features.predicates.sub.item
import io.github.ayfri.kore.features.predicates.sub.predicates
import io.github.ayfri.kore.functions.Function
import io.github.ayfri.kore.functions.generatedFunction
import io.github.ayfri.kore.generated.Advancements
import io.github.ayfri.kore.generated.Items
import net.benwoodworth.knbt.put
import net.benwoodworth.knbt.putNbtCompound

fun DataPack.oreCropsAdvancements(itemDatabase: MutableMap<String, OreCropItem>) {
	val (renewableMinerals, renewableHell) = baseAdvancements()
	for (value in itemDatabase.values) {
		val material = value.material
		val name = value.materialName
		when (value) {
			is Seeds -> {
				advancement("renewable_${name}") {
					namespace = NAMESPACE
					parent = renewableMinerals
					display(value.material) {
						icon(Items.ITEM_FRAME, 1) {
							copyFrom(value)
						}
						title = translatedTextComponent("$NAMESPACE.advancements.renewable_$name.name", fallback = "Renewable $name ?")
						description = translatedTextComponent("$NAMESPACE.advancements.renewable_$name.description", fallback = "Make an infinite $name generator.")
						frame = AdvancementFrameType.TASK
						showToast = true
						announceToChat = true
						hidden = false
					}
					criteria {
						impossible("none")
					}
				}
			}
			is Wart -> {
				advancement(if (material == Items.NETHERITE_SCRAP) "a_source_of_netherite" else "renewable_${name}") {
					namespace = NAMESPACE
					parent = renewableHell
					display(value.material) {
						icon(Items.ITEM_FRAME, 1) {
							copyFrom(value)
						}
						title = if (material == Items.NETHERITE_SCRAP) translatedTextComponent("$NAMESPACE.advancements.a_source_of_netherite.name", fallback = "A source of netherite.") else
							translatedTextComponent("$NAMESPACE.advancements.renewable_$name.name", fallback = "Renewable $name ?")
						frame = if (material == Items.NETHERITE_SCRAP) AdvancementFrameType.CHALLENGE else AdvancementFrameType.TASK
						description = if (material == Items.NETHERITE_SCRAP) translatedTextComponent("$NAMESPACE.advancements.a_source_of_netherite.description", fallback = "Build a netherite wart with a nether wart and two netherite ingots.") else
								translatedTextComponent("$NAMESPACE.advancements.renewable_$name.description", fallback = "Make an infinite $name generator.")
						showToast = true
						announceToChat = true
						hidden = false
					}
					criteria {
						impossible("none")
					}
				}
			}
		}

	}
}

fun DataPack.baseAdvancements(): Pair<AdvancementArgument, AdvancementArgument> {
	val renewableMinerals = advancement("renewable_minerals") {
		namespace = NAMESPACE
		parent = Advancements.Husbandry.PLANT_SEED
		display(Items.WHEAT_SEEDS) {
			title = translatedTextComponent("$NAMESPACE.advancements.renewable_minerals.name", fallback = "Renewable minerals ?")
			description = translatedTextComponent("$NAMESPACE.advancements.renewable_minerals.description", fallback = "Mining is boring... farming is better.")
			frame = AdvancementFrameType.TASK
			showToast = true
			announceToChat = true
			hidden = false
		}
		criteria {
			impossible("none")
		}
	}
	val renewableHell = advancement("renewable_hell") {
		namespace = NAMESPACE
		parent = Advancements.Nether.FIND_FORTRESS
		display(Items.NETHER_WART) {
			title = translatedTextComponent("$NAMESPACE.advancements.renewable_hell.name", fallback = "Renewable hell ?")
			description = translatedTextComponent("$NAMESPACE.advancements.renewable_hell.description", fallback = "Hell is boring... farming is better.")
			frame = AdvancementFrameType.TASK
			showToast = true
			announceToChat = true
			hidden = false
		}
		criteria {
			impossible("none")
		}
	}
	return renewableMinerals to renewableHell
}

fun DataPack.placeSeed(onPlace: Function.(AdvancementArgument) -> Unit) {
	val advancement = advancement("technical/placing_seed") {
		namespace = NAMESPACE
		criteria {
			itemUsedOnBlock("requirement") {
				location {
					predicate {
						matchTool {
							item(Items.ITEM_FRAME)
							predicates {
								customData {
									putNbtCompound(NAMESPACE) {
										put("seed", true)
									}
								}
							}
						}
					}
				}
			}
		}
		rewards(FunctionArgument("on_place", NAMESPACE, configuration.generatedFunctionsFolder))
	}
	generatedFunction("on_place", NAMESPACE) {
		onPlace(advancement)
	}
}