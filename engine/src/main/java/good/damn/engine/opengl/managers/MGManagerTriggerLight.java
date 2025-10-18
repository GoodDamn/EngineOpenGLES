package good.damn.engine.opengl.managers;

import androidx.annotation.NonNull;

import good.damn.engine.opengl.MGVector;
import good.damn.engine.opengl.enums.MGEnumStateTrigger;
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation;
import good.damn.engine.opengl.triggers.MGMatrixTriggerLight;
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateableLight;

public final class MGManagerTriggerLight
extends MGManagerTrigger<
    MGDrawerTriggerStateableLight
> {

    @NonNull
    private final MGManagerLight managerLight;

    public MGManagerTriggerLight(
        @NonNull final MGManagerLight managerLight
    ) {
        this.managerLight = managerLight;
    }

    @Override
    public synchronized void loopTriggers(
        float checkX,
        float checkY,
        float checkZ
    ) {
        @NonNull
        MGVector lightPosition;

        for (
            @NonNull
            final MGDrawerTriggerStateableLight trigger : mTriggers
        ) {
            lightPosition = trigger.getModelMatrix().getPosition();
            position4[0] = checkX - lightPosition.getX();
            position4[1] = checkY - lightPosition.getY();
            position4[2] = checkZ - lightPosition.getZ();

            @NonNull final MGEnumStateTrigger state = trigger.getStateManager().trigger(
                position4
            );

            switch (
                state
            ) {
                case BEGIN:
                    managerLight.register(
                        trigger
                    );
                    break;
                case END:
                    managerLight.unregister(
                        trigger
                    );
                    break;
            }
        }
    }
}
