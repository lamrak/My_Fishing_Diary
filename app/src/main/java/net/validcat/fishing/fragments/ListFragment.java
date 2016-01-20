package net.validcat.fishing.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.validcat.fishing.ListActivity;
import net.validcat.fishing.R;
import net.validcat.fishing.adapters.FishingAdapter;
import net.validcat.fishing.data.FishingContract;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = ListActivity.class.getSimpleName();
    private static final int LOADER_ID = 3;
    @Bind(R.id.my_recycler_view) RecyclerView recyclerView;
    public static FishingAdapter adapter;

    public ListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View listFragmentView = inflater.inflate(R.layout.list_fragment, container, false);
        ButterKnife.bind(this, listFragmentView);
//        initDataBase();
        initUI();

        return listFragmentView;
    }

    private void initUI() {
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // specify an adapter (see also next example)
        adapter = new FishingAdapter(getActivity(), null);
        recyclerView.setAdapter(adapter);
        adapter.setIClickListener((IClickListener) getActivity());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                FishingContract.FishingEntry.CONTENT_URI,
                FishingContract.FishingEntry.COLUMNS,
                null, null, FishingContract.FishingEntry.COLUMN_DATE + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    public interface IClickListener {
        void onItemClicked(long id, View... sharedViews);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

}
