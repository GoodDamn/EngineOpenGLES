package good.damn.engine.opengl.scene

import android.opengl.GLES30.GL_CCW
import android.opengl.GLES30.GL_CW
import android.opengl.GLES30.GL_REPEAT
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.MGObject3d
import good.damn.engine.opengl.MGSwitcherDrawMode
import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
import good.damn.engine.opengl.callbacks.MGCallbackOnCameraMovement
import good.damn.engine.opengl.callbacks.MGCallbackOnDeltaInteract
import good.damn.engine.opengl.callbacks.MGIListenerOnIntersectPosition
import good.damn.engine.opengl.camera.MGCameraFree
import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
import good.damn.engine.opengl.drawers.MGDrawerMeshOpaque
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGDrawerModeOpaque
import good.damn.engine.opengl.drawers.MGDrawerModeSingleShader
import good.damn.engine.opengl.drawers.MGDrawerModeSwitch
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.drawers.sky.MGDrawerSkyOpaque
import good.damn.engine.opengl.entities.MGLight
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.entities.MGMesh
import good.damn.engine.opengl.enums.MGEnumStateTrigger
import good.damn.engine.opengl.generators.MGGeneratorLandscape
import good.damn.engine.opengl.iterators.vertex.MGVertexIteratorLandscapeDisplace
import good.damn.engine.opengl.iterators.vertex.MGVertexIteratorLandscapeNormal
import good.damn.engine.opengl.managers.MGManagerLight
import good.damn.engine.opengl.maps.MGMapDisplace
import good.damn.engine.opengl.maps.MGMapNormal
import good.damn.engine.opengl.matrices.MGMatrixScale
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal
import good.damn.engine.opengl.matrices.MGMatrixTranslate
import good.damn.engine.opengl.models.MGMDrawMode
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.shaders.MGShaderSingleModeNormals
import good.damn.engine.opengl.shaders.MGShaderSkySphere
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.opengl.triggers.MGTriggerLight
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateable
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateableLight
import good.damn.engine.opengl.triggers.MGTriggerSimple
import good.damn.engine.opengl.triggers.methods.MGTriggerMethodBox
import good.damn.engine.runnables.MGCallbackModelSpawn
import good.damn.engine.touch.MGIListenerScale
import good.damn.engine.ui.MGUILayerEditor
import good.damn.engine.ui.clicks.MGClickImportMesh
import good.damn.engine.ui.clicks.MGClickPlaceMesh
import good.damn.engine.ui.clicks.MGClickSwitchDrawMode
import good.damn.engine.utils.MGUtilsBuffer
import good.damn.engine.utils.MGUtilsVertIndices
import java.util.concurrent.ConcurrentLinkedQueue
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MGScene(
    requesterUserContent: MGIRequestUserContent,
    private val shaderDefault: MGShaderDefault,
    private val shaderSky: MGShaderSkySphere,
    private val shaderNormals: MGShaderSingleModeNormals,
    private val shaderTexCoords: MGShaderSingleMode,
    private val shaderWireframe: MGShaderSingleMode
): GLSurfaceView.Renderer,
MGIListenerOnIntersectPosition {

    private val modelMatrixSky = MGMatrixScale().apply {
        setScale(
            200000f,
            200000f,
            200000f
        )
        invalidateScale()
        invalidatePosition()
    }
    private val modelMatrixCamera = MGMatrixTranslate()
    private val modelMatrixLandscape = MGMatrixTransformationNormal(
        MGMatrixTranslate(),
        shaderDefault
    )

    private val mVerticesSky = MGArrayVertex()
    private val mVerticesLandscape = MGArrayVertex()
    private val mVerticesDebugBox = MGArrayVertex()
    private val mVerticesDebugSphere = MGArrayVertex()

    private val mDrawerDebugBox = MGDrawerVertexArray(
        mVerticesDebugBox
    )

    private val mDrawerDebugSphere = MGDrawerVertexArray(
        mVerticesDebugSphere
    )

    private val mGeneratorLandscape = MGGeneratorLandscape(
        mVerticesLandscape
    )

    private val materialInteract = MGMaterial(
        shaderDefault.material
    )

    private val materialLandscape = MGMaterial(
        shaderDefault.material
    )

    private val mTextureSky = MGTexture(
        shaderSky
    )

    private val mTextureInteract = MGTexture(
        shaderDefault
    )

    private val mTextureLandscape = MGTexture(
        shaderDefault
    )

    private val mBridgeMatrix = MGBridgeRayIntersect()

    private val meshLandscape = MGMesh(
        MGDrawerModeSwitch(
            mVerticesLandscape,
            MGDrawerMeshOpaque(
                mVerticesLandscape,
                mTextureLandscape,
                materialLandscape
            ),
            GL_CW
        ),
        shaderDefault,
        modelMatrixLandscape.model,
        modelMatrixLandscape.normal
    )

    private val meshSky = MGMesh(
        MGDrawerModeSwitch(
            mVerticesSky,
            MGDrawerSkyOpaque(
                mVerticesSky,
                mTextureSky
            ),
            GL_CCW
        ),
        shaderSky,
        modelMatrixSky,
        normals = null
    )

    private val mCameraFree = MGCameraFree(
        modelMatrixCamera
    )

    private val mCallbackOnDeltaInteract = MGCallbackOnDeltaInteract(
        mBridgeMatrix
    )
    private val mCallbackOnCameraMove = MGCallbackOnCameraMovement(
        mCameraFree,
        mBridgeMatrix
    ).apply {
        setListenerIntersection(
            this@MGScene
        )
    }

    private val mHandler = MGHandlerGl()

    private val meshes = ConcurrentLinkedQueue<
        MGDrawerMeshSwitch
        >().apply {
        add(meshLandscape)
    }

    private val mTriggersLight = ConcurrentLinkedQueue<
        MGDrawerTriggerStateableLight
    >()

    private val mTriggers = ConcurrentLinkedQueue<
        MGDrawerTriggerStateable
    >()

    private val mDrawerLightDirectional = MGDrawerLightDirectional(
        shaderDefault.lightDirectional
    )

    private val managerLights = MGManagerLight(
        shaderDefault
    )

    private val mDrawerModeOpaque = MGDrawerModeOpaque(
        shaderSky,
        shaderDefault,
        shaderWireframe,
        meshSky,
        mCameraFree,
        mDrawerLightDirectional,
        meshes,
        mTriggers,
        mTriggersLight,
        managerLights
    )

    private val mSwitcherDrawMode = MGSwitcherDrawMode(
        meshSky,
        meshes,
        mDrawerModeOpaque
    )

    private val mLayerEditor = MGUILayerEditor(
        clickLoadUserContent = MGClickImportMesh(
            mHandler,
            MGCallbackModelSpawn(
                mDrawerDebugBox,
                mBridgeMatrix,
                mTextureInteract,
                materialInteract,
                MGTriggerSimple(
                    mDrawerLightDirectional
                ),
                shaderDefault,
                shaderWireframe,
                mTriggers,
                meshes
            ),
            requesterUserContent
        ),
        clickPlaceMesh = MGClickPlaceMesh(
            mBridgeMatrix
        ),
        clickSwitchDrawerMode = createDrawModeSwitcher()
    ).apply {
        setListenerTouchMove(
            mCallbackOnCameraMove
        )

        setListenerTouchDelta(
            mCallbackOnCameraMove
        )

        setListenerTouchScaleInteract(
            object: MGIListenerScale {
                override fun onScale(
                    scale: Float
                ) {
                    mBridgeMatrix.matrix?.run {
                        setScale(
                            scale,
                            scale,
                            scale
                        )
                        invalidateScaleRotation()
                        calculateInvertTrigger()
                        calculateNormalsMesh()
                    }
                }
            }
        )

        setListenerTouchDeltaInteract(
            mCallbackOnDeltaInteract
        )
    }


    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {
        mVerticesDebugBox.apply {
            configure(
                MGUtilsBuffer.createFloat(
                    MGUtilsVertIndices.createCubeVertices(
                        MGTriggerMethodBox.MIN,
                        MGTriggerMethodBox.MAX
                    )
                ),
                MGUtilsBuffer.createInt(
                    MGUtilsVertIndices.createCubeIndices()
                ),
                stride = 3 * 4
            )
        }

        mVerticesDebugSphere.apply {
            val obj = MGObject3d.createFromAssets(
                "objs/sphere_low.obj"
            )?.get(0) ?: return@apply

            configure(
                obj.vertices,
                obj.indices
            )
        }

        MGTriggerLight.createFromLight(
            MGLight(
                MGVector(1f,1f,0f)
            ),
            mDrawerDebugSphere,
            shaderDefault
        ).run {
            matrix.setPosition(
                2500f,
                -1850f,
                -3250f,
            )
            matrix.radius = 1250f
            matrix.invalidatePosition()
            matrix.invalidateRadius()
            matrix.calculateInvertTrigger()

            mTriggersLight.add(
                triggerState
            )
        }

        MGTriggerLight.createFromLight(
            MGLight(
                MGVector(
                    0f,1f,1f
                )
            ),
            mDrawerDebugSphere,
            shaderDefault
        ).run {
            matrix.setPosition(
                1250f,
                -1850f,
                -3250f
            )
            matrix.radius = 1250f
            matrix.invalidatePosition()
            matrix.invalidateRadius()
            matrix.calculateInvertTrigger()

            mTriggersLight.add(
                triggerState
            )
        }

        mTextureInteract.setupTexture(
            "textures/rock.jpg"
        )

        mTextureLandscape.setupTexture(
            "textures/terrain.png",
            GL_REPEAT
        )

        mTextureSky.setupTexture(
            "textures/sky/night.png"
        )

        MGObject3d.createFromAssets(
            "objs/semi_sphere.obj"
        )?.get(0)?.run {
            mVerticesSky.configure(
                vertices,
                indices
            )
        }

        val landSize = 1024
        mGeneratorLandscape.apply {
            setResolution(
                landSize,
                landSize
            )

            val mapNormal = MGMapNormal.createFromAssets(
                "maps/normal/terrain_normal.png"
            )

            forEachVertex(
                MGVertexIteratorLandscapeNormal(
                    mapNormal
                )
            )
            mapNormal.destroy()

            val mapHeight = MGMapDisplace.createFromAssets(
                "maps/terrain_height.png"
            )

            forEachVertex(
                MGVertexIteratorLandscapeDisplace(
                    mapHeight,
                    255 * 50,
                    landSize,
                    landSize
                )
            )
            mapHeight.destroy()
        }

        val off = mGeneratorLandscape.actualWidth / -2f

        modelMatrixLandscape.model.setPosition(
            off,
            -5500f,
            off
        )

        modelMatrixLandscape.model.invalidatePosition()

        modelMatrixCamera.setPosition(
            0f, 0f, 0f
        )
        modelMatrixCamera.invalidatePosition()
        mDrawerLightDirectional.setPosition(
            2.8929129f,
            10.986f,
            -9.247298f
        )
    }

    override fun onSurfaceChanged(
        gl: GL10?,
        width: Int,
        height: Int
    ) {
        val fWidth = width.toFloat()
        val fHeight = height.toFloat()

        mCameraFree.setPerspective(
            width,
            height
        )

        mLayerEditor.layout(
            0f, 0f,
            fWidth,
            fHeight
        )
    }

    override fun onDrawFrame(
        gl: GL10?
    ) {
        // 1. Camera point triggering needs to check only on self position changes
        // it doesn't need to check on each touch event
        // 2. For other entities who can trigger, check it inside infinite loop
        val model = mCameraFree.modelMatrix
        mTriggersLight.forEach {
            val spos = it.modelMatrix.position
            val state = it.stateManager.trigger(
                model.x - spos.x,
                model.y - spos.y,
                model.z - spos.z,
            )

            when (state) {
                MGEnumStateTrigger.BEGIN -> {
                    managerLights.register(it)
                }
                MGEnumStateTrigger.END -> {
                    managerLights.unregister(it)
                }
                else -> Unit
            }
        }

        mTriggers.forEach {
            it.stateManager.trigger(
                model.x - it.modelMatrix.x,
                model.y - it.modelMatrix.y,
                model.z - it.modelMatrix.z
            )
        }

        mSwitcherDrawMode
            .currentDrawerMode
            .draw()

        mHandler.run()
    }

    fun onTouchEvent(
        event: MotionEvent
    ) {
        mLayerEditor.onTouchEvent(
            event
        )
    }

    override fun onIntersectPosition(
        p: MGVector
    ) {
        mBridgeMatrix.matrix?.run {
            setPosition(
                p.x,
                p.y,
                p.z
            )
            invalidatePosition()
        }
    }

    private inline fun createDrawModeSwitcher() = MGClickSwitchDrawMode(
        mHandler,
        mSwitcherDrawMode,
        MGMDrawMode(
            mDrawerModeOpaque,
            shaderDefault,
            shaderSky,
            shaderDefault
        ),
        MGMDrawMode(
            MGDrawerModeSingleShader(
                shaderWireframe,
                meshSky,
                mCameraFree,
                meshes
            ),
            shaderWireframe,
            shaderWireframe
        ),
        MGMDrawMode(
            MGDrawerModeSingleShader(
                shaderNormals,
                meshSky,
                mCameraFree,
                meshes
            ),
            shaderNormals,
            shaderNormals,
            shaderNormals,
            shaderNormals
        ),
        MGMDrawMode(
            MGDrawerModeSingleShader(
                shaderTexCoords,
                meshSky,
                mCameraFree,
                meshes
            ),
            shaderTexCoords,
            shaderTexCoords
        )
    )
}