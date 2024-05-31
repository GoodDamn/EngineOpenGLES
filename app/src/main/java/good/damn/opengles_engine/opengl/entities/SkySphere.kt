package good.damn.opengles_engine.opengl.entities

import good.damn.opengles_engine.opengl.Object3D
import good.damn.opengles_engine.opengl.StaticMesh
import good.damn.opengles_engine.opengl.camera.BaseCamera

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
            100000f,
            100000f,
            100000f
        )
    }
}