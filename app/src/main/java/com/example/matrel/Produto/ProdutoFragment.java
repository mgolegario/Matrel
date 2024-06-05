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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProdutoFragment extends Fragment{
    RecyclerView prodRec;
    FirebaseFirestore db;
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

        prodRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        produtoModelList = new ArrayList<>();
        produtoAdapter = new ProdutoAdapter(getActivity(), produtoModelList);
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
}