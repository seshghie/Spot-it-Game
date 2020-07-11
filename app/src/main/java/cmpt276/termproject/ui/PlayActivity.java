package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.time.LocalTime;

import cmpt276.termproject.R;
import cmpt276.termproject.model.CardDrawer;
import cmpt276.termproject.model.GameManager;
import cmpt276.termproject.model.HighScores;
import cmpt276.termproject.model.MusicManager;

public class PlayActivity extends AppCompatActivity  {

    ConstraintLayout ps_Layout;
    ConstraintLayout.LayoutParams btn_size;
    private GameManager gameManager;

    String name;
    String dateTime;

    private Chronometer chronometer;
    private boolean chrono_started ;

    private boolean dialog_open = false;
    private double game_start_time;
    public MusicManager musicManager;
    public HighScores highscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        highscore = HighScores.getInstance();

        ps_Layout = findViewById(R.id.root);
        ps_Layout.setBackgroundResource(R.drawable.bg_play);


        chronometer = findViewById(R.id.stopwatch);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        musicManager = MusicManager.getInstance();
        setup();

        chronometer = findViewById(R.id.stopwatch);
        chrono_started = false;
    }







    private void setup(){
        //Setup Game Manager Class
        gameManager = GameManager.getInstance();
        gameManager.createCards();

        FrameLayout frameLayout = findViewById(R.id.frame);

        CardDrawer surfaceView = new CardDrawer(getApplicationContext());

        frameLayout.addView(surfaceView);

        setupBackButton();

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN){
            Log.e("Popup", "Touch");

        }

        if (!chrono_started) {
            chrono_started = true;
            game_start_time = System.currentTimeMillis();
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
        }

        if (gameManager.getDrawPile().size() == 0 && !dialog_open){
            int elapsed = ((int)(SystemClock.elapsedRealtime()-chronometer.getBase()))/1000;
            chronometer.stop();

            dateTime = highscore.getCurrentDateTime();
            double elapsed_time_ms = System.currentTimeMillis() - game_start_time;

            double time = elapsed_time_ms/1000;

            //Ex format: 8.5
            //Dont need to use the chrono since the elapsed time already includes seconds

            popup(dateTime, String.valueOf(time));
            Log.e("Popup", "Open");

            dialog_open = true;
        }
        return super.onTouchEvent(event);
    }





    private void popup(final String dateTime,final String time){
        musicManager = MusicManager.getInstance();
        musicManager.play();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_pop_up);

        Button save = dialog.findViewById(R.id.saveBtn);
        Button cancel = dialog.findViewById(R.id.cancelBtn);
        final EditText userId = dialog.findViewById(R.id.userId);
        final TextView score_p = dialog.findViewById(R.id.score);
        final TextView dateTime_p = dialog.findViewById(R.id.dateTime);
        score_p.setText(time);
        dateTime_p.setText(dateTime);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicManager.pause();
                dialog.dismiss();
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = userId.getText().toString();
                userId.setText(name);

                if(userId.getText().toString().length()!=0) {
                    userId.setClickable(false);
                    userId.setFocusableInTouchMode(false);
                    userId.setEnabled(false);
                    String entry = (time + "/ " + name + "/" + dateTime);

                    SharedPreferences entry_new = getSharedPreferences("entry", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = entry_new.edit();
                    int counter = entry_new.getInt("counter", 0);
                    counter ++;
                    editor.putString("new entry"+counter, entry);
                    editor.putInt("counter",counter);

                    editor.apply();
                    musicManager.pause();
                    dialog.dismiss();
                    finish();
                }
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        //<a href='https://www.freepik.com/free-photos-vectors/background'>Background vector created by starline - www.freepik.com</a>

    }



    private void setupBackButton(){
        Button back_btn = findViewById(R.id.play_back_btn);

        btn_size = (ConstraintLayout.LayoutParams) back_btn.getLayoutParams();
        btn_size.width = (getResources().getDisplayMetrics().widthPixels)/5;
        btn_size.height = (getResources().getDisplayMetrics().heightPixels)/8;
        back_btn.setLayoutParams(btn_size);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, PlayActivity.class);
    }

}
