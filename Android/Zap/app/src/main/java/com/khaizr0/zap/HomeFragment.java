package com.khaizr0.zap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    private Button buttonOn;
    private TextView logView;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "IPPrefs";
    private static final String KEY_SAVED_IP = "saved_ip";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        buttonOn = view.findViewById(R.id.buttonOn);
        logView = view.findViewById(R.id.logView);

        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        buttonOn.setOnClickListener(v -> {
            String savedIp = sharedPreferences.getString(KEY_SAVED_IP, null);
            if (savedIp != null) {
                String url = String.format("http://%s/relay/on", savedIp);
                sendRequest(url);
            } else {
                Toast.makeText(requireActivity(), "IP chưa được lưu", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void sendRequest(String url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() -> logView.setText("Request failed: " + e.getMessage()));
                Log.e("HomeFragment", "Request failed", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body() != null ? response.body().string() : "No Response Body";
                        requireActivity().runOnUiThread(() -> logView.append("Request succeeded: " + responseData + "\n"));
                    } else {
                        requireActivity().runOnUiThread(() -> logView.setText("Request failed with code: " + response.code()));
                    }
                } catch (IOException e) {
                    requireActivity().runOnUiThread(() -> logView.setText("Error reading response: " + e.getMessage()));
                    Log.e("HomeFragment", "Error reading response", e);
                } finally {
                    response.close();
                }
            }
        });
    }
}
