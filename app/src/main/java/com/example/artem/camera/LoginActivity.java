package com.example.artem.camera;

import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.artem.camera.GPS.FallbackLocationTracker;
import com.example.artem.camera.GPS.LocationTracker;
import com.example.artem.camera.GPS.ProviderLocationTracker;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBSettings;
import com.quickblox.users.model.QBUser;

import java.util.List;

/**
 * Created by Artem on 23.12.2015.
 */
public class LoginActivity extends ActionBarActivity {

    private EditText log;
    private EditText pass;
    private String password;
    private String login;
    private Button btn;
    private Button reg;
    private static QBUser user;
    private Dialog progressDialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //QBSettings.getInstance().fastConfigInit(String.valueOf(Consts.APP_ID), Consts.AUTH_KEY, Consts.AUTH_SECRET);
        log = (EditText)findViewById(R.id.eLogin);
        pass = (EditText)findViewById(R.id.ePass);
        btn = (Button)findViewById(R.id.btngo);
        reg = (Button)findViewById(R.id.btnreg);
        progressDialog = Utils.startLoadingDialog(LoginActivity.this,"Пожалуйста подождите.");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!log.getText().toString().replace(" ", "").isEmpty() && !pass.getText().toString().replace(" ", "").isEmpty()) {
                    password = pass.getText().toString();
                    login = log.getText().toString();
                    authUser(Consts.USER_LOGIN, Consts.USER_PASSWORD);
                    progressDialog.show();
                } else {
                    Toast.makeText(LoginActivity.this, "Введите логин и пароль!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"));
                startActivity(browser);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_auth).setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            System.exit(0);
        }
        if (id == R.id.action_places) {
            Toast.makeText(LoginActivity.this,"Сначала авторизируйтесь!",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void authUser(String l, String p) {
        user = new QBUser("qwerty123","12345678");
        QBAuth.createSession(user, new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                progressDialog.dismiss();
                //Toast.makeText(LoginActivity.this,user.,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(List<String> list) {
                progressDialog.dismiss();
                user = null;
                Toast.makeText(LoginActivity.this,
                        "Ошибка авторизации.\nНеверный логин или пароль"
                        ,Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
    public  static QBUser getUser() {
        return  user;
    }

}
