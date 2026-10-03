package good.damn.wrapper.runnables

import good.damn.common.COHandlerGlExecutor
import good.damn.wrapper.controllers.APControllerVr
import good.damn.wrapper.controllers.APIDrawer

class APRunnableVr(
    private val controllerVr: APControllerVr,
    private val outMatrixPose: FloatArray,
    private val handlerExecutor: COHandlerGlExecutor
): APIDrawer {

    override fun onDraw(
        indexEye: Int
    ) {
        controllerVr.getPose(
            outMatrixPose,
            indexEye
        )

        handlerExecutor.runCycle(
            controllerVr.width,
            controllerVr.height
        )
    }

}