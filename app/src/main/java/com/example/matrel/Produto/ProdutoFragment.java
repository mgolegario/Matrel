package com.example.matrel.Produto;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.matrel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProdutoFragment extends Fragment implements ProdutoInterface{
    RecyclerView prodRec;
    FirebaseFirestore db;
    FirebaseAuth auth;
    List<ProdutoModel> produtoModelList;
    ProdutoAdapter produtoAdapter;
    long quantDocs;
    String nome;
    Bundle b;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_produto, container, false);
        db = FirebaseFirestore.getInstance();
        prodRec = view.findViewById(R.id.recProd);
        auth = FirebaseAuth.getInstance();
        prodRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        produtoModelList = new ArrayList<>();
        produtoAdapter = new ProdutoAdapter(getActivity(), produtoModelList, this);
        prodRec.setAdapter(produtoAdapter);
        b = this.getArguments();

        if (b != null && b.getString("nome") != null) {
            nome = b.getString("nome");


            db.collection("TodosProdutos")
                    .whereEqualTo("nome", nome)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    ProdutoModel produtoModel = document.toObject(ProdutoModel.class);
                                    produtoModelList.add(produtoModel);
                                    produtoAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        return view;
    }

    @Override
    public void onItemClick(int position, int qualApertou) {

        if (qualApertou == 0) {
            final HashMap<String, Object> carrinhoMap = new HashMap<>();
            carrinhoMap.put("nome", produtoModelList.get(position).getNome());
            carrinhoMap.put("preco", produtoModelList.get(position).getPreco());
            carrinhoMap.put("img_url", produtoModelList.get(position).getImg_url());
            carrinhoMap.put("quantidade", 1);
            if (auth.getCurrentUser() != null) {
                final HashMap<String, Object> valorTotalMap = new HashMap<>();
                valorTotalMap.put("valorTotal", 0);
                db.collection("AddToCart").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        task.getResult().getReference().set(valorTotalMap);
                    }
                });
                Query query = db.collection("AddToCart").document(auth.getCurrentUser().getUid()).collection("CurrentUser").whereEqualTo("nome", produtoModelList.get(position).getNome());
                AggregateQuery countQuery = query.count();
                countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            AggregateQuerySnapshot snapshot = task.getResult();
                            quantDocs = snapshot.getCount();
                            if (quantDocs == 0) {
                                db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                                        .collection("CurrentUser").add(carrinhoMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                Toast.makeText(getContext(), "Adicionado ao carrinho", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }else{
                                Toast.makeText(getContext(), "O produto já foi adicionado ao carrinho", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }else {
                Toast.makeText(getContext(), "Crie uma conta para utilizar o Carrinho", Toast.LENGTH_SHORT).show();
            }
        }else if (qualApertou == 1){

            final HashMap<String, Object> favMap = new HashMap<>();
            favMap.put("nome", produtoModelList.get(position).getNome());
            favMap.put("preco", produtoModelList.get(position).getPreco());
            favMap.put("img_url", produtoModelList.get(position).getImg_url());
            favMap.put("avaliacoes", produtoModelList.get(position).getAvaliacoes());
            favMap.put("type", produtoModelList.get(position).getType());
            favMap.put("destaque", produtoModelList.get(position).getDestaque());
            favMap.put("procurado", produtoModelList.get(position).getProcurado());
            if (auth.getCurrentUser() != null) {
                Query query = db.collection("Favoritos").document(auth.getCurrentUser().getUid()).collection("CurrentUser").whereEqualTo("nome", produtoModelList.get(position).getNome());
                AggregateQuery countQuery = query.count();
                countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            AggregateQuerySnapshot snapshot = task.getResult();
                            quantDocs = snapshot.getCount();
                            if (quantDocs == 0) {
                                db.collection("Favoritos").document(auth.getCurrentUser().getUid())
                                        .collection("CurrentUser").add(favMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                Toast.makeText(getContext(), "Adicionado aos favoritos", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }else{
                                Toast.makeText(getContext(), "O produto já foi adicionado aos favoritos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }else{
                Toast.makeText(getContext(), "Crie uma conta para utilizar os Favoritos", Toast.LENGTH_SHORT).show();
            }
        }
    }
}