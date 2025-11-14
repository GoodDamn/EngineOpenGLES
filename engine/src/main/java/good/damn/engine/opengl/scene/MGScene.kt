package good.damn.engine.opengl.scene

import android.opengl.GLES30
import android.opengl.GLES30.GL_CW
import android.opengl.GLES30.GL_REPEAT
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import good.damn.engine.imports.MGImportA3D
import good.damn.engine.imports.MGImportLevel
import good.damn.engine.imports.MGImportMesh
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.opengl.objects.MGObject3d
import good.damn.engine.opengl.MGSwitcherDrawMode
import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
import good.damn.engine.opengl.callbacks.MGCallbackOnCameraMovement
import good.damn.engine.opengl.callbacks.MGCallbackOnDeltaInteract
import good.damn.engine.opengl.callbacks.MGCallbackOnScale
import good.damn.engine.opengl.callbacks.MGIListenerOnIntersectPosition
import good.damn.engine.opengl.camera.MGCameraFree
import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
import good.damn.engine.opengl.drawers.instance.MGDrawerMeshInstanced
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialSwitch
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGDrawerMeshTextureSwitch
import good.damn.engine.opengl.drawers.modes.MGDrawModeOpaque
import good.damn.engine.opengl.drawers.modes.MGDrawModeSingleMap
import good.damn.engine.opengl.drawers.modes.MGDrawModeSingleShader
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.drawers.modes.MGDrawModeSingleShaderNormals
import good.damn.engine.opengl.entities.MGLight
import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.managers.MGManagerLight
import good.damn.engine.opengl.managers.MGManagerTriggerLight
import good.damn.engine.opengl.managers.MGManagerTriggerMesh
import good.damn.engine.opengl.matrices.MGMatrixScale
import good.damn.engine.opengl.matrices.MGMatrixTranslate
import good.damn.engine.opengl.models.MGMShader
import good.damn.engine.opengl.pools.MGPoolMeshesStatic
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.opengl.shaders.MGShaderOpaque
import good.damn.engine.opengl.shaders.MGShaderSingleMap
import good.damn.engine.opengl.shaders.MGShaderSingleMapInstanced
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.shaders.MGShaderSingleModeInstanced
import good.damn.engine.opengl.shaders.MGShaderSingleModeNormals
import good.damn.engine.opengl.shaders.MGShaderSkySphere
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.opengl.triggers.MGTriggerLight
import good.damn.engine.opengl.triggers.MGTriggerSimple
import good.damn.engine.opengl.triggers.methods.MGTriggerMethodBox
import good.damn.engine.runnables.MGCallbackModelSpawn
import good.damn.engine.threads.MGHandlerCollision
import good.damn.engine.ui.MGUILayerEditor
import good.damn.engine.ui.clicks.MGClickImport
import good.damn.engine.ui.clicks.MGClickPlaceMesh
import good.damn.engine.ui.clicks.MGClickSwitchDrawMode
import good.damn.engine.ui.clicks.MGClickTriggerDrawingFlag
import good.damn.engine.utils.MGUtilsBuffer
import good.damn.engine.utils.MGUtilsVertIndices
import java.util.concurrent.ConcurrentLinkedQueue
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.random.Random

