package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cmpt276.termproject.R;
import cmpt276.termproject.model.MusicManager;

public class OptionActivity extends AppCompatActivity {

    ConstraintLayout os_Layout;
    public MusicManager musicManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        musicManager = MusicManager.getInstance();
        musicManager.play();
        os_Layout = findViewById(R.id.os_Layout);
        os_Layout.setBackgroundResource(R.drawable.bg_options);

        setupBackButton();
    }


    private void setupBackButton(){
        Button back_btn = findViewById(R.id.options_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context,OptionActivity.class);
        return intent;
    }
}