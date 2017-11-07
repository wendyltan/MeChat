package xyz.wendyltanpcy.mechat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private Button confirmBtn;
    private Button cancelBtn;
    private EditText reUserName;
    private EditText rePassword;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        confirmBtn = (Button) findViewById(R.id.confirm);
        cancelBtn = (Button) findViewById(R.id.cancel);
        reUserName = (EditText) findViewById(R.id.reUserName);
        rePassword = (EditText) findViewById(R.id.rePassword);

        confirmBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.confirm:
                String userText = reUserName.getText().toString();
                String pwdText = rePassword.getText().toString();
                if (!userText.isEmpty()&&!pwdText.isEmpty()) {
                    Intent i = new Intent(RegisterActivity.this, FriendListActivity.class);
                    i.putExtra("userName",userText);
                    startActivity(i);
                }else {
                    Toast.makeText(RegisterActivity.this,"fill in all field pls",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cancel:
                finish();
                break;
            default:

                break;

        }
    }
}

