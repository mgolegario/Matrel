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

public class DestaquesAdapter  extends RecyclerView.Adapter<DestaquesAdapter.ViewHolder>{

    private Context context;
    private List<DestaquesAdapter> destaquesAdapterList;

    public DestaquesAdapter(Context context, List<DestaquesAdapter> destaquesAdapterList) {
        this.context = context;
        this.destaquesAdapterList = destaquesAdapterList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.destaques_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Glide.with(context).load(destaquesAdapterList.get(position).getImg_url()).into(holder.destImg);
            holder.nome.setText(destaquesAdapterList.get(position).getNome());
            holder.avaliacoes.setText(destaquesAdapterList.get(position).getAvaliacoes());
    }

    @Override
    public int getItemCount() {
        return destaquesAdapterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView destImg;
        TextView nome, avaliacoes;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            destImg = itemView.findViewById(R.id.destaques_img);
            nome = itemView.findViewById(R.id.destaques_text);
            avaliacoes = itemView.findViewById(R.id.avaliacoes);
        }
    }
}
