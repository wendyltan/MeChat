package xyz.wendyltanpcy.mechat.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import xyz.wendyltanpcy.mechat.LoginActivity;
import xyz.wendyltanpcy.mechat.R;

public class BootCompleteReceiver extends BroadcastReceiver {

    //发送静态广播

    static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        // 当收听到的事件是“BOOT_COMPLETED”时，就创建并启动相应的Activity和Service
        if (intent.getAction().equals(ACTION)) {
            Toast.makeText(context,"BOOT COMPLETE!",Toast.LENGTH_SHORT).show();


            //看不到toast，使用notification 检验
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            // 设置通知的基本信息：icon、标题、内容
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle("Boot notification");
            builder.setContentText("Hello there");

            // 设置通知的点击行为：这里启动一个 Activity
            Intent i= new Intent(context, LoginActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            // 发送通知 id 需要在应用内唯一
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(222, builder.build());

        }
    }

}

