package net.validcat.fishing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        setDrawerFragment();
    }


    private void setDrawerFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_holder, new GoogleSignInFragment())
                .commit();
    }
}
