package com.csci3397.cadenyoung.groupproject.ui.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csci3397.cadenyoung.groupproject.R;
import com.csci3397.cadenyoung.groupproject.model.CustomAdapter;
import com.csci3397.cadenyoung.groupproject.model.Stats;

public class StatisticsFragment extends Fragment {

    private Stats stats;
    private StatisticsViewModel statisticsViewModel;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CustomAdapter adaptor;

    private TableLayout tableLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        statisticsViewModel = new ViewModelProvider(this).get(StatisticsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        adaptor = new CustomAdapter(stats.getStats());
        recyclerView.setAdapter(adaptor);

        stats = new Stats();
        stats.updateStat("eating", 80);

        /*for (Stat stat : stats.getStats()) {

            TextView name = new TextView(requireActivity());
            name.setText(stat.getName());

            TextView desc = new TextView(requireActivity());
            desc.setText(getString(stat.getDescId()));

        }*/

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