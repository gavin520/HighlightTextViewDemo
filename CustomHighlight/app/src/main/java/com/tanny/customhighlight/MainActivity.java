package com.tanny.customhighlight;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Created By tanny on 2017/9/27.
 */
public class MainActivity extends AppCompatActivity {
    private HighlightTextView hightview;
    private boolean disallowHightLineBreak = true;

    private String text = "Having a suite of unit and integration tests has many benefits."
            + "In most cases they are there to provide confidence that changes have not broken existing behaviour."
            + "Starting off with the less complex data classes was the clear choice for me. "
            + "They are being used throughout the project, yet their complexity is comparatively low. This makes them an ideal starting point to set off the journey into a new language."
            + "After migrating some of these using the Kotlin code converter, which is built into Android Studio, executing tests and making them pass, I worked my way up until eventually ending up migrating the tests themselves to Kotlin."
            + "Without tests, I would have been required to go through the touched features after each change, and manually verify them."
            + "By having this automated it was a lot quicker and easier to move through the codebase, migrating code as I went along."
            + "So, if you don’t have your app tested properly yet, there’s one more reason to do so right here";

    private String text2 = "Having a suite of unit and integration tests has many benefits.\n"
            + "In most cases they are there to provide confidence that changes have not broken existing behaviour.\n"
            + "Starting off with the less complex data classes was the clear choice for me. \n"
            + "They are being used throughout the project, yet their complexity is comparatively low. This makes them an ideal starting point to set off the journey into a new language.\n"
            + "After migrating some of these using the Kotlin code converter, which is built into Android Studio, executing tests and making them pass, I worked my way up until eventually ending up migrating the tests themselves to Kotlin.\n"
            + "Without tests, I would have been required to go through the touched features after each change, and manually verify them.\n"
            + "By having this automated it was a lot quicker and easier to move through the codebase, migrating code as I went along.\n"
            + "So, if you don’t have your app tested properly yet, there’s one more reason to do so right here";

    private List<String> highlighted = Arrays.asList("a suite of", "has many benefits", "In most cases",
            "have not broken existing behaviour", "Starting off with", "choice for", "throughout the project",
            "starting point to", "move through the codebase", "one more reason to do");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hightview = (HighlightTextView) findViewById(R.id.hightview);
    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.btn_set_text:
                disallowHightLineBreak = true;
                hightview.setDefaultColor(0x22000000);
                hightview.setHighlightColor(0xFFFF0000);
                hightview.setDisallowHighlightLineBreak(disallowHightLineBreak);
                hightview.setDisplayedText(text2, highlighted);
                break;
            case R.id.btn_set_highlight_color:
                hightview.setHighlightColor(0xFF00FF00);
                break;
            case R.id.btn_set_line_break:
                disallowHightLineBreak = !disallowHightLineBreak;
                hightview.setDisallowHighlightLineBreak(disallowHightLineBreak);
                break;
            case R.id.btn_set_default_color:
                hightview.setDefaultColor(0xAA000000);
                break;
            case R.id.btn_test_attr:
                startActivity(new Intent(this, SecondActivity.class));
                break;
        }
    }
}
