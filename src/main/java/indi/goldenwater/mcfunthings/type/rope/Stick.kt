package indi.goldenwater.mcfunthings.type.rope

const val defaultStickLength = 0.2

data class Stick(
    var point1: Point,
    var point2: Point,
    var length: Double = defaultStickLength,
)
