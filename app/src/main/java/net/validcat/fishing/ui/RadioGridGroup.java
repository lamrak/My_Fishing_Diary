package net.validcat.fishing.ui;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Created by Dobrunov on 29.12.2015.
 */
public class RadioGridGroup extends TableLayout implements View.OnClickListener {
    private int checkedButtonID = -1;

    /**
     * @param context
     */
    public RadioGridGroup(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public RadioGridGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof RadioButton) {
            int id = v.getId();
            check(id);
            checkedButtonID = id;
        }
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof RadioButton) {
            ((RadioButton) checkedView).setChecked(checked);
        }
    }

    /* (non-Javadoc)
     * @see android.widget.TableLayout#addView(android.view.View, int, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void addView(View child, int index,
                        android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        setChildrenOnClickListener((TableRow) child);
    }

    /* (non-Javadoc)
     * @see android.widget.TableLayout#addView(android.view.View, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        setChildrenOnClickListener((TableRow) child);
    }

    private void setChildrenOnClickListener(TableRow tr) {
        final int c = tr.getChildCount();
        for (int i = 0; i < c; i++) {
            final View v = tr.getChildAt(i);
            if (v instanceof RadioButton) {
                v.setOnClickListener(this);
//                if (((RadioButton) v).isChecked())
//                    activeRadioButton = (RadioButton) v;
            }
        }
    }

    /**
     * @return the checked button Id
     */
    public int getCheckedRadioButtonId() {
        return checkedButtonID;
    }


    /**
     * Check the id
     *
     * @param id
     */
    public void check(@IdRes int id) {
        // don't even bother
        if (id != -1 && (id == checkedButtonID)) {
            return;
        }
        if (checkedButtonID != -1) {
            setCheckedStateForView(checkedButtonID, false);
        }
        if (id != -1) {
            setCheckedStateForView(id, true);
        }
        setCheckedId(id);
    }

    /**
     * set the checked button Id
     *
     * @param id
     */
    private void setCheckedId(int id) {
        this.checkedButtonID = id;
    }

    public void clearCheck() {
        check(-1);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        this.checkedButtonID = ss.buttonId;
        setCheckedStateForView(checkedButtonID, true);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.buttonId = checkedButtonID;

        return savedState;
    }

    public void setChecked(int checked) {
        View v = getViewBySelection(this, checked, new int[1]);
        if (v != null)
            check(v.getId()); //
    }

    int position = 0;
    @Override
    public int indexOfChild(View child) {
        View v = getIndexOfViewById(this, child.getId(), new int[1]);
        return v != null ? position : -1;
    }

    private View getIndexOfViewById(ViewGroup root, @IdRes int id, int[] index) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof ViewGroup) {
                View vRes = getIndexOfViewById((ViewGroup) v, id, index);
                if (vRes != null)
                    return vRes;
            } else if (v instanceof RadioButton) {
                if (v.getId() == id) {
                    position = index[0];
                    return v;
                } else index[0]++;
            }
        }

        return null;
    }

    private View getViewBySelection(ViewGroup root, int selected, int[] ind) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof ViewGroup) {
                View resView = getViewBySelection((ViewGroup) v, selected, ind);
                if (resView != null)
                    return resView;
            } else if (v instanceof RadioButton) {
                if (ind[0] == selected) {
                    return v;
                } else ind[0]++;
            }
        }

        return null;
    }

    static class SavedState extends BaseSavedState {
        int buttonId;

        /**
         * Constructor used when reading from a parcel. Reads the state of the superclass.
         *
         * @param source
         */
        public SavedState(Parcel source) {
            super(source);
            buttonId = source.readInt();
        }

        /**
         * Constructor called by derived classes when creating their SavedState objects
         *
         * @param superState The state of the superclass of this view
         */
        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(buttonId);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
            new Parcelable.Creator<SavedState>() {
                public SavedState createFromParcel(Parcel in) {
                    return new SavedState(in);
                }

                public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            };
    }

}