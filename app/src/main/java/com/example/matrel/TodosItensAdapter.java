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

public class TodosItensAdapter  extends RecyclerView.Adapter<TodosItensAdapter.ViewHolder>{

    private Context context;
    private List<DestaquesModel> destaquesModelList;

    public TodosItensAdapter(Context context, List<DestaquesModel> destaquesModelList) {
        this.context = context;
        this.destaquesModelList = destaquesModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.todos_itens_card, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(destaquesModelList.get(position).getImg_url()).into(holder.destImg);
        holder.nome.setText(destaquesModelList.get(position).getNome());
        holder.avaliacoes.setText("("+destaquesModelList.get(position).getAvaliacoes()+")");
        holder.preco.setText("R$ " + destaquesModelList.get(position).getPreco());
    }

    @Override
    public int getItemCount() {
        return destaquesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView destImg;
        TextView nome, avaliacoes, preco;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            destImg = itemView.findViewById(R.id.img_todos);
            nome = itemView.findViewById(R.id.nome_todos);
            avaliacoes = itemView.findViewById(R.id.avaliacoes_todos);
            preco = itemView.findViewById(R.id.preco_todos);
        }
    }
}