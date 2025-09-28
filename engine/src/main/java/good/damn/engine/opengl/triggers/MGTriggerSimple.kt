package good.damn.engine.opengl.triggers

import android.util.Log
import good.damn.engine.opengl.drawers.MGDrawerLightDirectional

class MGTriggerSimple(
    private val dirLight: MGDrawerLightDirectional,
): MGITrigger {
    override fun onTriggerBegin() {
        Log.d("TAG", "onTriggerBegin: TRIGGER BEGIN")
        dirLight.ambColor.x = 0.5f
        dirLight.ambColor.y = 0.5f
        dirLight.ambColor.z = 0.5f
    }

    override fun onTriggerEnd() {
        Log.d("TAG", "onTriggerBegin: TRIGGER END")
        dirLight.ambColor.x = 0.05f
        dirLight.ambColor.y = 0.05f
        dirLight.ambColor.z = 0.05f
    }
}