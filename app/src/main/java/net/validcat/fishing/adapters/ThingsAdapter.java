package net.validcat.fishing.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import net.validcat.fishing.R;

import static net.validcat.fishing.data.FishingContract.ThingsEntry;

public class ThingsAdapter extends CursorRecyclerViewAdapter<ThingsAdapter.ViewHolder> {

    private static IRecyclerViewClickListener listener;

    public ThingsAdapter(Context context, Cursor cursor, IRecyclerViewClickListener listener) {
        super(context, cursor);
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final CheckBox ifEquippedCheckBox;
        public final TextView thingDesc;

        public ViewHolder(View view) {
            super(view);
            ifEquippedCheckBox = (CheckBox) view.findViewById(R.id.things_list_if_equipped_checkbox);
            thingDesc = (TextView) view.findViewById(R.id.things_list_description_text_view);
            ifEquippedCheckBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.recyclerViewListClicked(v, this.getLayoutPosition());
        }
    }

    @Override
    public void onBindViewHolder(final ThingsAdapter.ViewHolder holder, final Cursor cursor) {
        holder.thingDesc.setText(cursor.getString(
                ThingsEntry.INDEX_COLUMN_DESCRIPTION));
        holder.ifEquippedCheckBox.setChecked((cursor.getInt(ThingsEntry.INDEX_COLUMN_EQUIPPED)) != 0);
        if (holder.ifEquippedCheckBox.isChecked()) {
            holder.thingDesc.setPaintFlags(holder.thingDesc.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

    }

    @Override
    public ThingsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       return new ViewHolder(LayoutInflater.from(parent.getContext())
                       .inflate(R.layout.things_list_item, parent, false));
    }
}
