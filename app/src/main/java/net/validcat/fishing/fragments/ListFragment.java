package net.validcat.fishing.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.validcat.fishing.FishingAdapter;
import net.validcat.fishing.FishingItem;
import net.validcat.fishing.ListActivity;
import net.validcat.fishing.R;
import net.validcat.fishing.db.Constants;
import net.validcat.fishing.db.DB;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListFragment extends Fragment {
    public static final String LOG_TAG = ListActivity.class.getSimpleName();
    // init ui with butterknife
    private ImageView imageViewRound;
    @Bind(R.id.my_recycler_view)
    RecyclerView recyclerView;
//    @Bind(R.id.fab_add_fishing)
//    FloatingActionButton fab_add_fishing;

    private FishingAdapter adapter;
    private List<FishingItem> itemsList;

    public ListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View listFragmentView = inflater.inflate(R.layout.list_fragment,container,false);
        ButterKnife.bind(this, listFragmentView);
        itemsList = new ArrayList<>();
        initDataBase();
        initUI();

        return listFragmentView;
    }

    private void initUI() {
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        // specify an adapter (see also next example)
        adapter = new FishingAdapter(getActivity(), itemsList);
        recyclerView.setAdapter(adapter);

//        fab_add_fishing.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivityForResult(new Intent(getActivity(), AddNewFishingActivity.class), Constants.ITEM_REQUEST);
//            }
//        });

        adapter.setIClickListener((IClickListener) getActivity());
    }

        private void initDataBase() {
        DB db = new DB(getActivity());
        db.open();
        Cursor cursor = db.getAllData();
        if (cursor.moveToFirst()) itemsList = db.getData(itemsList, cursor);
        else cursor.close();
        db.close();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.ITEM_REQUEST) {
            FishingItem item = new FishingItem(data);
            itemsList.add(item);
            adapter.notifyDataSetChanged();
        }
    }

    public interface IClickListener {
        public void onItemClicked(long id);
    }



}
