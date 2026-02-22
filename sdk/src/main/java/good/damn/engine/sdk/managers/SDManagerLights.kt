package good.damn.engine.sdk.managers

import good.damn.engine.sdk.models.SDMLightPointEntity

class SDManagerLights(
    private val lights: MutableList<
        SDMLightPointEntity
    >
): Iterable<
    SDMLightPointEntity
> {

    fun add(
        light: SDMLightPointEntity
    ) = lights.add(
        light
    )

    override fun iterator() = lights.iterator()

    fun remove(
        light: SDMLightPointEntity
    ) = lights.remove(
        light
    )
}