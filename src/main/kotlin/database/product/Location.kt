package app.database.product

import kotlinx.serialization.Serializable

@Serializable
class Location (
    private val x: Int = 0,
    private val y: Long = 0,
    private val z: Int = 0,
) {
    override fun toString(): String {
        return "$x,$y,$z"
    }
}