package com.example.elormovpmdm.ui.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.elormovpmdm.BaseActivity
import com.example.elormovpmdm.R
import com.example.elormovpmdm.databinding.ActivityCameraBinding

/**
 * Actividad encargada de gestionar la captura de fotografías utilizando la API CameraX.
 * Permite la previsualización en tiempo real, captura, rotación de imagen y guardado en galería.
 */
class CameraActivity : BaseActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var lensFacing = CameraSelector.DEFAULT_BACK_CAMERA
    private var camera: androidx.camera.core.Camera? = null
    private var isFlashOn = false
    private var capturedBitmap: Bitmap? = null

    /**
     * Manejador de la solicitud de permisos de cámara.
     * Si el usuario acepta, inicia la cámara; de lo contrario, muestra un mensaje de error.
     */
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(
                this,
                R.string.permision_denied,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /**
         * Verificación inicial de permisos al crear la actividad.
         */
        if (checkCameraPermission()) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }

        initComponents()
    }

    /**
     * Configura y vincula los casos de uso de CameraX (Preview e ImageCapture)
     * al ciclo de vida de la actividad.
     */
    private fun startCamera () {
        val cameraProviderFuture  = ProcessCameraProvider.getInstance(this)

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetRotation(binding.previewCamera.display.rotation)
            .build()

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewCamera.surfaceProvider)
            }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, lensFacing, preview, imageCapture)
            } catch (e: Exception) {
                Log.i("GVA", "Error al abrir la camara")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    /**
     * Inicializa los listeners de los botones de la interfaz:
     * Flash, Obturador, Cambio de cámara, Confirmar y Cancelar.
     */
    private fun initComponents() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        /**
         * Control del estado del flash (Torcha).
         */
        binding.btnFlash.setOnClickListener {
            if (camera?.cameraInfo?.hasFlashUnit() == true) {
                isFlashOn = !isFlashOn
                camera?.cameraControl?.enableTorch(isFlashOn)
                val icon = if (isFlashOn) {
                    R.drawable.flash_on_ic
                } else {
                    R.drawable.flash_off_ic
                }
                binding.btnFlash.setImageResource(icon)
            }
        }

        /**
         * Lógica de disparo de la fotografía.
         */
        binding.btnTakePhoto.setOnClickListener {
            val imageCapture = imageCapture ?: return@setOnClickListener

            imageCapture.takePicture(
                ContextCompat.getMainExecutor(this),
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        showPreview(image)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.i("GVA", "Error: ${exception.message}")
                    }
                }
            )
        }

        /**
         * Alterna entre la cámara frontal y la trasera.
         */
        binding.btnCameraSwitch.setOnClickListener {
            lensFacing = if (lensFacing == CameraSelector.DEFAULT_BACK_CAMERA) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
            startCamera()
        }

        binding.btnCancell.setOnClickListener {
            setPreviewMode(false)
        }

        binding.btnConfirm.setOnClickListener {
            capturedBitmap?.let { bitmap ->
                saveImage(bitmap)
                finish()
            }
        }
    }

    /**
     * Convierte la imagen capturada (ImageProxy) a un Bitmap,
     * aplica la rotación necesaria y lo muestra en pantalla.
     */
    private fun showPreview(image: ImageProxy) {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)

        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        val matrix = Matrix().apply {
            postRotate(image.imageInfo.rotationDegrees.toFloat())
        }

        val rotatedBitmap = Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
        )

        capturedBitmap = rotatedBitmap

        runOnUiThread {
            binding.ivImage.setImageBitmap(rotatedBitmap)
            setPreviewMode(true)
        }

        image.close()
    }

    /**
     * Cambia la visibilidad de los elementos de la UI dependiendo de si se está
     * previsualizando la captura o usando la cámara en vivo.
     */
    private fun setPreviewMode(isPreviewing: Boolean) {
        if (isPreviewing) {
           binding.previewCamera.visibility = View.INVISIBLE
           binding.flButton.visibility = View.INVISIBLE

           binding.ivImage.visibility = View.VISIBLE
           binding.flConfirmation.visibility = View.VISIBLE
        } else {
            binding.previewCamera.visibility = View.VISIBLE
            binding.flButton.visibility = View.VISIBLE

            binding.ivImage.visibility = View.INVISIBLE
            binding.flConfirmation.visibility = View.INVISIBLE
        }
    }

    /**
     * Comprueba si el permiso de cámara ha sido otorgado por el sistema.
     */
    private fun checkCameraPermission(): Boolean {
        return PermissionChecker.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PermissionChecker.PERMISSION_GRANTED
    }

    /**
     * Guarda el Bitmap resultante en el almacenamiento externo (Galería)
     * utilizando MediaStore API.
     */
    private fun saveImage(bitmap: Bitmap) {
        val name = "IMG_${System.currentTimeMillis()}.jpg"
        val contentValues = android.content.ContentValues().apply {
            put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(android.provider.MediaStore.Images.Media.RELATIVE_PATH, "Pictures/ElorMov")
        }

        val contentResolver = contentResolver
        val uri = contentResolver.insert(
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
        )

        uri?.let { targetUri ->
            try {
                contentResolver.openOutputStream(targetUri)?.use { outputStream ->
                    if (bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                        Toast.makeText(this, "Imagen guardada en galería", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("GVA", "Error al guardar: ${e.message}")
            }
        }
    }
}