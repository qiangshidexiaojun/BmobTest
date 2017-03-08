

package com.example.bombtext;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity implements onDelListener{

    private RecyclerView mRecyclerView;
    private List<Person> Personlist = new ArrayList<Person>();
    private MyAdapter mMyAdapter;
    private EditText edtname, edtaddress, edtage;
    private Button btnadd;
    private ImageView imghead;
    protected static final int GET_HEAD_IMG = 1001;
    private static final int CROP_HEAD = 1002;
    protected static final int GET_CAMERA = 1003;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bmob.initialize(this, "328862a95773a70ef3dc5d1e2215d254");

        initViews();
        findViews();
    }

    private void findViews() {
        imghead = (ImageView) findViewById(R.id.imghead);
        imghead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent() ;
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GET_HEAD_IMG);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GET_HEAD_IMG  || requestCode == GET_CAMERA){
            Uri imgUri = data.getData();

            //拿到图片后先裁剪
            Intent intent = new  Intent();
            intent.setAction("com.android.camera.action.CROP");
            intent.setDataAndType(imgUri, "image/*");
            intent.putExtra("crop", true);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, CROP_HEAD);
        }

        if(requestCode == CROP_HEAD){
            Bundle bundle = data.getExtras();
            Bitmap bmp = bundle.getParcelable("data");
            imghead.setImageBitmap(bmp);
        }
    }



    private void initViews() {
        edtname = (EditText) findViewById(R.id.edtname);
        edtaddress = (EditText) findViewById(R.id.edtaddress);
        edtage = (EditText) findViewById(R.id.edtage);
        btnadd = (Button) findViewById(R.id.btnadd);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person person = new Person();
                if (edtname.getText().toString().trim().equals("") || edtaddress.getText().toString().trim().equals("") || edtage.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, "姓名，地址，年龄都不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    person.setName(edtname.getText().toString().trim());
                    person.setAddress(edtaddress.getText().toString().trim());
                    person.setAge(Integer.valueOf(edtage.getText().toString().trim()));
                    person.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                edtname.setText("");
                                edtaddress.setText("");
                                edtage.setText("");
                                Toast.makeText(MainActivity.this, "添加数据成功", Toast.LENGTH_SHORT).show();
                                queryall();
                            } else {
                                Toast.makeText(MainActivity.this, "创建数据失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryall();
    }

    private void queryall() {
        BmobQuery<Person> query = new BmobQuery<Person>();
        query.findObjects(new FindListener<Person>() {
            @Override
            public void done(List<Person> list, BmobException e) {
                if (e == null) {
                    Personlist = list;
                    mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                    LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
                    mRecyclerView.setLayoutManager(manager);
                    mMyAdapter = new MyAdapter(Personlist,MainActivity.this);
                    mRecyclerView.setAdapter(mMyAdapter);
                } else {
                    Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void del(String name) {

    }

    @Override
    public void refresh() {
        queryall();
    }
}
