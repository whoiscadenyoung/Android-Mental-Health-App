package com.csci3397.cadenyoung.groupproject.ui.statistics;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.csci3397.cadenyoung.groupproject.R;
import com.csci3397.cadenyoung.groupproject.model.Stat;
import com.csci3397.cadenyoung.groupproject.model.Stats;

public class StatisticsFragment extends Fragment {

    private Stats stats;
    private StatisticsViewModel statisticsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        statisticsViewModel = new ViewModelProvider(this).get(StatisticsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);

         stats = new Stats();
         stats.updateStat("eating", 0.8);

         for (Stat stat : stats.getStats()) {
             //ImageView imageView = new ImageView(this, stat.getImageId());
             String desc = getString(stat.getDescId());
             String name = stat.getName();
             Log.d("STAT", name + ": " + desc);
         }

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