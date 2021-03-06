package org.techtown.letseat.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.letseat.RestSearch2;
import org.techtown.letseat.util.AppHelper;
import org.techtown.letseat.util.DatePickerFragment;
import org.techtown.letseat.R;

public class RegisterActivity extends AppCompatActivity {
    private String birthday_string, email_string, pwd_string, name_string, pwd_check_string, gender;
    private Editable email_edit, pwd_edit, name_edit, pwd_check_edit;
    private EditText email, pwd, name, pwd_check, birthday;
    private RadioGroup gender_radio;
    private RadioButton male, female;
    Button btn_register, btn_email_check, btn_birthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        email = findViewById(R.id.nickName);
        pwd = findViewById(R.id.birthday);
        pwd_check = findViewById(R.id.regitser_pwd_check);
        name = findViewById(R.id.register_name);
        birthday = findViewById(R.id.register_birthday);
        gender_radio = findViewById(R.id.register_gender);
        male = findViewById(R.id.register_male);
        female = findViewById(R.id.register_female);
        birthday.setEnabled(false);
        pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pwd_check.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        MaterialToolbar toolbar = findViewById(R.id.topMain);

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_birthday = findViewById(R.id.register_birthday_input);
        btn_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });
        btn_email_check = findViewById(R.id.register_email_check);
        btn_email_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_string = email.getText().toString();
                sendLoginCheckRequest(email_string, email);
            }
        });
        if (AppHelper.requestQueue != null) { //RequestQueue ??????
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_string = email.getText().toString();
                pwd_string = pwd.getText().toString();
                name_string = name.getText().toString();
                pwd_check_string = pwd_check.getText().toString();
                if (male.isChecked() || female.isChecked()) {
                    if (male.isChecked()) {
                        gender = "male";
                    } else {
                        gender = "female";
                    }
                }
                if (email_string.isEmpty() || pwd_string.isEmpty() || name_string.isEmpty()
                        || birthday_string.isEmpty() || (!male.isChecked() && !female.isChecked())) {
                    Toast.makeText(getApplicationContext(), "email_string: " + email_string + " pwd_string: " + pwd_string +
                            " name_string: " + name_string + "birthday_string: " + birthday_string, Toast.LENGTH_SHORT).show();
                    Log.d("testcase", "email_edit: " + email_edit + " pwd_edit: " + pwd_edit +
                            " name_edit: " + name_edit + "birthday.text: " + birthday.getText().toString());
                } else if (!pwd_check_string.equals(pwd_string)) {
                    Toast.makeText(getApplicationContext(), "??????????????? ???????????? ???????????? ???????????? ????????????. ?????? ??????????????????.", Toast.LENGTH_SHORT).show();
                } else {
                    sendRegisterRequest();
                }
            }
        });
    }
    // DatePicker Fragment ?????????
    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    // DatePicker?????? Date ?????? ??? ??????
    public void processDatePickerResult(int year, int month, int day) {
        String month_string = Integer.toString(month + 1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        birthday_string = year_string + "." + month_string + "." + day_string;
        birthday.setText(birthday_string);
    }
    // ???????????? POST ??????
    public void sendRegisterRequest() {
        String url = "http://125.132.62.150:8000/letseat/register/normal";
        JSONObject postData = new JSONObject();
        try {
            postData.put("email", email_string);
            postData.put("password", pwd_string);
            postData.put("birthday", birthday_string);
            postData.put("gender", gender);
            postData.put("name", name_string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                postData,
                new Response.Listener<JSONObject>() {
                    @Override // ?????? ??? ????????? ???
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(RegisterActivity.this, Login.class);
                        Toast.makeText(getApplicationContext(), "??????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override // ?????? ?????? ???
                    public void onErrorResponse(VolleyError error) {
                        println("?????? ???????????? ???????????????.");
                    }
                }
        );
        request.setShouldCache(false); // ?????? ?????? ????????? ?????? ????????? ????????? ?????????
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requsetQueue ?????????
        AppHelper.requestQueue.add(request);
    }

    // ????????? ?????? ?????? GET
    public void sendLoginCheckRequest(String email_string, TextView email) {
        String url = "http://125.132.62.150:8000/letseat/register/email/check?email=" + email_string;
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override // ?????? ??? ????????? ???
                    public void onResponse(String response) {
                        if (response.equals("emailCheckFail")) {
                            println("??? ????????? ????????? ????????????????????????.");
                            email.setText("");
                        } else {
                            println("??????????????? ??????????????????.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override // ?????? ?????? ???
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Tag_Error",error.toString());
                        println("?????? ?????? ??????");
                        Log.d("error",error.toString());
                    }

                }
        );
        request.setShouldCache(false); // ?????? ?????? ????????? ?????? ????????? ????????? ?????????
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requsetQueue ?????????
        AppHelper.requestQueue.add(request);
    }

    public void println(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
