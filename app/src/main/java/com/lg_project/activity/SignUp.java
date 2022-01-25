package com.lg_project.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.R;
import com.lg_project.modelclass.country.Country;
import com.lg_project.modelclass.country.ResponseCountryData;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner labelledSpinner;
    TextView login_text;
    String[] country;
    String[] id;

    String str_id,str_email,str_firstname,str_lastame,str_age,str_password,str_phone,str_terms,str_pin;
    TextInputEditText email,firstname,lastname,age,password,phone;
    Button signup;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Country();

        checkBox=(CheckBox)findViewById(R.id.condition);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    str_terms="True";
                }
                else
                {
                    str_terms="False";
                }
            }
        });

        login_text =(TextView)findViewById(R.id.login_text);
       /* String text = "<font color=#FFFFFF>Already have an account? </font> <font color=#E31717>Log In</font>";
        login_text.setText(Html.fromHtml(text));*/
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUp.this,Login.class);
                startActivity(intent);
                finish();
            }
        });

        email=(TextInputEditText)findViewById(R.id.text_email);
        firstname=(TextInputEditText)findViewById(R.id.text_firstname);
        lastname=(TextInputEditText)findViewById(R.id.text_lastname);
        age=(TextInputEditText)findViewById(R.id.text_age);
        password=(TextInputEditText)findViewById(R.id.text_password);
        phone=(TextInputEditText)findViewById(R.id.text_phone);

        signup=(Button)findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_email=email.getText().toString();
                str_firstname=firstname.getText().toString();
                str_lastame=lastname.getText().toString();
                str_age=age.getText().toString();
                str_password=password.getText().toString();
                str_phone=phone.getText().toString();

                next(str_email,str_firstname,str_lastame,str_age,str_password,str_phone,str_id,str_terms);
            }
        });

        labelledSpinner = (Spinner) findViewById(R.id.spinner);
        labelledSpinner.setOnItemSelectedListener(this);


    }
    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long idd) {
        Log.d("ID",id[position]);
        str_id=id[position];
       // Toast.makeText(getApplicationContext(),""+id[position] , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void Country() {
        Retrofit retrofit = RetrofitInstance.getLocale(this);
        Api api = retrofit.create(Api.class);
        Call<ResponseCountryData> call = api.country();
        call.enqueue(new Callback<ResponseCountryData>() {
            @Override
            public void onResponse(Call<ResponseCountryData> call, Response<ResponseCountryData> response) {
                Log.d("COUN", String.valueOf(response.isSuccessful()));
                if (response.isSuccessful()) {
                 Log.d("COUN",String.valueOf(response.body()));
                    List<Country> list = response.body().getData();
                    country=new String[list.size()];
                    id=new String[list.size()];

                    for (int i = 0; i < list.size(); i++) {
                        country[i]=list.get(i).getTitle();
                        id[i]= String.valueOf(list.get(i).getId());
                        Log.d("COUN", list.get(i).getTitle());
                    }

                    ArrayAdapter adapter=new ArrayAdapter(SignUp.this,R.layout.text,country);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    labelledSpinner.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<ResponseCountryData> call, Throwable t) {
              //  Log.d("ERRor",t.getMessage());
                Toast.makeText(SignUp.this, ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });

        }

        public void next(String email,String firstname,String lastname,String age,String password,String phone,
                         String id,String terms)
        {
            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(firstname) || TextUtils.isEmpty(lastname) || TextUtils.isEmpty(age) ||
            TextUtils.isEmpty(password) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(id) || TextUtils.isEmpty(terms))
            {
                Toast.makeText(this, ""+getResources().getString(R.string.fill_all_fields),Toast.LENGTH_SHORT).show();
            }
            else
                {
                Intent intent = new Intent(SignUp.this, PinCode.class);
                intent.putExtra("First_Name", firstname);
                intent.putExtra("last_name", lastname);
                intent.putExtra("email", email);
                intent.putExtra("country", id);
                intent.putExtra("age", age);
                intent.putExtra("password", password);
                intent.putExtra("phone", phone);
                intent.putExtra("terms", terms);
                startActivity(intent);
                finish();
            }
        }



}