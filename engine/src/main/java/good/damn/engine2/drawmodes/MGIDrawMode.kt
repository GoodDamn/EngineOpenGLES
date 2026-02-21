package good.damn.engine2.drawmodes

import good.damn.apigl.drawers.GLIDrawer

interface MGIDrawMode
: GLIDrawer {
    fun applyDrawMode()
}