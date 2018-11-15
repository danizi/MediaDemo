package com.bangke.lib.common.base.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * ViewHolder抽象类
 *
 * @param <D> 绑定数据的对象
 */
public abstract class AbsViewHolder<D> extends RecyclerView.ViewHolder {
    protected Context context;
    private View itemView;
    private SparseArray<View> views;
    private int count = 0;
    private OnItemClickListener onItemClickListener;

    /**
     * todo 不应该写在这里，应该到adapter中去
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    protected void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        for (int i = 0; i < views.size(); i++) {
            View view = views.get(i);
            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, finalI);
                }
            });
        }
    }


    public AbsViewHolder(View parent, int layoutId) {
        super(getItemView(parent, layoutId));
    }

    public AbsViewHolder(View itemView) {
        super(itemView);
        views = new SparseArray<>();
        this.itemView = itemView;
        this.context = itemView.getContext();
    }

    protected static View getItemView(View parent, int layoutID) {
        Context context = parent.getContext();
        return LayoutInflater.from(context).inflate(layoutID, (ViewGroup) parent, false);
    }

    public View getItemView() {
        return itemView;
    }

    protected ImageView getImg(int id) {
        return findViewById(id);
    }

    protected TextView getTv(int id) {
        return findViewById(id);
    }

    protected Button getBtn(int id) {
        return findViewById(id);
    }

    protected RecyclerView getRv(int id) {
        return findViewById(id);
    }

    protected RelativeLayout getRl(int id) {
        return findViewById(id);
    }

    protected ConstraintLayout getCl(int id) {
        return findViewById(id);
    }

    protected LinearLayout getll(int id) {
        return findViewById(id);
    }

    protected <V extends View> V findViewById(int id) {
        View view = views.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            views.put(count, view);
            count++;
        }
        return (V) view;
    }

    /**
     * 完成View绑定数据的操作
     *
     * @param data 实体bean
     */
    public abstract void bind(D data);

    /**
     * 完成View绑定数据的操作
     *
     * @param datas 数据集
     */
    public void bind(List<D> datas, int position) {
    }
}
