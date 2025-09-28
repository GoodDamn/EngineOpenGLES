package good.damn.engine.opengl.renderer

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES30.*
import android.util.Log
import android.view.MotionEvent
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
import good.damn.engine.opengl.MGObject3D
import good.damn.engine.opengl.MGSwitcherDrawMode
import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.callbacks.MGCallbackOnCameraMovement
import good.damn.engine.opengl.callbacks.MGCallbackOnDeltaInteract
import good.damn.engine.opengl.callbacks.MGIListenerOnIntersectPosition
import good.damn.engine.opengl.camera.MGCameraFree
import good.damn.engine.opengl.drawers.MGDrawerMeshOpaque
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGDrawerModeOpaque
import good.damn.engine.opengl.drawers.MGDrawerModeSwitch
import good.damn.engine.opengl.drawers.MGDrawerModeSingleShader
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.sky.MGDrawerSkyOpaque
import good.damn.engine.opengl.entities.MGLight
import good.damn.engine.opengl.entities.MGMesh
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.generators.MGGeneratorLandscape
import good.damn.engine.opengl.maps.MGMapDisplace
import good.damn.engine.opengl.maps.MGMapNormal
import good.damn.engine.opengl.iterators.vertex.MGVertexIteratorLandscapeDisplace
import good.damn.engine.opengl.iterators.vertex.MGVertexIteratorLandscapeNormal
import good.damn.engine.opengl.managers.MGManagerLight
import good.damn.engine.opengl.matrices.MGMatrixNormal
import good.damn.engine.opengl.matrices.MGMatrixScale
import good.damn.engine.opengl.matrices.MGMatrixTransformation
import good.damn.engine.opengl.matrices.MGMatrixTranslate
import good.damn.engine.opengl.models.MGMDrawMode
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.opengl.shaders.MGShaderSkySphere
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.shaders.MGShaderSingleModeNormals
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.opengl.triggers.MGDrawerTriggerStateable
import good.damn.engine.opengl.triggers.MGManagerTriggerState
import good.damn.engine.opengl.triggers.MGTriggerMethodBox
import good.damn.engine.opengl.triggers.MGTriggerSimple
import good.damn.engine.touch.MGIListenerScale
import good.damn.engine.ui.MGUILayerEditor
import good.damn.engine.ui.clicks.MGClickGenerateLandscape
import good.damn.engine.ui.clicks.MGClickPlaceMesh
import good.damn.engine.ui.clicks.MGClickSwitchDrawMode
import good.damn.engine.utils.MGUtilsBuffer
import good.damn.engine.utils.MGUtilsVertIndices
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class MGRendererLevelEditor(
    requesterUserContent: MGIRequestUserContent
): GLSurfaceView.Renderer,
MGIListenerOnIntersectPosition {

    companion object {
        private const val TAG = "MGRendererLevelEditor"
    }

    private val mShaderDefault = MGShaderDefault()
    private val mShaderSky = MGShaderSkySphere()
    private val mShaderNormals = MGShaderSingleModeNormals()
    private val mShaderTexCoords = MGShaderSingleMode()
    private val mShaderWireframe = MGShaderSingleMode()

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
    private val modelMatrixLandscape = MGMatrixTransformation(
        MGMatrixTranslate(),
        mShaderDefault
    )
    private val modelMatrixTrigger = MGMatrixTransformation(
        MGMatrixScale().apply {
            setScale(50f, 50f, 50f)
            invalidatePosition()
            invalidateScale()
        },
        mShaderDefault
    )

    private val mVerticesBatchObject = MGArrayVertex()

    private val mVerticesSky = MGArrayVertex()
    private val mVerticesLandscape = MGArrayVertex()

    private val mGeneratorLandscape = MGGeneratorLandscape(
        mVerticesLandscape
    )

    private val materialInteract = MGMaterial(
        mShaderDefault.material
    )

    private val materialLandscape = MGMaterial(
        mShaderDefault.material
    )

    private val mTextureSky = MGTexture(
        mShaderSky
    )

    private val mTextureInteract = MGTexture(
        mShaderDefault
    )

    private val mTextureLandscape = MGTexture(
        mShaderDefault
    )

    private val mDrawerSwitchBatch = MGDrawerModeSwitch(
        mVerticesBatchObject,
        MGDrawerMeshOpaque(
            mVerticesBatchObject,
            mTextureInteract,
            materialInteract
        )
    )

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
        mShaderDefault,
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
        mShaderSky,
        modelMatrixSky,
        normals = null
    )

    private val mCameraFree = MGCameraFree(
        modelMatrixCamera
    )

    private val mCallbackOnDeltaInteract = MGCallbackOnDeltaInteract()
    private val mCallbackOnCameraMove = MGCallbackOnCameraMovement(
        mCameraFree
    ).apply {
        setListenerIntersection(
            this@MGRendererLevelEditor
        )
    }

    private val mHandler = MGHandlerGl()

    private val meshes = ConcurrentLinkedQueue<
        MGDrawerMeshSwitch
    >().apply {
        add(meshLandscape)
    }

    private val mTriggers = ConcurrentLinkedQueue<
        MGDrawerTriggerStateable
    >()

    private var mWidth = 0
    private var mHeight = 0

    private val mDrawerLightDirectional = MGDrawerLightDirectional(
        mShaderDefault.lightDirectional
    )

    private val managerLights = MGManagerLight(
        mShaderDefault
    )

    private val mDrawerModeOpaque = MGDrawerModeOpaque(
        mShaderSky,
        mShaderDefault,
        mShaderWireframe,
        meshSky,
        mCameraFree,
        mDrawerLightDirectional,
        meshes,
        mTriggers,
        managerLights
    )

    private val mSwitcherDrawMode = MGSwitcherDrawMode(
        meshSky,
        meshes,
        mDrawerModeOpaque
    )

    private val mLayerEditor = MGUILayerEditor(
        clickLoadUserContent = MGClickGenerateLandscape(
            mHandler,
            mGeneratorLandscape,
            requesterUserContent
        ),
        clickPlaceMesh = MGClickPlaceMesh(
            mCallbackOnDeltaInteract,
            meshes,
            mDrawerSwitchBatch,
            mShaderDefault,
            mCallbackOnCameraMove
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
                    mCallbackOnDeltaInteract.currentMeshInteract?.setScale(
                        scale,
                        scale,
                        scale
                    )
                }
            }
        )

        setListenerTouchDeltaInteract(
            mCallbackOnDeltaInteract
        )
    }

    private val mLight = MGLight(
        MGVector(
            1f, 1f, 0f
        ),
        MGVector(0f,0f,0f),
        600f
    ).apply {
        managerLights.register(
            this
        )
    }

    private val mLight2 = MGLight(
        MGVector(
            1f, 0f, 1f
        ),
        MGVector(0f,0f,0f),
        600f
    ).apply {
        managerLights.register(
            this
        )
    }
    
    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {
        mShaderWireframe.setup(
            "shaders/wireframe/vert.glsl",
            "shaders/wireframe/frag.glsl"
        )

        mShaderDefault.setup(
            "shaders/vert.glsl",
            "shaders/frag.glsl"
        )

        mShaderSky.setup(
            "shaders/sky/vert.glsl",
            "shaders/sky/frag.glsl"
        )

        mShaderNormals.setup(
            "shaders/normals/vert.glsl",
            "shaders/normals/frag.glsl"
        )

        mShaderTexCoords.setup(
            "shaders/texCoords/vert.glsl",
            "shaders/texCoords/frag.glsl"
        )


        MGObject3D.createFromAssets(
            "objs/box.obj"
        ).run {
            mVerticesBatchObject.configure(
                vertices,
                indices
            )
        }


        mTriggers.add(
            MGDrawerTriggerStateable(
                MGManagerTriggerState(
                    MGTriggerMethodBox(
                        modelMatrixTrigger.normal
                    ),
                    MGTriggerSimple(
                        mDrawerLightDirectional
                    )
                ),
                MGArrayVertex().apply {
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
                },
                mShaderWireframe,
                modelMatrixTrigger.model
            )
        )

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

        MGObject3D.createFromAssets(
            "objs/semi_sphere.obj"
        ).run {
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

        val lx = 0.0f
        val ly = 0.5f
        val lz = -0.5f
        val m = 2048f
        modelMatrixCamera.setPosition(
            0f,
            -1850f,
            -3250f
        )
        modelMatrixCamera.invalidatePosition()
        mDrawerLightDirectional.setPosition(
            lx,
            ly,
            lz
        )

        glEnable(
            GL_DEPTH_TEST
        )

        glEnable(
            GL_CULL_FACE
        )

        glCullFace(
            GL_FRONT
        )
    }

    override fun onSurfaceChanged(
        gl: GL10?,
        width: Int,
        height: Int
    ) {
        Log.d(TAG, "onSurfaceChanged: ${Thread.currentThread().name}")
        mWidth = width
        mHeight = height

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
        glViewport(
            0,
            0,
            mWidth,
            mHeight
        )

        glClear(GL_COLOR_BUFFER_BIT or
            GL_DEPTH_BUFFER_BIT
        )

        glClearColor(
            0.0f,
            0.0f,
            0.0f,
            1.0f
        )

        val t = System.currentTimeMillis() % 1000000L * 0.0007f

        mLight.position.run {
            x = cos(t) * 1250f
            y = -1850f
            z = -3250f
        }

        mLight2.position.run {
            x = sin(t) * 1250f
            y = -1850f
            z = -3250f
        }

        val error = glGetError()
        if (error != GL_NO_ERROR) {
            Log.d("TAG", "onDrawFrame: ERROR: ${error.toString(16)}")
            return
        }

        // 1. Camera point triggering needs to check only on self position changes
        // it doesn't need to check on each touch event
        // 2. For other entities who can trigger, check it inside infinite loop
        val model = mCameraFree.modelMatrix
        mTriggers.forEach {
            it.stateManager.trigger(
                model.x,
                model.y,
                model.z
            )
        }

        modelMatrixTrigger.model.run {
            msx = abs(sin(
                System.currentTimeMillis() % 1000000L * 0.001f
            )) * 50f
            invalidateScale()
        }
        modelMatrixTrigger.normal.calculateInvertModel()

        mSwitcherDrawMode
            .currentDrawerMode
            .draw()

        mHandler.run()
    }

    override fun onIntersectPosition(
        p: MGVector
    ) {
        mCallbackOnDeltaInteract.currentMeshInteract?.run {
            setPosition(
                p.x,
                p.y,
                p.z
            )
            invalidatePosition()
        }
    }

    fun onTouchEvent(
        event: MotionEvent
    ) {
        mLayerEditor.onTouchEvent(
            event
        )
    }

    private inline fun createDrawModeSwitcher() = MGClickSwitchDrawMode(
        mHandler,
        mSwitcherDrawMode,
        MGMDrawMode(
            mDrawerModeOpaque,
            mShaderDefault,
            mShaderSky,
            mShaderDefault
        ),
        MGMDrawMode(
            MGDrawerModeSingleShader(
                mShaderWireframe,
                meshSky,
                mCameraFree,
                meshes
            ),
            mShaderWireframe,
            mShaderWireframe
        ),
        MGMDrawMode(
            MGDrawerModeSingleShader(
                mShaderNormals,
                meshSky,
                mCameraFree,
                meshes
            ),
            mShaderNormals,
            mShaderNormals,
            mShaderNormals,
            mShaderNormals
        ),
        MGMDrawMode(
            MGDrawerModeSingleShader(
                mShaderTexCoords,
                meshSky,
                mCameraFree,
                meshes
            ),
            mShaderTexCoords,
            mShaderTexCoords
        )
    )
}