package com.example.retrofitdemo.presenter;

import android.graphics.Bitmap;

import com.example.retrofitdemo.model.MyModel;
import com.example.retrofitdemo.retrofit.MyInterfaces;
import com.example.retrofitdemo.retrofit.MyJieKou;

import java.util.Map;


/**
 *
 * @param <V>  v代表view视图
 *
 * p层依然是那么几步操作，
 *    1.p与v的绑定
 *    2.向model层传输url路径，并接收model层回调回来的数据
 *    3.实现p与v的解绑
 */

   //为p层指定泛型对象
public class MyPresenter<V>  {

    private final MyModel myModel;

    V view;

    public MyPresenter() {
        myModel = new MyModel();
    }
    //将p层与v层绑定
    public void attch(V view){
        this.view=view;
    }

    /**
     *  present中的普通get请求
     * @param url
     * @param myInterfaces
     */
    public void getPreContent(String url, final MyInterfaces myInterfaces){
        myModel.getModContent(url, new MyInterfaces() {
            @Override
            public void chenggong(String json) {
                myInterfaces.chenggong(json);
            }

            @Override
            public void shibai(String ss) {
                myInterfaces.shibai(ss);
            }
        });
    }

    /**
     * present中的普通post请求
     * @param url
     * @param map
     * @param myInterfaces
     */
    public void postPreContent(String url, Map<String,Object> map, final MyInterfaces myInterfaces){

        myModel.postModContent(url, map, new MyInterfaces() {
            @Override
            public void chenggong(String json) {
                myInterfaces.chenggong(json);
            }

            @Override
            public void shibai(String ss) {
                myInterfaces.shibai(ss);
            }
        });
    }


    /**
     * present中的postFlyRoute请求  添加头参数 且已json格式入参
     * @param url
     * @param map
     * @param myInterfaces
     */
    public void postFlyRoutePreContent(String url, Map<String,Object> map, final MyInterfaces myInterfaces){

        myModel.postFlyRouteModContent(url, map, new MyInterfaces() {
            @Override
            public void chenggong(String json) {
                myInterfaces.chenggong(json);
            }

            @Override
            public void shibai(String ss) {
                myInterfaces.shibai(ss);
            }
        });
    }

    /**
     *  P 层中的获取图形验证码
     * @param url
     * @param myJieKou
     */
    public void getPro_TuXingYanZheng(String url, final MyJieKou myJieKou){
        myModel.getMod_TuXingYanZheng(url, new MyJieKou() {
            @Override
            public void chenggong(Bitmap bitmap) {
                myJieKou.chenggong(bitmap);
            }

            @Override
            public void shibai(String ss) {
                myJieKou.shibai(ss);
            }
        });
    }




    //将泛型v与p进行解绑
    public void setonDestroy(){
        try{
            this.view=null;
        }catch (Exception e){

        }
    }
}
