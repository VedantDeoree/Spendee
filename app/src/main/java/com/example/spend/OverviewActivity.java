package com.example.spend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;

public class OverviewActivity extends AppCompatActivity {

    private TextView budgetTv,todayTv,weekTv,monthTv,saveTv;
    private FirebaseAuth mAuth;
    private DatabaseReference budgetRef,expenseRef,userRef;
    private String onlineUserId="";

    private int totalAmountMonth=0;
    private int totalAmountBudget=0;
    private int totalAmountBudgetB=0;
    private int totalAmountBudgetC=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        budgetTv=findViewById(R.id.budget);
        todayTv=findViewById(R.id.today);
        weekTv=findViewById(R.id.week);
        monthTv=findViewById(R.id.month);
        saveTv=findViewById(R.id.save);

        mAuth=FirebaseAuth.getInstance();
        onlineUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        budgetRef= FirebaseDatabase.getInstance().getReference("Budget").child(onlineUserId);
        expenseRef=FirebaseDatabase.getInstance().getReference("Expenses").child(onlineUserId);
        userRef=FirebaseDatabase.getInstance().getReference("Users").child(onlineUserId);

        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists() && snapshot.getChildrenCount()>0){
                    for (DataSnapshot ds :  snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>)ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmountBudgetB+=pTotal;
                    }
                    totalAmountBudgetC = totalAmountBudgetB;
                    userRef.child("budget").setValue(totalAmountBudgetC);
                }else {
                    userRef.child("budget").setValue(0);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getBudgetAmount();
        getTodaySpentAmount();
        getWeekSpentAmount();
        getMonthSpentAmount();
        getSavings();
    }


    private void getBudgetAmount() {

        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists() && snapshot.getChildrenCount()>0){
                    for (DataSnapshot ds :  snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>)ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmountBudget+=pTotal;
                        budgetTv.setText(String.valueOf(totalAmountBudget) + " ₹");
                    }
                }else {
                    totalAmountBudget=0;
                    budgetTv.setText(String.valueOf(0) + "₹");


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTodaySpentAmount(){

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expenses").child(onlineUserId);
        Query query = reference.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int totalAmount = 0;
                for (DataSnapshot ds :  dataSnapshot.getChildren()){
                    Map<String, Object> map = (Map<String, Object>)ds.getValue();
                    Object total = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totalAmount+=pTotal;
                    todayTv.setText(totalAmount + "₹");
                }
                userRef.child("today").setValue(totalAmount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OverviewActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getWeekSpentAmount(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expenses").child(onlineUserId);
        Query query = reference.orderByChild("week").equalTo(weeks.getWeeks());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalAmount = 0;
                for (DataSnapshot ds :  dataSnapshot.getChildren()){
                    Map<String, Object> map = (Map<String, Object>)ds.getValue();
                    Object total = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totalAmount+=pTotal;
                    weekTv.setText(totalAmount + "₹");
                }
                userRef.child("week").setValue(totalAmount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getMonthSpentAmount(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expenses").child(onlineUserId);
        Query query = reference.orderByChild("month").equalTo(months.getMonths());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalAmount = 0;
                for (DataSnapshot ds :  dataSnapshot.getChildren()){
                    Map<String, Object> map = (Map<String, Object>)ds.getValue();
                    Object total = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totalAmount+=pTotal;
                    monthTv.setText(totalAmount + "₹");

                }
                userRef.child("month").setValue(totalAmount);
                totalAmountMonth = totalAmount;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getSavings(){
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    int budget;
                    if (snapshot.hasChild("budget")) {
                        budget = Integer.parseInt(snapshot.child("budget").getValue().toString());
                    } else {
                        budget = 0;
                    }
                    int monthSpending;
                    if (snapshot.hasChild("month")) {
                        monthSpending = Integer.parseInt(Objects.requireNonNull(snapshot.child("month").getValue().toString()));
                    } else {
                        monthSpending = 0;
                    }

                    int savings = budget - monthSpending;
                    saveTv.setText(savings + "₹");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OverviewActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}