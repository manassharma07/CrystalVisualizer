package com.bragitoff.crystalviewer;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_about);
        simulateDayNight(/* DAY */ 0);
        Element adsElement = new Element();
        adsElement.setTitle("Advertise with us");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.logo)
                .setDescription((getString(R.string.about)))
                .addItem(new Element().setTitle("Version 1.0"))
                .addItem(license())
                .addWebsite("http://wiki.jmol.org/index.php/JSmol","JSmol")
                .addGitHub("manassharma07/CrystalVisualizer","Source Code")
                // .addItem(adsElement)
                .addGroup("Connect with the Developer:")
                .addEmail("feedback@bragitoff.com")
                .addWebsite("http://bragitoff.com/")
                .addFacebook("bragitoff")
                .addTwitter("manassharma07")
                .addYoutube("UCO0mdq1TnLctbwUGj_TnR7Q")
                .addPlayStore("com.bragitoff.crystalviewer")
                .addInstagram("ducktape07","Follow on Instagram")
                .addGitHub("manassharma07")
                .addItem(getCopyRightsElement())
                .create();

        setContentView(aboutPage);
    }


    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = "2017";//String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        //copyRightsElement.setIconDrawable(R.drawable.about_icon_copy_right);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(About.this, copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }

    void simulateDayNight(int currentSetting) {
        final int DAY = 0;
        final int NIGHT = 1;
        final int FOLLOW_SYSTEM = 3;

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        if (currentSetting == DAY && currentNightMode != Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        } else if (currentSetting == NIGHT && currentNightMode != Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else if (currentSetting == FOLLOW_SYSTEM) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }
    Element license() {
        Element license= new Element();
        license.setTitle("Open Source Libraries");
        license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(About.this, Libraries.class);
                startActivity(intent);
            }
        });
        return license;
    }

}
