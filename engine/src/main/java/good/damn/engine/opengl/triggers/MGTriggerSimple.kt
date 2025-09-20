package good.damn.engine.opengl.triggers

import android.util.Log
import good.damn.engine.opengl.MGVector

class MGTriggerSimple(
    min: MGVector,
    max: MGVector
): MGTriggerBase(
    MGTriggerMethodBox(
        min, max
    )
) {
    override fun onTriggerBegin() {
        Log.d("TAG", "onTriggerBegin: TRIGGER BEGIN")
    }

    override fun onTriggerEnd() {
        Log.d("TAG", "onTriggerBegin: TRIGGER END")
    }
}