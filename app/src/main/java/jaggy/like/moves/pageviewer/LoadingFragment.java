package jaggy.like.moves.pageviewer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class LoadingFragment extends DialogFragment {

    ImageView spinner;
    RotateAnimation rotateAnimation;
    Animation anim;

    public LoadingFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Initialize a RotateAnimation object */
        rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(500);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /* Inflate a root view */
        View view = inflater.inflate(R.layout.fragment_loading, container, false);

        /* Start rotate animation on ImageView */
        spinner = (ImageView)view.findViewById(R.id.loading_spinner);
        spinner.startAnimation(rotateAnimation);

        return view;
    }
    
    @Override
    public void onDismiss(DialogInterface dialog) {
        /* Stop the animation when the dialog dismissed */
        spinner.clearAnimation();
    }
}
