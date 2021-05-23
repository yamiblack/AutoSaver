package com.b11a.android.autosaver.ui.analysis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.b11a.android.autosaver.R;

public class AnalysisFragment extends Fragment {

    public static AnalysisFragment newInstance() {
        return new AnalysisFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_analysis, container, false);

        return root;
    }


}