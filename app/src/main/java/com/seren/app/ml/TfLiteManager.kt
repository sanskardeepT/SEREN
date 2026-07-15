package com.seren.app.ml

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * TfLiteManager handles loading TFLite models from assets.
 * Contains safety fallbacks: if model assets are missing, runs heuristic rules
 * to prevent crashes and return consistent screening indicators.
 */
class TfLiteManager private constructor(private val context: Context) {

    private var drawnetInterpreter: Interpreter? = null
    private var gazenetInterpreter: Interpreter? = null
    private var phonnetInterpreter: Interpreter? = null
    private var attentnetInterpreter: Interpreter? = null
    private var emotnetInterpreter: Interpreter? = null
    private var spatialnetInterpreter: Interpreter? = null

    @Synchronized
    private fun getDrawNetInterpreter(): Interpreter? {
        if (drawnetInterpreter == null) {
            drawnetInterpreter = loadModelFromAssets("seren_drawnet.tflite")
        }
        return drawnetInterpreter
    }

    @Synchronized
    private fun getGazeNetInterpreter(): Interpreter? {
        if (gazenetInterpreter == null) {
            gazenetInterpreter = loadModelFromAssets("seren_gazenet.tflite")
        }
        return gazenetInterpreter
    }

    @Synchronized
    private fun getPhonNetInterpreter(): Interpreter? {
        if (phonnetInterpreter == null) {
            phonnetInterpreter = loadModelFromAssets("seren_phonnet.tflite")
        }
        return phonnetInterpreter
    }

    @Synchronized
    private fun getAttentNetInterpreter(): Interpreter? {
        if (attentnetInterpreter == null) {
            attentnetInterpreter = loadModelFromAssets("seren_attentnet.tflite")
        }
        return attentnetInterpreter
    }

    @Synchronized
    private fun getEmotNetInterpreter(): Interpreter? {
        if (emotnetInterpreter == null) {
            emotnetInterpreter = loadModelFromAssets("seren_emotnet.tflite")
        }
        return emotnetInterpreter
    }

    @Synchronized
    private fun getSpatialNetInterpreter(): Interpreter? {
        if (spatialnetInterpreter == null) {
            spatialnetInterpreter = loadModelFromAssets("seren_spatialnet.tflite")
        }
        return spatialnetInterpreter
    }

    private fun loadModelFromAssets(fileName: String): Interpreter? {
        return try {
            val assetFileDescriptor: AssetFileDescriptor = context.assets.openFd(fileName)
            val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
            val fileChannel = fileInputStream.channel
            val startOffset = assetFileDescriptor.startOffset
            val declaredLength = assetFileDescriptor.declaredLength
            val buffer: MappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
            val options = Interpreter.Options().apply {
                setNumThreads(4)
                setUseXNNPACK(true)
            }
            Interpreter(buffer, options)
        } catch (e: Exception) {
            Log.w("TfLiteManager", "Model file $fileName not found or corrupt: ${e.message}. Using heuristic fallback.")
            null
        }
    }

    /**
     * Runs inference on DrawNet (efficientnet base image input for letter reversals).
     * Input: float array of shape [1, 224, 224, 3] representing drawing pixels.
     * Output: float array of shape [1, 3] representing probabilities for Normal, Reversal, Corrected.
     */
    fun runDrawNet(inputImage: Array<Array<Array<FloatArray>>>): FloatArray {
        require(inputImage.size == 1 && inputImage[0].size == 224 && inputImage[0][0].size == 224 && inputImage[0][0][0].size == 3) {
            "DrawNet input image tensor must be of shape [1, 224, 224, 3]"
        }
        val interpreter = getDrawNetInterpreter()
        val output = Array(1) { FloatArray(3) }
        
        if (interpreter != null) {
            try {
                interpreter.run(inputImage, output)
                Log.i(TAG, "DrawNet: TFLite model inference succeeded.")
                return output[0]
            } catch (e: Exception) {
                Log.e(TAG, "Error running DrawNet TFLite: ${e.message}")
            }
        }
        
        Log.i(TAG, "DrawNet: FALLBACK — using heuristic scorer (no trained model loaded).")
        return HeuristicScorers.scoreDrawNet(inputImage)
    }

