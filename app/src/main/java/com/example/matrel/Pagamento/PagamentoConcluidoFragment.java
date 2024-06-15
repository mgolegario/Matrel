package com.example.matrel.Pagamento;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.matrel.Destaques.HomeFragment;
import com.example.matrel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.function.Consumer;

public class PagamentoConcluidoFragment extends Fragment {
    Button btnok;
    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pagamento_concluido, container, false);
        btnok = view.findViewById(R.id.buttonOk);
db = FirebaseFirestore.getInstance();
auth = FirebaseAuth.getInstance();

db.collection("AddToCart").document(auth.getUid()).collection("CurrentUser").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {

        task.getResult().getDocuments().forEach(new Consumer<DocumentSnapshot>() {
            @Override
            public void accept(DocumentSnapshot documentSnapshot) {
                final HashMap<String, Object> compradoMap = new HashMap<>();
                compradoMap.put("nome", documentSnapshot.get("nome"));
                compradoMap.put("preco", documentSnapshot.get("preco"));
                compradoMap.put("img_url", documentSnapshot.get("img_url"));
                compradoMap.put("quantidade", documentSnapshot.get("quantidade"));
                db.collection("Comprado").document(auth.getUid()).collection("CurrentUser").add(compradoMap);
                documentSnapshot.getReference().delete();
            }
        });
    }
});

        db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .update("valorTotal", 0);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new HomeFragment());
            }
        });

        return view;
    }
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}