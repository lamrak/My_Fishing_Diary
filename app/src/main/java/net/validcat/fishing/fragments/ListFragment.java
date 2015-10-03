package net.validcat.fishing.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    public static FishingAdapter adapter;
    public static List<FishingItem> itemsList;
    private Intent data;


    public ListFragment() {
        itemsList = new ArrayList<>();
        adapter = new FishingAdapter(getActivity(), itemsList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View listFragmentView = inflater.inflate(R.layout.list_fragment, container, false);
        ButterKnife.bind(this, listFragmentView);
        // itemsList = new ArrayList<>();
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
        // adapter = new FishingAdapter(getActivity(), itemsList);
        recyclerView.setAdapter(adapter);
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

    public void updateUI(Intent data) {
        FishingItem item = new FishingItem(data);
        itemsList.add(item);
        adapter.notifyDataSetChanged();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult( requestCode, resultCode,  data);
//        if (resultCode == Activity.RESULT_OK && requestCode == Constants.ITEM_REQUEST) {
//            FishingItem item = new FishingItem(data);
//            itemsList.add(item);
//            adapter.notifyDataSetChanged();
//        }
//    }

    public interface IClickListener {
        public void onItemClicked(long id);
    }

}
