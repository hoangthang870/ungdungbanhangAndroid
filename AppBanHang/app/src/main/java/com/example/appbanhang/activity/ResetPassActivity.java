package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ResetPassActivity extends AppCompatActivity {
    EditText email;
    AppCompatButton btnreset;
    ProgressBar progressBar;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_reset_pass );
        initView();
        initControl();
    }

    private void initControl() {
        btnreset.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = email.getText().toString().trim();
                if (TextUtils.isEmpty( str_email )){
                    Toast.makeText( getApplicationContext(),"Bạn chưa nhập email",Toast.LENGTH_LONG ).show();
                }else{
                    progressBar.setVisibility( View.VISIBLE );
                    compositeDisposable.add(apiBanHang.resetpass( str_email )
                    .subscribeOn( Schedulers.io() )
                    .observeOn( AndroidSchedulers.mainThread() )
                    .subscribe(
                            userModel -> {
                                if(userModel.isSuccess()){
                                    Toast.makeText( getApplicationContext(),userModel.getMessage(),Toast.LENGTH_SHORT ).show();
                                    Intent intent = new Intent(getApplicationContext(),DangNhapActivity.class);
                                    startActivity( intent );
                                    finish();
                                }else{
                                    Toast.makeText( getApplicationContext(),userModel.getMessage(),Toast.LENGTH_SHORT ).show();
                                }
                                progressBar.setVisibility( View.INVISIBLE );
                            },
                            throwable -> {
                                Toast.makeText( getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT ).show();
                                progressBar.setVisibility( View.INVISIBLE );
                            }

                    ));

                }
            }
        } );
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance( Utils.BASE_URL ).create( ApiBanHang.class );
        email = findViewById( R.id.edittxtresetpass );
        btnreset = findViewById( R.id.btnresetpass );
        progressBar = findViewById( R.id.progressbarresetpass );
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}