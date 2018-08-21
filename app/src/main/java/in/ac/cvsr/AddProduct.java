package in.ac.cvsr.newapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProduct extends AppCompatActivity {
    EditText pid,pname,price;
    String shopid;
    Button done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Intent intent=getIntent();
        shopid=intent.getStringExtra("shopid");
        final String productid=intent.getStringExtra("barcodevalue");
        pid=findViewById(R.id.pid);
        pname=findViewById(R.id.pname);
        price=findViewById(R.id.price);
        done=findViewById(R.id.done);
        pname.requestFocus();
        pid.setText(productid);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productname=pname.getText().toString();
                String productprice=price.getText().toString();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(shopid+"/products");
                Item item=new Item(productid,productname,productprice);
                myRef.child(productid).setValue(item);
                Intent intent=new Intent(getApplicationContext(),Product.class);
                intent.putExtra("shopid",shopid);
                startActivity(intent);
            }
        });
    }
}
