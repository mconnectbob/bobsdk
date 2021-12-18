package com.bob.bobapp.utility;

import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.bob.bobapp.R;
import com.bob.bobapp.fragments.ReportFragment;
import com.bob.bobapp.listener.onSortItemListener;

public class PopUpClass {
    private Context context;
    private onSortItemListener onSortItemListener;

    public void showPopupWindow(final View view,Context context,onSortItemListener onSortItemListener)
    {
        this.context=context;
        this.onSortItemListener=onSortItemListener;

        int[] loc_int = new int[2];
        if (view == null);
        try
        {
            view.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe)
        {
            //Happens when the view doesn't exist on screen anymore.
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + view.getWidth();
        location.bottom = location.top + view.getHeight();


        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_menu, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.RIGHT|Gravity.TOP,  48, location.bottom);
       
        //Initialize the elements of our window, install the handler

        LinearLayout linearHome = popupView.findViewById(R.id.linearHome);
        LinearLayout linearProfile = popupView.findViewById(R.id.linearProfile);
        LinearLayout linearStopSip = popupView.findViewById(R.id.linearStopSip);
        LinearLayout linearDimatHolding = popupView.findViewById(R.id.linearDimatHolding);

        linearHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSortItemListener.onSortItemListener("home");
                popupWindow.dismiss();
            }
        });

        linearProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSortItemListener.onSortItemListener("profile");
                popupWindow.dismiss();
            }
        });

        linearStopSip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSortItemListener.onSortItemListener("stop sip");
                popupWindow.dismiss();
            }
        });
        linearDimatHolding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSortItemListener.onSortItemListener("demat holding");
                popupWindow.dismiss();
            }
        });


        //Handler for clicking on the inactive zone of the window
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
    }



}