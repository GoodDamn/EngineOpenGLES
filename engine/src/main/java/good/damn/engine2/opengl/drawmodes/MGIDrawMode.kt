package good.damn.engine2.opengl.drawmodes

import good.damn.apigl.drawers.GLIDrawer

interface MGIDrawMode
: GLIDrawer {
    fun applyDrawMode()
}