    /**
     * Runs inference on GazeNet LSTM (sequence eye-movement coordinates).
     * Input: float array of shape [1, 100, 6] representing [x, y, dx, dy, speed, is_fixation] over 100 timesteps.
     * Output: float array of shape [1, 1] representing dyslexia risk probability (0.0 to 1.0).
     */
    fun runGazeNet(gazeSequence: Array<Array<FloatArray>>): Float {
        require(gazeSequence.size == 1 && gazeSequence[0].size == 100 && gazeSequence[0][0].size == 6) {
            "GazeNet input gaze sequence tensor must be of shape [1, 100, 6]"
        }
        val interpreter = getGazeNetInterpreter()
        val output = Array(1) { FloatArray(1) }
        
        if (interpreter != null) {
            try {
                interpreter.run(gazeSequence, output)
                Log.i(TAG, "GazeNet: TFLite model inference succeeded.")
                return output[0][0]
            } catch (e: Exception) {
                Log.e(TAG, "Error running GazeNet TFLite: ${e.message}")
            }
        }
        
        Log.i(TAG, "GazeNet: FALLBACK — using heuristic scorer (no trained model loaded).")
        return HeuristicScorers.scoreGazeNet(gazeSequence)
    }

    /**
     * Runs inference on PhonNet (stuttering/disfluency audio classification).
     * Input: float array of shape [1, 48000] representing 3 seconds of 16kHz audio sample.
     * Output: float array of shape [1, 4] representing classes: Repetition, Prolongation, Block, Fluent.
     */
    fun runPhonNet(audioData: FloatArray): FloatArray {
        require(audioData.size == 48000) {
            "PhonNet input audio sequence must be exactly 48000 samples (3 seconds at 16kHz)"
        }
        val interpreter = getPhonNetInterpreter()
        val output = Array(1) { FloatArray(4) }
        
        if (interpreter != null) {
            try {
                interpreter.run(audioData, output)
                Log.i(TAG, "PhonNet: TFLite model inference succeeded.")
                return output[0]
            } catch (e: Exception) {
                Log.e(TAG, "Error running PhonNet TFLite: ${e.message}")
            }
        }
        
        Log.i(TAG, "PhonNet: FALLBACK — using heuristic scorer (no trained model loaded).")
        return HeuristicScorers.scorePhonNet(audioData)
    }

    /**
     * Runs inference on AttentNet MLP (ADHD behavioral classification).
     * Input: float array of shape [1, 4] representing:
     *   [miss_rate, commission_rate, rt_variability, gaze_off_task_pct]
     * Output: float array of shape [1, 4] representing classes: Control, Inattentive, Hyperactive, Combined.
     */
    fun runAttentNet(behaviorStats: FloatArray): FloatArray {
        require(behaviorStats.size == 4) {
            "AttentNet input behaviorStats must be of size 4"
        }
        val interpreter = getAttentNetInterpreter()
        val output = Array(1) { FloatArray(4) }
        
        if (interpreter != null) {
            try {
                interpreter.run(behaviorStats, output)
                Log.i(TAG, "AttentNet: TFLite model inference succeeded.")
                return output[0]
            } catch (e: Exception) {
                Log.e(TAG, "Error running AttentNet TFLite: ${e.message}")
            }
        }
        
        Log.i(TAG, "AttentNet: FALLBACK — using heuristic scorer (no trained model loaded).")
        return HeuristicScorers.scoreAttentNet(behaviorStats)
    }

