package karma.converter.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import karma.converter.listeners.OnItemClickListener;

import java.util.ArrayList;

public abstract class BaseAdapter<T, S extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<S> {

    protected OnItemClickListener<T> onItemClickListener;
    protected Context context;
    private ArrayList<T> mArrayList = new ArrayList<>();

    public BaseAdapter(Context context) {
        this.context = context;
    }

    public void setDataArrayList(ArrayList<T> mArrayList) {
        this.mArrayList = mArrayList;
    }

    public void addItem(T object) {
        this.mArrayList.add(object);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<T> arrayList) {
        this.mArrayList.clear();
        this.mArrayList.addAll(arrayList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.mArrayList.clear();
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<T> arrayList) {
        this.mArrayList.addAll(arrayList);
        notifyDataSetChanged();
    }

    public void remove(T item) {
        if (mArrayList.remove(item)) {
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ArrayList<T> getList() {
        return mArrayList;
    }

    public T getListItem(int position) {
        if (position >= mArrayList.size()) {
            return null;
        }
        return mArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public void updateItem(int pos, T memberModel) {
        mArrayList.set(pos, memberModel);
        notifyDataSetChanged();
    }




}
