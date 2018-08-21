package in.ac.cvsr.newapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    Button userlogin,adminlogin;
    EditText billno,shopid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userlogin=findViewById(R.id.userlogin);
        adminlogin=findViewById(R.id.adminlogin);
        shopid=findViewById(R.id.shopid);
        billno=findViewById(R.id.billno);
        userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(billno.getText().toString().matches("")||billno.getText().toString().length()<6){
                    Toast.makeText(MainActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), CustomerItem.class);
                    intent.putExtra("billno", billno.getText().toString());
                    intent.putExtra("shopid",shopid.getText().toString());
                    startActivity(intent);
                }
            }
        });
        adminlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),AdminLogin.class);
                startActivity(intent);
            }
        });
    }
}
