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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
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
    public void onItemClick(int position) {
        final HashMap<String,Object> carrinhoMap = new HashMap<>();
        carrinhoMap.put("nome", produtoModelList.get(position).getNome());
        carrinhoMap.put("preco", produtoModelList.get(position).getPreco());
        carrinhoMap.put("img_url", produtoModelList.get(position).getImg_url());

        db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("CurrentUser").add(carrinhoMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(getContext(), "Adicionado ao carrinho", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}