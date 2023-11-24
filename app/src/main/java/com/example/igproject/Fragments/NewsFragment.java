package com.example.igproject.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.igproject.LocalData.AttendanceData;
import com.example.igproject.R;
import com.example.igproject.RecyclerViewAdapters.AttendanceRVA;
import com.example.igproject.RecyclerViewAdapters.NewsRVA;
import com.example.igproject.RecyclerViewAdapters.OnClickRVAListener;

import org.threeten.bp.LocalDate;

public class NewsFragment extends Fragment implements OnClickRVAListener {
    private static final String ARG_ATTENDANCE = "attendanceData";
    private AttendanceData attendanceData;
    private AttendanceRVA attendanceRVA;
    private NewsRVA newsRVA;

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance(AttendanceData attendanceData) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ATTENDANCE, attendanceData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //Data is passed from main activity with parcelable (it becomes a string, then is unraveled)
            attendanceData = getArguments().getParcelable(ARG_ATTENDANCE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        if (attendanceData.isDataLoaded()){
            //If the data finished loading the loading screen disappears
            LinearLayout loading = view.findViewById(R.id.loading_attendance);
            loading.setVisibility(View.GONE);

            RecyclerView recyclerViewAttendance = view.findViewById(R.id.AttendanceRecyclerView);
            recyclerViewAttendance.setVisibility(View.VISIBLE);

            setUpAttendanceViews(view);
        }

        LinearLayout loading = view.findViewById(R.id.loading_news);
        loading.setVisibility(View.GONE);

        RecyclerView recyclerViewNews = view.findViewById(R.id.NewsRecyclerView);
        recyclerViewNews.setVisibility(View.VISIBLE);

        setUpNewsViews(view);

        return view;
    }

    private void setUpAttendanceViews(View view) {
        RecyclerView recyclerViewAttendance = view.findViewById(R.id.AttendanceRecyclerView);
        attendanceRVA = new AttendanceRVA(view.getContext(), attendanceData.attendanceDays, this);
        recyclerViewAttendance.setAdapter(attendanceRVA);
        recyclerViewAttendance.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        int dayNum = LocalDate.now().getDayOfWeek().getValue() - 1;
        attendanceRVA.updateSelectedPos(dayNum);
        recyclerViewAttendance.scrollToPosition(dayNum);
    }

    private void setUpNewsViews(View view) {
        RecyclerView recyclerViewNews = view.findViewById(R.id.NewsRecyclerView);
        newsRVA = new NewsRVA(view.getContext());
        recyclerViewNews.setAdapter(newsRVA);
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onClick(int position) {
        attendanceRVA.updateSelectedPos(position);
    }
}