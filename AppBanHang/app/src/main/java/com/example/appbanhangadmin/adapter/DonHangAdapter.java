package com.example.appbanhangadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhangadmin.model.DonHang;

import java.text.DecimalFormat;
import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<DonHang> listDonhang;
    long tongtien;
    public DonHangAdapter(Context context, List<DonHang> listDonhang) {
        this.context = context;
        this.listDonhang = listDonhang;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_xemdonhang,parent,false );
        return new MyViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonHang donHang = listDonhang.get(position);
   //     holder.txtdonhang.setText( " "+ donHang.getId() );
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien = Long.parseLong( donHang.getTongtien() );
        holder.txttongtien.setText(  decimalFormat.format( tongtien )+ " đ");
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.reChitiet.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount( donHang.getItem().size() );
        // adapter chitiet
        ChitietAdapter chitietAdapter = new ChitietAdapter(context, donHang.getItem() );
        holder.reChitiet.setLayoutManager( layoutManager );
        holder.reChitiet.setAdapter( chitietAdapter );
        holder.reChitiet.setRecycledViewPool( viewPool );

    }

    @Override
    public int getItemCount() {
        return listDonhang.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtdonhan,txttongtien;
        RecyclerView reChitiet;
        public MyViewHolder(@NonNull View itemView) {
            super( itemView );
       //     txtdonhang = itemView.findViewById( R.id.iddonhang );
            txttongtien = itemView.findViewById( R.id.idTongtienxemdonhang );
            reChitiet = itemView.findViewById( R.id.recycleview_itemxemdonhang );
        }

    }
}
