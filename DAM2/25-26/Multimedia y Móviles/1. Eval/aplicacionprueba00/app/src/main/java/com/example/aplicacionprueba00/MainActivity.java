package com.example.aplicacionprueba00;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button button = null;
    TextView text = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        button = (Button) findViewById(R.id.button);
        text = (TextView) findViewById(R.id.textView);
        button.setOnClickListener(this);

    }

    public void onClick(View v) {
        String textview = text.getText().toString();
        String textpulsa = getString(R.string.text_pulsa);
        String texthola = getString(R.string.text_hola_mundo);

        if (textview.equalsIgnoreCase(textpulsa)) {
            text.setText(texthola);
        } else {
            text.setText(textpulsa);
        }
    }

}