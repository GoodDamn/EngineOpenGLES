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
import good.damn.engine.opengl.camera.MGMMatrix
import good.damn.engine.opengl.drawers.MGDrawerMeshOpaque
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGDrawerModeOpaque
import good.damn.engine.opengl.drawers.MGDrawerModeSwitch
import good.damn.engine.opengl.drawers.MGDrawerModeSingleShader
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.sky.MGDrawerSkyOpaque
import good.damn.engine.opengl.entities.MGMesh
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.generators.MGGeneratorLandscape
import good.damn.engine.opengl.maps.MGMapDisplace
import good.damn.engine.opengl.maps.MGMapNormal
import good.damn.engine.opengl.iterators.vertex.MGVertexIteratorLandscapeDisplace
import good.damn.engine.opengl.iterators.vertex.MGVertexIteratorLandscapeNormal
import good.damn.engine.opengl.models.MGMDrawMode
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.opengl.shaders.MGShaderSkySphere
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.shaders.MGShaderSingleModeNormals
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.opengl.triggers.MGTriggerBase
import good.damn.engine.opengl.triggers.MGTriggerBaseDebug
import good.damn.engine.opengl.triggers.MGTriggerSimple
import good.damn.engine.touch.MGIListenerScale
import good.damn.engine.ui.MGUILayerEditor
import good.damn.engine.ui.clicks.MGClickGenerateLandscape
import good.damn.engine.ui.clicks.MGClickPlaceMesh
import good.damn.engine.ui.clicks.MGClickSwitchDrawMode
import good.damn.engine.ui.seek.MGSeekValueChangedLightAmbient
import good.damn.engine.utils.MGUtilsBuffer
import good.damn.engine.utils.MGUtilsVertIndices
import java.util.concurrent.ConcurrentLinkedQueue

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

    private val modelMatrixSky = MGMMatrix().apply {
        setScale(
            200000f,
            200000f,
            200000f
        )
    }
    private val modelMatrixCamera = MGMMatrix()
    private val modelMatrixLandscape = MGMMatrix()
    private val modelMatrixTrigger = MGMMatrix()
    private val modelMatrixTransformed = MGMMatrix()

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
        modelMatrixLandscape
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
        modelMatrixSky
    )

    private val meshTransformed = MGMesh(
        MGDrawerModeSwitch(
            mVerticesBatchObject,
            MGDrawerMeshOpaque(
                mVerticesBatchObject,
                mTextureLandscape,
                materialLandscape
            ),
            GL_CCW
        ),
        mShaderDefault,
        modelMatrixTransformed
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
        add(meshTransformed)
    }

    private val mTriggers = ConcurrentLinkedQueue<
        MGTriggerBaseDebug
    >()

    private var mWidth = 0
    private var mHeight = 0

    private val mDrawerLightDirectional = MGDrawerLightDirectional(
        mShaderDefault.light
    )

    private val modelMatrixLightMesh = MGMMatrix()

    private val mDrawerModeOpaque = MGDrawerModeOpaque(
        mShaderSky,
        mShaderDefault,
        mShaderWireframe,
        meshSky,
        mCameraFree,
        mDrawerLightDirectional,
        meshes,
        mTriggers
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
            MGTriggerSimple(
                mDrawerLightDirectional,
                mShaderWireframe,
                modelMatrixTrigger,
                MGVector(
                    -100f,
                    -10f,
                    -10f
                ),
                MGVector(
                    100f,
                    10f,
                    10f
                ),
                modelMatrixTransformed
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

            val mapHeight = MGMapDisplace.createFromAssets(
                "maps/terrain_height.png"
            )

            forEachVertex(
                MGVertexIteratorLandscapeNormal(
                    mapNormal
                )
            )
            mapNormal.destroy()

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

        modelMatrixLandscape.addPosition(
            off,
            -5500f,
            off
        )

        modelMatrixLandscape.invalidatePosition()

        meshes.add(
            MGDrawerMeshSwitch(
                mDrawerSwitchBatch,
                MGDrawerPositionEntity(
                    mDrawerSwitchBatch,
                    mShaderDefault,
                    modelMatrixLightMesh
                )
            )
        )

        val lx = 0.0f
        val ly = 0.5f
        val lz = -0.5f
        val m = 2048f
        modelMatrixLightMesh.x = lx * m
        modelMatrixLightMesh.y = ly * m
        modelMatrixLightMesh.z = lz * m
        mDrawerLightDirectional.setPosition(
            lx,
            ly,
            lz
        )
        modelMatrixLightMesh.invalidatePosition()

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
            it.trigger(
                model.x,
                model.y,
                model.z
            )
        }

        modelMatrixTrigger.addRotation(
            0.01f,
            0f
        )

        mSwitcherDrawMode
            .currentDrawerMode
            .draw()

        mHandler.run()
    }

    override fun onIntersectPosition(
        p: MGVector
    ) {
        mCallbackOnDeltaInteract.currentMeshInteract?.run {
            x = p.x
            y = p.y
            z = p.z
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