package com.e.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.ClipData;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private ConstraintLayout constraintLayout;
    private ConstraintLayout holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        Button btn = findViewById(R.id.hold);

        imageView.setOnTouchListener(new MyTouchListener());

        constraintLayout = findViewById(R.id.holder);
        holder = findViewById(R.id.inner_holder);

        constraintLayout.setOnDragListener(new MyDragListener());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.getLayoutParams();
                params.width =dpToPixels(getResources().getDisplayMetrics(), 250);
                holder.setLayoutParams(params);
            }
        });

    }

    public  int dpToPixels(final DisplayMetrics display_metrics, final float dps) {
        final float scale = display_metrics.density;
        return (int) (dps * scale + 0.5f);
    }


    // This defines your touch listener
    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            } else {
                return false;
            }
        }
    }

    class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    ConstraintSet constraintSet = new ConstraintSet();
                    if (event.getX() >= ((int) (holder.getX() + holder.getWidth() - (imageView.getWidth() / 2)))) {
                        constraintSet.clone(holder);
                        constraintSet.clear(R.id.imageView, ConstraintSet.START);
                        constraintSet.connect(R.id.imageView, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
                        constraintSet.setMargin(R.id.imageView, ConstraintSet.TOP, (int) (event.getY() - imageView.getHeight() / 2 - holder.getY()));
                        constraintSet.setMargin(R.id.imageView, ConstraintSet.END, 0);
                        constraintSet.applyTo(holder);
                        imageView.setVisibility(View.VISIBLE);
                        imageView.bringToFront();
                    } else {
                        constraintSet.clone(holder);
                        constraintSet.clear(R.id.imageView, ConstraintSet.END);
                        constraintSet.connect(R.id.imageView, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
                        constraintSet.setMargin(R.id.imageView, ConstraintSet.TOP, (int) (event.getY() - imageView.getHeight() / 2 - holder.getY()));
                        constraintSet.setMargin(R.id.imageView, ConstraintSet.START, (int) (event.getX() - (imageView.getWidth() / 2) - holder.getX()));
                        constraintSet.applyTo(holder);
                        imageView.setVisibility(View.VISIBLE);
                        imageView.bringToFront();
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            return true;
        }
    }
}
