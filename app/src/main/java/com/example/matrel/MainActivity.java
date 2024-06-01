package com.example.matrel;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    TextView tvHeader;
    EditText searchBox;
    ImageView carrinho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        frameLayout = findViewById(R.id.frameLayout);
        tvHeader = findViewById(R.id.textView);
        searchBox = findViewById(R.id.search_box);
        carrinho = findViewById(R.id.carrinho);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                int itemId = menuItem.getItemId();

                if (itemId == R.id.home){
                    searchBox.setVisibility(View.VISIBLE);
                    carrinho.setVisibility(View.VISIBLE);
                    tvHeader.setVisibility(View.INVISIBLE);
                }else{
                    searchBox.setVisibility(View.INVISIBLE);
                    carrinho.setVisibility(View.INVISIBLE);
                    tvHeader.setVisibility(View.VISIBLE);
                }


                if (itemId == R.id.home){
                    homeClicked();
                }else if (itemId == R.id.departamentos){
                    tvHeader.setText("Departamentos");
                    departamentosClicked();
                } else if (itemId == R.id.favoritos){
                    tvHeader.setText("Favoritos");
                    favoritosClicked();
                }else if (itemId == R.id.conta){
                    tvHeader.setText("Conta");
                    contaClicked();
                }
                if (itemId == R.id.home){
                    return true;
                }else if (itemId == R.id.departamentos){
                    return true;
                } else if (itemId == R.id.favoritos){
                    return true;
                }else if (itemId == R.id.conta) {
                    return true;
                }
                return true;
            }

        });

       homeClicked();



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
    private void homeClicked(){
        loadFragment(new HomeFragment());
    }
    private void departamentosClicked(){
        loadFragment(new DepartamentosFragment());
    }
    private void favoritosClicked(){
        loadFragment(new FavoritosFragment());
    }
    private void contaClicked(){
        loadFragment(new ContaFragment());
    }
}


