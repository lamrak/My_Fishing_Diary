//package net.validcat.fisging.test;
//
//import android.support.test.espresso.contrib.RecyclerViewActions;
//import android.support.test.runner.AndroidJUnit4;
//import android.test.suitebuilder.annotation.LargeTest;
//
//import net.validcat.fishing.R;
//import net.validcat.fishing.tools.RandomStringGenerator;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.action.ViewActions.typeText;
//import static android.support.test.espresso.assertion.ViewAssertions.matches;
//import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static android.support.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.Matchers.endsWith;
//
//@RunWith(AndroidJUnit4.class)
//@LargeTest
//public class ThingsListTest {
//
//    private String fishingName;
//    private String itemName;
//
//
//    @Before
//    public void intiData() {
//        fishingName = RandomStringGenerator.nextString();
//        itemName = RandomStringGenerator.nextString();
//    }
//
//    @Test
//    public void checkBasicFunctionality() {
//        onView(withId(R.id.fab_add_fishing)).perform(click());
//        onView(withId(R.id.et_place)).perform(typeText(fishingName));
//        onView(withId(R.id.fab_add_thing)).perform(click());
//        onView(withClassName(endsWith("EditText"))).perform(typeText(itemName));
//        onView(withText(R.string.ok)).perform(click());
//        onView(withId(R.id.action_save_list)).perform(click());
//        onView(withId(R.id.action_add_new_fishing)).perform(click());
//        onView(withId(R.id.recycler_view)).perform(
//                RecyclerViewActions.actionOnItemAtPosition(0, click())
//        );
//        onView(withId(R.id.view_things_list_button)).perform(click());
//        onView(withId(R.id.things_list_recycler_view)).perform(
//                RecyclerViewActions.scrollToPosition(9)
//        );
//        onView(withRecyclerView(R.id.things_list_recycler_view)
//                .atPositionOnView(9, R.id.things_list_description_text_view))
//                .check(matches(withText(itemName)));
//    }
//
//    private RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
//        return new RecyclerViewMatcher(recyclerViewId);
//    }
//}
