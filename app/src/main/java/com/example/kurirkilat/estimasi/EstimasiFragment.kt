package com.example.kurirkilat.estimasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kurirkilat.R
import com.example.kurirkilat.utils.EstimasiHandler

class EstimasiFragment : Fragment() {

    // Service type options
    private lateinit var kurirMotorLayout: LinearLayout
    private lateinit var kurirMobilLayout: LinearLayout
    private lateinit var instantLayout: LinearLayout
    private lateinit var sameDayLayout: LinearLayout

    // Package details
    private lateinit var weightLayout: LinearLayout
    private lateinit var weightText: TextView
    private lateinit var lengthLayout: LinearLayout
    private lateinit var lengthText: TextView
    private lateinit var widthLayout: LinearLayout
    private lateinit var widthText: TextView
    private lateinit var heightLayout: LinearLayout
    private lateinit var heightText: TextView

    // Currently selected service
    private var selectedServiceLayout: LinearLayout? = null

    // Package weight and dimensions
    private var packageWeight: Int = 0
    private var packageLength: Int = 0
    private var packageWidth: Int = 0
    private var packageHeight: Int = 0

    // Estimation info
    /*private lateinit var estimatedTimeText: TextView
    private lateinit var estimatedCostText: TextView*/

    // Locations
    private lateinit var pickupLocationLayout: LinearLayout
    private lateinit var pickupLocationText: TextView
    private lateinit var destinationLocationLayout: LinearLayout
    private lateinit var destinationLocationText: TextView

    // Back button
    private lateinit var btnBack: ImageButton

    // Order button
    private lateinit var btnOrder: Button

