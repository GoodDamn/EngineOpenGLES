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
import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.callbacks.MGCallbackOnDeltaInteract
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
import good.damn.engine.opengl.models.MGMUserContent
import good.damn.engine.opengl.rays.MGRayIntersection
import good.damn.engine.opengl.shaders.MGIShaderCamera
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.opengl.shaders.MGShaderSkySphere
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.opengl.ui.MGButtonGL
import good.damn.engine.opengl.ui.MGSeekBarGl
import good.damn.engine.touch.MGIListenerMove
import good.damn.engine.touch.MGIListenerDelta
import good.damn.engine.touch.MGIListenerScale
import good.damn.engine.touch.MGTouchFreeMove
import good.damn.engine.touch.MGTouchScale
import java.util.LinkedList
import java.util.concurrent.ConcurrentLinkedQueue

class MGRendererLevelEditor(
    private val requesterUserContent: MGIRequestUserContent
): GLSurfaceView.Renderer,
MGIListenerOnGetUserContent,
MGIListenerScale,
MGIListenerDelta,
MGIListenerMove {

    companion object {
        private const val TAG = "MGRendererLevelEditor"
    }

    private val mShaderDefault = MGShaderDefault()
    private val mShaderSky = MGShaderSkySphere()
    private val mShaderNormals = MGShaderSingleMode()
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

    private val mTouchMove = MGTouchFreeMove().apply {
        setListenerMove(
            this@MGRendererLevelEditor
        )
        setListenerDelta(
            this@MGRendererLevelEditor
        )
    }

    private val mCallbackOnDeltaInteract = MGCallbackOnDeltaInteract()

    private val mTouchScale = MGTouchScale().apply {
        onScale = this@MGRendererLevelEditor
        onDelta = mCallbackOnDeltaInteract
    }

    private val mBtnLoadUserContent = MGButtonGL {
        requesterUserContent.requestUserContent(
            this,
            "*/*"
        )
    }

    private val mBtnSwitchWireframe = MGButtonGL {
        mHandler.post {
            when (MGEngine.drawMode) {
                MGEnumDrawMode.OPAQUE -> {
                    switchDrawMode(
                        MGEnumDrawMode.OPAQUE,
                        mDrawerModeWireframe,
                        mShaderWireframe,
                        mShaderWireframe
                    )
                    MGEngine.drawMode = MGEnumDrawMode.WIREFRAME
                }

                MGEnumDrawMode.WIREFRAME -> {
                    switchDrawMode(
                        MGEnumDrawMode.WIREFRAME,
                        mDrawerModeNormals,
                        mShaderNormals,
                        mShaderNormals
                    )
                    MGEngine.drawMode = MGEnumDrawMode.NORMALS
                }

                MGEnumDrawMode.NORMALS -> {
                    switchDrawMode(
                        MGEnumDrawMode.NORMALS,
                        mDrawerModeTexCoords,
                        mShaderTexCoords,
                        mShaderTexCoords
                    )
                    MGEngine.drawMode = MGEnumDrawMode.TEX_COORDS
                }

                MGEnumDrawMode.TEX_COORDS -> {
                    switchDrawMode(
                        MGEnumDrawMode.TEX_COORDS,
                        mDrawerModeOpaque,
                        mShaderDefault,
                        mShaderSky
                    )
                    MGEngine.drawMode = MGEnumDrawMode.OPAQUE
                }
            }
            val error = glGetError()
            if (error != GL_NO_ERROR) {
                Log.d("TAG", "post: ERROR: ${error.toString(16)}")
                return@post
            }
        }
    }

    private val mBtnPlaceMesh = MGButtonGL {
        if (MGEngine.drawMode != MGEnumDrawMode.OPAQUE) {
            return@MGButtonGL
        }
        placeMesh()
    }

    private val mBarSeekAmbient = MGSeekBarGl {
        mDrawerLightDirectional.ambient = it
    }

    private val mHandler = MGHandlerGl()

    private val mOutPointLead = MGVector(0f)
    private val mPointCamera = MGVector(0f)
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

    private val mRayIntersection = MGRayIntersection()

    private val mDrawerModeOpaque = MGDrawerModeOpaque(
        mShaderSky,
        mShaderDefault,
        meshSky,
        mCameraFree,
        mDrawerLightDirectional,
        meshes
    )

    private val mDrawerModeWireframe = MGDrawerModeSingleShader(
        mShaderWireframe,
        meshSky,
        mCameraFree,
        meshes
    )

    private val mDrawerModeNormals = MGDrawerModeSingleShader(
        mShaderNormals,
        meshSky,
        mCameraFree,
        meshes
    )

    private val mDrawerModeTexCoords = MGDrawerModeSingleShader(
        mShaderTexCoords,
        meshSky,
        mCameraFree,
        meshes
    )

    private var mCurrentDrawerMode: MGIDrawer = mDrawerModeOpaque

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

        /*mCameraRotation.radius = 1250f
        mTouchScale.scale = mCameraRotation.radius

        mCameraRotation.setRotation(
            0f,
            0.01f
        )*/

        mTextureLandscape.setupTexture(
            "textures/terrain.png",
            GL_REPEAT
        )

        mTextureSky.setupTexture(
            "textures/sky/skysphere_light.jpg"
        )

        MGObject3D.createFromAssets(
            "objs/semi_sphere.obj"
        ).run {
            mVerticesSky.configure(
                vertices,
                indices
            )
        }

        mGeneratorLandscape.apply {
            setResolution(
                1024,
                1024
            )

            displace(
                MGMapDisplace.createFromAssets(
                    "maps/terrain_height.png"
                )
            )
        }

        modelMatrixLandscape.setScale(
            3.0f,
            3.0f,
            3.0f
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

        val w: Float
        val h: Float

        if (width < height) {
            w = fWidth
            h = fHeight
        } else {
            w = fHeight
            h = fWidth
        }

        mTouchMove.setBoundsMove(
            0f,
            0f,
            fWidth * 0.5f,
            fHeight
        )

        val btnLen = w * 0.1f

        mTouchMove.setBoundsDelta(
            fWidth * 0.5f,
            0f,
            fWidth,
            fHeight
        )

        mBarSeekAmbient.bounds(
            0f,
            0f,
            btnLen,
            fHeight
        )

        mBtnSwitchWireframe.bounds(
            fWidth - btnLen,
            fHeight - btnLen,
            btnLen,
            btnLen
        )

        mBtnLoadUserContent.bounds(
            fWidth - btnLen,
            0f,
            btnLen,
            btnLen
        )

        val btnLen2 = btnLen
        val midX = (fWidth - btnLen2) * 0.5f
        val midY = (fHeight - btnLen2) * 0.5f
        mBtnPlaceMesh.bounds(
            midX,
            fHeight - btnLen2,
            btnLen2,
            btnLen2
        )

        mTouchScale.setBounds(
            midX, midY,
            midX + btnLen2,
            midY + btnLen2
        )
    }

    override fun onDrawFrame(
        gl: GL10?
    ) {
        /*val f = System.currentTimeMillis() % 100000L * 0.001f
        val fx = sin(f) * 840f
        val fz = cos(f) * 840f

        mDrawerLightDirectional.setPosition(
            fx, 600f, fz
        )*/

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
        mCurrentDrawerMode.draw()

        mHandler.run()
    }

    override fun onGetUserContent(
        userContent: MGMUserContent
    ) {
        val mapDisplace = MGMapDisplace.createFromStream(
            userContent.stream
        )

        Handler(
            Looper.getMainLooper()
        ).post {
            mHandler.post {
                mGeneratorLandscape.displace(
                    mapDisplace
                )
            }
        }
    }

    fun onTouchEvent(
        event: MotionEvent
    ) {
        if (event.action == MotionEvent.ACTION_DOWN) {
            mBarSeekAmbient.intercept(event.x, event.y)
            mBtnLoadUserContent.intercept(event.x, event.y)
            mBtnSwitchWireframe.intercept(event.x, event.y)
            mBtnPlaceMesh.intercept(event.x, event.y)

            mTouchScale.onTouchEvent(
                event
            )

            if (mTouchScale.touchId != -1) {
                return
            }

            mTouchMove.onTouchEvent(
                event
            )
            return
        }

        mTouchScale.onTouchEvent(
            event
        )

        mTouchMove.onTouchEvent(
            event
        )
    }

    override fun onDelta(
        dx: Float,
        dy: Float
    ) {
        mCameraFree.addRotation(
            dx * 0.001f,
            dy * 0.001f
        )
        mCameraFree.invalidatePosition()
        updateIntersection()
    }

    override fun onScale(
        scale: Float
    ) {
        Log.d(TAG, "onScale: $scale")
    }

    override fun onMove(
        x: Float,
        y: Float,
        directionX: Float,
        directionY: Float
    ) {
        mCameraFree.addPosition(
            x, y,
            directionX,
            directionY
        )
        mCameraFree.invalidatePosition()
        updateIntersection()
    }

    private inline fun updateIntersection() {
        mPointCamera.x = modelMatrixCamera.x
        mPointCamera.y = modelMatrixCamera.y
        mPointCamera.z = modelMatrixCamera.z

        mRayIntersection.intersect(
            mPointCamera,
            mCameraFree.direction,
            mOutPointLead
        )

        invalidatePositionInteract()
    }

    private fun switchDrawMode(
        drawMode: MGEnumDrawMode,
        drawerMode: MGIDrawer,
        shader: MGIShaderCamera,
        shaderSky: MGIShaderCamera
    ) {
        mCurrentDrawerMode = drawerMode

        meshSky.switchDrawMode(
            shaderSky,
            drawMode
        )

        meshes.forEach {
            it.switchDrawMode(
                shader,
                drawMode
            )
        }
    }

    private inline fun placeMesh() {
        if (mCallbackOnDeltaInteract.currentMeshInteract != null) {
            mCallbackOnDeltaInteract.currentMeshInteract = null
            return
        }

        val modelMatrix = MGMMatrix().apply {
            setScale(
                0.01f,
                0.01f,
                0.01f
            )
        }
        mCallbackOnDeltaInteract.currentMeshInteract = modelMatrix
        meshes.add(
            MGDrawerMeshSwitch(
                mDrawerSwitchBatch,
                MGDrawerPositionEntity(
                    mDrawerSwitchBatch,
                    mShaderDefault,
                    modelMatrix
                )
            )
        )

        invalidatePositionInteract()
    }

    private inline fun invalidatePositionInteract() {
        mCallbackOnDeltaInteract.currentMeshInteract?.run {
            x = mOutPointLead.x
            y = mOutPointLead.y
            z = mOutPointLead.z
            invalidatePosition()
        }
    }
}