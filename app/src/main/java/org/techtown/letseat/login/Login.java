package org.techtown.letseat.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.ApiErrorCode;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.letseat.Kakao_Login_userInfo;
import org.techtown.letseat.util.AppHelper;
import org.techtown.letseat.MainActivity;
import org.techtown.letseat.R;

import java.security.MessageDigest;




public class Login extends AppCompatActivity {
    private Button  btn_register, login_button;
    private LoginButton kakao_login_button;
    private ImageView fakeKakao;
    private EditText input_email, input_password;
    private String email_string, pwd_string,kakao_email_string;



    private KaKaoCallBack kaKaoCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        login_button = findViewById(R.id.login_button);
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        input_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        fakeKakao = findViewById(R.id.fake_kakao);

        //???????????????
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_string = input_email.getText().toString();
                pwd_string = input_password.getText().toString();
                login();
            }
        });


        btn_register = findViewById(R.id.btnRegister);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });

        //????????? ?????????
        kaKaoCallBack = new KaKaoCallBack();
        Session.getCurrentSession().addCallback(kaKaoCallBack);
        Session.getCurrentSession().checkAndImplicitOpen();


        kakao_login_button = findViewById(R.id.kakao_login_button);

        fakeKakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kakao_login_button.performClick();
            }
        });

        HashKey();
    }



    public void kakaoError(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(kaKaoCallBack);
    }

    private class KaKaoCallBack implements ISessionCallback{
        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if (result == ApiErrorCode.CLIENT_ERROR_CODE)
                        kakaoError("???????????? ????????? ??????????????????. ?????? ????????? ?????????.");
                    else kakaoError("????????? ?????? ????????? ??????????????????.");
                }
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    kakaoError("????????? ???????????????. ?????? ????????? ?????????.");
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    String needsScopeAutority = "";

                    if(result.getKakaoAccount().needsScopeAccountEmail()) {
                        needsScopeAutority = needsScopeAutority + "?????????";
                    }
                    if(result.getKakaoAccount().needsScopeGender()) {
                        needsScopeAutority = needsScopeAutority + ", ??????";
                    }
                    if(result.getKakaoAccount().needsScopeAgeRange()) {
                        needsScopeAutority = needsScopeAutority + ", ?????????";
                    }
                    if(result.getKakaoAccount().needsScopeBirthday()) {
                        needsScopeAutority = needsScopeAutority + ", ??????";
                    }

                    if(needsScopeAutority.length() != 0) { // ?????? ????????? ???????????? ?????? ????????? ????????? -> ???????????? ?????? ????????? ???????????? ???????????? ??????
                        if (needsScopeAutority.charAt(0) == ',') {
                            needsScopeAutority = needsScopeAutority.substring(2);
                        }
                        Toast.makeText(getApplicationContext(), needsScopeAutority + "??? ?????? ????????? ???????????? ???????????????. ???????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show(); // ???????????? ????????? ?????????????????? Toast ????????? ??????

                        // ???????????? ??????


                        UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
                            @Override
                            public void onFailure(ErrorResult errorResult) {
                                int result = errorResult.getErrorCode();

                                if (result == ApiErrorCode.CLIENT_ERROR_CODE) {
                                    Toast.makeText(getApplicationContext(), "???????????? ????????? ??????????????????. ?????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "????????? ??????????????????. ?????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onSessionClosed(ErrorResult errorResult) {
                                Toast.makeText(getApplicationContext(), "????????? ????????? ???????????????. ?????? ???????????? ?????????.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNotSignedUp() {
                                Toast.makeText(getApplicationContext(), "???????????? ?????? ???????????????. ?????? ???????????? ?????????.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(Long result) {

                            }
                        });
                    }

                    else{
                        kakao_email_string = result.getKakaoAccount().getEmail().toString();
                        sendLoginCheckRequest();
                    }
                }
            });
        }
        @Override
        public void onSessionOpenFailed (KakaoException e){
            kakaoError("????????? ?????? ????????? ??????????????????. ????????? ????????? ??????????????????.");
        }

    }

    //????????? ????????? ????????????

    // ????????? ?????? ?????? GET
    public void sendLoginCheckRequest() {
        String url = "http://125.132.62.150:8000/letseat/register/email/check?email=" + kakao_email_string;
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override // ?????? ??? ????????? ???
                    public void onResponse(String response) {
                        if (response.equals("emailCheckFail")) // ??????????????? ????????? ?????????????????? ?????? ????????????????????????
                        {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else  //DB??? ???????????? ???????????? ???????????????
                            {
                            Intent intent = new Intent(getApplicationContext(), Kakao_Login_userInfo.class);
                            intent.putExtra("send",kakao_email_string);
                            startActivity(intent);
                            finish();
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



    private void HashKey() {
        try {
            PackageInfo pkinfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : pkinfo.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
                messageDigest.update(signature.toByteArray());
                String result = new String(Base64.encode(messageDigest.digest(), 0));
                Log.d("??????", result);
            }
        }
        catch (Exception e) {

        }
    }
    public void println(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    // ?????? ????????? POST ??????
    void login(){
        String url = "http://125.132.62.150:8000/letseat/login/normal";
        JSONObject postData = new JSONObject();
        try {
            postData.put("email", email_string);
            postData.put("password", pwd_string);
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
                        SharedPreferences preferences = getSharedPreferences("email",MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("email",email_string);
                        editor.commit();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override // ?????? ?????? ???
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error",error.toString());
                        println("???????????? ??????????????? ?????? ??????????????????.");
                    }
                }
        );
        request.setShouldCache(false); // ?????? ?????? ????????? ?????? ????????? ????????? ?????????
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requsetQueue ?????????
        AppHelper.requestQueue.add(request);
    }


}