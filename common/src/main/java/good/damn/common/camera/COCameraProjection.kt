package good.damn.common.camera

import android.opengl.Matrix
import good.damn.common.matrices.COMatrixTranslate

class COCameraProjection(
    override val modelMatrix: COMatrixTranslate
): COICameraProjection {

    override val projection = FloatArray(
        16
    )

    override fun setPerspective(
        width: Int,
        height: Int
    ) {
        Matrix.perspectiveM(
            projection,
            0,
            85.0f,
            width.toFloat() / height.toFloat(),
            0.9999999f,
            Float.MAX_VALUE / 2
        )
    }
}