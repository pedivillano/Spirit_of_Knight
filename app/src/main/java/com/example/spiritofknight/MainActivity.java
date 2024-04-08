package com.example.spiritofknight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.transition.Visibility;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class MainActivity extends AppCompatActivity {
    private ImageView horse;
    private ImageView myDragImageView;
    private RelativeLayout mainRelativeLayout;
    private RelativeLayout relativeLayout;

    private GridLayout mGridLayout;
    private float previousX, previousY;

    private boolean is_drad_Moving = false;

    float screenWidth = 0;
    float screenHeight = 0;

    float left_rn =0;
    float top_rn=0;

    float right_rn =0;

    float bottom_rn = 0;
    int[] location = new int[2];
//真正的坐标之力
    int leftOnScreen = 0;
    int topOnScreen = 0;
    int rightOnScreen = 0;
    int bottomOnScreen = 0;

    float[] coordination_start;

    int[] coordination_start_in_chess;

    // 定义骑士马可以移动的相对坐标
    int[][] knightMoves = {
            {1, 2}, {2, 1},
            {1, -2}, {2, -1},
            {-1, 2}, {-2, 1},
            {-1, -2}, {-2, -1}
    };

    int j =0;
    private static String TAG = "白金之星";
    
    private boolean isfinished = false;
    
    int height_statusbar =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        WindowManager windowManager = (WindowManager) MainActivity.this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(metrics);
            screenWidth = metrics.widthPixels;
            screenHeight = metrics.heightPixels;
            System.out.println("Screen width: " + screenWidth + "px");
            System.out.println("Screen height: " + screenHeight + "px");
        }
        mainRelativeLayout = findViewById(R.id.main_activity);
        
        ViewTreeObserver viewTreeObserver_main = mainRelativeLayout.getViewTreeObserver();

        viewTreeObserver_main.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mainRelativeLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int[] location = new int[2];
                mainRelativeLayout.getLocationOnScreen(location);
                height_statusbar =location[1];
                Log.d(TAG, "onGlobalLayout: height_statusbar:"+height_statusbar);
            }
        });

        relativeLayout = findViewById(R.id.relativelayout);
        mGridLayout = findViewById(R.id.gridLayout);
        int size = Math.min(relativeLayout.getWidth(), relativeLayout.getHeight());
        Log.d("白金之星", "onResume: size：" + size);


        ViewTreeObserver viewTreeObserver = mGridLayout.getViewTreeObserver();

        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                j++;
                Log.d(TAG, "onGlobalLayout: 第"+j+"次调用");
                // 获取宽度和高度
                int width = relativeLayout.getWidth();
                int height = relativeLayout.getHeight();
                int[] location = new int[2];
                // 确保宽度和高度都不为0
                if (width != 0 && height != 0) {
                    // 设置宽度和高度相同
                    int size = Math.min(width, height);
                    ViewGroup.LayoutParams layoutParams = relativeLayout.getLayoutParams();
                    layoutParams.width = size;
                    layoutParams.height = size;
                    relativeLayout.setLayoutParams(layoutParams);
                    location = new int[2];
                    mGridLayout.getLocationOnScreen(location);

                    int leftOnScreen = location[0];
                    int topOnScreen = location[1];
                    int rightOnScreen = leftOnScreen + mGridLayout.getWidth();
                    int bottomOnScreen = topOnScreen + mGridLayout.getHeight();
                    Log.d("白金之星1", "Left on screen: " + leftOnScreen);
                    Log.d("白金之星1", "Top on screen: " + topOnScreen);
                    Log.d("白金之星1", "Right on screen: " + rightOnScreen);
                    Log.d("白金之星1", "Bottom on screen: " + bottomOnScreen);
                }
                if(width==height){
                    // 移除监听器，避免重复调用
                    Log.d(TAG, "onGlobalLayout: 移除，重复 start");
                    relativeLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    leftOnScreen = location[0];
                    topOnScreen = location[1];
                    rightOnScreen = leftOnScreen + mGridLayout.getWidth();
                    bottomOnScreen = topOnScreen + mGridLayout.getHeight();
                    Log.d("白金之星2", "Left on screen: " + leftOnScreen);
                    Log.d("白金之星2", "Top on screen: " + topOnScreen);
                    Log.d("白金之星2", "Right on screen: " + rightOnScreen);
                    Log.d("白金之星2", "Bottom on screen: " + bottomOnScreen);
                    Log.d(TAG, "onGlobalLayout: 移除，重复 end");
                }
            }
        });

        horse = findViewById(R.id.white_horse_1_1);
        myDragImageView = findViewById(R.id.my_imageview);
        horse.setOnTouchListener(new View.OnTouchListener() {

            private boolean isMoving = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        previousX = event.getRawX();
                        previousY = event.getRawY();
                        // 获取第一个 ImageView 的图像资源
                        if(myDragImageView.getVisibility()!=View.VISIBLE){
                            myDragImageView.setVisibility(View.VISIBLE);
                        }
                        coordination_start = getCoordinate(previousX,previousY);
                        myDragImageView.setX(coordination_start[0]);
                        myDragImageView.setY(coordination_start[1]);
                        coordination_start_in_chess =getCoordinate_in_chess(previousX,previousY);
                        Drawable drawable = horse.getDrawable();

                        // 将获取的图像资源设置给第二个 ImageView
                        myDragImageView.setImageDrawable(drawable);

                        int newWidth = horse.getWidth();
                        int newHeight = horse.getHeight();
                        ViewGroup.LayoutParams layoutParams = myDragImageView.getLayoutParams();
                        layoutParams.width = newWidth;
                        layoutParams.height = newHeight;
                        myDragImageView.setLayoutParams(layoutParams);

                        horse.setVisibility(View.GONE);
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            // 在 move 动作中，将事件传递给另一个 View
                            myDragImageView.dispatchTouchEvent(event);
                        }

                        isMoving = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (event.getAction() == MotionEvent.ACTION_MOVE) {
                            // 在 move 动作中，将事件传递给另一个 View
                            myDragImageView.dispatchTouchEvent(event);
                        }
                        Log.d("移动", "onTouch原ImageView: ");
                        if (isMoving) {
                            float dx = event.getRawX() - previousX;
                            float dy = event.getRawY() - previousY;

                            // 移动 horse
                            moveHorse(horse, dx, dy);

                            previousX = event.getRawX();
                            previousY = event.getRawY();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            // 在 move 动作中，将事件传递给另一个 View
                            Log.d("原马松开", "onTouch: 传递");
                            myDragImageView.dispatchTouchEvent(event);
                        }
                        isMoving = false;
                        break;
                }
                return true;
            }
        });

        myDragImageView.setOnTouchListener(new View.OnTouchListener() {//拖动之物
            int i=0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d("按下", "onTouch: ");
                        previousX = event.getRawX();
                        previousY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        i++;
                        Log.d("移动"+i, "onTouch: ");
                        float dx = event.getRawX() - previousX;
                        float dy = event.getRawY() - previousY;
                        float newX = myDragImageView.getX()+ dx;
                        float newY = myDragImageView.getY() + dy;
                        myDragImageView.setX(newX);
                        myDragImageView.setY(newY);
                        previousX = event.getRawX();
                        previousY = event.getRawY();
                        Log.d("DragView", "dx: " + dx + ", dy: " + dy + ", newX: " + newX + ", newY: " + newY);

                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d("松开", "onTouch: 拖动之物");
                        previousX = event.getRawX();
                        previousY = event.getRawY();
                        float[] coor = getCoordinate(previousX,previousY);
                        myDragImageView.setX(coor[0]);
                        myDragImageView.setY(coor[1]);


//                        if(myDragImageView.getVisibility()!=View.GONE){
//                            myDragImageView.setVisibility(View.GONE);
//                        }
//                        horse.setVisibility(View.VISIBLE);//已经看不见了
                        // 可以在这里执行拖动结束后的逻辑
//                        myDragImageView.setVisibility(View.GONE);
                        break;
                }
                return true;
            }
        });

        RelativeLayout relativeLayout1 = findViewById(R.id.rn_1_1);
        relativeLayout1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d("TouchEvent", "Down over RelativeLayout");
                        // 处理拖拽开始的逻辑
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.d("TouchEvent", "Moving over RelativeLayout");
                        // 处理拖拽移动的逻辑
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d("TouchEvent", "UP over RelativeLayout");
                        // 处理放下 ImageView 的逻辑
                        // 在这里触发事件
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        Log.d("TouchEvent", "UP2 over RelativeLayout");
                        //按钮弹起逻辑
                        break;
                }
                return true;
            }
        });
    }

        private void moveHorse (View horse,float dx, float dy){
            // 计算新的位置
            float newX = horse.getX() + dx;
            float newY = horse.getY() + dy;

            // 设置新的位置
            horse.setX(newX);
            horse.setY(newY);
        }

        private float[] getCoordinate(float x, float y){
            Log.d(TAG, "coordination is x=" + x +", " + "y="+y );
            Log.d(TAG, "topOnScreen: "+topOnScreen);
            Log.d(TAG, "screenWidth: "+screenWidth);
            int x_coor = (int)( x/(screenWidth/5));
            int y_coor = (int)((y- topOnScreen)/(screenWidth/5));
            if(y_coor<0) y_coor=0;else if(y_coor>4) y_coor=4;
            Log.d(TAG, "x_coor=" + x_coor +", " + "y_coor="+y_coor );
            float [] result = new float[2];
            result[0] = x_coor*(screenWidth/5);
            result[1] = y_coor*(screenWidth/5)+topOnScreen-height_statusbar;
            // 打印结果
            Log.d(TAG, "Result: (" + result[0] + ", " + result[1]+")");
            return result;
    }

    private int[] getCoordinate_in_chess(float x, float y){
        Log.d(TAG, "coordination is x=" + x +", " + "y="+y );
        Log.d(TAG, "topOnScreen: "+topOnScreen);
        Log.d(TAG, "screenWidth: "+screenWidth);
        int x_coor = (int)( x/(screenWidth/5));
        int y_coor = (int)((y- topOnScreen)/(screenWidth/5));
        if(y_coor<0) y_coor=0;else if(y_coor>4) y_coor=4;
        Log.d(TAG, "x_coor=" + x_coor +", " + "y_coor="+y_coor );
        int [] result = new int[2];
        result[0] = x_coor;
        result[1] = y_coor;
        // 打印结果
        Log.d(TAG, "Result: 棋盘坐标(" + result[0] + ", " + result[1]+")");
        return result;
    }


    public static void printScreenDimensions(Context context) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            if (windowManager != null) {
                windowManager.getDefaultDisplay().getMetrics(metrics);
                float screenWidth = metrics.widthPixels;
                float screenHeight = metrics.heightPixels;
                System.out.println("Screen width: " + screenWidth + "px");
                System.out.println("Screen height: " + screenHeight + "px");
            }
    }

}
