package com.example.liuhaifeng.littletoolsdemo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {
    private Context context;
    //更新周期
    private static final int UPDATE_TIME=5000;
    //周期性更新的Widget 线程
    private MyService.WidgetThread widgetThread;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate()
    {
        widgetThread=new MyService.WidgetThread();
        widgetThread.start();

        context=this.getApplicationContext();
        super.onCreate();
    }

    @Override
    public void onDestroy()
    {
        if(widgetThread!=null&&widgetThread.isAlive())
            widgetThread.interrupt();

        super.onDestroy();
    }

    private class WidgetThread extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                while(true)
                {
                    Intent intent=new Intent(Constant.ACTION_UPDATE_ALL);
                    context.sendBroadcast(intent);

                    sleep(UPDATE_TIME);
                }
            }catch(InterruptedException error)
            {
                // 将 InterruptedException 定义在while循环之外，意味着抛出 InterruptedException 异常时，终止线程。
            }
        }
    }
}
