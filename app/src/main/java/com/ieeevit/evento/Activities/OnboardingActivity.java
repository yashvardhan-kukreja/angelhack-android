package com.ieeevit.evento.activities;

import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.view.View;

import com.ieeevit.evento.R;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;

public class OnboardingActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);
        hideBackButton();

        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });

        getNextButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorAccent)
                .buttonsColor(R.color.colorPrimaryDark)
                .image(R.drawable.onboarding_one)
                .title("Who we are?")
                .description("IEEE VIT Core Committee is\n" +
                        "one of the most active chapters\n" +
                        "inside Region 10 of IEEE International.\n" +
                        "We boast of highly skilled members in\n" +
                        "technical and non-technical disciplines.\n").build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorAccent)
                .buttonsColor(R.color.colorPrimaryDark)
                .image(R.drawable.onboarding_two)
                .title("What do we provide?")
                .description("Evento provides you a digital\n" +
                        "platform to host your events \n" +
                        "effortlessly. Just a few clicks \n" +
                        "and you can have an app that \n" +
                        "manages all the event related \n" +
                        "aspects.   ").build());
    }

    @Override
    public void onFinish() {
        super.onFinish();
        finishAfterTransition();
    }
}
