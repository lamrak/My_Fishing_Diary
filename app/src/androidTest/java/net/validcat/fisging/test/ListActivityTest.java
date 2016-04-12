package net.validcat.fisging.test;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import net.validcat.fishing.ListActivity;
import net.validcat.fishing.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static net.validcat.fisging.test.ImageViewHasDrawableMatcher.hasDrawable;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ListActivityTest {

    @Rule
    public ActivityTestRule<ListActivity> mActivityRule = new ActivityTestRule<>(ListActivity.class);

    @Test
    public void checkBasicFunctionality() {
        //for (int i = 0;i<3;i++){
        onView(withId(R.id.fab_add_fishing)).perform(click());
        onView(withId(R.id.et_place)).perform(typeText("Dnepr"), closeSoftKeyboard());
        onView(withId(R.id.et_bait)).perform(typeText("Bloodworm"), closeSoftKeyboard());
        onView(withId(R.id.action_add_new_fishing)).perform(click());
        onView(withId(R.id.photo_preview)).check(matches(hasDrawable()));
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        pressBack();
     //  }
    }
}
