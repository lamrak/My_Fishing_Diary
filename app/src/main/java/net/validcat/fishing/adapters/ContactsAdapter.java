package net.validcat.fishing.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.validcat.fishing.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {
    private static final String LOG_TAG = ContactsAdapter.class.getSimpleName();
    private List<ContactItem> personsList;
    private Context context;
    private static IRecyclerViewClickListener listener;
    private int selectedPos = 0;

    public ContactsAdapter (Context context, IRecyclerViewClickListener listener) {
        this.context = context;
        personsList = getContactsList(new ArrayList());
        this.listener = listener;
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public TextView email;
        public View vv;

        public ContactsViewHolder(View v) {
            super(v);
            vv = v;
            name = (TextView) v.findViewById(android.R.id.text1);
            email = (TextView) v.findViewById(android.R.id.text2);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.recyclerViewListClicked(v, this.getLayoutPosition());
            notifyItemChanged(selectedPos);
            selectedPos = getLayoutPosition();
            notifyItemChanged(selectedPos);
        }
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false));
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, final int position) {
        holder.name.setText((personsList.get(position).personName));
        holder.email.setText((personsList.get(position).personEmail));
     }

    @Override
    public int getItemCount() {
        return personsList.size();
    }

    private List getContactsList(List list) {
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        cursor.moveToFirst();
        do {
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor tempCursor = context.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
            tempCursor.moveToFirst();
            String email = null;
            try {
                email = tempCursor.getString(tempCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            } catch (CursorIndexOutOfBoundsException e) {
                Log.d(LOG_TAG, "no email");
            }
            if (email == null) continue;
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            list.add(new ContactItem(name, email));
            tempCursor.close();
        } while (cursor.moveToNext());
        cursor.close();
        return list;
    }

    private static class ContactItem {
        String personName;
        String personEmail;

        public ContactItem(String personName, String personEmail) {
            this.personEmail = personEmail;
            this.personName = personName;
        }
    }
}
