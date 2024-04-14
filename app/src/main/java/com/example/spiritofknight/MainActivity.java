package com.example.spiritofknight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.transition.Visibility;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spiritofknight.CustomView.PopupWindowCreator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;


public class MainActivity extends AppCompatActivity {
    
    private static int ROW=5;

    private static int COLUMN=5;
    private ImageView horse;

    private List<RelativeLayout> horse_RN_collection;
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

    RelativeLayout mCurrentRelativeLayout;

    ImageView mCurrentImageView;

    int j =0;
    private static String TAG = "白金之星";
    
    private boolean isfinished = false;

    List<Character> init_list;
    
    int height_statusbar =0;

    private static final int DATA_LOADED = 1;

    private Handler handler = new Handler(Looper.getMainLooper(),new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case DATA_LOADED:
                    // 数据加载完成后的处理
                    processDataLoaded();
                    break;
            }
            return true;
        }
    });

    private TextView textView;

    private int count_move=0;

    private View.OnTouchListener grid_touchListener = null;

    private View.OnTouchListener grid_touchListener2 = null;

    private Button hint_button;

    private  ArrayList<Pair<Integer,Integer>> swap_coor =null;

    private Button verify_button = null;

    private static final char[][] goal = { // 目标状态
            {'0', '0', '0', '0', '0'},
            {'0', '0', '0', '0', '1'},
            {'0', '0', '*', '1', '1'},
            {'0', '1', '1', '1', '1'},
            {'1', '1', '1', '1', '1'}
    };

//    private static char[][] board = new char[ROW][COLUMN];

    private static char[][] board = { // 当前状态
            {'0', '0', '0', '0', '0'},
            {'0', '0', '0', '1', '0'},
            {'0', '0', '*', '1', '1'},
            {'0', '1', '1', '1', '1'},
            {'1', '1', '1', '1', '1'}
    };

    static Deque<Integer> stackX = new ArrayDeque<>();
    static Deque<Integer> stackY = new ArrayDeque<>();

    private static int ans;

    private static int blankX, blankY;

    private static final int[][] tryStep = { // 尝试方向
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2},
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1}
    };

    private Runnable mHintTask = null;

    private static View customView;
    private static WindowManager windowManager;

    private static PopupWindowCreator sPopupWindowCreator;

    private TextView hint_title;

    private boolean isHintMove = false;

    List<Integer> arrayX = null;
    List<Integer> arrayY = null;

    private static RelativeLayout blue_RelativeLayout;
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();
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
//        relativeLayout.setVisibility(View.GONE);
        ViewTreeObserver viewTreeObserver_rl = relativeLayout.getViewTreeObserver();

        viewTreeObserver_rl.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                relativeLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                relativeLayout.setVisibility(View.VISIBLE);
            }
        });

//
        mGridLayout = findViewById(R.id.gridLayout);
        mGridLayout.setVisibility(View.GONE);
        int size = Math.min(relativeLayout.getWidth(), relativeLayout.getHeight());
        Log.d("白金之星", "onResume: size：" + size);


        ViewTreeObserver viewTreeObserver_grid = mGridLayout.getViewTreeObserver();

        viewTreeObserver_grid.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
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
//                    ViewGroup.LayoutParams layoutParams_mian = mainRelativeLayout.getLayoutParams();
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
                    mGridLayout.setVisibility(View.VISIBLE);
