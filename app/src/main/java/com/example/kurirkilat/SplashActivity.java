package com.example.kurirkilat;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kurirkilat.LoginActivity;
import com.example.kurirkilat.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView imgSplash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imgSplash = findViewById(R.id.imgSplash);

        // Animasi Fade In
        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(1000); // 1 detik

        // Animasi Fade Out
        AlphaAnimation fadeOut = new AlphaAnimation(1f, 0f);
        fadeOut.setDuration(1000); // 1 detik
        fadeOut.setStartOffset(2000); // Setelah 2 detik diam, baru fade out

        imgSplash.startAnimation(fadeIn);

        imgSplash.postDelayed(() -> {
            imgSplash.startAnimation(fadeOut);
        }, 2000); // Tunggu 2 detik baru fade out

        imgSplash.postDelayed(() -> {
            // Setelah animasi selesai, pindah ke MainActivity
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, 3000); // total 3 detik splash
    }
}
