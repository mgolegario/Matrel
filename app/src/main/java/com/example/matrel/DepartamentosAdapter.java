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

    private final DepartamentosInterface departamentosInterface;
    private Context context;
    private List<DepartamentosModel> departamentosModelList;

    public DepartamentosAdapter(Context context, List<DepartamentosModel> departamentosModelList, DepartamentosInterface departamentosInterface) {
        this.context = context;
        this.departamentosModelList = departamentosModelList;
        this.departamentosInterface = departamentosInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.departamento_card, parent, false), departamentosInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(departamentosModelList.get(position).getImg_url()).into(holder.depImg);
        holder.nome.setText(departamentosModelList.get(position).getNome());

    }

    @Override
    public int getItemCount() {
        return departamentosModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView depImg;
        TextView nome;
        public ViewHolder(@NonNull View itemView, DepartamentosInterface departamentosInterface) {
            super(itemView);

            depImg = itemView.findViewById(R.id.dep_img);
            nome = itemView.findViewById(R.id.dep_nome);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(departamentosInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            departamentosInterface.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
