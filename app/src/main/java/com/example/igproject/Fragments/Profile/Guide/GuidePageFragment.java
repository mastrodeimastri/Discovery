package com.example.igproject.Fragments.Profile.Guide;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.igproject.R;

import java.util.ArrayList;
import java.util.List;


public class GuidePageFragment extends Fragment {


    private int n;
    public GuidePageFragment() {
        // Required empty public constructor
    }

    private GuidePageFragment(int n) {
        this.n = n;
    }

    public static GuidePageFragment newInstance(int n) {
        GuidePageFragment fragment = new GuidePageFragment(n);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = view.findViewById(R.id.pageGuide);
        List<Integer> l= new ArrayList<>();
        l.add(R.drawable.p0);
        l.add(R.drawable.p2);
        l.add(R.drawable.p3);
        l.add(R.drawable.p4);
        l.add(R.drawable.p5);
        l.add(R.drawable.p6);
        l.add(R.drawable.p7);
        l.add(R.drawable.p8);
        l.add(R.drawable.p9);
        l.add(R.drawable.p10);
        l.add(R.drawable.p11);
        l.add(R.drawable.p12);
        l.add(R.drawable.p13);
        l.add(R.drawable.p14);
        l.add(R.drawable.p15);
        l.add(R.drawable.p16);
        l.add(R.drawable.p17);
        l.add(R.drawable.p18);
        l.add(R.drawable.p19);
        l.add(R.drawable.p20);
        l.add(R.drawable.p21);
        l.add(R.drawable.p22);
        l.add(R.drawable.p23);
        l.add(R.drawable.p25);
        l.add(R.drawable.p26);
        l.add(R.drawable.p27);
        l.add(R.drawable.p28);

        imageView.setImageResource(l.get(n));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guide_page, container, false);
    }
}