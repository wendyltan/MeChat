package xyz.wendyltanpcy.mechat;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity{


    @BindView(R.id.UserName)
    EditText user;
    @BindView(R.id.password)
    EditText password;

    private IntentFilter intentFilter;

    @BindView(R.id.remember_pwd)
    CheckBox rememberPwd;
    private boolean isRemember;

    @BindView(R.id.login_view)
    View mainView ;


    @BindView(R.id.login)
    Button login;
    @BindView(R.id.register)
    Button register;



    private NetworkChangeReceiver networkChangeReceiver;



    class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI已连接,移动数据已连接", Toast.LENGTH_SHORT).show();
            } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI已连接,移动数据已断开", Toast.LENGTH_SHORT).show();
            } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI已断开,移动数据已连接", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "WIFI已断开,移动数据已断开", Toast.LENGTH_SHORT).show();
            }

        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

//        intentFilter = new IntentFilter();
//        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        networkChangeReceiver = new NetworkChangeReceiver();
//        registerReceiver(networkChangeReceiver, intentFilter);


        judgeIfRememberPwd();

        //using observer to solve login logic

        final Observer<Button> loginObserver = new Observer<Button>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(Button button) {
                loginLogic(button);
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onComplete() {}
        };

        final Observable<Button> loginObservable = Observable.just(login);

        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                loginObservable.subscribe(loginObserver);
            }
        });



        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        Snackbar.make(mainView,"WelCome to Me Chat!",Snackbar.LENGTH_SHORT)
                .show();


    }


    /**
     * login button logic
     * @param button
     */

    public void loginLogic(Button button){
        final String userText = user.getText().toString();
        String pwdText = password.getText().toString();
        final ProgressDialog pdialog = new ProgressDialog(LoginActivity.this);
        pdialog.setTitle("Wait for login...");
        pdialog.setMessage("Loading...");
        pdialog.setCancelable(true);


        if(userText.equals("admin")&&pwdText.equals("123")){
            Intent intent = new Intent("xyz.wendyltanpcy.exp3.FORCE_OFFLINE");
            sendBroadcast(intent);

        }
        else if (!userText.isEmpty()&&!pwdText.isEmpty()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.show();
                        }
                    });
                    try {
                        Thread.sleep(1000);
                        Intent i = new Intent(LoginActivity.this, FriendListActivity.class);
                        i.putExtra("userName",userText);
                        startActivity(i);
                        pdialog.dismiss();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
            if (rememberPwd.isChecked()){
                //remember pwd
                inputPwdPreference("repwd","checkboxEditor",true);
                //rememeber usr and password
                inputInfoPreference("user","infoEditor",userText);
                inputInfoPreference("pwd","infoEditor",pwdText);
                Snackbar.make(button.getRootView(),"save info success!",Snackbar.LENGTH_SHORT).show();
            }else{
                //do nothing,using default
                inputPwdPreference("repwd","checkboxEditor",false);
                inputInfoPreference("user","infoEditor",null);
                inputInfoPreference("pwd","infoEditor",null);
            }


        }
        else{
            Toast.makeText(LoginActivity.this,"fill in all field pls",Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * judge if isRemember,if is,load data
     */
    private void judgeIfRememberPwd(){
        //read status code
        isRemember = outputPwdPreference("repwd","checkboxEditor");
        if (isRemember){
            rememberPwd.setChecked(isRemember);
            user.setText(outputInfoPreference("user","infoEditor"));
            password.setText(outputInfoPreference("pwd","infoEditor"));
        }
    }


    //using sharedpreference

    private void inputPwdPreference(String tag, String editorTag, boolean data){
        SharedPreferences.Editor editor = getSharedPreferences(tag,MODE_PRIVATE).edit();
        editor.putBoolean(editorTag,data);
        editor.apply();
    }

    private boolean outputPwdPreference(String tag, String editorTag){
        SharedPreferences preferences = getSharedPreferences(tag,MODE_PRIVATE);
        boolean content = preferences.getBoolean(editorTag,false);
        return content;
    }

    private void inputInfoPreference(String tag, String editorTag, String data){
        SharedPreferences.Editor editor = getSharedPreferences(tag,MODE_PRIVATE).edit();
        editor.putString(editorTag,data);
        editor.apply();
    }

    private String outputInfoPreference(String tag, String editorTag){
        SharedPreferences preferences = getSharedPreferences(tag,MODE_PRIVATE);
        String content = preferences.getString(editorTag,null);
        return content;
    }


}