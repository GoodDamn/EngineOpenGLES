package good.damn.engine.opengl.triggers.methods;

import good.damn.engine.opengl.MGVector;

public final class MGTriggerMethodSphere
implements MGITriggerMethod {
    private static final float RADIUS = 1f;

    public final MGVector mPosition;

    public MGTriggerMethodSphere(
        final MGVector position
    ) {
        mPosition = position;
    }

    @Override
    public final boolean canTrigger(
        float x,
        float y,
        float z
    ) {
        final float cx = x - mPosition.getX();
        final float cy = y - mPosition.getY();
        final float cz = z - mPosition.getZ();
        return Math.sqrt(
            cx * cx + cy * cy + cz * cz
        ) < RADIUS;
    }
}
