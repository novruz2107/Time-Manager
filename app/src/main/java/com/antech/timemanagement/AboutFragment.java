package com.antech.timemanagement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutFragment extends Fragment {

    TextView textView9;
    ImageView facebook;
    ImageView instagram;
    ImageView twitter;
    private DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle toolbarDrawerToggle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        Toolbar toolbar = view.findViewById(R.id.about_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_button);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        toolbar.setTitle("About");

        mDrawerLayout =  view.findViewById(R.id.drawer_layout);
        Toolbar myToolbar =  view.findViewById(R.id.my_toolbar);
        toolbarDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, myToolbar, R.string.open, R.string.close);


        facebook = (ImageView) view.findViewById(R.id.facebook);
        instagram = (ImageView) view.findViewById(R.id.instagram);
        twitter = (ImageView) view.findViewById(R.id.twitter);
        textView9 = (TextView) view.findViewById(R.id.textView9);
        textView9.setMovementMethod(LinkMovementMethod.getInstance());

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.facebook.com/antech21/"));
                startActivity(intent);
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.instagram.com/novruz2107/"));
                startActivity(intent);
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://twitter.com/Novruz2107"));
                startActivity(intent);
            }
        });



        return view;
    }
}
