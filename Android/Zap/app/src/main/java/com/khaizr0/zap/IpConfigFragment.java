package com.khaizr0.zap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class IpConfigFragment extends Fragment {
    private EditText editTextIp;
    private TextView textViewSavedIp;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "IPPrefs";
    private static final String KEY_SAVED_IP = "saved_ip";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ip_config, container, false);

        editTextIp = view.findViewById(R.id.editTextIp);
        textViewSavedIp = view.findViewById(R.id.textViewSavedIp);
        Button buttonSaveIp = view.findViewById(R.id.buttonSaveIp);

        sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Load saved IP and display it
        String savedIp = sharedPreferences.getString(KEY_SAVED_IP, "No IP saved");
        textViewSavedIp.setText(savedIp);

        buttonSaveIp.setOnClickListener(v -> {
            String ipToSave = editTextIp.getText().toString().trim();
            if (!ipToSave.isEmpty()) {
                // Save IP to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_SAVED_IP, ipToSave);
                editor.apply();

                // Update TextView to show saved IP
                textViewSavedIp.setText(ipToSave);
            }
        });

        return view;
    }
}