package com.example.ahmedmakaty.base.presentation.screens.MainScreen;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedmakaty.base.R;
import com.example.ahmedmakaty.base.data.model.DateResponse;
import com.example.ahmedmakaty.base.data.remote.ApiServiceInterface;
import com.example.ahmedmakaty.base.presentation.BaseFragment;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class MainFragment extends BaseFragment {

    private static final int ROTATE_ANIMATION_DURATION = 1000;

    @BindView(R.id.object)
    TextView object;
    @BindView(R.id.target)
    View target;

    Toolbar toolbar;
    @Inject
    MainViewModelFactory mainViewModelFactory;
    MainViewModel mainViewModel;
    float mLastTouchX, mLastTouchY;
    int targetHeight;
    int originalHeight;
    boolean collapsed = false;
    boolean channelling = false;
    @Inject
    ApiServiceInterface apiServiceInterface;
    Disposable disposable;
    private int mActivePointerId = INVALID_POINTER_ID;

    public static MainFragment newInstance() {

        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainViewModel = ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel.class);

        initiateLiveObservers();

        rotateObjectForever();

        defineObjectTouchListener();

        targetHeight = target.getHeight() * 3;
        originalHeight = target.getHeight();

        //Sitting the api call to be every 10 milliseconds (I tried 1 millisecond but the performance was not stable)
        disposable = Observable.fromCallable(() -> 1000)
                .repeatWhen(o -> o.concatMap(v -> Observable.timer(10, TimeUnit.MILLISECONDS)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getDateTime, this::onError);

    }

    //Handle square dragging, retrieving position of touch and saving it to calculate the distance the square should be moved
    //checks if the square goes into the drawer by 25% and dropping it inside
    @SuppressLint("ClickableViewAccessibility")
    private void defineObjectTouchListener() {

        object.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                if (originalHeight == 0) {
                    originalHeight = target.getHeight();
                    targetHeight = originalHeight * 3;
                }

                final int action = MotionEventCompat.getActionMasked(ev);

                switch (action) {
                    case MotionEvent.ACTION_DOWN: {
                        prepareBin();

                        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                        final float x = ev.getRawX();
                        final float y = ev.getRawY();

                        // Remember where we started (for dragging)
                        mLastTouchX = x;
                        mLastTouchY = y;
                        // Save the ID of this pointer (for dragging)
                        mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                        break;
                    }

                    case MotionEvent.ACTION_MOVE: {

                        object.clearAnimation();


                        // Find the index of the active pointer and fetch its position
                        final int pointerIndex =
                                MotionEventCompat.findPointerIndex(ev, mActivePointerId);

                        final float x = ev.getRawX();
                        final float y = ev.getRawY();

                        // Calculate the distance moved
                        final float dx = x - mLastTouchX;
                        final float dy = y - mLastTouchY;

                        object.setX(object.getX() + dx);
                        object.setY(object.getY() + dy);
                        object.requestLayout();

                           // object.animate().x(object.getX() + dx).y(object.getY() + dy).setDuration(0).setInterpolator(new AccelerateDecelerateInterpolator());

                        object.animate().rotationBy(6).setDuration(0).setInterpolator(new LinearInterpolator());
                        Log.d("DETAILS", "X:" + object.getX() + " Y:" + object.getY() + " PivotX:" + object.getPivotX() + " PivotY:" + object.getPivotY() + " width:" + object.getWidth());

                        // Remember this touch position for the next move event
                        mLastTouchX = x;
                        mLastTouchY = y;

                        if (((object.getY() + (object.getHeight() * 0.75)) > target.getY()) && !collapsed) {
                            collapsed = true;
                            dropObject();
                        } else if (((object.getY() + (object.getHeight() * 0.75)) < target.getY())) {
                            collapsed = false;
                        }

                        break;
                    }

                    case MotionEvent.ACTION_UP: {
                        rotateObjectForever();

                        if (!collapsed) {
                            target.clearAnimation();
                            restoreNormalBin();
                        }
                        mActivePointerId = INVALID_POINTER_ID;
                        break;
                    }

                    case MotionEvent.ACTION_CANCEL: {
                        mActivePointerId = INVALID_POINTER_ID;
                        break;
                    }

                    case MotionEvent.ACTION_POINTER_UP: {

                        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);

                        if (pointerId == mActivePointerId) {
                            // This was our active pointer going up. Choose a new
                            // active pointer and adjust accordingly.
                            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                            mLastTouchX = MotionEventCompat.getX(ev, newPointerIndex);
                            mLastTouchY = MotionEventCompat.getY(ev, newPointerIndex);
                            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                        }
                        break;
                    }
                }

                return true;
            }
        });
    }

    //translation animation to move the object from it's current location to the center of the drawer
    private void dropObject() {
        channelling = true;
        object.animate().x((target.getX() + (target.getWidth() / 2)) - object.getWidth() / 2).y((target.getY() + (target.getHeight() / 2)) - object.getHeight() / 2).setDuration(100).setInterpolator(new LinearInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                channelling = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    //animating the drawer close
    private void restoreNormalBin() {
        slideDrawer(300, target.getHeight(), originalHeight);
    }

    //animating the drawer open
    private void prepareBin() {
        slideDrawer(300, target.getHeight(), targetHeight);
    }

    //the api call to get the time and handling result and error
    private void getDateTime(Integer integer) {
        Observable<DateResponse> observable = apiServiceInterface.getDateTime();
        observable.subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread())
                .map(result -> result.getDateTime())
                .subscribe(this::handleResults, this::handleError);
    }

    //parsing the result date and printing the time on the square
    private void handleResults(String s) {

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssss");
        SimpleDateFormat df3 = new SimpleDateFormat("HH:mm:ss");
        try {
            date = format.parse(s);
            Log.d("RESULT", df3.format(date));
            object.setText(df3.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void onError(Throwable throwable) {
        Toast.makeText(getContext(), "OnError in Observable Timer",
                Toast.LENGTH_LONG).show();
    }

    private void handleError(Throwable t) {

        //Add your error here.
    }

    //Not the perfect way for handling the infinite rotation, needed a work around when the user is dragging the object
    //I would have implemented a custom view which rotates all the time given it's x and y position on the screen and renewing the pivot and starting the animation from the last animated angle and so on
    private void rotateObjectForever() {
        object.animate().rotation(object.getRotation() + 360).setDuration(1000).setInterpolator(new LinearInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rotateObjectForever();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    //handles drawer animation
    public void slideDrawer(long duration, int height, int heightAfter) {
        ValueAnimator slideAnimator = ValueAnimator
                .ofInt(height, heightAfter)
                .setDuration(duration);


        // we want to manually handle how each tick is handled so add a
        // listener
        slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // get the value the interpolator is at
                Integer value = (Integer) animation.getAnimatedValue();
                // I'm going to set the layout's height 1:1 to the tick
                target.getLayoutParams().height = value.intValue();
                // force all layouts to see which ones are affected by
                // this layouts height change
                target.requestLayout();
            }
        });

        // create a new animationset
        AnimatorSet set = new AnimatorSet();
        // since this is the only animation we are going to run we just use
        // play
        set.play(slideAnimator);
        // this is how you set the parabola which controls acceleration
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        // start the animation
        set.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(disposable.isDisposed()){
            disposable = Observable.fromCallable(() -> 1000)
                    .repeatWhen(o -> o.concatMap(v -> Observable.timer(10, TimeUnit.MILLISECONDS)))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::getDateTime, this::onError);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        disposable.dispose();
    }

    private void initiateLiveObservers() {

    }

    private void onBackPressed() {
        getActivity().finish();
    }

}