    // Package input handler
    private lateinit var packageInputHandler: EstimasiHandler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_estimasi, container, false)

        // Initialize package input handler
        packageInputHandler = EstimasiHandler(requireContext())

        // Initialize UI elements
        initializeViews(view)

        // Set up listeners
        setupListeners()

        // Set initial state
        setupInitialState()

        return view
    }

    private fun initializeViews(view: View) {
        // Service type options
        kurirMotorLayout = view.findViewById(R.id.layoutKurirMotor)
        kurirMobilLayout = view.findViewById(R.id.layoutKurirMobil)
        instantLayout = view.findViewById(R.id.layoutInstant)
        sameDayLayout = view.findViewById(R.id.layoutSameDay)

        // Package details
        weightLayout = view.findViewById(R.id.weightLayout)
        weightText = view.findViewById(R.id.textWeight)
        lengthLayout = view.findViewById(R.id.lengthLayout)
        lengthText = view.findViewById(R.id.textLength)
        widthLayout = view.findViewById(R.id.widthLayout)
        widthText = view.findViewById(R.id.textWidth)
        heightLayout = view.findViewById(R.id.heightLayout)
        heightText = view.findViewById(R.id.textHeight)

        // Estimation info
       /* estimatedTimeText = view.findViewById(R.id.textEstimatedTime)
        estimatedCostText = view.findViewById(R.id.textEstimatedCost)*/

        // Locations
        pickupLocationLayout = view.findViewById(R.id.pickupLocationLayout)
        pickupLocationText = view.findViewById(R.id.textPickupLocation)
        destinationLocationLayout = view.findViewById(R.id.destinationLocationLayout)
        destinationLocationText = view.findViewById(R.id.textDestinationLocation)

        // Back button
        btnBack = view.findViewById(R.id.btnBack)

        // Order button
        btnOrder = view.findViewById(R.id.btnOrder)
    }

    private fun setupListeners() {
        // Set listener for service type selection
        kurirMotorLayout.setOnClickListener {
            selectServiceType(kurirMotorLayout)
            updateEstimation()
        }

        kurirMobilLayout.setOnClickListener {
            selectServiceType(kurirMobilLayout)
            updateEstimation()
        }

        instantLayout.setOnClickListener {
            selectServiceType(instantLayout)
            updateEstimation()
        }

        sameDayLayout.setOnClickListener {
            selectServiceType(sameDayLayout)
            updateEstimation()
        }

        // Weight and dimensions input
        weightLayout.setOnClickListener {
            packageInputHandler.showWeightInputDialog(
                packageWeight,
                weightText
            ) { newWeight ->
                packageWeight = newWeight
                updateEstimation()
            }
        }

        lengthLayout.setOnClickListener {
            packageInputHandler.showDimensionInputDialog(
                packageLength,
                "Panjang",
                lengthText
            ) { newLength ->
                packageLength = newLength
            }
        }

        widthLayout.setOnClickListener {
            packageInputHandler.showDimensionInputDialog(
                packageWidth,
                "Lebar",
                widthText
            ) { newWidth ->
                packageWidth = newWidth
            }
        }

        heightLayout.setOnClickListener {
            packageInputHandler.showDimensionInputDialog(
                packageHeight,
                "Tinggi",
                heightText
            ) { newHeight ->
                packageHeight = newHeight
            }
        }

        // Location selection
        pickupLocationLayout.setOnClickListener {
            navigateToLocationPicker(isPickup = true)
        }

        destinationLocationLayout.setOnClickListener {
            navigateToLocationPicker(isPickup = false)
        }

        // Set listener for back button
        btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Set listener for order button
        btnOrder.setOnClickListener {
            if (validateOrder()) {
                placeOrder()
            }
        }
    }

    private fun setupInitialState() {
        // Set default service type to Kurir Motor
        selectServiceType(kurirMotorLayout)

        // Set default estimations
       /* estimatedTimeText.text = "45 menit"
        estimatedCostText.text = "Rp 23.000" */
    }

    private fun selectServiceType(layout: LinearLayout) {
        // Reset all layouts to default style
        kurirMotorLayout.setBackgroundResource(R.drawable.rounded_white_bg)
        kurirMobilLayout.setBackgroundResource(R.drawable.rounded_white_bg)
        instantLayout.setBackgroundResource(R.drawable.rounded_white_bg)
        sameDayLayout.setBackgroundResource(R.drawable.rounded_white_bg)

        // Set the selected one to selected style
        layout.setBackgroundResource(R.drawable.rounded_blue_selector)

        // Update the selected service
        selectedServiceLayout = layout
    }

    private fun updateEstimation() {
        // This would normally calculate based on distance, weight, and service type
        // For now, we'll just set mock values based on service type and weight

        // Base prices
        val basePrices = mapOf(
            kurirMotorLayout to 15000,
            kurirMobilLayout to 35000,
            instantLayout to 25000,
            sameDayLayout to 10000
        )

        // Base times
        val baseTimes = mapOf(
            kurirMotorLayout to "45 menit",
            kurirMobilLayout to "50 menit",
            instantLayout to "30 menit",
            sameDayLayout to "3-6 jam"
        )

        // Calculate additional cost based on weight
        val weightCost = packageWeight * 400  // 400 rupiah per kg

        // Get base price for selected service
        val basePrice = basePrices[selectedServiceLayout] ?: 15000

        // Calculate total cost
        val totalCost = basePrice + weightCost

        // Update UI
       // estimatedTimeText.text = baseTimes[selectedServiceLayout] ?: "45 menit"
       // estimatedCostText.text = "Rp ${totalCost.toFormattedPrice()}"
    }

    private fun Int.toFormattedPrice(): String {
        return toString().reversed().chunked(3).joinToString(".").reversed()
    }

    private fun validateOrder(): Boolean {
        // Check if pickup location is set
        if (pickupLocationText.text.toString().contains("Masukkan alamat")) {
            showToast("Silakan masukkan alamat pengambilan")
            return false
        }

        // Check if destination location is set
        if (destinationLocationText.text.toString().contains("Masukkan alamat")) {
            showToast("Silakan masukkan alamat tujuan")
            return false
        }

        // Check if the package weight is valid
        if (packageWeight <= 0) {
            showToast("Berat barang harus lebih dari 0 kg")
            return false
        }

        // Check if the package weight is valid for motorcycle delivery
        if (selectedServiceLayout == kurirMotorLayout && packageWeight > 20) {
            showToast("Berat maksimal untuk motor adalah 20 kg")
            return false
        }

        return true
    }

    private fun placeOrder() {
        // In a real app, you would save order details to database
        // and navigate to confirmation page

        // For this example, we'll just show a toast
        showToast("Pesanan berhasil dibuat")

        // Navigate back to home
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun navigateToLocationPicker(isPickup: Boolean) {
        // In a real app, you would navigate to a map or location picker screen
        // For this example, we'll simulate location selection with a dialog

        val locationOptions = arrayOf(
            "Jl. Sudirman No. 123, Jakarta",
            "Jl. Gatot Subroto No. 45, Jakarta",
            "Jl. MH Thamrin No. 67, Jakarta",
            "Jl. Kuningan No. 89, Jakarta"
        )

        val title = if (isPickup) "Pilih Lokasi Pengambilan" else "Pilih Lokasi Tujuan"

        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setItems(locationOptions) { _, which ->
                val selectedLocation = locationOptions[which]
                if (isPickup) {
                    pickupLocationText.text = selectedLocation
                    pickupLocationText.setTextColor(resources.getColor(R.color.black, null))
                } else {
                    destinationLocationText.text = selectedLocation
                    destinationLocationText.setTextColor(resources.getColor(R.color.black, null))
                }
                updateEstimation()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}