package indi.goldenwater.mcfunthings.utils

import net.kyori.adventure.text.Component

fun String.toComponent():Component = Component.text(this)