package com.example.spend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MonthlyAnalyticsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String onlineUserId = "";
    private DatabaseReference expensesRef;
    private String[] type = {"Transport", "Food", "House", "Entertainment", "Education", "Clothes", "Health", "Personal","Other"};
    public int[] values = {0, 0, 0, 0, 0, 0, 0, 0, 0};

    private TextView totalBudgetAmountTextView, analyticsTransportAmount,analyticsFoodAmount,analyticsHouseExpensesAmount,analyticsEntertainmentAmount;
    private TextView analyticsEducationAmount,analyticsCharityAmount,analyticsApparelAmount,analyticsHealthAmount,analyticsPersonalExpensesAmount,analyticsOtherAmount, monthSpentAmount;

    private RelativeLayout linearLayoutFood,linearLayoutTransport,linearLayoutFoodHouse,linearLayoutEntertainment,linearLayoutEducation;
    private RelativeLayout linearLayoutCharity,linearLayoutApparel,linearLayoutHealth,linearLayoutPersonalExp,linearLayoutOther, linearLayoutAnalysis;

    private PieChart pieChart;

    private TextView progress_ratio_transport,progress_ratio_food,progress_ratio_house,progress_ratio_ent,progress_ratio_edu,progress_ratio_cha, progress_ratio_app,progress_ratio_hea,progress_ratio_per,progress_ratio_oth, monthRatioSpending;
    private ImageView status_Image_transport, status_Image_food,status_Image_house,status_Image_ent,status_Image_edu,status_Image_cha,status_Image_app,status_Image_hea,status_Image_per,status_Image_oth, monthRatioSpending_Image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_analytics);

        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();
        expensesRef = FirebaseDatabase.getInstance().getReference("Expenses").child(onlineUserId);


        totalBudgetAmountTextView = findViewById(R.id.totalBudgetAmountTextView);

        //general analytic
        monthSpentAmount = findViewById(R.id.monthSpentAmount);
        linearLayoutAnalysis = findViewById(R.id.linearLayoutAnalysis);
        //monthRatioSpending = findViewById(R.id.monthRatioSpending);
        //monthRatioSpending_Image = findViewById(R.id.monthRatioSpending_Image);

        analyticsTransportAmount = findViewById(R.id.analyticsTransportAmount);
        analyticsFoodAmount = findViewById(R.id.analyticsFoodAmount);
        analyticsHouseExpensesAmount = findViewById(R.id.analyticsHouseExpensesAmount);
        analyticsEntertainmentAmount = findViewById(R.id.analyticsEntertainmentAmount);
        analyticsEducationAmount = findViewById(R.id.analyticsEducationAmount);
        analyticsApparelAmount = findViewById(R.id.analyticsApparelAmount);
        analyticsHealthAmount = findViewById(R.id.analyticsHealthAmount);
        analyticsPersonalExpensesAmount = findViewById(R.id.analyticsPersonalExpensesAmount);
        analyticsOtherAmount = findViewById(R.id.analyticsOtherAmount);

        //Relative layouts views
        linearLayoutTransport = findViewById(R.id.linearLayoutTransport);
        linearLayoutFood = findViewById(R.id.linearLayoutFood);
        linearLayoutFoodHouse = findViewById(R.id.linearLayoutFoodHouse);
        linearLayoutEntertainment = findViewById(R.id.linearLayoutEntertainment);
        linearLayoutEducation = findViewById(R.id.linearLayoutEducation);
        linearLayoutApparel = findViewById(R.id.linearLayoutApparel);
        linearLayoutHealth = findViewById(R.id.linearLayoutHealth);
        linearLayoutPersonalExp = findViewById(R.id.linearLayoutPersonalExp);
        linearLayoutOther = findViewById(R.id.linearLayoutOther);
        linearLayoutAnalysis=findViewById(R.id.linearLayoutAnalysis);




        //anyChartView
        pieChart = findViewById(R.id.pieChart);

        getTotalMonthsTransportExpense();
        getTotalMonthsFoodExpense();
        getTotalMonthsHouseExpense();
        getTotalMonthsEntertainmentExpenses();
        getTotalMonthsEducationExpenses();
        getTotalMonthsApparelExpenses();
        getTotalMonthsHealthExpenses();
        getTotalMonthsPersonalExpenses();
        getTotalMonthsOtherExpenses();
        getTotalMonthsSpending();

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                setUpPieChart();
                loadGraph();
            }
        }, 4000);
    }

    private void getTotalMonthsTransportExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        String itemNmonth = "Transport"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        values[0] += pTotal;
                        analyticsTransportAmount.setText("Spent: " + values[0]+ "₹");
                    }

                }
                else {
                    linearLayoutTransport.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void getTotalMonthsFoodExpense(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        String itemNmonth = "Food"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        values[1] += pTotal;
                        analyticsFoodAmount.setText("Spent: " + values[1]+ "₹");
                    }
                }else {
                    linearLayoutFood.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalMonthsHouseExpense(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        String itemNmonth = "House"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        values[2] += pTotal;
                        analyticsHouseExpensesAmount.setText("Spent: " + values[2]+ "₹");
                    }
                }else {
                    linearLayoutFoodHouse.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalMonthsEntertainmentExpenses(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        String itemNmonth = "Entertainment"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        values[3] += pTotal;
                        analyticsEntertainmentAmount.setText("Spent: " + values[3]+ "₹");
                    }
                }else {
                    linearLayoutEntertainment.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalMonthsEducationExpenses(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        String itemNmonth = "Education"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        values[4] += pTotal;
                        analyticsEducationAmount.setText("Spent: " + values[4] + "₹");
                    }
                }else {
                    linearLayoutEducation.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
//    private void getTotalMonthsCharityExpenses(){
//        MutableDateTime epoch = new MutableDateTime();
//        epoch.setDate(0); //Set to Epoch time
//        DateTime now = new DateTime();
//        Months months = Months.monthsBetween(epoch, now);
//
//        String itemNmonth = "Charity"+months.getMonths();
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expenses").child(onlineUserId);
//        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    int totalAmount = 0;
//                    for (DataSnapshot ds :  snapshot.getChildren()) {
//                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
//                        Object total = map.get("amount");
//                        int pTotal = Integer.parseInt(String.valueOf(total));
//                        totalAmount += pTotal;
//                        analyticsCharityAmount.setText("Spent: " + totalAmount);
//                    }
//
//                }else {
//                    linearLayoutCharity.setVisibility(View.GONE);
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
    private void getTotalMonthsApparelExpenses(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        String itemNmonth = "Clothes"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        values[5] += pTotal;
                        analyticsApparelAmount.setText("Spent: " + values[5] + "₹");
                    }
                }else {
                    linearLayoutApparel.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalMonthsHealthExpenses(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        String itemNmonth = "Health"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        values[6] += pTotal;
                        analyticsHealthAmount.setText("Spent: " + values[6]+ "₹");
                    }
                }else {
                    linearLayoutHealth.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalMonthsPersonalExpenses(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        String itemNmonth = "Personal"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        values[7] += pTotal;
                        analyticsPersonalExpensesAmount.setText("Spent: " + values[7] + "₹");
                    }
                }else {
                    linearLayoutPersonalExp.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalMonthsOtherExpenses(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        String itemNmonth = "Other"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        values[8] += pTotal;
                        analyticsOtherAmount.setText("Spent: " + values[8]+ "₹");
                    }
                }else {
                    linearLayoutOther.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalMonthsSpending(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expenses").child(onlineUserId);
        Query query = reference.orderByChild("month").equalTo(months.getMonths());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    int totalAmount = 0;
                    for (DataSnapshot ds :  dataSnapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>)ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount+=pTotal;

                    }
                    totalBudgetAmountTextView.setText("Total Month's spending: "+ totalAmount + "₹");
                    monthSpentAmount.setText("Total Spent: "+totalAmount + "₹");
                }else {
                    totalBudgetAmountTextView.setText("You've not spent in this month");
                    linearLayoutAnalysis.setVisibility(View.GONE);
                    pieChart.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUpPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Monthly Analytics");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);

    }

    private void loadGraph(){
        ArrayList<PieEntry> data = new ArrayList<PieEntry>();
        for (int i = 0; i < values.length; i++) {
            if (values[i] > 0) {
                data.add(new PieEntry(values[i], type[i]));
            }

            System.out.println(values[i] + "#######" + type[i]);
        }

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }
        for (int color : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(data, "Expense Category");
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.BLACK);

        pieChart.setData(pieData);
        pieChart.invalidate();
        pieChart.animateXY(2000, 2000);
    }

}