package com.example.matrel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.FirebaseDatabase;


public class CadastroFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseDatabase database;
    EditText edtEmail, edtSenha, edtNome;
    Button cadastro;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro, container, false);
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        edtEmail = view.findViewById(R.id.edt_emailCadastro);
        edtSenha = view.findViewById(R.id.edt_senhaCadastro);
        edtNome = view.findViewById(R.id.edt_nome);
        cadastro = view.findViewById(R.id.btn_cadastro);

        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
        return view;
    }

    private void createUser() {
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();
        String nome  = edtNome.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(getActivity(), "Email está vazio", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(senha)){
            Toast.makeText(getActivity(), "Senha está vazia", Toast.LENGTH_SHORT).show();
            return;
        }

       auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            UserModel userModel = new UserModel(nome, email, senha);
                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(userModel);

                            Toast.makeText(getActivity(), "Cadastro concluído", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getActivity(), "Error:"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}