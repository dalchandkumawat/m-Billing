package in.ac.cvsr.newapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminRegister extends AppCompatActivity {
    Button register;
    EditText phonenumber,location,nameoforganisation,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);
        phonenumber=findViewById(R.id.phonenumber);
        location=findViewById(R.id.location);
        nameoforganisation=findViewById(R.id.nameoforganisation);
        register=findViewById(R.id.register);
        password=findViewById(R.id.password);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phonenumber.getText().toString().matches("")||location.getText().toString().matches("")||nameoforganisation.getText().toString().matches("")||password.getText().toString().matches("")){
                    Toast.makeText(AdminRegister.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
                else {
                    String phn = phonenumber.getText().toString();
                    String loc = location.getText().toString();
                    String nameoforg = nameoforganisation.getText().toString();
                    String pass = password.getText().toString();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("adminregistration");
                    Reg reg = new Reg(phn, loc, nameoforg, pass);
                    myRef.child(phn).setValue(reg);
                    Intent intent = new Intent(getApplicationContext(), AdminHome.class);
                    intent.putExtra("shopid",phonenumber.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}
