package com.bradperkins.chatgroupapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bradperkins.chatgroupapp.GroupObj;
import com.bradperkins.chatgroupapp.R;
import com.bradperkins.chatgroupapp.utilities.GroupsAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final String ARG_GROUPS = "ARG_GROUPS";
    private static final String ARG_POS = "ARG_POS";

    private static ArrayList<GroupObj> groupList;
    private static int pos;
    public static RecyclerView recyclerView;
    public static GroupsAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(ArrayList<GroupObj> groupList, int pos) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GROUPS, groupList);
        args.putInt(ARG_POS, pos);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        groupList = (ArrayList<GroupObj>) getArguments().getSerializable(ARG_GROUPS);
        pos = getArguments().getInt(ARG_POS);

        adapter = new GroupsAdapter(getActivity(), groupList);

        recyclerView = getView().findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.getLayoutManager().scrollToPosition(pos);
        recyclerView.setAdapter(adapter);
    }
}
