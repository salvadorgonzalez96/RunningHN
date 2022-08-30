package com.example.runninghn;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import com.airbnb.lottie.LottieAnimationView;

public class ActivityAnuncio3 extends AppCompatActivity {
    private GestureDetectorCompat mDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio3);
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        TextView next3 = findViewById(R.id.tvnext3);
        next3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity4();
            }
        });

        // Lottie Animation
        LottieAnimationView animationViewRemote = findViewById(R.id.abtnnext3);
        animationViewRemote.setAnimation(R.raw.boton);
        animationViewRemote.loop(false);
        animationViewRemote.playAnimation();

        // handler
        Handler handler = new Handler();

        animationViewRemote.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animationViewRemote.pauseAnimation();
                    }
                },1000);

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //que no siga el loop animationViewRemote.playAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
    }

    public void openActivity2() {
        Intent intent = new Intent(this, ActivityAnuncio2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void openActivity4() {
        Intent intent = new Intent(this, ActivityAnuncio4.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityx, float velocityY) {
            if(e1.getX()<e2.getX()){
                openActivity2();
            }else if(e1.getX()>e2.getX()){
                openActivity4();
            }
            return true;
        }
    }
}