package tech.e_psi_lon.ore_crops

import java.util.*

fun String.capitalize() = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }