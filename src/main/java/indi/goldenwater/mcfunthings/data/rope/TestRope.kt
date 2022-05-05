package indi.goldenwater.mcfunthings.data.rope

import indi.goldenwater.mcfunthings.type.rope.Point
import indi.goldenwater.mcfunthings.type.rope.Rope
import org.bukkit.util.Vector

const val xMax = 21
const val yMax = 25
const val space = 0.15

fun createTestRope(pos: Vector): Rope {
    val testRope = Rope(iterationTimes = 100)

    for (x in 1..xMax) {
        testRope.addPoint(Point(pos.clone(), locked = (x - 1) % 20 == 0))
        pos.x += space
    }

    for (y in 2..yMax) {
        pos.x -= space * xMax
        pos.z -= space
        for (x in 1..xMax) {
            testRope.addPoint(Point(pos.clone()/*, locked = (x - 1) % 20 == 0 && y == yMax*/))
            pos.x += space
        }
    }

    for (y in 1..yMax) {
        for (x in 2..xMax) {
            testRope.addStick(
                testRope.points[xYToIndex(x - 1, y, xMax)],
                testRope.points[xYToIndex(x, y, xMax)],
                space
            )
        }
    }

    for (y in 2..yMax) {
        for (x in 1..xMax) {
            testRope.addStick(
                testRope.points[xYToIndex(x, y - 1, xMax)],
                testRope.points[xYToIndex(x, y, xMax)],
                space
            )
        }
    }

    return testRope
}

fun createTestRope2(pos: Vector, prevPos: Vector): Rope {
    val testRope = Rope()
//    for (i in 1..100) {
//        testRope.addPoint(
//            Point(
//                pos.clone().add(Vector.getRandom().subtract(Vector(0.5, 0.5, 0.5)).normalize() * 0.2),
//                prevPos.clone()
//            )
//        )
//    }
    return testRope
}

fun xYToIndex(x: Int, y: Int, xMax: Int): Int {
    return (y - 1) * xMax + (x - 1)
}