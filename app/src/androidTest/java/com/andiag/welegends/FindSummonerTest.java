package com.andiag.welegends;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.andiag.welegends.views.summoners.ActivitySummoners;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FindSummonerTest {

    @Rule
    public ActivityTestRule<ActivitySummoners> mActivityTestRule = new ActivityTestRule<>(ActivitySummoners.class);

    @Test
    public void findSummonerTestEditor() {
        onView(withId(R.id.editTextSummoner))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.editTextSummoner))
                .check(matches(isDisplayed()))
                .perform(replaceText("brrokken"), pressImeActionButton());

        onView(withId(R.id.drawer_layout))
                .check(matches(isDisplayed()));

        pressBack();

        onView(withId(R.id.buttonHistoric))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.recyclerSummoners))
                .check(matches(isDisplayed()))
                .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.drawer_layout))
                .check(matches(isDisplayed()));

    }

    @Test
    public void findSummonerTestButton() {
        onView(withId(R.id.editTextSummoner))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.editTextSummoner))
                .check(matches(isDisplayed()))
                .perform(replaceText("brrokken"), closeSoftKeyboard());

        onView(withId(R.id.buttonGo))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.drawer_layout))
                .check(matches(isDisplayed()));

        pressBack();

        onView(withId(R.id.buttonHistoric))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.recyclerSummoners))
                .check(matches(isDisplayed()))
                .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.drawer_layout))
                .check(matches(isDisplayed()));

    }

}
