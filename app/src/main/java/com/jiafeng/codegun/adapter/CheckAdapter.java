package com.jiafeng.codegun.adapter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiafeng.codegun.R;
import com.jiafeng.codegun.model.CheckModel;
import com.jiafeng.codegun.swipeitemview.SwipeItemView;
import com.jiafeng.codegun.swipeitemview.SwipeOnItemListener;

import java.util.ArrayList;

/**
 * Created by yangshuquan on 2018/3/2.
 */

public class CheckAdapter extends RecyclerView.Adapter<CheckAdapter.ViewHolder> implements
        SwipeOnItemListener.OnDeleteListener, View.OnClickListener {
    private ArrayList<CheckModel> mData;
    private OnItemClickListener mOnItemClickListener = null;

    private SwipeOnItemListener swipeOnItemListener = new SwipeOnItemListener();

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public SwipeOnItemListener getSwipeOnItemListener() {
        return swipeOnItemListener;
    }

    @Override
    public void onDeleteClick(final SwipeItemView curSwipeItemView) {
        new AlertDialog.Builder(curSwipeItemView.getContext())
                .setTitle("温馨提示")
                .setMessage("确定要删除吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        swipeOnItemListener.reset();
                        mOnItemClickListener.onItemDelete((Integer) curSwipeItemView.getTag());
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 这里点击取消之后可以进行的操作
                        swipeOnItemListener.shrink();
                    }
                }).show();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemDelete(int position);
    }

    public CheckAdapter(ArrayList<CheckModel> data) {
        this.mData = data;
        swipeOnItemListener.setOnDeleteListener(this);
    }

    public void setModels(ArrayList<CheckModel> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }

    @Override
    public CheckAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        SwipeItemView swipeItemView = new SwipeItemView(parent.getContext());
        swipeItemView.setLayoutParams(lp);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check, swipeItemView, false);
        swipeItemView.setContentView(v);
        swipeItemView.setOnSlideListener(swipeOnItemListener);
        swipeItemView.setOnClickListener(this);
        return new ViewHolder(swipeItemView);
    }

    @Override
    public void onBindViewHolder(CheckAdapter.ViewHolder holder, int position) {
        holder.swipeItemView.setTag(position);
        holder.swipeItemView.setItemCanSlide(true);

        holder.shopName.setText(mData.get(position).shopName);
        holder.checkNum.setText(mData.get(position).checkNum);
        holder.time.setText(mData.get(position).createTime);
        holder.checkStatus.setImageResource(mData.get(position).isCompile() ? R.drawable.compile : R.drawable.uncompile);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopName;
        TextView checkNum;
        TextView time;
        ImageView checkStatus;

        SwipeItemView swipeItemView;

        public ViewHolder(View itemView) {
            super(itemView);
            checkNum = itemView.findViewById(R.id.checkNum);
            shopName = itemView.findViewById(R.id.shopName);
            checkStatus = itemView.findViewById(R.id.checkStatus);
            time = itemView.findViewById(R.id.time);

            swipeItemView = (SwipeItemView) itemView;
        }
    }

}
