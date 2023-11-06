/**
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.soundchecker.utils.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp

/**
 * Composable that displays a waveform.
 * The graph goes from -1.0f to 1.0f as the y-axis.
 * This function accepts a modifier, which modifies only the outer box, and a set of yValues.
 *
 * If the data is sparse, each value will have its own bar.
 * If the data is dense, each vertical bar will represent multiple samples.
 */
@Composable
fun WaveformDisplay(
    modifier : Modifier,
    yValues: FloatArray?,
) {
    if (yValues == null) {
        return;
    }
    Box(
        modifier = modifier
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        contentAlignment = Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
        ) {
            val offsetY = 0.5f * size.height;
            val scaleY = 0.0f - offsetY;

            val xScale = size.width / (yValues.size - 1)
            var x0 = 0.0f
            if (xScale < 1.0) {
                // Draw a vertical bar for multiple samples.
                var ymin = offsetY
                var ymax = offsetY
                for (i in 0 until yValues.size) {
                    val x1 = i * xScale
                    if (x0.toInt() != x1.toInt()) {
                        // draw old data
                        drawLine(
                            start = Offset(x0, ymin),
                            end = Offset(x0, ymax),
                            color = Color.Black,
                            strokeWidth = 1F)
                        x0 = x1;
                        ymin = offsetY;
                        ymax = offsetY;
                    }
                    val y1: Float = yValues.get(i) * scaleY + offsetY
                    ymin = Math.min(ymin, y1)
                    ymax = Math.max(ymax, y1)
                }
            } else {
                // Draw line between samples.
                var y0: Float = yValues.get(0) * scaleY + offsetY
                for (i in 1 until yValues.size) {
                    val x1 = i * xScale
                    val y1: Float = yValues.get(i) * scaleY + offsetY
                    drawLine(
                        start = Offset(x0, y0),
                        end = Offset(x0, y1),
                        color = Color.Black,
                        strokeWidth = 5F)
                    x0 = x1
                    y0 = y1
                }
            }
        }
    }
}
