package in.ac.cvsr.newapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CustomerItem extends AppCompatActivity {
    Button addproduct,done;
    Intent newintent;
    String billno,shopid;
    float sum=0;
    ListView listview;
    static ArrayList<Item> list;
    AdapterItem adapter;
    TextView totalamount,refno;
    public static int PERMISSION_REQUEST = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_item);
        addproduct=findViewById(R.id.addproduct);
        listview=findViewById(R.id.listview);
        newintent=getIntent();
        billno=newintent.getStringExtra("billno");
        shopid=newintent.getStringExtra("shopid");
        refno=findViewById(R.id.refno);
        refno.setText(billno);
        done=findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                Toast.makeText(CustomerItem.this, "Thanks for shopping with us!", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });
        totalamount=findViewById(R.id.totalamount);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PERMISSION_REQUEST);
        }
        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Scan.class);
                startActivityForResult(intent,0);
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("bill");
        list=new ArrayList<Item>();
        adapter = new AdapterItem(getApplicationContext(),list);
        myRef.child(billno).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Item item= postSnapshot.getValue(Item.class);
                    String pid=item.getId();
                    String pname=item.getName();
                    String price=item.getPrice();
                    String quantity=item.getQuantity();
                    String amount=String.valueOf(Integer.parseInt(quantity)*Float.valueOf(price));
                    sum+=Float.valueOf(amount);
                    list.add(new Item(pid,pname,price,quantity,amount));
                    adapter.notifyDataSetChanged();
                    totalamount.setText(String.valueOf(sum));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listview.setAdapter(adapter);
        listview.setLongClickable(true);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item it=(Item) adapterView.getItemAtPosition(i);
                String id=String.valueOf(it.getId());
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("bill/"+billno);
                Query applesQuery = ref.orderByChild("id").equalTo(id);
                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                startActivity(newintent);
                return true;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==0){
            if(resultCode== CommonStatusCodes.SUCCESS){
                Barcode barcode = data.getParcelableExtra("barcode");
                Intent intent=new Intent(getApplicationContext(),SelectQuantity.class);
                intent.putExtra("barcodevalue",barcode.displayValue);
                intent.putExtra("billno",billno);
                intent.putExtra("shopid",shopid);
                startActivityForResult(intent,5);
            }
        }
        else if(requestCode==5){
            if(resultCode==CommonStatusCodes.SUCCESS){
                finish();
                startActivity(newintent);
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
