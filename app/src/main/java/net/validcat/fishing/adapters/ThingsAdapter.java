package net.validcat.fishing.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import net.validcat.fishing.R;
import net.validcat.fishing.data.FishingContract;

public class ThingsAdapter extends CursorRecyclerViewAdapter<ThingsAdapter.ViewHolder> {

    private Context mContext;
    private static IRecyclerViewClickListener mItemListener;

    public ThingsAdapter(Context context, Cursor cursor
            , IRecyclerViewClickListener itemListener) {
        super(context, cursor);
        mContext = context;
        this.mItemListener = itemListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final CheckBox ifEquipped;
        public final TextView thingDescription;

        public ViewHolder(View view) {
            super(view);
            ifEquipped = (CheckBox) view.findViewById(R.id.things_list_if_equipped_checkbox);
            thingDescription = (TextView) view.findViewById(R.id.things_list_description_text_view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemListener.recyclerViewListClicked(v, this.getLayoutPosition());
        }
    }

    @Override
    public void onBindViewHolder(final ThingsAdapter.ViewHolder viewHolder, final Cursor cursor) {
        viewHolder.thingDescription.setText(cursor.getString(
                FishingContract.ThingsEntry.INDEX_COLUMN_DESCRIPTION));
        viewHolder.ifEquipped.setChecked(
                (cursor.getInt(FishingContract
                        .ThingsEntry.INDEX_COLUMN_EQUIPPED)) == 0 ? false : true);

    }

    @Override
    public ThingsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.things_list_item, parent, false));
       return holder;
    }
}
