package com.example.kurirkilat.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.kurirkilat.R
import com.example.kurirkilat.estimasi.EstimasiFragment

class BerandaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_beranda, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup menu item clicks (now in the fragment)
        setupMenuItemClicks(view)
    }

    private fun setupMenuItemClicks(view: View) {
        // Find the LinearLayout containers for each menu item
        val trackCourierLayout = view.findViewById<androidx.cardview.widget.CardView>(R.id.menuCard)
            .findViewWithTag<LinearLayout>("lacak_kurir_container")

        val estimasiLayout = view.findViewById<androidx.cardview.widget.CardView>(R.id.menuCard)
            .findViewWithTag<LinearLayout>("estimasi_container")

        val customerServiceLayout = view.findViewById<androidx.cardview.widget.CardView>(R.id.menuCard)
            .findViewWithTag<LinearLayout>("layanan_cs_container")

        val historyLayout = view.findViewById<androidx.cardview.widget.CardView>(R.id.menuCard)
            .findViewWithTag<LinearLayout>("riwayat_container")

        // Set click listeners
        estimasiLayout.setOnClickListener {
            // Navigate to EstimasiFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.mainFragmentContainer, EstimasiFragment())
                .addToBackStack(null)
                .commit()
        }

        trackCourierLayout.setOnClickListener {
            // Navigate to your TrackingFragment (you'll need to create this)
            // For now, just show a toast
            android.widget.Toast.makeText(context, "Lacak Kurir coming soon", android.widget.Toast.LENGTH_SHORT).show()
        }

        customerServiceLayout.setOnClickListener {
            // Navigate to your CustomerServiceFragment (you'll need to create this)
            android.widget.Toast.makeText(context, "Layanan CS coming soon", android.widget.Toast.LENGTH_SHORT).show()
        }

        historyLayout.setOnClickListener {
            // Navigate to your HistoryFragment (you'll need to create this)
            android.widget.Toast.makeText(context, "Riwayat coming soon", android.widget.Toast.LENGTH_SHORT).show()
        }
    }
}