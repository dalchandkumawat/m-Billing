package in.ac.cvsr.newapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminHome extends AppCompatActivity {
    Button product,next;
    EditText billno,cname,cno;
    String shopid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        product=findViewById(R.id.product);
        Intent intent=getIntent();
        shopid=intent.getStringExtra("shopid");
        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Product.class);
                intent.putExtra("shopid",shopid);
                startActivity(intent);
            }
        });
        next=findViewById(R.id.next);
        billno=findViewById(R.id.billno);
        cname=findViewById(R.id.cname);
        cno=findViewById(R.id.cno);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(billno.getText().toString().equals("")||cname.getText().toString().equals("")||cno.getText().toString().equals("")||cno.getText().toString().length()<10){
                    Toast.makeText(AdminHome.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), Bill.class);
                    intent.putExtra("billno", billno.getText().toString());
                    intent.putExtra("cname", cname.getText().toString());
                    intent.putExtra("cno", cno.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}
