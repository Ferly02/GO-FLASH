package com.example.kurirkilat.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.kurirkilat.R

/**
 * Utility class for handling package input (weight and dimensions)
 */
class EstimasiHandler(private val context: Context) {

    /**
     * Shows a dialog for weight input
     * @param currentWeight Current weight value
     * @param weightTextView TextView to update with new weight
     * @param onWeightChanged Callback when weight changes
     */
    fun showWeightInputDialog(
        currentWeight: Int,
        weightTextView: TextView,
        onWeightChanged: (Int) -> Unit
    ) {
        val view = View.inflate(context, R.layout.dialog_number_input, null)
        val inputText = view.findViewById<TextView>(R.id.inputText)
        inputText.text = currentWeight.toString()

        val dialog = AlertDialog.Builder(context)
            .setTitle("Masukkan Berat (kg)")
            .setView(view)
            .setPositiveButton("Simpan") { _, _ ->
                try {
                    val weight = inputText.text.toString().toInt()
                    if (weight > 50) {
                        Toast.makeText(context, "Berat maksimal 50 kg", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }
                    weightTextView.text = weight.toString()
                    onWeightChanged(weight)
                } catch (e: NumberFormatException) {
                    Toast.makeText(context, "Masukkan angka yang valid", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .create()

        dialog.show()
    }

    /**
     * Shows a dialog for dimension input
     * @param currentValue Current dimension value
     * @param dimensionType Type of dimension (length, width, height)
     * @param dimensionTextView TextView to update with new dimension
     * @param onDimensionChanged Callback when dimension changes
     */
    fun showDimensionInputDialog(
        currentValue: Int,
        dimensionType: String,
        dimensionTextView: TextView,
        onDimensionChanged: (Int) -> Unit
    ) {
        val view = View.inflate(context, R.layout.dialog_number_input, null)
        val inputText = view.findViewById<TextView>(R.id.inputText)

        // Remove the "- " prefix if present
        val currentText = if (currentValue <= 0) "0" else currentValue.toString()
        inputText.text = currentText

        val dialog = AlertDialog.Builder(context)
            .setTitle("Masukkan $dimensionType (cm)")
            .setView(view)
            .setPositiveButton("Simpan") { _, _ ->
                try {
                    val dimension = inputText.text.toString().toInt()
                    if (dimension > 200) {
                        Toast.makeText(context, "Ukuran maksimal 200 cm", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    val formattedValue = if (dimension <= 0) "- 0" else dimension.toString()
                    dimensionTextView.text = formattedValue
                    onDimensionChanged(dimension)
                } catch (e: NumberFormatException) {
                    Toast.makeText(context, "Masukkan angka yang valid", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .create()

        dialog.show()
    }
}