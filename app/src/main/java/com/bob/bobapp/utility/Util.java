package com.bob.bobapp.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bob.bobapp.Home.BaseContainerFragment;
import com.bob.bobapp.R;
import com.bob.bobapp.activities.BOBActivity;
import com.bob.bobapp.api.bean.ProductValueBean;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;

import static android.graphics.Typeface.BOLD;

public class Util {

    public static boolean isAlert = false;


    public static Typeface iconFont;

    private Context context;

    public Util(Context context){

        this.context = context;

        iconFont = FontManager.getTypeface(context, FontManager.FONTAWESOME);
    }

    public String callAPIUrlWithPost(String strUrl){

        strUrl = strUrl.replaceAll(" ", "%20");

        System.out.println("URLLLLLLLLLLLLLL :"+strUrl);

        String data = "";

        InputStream iStream = null;

        try{

            URL url = new URL(strUrl);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(15000);

            urlConnection.setConnectTimeout(15000);

            urlConnection.setRequestMethod("POST");

            urlConnection.setDoInput(true);

            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");

            urlConnection.setRequestProperty("Accept", "application/json");

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";

            while( ( line = br.readLine())  != null){

                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){

            /*data = new Gson().toJson(createResponseOnConnectionFail());

            System.out.println("ON CONNECTION FAIL POST URL :" + data);*/

            e.printStackTrace();
        }

        return data;
    }

    public String httpPostJsonRequestForMATM(String strUrl, String jsonObject){


        InputStream inputStream = null;

        String result = "";

        try {

            URL url = new URL(strUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(15000);

            conn.setConnectTimeout(15000);

            conn.setRequestMethod("POST");

            conn.setDoInput(true);

            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");

            conn.setRequestProperty("Accept", "application/json");

            conn.connect();

            OutputStream os = new BufferedOutputStream(conn.getOutputStream());

            os.write(jsonObject.getBytes());

            os.flush();

            inputStream = conn.getInputStream();

            if(inputStream != null){

                result = convertInputStreamToString(inputStream);
            }
            else{

                result = "Did not work!";
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return result;
    }

    public String authorize(String userName, String userType)  {

        String authorization="";

        try {

            String passbuffer = userName + ":" + userType;

            byte[] data = passbuffer.getBytes("UTF-8");

            String base64 = Base64.encodeToString(data, Base64.NO_WRAP);

            authorization = "Basic " + base64;

        }catch (UnsupportedEncodingException ex){

            ex.printStackTrace();
        }

        return authorization;
    }

    public String httpPostJsonRequest(String strUrl, String jsonObject){

        String auth = authorize("Ebix", "Ebix@2019");

        InputStream inputStream = null;

        String result = "";

        try {

            URL url = new URL(strUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(60000);

            conn.setConnectTimeout(60000);

            conn.setRequestMethod("POST");

            conn.setDoInput(true);

            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");

            conn.setRequestProperty("Accept", "application/json");

            conn.setRequestProperty("Authorization", auth);

            conn.connect();

            OutputStream os = new BufferedOutputStream(conn.getOutputStream());

            os.write(jsonObject.getBytes());

            os.flush();

            inputStream = conn.getInputStream();

            if(inputStream != null){

                result = convertInputStreamToString(inputStream);
            }
            else{

                result = "Did not work!";
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return result;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));

        String line = "";

        String result = "";

        while((line = bufferedReader.readLine()) != null) {

            result += line;
        }

        inputStream.close();

        return result;
    }

    public JSONObject createJsonObject(LinkedHashMap<String, String> map){

        JSONObject jsonObject = new JSONObject();

        try {

            for (Map.Entry<String, String> entry : map.entrySet()) {

                String key = entry.getKey();

                String value = entry.getValue();

                jsonObject.put(key, value);
            }

        }catch(Exception e){

            e.printStackTrace();
        }

        return jsonObject;
    }

    public void alertboxConnectivity(String title, String mymessage) {

        TextView textView = new TextView(context);

        textView.setText(title);

        textView.setPadding(70,20,10,10);

        textView.setTypeface(null,BOLD);

        textView.setTextSize(18);

        new AlertDialog.Builder(context)

                .setMessage(mymessage)

                .setCustomTitle(textView)

                .setCancelable(false)

                .setNeutralButton(android.R.string.ok,

                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {


                            }

                        }).show();
    }

    public String validateData(LinkedHashMap<String, String> linkedHashMap){

        String validateStr = "";

        for (Map.Entry<String,String> entry : linkedHashMap.entrySet()) {

            String key = entry.getKey();

            String value = entry.getValue();

            if(value.equals("")){

                if(key.equalsIgnoreCase("Terms and Conditions")){

                    validateStr += "Please accept " + key + ".\n";

                }else if(key.contains("select")){

                    validateStr += "Please " + key + ".\n";

                }else {

                    validateStr += "Please enter " + key + ".\n";
                }
            }
        }

        return validateStr;
    }

    public String getXMLRequest(String startNode, LinkedHashMap<String, String> linkedHashMap){

        String xml = "<" + startNode + ">";

        for (Map.Entry<String,String> entry : linkedHashMap.entrySet()) {

            String key = entry.getKey();

            String value = entry.getValue();

            xml += "<" + key + ">" + value + "</" + key + ">";
        }

        xml += "</" + startNode + ">";

        return xml;
    }

    public boolean checkInternetConnection() {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {

            return true;

        } else {

            return false;
        }
    }

    static ProgressDialog dialog = null;

    public static void showProgressDialog(Context context, boolean isShow) {

        try {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (dialog == null && isShow) {

                dialog = ProgressDialog.show(context, null, null);

                dialog.setContentView(R.layout.small_progress_bar);

                dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);

                /*Drawable drawable = new ColorDrawable(Color.BLACK);

                drawable.setAlpha(90);

                dialog.getWindow().setBackgroundDrawable(drawable);*/

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();

                lp.dimAmount = 0.0f;

                dialog.getWindow().setAttributes(lp);

                dialog.getWindow().addFlags(WindowManager.LayoutParams.ALPHA_CHANGED);

                dialog.setCancelable(false);

                dialog.setCanceledOnTouchOutside(false);

                dialog.show();

            } else {

                if (dialog != null && dialog.isShowing()) {

                    dialog.dismiss();

                    dialog = null;
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public String formatDate(String value){

        System.out.println("DATE VALUE: "+value);

        String formattedDate = "";

        String valueArray[] = value.split("T");

        String dateArray[] = valueArray[0].split("-");

        String date = dateArray[2];

        String month = dateArray[1];

        String year = dateArray[0];

        Calendar c = Calendar.getInstance();

        c.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(date));

        DateFormat dateFormat=new SimpleDateFormat("dd MMM");

        formattedDate = dateFormat.format(c.getTime());

        return formattedDate;
    }

    public String getCurrentDate(boolean getIsBefore){
        //2020-01-07T00:00:00

        String formatedDate = "";

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Calendar cal = Calendar.getInstance();

        if(getIsBefore) {

            cal.add(Calendar.DATE, -7);
        }

        Date date = cal.getTime();

        formatedDate = dateFormat.format(date);

        return formatedDate;
    }

    public BigDecimal truncateDecimal(double x)
    {

        if ( x > 0) {

            return new BigDecimal(String.valueOf(x)).setScale(2, BigDecimal.ROUND_FLOOR);

        } else {

            return new BigDecimal(String.valueOf(x)).setScale(2, BigDecimal.ROUND_CEILING);
        }
    }


    public String formatDateForFactsheet(String strDate) {

        String ddate = null;

        try {

            SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd");

            SimpleDateFormat dFormatFinal = new SimpleDateFormat("MMM dd, yyyy");

            Date date = dFormat.parse(strDate);

            ddate = dFormatFinal.format(date);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return ddate;
    }

    public void hideKeyboard(View view){

        try {

            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }catch (Exception e){

            e.printStackTrace();
        }
    }

    private View createSpinnerItemInPopup(int layoutId, Fragment fragment){

        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
        LayoutInflater inflater = fragment.getLayoutInflater();
        final View dialogView = inflater.inflate(layoutId, null);
        dialogBuilder.setView(dialogView);
        androidx.appcompat.app.AlertDialog b = dialogBuilder.create();
        b.show();

        return dialogView;
    }

    public void alertboxExitFromApp(String title, String mymessage) {

        TextView textView = new TextView(context);

        textView.setText(title);

        textView.setPadding(70, 20, 10, 10);

        textView.setTypeface(null, BOLD);

        textView.setTextSize(18);

        new AlertDialog.Builder(context)

                .setMessage(mymessage)

                .setCustomTitle(textView)

                .setCancelable(false)

                .setPositiveButton("Yes",

                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                System.exit(0);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                    }

                }).show();
    }

    public int[] getBobStyleColors(ArrayList<ProductValueBean> productValueBeanArrayList){

        int colorArray[] = new int[8];

        for(int i = 0; i < 8; i++){

            String fileName = "color_bob_style_" + i;

            int resId = context.getResources().getIdentifier(fileName, "color", context.getPackageName());

            int colorId = context.getResources().getColor(resId);

            colorArray[i] = colorId;
        }

        colorArray = createColorArray(productValueBeanArrayList, colorArray);

        return colorArray;
    }

    private int[] createColorArray(ArrayList<ProductValueBean> productValueBeanArrayList, int[] VORDIPLOM_COLORS){

        int quotient = productValueBeanArrayList.size() / VORDIPLOM_COLORS.length;

        int remainder = productValueBeanArrayList.size() % VORDIPLOM_COLORS.length;

        int arrNew[] = null;

        if(remainder == 0) {

            arrNew = new int[productValueBeanArrayList.size()];

        }else{

            arrNew = new int[productValueBeanArrayList.size() + VORDIPLOM_COLORS.length];

            quotient = quotient + 1;
        }

        for(int i = 0; i < quotient; i++){

            for(int j = 0; j < VORDIPLOM_COLORS.length; j++){

                if(i == 0) {

                    arrNew[j] = VORDIPLOM_COLORS[j];

                }else{

                    arrNew[(i * VORDIPLOM_COLORS.length) + j] = VORDIPLOM_COLORS[j];
                }
            }
        }

        return arrNew;
    }

    public void highlightTabText(){

        for(int i = 0; i < BOBActivity.mTabHost.getTabWidget().getTabCount(); i++){

            View v = BOBActivity.mTabHost.getTabWidget().getChildTabViewAt(i);

            TextView text = (TextView) v.findViewById(R.id.tv_title);

            if(i == BOBActivity.mTabHost.getCurrentTab()){

                text.setTypeface(null, Typeface.BOLD);

            }else{

                text.setTypeface(null, Typeface.NORMAL);
            }
        }
    }

    public LinkedHashMap<String, String> sortByValue(LinkedHashMap<String, Double> hm)
    {

        List<Map.Entry<String, Double> > list = new LinkedList<Map.Entry<String, Double> >(hm.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Double> >() {

            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2){

                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        LinkedHashMap<String, String> temp = new LinkedHashMap<String, String>();

        for (Map.Entry<String, Double> aa : list) {

            temp.put(aa.getKey(), String.valueOf(aa.getValue()));
        }

        return temp;
    }

}
