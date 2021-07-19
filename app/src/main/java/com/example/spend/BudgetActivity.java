package com.example.spend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.sql.Ref;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;


public class BudgetActivity extends AppCompatActivity {

    private TextView totalBudgetAmountTextView;
    private RecyclerView recyclerView;


    private FloatingActionButton fab;

    private DatabaseReference budgetRef;
    private FirebaseAuth mAuth;
    private ProgressDialog loader;

    private String post_key = "";
    private String item = "";
    private int amount = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        mAuth = FirebaseAuth.getInstance();
        budgetRef = FirebaseDatabase.getInstance().getReference().child("Budget").child(mAuth.getCurrentUser().getUid());
        loader = new ProgressDialog(this);

        totalBudgetAmountTextView  = findViewById(R.id.totalBudgetAmountTextView);
        recyclerView = findViewById(R.id.recycleView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount()>0) {
                    int totalAmount = 0;

                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Data data = snap.getValue(Data.class);
                        totalAmount += data.getAmount();
                        String sTotal = String.valueOf("Month's budget: " + totalAmount + "₹");
                        totalBudgetAmountTextView.setText(sTotal);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                additem();
            }
        });

        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount()>0){
                    int totalammount = 0;

                    for (DataSnapshot snap:snapshot.getChildren()){

                        Data data =snap.getValue(Data.class);

                        totalammount+=data.getAmount();

                        String sttotal=String.valueOf("Month's Budget: "+totalammount + "₹");

                        totalBudgetAmountTextView.setText(sttotal);

                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         //getMonthTransportBudgetRatios();
        //getMonthFoodBudgetRatios();
        //getMonthHouseBudgetRatios();
        //getMonthEntBudgetRatios();
        //getMonthEduBudgetRatios();
        //getMonthCharityBudgetRatios();
        //getMonthAppBudgetRatios();
        //getMonthHealthBudgetRatios();
        //getMonthPerBudgetRatios();
        //getMonthOtherBudgetRatios();


    }

    private void additem() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_layout, null);
        myDialog.setView(myView);

        final  AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final Spinner itemSpinner = myView.findViewById(R.id.itemspinner);
        final EditText amount = myView.findViewById(R.id.amount);
        final Button cancel = myView.findViewById(R.id.cancel);
        final  Button save = myView.findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String budgetAmount = amount.getText().toString();
                String budgetItem = itemSpinner.getSelectedItem().toString();

                if (TextUtils.isEmpty(budgetAmount)){
                    amount.setError("Amount is required!");
                    return;
                }

                if (budgetItem.equals("Select item")){
                    Toast.makeText(BudgetActivity.this, "Select a valid item", Toast.LENGTH_SHORT).show();
                }

                else {
                    loader.setMessage("Adding a budget item");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    String id  = budgetRef.push().getKey();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar cal = Calendar.getInstance();
                    String date = dateFormat.format(cal.getTime());

                    MutableDateTime epoch = new MutableDateTime();
                    epoch.setDate(0);
                    DateTime now = new DateTime();
                    Weeks weeks = Weeks.weeksBetween(epoch, now);
                    Months months = Months.monthsBetween(epoch, now);

                   String itemNday = budgetItem+date;
                   String itemNweek = budgetItem+weeks.getWeeks();
                   String itemNmonth = budgetItem+months.getMonths();

                    Data data = new Data(budgetItem, date, id, null, itemNday, itemNweek, itemNmonth, Integer.parseInt(budgetAmount),months.getMonths(),weeks.getWeeks());
                    budgetRef.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(BudgetActivity.this, "Budget item added successfuly", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(BudgetActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }

                            loader.dismiss();
                        }
                    });
                }
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(budgetRef, Data.class)
                .build();

        FirebaseRecyclerAdapter<Data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, final int position, @NonNull final Data model) {

                holder.setItemAmount("Allocated amount: "+ model.getAmount() + "₹");
                holder.setDate("On: "+model.getDate());
                holder.setItemName("Budget Item: "+model.getItem());

                holder.notes.setVisibility(View.GONE);

                switch (model.getItem()){
                    case "Transport":
                        holder.imageView.setImageResource(R.drawable.ic_transport);
                        break;
                    case "Food":
                        holder.imageView.setImageResource(R.drawable.ic_food);
                        break;
                    case "House":
                        holder.imageView.setImageResource(R.drawable.ic_house);
                        break;
                    case "Entertainment":
                        holder.imageView.setImageResource(R.drawable.ic_entertainment);
                        break;
                    case "Education":
                        holder.imageView.setImageResource(R.drawable.ic_education);
                        break;
                    case "Clothes":
                        holder.imageView.setImageResource(R.drawable.ic_shirt);
                        break;
                    case "Health":
                        holder.imageView.setImageResource(R.drawable.ic_health);
                        break;
                    case "Personal":
                        holder.imageView.setImageResource(R.drawable.ic_personalcare);
                        break;
                    case "Other":
                        holder.imageView.setImageResource(R.drawable.ic_other);
                        break;
                }

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        post_key = getRef(position).getKey();
                        item = model.getItem();
                        amount = model.getAmount();
                        updateData();
                    }
                });


            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieve_layout, parent, false);
                return new MyViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public ImageView imageView;
        public TextView notes, date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            imageView = itemView.findViewById(R.id.imageView);
            notes = itemView.findViewById(R.id.note);
            date = itemView.findViewById(R.id.date);

        }

        public  void setItemName (String itemName){
            TextView item = mView.findViewById(R.id.item);
            item.setText(itemName);
        }

        public void setItemAmount(String itemAmount){
            TextView amount = mView.findViewById(R.id. amount);
            amount.setText(itemAmount);
        }

        public void setDate(String itemDate){
            TextView date = mView.findViewById(R.id.date);
            date.setText(itemDate);
        }
    }

    private void updateData(){
        AlertDialog.Builder myDialog= new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View mView = inflater.inflate(R.layout.update_layout, null);

        myDialog.setView(mView);
        final  AlertDialog dialog = myDialog.create();

        final TextView mItem = mView.findViewById(R.id.itemName);
        final EditText mAmount = mView.findViewById(R.id.amount);
        final  EditText mNotes = mView.findViewById(R.id.note);

        mNotes.setVisibility(View.GONE);

        mItem.setText(item);

        mAmount.setText(String.valueOf(amount));
        mAmount.setSelection(String.valueOf(amount).length());

        Button delBut = mView.findViewById(R.id.btnDelete);
        Button btnUpdate = mView.findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                amount = Integer.parseInt(mAmount.getText().toString());

                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                String date = dateFormat.format(cal.getTime());

                MutableDateTime epoch = new MutableDateTime();
                epoch.setDate(0);
                DateTime now = new DateTime();
                Weeks weeks = Weeks.weeksBetween(epoch, now);
                Months months = Months.monthsBetween(epoch, now);

                String itemNday = item+date;
                String itemNweek = item+weeks.getWeeks();
                String itemNmonth = item+months.getMonths();

                Data data = new Data(item, date, post_key,null,itemNday,itemNweek, itemNmonth, amount, months.getMonths(),weeks.getWeeks());
                budgetRef.child(post_key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(BudgetActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(BudgetActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                dialog.dismiss();

            }
        });

        delBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                budgetRef.child(post_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(BudgetActivity.this, "Deleted  successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(BudgetActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                dialog.dismiss();
            }
        });

        dialog.show();
    }

