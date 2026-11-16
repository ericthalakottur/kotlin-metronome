package com.ericthalakottur.metronome

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ericthalakottur.metronome.ui.theme.MetronomeTheme
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.ranges.rangeTo
import kotlin.time.Duration.Companion.milliseconds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MetronomeTheme {
                Metronome()
            }
        }
    }
}

@Composable
fun Metronome() {
    val minBPM = 30f
    val maxBPM = 200f
    var isPlaying by remember { mutableStateOf(false) }
    val textColor = Color(0xFF59FFEE)
    var bpm by remember { mutableFloatStateOf(90f) }
    val toneGen = remember { ToneGenerator(AudioManager.STREAM_MUSIC, 100) }

    fun handleButtonClick() {
        isPlaying = !isPlaying
    }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            toneGen.startTone(ToneGenerator.TONE_PROP_BEEP2, 70)

            val currentBPM =
                bpm.roundToInt().coerceAtLeast(minBPM.toInt()).coerceAtMost(maxBPM.toInt())
            val delayMilliseconds = 60_000L / currentBPM
            delay(delayMilliseconds.milliseconds)
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Metronome", color = textColor, fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "BPM ${bpm.toInt()}", color = textColor, fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Slider(
                value = bpm,
                onValueChange = { bpm = it.toInt().toFloat() },
                valueRange = minBPM..maxBPM,
                steps = (maxBPM - minBPM).toInt() - 1,
                colors = SliderDefaults.colors(
                    thumbColor = textColor,
                    activeTrackColor = textColor,
                    inactiveTrackColor = textColor.copy(alpha = 0.24f)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { handleButtonClick() }, colors = ButtonDefaults.buttonColors(
                    containerColor = textColor,
                    contentColor = Color.Black,
                )
            ) {
                Text(
                    text = if (!isPlaying) "Play" else "Pause",
                    modifier = Modifier.width(50.dp),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MetronomePreview() {
    MetronomeTheme {
        Metronome()
    }
}