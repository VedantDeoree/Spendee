package com.example.spend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeekSpendingActivity extends AppCompatActivity {

    private TextView totalWeekAmountTextView;
    private ProgressBar progressBar;
    private RecyclerView recycleView;

    private WeekSpendingAdapter weekSpendingAdapter;
    private List<Data> myDataList;

    private FirebaseAuth mAuth;
    private String onlineUserId = "";
    private DatabaseReference expenseRef;

    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_spending);

        totalWeekAmountTextView = findViewById(R.id.totalWeekAmountTextView);
        progressBar = findViewById(R.id.progressBar);

        recycleView = findViewById(R.id.recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();

        myDataList = new ArrayList<>();
        weekSpendingAdapter = new WeekSpendingAdapter(WeekSpendingActivity.this,myDataList);
        recycleView.setAdapter(weekSpendingAdapter);

        if(getIntent().getExtras() != null){
            type = getIntent().getStringExtra("type");
            if(type.equals("week")){
                readWeekSpendingItems();
            }else if(type.equals("month")){
                readMonthSpendingItems();
            }
        }


    }

    private void readMonthSpendingItems() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        expenseRef = FirebaseDatabase.getInstance().getReference("Expenses").child(onlineUserId);
        Query query = expenseRef.orderByChild("month").equalTo(months.getMonths());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myDataList.clear();
                for(DataSnapshot snap: snapshot.getChildren()){
                    Data data = snap.getValue(Data.class);
                    myDataList.add(data);
                }
                weekSpendingAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                int totalAmount = 0;
                for(DataSnapshot s : snapshot.getChildren()){
                    Map<String,Object> map = (Map<String,Object>)s.getValue();
                    Object total = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totalAmount += pTotal;

                    totalWeekAmountTextView.setText("Total Month's Spending: " +totalAmount + "₹");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readWeekSpendingItems() {

        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        expenseRef = FirebaseDatabase.getInstance().getReference("Expenses").child(onlineUserId);
        Query query = expenseRef.orderByChild("week").equalTo(weeks.getWeeks());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myDataList.clear();
                for(DataSnapshot snap: snapshot.getChildren()){
                    Data data = snap.getValue(Data.class);
                    myDataList.add(data);
                }
                weekSpendingAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                int totalAmount = 0;
                for(DataSnapshot s : snapshot.getChildren()){
                    Map<String,Object> map = (Map<String,Object>)s.getValue();
                    Object total = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totalAmount += pTotal;

                    totalWeekAmountTextView.setText("Total Week's Spending: " +totalAmount + "₹");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}