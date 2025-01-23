data class Gesture(
    val x: Float,
    val y: Float,
    val type: String, // e.g., TAP, SWIPE
    val duration: Long // Duration of the gesture in milliseconds
)
