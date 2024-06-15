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
import android.widget.EditText;

import com.example.matrel.Auths.UserModel;
import com.example.matrel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class PagamentoCartaoFragment extends Fragment {
    EditText nome, numCartao, codSeg, dataVal;
    DatabaseReference database;
    FirebaseAuth auth;
    Button pagar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pagamento_cartao, container, false);
        nome = view.findViewById(R.id.edtNomeTitular);
        numCartao = view.findViewById(R.id.edtNumCartao);
        codSeg = view.findViewById(R.id.edtCodSeg);
        dataVal = view.findViewById(R.id.edtDataValid);
        database = FirebaseDatabase.getInstance().getReference();
        pagar = view.findViewById(R.id.btnPagar);
        auth = FirebaseAuth.getInstance();

pagar.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        final HashMap<String, Object> cartaoMap = new HashMap<>();
        cartaoMap.put("numCartao", Integer.parseInt(numCartao.getText().toString()));
        cartaoMap.put("codSeg", Integer.parseInt(codSeg.getText().toString()));
        cartaoMap.put("dataVal", dataVal.getText().toString());
        cartaoMap.put("nomeTitular", nome.getText().toString());
        database.child("Users").child(auth.getUid()).child("Cartao").setValue(cartaoMap);

        loadFragment(new PagamentoConcluidoFragment());
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