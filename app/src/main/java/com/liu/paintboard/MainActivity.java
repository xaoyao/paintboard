package com.liu.paintboard;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ImageView iv;
    private Bitmap bmpCopy;
    private Paint paint;
    private Canvas canvas;
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv= (ImageView) findViewById(R.id.iv);
        bmp= BitmapFactory.decodeResource(getResources(),R.drawable.bg);
        bmpCopy=Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(),bmp.getConfig());
        //创建画笔对象
        paint=new Paint();
        //创建画板对象
        canvas=new Canvas(bmpCopy);
        //画出可编辑的空白图像
        canvas.drawBitmap(bmp,new Matrix(),paint);
        iv.setImageBitmap(bmpCopy);
        //设置触摸监听
        iv.setOnTouchListener(new View.OnTouchListener() {
            int startX;
            int startY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX= (int) event.getX();
                        startY= (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int newX= (int) event.getX();
                        int newY= (int) event.getY();
                        //划线
                        canvas.drawLine(startX,startY,newX,newY,paint);
                        //更新起点坐标
                        startX=newX;
                        startY=newY;iv.setImageBitmap(bmpCopy);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });
    }

    public void click(View view){
        switch (view.getId()){
            case R.id.btn_black:
                paint.setColor(Color.BLACK);
                break;
            case R.id.btn_red:
                paint.setColor(Color.RED);
                break;
            case R.id.btn_blue:
                paint.setColor(Color.BLUE);
                break;
            case R.id.btn_bisua:
                paint.setStrokeWidth(10);
                break;
            case R.id.btn_pen:
                paint.setStrokeWidth(1);
                break;
            case R.id.btn_save:
                FileOutputStream fos=null;
                //保存
                try {
                    fos=new FileOutputStream("sdcard/paintBoard.jpg");
                    bmpCopy.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    Toast.makeText(MainActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }finally {
                    if(fos!=null){
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case R.id.btn_clear:
                //清除
                canvas.drawBitmap(bmp,new Matrix(),paint);
                iv.setImageBitmap(bmpCopy);
                break;
        }
    }
}
