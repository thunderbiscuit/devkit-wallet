package com.goldenraven.bdksampleapp.utilities

import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import android.graphics.Bitmap

private const val WHITE = -0x1
private const val BLACK = -0x1000000

class QRCodeGenerator(
    private val blackColor: Int = BLACK,
    private val whiteColor: Int = WHITE
) {

    fun generate(value: String, size: Int): Bitmap {
        val writer = QRCodeWriter()
        val result: BitMatrix = writer.encode(value, BarcodeFormat.QR_CODE, size, size)

        val pixels = generatePixelArray(result)

        val bitmap = Bitmap.createBitmap(result.width, result.height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, result.width, 0, 0, result.width, result.height)
        return bitmap
    }

    private fun generatePixelArray(bitMatrix: BitMatrix): IntArray {
        val width = bitMatrix.width
        val height = bitMatrix.height

        val pixels = IntArray(width * height)
        // All are 0, or black, by default
        for (y in 0 until height) {
            val offset: Int = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (bitMatrix[x, y]) blackColor else whiteColor
            }
        }
        return pixels
    }
}
