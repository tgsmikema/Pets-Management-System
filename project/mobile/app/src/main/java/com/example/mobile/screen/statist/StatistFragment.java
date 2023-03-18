package com.example.mobile.screen.statist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mobile.databinding.FragmentHomeBinding;
import com.example.mobile.databinding.FragmentStatistBinding;

public class StatistFragment extends Fragment {

    private FragmentStatistBinding binding;
    private TextView textView;
    private Button button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStatistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        textView = binding.textStatist;
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}