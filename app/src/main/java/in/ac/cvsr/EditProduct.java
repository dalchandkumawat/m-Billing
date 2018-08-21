package in.ac.cvsr.newapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditProduct extends AppCompatActivity {
    Intent intent;
    Button save,delete;
    String shopid;
    EditText pid,pname,price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        intent=getIntent();
        shopid=intent.getStringExtra("shopid");
        save=findViewById(R.id.save);
        delete=findViewById(R.id.delete);
        pid=findViewById(R.id.pid);
        pname=findViewById(R.id.pname);
        price=findViewById(R.id.price);
        final String id=intent.getStringExtra("pid");
        final String name=intent.getStringExtra("pname");
        final String sprice=intent.getStringExtra("price");
        pid.setText(id);
        pname.setText(name);
        price.setText(sprice);
        price.requestFocus();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prid=pid.getText().toString();
                String prname=pname.getText().toString();
                String prrice=price.getText().toString();
                Item item=new Item(prid,prname,prrice);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(shopid+"/products");
                myRef.child(prid).setValue(item);
                Intent intent=new Intent(getApplicationContext(),Product.class);
                intent.putExtra("shopid",shopid);
                startActivity(intent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(shopid+"/products");
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
                Intent intent=new Intent(getApplicationContext(),Product.class);
                intent.putExtra("shopid",shopid);
                startActivity(intent);
            }
        });
    }
}

