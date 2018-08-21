package in.ac.cvsr.newapplication;

import android.content.Context;
import android.content.Intent;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Bill extends AppCompatActivity {
    Button print,minus,plus;
    float sum=0;
    String htmlDocument;
    ListView listview;
    static ArrayList<Item> list;
    AdapterItem adapter;
    TextView totalamount;
    private WebView myWebView;
    TextView bno,cusname,cusno,discount,discountamt,finalamt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        Intent intent=getIntent();
        final String  billno=intent.getStringExtra("billno");
        String cname=intent.getStringExtra("cname");
        String cno=intent.getStringExtra("cno");
        listview=findViewById(R.id.listview);

        minus=findViewById(R.id.minus);
        plus=findViewById(R.id.plus);
        discountamt=findViewById(R.id.discountamt);
        finalamt=findViewById(R.id.finalamt);
        discount=findViewById(R.id.discount);
        print=findViewById(R.id.print);
        totalamount=findViewById(R.id.totalamount);
        bno=findViewById(R.id.billno);
        cusname=findViewById(R.id.cname);
        cusno=findViewById(R.id.cno);
        bno.setText(bno.getText().toString()+billno);
        cusname.setText(cusname.getText().toString()+cname);
        cusno.setText(cusno.getText().toString()+cno);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("bill");
        list=new ArrayList<Item>();
        adapter = new AdapterItem(getApplicationContext(),list);
        htmlDocument="<html><style>table,th,td{border:1px solid black;border-collapse:collapse;}</style>" +
                "<body><center><h1>Retail Invoice</h1><h2>Name of Organisation</h2></center>" +
                "<h2>Customer Name: "+cname+"</h2><h2>Customer Number: "+
                cno.toString()+"</h2><table border='1' width='100%'><tr><th>" +
                "PID</th><th>PNAME</th><th>QTY</th><th>PRICE</th><th>AMOUNT</th>" +
                "</tr>";
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s=discount.getText().toString();
                double x=Double.parseDouble(s)-0.5;
                String s1=String.format("%.1f",x);
                discount.setText(s1);
                float y=(Float.valueOf(totalamount.getText().toString())*Float.valueOf(discount.getText().toString()))/100;
                discountamt.setText(String.valueOf(y));
                float xy=Float.valueOf(totalamount.getText().toString())-Float.valueOf(discountamt.getText().toString());
                finalamt.setText(String.valueOf(xy));
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s=discount.getText().toString();
                double x=Double.parseDouble(s)+0.5;
                String s1=String.format("%.1f",x);
                discount.setText(s1);
                float y=(Float.valueOf(totalamount.getText().toString())*Float.valueOf(discount.getText().toString()))/100;
                discountamt.setText(String.valueOf(y));
                float xy=Float.valueOf(totalamount.getText().toString())-Float.valueOf(discountamt.getText().toString());
                finalamt.setText(String.valueOf(xy));
            }
        });
        float x=Float.valueOf(totalamount.getText().toString())*Float.valueOf(discount.getText().toString());
        discountamt.setText(String.valueOf(x));
        float xy=Float.valueOf(totalamount.getText().toString())-Float.valueOf(discountamt.getText().toString());
        finalamt.setText(String.valueOf(xy));
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
                    float xy=Float.valueOf(totalamount.getText().toString())-Float.valueOf(discountamt.getText().toString());
                    finalamt.setText(String.valueOf(xy));
                    htmlDocument+=" <tr><td>"+pid+"</td>" +
                            "<td>"+pname+"</td><td>"+quantity+"</td><td>" +
                            ""+price+"</td><td>"+amount+"</td></tr>";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listview.setAdapter(adapter);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebView webView = new WebView(getApplicationContext());
                htmlDocument+="<tr><td colspan='3'><h2> Total Amount</h2> </td><td colspan='2'>" +
                        "<h2>"+totalamount.getText().toString()+"</h2></td>" + "</tr>" +
                        "<tr><td colspan='3'><h2>Discount</h2></td><td colspan='2'>" +
                        "<h2>"+discountamt.getText().toString()+"</h2></td></tr>" +
                        "<tr><td colspan='3'><h2>Final Amount </h2></td><td colspan='2'>" +
                        "<h2>"+finalamt.getText().toString()+"</h2></td></tr>" +
                        "</table></body></html>";
                webView.loadDataWithBaseURL(null, htmlDocument,
                        "text/HTML", "UTF-8", null);

                myWebView = webView;
                webView.setWebViewClient(new WebViewClient() {

                    public boolean shouldOverrideUrlLoading(WebView view,
                                                            String url)
                    {
                        return false;
                    }

                    @Override
                    public void onPageFinished(WebView view, String url)
                    {
                        createWebPrintJob(view);
                        myWebView = null;
                    }
                });
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("bill");
                ref.child(billno).removeValue();
                Toast.makeText(Bill.this, "Receipt Printed!", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),AdminHome.class);
                startActivity(intent);
            }
        });
    }
    private void createWebPrintJob(WebView webView) {

        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        PrintDocumentAdapter printAdapter =
                webView.createPrintDocumentAdapter("MyDocument");

        String jobName = getString(R.string.app_name) + " Print Test";

        printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }
}
