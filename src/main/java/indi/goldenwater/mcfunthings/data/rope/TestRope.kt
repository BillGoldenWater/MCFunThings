package indi.goldenwater.mcfunthings.data.rope

import indi.goldenwater.mcfunthings.type.rope.Point
import indi.goldenwater.mcfunthings.type.rope.Rope
import org.bukkit.util.Vector

val testRope = Rope(stickIterationTimes = 10)

fun initTestRope() {
    var p = Point(Vector(0.0, 0.0, 0.0), locked = true)
    testRope.addPoint(p)

    val pointNum = 50
    for (i in 1..pointNum) {
        val tempPoint = Point(Vector(0.0, 0.0, 0.0))
        testRope.attachAPoint(p, tempPoint)
        p = tempPoint
    }
}