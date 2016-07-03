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

import static net.validcat.fishing.data.FishingContract.ThingsEntry;

public class ThingsAdapter extends CursorRecyclerViewAdapter<ThingsAdapter.ViewHolder> {

    private Context mContext;
    private static IRecyclerViewClickListener listener;

    public ThingsAdapter(Context context, Cursor cursor, IRecyclerViewClickListener listener) {
        super(context, cursor);
        mContext = context;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final CheckBox ifEquipped;
        public final TextView thingDescription;

        public ViewHolder(View view) {
            super(view);
            ifEquipped = (CheckBox) view.findViewById(R.id.things_list_if_equipped_checkbox);
            thingDescription = (TextView) view.findViewById(R.id.things_list_description_text_view);
            ifEquipped.setOnClickListener(this);
            //view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.recyclerViewListClicked(v, this.getLayoutPosition());
        }
    }

    @Override
    public void onBindViewHolder(final ThingsAdapter.ViewHolder viewHolder, final Cursor cursor) {
        viewHolder.thingDescription.setText(cursor.getString(
                ThingsEntry.INDEX_COLUMN_DESCRIPTION));
        viewHolder.ifEquipped.setChecked((cursor.getInt(ThingsEntry.INDEX_COLUMN_EQUIPPED)) != 0);

    }

    @Override
    public ThingsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       return new ViewHolder(LayoutInflater.from(parent.getContext())
                       .inflate(R.layout.things_list_item, parent, false));
    }
}