    /**
     * Runs inference on EmotNet DistilBERT (sentiment and worry classification from transcripts).
     * Input: String transcript text.
     * Output: float array of shape [1, 4] representing classes: Control, Worry, Perfectionism, Sadness.
     */
    fun runEmotNet(textInput: String): FloatArray {
        require(textInput.isNotBlank()) {
            "EmotNet input text must not be empty or blank"
        }
        val interpreter = getEmotNetInterpreter()
        
        val output = Array(1) { FloatArray(4) }
        if (interpreter != null) {
            try {
                val mockInputIds = Array(1) { IntArray(64) { 0 } }
                val words = textInput.lowercase().split(" ")
                for (i in 0 until minOf(words.size, 64)) {
                    mockInputIds[0][i] = words[i].hashCode() % 30000
                }
                val mockMask = Array(1) { IntArray(64) { if (it < words.size) 1 else 0 } }
                
                val inputs = arrayOf(mockInputIds, mockMask)
                interpreter.run(inputs, output)
                Log.i(TAG, "EmotNet: TFLite model inference succeeded.")
                return output[0]
            } catch (e: Exception) {
                Log.e(TAG, "Error running EmotNet TFLite: ${e.message}")
            }
        }
        
        Log.i(TAG, "EmotNet: FALLBACK — using heuristic scorer (no trained model loaded).")
        return HeuristicScorers.scoreEmotNet(textInput)
    }

    /**
     * Runs inference on SpatialNet MLP (Corsi recall patterns and touch coordinates paths).
     * Input: float array of shape [1, 4] representing:
     *   [corsi_span, planning_time_ms, error_count, sequence_length]
     * Output: float array of shape [1, 4] representing classes: Control, Memory Deficit, Executive Deficit, Combined.
     */
    fun runSpatialNet(spatialStats: FloatArray): FloatArray {
        require(spatialStats.size == 4) {
            "SpatialNet input spatialStats must be of size 4"
        }
        val interpreter = getSpatialNetInterpreter()
        val output = Array(1) { FloatArray(4) }
        
        if (interpreter != null) {
            try {
                interpreter.run(spatialStats, output)
                Log.i(TAG, "SpatialNet: TFLite model inference succeeded.")
                return output[0]
            } catch (e: Exception) {
                Log.e(TAG, "Error running SpatialNet TFLite: ${e.message}")
            }
        }
        
        Log.i(TAG, "SpatialNet: FALLBACK — using heuristic scorer (no trained model loaded).")
        return HeuristicScorers.scoreSpatialNet(spatialStats)
    }

    suspend fun runDrawNetAsync(inputImage: Array<Array<Array<FloatArray>>>): FloatArray = withContext(Dispatchers.Default) {
        runDrawNet(inputImage)
    }

    suspend fun runGazeNetAsync(gazeSequence: Array<Array<FloatArray>>): Float = withContext(Dispatchers.Default) {
        runGazeNet(gazeSequence)
    }

    suspend fun runPhonNetAsync(audioData: FloatArray): FloatArray = withContext(Dispatchers.Default) {
        runPhonNet(audioData)
    }

    suspend fun runAttentNetAsync(behaviorStats: FloatArray): FloatArray = withContext(Dispatchers.Default) {
        runAttentNet(behaviorStats)
    }

    suspend fun runEmotNetAsync(textInput: String): FloatArray = withContext(Dispatchers.Default) {
        runEmotNet(textInput)
    }

    suspend fun runSpatialNetAsync(spatialStats: FloatArray): FloatArray = withContext(Dispatchers.Default) {
        runSpatialNet(spatialStats)
    }

    @Synchronized
    fun close() {
        try {
            drawnetInterpreter?.close()
            gazenetInterpreter?.close()
            phonnetInterpreter?.close()
            attentnetInterpreter?.close()
            emotnetInterpreter?.close()
            spatialnetInterpreter?.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error closing TFLite interpreters: ${e.message}")
        } finally {
            drawnetInterpreter = null
            gazenetInterpreter = null
            phonnetInterpreter = null
            attentnetInterpreter = null
            emotnetInterpreter = null
            spatialnetInterpreter = null
        }
    }

    companion object {
        private const val TAG = "TfLiteManager"

        @Volatile
        private var INSTANCE: TfLiteManager? = null

        fun getInstance(context: Context): TfLiteManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: TfLiteManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
