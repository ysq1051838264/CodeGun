package com.jiafeng.codegun.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiafeng.codegun.R;
import com.jiafeng.codegun.model.CheckDetailModel;

import java.util.ArrayList;

/**
 * Created by yangshuquan on 2018/3/2.
 */

public class CheckDetailAdapter extends RecyclerView.Adapter<CheckDetailAdapter.ViewHolder> implements View.OnClickListener {

    private ArrayList<CheckDetailModel> mData;
    private OnItemClickListener mOnItemClickListener = null;

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

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public CheckDetailAdapter(ArrayList<CheckDetailModel> data) {
        this.mData = data;
    }

    public void setModels(ArrayList<CheckDetailModel> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }

    @Override
    public CheckDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_detail, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        v.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CheckDetailAdapter.ViewHolder holder, int position) {
        CheckDetailModel model = mData.get(position);
        holder.oldBarCode.setText(model.oldBarCode);
        holder.barCode.setText(model.barCode);
        holder.goodsName.setText(model.goodsName);

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView barCode;
        TextView oldBarCode;
        TextView goodsName;


        public ViewHolder(View itemView) {
            super(itemView);
            barCode = itemView.findViewById(R.id.barCode);
            oldBarCode = itemView.findViewById(R.id.oldBarCode);
            goodsName = itemView.findViewById(R.id.goodsName);
        }
    }

}
