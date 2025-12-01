package good.damn.engine.opengl.managers;

import androidx.annotation.NonNull;

import good.damn.engine.sdk.MGVector3;
import good.damn.engine.opengl.drawers.MGDrawerVertexArray;
import good.damn.engine.opengl.enums.MGEnumStateTrigger;
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateableLight;

public final class MGManagerTriggerLight
extends MGManagerTrigger<
    MGDrawerTriggerStateableLight
> {

    @NonNull
    private final MGManagerLight managerLight;

    public MGManagerTriggerLight(
        @NonNull final MGManagerLight managerLight,
        @NonNull final MGDrawerVertexArray drawerTrigger
    ) {
        super(drawerTrigger);
        this.managerLight = managerLight;
    }

    @Override
    public synchronized void loopTriggers(
        float checkX,
        float checkY,
        float checkZ
    ) {
        @NonNull
        MGVector3 lightPosition;

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
