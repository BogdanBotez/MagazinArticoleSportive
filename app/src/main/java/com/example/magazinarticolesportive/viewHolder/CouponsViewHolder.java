package com.example.magazinarticolesportive.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magazinarticolesportive.R;

public class CouponsViewHolder extends RecyclerView.ViewHolder {

    public TextView couponName, couponValue;

    public CouponsViewHolder(@NonNull View itemView) {
        super(itemView);

        couponName = itemView.findViewById(R.id.name_coupons_layout);
        couponValue = itemView.findViewById(R.id.value_coupons_layout);
    }



}
