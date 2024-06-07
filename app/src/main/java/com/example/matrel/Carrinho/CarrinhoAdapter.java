package com.example.matrel.Carrinho;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.matrel.Carrinho.CarrinhoModel;
import com.example.matrel.R;

import java.util.List;

public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.ViewHolder>{
    
    private Context context;
    private List<CarrinhoModel> carrinhoModelList;

    public CarrinhoAdapter(Context context, List<CarrinhoModel> carrinhoModelList) {
        this.context = context;
        this.carrinhoModelList = carrinhoModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.carrinho_card, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(carrinhoModelList.get(position).getImg_url()).into(holder.cartImg);
        holder.nome.setText(carrinhoModelList.get(position).getNome());
        holder.quant.setText(carrinhoModelList.get(position).getQuantidade());
        holder.preco.setText("R$ " + (carrinhoModelList.get(position).getPreco() * Integer.parseInt(holder.quant.getText().toString())));
    }

    @Override
    public int getItemCount() {
        return carrinhoModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cartImg;
        Integer quantidade_num;
        TextView nome, preco, quant;
        ImageView aumentar, diminuir;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cartImg = itemView.findViewById(R.id.img_carrinho);
            nome = itemView.findViewById(R.id.nome_carrinho);
            preco = itemView.findViewById(R.id.preco_carrinho);
            quant = itemView.findViewById(R.id.tv_quant);
            aumentar = itemView.findViewById(R.id.aumentar);
            diminuir = itemView.findViewById(R.id.diminuir);

            aumentar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                quantidade_num += 1;
                quant.setText(""+quantidade_num);
                }
            });
            diminuir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quantidade_num != 1){
                        quantidade_num -= 1;
                        quant.setText(""+quantidade_num);
                    }

                }
            });

        }
    }
}
