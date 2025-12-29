package good.damn.engine.opengl.runnables

import android.opengl.Matrix

class MGRunnableCycleFrustrumCalc(
    //private val managerMatrix: MGManagerMatrix
): MGIRunnableBounds {

    private val matrixClip = FloatArray(16)
    private val matrixWorldSpace = FloatArray(16)
    private val matrixViewSpace = FloatArray(16)

    override fun run(
        width: Int,
        height: Int
    ) {
        // calculate clip matrix for each model matrix
        // and for each vertex
        // how I calculate clip space for sphere?
        /*Matrix.multiplyMM(
            matrixViewSpace,
            0,
            matrixView,
            0,
            matrixModel,
            0
        )*/

    }

}