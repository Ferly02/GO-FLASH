package com.example.kurirkilat

import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.kurirkilat.databinding.ActivityMainBinding
import com.example.kurirkilat.databinding.CustomBottomNavBinding
import com.example.kurirkilat.navigation.BerandaFragment
import com.example.kurirkilat.navigation.PenjemputanFragment
import com.example.kurirkilat.navigation.ProfileFragment
import com.example.kurirkilat.navigation.TambahPaketFragment

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding
	private lateinit var bottomNavBinding: CustomBottomNavBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		bottomNavBinding = binding.customBottomNav

		loadFragment(BerandaFragment())
		bindNavigation()
		setSelectedTab(bottomNavBinding.tabBeranda) // default pilih Beranda
	}

	private fun bindNavigation() {
		bottomNavBinding.tabBeranda.setOnClickListener {
			loadFragment(BerandaFragment())
			setSelectedTab(bottomNavBinding.tabBeranda)
		}
		bottomNavBinding.tabTambah.setOnClickListener {
			loadFragment(TambahPaketFragment())
			setSelectedTab(bottomNavBinding.tabTambah)
		}
		bottomNavBinding.tabPenjemputan.setOnClickListener {
			loadFragment(PenjemputanFragment())
			setSelectedTab(bottomNavBinding.tabPenjemputan)
		}
		bottomNavBinding.tabProfile.setOnClickListener {
			loadFragment(ProfileFragment())
			setSelectedTab(bottomNavBinding.tabProfile)
		}
	}

	private fun loadFragment(fragment: Fragment) {
		supportFragmentManager.beginTransaction()
			.replace(R.id.mainFragmentContainer, fragment)
			.commit()
	}

	private fun setSelectedTab(selectedTab: LinearLayout) {
		val tabs = listOf(
			bottomNavBinding.tabBeranda,
			bottomNavBinding.tabTambah,
			bottomNavBinding.tabPenjemputan,
			bottomNavBinding.tabProfile
		)

		for (tab in tabs) {
			val icon = tab.getChildAt(0) as androidx.appcompat.widget.AppCompatImageView
			val text = tab.getChildAt(1) as androidx.appcompat.widget.AppCompatTextView

			tab.orientation = LinearLayout.VERTICAL
			tab.gravity = Gravity.CENTER
			tab.setBackgroundResource(R.drawable.bg_tab_normal)

			val tabParams = tab.layoutParams as LinearLayout.LayoutParams
			tabParams.width = 0
			tabParams.weight = 1f
			tab.layoutParams = tabParams

			tab.setPadding(0, dpToPx(8), 0, dpToPx(8))

			icon.setColorFilter(ContextCompat.getColor(this, R.color.NavUnselected))
			text.setTextColor(ContextCompat.getColor(this, R.color.NavUnselected))

			icon.scaleX = 1.0f
			icon.scaleY = 1.0f
			text.scaleX = 1.0f
			text.scaleY = 1.0f

			text.maxLines = 1
			text.ellipsize = TextUtils.TruncateAt.END

			val textParams = text.layoutParams as LinearLayout.LayoutParams
			textParams.setMargins(0, dpToPx(0), 0, 0)
			text.layoutParams = textParams

			tab.isClickable = true
			tab.isFocusable = true

			// Reset translationY untuk tab yang tidak dipilih
			tab.translationY = 0f
		}

		// Yang dipilih
		val icon = selectedTab.getChildAt(0) as androidx.appcompat.widget.AppCompatImageView
		val text = selectedTab.getChildAt(1) as androidx.appcompat.widget.AppCompatTextView

		selectedTab.orientation = LinearLayout.HORIZONTAL
		selectedTab.gravity = Gravity.CENTER_VERTICAL
		selectedTab.setBackgroundResource(R.drawable.bg_tab_selected)

		val tabParams = selectedTab.layoutParams as LinearLayout.LayoutParams
		tabParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
		tabParams.weight = 0f
		selectedTab.layoutParams = tabParams

		// Padding tetap
		selectedTab.setPadding(dpToPx(16), dpToPx(8), dpToPx(16), dpToPx(8))

		icon.setColorFilter(ContextCompat.getColor(this, R.color.NavSelected))
		text.setTextColor(ContextCompat.getColor(this, R.color.NavSelected))

		// Menggunakan translationY untuk menurunkan elemen
		selectedTab.translationY = dpToPx(5).toFloat() // Menurunkan posisi tab yang terpilih sedikit

		// Menyusun margin untuk teks agar sejajar
		val textParams = text.layoutParams as LinearLayout.LayoutParams
		textParams.setMargins(dpToPx(8), dpToPx(0), 0, dpToPx(0)) // Sejajarkan teks dengan ikon
		text.layoutParams = textParams

		selectedTab.isClickable = false
		selectedTab.isFocusable = false
	}

	private fun dpToPx(dp: Int): Int {
		val density = resources.displayMetrics.density
		return (dp * density).toInt()
	}
}
