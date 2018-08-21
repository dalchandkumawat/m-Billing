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
import android.widget.Toast;

import java.util.ArrayList;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Product extends AppCompatActivity {
    Button addproduct;
    Intent newintent;
    ListView listview;
    String shopid;
    static ArrayList<Item> list;
    Adapter adapter;
    public static int PERMISSION_REQUEST = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        addproduct=findViewById(R.id.addproduct);
        listview=findViewById(R.id.listview);
        newintent=getIntent();
        shopid=newintent.getStringExtra("shopid");
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
        DatabaseReference myRef = database.getReference(shopid+"/products");
        list=new ArrayList<Item>();
        adapter = new Adapter(getApplicationContext(),list);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Item item= postSnapshot.getValue(Item.class);
                    String pid=item.getId();
                    String pname=item.getName();
                    String price=item.getPrice();
                    list.add(new Item(pid,pname,price));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item it=(Item) adapterView.getItemAtPosition(i);
                Intent editintent=new Intent(getApplicationContext(),EditProduct.class);
                String id=String.valueOf(it.getId());
                String price=String.valueOf(it.getPrice());
                editintent.putExtra("pid",id);
                editintent.putExtra("pname",it.getName());
                editintent.putExtra("price",price);
                editintent.putExtra("shopid",shopid);
                startActivity(editintent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==0){
            if(resultCode== CommonStatusCodes.SUCCESS){
                Barcode barcode = data.getParcelableExtra("barcode");
                Intent intent=new Intent(getApplicationContext(),AddProduct.class);
                intent.putExtra("barcodevalue",barcode.displayValue);
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
