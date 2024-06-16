package com.example.matrel.Pagamento;

import static android.content.Context.CLIPBOARD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.matrel.R;

import java.util.Random;

public class PagamentoPixFragment extends Fragment {
Button gerarOutra, jaPago;
    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
EditText edtChavePix;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pagamento_pix, container, false);
        gerarOutra = view.findViewById(R.id.btnGerarOutra);
        jaPago = view.findViewById(R.id.btnJaPaguei);
        edtChavePix = view.findViewById(R.id.edtChavePix);
        edtChavePix.setText(getRandomString(30));
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(edtChavePix.getText().toString());
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Pix Copia e Cola", edtChavePix.getText().toString());
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(getContext(), "Código copiado para Área de Transfêrencia", Toast.LENGTH_SHORT).show();


        gerarOutra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtChavePix.setText(getRandomString(30));
                if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(edtChavePix.getText().toString());
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Pix Copia e Cola", edtChavePix.getText().toString());
                    clipboard.setPrimaryClip(clip);
                }
                Toast.makeText(getContext(), "Código copiado para Área de Transfêrencia", Toast.LENGTH_SHORT).show();
            }
        });

        jaPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new PagamentoConcluidoFragment());
            }
        });


        return view;
    }
    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public String getRandomString(int sizeOfRandomString)
    {
        final Random random= new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

}