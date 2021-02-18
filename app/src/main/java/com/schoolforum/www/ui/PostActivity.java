package com.schoolforum.www.ui;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.schoolforum.www.R;
import com.schoolforum.www.entity.User;
import com.schoolforum.www.util.FileUtil;
import com.schoolforum.www.util.ImageUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostActivity extends AppCompatActivity {

    @BindView(R.id.left)
    ImageView left_iv;
    @BindView(R.id.publish)
    Button publishBtn;
    @BindView(R.id.title)
    EditText title_et;
    @BindView(R.id.content)
    EditText content_et;
    @BindView(R.id.picture)
    ImageView picture_iv;
    Context context;
    CharSequence[] items = {"相册", "相机"};
    public static final int RESULT_CAPTURE_IMAGE = 99;//拍照返回值
    public static final int RESULT_SELECT_IMAGE = 100;//相册返回值
    String takePath;//照片路径
    String title,content;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
        context = this;
        user = (User)getIntent().getSerializableExtra("user");
        left_iv.setOnClickListener(clickListener);
        publishBtn.setOnClickListener(clickListener);
        picture_iv.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch(v.getId()) {
                case R.id.left:
                    intent = new Intent(context,UserMainActivity.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.publish:
                    title = title_et.getText().toString().trim();
                    content = content_et.getText().toString().trim();
                    if(title.equals("")){
                        Toast.makeText(context,"请输入标题！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(content.equals("")){
                        Toast.makeText(context,"请输入正文！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<File> files = FileUtil.getFiles(LoginActivity.fileDir);
                    String reqUrl = getResources().getString(R.string.api_url)+"addpost";
                    Map<String, String> params = new HashMap<>();
                    params.put("title",title);
                    params.put("content",content);
                    params.put("postman",user.getLoginnname());
                    upload(reqUrl,params,"uploadImg",files);

                    break;
                case R.id.picture:
                    new AlertDialog.Builder(context)
                            .setTitle("选择图片来源")
                            .setItems(items, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if( which == 0 ){
                                        takePath = LoginActivity.fileDir + "/"+System.currentTimeMillis()+ ".jpg";
                                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                                        intent.setType("image/*");
                                        startActivityForResult(Intent.createChooser(intent, "选择图片"), RESULT_SELECT_IMAGE);
                                    }else{
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        takePath = LoginActivity.fileDir + "/"+System.currentTimeMillis()+ ".jpg";
                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(context,new File(takePath)));
                                        startActivityForResult(intent, RESULT_CAPTURE_IMAGE);
                                    }
                                }
                            })
                            .create().show();
                    break;
                default:
                    break;
            }
        }
    };

    private Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.schoolforum.www.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_CAPTURE_IMAGE:
                if (resultCode == RESULT_OK) {
                    Bitmap mTakeBitmap = ImageUtil.getimage(takePath);
                    if(mTakeBitmap!=null){
                        ImageUtil.saveFile(mTakeBitmap,takePath);
                        picture_iv.setImageBitmap(mTakeBitmap);
                    }
                }
                break;
            case RESULT_SELECT_IMAGE:
                if(resultCode == RESULT_OK){
                    Uri uri = data.getData();
                    ContentResolver resolver = context.getContentResolver();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver, uri);
                        if (bitmap != null) {
                            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                            Bitmap mTakeBitmap = ImageUtil.compressImage(bitmap);
                            //释放原始图片占用的内存，防止out of memory异常发生
                            bitmap.recycle();
                            if(mTakeBitmap!=null){
                                ImageUtil.saveFile(mTakeBitmap,takePath);
                                picture_iv.setImageBitmap(mTakeBitmap);
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(context, "请重新选择图片", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private void upload(String reqUrl, Map<String, String> params, String pic_key, List<File> files){
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        //遍历map中所有参数到builder
        if (params != null){
            for (String key : params.keySet()) {
                multipartBodyBuilder.addFormDataPart(key, params.get(key));
            }
        }
        //遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
        if (files != null){
            for (File file : files) {
                multipartBodyBuilder.addFormDataPart(pic_key, file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
            }
        }
        //构建请求体
        RequestBody requestBody = multipartBodyBuilder.build();
        Request.Builder RequestBuilder = new Request.Builder();
        RequestBuilder.url(reqUrl);// 添加URL地址
        RequestBuilder.post(requestBody);
        Request request = RequestBuilder.build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "发帖成功！", Toast.LENGTH_SHORT).show();
                        FileUtil.deleteFile(LoginActivity.fileDir);
                    }
                });
            }
        });

    }

}
