package com.example.matrel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class DepartamentosAdapter  extends RecyclerView.Adapter<DepartamentosAdapter.ViewHolder>{

    private Context context;
    private List<DepartamentosModel> departamentosModelList;

    public DepartamentosAdapter(Context context, List<DepartamentosModel> departamentosModelList) {
        this.context = context;
        this.departamentosModelList = departamentosModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.departamento_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(departamentosModelList.get(position).getImg_url()).into(holder.depImg);
        holder.nome.setText(departamentosModelList.get(position).getNome());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(position, item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return departamentosModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView depImg;
        TextView nome;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            depImg = itemView.findViewById(R.id.dep_img);
            nome = itemView.findViewById(R.id.dep_nome);

        }
    }
}
