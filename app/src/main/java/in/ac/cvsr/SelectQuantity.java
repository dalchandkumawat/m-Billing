package in.ac.cvsr.newapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SelectQuantity extends AppCompatActivity {
    String billno,barcodevalue,shopid;
    TextView qty;
    EditText productid,productname,productprice;
    Button save,plus,minus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_quantity);
        minus=findViewById(R.id.minus);
        plus=findViewById(R.id.plus);
        qty=findViewById(R.id.qty);
        productid=findViewById(R.id.pid);
        productname=findViewById(R.id.pname);
        productprice=findViewById(R.id.price);
        save=findViewById(R.id.save);
        Intent intent=getIntent();
        barcodevalue=intent.getStringExtra("barcodevalue");
        billno=intent.getStringExtra("billno");
        shopid=intent.getStringExtra("shopid");
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i=Integer.parseInt(qty.getText().toString());
                i++;
                qty.setText(String.valueOf(i));
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i=Integer.parseInt(qty.getText().toString());
                i--;
                qty.setText(String.valueOf(i));
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(shopid+"/products");
        Query query = myRef.orderByChild("id").equalTo(barcodevalue);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Item item = postSnapshot.getValue(Item.class);
                    String pid=item.getId();
                    String pname = item.getName();
                    String price = item.getPrice();
                    productname.setText(pname);
                    productprice.setText(price);
                    productid.setText(pid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(shopid+"/products");
                Query query = myRef.orderByChild("id").equalTo(barcodevalue);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Item item = postSnapshot.getValue(Item.class);
                            String pid = item.getId();
                            String pname = item.getName();
                            String price = item.getPrice();
                            String quantity=qty.getText().toString();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("bill/"+billno);
                            String amount=String.valueOf(Integer.parseInt(quantity)*Float.valueOf(price));
                            Item item1=new Item(pid,pname,price,quantity,amount);
                            myRef.child(pid).setValue(item1);
                            Intent intent=new Intent();
                            setResult(CommonStatusCodes.SUCCESS,intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
