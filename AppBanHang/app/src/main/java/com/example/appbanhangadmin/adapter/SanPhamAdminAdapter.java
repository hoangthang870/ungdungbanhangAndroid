package com.example.appbanhangadmin.adapter;

        import android.content.Context;
        import android.content.Intent;
        import android.view.ContextMenu;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.ProgressBar;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.bumptech.glide.Glide;
        import com.example.appbanhangadmin.Interface.ItemClickListener;
        import com.example.appbanhang.R;
        import com.example.appbanhangadmin.activity.ChiTietActivity;
        import com.example.appbanhangadmin.activity.ChiTietAdminActivity;
        import com.example.appbanhangadmin.model.EventBus.SuaXoaEvent;
        import com.example.appbanhangadmin.model.SanPhamMoi;
        import com.example.appbanhangadmin.utils.Utils;

        import org.greenrobot.eventbus.EventBus;

        import java.text.DecimalFormat;
        import java.util.List;

public class SanPhamAdminAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SanPhamMoi> array;
    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    public SanPhamAdminAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType ==VIEW_TYPE_DATA){
            View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.item_thoitrangnam,parent,false);
            return new MyViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
            return new LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            SanPhamMoi sanpham = array.get(position);
            myViewHolder.tensp.setText(sanpham.getTensanpham().trim());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            myViewHolder.giasp.setText("Giá: "+decimalFormat.format(Double.parseDouble(sanpham.getGiasanpham()))+"Đ");
            if(sanpham.getHinhanhsanpham().contains( "https" )){
                Glide.with(context).load(sanpham.getHinhanhsanpham()).into(myViewHolder.imghinhanh);
            }else{
                String hinh = Utils.BASE_URL+"images/"+sanpham.getHinhanhsanpham();
                Glide.with(context).load(hinh).into(myViewHolder.imghinhanh);
            }
            myViewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    if(!isLongClick){
                        //click
                        Intent intent= new Intent(context, ChiTietAdminActivity.class);
                        intent.putExtra( "chitiet",sanpham);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    }
                    else{
                        EventBus.getDefault().postSticky( new SuaXoaEvent( sanpham ) );
                    }
                }
            });
        }else{
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return array.get(position)==null ? VIEW_TYPE_LOADING:VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return array.size();
    }
    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnCreateContextMenuListener {
        TextView tensp,giasp,idsp;
        ImageView imghinhanh;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tensp = itemView.findViewById(R.id.itemttn_ten);
            giasp = itemView.findViewById(R.id.itemttn_gia);
            imghinhanh =itemView.findViewById(R.id.itemttn_image);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener( this );
            itemView.setOnLongClickListener( this );


        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(0,0,getAdapterPosition(),"Sửa");
            contextMenu.add(0,0,getAdapterPosition(),"Xóa");
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick( view,getAdapterPosition(),true );
            return false;
        }
    }
}
