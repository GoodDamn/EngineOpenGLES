package good.damn.engine.ui.seek

import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
import good.damn.engine.ui.MGIListenerValueChanged

class MGSeekValueChangedLightAmbient(
    private val light: MGDrawerLightDirectional
): MGIListenerValueChanged {
    override fun onValueChanged(
        v: Float
    ) {
        light.ambient = v
    }
}