//    private void getMonthTransportBudgetRatios(){
//        Query query = budgetRef.orderByChild("item").equalTo("Transport");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    int pTotal = 0;
//                    for (DataSnapshot ds :  snapshot.getChildren()) {
//                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
//                        Object total = map.get("amount");
//                        pTotal = Integer.parseInt(String.valueOf(total));
//                    }
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//    private void getMonthFoodBudgetRatios(){
//        Query query = budgetRef.orderByChild("item").equalTo("Food");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    int pTotal = 0;
//                    for (DataSnapshot ds :  snapshot.getChildren()) {
//                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
//                        Object total = map.get("amount");
//                        pTotal = Integer.parseInt(String.valueOf(total));
//                    }
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//    private void getMonthHouseBudgetRatios(){
//        Query query = budgetRef.orderByChild("item").equalTo("House");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    int pTotal = 0;
//                    for (DataSnapshot ds :  snapshot.getChildren()) {
//                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
//                        Object total = map.get("amount");
//                        pTotal = Integer.parseInt(String.valueOf(total));
//                    }
//
//                    int dayHouseRatio = pTotal/30;
//                    int weekHouseRatio = pTotal/4;
//                    int monthHouseRatio = pTotal;
//
//                    userRef.child("dayHouseRatio").setValue(dayHouseRatio);
//                    userRef.child("weekHouseRatio").setValue(weekHouseRatio);
//                    userRef.child("monthHouseRatio").setValue(monthHouseRatio);
//
//                }else {
//                    userRef.child("dayHouseRatio").setValue(0);
//                    userRef.child("weekHouseRatio").setValue(0);
//                    userRef.child("monthHouseRatio").setValue(0);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//    private void getMonthEntBudgetRatios(){
//        Query query = budgetRef.orderByChild("item").equalTo("Entertainment");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    int pTotal = 0;
//                    for (DataSnapshot ds :  snapshot.getChildren()) {
//                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
//                        Object total = map.get("amount");
//                        pTotal = Integer.parseInt(String.valueOf(total));
//                    }
//
//                    int dayEntRatio = pTotal/30;
//                    int weekEntRatio = pTotal/4;
//                    int monthEntRatio = pTotal;
//
//                    userRef.child("dayEntRatio").setValue(dayEntRatio);
//                    userRef.child("weekEntRatio").setValue(weekEntRatio);
//                    userRef.child("monthEntRatio").setValue(monthEntRatio);
//
//                }else {
//                    userRef.child("dayEntRatio").setValue(0);
//                    userRef.child("weekEntRatio").setValue(0);
//                    userRef.child("monthEntRatio").setValue(0);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//    private void getMonthEduBudgetRatios(){
//        Query query = budgetRef.orderByChild("item").equalTo("Education");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    int pTotal = 0;
//                    for (DataSnapshot ds :  snapshot.getChildren()) {
//                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
//                        Object total = map.get("amount");
//                        pTotal = Integer.parseInt(String.valueOf(total));
//                    }
//
//                    int dayEduRatio = pTotal/30;
//                    int weekEduRatio = pTotal/4;
//                    int monthEduRatio = pTotal;
//
//                    userRef.child("dayEduRatio").setValue(dayEduRatio);
//                    userRef.child("weekEduRatio").setValue(weekEduRatio);
//                    userRef.child("monthEduRatio").setValue(monthEduRatio);
//
//                }else {
//                    userRef.child("dayEduRatio").setValue(0);
//                    userRef.child("weekEduRatio").setValue(0);
//                    userRef.child("monthEduRatio").setValue(0);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//    private void getMonthCharityBudgetRatios(){
//        Query query = budgetRef.orderByChild("item").equalTo("Charity");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    int pTotal = 0;
//                    for (DataSnapshot ds :  snapshot.getChildren()) {
//                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
//                        Object total = map.get("amount");
//                        pTotal = Integer.parseInt(String.valueOf(total));
//                    }
//
//                    int dayCharRatio = pTotal/30;
//                    int weekCharRatio = pTotal/4;
//                    int monthCharRatio = pTotal;
//
//                    userRef.child("dayCharRatio").setValue(dayCharRatio);
//                    userRef.child("weekCharRatio").setValue(weekCharRatio);
//                    userRef.child("monthCharRatio").setValue(monthCharRatio);
//
//                }else {
//                    userRef.child("dayCharRatio").setValue(0);
//                    userRef.child("weekCharRatio").setValue(0);
//                    userRef.child("monthCharRatio").setValue(0);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//    private void getMonthAppBudgetRatios(){
//        Query query = budgetRef.orderByChild("item").equalTo("Apparel");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    int pTotal = 0;
//                    for (DataSnapshot ds :  snapshot.getChildren()) {
//                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
//                        Object total = map.get("amount");
//                        pTotal = Integer.parseInt(String.valueOf(total));
//                    }
//
//                    int dayAppRatio = pTotal/30;
//                    int weekAppRatio = pTotal/4;
//                    int monthAppRatio = pTotal;
//
//                    userRef.child("dayAppRatio").setValue(dayAppRatio);
//                    userRef.child("weekAppRatio").setValue(weekAppRatio);
//                    userRef.child("monthAppRatio").setValue(monthAppRatio);
//
//                }else {
//                    userRef.child("dayAppRatio").setValue(0);
//                    userRef.child("weekAppRatio").setValue(0);
//                    userRef.child("monthAppRatio").setValue(0);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//    private void getMonthHealthBudgetRatios(){
//        Query query = budgetRef.orderByChild("item").equalTo("Health");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    int pTotal = 0;
//                    for (DataSnapshot ds :  snapshot.getChildren()) {
//                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
//                        Object total = map.get("amount");
//                        pTotal = Integer.parseInt(String.valueOf(total));
//                    }
//
//                    int dayHealthRatio = pTotal/30;
//                    int weekHealthRatio = pTotal/4;
//                    int monthHealthRatio = pTotal;
//
//                    userRef.child("dayHealthRatio").setValue(dayHealthRatio);
//                    userRef.child("weekHealthRatio").setValue(weekHealthRatio);
//                    userRef.child("monthHealthRatio").setValue(monthHealthRatio);
//
//                }else {
//                    userRef.child("dayHealthRatio").setValue(0);
//                    userRef.child("weekHealthRatio").setValue(0);
//                    userRef.child("monthHealthRatio").setValue(0);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//    private void getMonthPerBudgetRatios(){
//        Query query = budgetRef.orderByChild("item").equalTo("Personal");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    int pTotal = 0;
//                    for (DataSnapshot ds :  snapshot.getChildren()) {
//                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
//                        Object total = map.get("amount");
//                        pTotal = Integer.parseInt(String.valueOf(total));
//                    }
//
//                    int dayPerRatio = pTotal/30;
//                    int weekPerRatio = pTotal/4;
//                    int monthPerRatio = pTotal;
//
//                    userRef.child("dayPerRatio").setValue(dayPerRatio);
//                    userRef.child("weekPerRatio").setValue(weekPerRatio);
//                    userRef.child("monthPerRatio").setValue(monthPerRatio);
//
//                }else {
//                    userRef.child("dayPerRatio").setValue(0);
//                    userRef.child("weekPerRatio").setValue(0);
//                    userRef.child("monthPerRatio").setValue(0);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//    private void getMonthOtherBudgetRatios(){
//        Query query = budgetRef.orderByChild("item").equalTo("Other");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    int pTotal = 0;
//                    for (DataSnapshot ds :  snapshot.getChildren()) {
//                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
//                        Object total = map.get("amount");
//                        pTotal = Integer.parseInt(String.valueOf(total));
//                    }
//
//                    int dayOtherRatio = pTotal/30;
//                    int weekOtherRatio = pTotal/4;
//                    int monthOtherRatio = pTotal;
//
//                    userRef.child("dayOtherRatio").setValue(dayOtherRatio);
//                    userRef.child("weekOtherRatio").setValue(weekOtherRatio);
//                    userRef.child("monthOtherRatio").setValue(monthOtherRatio);
//
//                }else {
//                    userRef.child("dayOtherRatio").setValue(0);
//                    userRef.child("weekOtherRatio").setValue(0);
//                    userRef.child("monthOtherRatio").setValue(0);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
}