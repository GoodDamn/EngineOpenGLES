package good.damn.wrapper.models

data class APMVrParams(
    val distanceInterLens: Float,
    val distanceTrayToLens: Float,
    val distanceScreenToLens: Float,
    val fov: FloatArray,
    val distortionCoeffs: FloatArray
)