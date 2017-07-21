package com.example.liuhaifeng.littletoolsdemo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by liuhaifeng on 2017/4/11.
 */

public class MyAppWidgetProvider extends AppWidgetProvider {

    //保存WidgetId HashSet  可能有用户创建了多个Widget
    private static HashSet<Integer> hashSet=new HashSet<Integer>();
    //图片资源,作者请自行修改成自己的图片
    private static final int[] ResId={R.drawable.u0,R.drawable.group_green,R.drawable.home_green,R.drawable.home_white,R.drawable.huihua_black};
    //文本资源
    private static final String[] intro={"神挡杀神，佛挡杀佛","仿佛兮若轻云之蔽月，飘飘兮若流风之回雪",
            "此乃驱狼吐虎之计，敬你之手他一搏吧","汝等小儿,可敢杀我",
            "汝等看好了"};
    private static int iCur=-1;

    //周期更新时调用
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetProvider, int[] appWidgetIds)
    {
        Log.v("创建Widget", "OK");
        // 每次 widget 被创建时，对应的将widget的id添加到set中
        for(int id:appWidgetIds)
            hashSet.add(Integer.valueOf(id));

    }

    //当桌面组件删除时调用
    @Override
    public void onDeleted(Context context,int[] appWidgetIds)
    {
        for(int id:appWidgetIds)
            hashSet.remove(id);

        super.onDeleted(context, appWidgetIds);
    }

    //当桌面提供的第一个组件创建时调用
    @Override
    public void onEnabled(Context context)
    {
        //启动服务
        context.startService(new Intent(context,MyService.class));

        super.onEnabled(context);
    }

    //当桌面提供的最后一个组件删除时调用
    @Override
    public void onDisabled(Context context)
    {
        //停止服务
        context.stopService(new Intent(context,MyService.class));

        super.onDisabled(context);
    }

    /*
     * 重写广播接收方法
     * 用于接收除系统默认的更新广播外的  自定义广播（本程序由服务发送过来的，一个是更新UI，一个是按钮事件消息）
     */
    @Override
    public void onReceive(Context context,Intent intent)
    {
        String getAction=intent.getAction();
        if(getAction.equals(Constant.ACTION_UPDATE_ALL))
        {
            //更新广播

            updateAllWidget(context,AppWidgetManager.getInstance(context), hashSet);
        }
        else if(intent.hasCategory(Intent.CATEGORY_ALTERNATIVE))
        {
            Uri data=intent.getData();
            Log.v("button",data.toString()+" ");
            int buttonid=Integer.parseInt(data.getSchemeSpecificPart());
            Toast.makeText(context, intro[buttonid], Toast.LENGTH_SHORT).show();
        }

        super.onReceive(context, intent);
    }

    //更新UI
    public void updateAllWidget(Context context,AppWidgetManager manager,HashSet<Integer> set)
    {
        int AppId;
        Iterator iterator=set.iterator();

        iCur=iCur+1>=intro.length? 0: iCur+1;

        RemoteViews remoteViews=new RemoteViews(context.getPackageName(),R.layout.layout);

           //   remoteViews.setImageViewResource(R.id.imageView, ResId[0]);

        while(iterator.hasNext())
        {
//            AppId=((Integer)iterator.next()).intValue();
//
//            //设置显示的文字图片
//            remoteViews.setTextViewText(R.id.editText4, intro[iCur]);
//            //添加按钮事件处理
//            remoteViews.setOnClickPendingIntent(R.id.button_1, getPendingIntent(context, iCur));
            //更新
//            manager.updateAppWidget(AppId, remoteViews);
        }
    }

    //设置按钮事件处理
    private PendingIntent getPendingIntent(Context context, int buttonid)
    {
        Intent intent=new Intent("test.test");
        intent.setClass(context, MyAppWidgetProvider.class);
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        intent.setData(Uri.parse("custom:"+buttonid));
        //进行广播
        PendingIntent pi=PendingIntent.getBroadcast(context, 0, intent, 0);
        return pi;
    }
}
