package indi.goldenwater.mcfunthings.utils

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.block.Block
import org.bukkit.util.Vector

data class ParticleInfo(val particle: Particle, val dust: DustOptions? = null)

fun ParticleInfo.drawPoint(location: Location) {
    location.world.spawnParticle(particle, location, 1, 0.0, 0.0, 0.0, 0.0, dust, true)
}

fun ParticleInfo.drawLine(srcLoc: Location, desLoc: Location, stepLength: Double = 0.1) {
    val cLoc: Location = srcLoc.clone()

    val totalStep = (srcLoc.distance(desLoc) * (1.0 / stepLength)).toInt()

    val xStep = (desLoc.x - srcLoc.x) * (1.0 / totalStep)
    val yStep = (desLoc.y - srcLoc.y) * (1.0 / totalStep)
    val zStep = (desLoc.z - srcLoc.z) * (1.0 / totalStep)

    for (i in 0..totalStep) {
        this.drawPoint(cLoc)
        cLoc.add(xStep, yStep, zStep)
    }
}

@Suppress("DuplicatedCode")
fun ParticleInfo.drawRect(srcLoc: Location, desLoc: Location) {
    if (srcLoc.y != desLoc.y) {
        srcLoc.clone().let { it.y = desLoc.y;this.drawLine(srcLoc, it) }
        desLoc.clone().let { it.y = srcLoc.y;this.drawLine(desLoc, it) }
    }

    if (srcLoc.x != desLoc.x) {
        srcLoc.clone().let { it.x = desLoc.x;this.drawLine(srcLoc, it) }
        desLoc.clone().let { it.x = srcLoc.x;this.drawLine(desLoc, it) }
    }

    if (srcLoc.z != desLoc.z) {
        srcLoc.clone().let { it.z = desLoc.z;this.drawLine(srcLoc, it) }
        desLoc.clone().let { it.z = srcLoc.z;this.drawLine(desLoc, it) }
    }
}

fun ParticleInfo.drawBox(block: Block, size: Double = 1.0) {
    val loc = block.location.clone()

    this.drawRect(loc, loc.clone().add(size, size, 0.0))
    this.drawRect(loc, loc.clone().add(0.0, size, size))

    loc.x += size
    loc.z += size

    this.drawRect(loc, loc.clone().add(-1.0 * size, size, 0.0))
    this.drawRect(loc, loc.clone().add(0.0, size, -1.0 * size))
}

fun ParticleInfo.drawProjectileTrace(
    initialVelocity: Vector,
    initialLocation: Location,
    iterationTimes: Int = 100,
    drawEndBlock: Boolean = true,
    endBlockParticleInfo: ParticleInfo? = null,
    gravityAcceleration: Double = 0.05,
    resistance: Vector = Vector(0.01, 0.01, 0.01),
) {
    val projectileTrace =
        initialVelocity.calcProjectileTrace(
            initialLocation,
            iterationTimes,
            gravityAcceleration,
            resistance,
            ignoreBlock = !drawEndBlock
        )

    projectileTrace.forEach {
        this.drawPoint(it)
    }

    endBlockParticleInfo?.let {
        if (drawEndBlock && projectileTrace.size > 0)
            it.drawBox(projectileTrace.last().block)
    }
}