//                     发送消息通知数据加载完成
//                    handler.sendEmptyMessage(DATA_LOADED);
                }
            }
        });

        horse = findViewById(R.id.white_horse_1_1);

        initView();
        initView1();
        initview2();
        if (grid_touchListener == null){
            grid_touchListener= new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //获取点击的坐标位置
                            previousX = event.getRawX();
                            previousY = event.getRawY();
                            Log.d(TAG, "onTouch: 在Gird点击事件中获取坐标位置");
                            coordination_start = getCoordinate(previousX,previousY);
                            //设置第二个ImageView的坐标位置
                            myDragImageView.setX(coordination_start[0]);
                            myDragImageView.setY(coordination_start[1]);
                            Log.d(TAG, "onTouch: 在Gird点击事件中获取棋盘坐标位置");
                            //获取第一个ImageView对象 开始
                            coordination_start_in_chess =getCoordinate_in_chess(previousX,previousY);
                            mCurrentRelativeLayout = horse_RN_collection.get(coordination_start_in_chess[1]*5+(coordination_start_in_chess[0]+1)-1);
                            // 获取子视图数量
                            int childCount = mCurrentRelativeLayout.getChildCount();

                            mCurrentImageView =null;

                            // 遍历子视图
                            Log.d(TAG, "onTouch: 遍历子视图");
                            for (int i = 1; i < childCount; i++) {
                                // 获取子视图
                                Log.d(TAG, "onTouch: 获取子视图");
                                View childView = mCurrentRelativeLayout.getChildAt(i);
                                // 检查子视图是否为 ImageView
                                if (childView instanceof ImageView) {
//                                imageView = (ImageView) childView;//引发空位生马bug

                                    if(childView.getVisibility()== View.VISIBLE){
                                        mCurrentImageView = (ImageView) childView;
                                        Log.d(TAG, "onTouch: 得到子视图");
                                        break;
                                    }
                                }
                            }
                            if(mCurrentImageView== null) return true;
//                        mCurrentImageView = imageView;
                            //获取第一个ImageView对象 结束

                            // 将第一个 ImageView 的图像资源设置给第二个 ImageView
                            Drawable drawable = mCurrentImageView.getDrawable();
                            myDragImageView.setImageDrawable(drawable);
                            // 将第一个 ImageView 的宽高参数设置给第二个ImageView
                            int newWidth = mCurrentImageView.getWidth();
                            int newHeight = mCurrentImageView.getHeight();
                            ViewGroup.LayoutParams layoutParams = myDragImageView.getLayoutParams();
                            layoutParams.width = newWidth;
                            layoutParams.height = newHeight;
                            myDragImageView.setLayoutParams(layoutParams);
                            // 设置第二个ImageView的tag
                            Log.d(TAG, "onTouch: 获取TAG"+mCurrentImageView.getTag().toString());
                            myDragImageView.setTag(mCurrentImageView.getTag());
                            // 设置第二个ImageView可见
                            if(myDragImageView.getVisibility()!=View.VISIBLE){
                                myDragImageView.setVisibility(View.VISIBLE);
                            }
                            // 设置第一个ImageView不可见
                            mCurrentImageView.setVisibility(View.GONE);
                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                // 在 down 动作中，将事件传递给第一个ImageView
                                myDragImageView.dispatchTouchEvent(event);
                            }

                            Log.d("TouchEvent", "Down over RelativeLayout");
                            // 处理拖拽开始的逻辑
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if(mCurrentImageView==null) return true;
                            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                                // 在 move 动作中，将事件传递给另一个 View
                                myDragImageView.dispatchTouchEvent(event);
                            }
                            Log.d("TouchEvent", "Moving over RelativeLayout");
                            // 处理拖拽移动的逻辑
                            break;
                        case MotionEvent.ACTION_UP:
                            if(mCurrentImageView==null) return true;
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                // 在 move 动作中，将事件传递给另一个 View
                                Log.d("原马松开", "onTouch: 传递");
                                myDragImageView.dispatchTouchEvent(event);
                            }
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
            };
        }
        mGridLayout.setOnTouchListener(grid_touchListener);

        myDragImageView = findViewById(R.id.my_imageview);

        myDragImageView.setOnTouchListener(new View.OnTouchListener() {//拖动之物
            int i=0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "onTouch: 在拖动之物的onTouch事件中");
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
                        int[] final_Coordination = getCoordinate_in_chess(previousX,previousY);
                        float[] coor = getCoordinate(previousX,previousY);
                        ImageView imageView= getCurrentImageView(final_Coordination);
                        if(!isDownAppropriate(final_Coordination)||imageView!=null){
                            Log.d(TAG, "onTouch: 落脚点不合适或者位置不为空，回复原位");
                            //落脚点不合适
                            //或者落脚点不为空

//                            imageView.setVisibility(View.VISIBLE);
//                            mCurrentImageView = imageView;
                            mCurrentImageView.setVisibility(View.VISIBLE);
                            myDragImageView.setVisibility(View.GONE);
                            return true;
                        }
                        count_move++;
                        textView.setText("移动了"+count_move+"次数");
                        Log.d(TAG, "onTouch: 落脚点合适，落脚点图片显示");
                        RelativeLayout tempRL = horse_RN_collection.get(final_Coordination[1]*5+(final_Coordination[0]+1)-1);
                        // 获取子视图数量
                        int childCount = tempRL.getChildCount();

                        ImageView targetImageView = null;
                        // 遍历子视图
                        Log.d(TAG, "onTouch: 遍历子视图");
                        for (int i = 1; i < childCount; i++) {
                            // 获取子视图
                            Log.d(TAG, "onTouch: 获取子视图");
                            View childView = tempRL.getChildAt(i);
                            // 检查子视图是否为 ImageView
                            if (childView instanceof ImageView) {
                                targetImageView = (ImageView) childView;
                                if(targetImageView.getTag().equals(myDragImageView.getTag())){
                                    Log.d(TAG, "onTouch: 匹配");
                                    break;
                                }
                            }
                        }
                        targetImageView.setVisibility(View.VISIBLE);
                        if(isHintMove==true&&(arrayX!=null&&arrayX.size()!=0&&arrayY!=null&&arrayY.size()!=0)&&coordination_start_in_chess[0]==arrayX.get(0)&&coordination_start_in_chess[1]==arrayY.get(0)){
                            arrayX.remove(0);
                            arrayY.remove(0);
                            StringBuilder sb = new StringBuilder();
                            for(int i=0;i<arrayX.size();i++){
                                int x = arrayX.get(i);
                                int y = arrayY.get(i);
                                sb.append("(").append(x).append(", ").append(y).append(") ");
                            }
                            //蓝色标记
                            //清除上一个蓝色标记
                            blue_RelativeLayout.getChildAt(0).setAlpha(1f);
                            blue_RelativeLayout.setBackgroundColor(Color.TRANSPARENT);
                            //添加下一个蓝色标记
                            if(arrayX.size()!=0&&arrayY.size()!=0){
                                blue_RelativeLayout = horse_RN_collection.get(arrayY.get(0)* ROW+arrayX.get(0));
                                blue_RelativeLayout.getChildAt(0).setAlpha(0.5f);
                                blue_RelativeLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.blue));
                                hint_title.setText(sb.toString());
                            }else{
                                hint_title.setText("提示结束，请继续");
                            }


                        }else if(isHintMove==true&&(arrayX!=null&&arrayX.size()!=0&&arrayY!=null&&arrayY.size()!=0)&&(coordination_start_in_chess[0]!=arrayX.get(0)||coordination_start_in_chess[1]!=arrayY.get(0))){
                            StringBuilder sb = new StringBuilder("未按照提示移动：提示结束");
                            System.out.println("final_Coordination[0] == "+final_Coordination[0]);
                            System.out.println("final_Coordination[1] == "+final_Coordination[1]);
                            isHintMove =  false;
                            hint_title.setText(sb.toString());
                            //蓝色标记
                            //放弃提示，清除标记
                            blue_RelativeLayout = horse_RN_collection.get(arrayY.get(0)* ROW+arrayX.get(0));
                            blue_RelativeLayout.getChildAt(0).setAlpha(1f);
                            blue_RelativeLayout.setBackgroundColor(Color.TRANSPARENT);
                        }
                        myDragImageView.setVisibility(View.GONE);
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
//        loadData();
//        ShowHorse(createInitialMap());
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init_list=createInitialMap();
                //重置
                resetHorse(init_list);
            }
        });
    }//onResume Center



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 将需要保存的数据放入Bundle中
        // 将 List 转换为 ArrayList 并放入 Bundle 中
        // 将 Character 列表转换为 String
        StringBuilder stringBuilder = new StringBuilder();
        for (Character character : init_list) {
            stringBuilder.append(character);
        }
        String characterString = stringBuilder.toString();
        outState.putString("characterList", characterString);

        Log.d(TAG, "onSaveInstanceState: 退出");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // 从Bundle中恢复数据
        // 从 Bundle 中取出 String
        String characterString = savedInstanceState.getString("characterList");
        // 将 String 转换为 Character 列表
