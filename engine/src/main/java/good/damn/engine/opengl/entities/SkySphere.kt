package good.damn.engine.opengl.entities

import good.damn.engine.opengl.Object3D
import good.damn.engine.opengl.StaticMesh
import good.damn.engine.opengl.camera.BaseCamera

class SkySphere(
    program: Int,
    camera: BaseCamera
): StaticMesh(
    Object3D.createFromAssets(
        "objs/semi_sphere.obj"
    ),
    "textures/sky/skysphere_light.jpg",
    program,
    camera
) {
    init {
        setScale(
            200000f,
            200000f,
            200000f
        )
    }
}