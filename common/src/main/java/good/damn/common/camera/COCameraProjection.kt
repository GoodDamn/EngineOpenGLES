package good.damn.common.camera

import android.opengl.Matrix
import good.damn.engine.sdk.matrices.SDMatrixTranslate

class COCameraProjection(
    override val modelMatrix: SDMatrixTranslate
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