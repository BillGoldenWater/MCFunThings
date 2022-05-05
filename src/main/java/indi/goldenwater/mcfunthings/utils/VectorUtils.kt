package indi.goldenwater.mcfunthings.utils

import org.bukkit.Location
import org.bukkit.util.Vector

object Vector3 {
    fun up() = Vector(0.0, 1.0, 0.0)
    fun down() = Vector(0.0, -1.0, 0.0)
    fun zero() = Vector(0.0, 0.0, 0.0)
    fun epsilon() = Vector(0.000001, 0.000001, 0.000001)
}

//region operators
operator fun Vector.plus(vector: Vector) = this.clone().add(vector)
operator fun Vector.plus(location: Location) = this.clone().add(location.toVector())
operator fun Location.plus(vector: Vector) = this.clone().add(vector)

operator fun Vector.minus(vector: Vector) = this.clone().subtract(vector)
operator fun Vector.minus(location: Location) = this.clone().subtract(location.toVector())
operator fun Location.minus(vector: Vector) = this.clone().subtract(vector)

operator fun Vector.times(vector: Vector) = this.clone().multiply(vector)
operator fun Vector.times(location: Location) = this.clone().multiply(location.toVector())
operator fun Vector.times(n: Double) = this.clone().multiply(n)
operator fun Location.times(n: Double) = this.clone().multiply(n)

operator fun Vector.div(vector: Vector) = this.clone().divide(vector)
operator fun Vector.div(location: Location) = this.clone().divide(location.toVector())
//endregion
