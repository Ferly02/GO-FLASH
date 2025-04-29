package com.example.kurirkilat.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kurirkilat.R
import com.example.kurirkilat.adapter.OrderAdapter
import com.example.kurirkilat.model.OrderItem


class PenjemputanFragment : Fragment() {

    private lateinit var btnInfoPengiriman: Button
    private lateinit var btnDibatalkan: Button
    private lateinit var btnRating: Button

    private lateinit var buttonList: List<Button>

    private lateinit var rvOrders: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_penjemputan, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        initListeners()

        // Default tab
        setActiveTab(btnInfoPengiriman)

        val dummyData = listOf(
            OrderItem("Pesanan 1", "Diproses", "Sabtu, 12 Agustus 2023", R.drawable.ic_truk),
            OrderItem("Pesanan 2", "Diproses", "Minggu, 13 Agustus 2023", R.drawable.ic_truk),
            OrderItem("Pesanan 3", "Diproses", "Senin, 14 Agustus 2023", R.drawable.ic_truk)
        )

        val adapter = OrderAdapter(dummyData)
        rvOrders.layoutManager = LinearLayoutManager(requireContext())
        rvOrders.adapter = adapter

    }

    private fun initViews(view: View) {
        btnInfoPengiriman = view.findViewById(R.id.btnInfoPengiriman)
        btnDibatalkan = view.findViewById(R.id.btnDibatalkan)
        btnRating = view.findViewById(R.id.btnRating)
        rvOrders = view.findViewById(R.id.rvOrders) // RecyclerView ID dari XML
        buttonList = listOf(btnInfoPengiriman, btnDibatalkan, btnRating)
    }

    private fun initListeners() {
        btnInfoPengiriman.setOnClickListener {
            setActiveTab(it as Button)
            // TODO: Tambahkan aksi Info Pengiriman
        }

        btnDibatalkan.setOnClickListener {
            setActiveTab(it as Button)
            // TODO: Tambahkan aksi Dibatalkan
        }

        btnRating.setOnClickListener {
            setActiveTab(it as Button)
            // TODO: Tambahkan aksi Rating/Nilai Kurir
        }
    }

    private fun setActiveTab(activeButton: Button) {
        buttonList.forEach { button ->
            val isActive = button == activeButton
            val backgroundRes = if (isActive) {
                R.drawable.penjemputan_active_background
            } else {
                R.drawable.penjemputan_inactive_background
            }
            button.setBackgroundResource(backgroundRes)
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }
    }



}
