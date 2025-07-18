package good.damn.engine.opengl.entities

import good.damn.engine.opengl.MGObject3D
import good.damn.engine.opengl.MGMeshStatic

class MGSkySphere(
    program: Int
): MGMeshStatic(
    MGObject3D.createFromAssets(
        "objs/semi_sphere.obj"
    ),
    "textures/sky/skysphere_light.jpg",
    program
) {
    init {
        setScale(
            200000f,
            200000f,
            200000f
        )
    }
}