//        List<Character> characterList = new ArrayList<>();
        for (int i = 0; i < characterString.length(); i++) {
            init_list.set(i,characterString.charAt(i));
        }
        resetHorse(init_list);
        Log.d(TAG, "onSaveInstanceState: 回来");
    }


    private void resetHorse(List<Character> list){//一键换新
        int i=0;
        for (Character cell : list) {
            Log.d(TAG, "ShowHorse: cele i:"+i);
            if(cell=='0'){
                horse_RN_collection.get(i).getChildAt(1).setVisibility(View.VISIBLE);
                horse_RN_collection.get(i).getChildAt(2).setVisibility(View.INVISIBLE);
            } else if (cell =='1') {
                horse_RN_collection.get(i).getChildAt(1).setVisibility(View.INVISIBLE);
                horse_RN_collection.get(i).getChildAt(2).setVisibility(View.VISIBLE);
            }else{
                horse_RN_collection.get(i).getChildAt(1).setVisibility(View.INVISIBLE);
                horse_RN_collection.get(i).getChildAt(2).setVisibility(View.INVISIBLE);
            }
            i++;
        }
        count_move = 0;
        textView.setText("移动了"+count_move+"次数");
    }

    private void loadData() {
        // 在后台线程加载数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                init_list=createInitialMap();
                // 在需要发送消息的地方，创建一个 Message 对象并发送：
                Message message = handler.obtainMessage(DATA_LOADED);
                handler.sendMessage(message);
            }
        }).start();
    }

    private void processDataLoaded() {
        ShowHorse(init_list);
        // 在这里处理数据加载完成后的操作
    }


    private void ShowHorse(List<Character> list){
        int i=0;
        for (Character cell : list) {
            Log.d(TAG, "ShowHorse: cele i:"+i);
                if(cell=='0'){
                    horse_RN_collection.get(i).getChildAt(1).setVisibility(View.VISIBLE);
                } else if (cell =='1') {
                    horse_RN_collection.get(i).getChildAt(2).setVisibility(View.VISIBLE);
                }
                i++;
        }
        System.out.println();
    }

    private List<Character> createInitialMap(){
        // 生成包含12个'1'和12个'0'的列表
        Character[] elements = new Character[25];
        Arrays.fill(elements, 0, 12, '1');
        Arrays.fill(elements, 12, 24, '0');
        elements[24] = '*';
        // 将列表中的元素顺序打乱
        List<Character> list = Arrays.asList(elements);
        Collections.shuffle(list);
        System.out.println("列表长度是"+list.size());
        int i = 0;
        for (Character cell : list) {
            if (cell != null) {
                Log.d(TAG, "列表第"+i+"个元素: "+cell.charValue());
            } else {
                Log.d(TAG, "列表第"+i+"个元素: "+"Null character");
            }
            i++;
        }

        return list;
    }

    private ImageView getCurrentImageView(int[] final_Coordination){

        RelativeLayout tempRL = horse_RN_collection.get(final_Coordination[1]*5+(final_Coordination[0]+1)-1);
        // 获取子视图数量
        int childCount = tempRL.getChildCount();

        ImageView imageView = null;
        // 遍历子视图
        Log.d(TAG, "onTouch: 遍历子视图");
        for (int i = 1; i < childCount; i++) {
            // 获取子视图
            Log.d(TAG, "onTouch: 获取子视图");
            View childView = tempRL.getChildAt(i);
            // 检查子视图是否为 ImageView
            if (childView instanceof ImageView) {
                if(childView.getVisibility()== View.VISIBLE){
                    Log.d(TAG, "onTouch: 得到子视图");
                    imageView = (ImageView) childView;
                    break;
                }
            }
        }
        return imageView;
        //没有找到就返回空对象
    }

    private Boolean isDownAppropriate(int[] final_Coordination){
        for(int[] move:knightMoves){
            if(coordination_start_in_chess[0]+move[0] == final_Coordination[0]&&
               coordination_start_in_chess[1]+move[1] == final_Coordination[1]){

                Log.d(TAG, "isDownAppropriate: 下的位置正确");
                Log.d(TAG, "isDownAppropriate: 从坐标"+coordination_start_in_chess[0]+","+coordination_start_in_chess[1]);
                Log.d(TAG, "isDownAppropriate: 移动步长"+move[0]+","+move[1]);
                Log.d(TAG, "isDownAppropriate: 移动到"+final_Coordination[0]+","+final_Coordination[1]);
                return true;
            }
        }
        Log.d(TAG, "isDownAppropriate: 下的位置错误");
        return false;
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


    private void initView() {
        horse_RN_collection = new LinkedList<>();
        horse_RN_collection.add(findViewById(R.id.rn_1_1));
        horse_RN_collection.add(findViewById(R.id.rn_1_2));
        horse_RN_collection.add(findViewById(R.id.rn_1_3));
        horse_RN_collection.add(findViewById(R.id.rn_1_4));
        horse_RN_collection.add(findViewById(R.id.rn_1_5));
        horse_RN_collection.add(findViewById(R.id.rn_2_1));
        horse_RN_collection.add(findViewById(R.id.rn_2_2));
        horse_RN_collection.add(findViewById(R.id.rn_2_3));
        horse_RN_collection.add(findViewById(R.id.rn_2_4));
        horse_RN_collection.add(findViewById(R.id.rn_2_5));
        horse_RN_collection.add(findViewById(R.id.rn_3_1));
        horse_RN_collection.add(findViewById(R.id.rn_3_2));
        horse_RN_collection.add(findViewById(R.id.rn_3_3));
        horse_RN_collection.add(findViewById(R.id.rn_3_4));
        horse_RN_collection.add(findViewById(R.id.rn_3_5));
        horse_RN_collection.add(findViewById(R.id.rn_4_1));
        horse_RN_collection.add(findViewById(R.id.rn_4_2));
        horse_RN_collection.add(findViewById(R.id.rn_4_3));
        horse_RN_collection.add(findViewById(R.id.rn_4_4));
        horse_RN_collection.add(findViewById(R.id.rn_4_5));
        horse_RN_collection.add(findViewById(R.id.rn_5_1));
        horse_RN_collection.add(findViewById(R.id.rn_5_2));
        horse_RN_collection.add(findViewById(R.id.rn_5_3));
        horse_RN_collection.add(findViewById(R.id.rn_5_4));
        horse_RN_collection.add(findViewById(R.id.rn_5_5));
        textView = findViewById(R.id.tv_helloworld);
//        count_move = 0;
        textView.setText("移动了"+count_move+"次数");
    }

    private void initView1() {
        hint_button = findViewById(R.id.hint_button);
        hint_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGridLayout.setOnTouchListener(null);
                if(grid_touchListener2==null){
                    grid_touchListener2 = new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    //获取点击的坐标位置
                                    previousX = event.getRawX();
                                    previousY = event.getRawY();
                                    Log.d(TAG, "onTouch: 在Gird点击事件中获取坐标位置");
                                    coordination_start = getCoordinate(previousX,previousY);
                                    //设置第二个ImageView的坐标位置
                                    if(swap_coor==null){
                                        swap_coor = new ArrayList<>();
                                    }

                                    Log.d(TAG, "onTouch: 在Gird点击事件中获取棋盘坐标位置");
                                    //获取第一个ImageView对象 开始
                                    coordination_start_in_chess =getCoordinate_in_chess(previousX,previousY);
                                    Pair temppair = Pair.create(coordination_start_in_chess[0],coordination_start_in_chess[1]);
                                    System.out.print("swap：点击的坐标是("+temppair.first+","+temppair.second+")");

                                    if(containsPair(swap_coor, temppair)){
                                        System.out.print("swap：路径1 重复移除相同的 ");
                                        removePair(swap_coor,temppair);
                                        horse_RN_collection.get(coordination_start_in_chess[1]*5+(coordination_start_in_chess[0])).getChildAt(0).setAlpha(1f);
                                        horse_RN_collection.get(coordination_start_in_chess[1]*5+(coordination_start_in_chess[0])).setBackgroundColor(Color.TRANSPARENT);
                                    }
                                    else if(swap_coor.size()<=1&&!containsPair(swap_coor, temppair)){
                                        System.out.print("swap：路径2 添加一个不在的 ");
                                        swap_coor.add(temppair);
                                        mCurrentRelativeLayout = horse_RN_collection.get(coordination_start_in_chess[1]*5+(coordination_start_in_chess[0]));
                                        mCurrentRelativeLayout.getChildAt(0).setAlpha(0f);
                                        mCurrentRelativeLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.pink));
                                    }else if(swap_coor.size()>=2){
                                        System.out.print("swap：路径3 超了移除最后一个 ");
                                        swap_coor.remove(1);
                                        mCurrentRelativeLayout.getChildAt(0).setAlpha(1f);
                                        mCurrentRelativeLayout.setBackgroundColor(Color.TRANSPARENT);
                                        swap_coor.add(temppair);
                                        mCurrentRelativeLayout = horse_RN_collection.get(coordination_start_in_chess[1]*5+(coordination_start_in_chess[0]));
                                        mCurrentRelativeLayout.getChildAt(0).setAlpha(0f);
                                        mCurrentRelativeLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.pink));
                                    }
                                    System.out.print("swap：大小是"+swap_coor.size()+" ");
                                    printPairList(swap_coor);

                                    // 获取子视图数量
                                    int childCount = mCurrentRelativeLayout.getChildCount();

                                    mCurrentImageView =null;


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
                    };
                }
                mGridLayout.setOnTouchListener(grid_touchListener2);
                if (verify_button.getVisibility()==View.INVISIBLE){
                    verify_button.setVisibility(View.VISIBLE);
                }
            }
        });
        verify_button = findViewById(R.id.makesure_button);
        verify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swap_coor==null||swap_coor.size()!=2) return;
                for (int y = 0; y < 5; y++) {
                    for (int x = 0; x < 5; x++) {
                        boolean isFirstVisible = horse_RN_collection.get(y * 5 + x).getChildAt(1).getVisibility() == View.VISIBLE;
                        boolean isSecondVisible = horse_RN_collection.get(y * 5 + x).getChildAt(2).getVisibility() == View.VISIBLE;
                        if (!isFirstVisible && !isSecondVisible) {
                            board[x][y] = '*';
                        } else {
                            board[x][y] = (isFirstVisible ? '0' : '1');
                        }
                    }
                }

                // 打印棋盘（当前状态）
                System.out.println("起始状态");
                printBoard(board);
                for (int y = 0; y < 5; y++) {
                    for (int x = 0; x < 5; x++) {
                        goal[x][y] = board[x][y];
                    }
                }
                Pair<Integer, Integer> pair1 = swap_coor.get(0);
                Pair<Integer, Integer> pair2 = swap_coor.get(1);
                swapCells(goal, pair1.first, pair1.second, pair2.first, pair2.second);
                System.out.println("终点状态");
                // 打印棋盘（目标状态）
                printBoard(goal);

                // 创建线程对象，并传入 Lambda 表达式作为任务
                if(mHintTask==null){
                    mHintTask = new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < ROW; i++) {
                                for (int j = 0; j < COLUMN; j++) {
                                    if (board[j][i] == '*') {
                                        blankX = j;
                                        blankY = i;
                                    }
                                }
                            }

                            ans = 0;
                            for (int i = 0; i <= 15; i++) {
                                idaDfs(blankX, blankY, i, 0);
                                if (ans != 0) {
                                    System.out.println(i);
                                    StringBuilder sb = new StringBuilder();
                                    if(arrayX==null){
                                        arrayX = new ArrayList<>();
                                    }
                                    if(arrayY==null){
                                        arrayY = new ArrayList<>();
                                    }
                                    int index = 0;
                                    while (!stackX.isEmpty() && !stackY.isEmpty()) {
                                        int x = stackX.pollLast();
                                        int y = stackY.pollLast();
                                        arrayX.add(x);
                                        arrayY.add(y);
                                        index++;
                                        sb.append("(").append(x).append(", ").append(y).append(") ");
                                        System.out.println("(" + x + ", " + y + ")");
                                    }
                                    isHintMove = true;
                                    System.out.println("找到了结果");
                                    printBoard(board);
                                    runOnUiThread(()->{
                                        restoreAllBG();
                                        swap_coor.clear();
                                        verify_button.setClickable(true);
                                        verify_button.setVisibility(View.INVISIBLE);
                                        mGridLayout.setOnTouchListener(grid_touchListener);
                                        sPopupWindowCreator.closeWindow();
                                        hint_title.setText(sb.toString());
                                        //蓝色标记1
                                        Log.d(TAG, "run: 尼玛的 外部");
                                        if(!arrayX.isEmpty() && !arrayX.isEmpty()){
                                            Log.d(TAG, "run: 尼玛的 内部");
                                            blue_RelativeLayout = horse_RN_collection.get(arrayY.get(0)* ROW+arrayX.get(0));
                                            blue_RelativeLayout.getChildAt(0).setAlpha(0.5f);
                                            blue_RelativeLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.blue));
                                        }
                                    });
                                    break;
                                }
                            }
                            if (ans == 0) {
                                runOnUiThread(()->{
                                    verify_button.setClickable(true);
                                    hint_title.setText("15步之内无法做到");
                                    isHintMove = false;
                                });
                                System.out.println("没有找到结果");
                                printBoard(board);
                            }
                        }
                    };
                }
                Thread thread = new Thread(mHintTask);
                // 启动线程
                thread.start();
                verify_button.setClickable(false);
                if (sPopupWindowCreator==null){
                    sPopupWindowCreator = new PopupWindowCreator();
                }
                sPopupWindowCreator.createaPopupWindow(MainActivity.this,"搜索中...",MainActivity.this);
            }
        });

    }

    private static void printBoard(char[][] board) {
        Log.d(TAG, "printBoard: 打印棋盘");
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                System.out.print(board[j][i] + " ");
            }
            System.out.println();
        }
    }

    public static <T> boolean containsPair(List<Pair<T, T>> list, Pair<T, T> target) {
        for (Pair<T, T> pair : list) {
            if (Objects.equals(pair.first, target.first) && Objects.equals(pair.second, target.second)) {
                return true;
            }
        }
        return false;
    }

    public static <T> void removePair(List<Pair<T, T>> list, Pair<T, T> target) {
        list.removeIf(pair -> pair.first.equals(target.first) && pair.second.equals(target.second));
    }

    public static void swapCells(char[][] cells, int x1, int y1, int x2, int y2) {
        System.out.println("swap: cell1 (" + x1 + ", " + y1 + ") cell2 (" + x2 + ", " + y2 + ")");
        char temp = cells[x1][y1];
        cells[x1][y1] = cells[x2][y2];
        cells[x2][y2] = temp;
    }

    // 估价函数
    private static int evaluate() {
        int cnt = 0;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                if (board[j][i] != '*' && goal[j][i] != board[j][i]) {
                    cnt++;
                }
            }
        }
        return cnt;
    }


    // 启发式搜索
    private static void idaDfs(int x, int y, int maxStep, int step) {
        if (step == maxStep) {
            if (evaluate() == 0) {
                System.out.println("找到");
                printBoard(board);
                ans = 1;
            }
            return;
        }
        for (int[] dir : tryStep) {
            int tX = x + dir[0];
            int tY = y + dir[1];
            if (!isSafe(tX, tY)) {
                continue;
            }
            swap(x, y, tX, tY);
            stackX.push(tX);
            stackY.push(tY);
//            System.out.println("Step: " + (step + 1) + ", Evaluate: " + evaluate() + ", MaxStep: " + maxStep);
            if (step + 1 + evaluate() <= maxStep) {
//                System.out.println("深入");
                idaDfs(tX, tY, maxStep, step + 1);
            }
//            System.out.println("退出");
            if(ans!=1){
                swap(tX, tY, x, y);
                // 弹栈操作
                stackX.pop();
                stackY.pop();
            }else{
                return;//战争结束了
            }
        }
    }

    // 检查是否越界
    private static boolean isSafe(int x, int y) {
        return x >= 0 && x < ROW && y >= 0 && y < COLUMN;
    }

    // 交换棋盘上两个位置的值
    private static void swap(int x1, int y1, int x2, int y2) {
        char temp = board[x1][y1];
        board[x1][y1] = board[x2][y2];
        board[x2][y2] = temp;
    }

    private   void restoreAllBG(){
        for(Pair pair: swap_coor){
            System.out.println("第一个值：" + pair.second);
            System.out.println("第二个值：" + pair.first);
            System.out.println("两个值的和：" + ((Integer)pair.second*5+(Integer)pair.first));
            RelativeLayout tempRn = horse_RN_collection.get((Integer)pair.second*5+(Integer)pair.first);
            tempRn.getChildAt(0).setAlpha(1f);
            tempRn.setBackgroundColor(Color.TRANSPARENT);
        }
    }


    // 显示 Toast 方法
// 打开自定义窗口方法
    public static void openWindow(Context context) {
        if (customView != null) {
            return; // 如果窗口已经打开，直接返回
        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        LayoutInflater inflater = LayoutInflater.from(context);
        customView = inflater.inflate(R.layout.custom_layout, null);

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.addView(customView, params);
        }
    }

    // 关闭自定义窗口方法
    public static void closeWindow(Context context) {
        if (customView != null && windowManager != null) {
            windowManager.removeView(customView);
            customView = null;
            windowManager = null;
        }
    }

    private void initview2(){
        hint_title = findViewById(R.id.title);
    }

    public static void printPairList(List<Pair<Integer, Integer>> pairList) {
        System.out.print("swap: 打印所有值 ");
        for (Pair<Integer, Integer> pair : pairList) {
            System.out.print("(" + pair.first + ", " + pair.second + ")");
        }
        System.out.println("");
    }



}
