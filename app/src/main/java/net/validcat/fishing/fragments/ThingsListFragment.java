package net.validcat.fishing.fragments;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import net.validcat.fishing.R;
import net.validcat.fishing.adapters.IRecyclerViewClickListener;
import net.validcat.fishing.adapters.ThingsAdapter;
import net.validcat.fishing.data.Constants;
import net.validcat.fishing.data.FishingContract;

import butterknife.Bind;

public class ThingsListFragment extends Fragment implements IRecyclerViewClickListener {
    private String mThingsListReference;
    private ThingsAdapter adapter;
    @Bind(R.id.things_list_description_text_view) TextView mItemDescription;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.things_list_fragment, container, false);

        mThingsListReference = getActivity().getIntent().getStringExtra(Constants.THINGS_LIST_REFERENCE);

        Cursor cursor = getThingsListCursor();
        //Initialize and write data into db for new things list
        if (!cursor.moveToFirst()) {
            String[] values = getResources().getStringArray(R.array.default_things_list_array);
            for (String value : values) {
                writeThingIntoDb(value, mThingsListReference);
            }
            cursor = getThingsListCursor();
        }

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.things_list_recycler_view);
        adapter = new ThingsAdapter(getActivity(), cursor, this);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);
        //initSwipeToDelete(rv);

        FloatingActionButton fabAddThing = (FloatingActionButton) view.findViewById(R.id.fab_add_thing);
        fabAddThing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.item_description));


                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton(getResources()
                        .getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        writeThingIntoDb(input.getText().toString(), mThingsListReference);
                        adapter.changeCursor(getThingsListCursor());
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(getResources()
                        .getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.things_list_action_bar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_save_list:
                getActivity().finish();
                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    private void writeThingIntoDb(String desc, String fishingId) {
        ContentValues cv = new ContentValues();
        cv.put(FishingContract.ThingsEntry.COLUMN_DESCRIPTION, desc);
        cv.put(FishingContract.ThingsEntry.COLUMN_EQUIPPED, 0);
        cv.put(FishingContract.ThingsEntry.COLUMN_FISHING_ID, fishingId);
        getActivity().getContentResolver().insert(
                FishingContract.ThingsEntry.CONTENT_URI.buildUpon()
                        .appendPath(mThingsListReference).build(), cv);
    }

    private void deleteThingFromDb(String desc, String fishingId) {
      getActivity().getContentResolver().delete(
                FishingContract.ThingsEntry.CONTENT_URI.buildUpon()
                        .appendPath(mThingsListReference).build(),
              desc,
              null);
    }

    private Cursor getThingsListCursor() {
        return getActivity().getContentResolver().query(
                FishingContract.ThingsEntry.CONTENT_URI.buildUpon()
                        .appendPath(mThingsListReference).build(),
                FishingContract.ThingsEntry.PROJECTION,
                null,
                null,
                null
        );
    }

    private void markAsEquipped(String desc, String fishingId, boolean equippedState) {
        ContentValues cv = new ContentValues();
        cv.put(FishingContract.ThingsEntry.COLUMN_DESCRIPTION, desc);
        if (equippedState) {
            cv.put(FishingContract.ThingsEntry.COLUMN_EQUIPPED, 1);
        } else {
            cv.put(FishingContract.ThingsEntry.COLUMN_EQUIPPED, 1);
        }
        cv.put(FishingContract.ThingsEntry.COLUMN_FISHING_ID, fishingId);
        getActivity().getContentResolver().update(
                FishingContract.ThingsEntry.CONTENT_URI.buildUpon()
                        .appendPath(mThingsListReference).build(), cv,
                FishingContract.ThingsEntry.COLUMN_DESCRIPTION, new String[] {desc});
    }

    private void initSwipeToDelete(RecyclerView rv) {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //deleteThingFromDb();
                adapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rv);
    }


    @Override
    public void recyclerViewListClicked(View v, final int position) {
      /*  String desc = mItemDescription.getText().toString();
        boolean equippedState =
                ((CheckBox) v.findViewById(R.id.things_list_if_equipped_checkbox)).isEnabled();
        markAsEquipped(desc, mThingsListReference, equippedState);*/
    }
}

