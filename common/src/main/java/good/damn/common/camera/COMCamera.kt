package good.damn.common.camera

import good.damn.common.matrices.MGMatrixTranslate

data class COMCamera(
    val camera: COICameraFree,
    val projection: COICameraProjection
)