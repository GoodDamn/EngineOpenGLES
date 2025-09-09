package good.damn.engine.opengl.renderer

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES30.*
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import good.damn.engine.MGEngine
import good.damn.engine.interfaces.MGIListenerOnGetUserContent
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
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.drawers.sky.MGDrawerSkyOpaque
import good.damn.engine.opengl.entities.MGMesh
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.generators.MGGeneratorLandscape
import good.damn.engine.opengl.maps.MGMapDisplace
import good.damn.engine.opengl.maps.MGMapNormal
import good.damn.engine.opengl.models.MGMDrawMode
import good.damn.engine.opengl.models.MGMUserContent
import good.damn.engine.opengl.shaders.MGIShaderCamera
import good.damn.engine.opengl.shaders.MGIShaderNormal
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.opengl.shaders.MGShaderSkySphere
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.shaders.MGShaderSingleModeNormals
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.touch.MGIListenerScale
import good.damn.engine.touch.MGTouchFreeMove
import good.damn.engine.touch.MGTouchScale
import good.damn.engine.ui.MGIClick
import good.damn.engine.ui.MGIListenerValueChanged
import good.damn.engine.ui.MGUILayerEditor
import good.damn.engine.ui.clicks.MGClickGenerateLandscape
import good.damn.engine.ui.clicks.MGClickPlaceMesh
import good.damn.engine.ui.clicks.MGClickSwitchDrawMode
import good.damn.engine.ui.seek.MGSeekValueChangedLightAmbient
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class MGRendererLevelEditor(
    private val requesterUserContent: MGIRequestUserContent
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

    private var mWidth = 0
    private var mHeight = 0

    private val mDrawerLightDirectional = MGDrawerLightDirectional(
        mShaderDefault.light
    )

    private val modelMatrixLightMesh = MGMMatrix()

    private val mDrawerModeOpaque = MGDrawerModeOpaque(
        mShaderSky,
        mShaderDefault,
        meshSky,
        mCameraFree,
        mDrawerLightDirectional,
        meshes
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
        seekAmbientChanged = MGSeekValueChangedLightAmbient(
            mDrawerLightDirectional
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
            "objs/house.obj"
        ).run {
            mVerticesBatchObject.configure(
                vertices,
                indices
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

            displace(
                MGMapDisplace.createFromAssets(
                    "maps/terrain_height.png"
                ),
                MGMapNormal.createFromAssets(
                    "maps/normal/terrain_normal.png"
                )
            )
        }

        modelMatrixLandscape.setScale(
            3.0f,
            3.0f,
            3.0f
        )


        val off = landSize / -2f * 3f

        modelMatrixLandscape.addPosition(
            off,
            0f,
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
        val t = System.currentTimeMillis() % 1000000L * 0.001f
        val m = 2048f
        val s = sin(t)
        val c = cos(t)
        modelMatrixLightMesh.x = 0.0f
        modelMatrixLightMesh.y = c * m
        modelMatrixLightMesh.z = s * m
        mDrawerLightDirectional.setPosition(
            0.0f,
            c,
            s
        )
        modelMatrixLightMesh.invalidatePosition()

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