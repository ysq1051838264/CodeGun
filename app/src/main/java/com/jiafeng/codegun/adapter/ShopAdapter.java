package com.jiafeng.codegun.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiafeng.codegun.R;
import com.jiafeng.codegun.model.ShopModel;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by yangshuquan on 2018/3/2.
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> implements View.OnClickListener {

    private ArrayList<ShopModel> mData;
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

    public ShopAdapter(ArrayList<ShopModel> data) {
        this.mData = data;
    }

    @Override
    public ShopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        v.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShopAdapter.ViewHolder holder, int position) {
        holder.shopInfo.setText(mData.get(position).shopInfo);
        holder.shopName.setText(mData.get(position).shopName);
        Picasso.with(holder.itemView.getContext()).load(mData.get(position).shopImage).into(holder.shopImage);

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView shopName;
        TextView shopInfo;
        ImageView shopImage;

        public ViewHolder(View itemView) {
            super(itemView);
            shopInfo = itemView.findViewById(R.id.shopInfoTv);
            shopName = itemView.findViewById(R.id.shopNameTv);
            shopImage = itemView.findViewById(R.id.shopImage);
        }
    }

}
