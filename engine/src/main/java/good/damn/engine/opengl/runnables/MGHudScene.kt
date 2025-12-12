package good.damn.engine.opengl.runnables

import android.util.Log
import good.damn.engine.hud.MGHud
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.MGSwitcherDrawMode
import good.damn.engine.opengl.entities.MGLight
import good.damn.engine.opengl.framebuffer.MGFrameBufferG
import good.damn.engine.opengl.framebuffer.MGFramebuffer
import good.damn.engine.opengl.managers.MGManagerLight
import good.damn.engine.opengl.matrices.MGMatrixScale
import good.damn.engine.opengl.matrices.MGMatrixTransformationInvert
import good.damn.engine.opengl.triggers.MGMatrixTriggerLight
import good.damn.engine.opengl.triggers.MGTriggerLight
import good.damn.engine.opengl.triggers.callbacks.MGManagerTriggerState
import good.damn.engine.opengl.triggers.methods.MGITriggerMethod
import good.damn.engine.opengl.triggers.methods.MGTriggerMethodSphere
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateableLight
import good.damn.engine.sdk.MGVector3
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class MGHudScene(
    requesterUserContent: MGIRequestUserContent,
    informator: MGMInformator,
    framebufferG: MGFramebuffer
) {
    val hud: MGHud
    val runnableCycle: MGIRunnableBounds

    init {
        val switcherDrawMode = MGSwitcherDrawMode(
            informator,
            framebufferG
        )
        val lightCount = 8
        val dpi = (PI * 2 / lightCount).toFloat()
        val radius = 1600f
        var current = 0f
        for (i in 0 until lightCount) {
            val light = MGLight(
                MGVector3(
                    1.0f,
                    .784f,
                    .515f
                ),
                0.6f,
                0.4f,
                0.25f,
                2700f
            )
            MGTriggerLight.createFromLight(
                light
            ).run {
                matrix.radius = 10000f
                matrix.setPosition(
                    250f + radius * sin(current),
                    2160.65f,
                    250f + radius * cos(current)
                )
                matrix.invalidatePosition()
                matrix.invalidateRadius()
                matrix.calculateInvertTrigger()

                informator.managerTriggerLight.addTrigger(
                    triggerState
                )
                current += dpi
            }
        }

        hud = MGHud(
            requesterUserContent,
            informator,
            switcherDrawMode
        )

        runnableCycle = MGRunnableCycleSwitcherDrawMode(
            switcherDrawMode
        )
    }


}