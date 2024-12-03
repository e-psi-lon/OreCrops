package tech.e_psi_lon.ore_crops

import java.util.*

fun String.capitalize() = // La premiere lettre en majuscule, le reste en minuscule
	this.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }