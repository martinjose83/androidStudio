package com.marts.mynote;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,OnClickInMyAdapterListener,OnClickInMyAdapterListener2{
    String Date;
    EditText muNameET, mpWordET, reguEmailET, reguPwordET, rfullNameReg, rfamilycodeReg, ashopItemET, ashopQntyET, ataskNameET, ataskDateET;
    String uEmail;
    TextView hhomeWelcomeTV;
    TextView mmsgTvLogin;
    FirebaseFirestore db;
    ArrayList<TaskInfo> tasklist;
    Map<String, Object> map;
    ListView pListView, fListView;
    String fCode = "";
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    ListViewCustomAdaptorBM listAdapterBM;
    ListViewCustomAdaptor listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();

        setContentView(R.layout.activity_main);
        muNameET = findViewById(R.id.uNameET);
        mpWordET = findViewById(R.id.pWordET);

        mmsgTvLogin = findViewById(R.id.msgTvLogin);

    }

    public void mainLoginBtn(View view) {
        muNameET = findViewById(R.id.uNameET);
        mpWordET = findViewById(R.id.pWordET);
        uEmail = muNameET.getText().toString().trim();
        String upWord = mpWordET.getText().toString().trim();
        if(TextUtils.isEmpty(uEmail)||TextUtils.isEmpty(upWord)){
            Toast.makeText(MainActivity.this, "Empty Cridentials", Toast.LENGTH_LONG).show();
        }
        else{
        loginUser(uEmail, upWord);}
    }

    private void btnViewHomeLayout() {
        viewHomeLayout(getUserEmail());
    }

    private void loginUser(String uEmail, String upWord) {
        auth.signInWithEmailAndPassword(uEmail, upWord).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(MainActivity.this, "Login Successfull", Toast.LENGTH_LONG).show();

                viewHomeLayout(uEmail);
                // DatabaseReference reference = database.getReference().child(toUserId(uEmail));
                //finish();
            }

        });

    }

    public void showhomePage4btn(View view) {
        viewHomeLayout(getUserEmail());
    }

    private void viewHomeLayout(String uEmail) {
        setContentView(R.layout.home_main);
        hhomeWelcomeTV = findViewById(R.id.homeWelcomeTV);
        DocumentReference docRef = db.collection("user").document(toUserId(uEmail));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();

                    if (doc.exists()) {
                        Map<String, Object> user = doc.getData();
                        String name = user.get("Name").toString();
                        Log.d("db", name);
                        hhomeWelcomeTV.setText("Welcome " + name);

                    } else {
                        Log.d("db", "no Data");
                        hhomeWelcomeTV.setText("Welcome");
                    }
                }
            }
        });


    }

    public void showmyPpage4btn(View view) {
        showmyPpage();
        maketasklist2();
        setupScreen();
    }

    /*public void showmyPpage1() {
        setContentView(R.layout.myppage);

        pListView = findViewById(R.id.plistView);

        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_task, list);
        pListView.setAdapter(adapter);

        DatabaseReference ref = database.getReference().child(toUserId(getUserEmail()));

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();


                    Log.d("map", map.toString());
                    for (String key : map.keySet()) {
                        Object obj = map.get(key);
                        Map<String, Object> map1 = (Map) obj;
                        String tName = map1.get("tName").toString();
                        String tDate = map1.get("tDate").toString();
                        list.add(tName + " " + tDate);
                        Log.d("map", tName + " " + tDate);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

    public void showmyFpage4btn(View view) {
        showmyFpage();
        maketasklist();
        setupScreen2();

    }

    public void showmyFpage1() {
        setContentView(R.layout.myfpage);
        String fCode = getFCode(getUserEmail());
        Log.d("Fp", fCode + " " + getUserEmail());
        fListView = findViewById(R.id.flistView);
        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_task, list);
        fListView.setAdapter(adapter);
        DatabaseReference ref = database.getReference().child(toUserId(fCode + "fgroup"));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();

                    Log.d("map", map.toString());
                    for (String key : map.keySet()) {
                        Object obj = map.get(key);
                        Map<String, Object> map1 = (Map) obj;
                        String itemName = map1.get("itemName").toString();
                        String itemDet = map1.get("itemDet").toString();
                        list.add(itemName + " " + itemDet);
                        Log.d("map", itemName + " " + itemDet);
                    }
                }


                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String getFCode(String email) {
        Log.d("Fp", "getFcodemethod" + email);
        DocumentReference docRef = db.collection("user").document(toUserId(email));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();

                    if (doc.exists()) {
                        Map<String, Object> user = doc.getData();
                        fCode = user.get("fCode").toString();
                        Log.d("db", fCode);

                    }
                }
            }
        });
        return fCode;
    }

    public void logout(View view) {
        auth.signOut();
        setContentView(R.layout.activity_main);
    }


    public void mainRegisterBtn(View view) {
        setContentView(R.layout.register_main);
        reguEmailET = findViewById(R.id.userNameReg);
        reguPwordET = findViewById(R.id.passwordReg);
        rfullNameReg = findViewById(R.id.shopItemET);
        rfamilycodeReg = findViewById(R.id.familycodeReg);


    }

    public void registerAddNewBtn(View view) {
        Log.d("reg", "regbtn clicked");
        String ruEmail = (reguEmailET.getText().toString()).trim();
        String rupWord = (reguPwordET.getText().toString()).trim();
        String rfullName = (rfullNameReg.getText().toString()).trim();
        String rfCode = (rfamilycodeReg.getText().toString()).trim();
        if (TextUtils.isEmpty(rfCode)) {
            rfCode = ruEmail;
        }
        if (TextUtils.isEmpty(ruEmail) || TextUtils.isEmpty(rupWord) || TextUtils.isEmpty(rfullName)) {
            Toast.makeText(MainActivity.this, "Empty Cridentials", Toast.LENGTH_LONG).show();
        } else {
            registerUser(ruEmail, rupWord, rfullName, rfCode);

        }

    }

    public String toUserId(String email) {
        StringBuilder userid = new StringBuilder();
        for (int i = 0; i < email.length(); i++) {
            char ch = email.charAt(i);
            int x = (int) ch;
            userid.append(x);
        }
        return userid.toString();
    }

    private void registerUser(String ruEmail, String rupWord, String rfullName, String rfCode) {

        HashMap<String, Object> user = new HashMap<>();

        Log.d("reg", "regUser method" + ruEmail + " " + rupWord);

        auth.createUserWithEmailAndPassword(ruEmail, rupWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "User Registered", Toast.LENGTH_LONG).show();
                    Log.d("reg", "User Added");
                    setContentView(R.layout.activity_main);
                    user.put("Name", rfullName);
                    user.put("Email", ruEmail);
                    user.put("fCode", rfCode);
                    db.collection("user").document(toUserId(ruEmail)).set(user);
                    //database.getReference().child(toUserId(ruEmail)).updateChildren(user);

                } else {
                    Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_LONG).show();
                    Log.d("reg", "reg failed" + ruEmail + "" + task.getException());

                }
            }
        });

    }

    @Override
    protected void onStart() {

        super.onStart();
        String user = getUserEmail();
        if (user != null) {
            viewHomeLayout(user);
        }
    }
    public String getUserEmail() {
        String email;
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            return email;
        }
        else return null;

    }

    public void addTaskMyPage(View view) {
        setContentView(R.layout.add_task);
        ataskNameET = findViewById(R.id.taskNameET);
        ataskDateET = findViewById(R.id.taskDateET);
        ImageButton mbtncalr = findViewById(R.id.btncaldr);
        mbtncalr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }


    public void addtask(View view) {
        //add task to db
        HashMap<String, Object> task = new HashMap<>();
        String tname = ataskNameET.getText().toString();
        String tDate = ataskDateET.getText().toString();
        Log.d("dp", "addtask read" + uEmail);
        task.put("tName", tname);
        task.put("tDate", tDate);
        if (TextUtils.isEmpty(uEmail)) {
            uEmail = getUserEmail();
        }
        if (TextUtils.isEmpty(tname) || TextUtils.isEmpty(tDate)) {
            Toast.makeText(MainActivity.this, "Empty Cridentials", Toast.LENGTH_LONG).show();
        } else {
            database.getReference().child(toUserId(uEmail)).child(String.valueOf(System.currentTimeMillis())).updateChildren(task).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Task Added Successfully", Toast.LENGTH_LONG).show();
                        showmyPpage();
                    } else {
                        Toast.makeText(MainActivity.this, "Actiion Failed", Toast.LENGTH_LONG).show();
                        // Log.d("reg","action failed");

                    }
                }
            });
        }
    }

    public void addItemFPage(View view) {
        setContentView(R.layout.add_shoplist);
        ashopItemET = findViewById(R.id.shopItemET);
        ashopQntyET = findViewById(R.id.shopQntyET);
    }

    public void additem(View view) {
        //add item to db
        HashMap<String, Object> item = new HashMap<>();
        String itemName = ashopItemET.getText().toString();
        String itemDet = ashopQntyET.getText().toString();
        Log.d("dp", "addtask read" + uEmail);
        item.put("itemName", itemName);
        item.put("itemDet", itemDet);


        if (TextUtils.isEmpty(itemName)) {
            Toast.makeText(MainActivity.this, "Empty Cridentials", Toast.LENGTH_LONG).show();
        } else {
            database.getReference().child(toUserId(fCode + "fgroup")).child(String.valueOf(System.currentTimeMillis())).updateChildren(item).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Task Added Successfully", Toast.LENGTH_LONG).show();
                        showmyFpage();
                    } else {
                        Toast.makeText(MainActivity.this, "Actiion Failed", Toast.LENGTH_LONG).show();
                        // Log.d("reg","action failed");

                    }
                }
            });
        }

    }

    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        String dateString = dayOfMonth + "/" + month + 1 + "/" + year;
        Log.d("dp", dateString);
        ataskDateET.setText(dateString);
    }

    public void showMainActivity(View view) {
        setContentView(R.layout.activity_main);
    }






     public void showmyFpage() {
         tasklist = new ArrayList<>();
         map = new HashMap<String, Object>();


    setContentView(R.layout.myfpage);


    String fCode = getFCode(getUserEmail());

        DatabaseReference ref = database.getReference().child(toUserId(fCode + "fgroup"));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tasklist.clear();
                if (snapshot.exists()) {
                    map = (Map<String, Object>) snapshot.getValue();
                    }
                maketasklist();
                setupScreen2();
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
                    public void maketasklist(){

                        for (String key : map.keySet()) {
                            Object obj = map.get(key);
                            Map<String, Object> map1 = (Map) obj;
                            String itemName = map1.get("itemName").toString();
                            String itemDet = map1.get("itemDet").toString();
                            TaskInfo task = new TaskInfo(itemName,itemDet,key);
                            tasklist.add(task);
                            Log.d("map", itemName + " " + itemDet);
         }}
    public void maketasklist2(){

        for (String key : map.keySet()) {
            Object obj = map.get(key);
            Map<String, Object> map1 = (Map) obj;
            String itemName = map1.get("tName").toString();
            String itemDet = map1.get("tDate").toString();
            TaskInfo task = new TaskInfo(itemName,itemDet,key);
            tasklist.add(task);
            Log.d("map", itemName + " " + itemDet);
        }}

    public void setupScreen2() {
        setContentView(R.layout.myfpage);
        fListView = findViewById(R.id.flistView);
       listAdapterBM = new ListViewCustomAdaptorBM(MainActivity.this, tasklist, (OnClickInMyAdapterListener)MainActivity.this);


                fListView.setAdapter(listAdapterBM);
                        fListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Log.d("his", "Edit the task");

            }


        });


    }

    public void showmyPpage() {
        tasklist = new ArrayList<>();
        map = new HashMap<String, Object>();


        setContentView(R.layout.myppage);


        DatabaseReference ref = database.getReference().child(toUserId(getUserEmail()));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tasklist.clear();
                if (snapshot.exists()) {
                    map = (Map<String, Object>) snapshot.getValue();
                }
                maketasklist2();
                setupScreen();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void setupScreen() {
        setContentView(R.layout.myppage);
        pListView = findViewById(R.id.plistView);
        listAdapter = new ListViewCustomAdaptor(MainActivity.this, tasklist, (OnClickInMyAdapterListener2)MainActivity.this);


        pListView.setAdapter(listAdapter);
        pListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Log.d("his", "Edit the task");

            }


        });


    }

    @Override
    public void removeTaskOnClick(int i) {
        //code to remove task
String index = tasklist.get(i).getKey();
        DatabaseReference ref = database.getReference().child(toUserId(getFCode(getUserEmail())+"fgroup")).child(index);
        //Query query = ref.child(toUserId(getUserEmail()+"fgroup").orderByChild("title").equalTo("Apple");
        Log.d("rem","here");
        ref.removeValue();
    }

    @Override
    public void removeTaskOnClick2(int i) {
        //code to remove task
        String index = tasklist.get(i).getKey();
        DatabaseReference ref = database.getReference().child(toUserId(getUserEmail())).child(index);
        //Query query = ref.child(toUserId(getUserEmail()+"fgroup").orderByChild("title").equalTo("Apple");
        Log.d("rem","here");
        ref.removeValue();
    }
}
