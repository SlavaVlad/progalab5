package persistence.database.product

import kotlinx.serialization.Serializable

@Serializable
class Location (
    val x: Int = 0,
    val y: Long = 0,
    val z: Int = 0,
) {
    override fun toString(): String {
        return "$x,$y,$z"
    }
}