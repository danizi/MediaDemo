package com.bangke.lib.common.base.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.List;

public abstract class AbsRvAdapter<B> extends RecyclerView.Adapter<AbsViewHolder> {

    public List<B> datas;

    public AbsRvAdapter(List<B> datas) {
        this.datas = datas;
    }

    private SparseArray<AbsViewHolder> holders = new SparseArray<>();

    /**
     * 注册ViewHolder
     *
     * @param itemType
     * @param holder
     */
    public void regist(int itemType, AbsViewHolder holder) {
        holders.put(itemType, holder);
    }

    @NonNull
    @Override
    public AbsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        holders.get(viewType);
        return createHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull AbsViewHolder holder, int position) {
        bindHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return datas.isEmpty() ? 0 : datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(position);
    }

    protected abstract AbsViewHolder createHolder(ViewGroup parent, int viewType);

    protected void bindHolder(AbsViewHolder holder, int position) {
        try {
            holder.bind(datas.get(position));
            holder.bind(datas, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract int getViewType(int position);

}
