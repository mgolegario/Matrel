package com.example.matrel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.matrel.Auths.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MeusDadosFragment extends Fragment {
    DatabaseReference db;
    FirebaseAuth auth;
    EditText nome, email, senha;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meus_dados, container, false);
        nome = view.findViewById(R.id.edt_nomeComp);
        email = view.findViewById(R.id.edt_email);
        senha = view.findViewById(R.id.edt_senha);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        db.child("Users").child(auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {

                    UserModel query = task.getResult().getValue(UserModel.class);

                    nome.setText(""+ query.getName());
                    email.setText(""+ query.getEmail());
                    senha.setText(""+ query.getSenha());
                }
            }
        });

        return view;
    }
}