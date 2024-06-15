package com.example.matrel.Opcoes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.matrel.Auths.UserModel;
import com.example.matrel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CartoesFragment extends Fragment {
    DatabaseReference db;
    FirebaseAuth auth;
    EditText nome, numCartao, codSeg, dataVal;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cartoes, container, false);
        db = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        nome = view.findViewById(R.id.edtNomeTitular2);
        numCartao = view.findViewById(R.id.edtNumCartao2);
        codSeg = view.findViewById(R.id.edtCodSeg2);
        dataVal = view.findViewById(R.id.edtDataValid2);

        db.child("Users").child(auth.getUid()).child("Cartao").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {

                    CartaoModel cartaoModel = task.getResult().getValue(CartaoModel.class);
                    nome.setText(cartaoModel.nomeTitular);
                    numCartao.setText(""+cartaoModel.numCartao);
                    codSeg.setText(""+cartaoModel.segCod);
                    dataVal.setText(cartaoModel.dataVal);

                }
            }
        });

        return view;
    }
}