package good.damn.engine.opengl.managers;

import good.damn.engine.opengl.drawers.MGIDrawer;
import good.damn.engine.opengl.drawers.MGIDrawerShader;
import good.damn.engine.opengl.shaders.MGIShaderModel;

public interface MGIManagerTrigger
extends MGIDrawerShader<MGIShaderModel> {
    public void loopTriggers(
        final float checkX,
        final float checkY,
        final float checkZ
    );
}
