package good.damn.engine.opengl.triggers;

import good.damn.engine.opengl.MGVector;

public final class MGTriggerMethodSphere
implements MGITriggerMethod {
    public float radius;

    public final MGVector mPosition;

    public MGTriggerMethodSphere(
        final float radius,
        final MGVector position
    ) {
        this.radius = radius;
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
        ) < radius;
    }
}
