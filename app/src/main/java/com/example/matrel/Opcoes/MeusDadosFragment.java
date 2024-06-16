package com.example.matrel.Opcoes;

import static android.graphics.Color.rgb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.matrel.Auths.UserModel;
import com.example.matrel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MeusDadosFragment extends Fragment {
    DatabaseReference db;
    FirebaseAuth auth;
    EditText nome, email, senha;
    Button editar, salvar;
    UserModel query;
    Boolean tavaIgual;
    String enviaToast;
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
        editar = view.findViewById(R.id.btn_editar);
        salvar = view.findViewById(R.id.btn_salvar);

        db.child("Users").child(auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {

                    query = task.getResult().getValue(UserModel.class);

                    nome.setText(""+ query.getName());
                    email.setText(""+ query.getEmail());
                    senha.setText(""+ query.getSenha());
                }
            }
        });
        salvar.setBackgroundColor(rgb(126, 56, 2));

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nome.isEnabled()){
                    nome.setEnabled(true);
                    email.setEnabled(true);
                    senha.setEnabled(true);
                    salvar.setEnabled(true);
                    salvar.setBackgroundColor(rgb(    229, 103, 10));

                }else {
                    nome.setEnabled(false);
                    email.setEnabled(false);
                    senha.setEnabled(false);
                    salvar.setEnabled(false);
                    salvar.setBackgroundColor(rgb(126, 56, 2));
                }
            }
        });

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enviaToast = "igual";
                db.child("Users").child(auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            query = task.getResult().getValue(UserModel.class);
                        }
                    }
                });
                if (enviaToast == "igual"){ enviaToast = confereAlterado(nome, "name");}else{ confereAlterado(nome, "name");}
                if (enviaToast == "igual"){ enviaToast = confereAlterado(email, "email");}else{ confereAlterado(email, "email");}
                if (enviaToast == "igual"){ enviaToast = confereAlterado(senha, "senha");}else{ confereAlterado(senha, "senha");}


                if (enviaToast != "igual"){
                    Toast.makeText(getContext(), "Alterações salvas com sucesso!", Toast.LENGTH_SHORT).show();
                }
                nome.setEnabled(false);
                email.setEnabled(false);
                senha.setEnabled(false);
                salvar.setEnabled(false);
                salvar.setBackgroundColor(rgb(126, 56, 2));
            }
        });


        return view;
    }

    public String confereAlterado(EditText editText, String campo){
        switch (campo){
            case "name":
                if(editText.getText().toString() != query.getName()){
                final HashMap<String, Object> salvarMap = new HashMap<>();
                salvarMap.put(campo, editText.getText().toString());
                db.child("Users").child(auth.getUid()).updateChildren(salvarMap);
                return campo;

                 }else {
                return "igual";

                }

            case "email": if(editText.getText().toString() != query.getEmail().toString()){
                final HashMap<String, Object> salvarMap = new HashMap<>();
                salvarMap.put(campo, editText.getText().toString());
                db.child("Users").child(auth.getUid()).updateChildren(salvarMap);
                return campo;
            }else {
                return "igual";
            }

            case "senha": if(editText.getText().toString() != query.getSenha().toString()){
                final HashMap<String, Object> salvarMap = new HashMap<>();
                salvarMap.put(campo, editText.getText().toString());
                db.child("Users").child(auth.getUid()).updateChildren(salvarMap);
                return campo;
            }else {
                return "igual";
            }
            default:
                return "igual";
        }


    }
}