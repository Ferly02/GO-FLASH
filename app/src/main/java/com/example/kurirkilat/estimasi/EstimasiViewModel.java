package com.example.kurirkilat.estimasi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EstimasiViewModel extends ViewModel {

    private final MutableLiveData<Integer> estimatedCost = new MutableLiveData<>();

    public LiveData<Integer> getEstimatedCost() {
        return estimatedCost;
    }

    public void hitungEstimasi(int jumlahBarang, int hargaPerBarang) {
        int hasil = jumlahBarang * hargaPerBarang;
        estimatedCost.setValue(hasil);
    }
}
