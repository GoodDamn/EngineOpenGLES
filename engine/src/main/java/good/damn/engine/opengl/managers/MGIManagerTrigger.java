package good.damn.engine.opengl.managers;

import good.damn.engine.opengl.drawers.MGIDrawer;

public interface MGIManagerTrigger
extends MGIDrawer {
    public void loopTriggers(
        final float checkX,
        final float checkY,
        final float checkZ
    );
}
