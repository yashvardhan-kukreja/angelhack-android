package com.ieeevit.evento.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ieeevit.evento.R;

import static maes.tech.intentanim.CustomIntent.customType;

public class MoreInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        findViewById(R.id.aboutTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoreInfoActivity.this, IeeeInfoActivity.class);
                startActivity(intent);
                customType(MoreInfoActivity.this, "left-to-right");
            }
        });

        findViewById(R.id.contactTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] address = {"projects.ieeevit@gmail.com"};
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, address);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        findViewById(R.id.appTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoreInfoActivity.this, AppInfoActivity.class);
                startActivity(intent);
                customType(MoreInfoActivity.this, "left-to-right");
            }
        });

    }
}
