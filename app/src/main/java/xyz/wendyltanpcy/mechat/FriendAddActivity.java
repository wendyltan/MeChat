package xyz.wendyltanpcy.mechat;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.wendyltanpcy.mechat.model.FriendInfo;

public class FriendAddActivity extends BaseActivity {

    @BindView(R.id.enterName)
    EditText enterName;
    @BindView(R.id.enterID)
    EditText enterID;

    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @BindView(R.id.radio1)
    RadioButton mRadioButton1;
    @BindView(R.id.radio2)
    RadioButton mRadioButton2;
    @BindView(R.id.radio3)
    RadioButton mRadioButton3;
    @BindView(R.id.radio4)
    RadioButton mRadioButton4;

    @BindView(R.id.floatingActionButton)
    FloatingActionButton fab;

    private int checkID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_add);
        ButterKnife.bind(this);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                checkID = i;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendInfo info = new FriendInfo();
                List<FriendInfo> friendInfos = DataSupport.findAll(FriendInfo.class);

                if (!enterName.getText().toString().isEmpty()&&!enterID.getText().toString().isEmpty()){
                    String name = enterName.getText().toString();
                    int id = Integer.parseInt(enterID.getText().toString());
                    for (FriendInfo inf:friendInfos){
                        if (inf.getId()==id){
                            Toast.makeText(getApplicationContext(),"id已经存在！",Toast.LENGTH_SHORT).show();
                            enterID.getText().clear();
                            break;
                        }
                        else
                            info.setId(id);
                        if(inf.getFriendName().equals(name)){
                            Toast.makeText(getApplicationContext(),"name已经存在！",Toast.LENGTH_SHORT).show();
                            enterName.getText().clear();
                            break;
                        }
                        else
                            info.setFriendName(name);
                    }


                    if (checkID==mRadioButton1.getId())
                        info.setFriendCategory(1);
                    else if (checkID==mRadioButton2.getId())
                        info.setFriendCategory(2);
                    else if (checkID==mRadioButton3.getId())
                        info.setFriendCategory(3);
                    else if (checkID==mRadioButton4.getId())
                        info.setFriendCategory(4);
                    if (!enterName.getText().toString().isEmpty()&&!enterID.getText().toString().isEmpty()) {
                        info.save();
                        Toast.makeText(getApplicationContext(), "朋友创建成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else
                    Toast.makeText(getApplicationContext(),"有些信息没填写？",Toast.LENGTH_SHORT).show();



            }
        });



    }
}
