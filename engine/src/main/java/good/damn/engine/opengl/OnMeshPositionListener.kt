package good.damn.engine.opengl

interface OnMeshPositionListener {

    fun onChangeMeshPosition(
        dx: Float,
        dy: Float,
        dz: Float
    )

}