class MGScene(
    requesterUserContent: MGIRequestUserContent,
    shaderOpaque: MGMShader<MGShaderDefault, MGShaderOpaque>,
    shaderSky: MGShaderSkySphere,
    private val shaderNormals: MGMShader<MGShaderSingleModeNormals, MGShaderSingleModeInstanced>,
    private val shaderTexCoords: MGMShader<MGShaderSingleMode, MGShaderSingleModeInstanced>,
    private val shaderWireframe: MGMShader<MGShaderSingleMode, MGShaderSingleModeInstanced>,
    private val shaderMapEmissive: MGMShader<MGShaderSingleMap, MGShaderSingleMapInstanced>
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

    private val mVerticesSky = MGArrayVertexConfigurator(
        MGEnumArrayVertexConfiguration.SHORT
    )

    private val mVerticesDebugBox = MGArrayVertexConfigurator(
        MGEnumArrayVertexConfiguration.BYTE
    )

    private val mVerticesDebugSphere = MGArrayVertexConfigurator(
        MGEnumArrayVertexConfiguration.BYTE
    )

    private val mDrawerDebugBox = MGDrawerVertexArray(
        mVerticesDebugBox
    )

    private val mDrawerDebugSphere = MGDrawerVertexArray(
        mVerticesDebugSphere
    )

    private val mTextureSky = MGTexture(
        MGEnumTextureType.DIFFUSE
    )

    private val mTextureDefault = MGTexture(
        MGEnumTextureType.DIFFUSE
    )

    private val mTextureMetallicNo = MGTexture(
        MGEnumTextureType.METALLIC
    )

    private val mTextureEmissiveNo = MGTexture(
        MGEnumTextureType.EMISSIVE
    )

    private val mBridgeMatrix = MGBridgeRayIntersect()

    private val meshSky = MGDrawerMeshSwitch(
        MGDrawerVertexArray(
            mVerticesSky
        ),
        MGDrawerPositionEntity(
            modelMatrixSky
        ),
        GL_CW
    ).run {
        MGDrawerMeshTextureSwitch(
            mTextureSky,
            mTextureMetallicNo,
            mTextureEmissiveNo,
            this
        )
    }

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
        MGDrawerMeshMaterialSwitch
    >()

    private val meshesInstanced = ConcurrentLinkedQueue<
        MGDrawerMeshInstanced
    >()

    private val mDrawerLightDirectional = MGDrawerLightDirectional()

    private val managerLights = MGManagerLight(
        shaderOpaque.single
    )

    private val managerTriggerLight = MGManagerTriggerLight(
        managerLights,
        mDrawerDebugSphere
    )

    private val managerTrigger = MGManagerTriggerMesh(
        mDrawerDebugBox
    )

    private val mHandlerCollision = MGHandlerCollision(
        managerTrigger,
        managerTriggerLight,
        mCameraFree
    )

    private val mDrawerModeOpaque = MGDrawModeOpaque(
        shaderSky,
        shaderOpaque,
        shaderWireframe.single,
        meshSky,
        mCameraFree,
        mDrawerLightDirectional,
        meshes,
        meshesInstanced,
        arrayOf(
            managerTrigger,
            managerTriggerLight
        ),
        managerLights
    )

    private val mSwitcherDrawMode = MGSwitcherDrawMode(
        meshSky,
        meshes,
        meshesInstanced,
        mDrawerModeOpaque
    )

    private val mPoolTextures = MGPoolTextures(
        mTextureDefault,
        mTextureMetallicNo,
        mTextureEmissiveNo
    )

    private val mPoolMeshes = MGPoolMeshesStatic()
    private val mCallbackModelSpawn = MGCallbackModelSpawn(
        mBridgeMatrix,
        MGTriggerSimple(
            mDrawerLightDirectional
        ),
        managerTrigger,
        meshes,
        mPoolTextures,
        mPoolMeshes
    )

    private val mLayerEditor = MGUILayerEditor(
        clickLoadUserContent = MGClickImport(
            mHandler,
            MGImportLevel(
                meshesInstanced,
                mPoolTextures
            ),
            MGImportMesh(
                mPoolMeshes,
                mCallbackModelSpawn
            ),
            MGImportA3D(
                mPoolMeshes,
                mCallbackModelSpawn
            ),
            requesterUserContent
        ),
        clickPlaceMesh = MGClickPlaceMesh(
            mBridgeMatrix
        ),
        clickSwitchDrawerMode = createDrawModeSwitcher(),
        clickTriggerDrawing = MGClickTriggerDrawingFlag(
            mDrawerModeOpaque
        )
    ).apply {
        setListenerTouchMove(
            mCallbackOnCameraMove
        )

        setListenerTouchDelta(
            mCallbackOnCameraMove
        )

        setListenerTouchScaleInteract(
            MGCallbackOnScale(
                mBridgeMatrix
            )
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
                MGUtilsBuffer.createByte(
                    MGUtilsVertIndices.createCubeIndices()
                ),
                stride = 3 * 4
            )
        }

        mVerticesDebugSphere.apply {
            val obj = MGUtilsVertIndices.createSphere(
                23
            )

            configure(
                obj.second,
                obj.first,
                stride = 3 * 4,
            )
        }

        for (i in 0 until 10) {
            MGTriggerLight.createFromLight(
                MGLight(
                    MGVector(
                        0.5f + Random.nextFloat() * 0.5f,
                        0.5f + Random.nextFloat() * 0.5f,
                        0.5f + Random.nextFloat() * 0.5f
                    )
                )
            ).run {
                matrix.setPosition(
                    1500f * (i + 1),
                    -1850f,
                    -3250f,
                )
                matrix.radius = 1250f
                matrix.invalidatePosition()
                matrix.invalidateRadius()
                matrix.calculateInvertTrigger()

                managerTriggerLight.addTrigger(
                    triggerState
                )
            }
        }

        mTextureMetallicNo.setupTexture(
            "textures/black.jpg",
            GL_REPEAT
        )

        mTextureEmissiveNo.setupTexture(
            "textures/black.jpg",
            GL_REPEAT
        )

        mTextureDefault.setupTexture(
            "textures/white.jpg"
        )

        mTextureSky.setupTexture(
            "textures/sky/night.png"
        )

        MGObject3d.createFromAssets(
            "objs/semi_sphere.obj"
        )?.get(0)?.run {
            mVerticesSky.configure(
                vertices,
                indices,
                MGArrayVertexConfigurator.STRIDE
            )
        }

        modelMatrixCamera.setPosition(
            0f, 0f, 0f
        )
        modelMatrixCamera.invalidatePosition()
        mDrawerLightDirectional.setPosition(
            2.8929129f,
            10.986f,
            -9.247298f
        )

        mHandlerCollision.start()
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
        mDrawerModeOpaque,
        MGDrawModeSingleShader(
            shaderWireframe,
            meshSky,
            mCameraFree,
            meshes,
            meshesInstanced
        ),
        MGDrawModeSingleShaderNormals(
            shaderNormals,
            meshSky,
            mCameraFree,
            meshes,
            meshesInstanced
        ),
        MGDrawModeSingleShader(
            shaderTexCoords,
            meshSky,
            mCameraFree,
            meshes,
            meshesInstanced
        ),
        MGDrawModeSingleMap(
            shaderMapEmissive,
            mCameraFree,
            meshes,
            meshesInstanced
        )
    )
}