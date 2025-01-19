package tech.e_psi_lon.ore_crops


import io.github.ayfri.kore.arguments.types.resources.FunctionArgument
import io.github.ayfri.kore.commands.Command
import io.github.ayfri.kore.commands.execute.Execute
import io.github.ayfri.kore.functions.Function
import io.github.ayfri.kore.functions.generatedFunction
import java.util.Locale

fun String.capitalize() = // La premiere lettre en majuscule, le reste en minuscule
	this.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

context(Function)
fun Execute.run(namespace: String, block: Function.() -> Command): FunctionArgument {
	val function = Function("", "", "", datapack).apply { block() }
	val name = "generated_${hashCode()}"
	val generatedFunction = datapack.generatedFunction(name, namespace) { block() }
	if (generatedFunction.name == name && datapack.configuration.generateCommentOfGeneratedFunctionCall) comment("Generated function ${generatedFunction.asString()}")
	run(generatedFunction)

	return generatedFunction
}