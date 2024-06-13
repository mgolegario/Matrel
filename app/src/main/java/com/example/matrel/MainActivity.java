package com.example.matrel;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.matrel.Auths.ContaFragment;
import com.example.matrel.Auths.LoginFragment;
import com.example.matrel.Carrinho.CarrinhoFragment;
import com.example.matrel.Departamentos.DepartamentosFragment;
import com.example.matrel.Destaques.HomeFragment;
import com.example.matrel.Favoritos.FavoritosFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    TextView tvHeader;
    EditText searchBox;
    ImageView carrinho;
    FirebaseAuth auth;


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
        auth = FirebaseAuth.getInstance();

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
                    if(auth.getCurrentUser() != null){

                        loadFragment(new ContaFragment());
                    }else{
                        loginClicked();
                    }

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

        Fragment myFragment = (Fragment) getSupportFragmentManager().findFragmentByTag("produto");
        if (myFragment != null && myFragment.isVisible()) {

            tvHeader.setVisibility(View.VISIBLE);
            searchBox.setVisibility(View.INVISIBLE);
        }

       homeClicked();

carrinho.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        loadFragment(new CarrinhoFragment());
    }
});

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
    private void loginClicked(){
        loadFragment(new LoginFragment());
    }
}


