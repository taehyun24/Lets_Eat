package org.techtown.letseat.mytab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.techtown.letseat.MainActivity;
import org.techtown.letseat.MyReview;
import org.techtown.letseat.MytabreviewManage;
import org.techtown.letseat.R;
import org.techtown.letseat.RestSearch2;
import org.techtown.letseat.login.Login;
import org.techtown.letseat.settingActivity;
import org.techtown.letseat.zzimActivity;

public class MyTab extends AppCompatActivity {

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mytab_main);

        Intent intent = getIntent();
        userId = intent.getIntExtra("userId",0);


        MaterialToolbar toolbar = findViewById(R.id.topMain);

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(homeIntent);
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.actionsearch:
                        Intent settingIntent = new Intent(getApplicationContext(), RestSearch2.class);
                        startActivity(settingIntent);
                        break;
                }
                return true;
            }
        });

        ImageButton settingBtn = findViewById(R.id.settingBtn);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), settingActivity.class);
                startActivity(intent);
            }
        });
        ImageButton favoriteBtn = findViewById(R.id.favoriteBtn);
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), zzimActivity.class);
                startActivity(intent);
            }
        });

        ImageButton reviewManageBtn = findViewById(R.id.reviewManageBtn);
        reviewManageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyReview.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        ImageButton reviewManagebtn = findViewById(R.id.reviewManageBtn);
        reviewManagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MytabreviewManage.class);
                startActivity(intent);
            }
        });

        Button w_s_button = findViewById(R.id.waiting_status_button);
        w_s_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), waiting_Layout.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        ImageButton i_f_button = findViewById(R.id.information_fix_button);
        i_f_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), information_fix.class);
                startActivity(intent);
            }
        });

        //카카오 로그아웃
        Button kakao_logout_button = findViewById(R.id.kakao_logout_button);
        kakao_logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(MyTab.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}