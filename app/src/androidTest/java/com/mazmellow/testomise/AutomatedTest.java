package com.mazmellow.testomise;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mazmellow.testomise.util.ConnectionUtil;
import com.mazmellow.testomise.util.RootUtil;
import com.mazmellow.testomise.view.activity.MainActivity;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AutomatedTest {

    private Context context;
    private ArrayList datas;
    private boolean isCheckDeviceRooted, isCheckEachState;
    private boolean hasData;
    private int index;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {
        context = InstrumentationRegistry.getTargetContext();
        datas = new ArrayList();
        isCheckDeviceRooted = false;
        isCheckEachState = false;
        hasData = false;
    }

    public void getDatas(){
        datas = mainActivityRule.getActivity().getFragment().getDatas();
    }

    private void deviceRootedMustShowDialog() throws Exception {
        if (RootUtil.isDeviceRooted()) {
            onView(withText(context.getString(R.string.root_detected_title))).check(matches(isDisplayed()));
            onView(withText(context.getString(R.string.ok))).perform(click());
        }

        isCheckDeviceRooted = true;
    }

    private void checkEachState() throws Exception {
        if(!isCheckDeviceRooted) deviceRootedMustShowDialog();

        if (!ConnectionUtil.hasInternet(context)) {
            //TODO: It hasn't internet connection then show error "No Internet" dialog.
            onView(withText(context.getString(R.string.please_connect_internet))).check(matches(isDisplayed()));
            hasData = false;

        }else{

            getDatas();

            if(datas.size()==0) {
                //TODO: If hasn't any data from server then show "No Result" label.
                onView(withId(R.id.txtNoResult)).check(matches(isDisplayed()));
                hasData = false;

            }else{
                //TODO: If has data then show recyclerView.
                onView(withId(R.id.rvFeed)).check(matches(isDisplayed()));
                hasData = true;
            }
        }

        isCheckEachState = true;
    }

    @Test
    public void donate2000_withBeforeExpire_isSuccess() throws Exception {
        if(!isCheckEachState) checkEachState();
        if(!hasData) return;

        //Mock datas
        String userName = "DONATE_2000 BEFORE_EXP";
        String amount = 2000+"";
        String creditCardNumber = "5574453727851301";  // http://www.getcreditcardnumbers.com/
        String cvv = "123";
        final int waitTime = 5*1000;

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        String expMonth = "";
        String expYear = "";

        //Set expired month after current month. If month is December then set expired year to next year instead.
        if(month==11){
            year += 1;
        }else{
            month += 1;
        }
        expMonth = month + "";
        expYear = year + "";
        if(expMonth.length()==1) expMonth = "0"+expMonth;

        index = 0;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                onView(withId(R.id.btnSubmit)).check(matches(isEnabled()));
                onView(withId(R.id.btnSubmit)).perform(click());

                //Waiting for end of calling Api.
                try {
                    Thread.sleep(waitTime);

                    //Must show success
                    checkSuccess();

                }catch (InterruptedException e){}

            }
        };

        willDonate(userName, amount, creditCardNumber, expMonth, expYear, cvv, runnable, index);
    }

    @Test
    public void donate2000_withAfterExpire_isFailed() throws Exception {
        if(!isCheckEachState) checkEachState();
        if(!hasData) return;

        //Mock datas
        String userName = "DONATE_2000 AFTER_EXP";
        String amount = 2000+"";
        String creditCardNumber = "5574453727851301";  // http://www.getcreditcardnumbers.com/
        String cvv = "123";
        final int waitTime = 5*1000;

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        String expMonth = "";
        String expYear = "";

        //Set expired month before current month. If month is January then set expired year to previous year instead.
        if(month==0){
            year -= 1;
        }else{
            month -= 1;
        }
        expMonth = month + "";
        expYear = year + "";
        if(expMonth.length()==1) expMonth = "0"+expMonth;

        int index = 0;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                onView(withId(R.id.btnSubmit)).check(matches(isEnabled()));
                onView(withId(R.id.btnSubmit)).perform(click());

                //Waiting for end of calling Api.
                try {
                    Thread.sleep(waitTime);

                    //Must show failed
                    checkFailed();

                }catch (InterruptedException e){}

            }
        };

        willDonate(userName, amount, creditCardNumber, expMonth, expYear, cvv, runnable, index);
    }

    @Test
    public void donateLessThan2000_isFailed() throws Exception {
        if(!isCheckEachState) checkEachState();
        if(!hasData) return;

        //Mock datas
        String userName = "DONATE_1000";
        String amount = 1000+"";
        String creditCardNumber = "5574453727851301";  // http://www.getcreditcardnumbers.com/
        String cvv = "123";
        final int waitTime = 5*1000;

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        String expMonth = "";
        String expYear = "";

        //Set expired month after current month. If month is December then set expired year to next year instead.
        if(month==11){
            year += 1;
        }else{
            month += 1;
        }
        expMonth = month + "";
        expYear = year + "";
        if(expMonth.length()==1) expMonth = "0"+expMonth;

        int index = 0;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                onView(withId(R.id.btnSubmit)).check(matches(isEnabled()));
                onView(withId(R.id.btnSubmit)).perform(click());

                //Waiting for end of calling Api.
                try {
                    Thread.sleep(waitTime);

                    //Must show failed
                    checkFailed();

                }catch (InterruptedException e){}

            }
        };

        willDonate(userName, amount, creditCardNumber, expMonth, expYear, cvv, runnable, index);
    }

    @Test
    public void donateWithWrongCreditCardNumber_isFailed() throws Exception {
        if(!isCheckEachState) checkEachState();
        if(!hasData) return;

        //Mock datas
        String userName = "WRONG CREDIT";
        String amount = 2000+"";
        String creditCardNumber = "1234567890123456";  // http://www.getcreditcardnumbers.com/
        String cvv = "123";
        final int waitTime = 5*1000;

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        String expMonth = "";
        String expYear = "";

        //Set expired month after current month. If month is December then set expired year to next year instead.
        if(month==11){
            year += 1;
        }else{
            month += 1;
        }
        expMonth = month + "";
        expYear = year + "";
        if(expMonth.length()==1) expMonth = "0"+expMonth;

        int index = 0;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                onView(withId(R.id.btnSubmit)).check(matches(isEnabled()));
                onView(withId(R.id.btnSubmit)).perform(click());

                //Waiting for end of calling Api.
                try {
                    Thread.sleep(waitTime);

                    //Must show failed
                    checkFailed();

                }catch (InterruptedException e){}

            }
        };

        willDonate(userName, amount, creditCardNumber, expMonth, expYear, cvv, runnable, index);
    }

    private void willDonate(String userName, String amount, String creditCardNumber, String expMonth, String expYear, String cvv, Runnable runnable, int index) {
        if(index == datas.size()) {
            mainActivityRule.getActivity().finishAffinity();
            return;
        }

        onView(withId(R.id.rvFeed)).perform(RecyclerViewActions.actionOnItemAtPosition(index, click()));
        onView(withId(R.id.txtCharityName)).check(matches(isDisplayed()));

        onView(withId(R.id.edtUsername)).perform(typeText(userName), closeSoftKeyboard());
        onView(withId(R.id.edtAmount)).perform(typeText(amount), closeSoftKeyboard());
        onView(withId(R.id.edtCardNumber)).perform(typeText(creditCardNumber), closeSoftKeyboard());
        onView(withId(R.id.spnExpMonth)).perform(click());
        Espresso.onData(CoreMatchers.allOf(CoreMatchers.is(CoreMatchers.instanceOf(String.class)), CoreMatchers.is(expMonth))).perform(click());
        onView(withId(R.id.spnExpYear)).perform(click());
        Espresso.onData(CoreMatchers.allOf(CoreMatchers.is(CoreMatchers.instanceOf(String.class)), CoreMatchers.is(expYear))).perform(click());
        onView(withId(R.id.edtCvv)).perform(typeText(cvv), closeSoftKeyboard());

        runnable.run();
    }

    private void checkSuccess() {
        onView(withText(context.getString(R.string.success))).check(matches(isDisplayed()));
        onView(withId(R.id.btnOk)).perform(click());  // back to list
    }

    private void checkFailed() {
        onView(withText(context.getString(R.string.error))).check(matches(isDisplayed()));
        onView(withText(context.getString(R.string.ok))).perform(click());  //close dialog
        pressBack();  // back to list
    }
}
