package com.b11a.android.autosaver.ui.driving;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.b11a.android.autosaver.MainActivity;
import com.b11a.android.autosaver.R;

public class DrivingFragment extends Fragment {

    private Button btnStart;
    private Button btnEnd;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_driving_start, container, false);
        btnStart = root.findViewById(R.id.btn_start);
        btnEnd = root.findViewById(R.id.btn_end);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        return root;
    }

}