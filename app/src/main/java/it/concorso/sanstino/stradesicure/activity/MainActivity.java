package it.concorso.sanstino.stradesicure.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.concorso.android.stradesicure.R;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.ss.stradesicure.UnityPlayerActivity;

import java.util.ArrayList;
import java.util.List;

import it.concorso.sanstino.stradesicure.blurredgridmenu.GridMenu;
import it.concorso.sanstino.stradesicure.blurredgridmenu.GridMenuFragment;

public class MainActivity extends AppCompatActivity {

    public static final String ACTION_FORWARD_INTRO = "it.concorso.sanstino.stradesicure.activity.ACTION_FORWARD_INTRO";
    private static String url = "http://www.sanstino.it";
    private GridMenuFragment mGridMenuFragment;
    private Context context;

    LinearLayout menuContainer;
    LinearLayout goToQuiz,
            goToGame,
            goToSite,
            goToSettings,
            goToAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this.getApplicationContext();

        Intent intent = getIntent();
        String action = intent.getAction();
        if (action != null && action.equals(ACTION_FORWARD_INTRO)) {
            Intent introIntent = new Intent(MainActivity.this, MaterialIntroActivity.class);
            startActivity(introIntent);
        }

        mGridMenuFragment = GridMenuFragment.newInstance(R.drawable.splash_initial);
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.main_frame, mGridMenuFragment);
        tx.addToBackStack(null);
        tx.commit();

        setupGridMenu();

        mGridMenuFragment.setOnClickMenuListener(new GridMenuFragment.OnClickMenuListener() {
            @Override
            public void onClickMenu(GridMenu gridMenu, int position) {
                switch(position) {
                    case 0:
                        goToCategorySelectionActivity();
                        break;
                    case 1:
                        goToGameActivity();
                        break;
                    case 2:
                        goToSiteActivity();
                        break;
                    case 3:
                        goToSettingsActivity();
                        break;
                    case 4:
                        goToAboutActivity();
                        break;
                }
            }
        });

        /*
        menuContainer = (LinearLayout) findViewById(R.id.menu_container);
        goToQuiz = (LinearLayout) findViewById(R.id.go_to_quiz);
        goToGame = (LinearLayout) findViewById(R.id.go_to_game);
        goToSite = (LinearLayout) findViewById(R.id.go_to_site);
        goToSettings = (LinearLayout) findViewById(R.id.settings_group);
        goToAbout = (LinearLayout) findViewById(R.id.go_to_about);

        goToQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCategorySelectionActivity();
            }
        });

        goToGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGameActivity();
            }
        });

        goToSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSiteActivity();
            }
        });

        goToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSettingsActivity();
            }
        });

        goToAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAboutActivity();
            }
        });

        */

    }

    private void setupGridMenu() {
        List<GridMenu> menus = new ArrayList<>();
        menus.add(new GridMenu("Quiz", R.drawable.ic_question_answer_white_24dp));
        menus.add(new GridMenu("Gioco", R.drawable.ic_gamepad_white_24dp));
        menus.add(new GridMenu("Sito Web", R.drawable.ic_open_in_browser_white_24dp));
        menus.add(new GridMenu("Impostazioni", R.drawable.ic_settings_white_24dp));
        menus.add(new GridMenu("Open Source", R.drawable.ic_github_white_24dp));

        mGridMenuFragment.setupMenu(menus);
    }

    private void goToCategorySelectionActivity() {
        Intent i = new Intent(MainActivity.this, CategorySelectionActivity.class);
        startActivity(i);
    }

    private void goToGameActivity() {
        /*Snackbar snackbar = Snackbar
                .make(menuContainer, "GAME LAUNCHED", Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        */
        Intent i = new Intent(MainActivity.this, UnityPlayerActivity.class);
        startActivity(i);
        // finish();
    }

    private void goToSiteActivity() {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        startActivity(intent);
    }

    private void goToSettingsActivity() {
        Intent i = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(i);
    }

    private void goToAboutActivity() {
        new LibsBuilder()
                // pass the fields of your application to the lib so it can find all external lib information
                .withFields(R.string.class.getFields())
                .withActivityTitle("Open Source")
                .withAboutAppName("Strade Sicure")
                // provide a style (optional) (LIGHT, DARK, LIGHT_DARK_TOOLBAR)
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                // start the activity
                .start(this);
    }

    // handle double tap to close
    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();
    private Toast toastExit;

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
            // dismiss Toast to avoid it being visible after the app is terminated
            // wtf it throws error
            // toastExit.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        toastExit = Toast.makeText(this, "Premi INDIETRO per uscire dall'app", Toast.LENGTH_SHORT);
        toastExit.show();
        mHandler.postDelayed(mRunnable, 2000);
    }
}
