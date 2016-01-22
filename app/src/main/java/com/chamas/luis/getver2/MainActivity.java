package com.chamas.luis.getver2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {
    LogInFragment logInFragment = new LogInFragment();
    SignUpFragment signUpFragment = new SignUpFragment();
    MainFragment mainFragment = new MainFragment();
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

           if(position ==0){
               return signUpFragment;
           }else if(position == 1){
               return mainFragment;
           }else{
               return logInFragment;
           }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class MainFragment extends Fragment {
        public MainFragment(){

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            return inflater.inflate(R.layout.fragment_main, container, false);
        }
    }

    public static class SignUpFragment extends Fragment{
        private EditText Username, Email, password, ConfirmPass;
        private Button SignupButton;
        private String usernameStr, emailStr, passwordStr, confirmPassStr;

        public SignUpFragment (){
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootview = inflater.inflate(R.layout.fragment_sign_up, container, false);

            Username = (EditText)rootview.findViewById(R.id.SignUpUsernameEditText);
            Email = (EditText)rootview.findViewById(R.id.SignUpEmailEditText);
            password = (EditText)rootview.findViewById(R.id.SignUpPasswordEditText);
            ConfirmPass = (EditText)rootview.findViewById(R.id.SignUpConfirmPasswordEditText);
            SignupButton = (Button)rootview.findViewById(R.id.SignUpButton);


            SignupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishSignUp(v);
                }
            });


            return rootview;
        }

        public void finishSignUp(View v){
            usernameStr = Username.getText().toString();
            emailStr = Email.getText().toString();
            passwordStr = password.getText().toString();
            // confirmPassStr = ConfirmPass.getText().toString();

            ParseUser user = new ParseUser();
            user.setUsername(usernameStr);
            user.setPassword(passwordStr);
            user.setEmail(emailStr);

            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Signing up");
            progressDialog.show();

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getActivity(), "sign up successful", Toast.LENGTH_LONG).show();
                        Intent setupProfile = new Intent(getActivity(), SetUpProfile.class);
                        getActivity().startActivity(setupProfile);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "failed", Toast.LENGTH_LONG).show();
                        Log.d("parse error", e.toString());
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    public static class LogInFragment extends Fragment{
        public  LogInFragment(){
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            return inflater.inflate(R.layout.fragment_log_in, container, false);
        }
    }


}
