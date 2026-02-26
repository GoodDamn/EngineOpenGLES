package good.damn.wrapper.hud.callbacks

import good.damn.engine.sdk.SDVector3

interface APIListenerOnIntersectPosition {
    fun onIntersectPosition(
        p: SDVector3
    )
}