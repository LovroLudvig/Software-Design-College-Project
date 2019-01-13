package com.example.lovro.myapplication.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.adapters.CommentAdapter;
import com.example.lovro.myapplication.adapters.TransactionAdapter;
import com.example.lovro.myapplication.domain.Comment;
import com.example.lovro.myapplication.domain.Transaction;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.lovro.myapplication.network.InitApiService.apiService;

public class TransactionsActivity extends BasicActivity {

    private RecyclerView recyclerView;
    private TransactionAdapter transactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        recyclerView = findViewById(R.id.transactions_recycleView);

        initRecyclerView();
        getAllTransactions();


    }

    private void getAllTransactions() {
        show_loading("Loading...");
        apiService.getAllTransactions(getUserAuth()).enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                stop_loading();
                if(response.isSuccessful()){
                    Collections.reverse(response.body());
                    initAdapter(response.body());
                }else{
                    if(call.isCanceled()){
                        //nothing
                    }else{
                        try {
                            showError(response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                stop_loading();
                Toast.makeText(TransactionsActivity.this,"Error loading transactions", Toast.LENGTH_LONG).show();
                t.printStackTrace();


            }
        });
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initAdapter(List<Transaction> transactions){
        transactionAdapter = new TransactionAdapter(transactions);
        recyclerView.setAdapter(transactionAdapter);
    }
}
