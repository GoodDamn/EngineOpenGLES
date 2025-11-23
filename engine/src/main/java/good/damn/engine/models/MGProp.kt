package good.damn.engine.models

import good.damn.engine.opengl.matrices.MGMatrixScaleRotation
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal
import java.util.LinkedList

data class MGProp(
    val fileNameA3d: String,
    val fileNameDiffuse: String,
    val fileNameMetallic: String?,
    val fileNameEmissive: String?,
    val fileNameOpacity: String?,
    val enableCullFace: Boolean,
    val matrices: LinkedList<
        MGMatrixTransformationNormal<
            MGMatrixScaleRotation
            >
        >
)