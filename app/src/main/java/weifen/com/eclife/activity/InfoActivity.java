package weifen.com.eclife.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import weifen.com.common.app.BaseApplication;
import weifen.com.common.base.BaseActivity;
import weifen.com.eclife.R;
import weifen.com.eclife.app.MyApplication;
import weifen.com.eclife.domain.Constant;
import weifen.com.eclife.domain.URLUtil;
import weifen.com.eclife.requestResult.RequestResult;

/**
 * Created by zhurencong on 2017/10/16.
 */
public class InfoActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private CircleImageView image;
    private EditText nickName, qq, signatureET;
    private Button save;

    private TextView fromAlbum, fromCamera, cancel,number;
    private PopupWindow popup;
    private LinearLayout rootview;
    private Uri imageUri;

    //图片路径
    private String imagePath;

    @Override
    protected int setLayout() {
        return R.layout.activity_info;
    }

    @Override
    protected void initView() {

        back = (ImageView) findViewById(R.id.back);
        image = (CircleImageView) findViewById(R.id.image);
        nickName = (EditText) findViewById(R.id.nick_name);
        number = (TextView) findViewById(R.id.number);
        qq = (EditText) findViewById(R.id.qq);
        signatureET = (EditText) findViewById(R.id.signature);
        save = (Button) findViewById(R.id.save);
        rootview = (LinearLayout) findViewById(R.id.root);


        image.setOnClickListener(this);
        save.setOnClickListener(this);
        back.setOnClickListener(this);
        initUserData();
    }

    //初始化用户个人信息
    private void initUserData() {
        nickName.setText(TextUtils.isEmpty(MyApplication.user.getNickName())?"": MyApplication.user.getNickName());
        number.setText(TextUtils.isEmpty(MyApplication.user.getTel())?"": MyApplication.user.getTel());
        signatureET.setText(TextUtils.isEmpty(MyApplication.user.getSignature())?"": MyApplication.user.getSignature());
        Picasso.with(this).load(URLUtil.URL_PATH+ MyApplication.user.getImageUrl()).error(getResources().getDrawable(R.mipmap.person_iocn)).into(image);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //退出
            case R.id.back:
                finish();
                //挑选图片
            case R.id.image:
                setPopup();
                break;
            case R.id.save:
                //发送头像
                if(TextUtils.isEmpty(imagePath)){
                    Toast.makeText(this,"未做修改",Toast.LENGTH_SHORT).show();
                    return;
                }
                ///my_image/Android/data/weifen.com.eclife/cache/image.png
                RequestResult.uploadUserPhoto(this, imagePath);
                break;
            //从相册选择图片
            case R.id.album:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    fromAlbum();
                }
                popup.dismiss();
                break;
            //相机拍照
            case R.id.camera:
                fromCamera();
                break;
            //取消popupwindow
            case R.id.cancel:
                popup.dismiss();
                break;
        }
    }

    public void saveUserInfo() {
        //发送修改个人信息请求
        String name = nickName.getText().toString();
        String tel = number.getText().toString();
        String signature = signatureET.getText().toString();
        String image_url = MyApplication.user.getImageUrl();
        RequestResult.updateUserInfo(this, name, tel, signature, image_url);
    }

    //---------------------------------选择图片的popupWindow---------------------------------------
    private void setPopup() {
        View root = getLayoutInflater().inflate(R.layout.select_image_layout, null);
        popup = new PopupWindow(root, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popup.setOutsideTouchable(false);
        popup.setAnimationStyle(R.style.Animations_GrowFromBottom);
        popup.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);

        fromAlbum = (TextView) root.findViewById(R.id.album);
        fromCamera = (TextView) root.findViewById(R.id.camera);
        cancel = (TextView) root.findViewById(R.id.cancel);

        fromCamera.setOnClickListener(this);
        fromAlbum.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    //从相册中选择
    private void fromAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, Constant.TAKE_PITURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.PITRUE_CODE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fromAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @TargetApi(19)
    private void handleImageKitat(Intent data) {
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            this.imagePath = imagePath;
            image.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    private void fromCamera() {
        File outputImage = new File(getExternalCacheDir(), "image.png");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(this, "weifen.com.eclife.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }

        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, Constant.TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        image.setImageBitmap(bitmap);
                        this.imagePath = imageUri.getPath();
                        popup.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Constant.TAKE_PITURE:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageKitat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
        }
    }
}
