package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.MGArrayVertex

interface MGIDrawerMesh
: MGIDrawer {
    var vertexArray: MGArrayVertex
}