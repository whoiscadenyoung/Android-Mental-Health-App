package com.csci3397.cadenyoung.groupproject.ui.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csci3397.cadenyoung.groupproject.R;
import com.csci3397.cadenyoung.groupproject.model.CustomAdapter;
import com.csci3397.cadenyoung.groupproject.model.Stats;
import com.csci3397.cadenyoung.groupproject.ui.home.HomeViewModel;

public class StatisticsFragment extends Fragment {

    private Stats stats;
    private StatisticsViewModel statisticsViewModel;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CustomAdapter adaptor;

    private TableLayout tableLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        statisticsViewModel = ViewModelProviders.of(requireActivity()).get(StatisticsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);

        stats = new Stats();
        stats.updateStat("eating", 80);

        recyclerView = root.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        adaptor = new CustomAdapter(stats.getStats());
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(layoutManager);

//        final TextView textView = root.findViewById(R.id.text_notifications);
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
}