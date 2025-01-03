package io.isometrik.gs.ui.utils;

import static io.isometrik.gs.ui.utils.Constants.ZERO;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Utilities {

    public static DecimalFormat df = new DecimalFormat("#.##");

    public static ArrayList<String> getPermissionsListForExternalStorage(boolean isForRead) {
        ArrayList<String> permissionsRequired = new ArrayList<>();
        if (Build.VERSION.SDK_INT < 33) {
            permissionsRequired.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(isForRead){
                permissionsRequired.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else {
            permissionsRequired.add(Manifest.permission.READ_MEDIA_IMAGES);
            permissionsRequired.add(Manifest.permission.READ_MEDIA_VIDEO);
            permissionsRequired.add(Manifest.permission.READ_MEDIA_AUDIO);
        }

        return permissionsRequired;
    }

    public static boolean checkSelfExternalStoragePermissionIsGranted(Context context, boolean isForRead) {

        if (Build.VERSION.SDK_INT < 33) {
            if (isForRead) {
                return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            } else {
                return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            }
        } else {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED;
        }

    }

    public static boolean shouldShowExternalPermissionStorageRational(Activity activity, boolean isForRead) {

        if (Build.VERSION.SDK_INT < 33) {
            if (isForRead) {
                return ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            } else {
                return ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            }
        } else {
            return ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_MEDIA_IMAGES)
                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_MEDIA_VIDEO)
                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_MEDIA_AUDIO);
        }
    }

    public static void requestExternalStoragePermission(Activity activity, int requestCode, boolean isForRead) {

        if (Build.VERSION.SDK_INT < 33) {
            if (isForRead) {
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        requestCode);
            } else {
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        requestCode);
            }

        } else {
            activity.requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_AUDIO},
                    requestCode);
        }

    }

    public static boolean isAllPermissionGranted(int[] grantResults) {

        boolean allPermissionsGranted = true;
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }
        return allPermissionsGranted;
    }

    private static final NavigableMap<Double, String> suffixes = new TreeMap<>();

    public static String formatMoney(double value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return formatMoney(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + formatMoney(-value);
        if (value < 1000) return Double.toString(value); //deal with easy case

        Map.Entry<Double, String> e = suffixes.floorEntry(value);
        Double divideBy = e.getKey();
        String suffix = e.getValue();

        double truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        String b = hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
        String lastChar = b.substring(b.length() - 1);

        double balance = Double.parseDouble(b.replace(lastChar, ""));
        return df.format(balance) + "" + lastChar;
    }

    //to get image optimized gumlet image
    public static String getOptimizedGumletImage(View view, String imageUrl) {
        int imageWidth = view.getWidth() - view.getPaddingLeft() - view.getPaddingRight();
        int imageHeight = view.getHeight() - view.getPaddingTop() - view.getPaddingBottom();
        return String.format(Locale.getDefault(), "%s?q=%d?w=%d?h=%d", imageUrl.replace("s3.eu-central-1.amazonaws.com/soldliveapp", "soldlive.gumlet.io"), 80, Math.round(imageWidth), Math.round(imageHeight));
    }

    public static String convertTimeStamptoString(Long timestamp, String format) {
        String originalFormat = format;
        format = format.replace("Today", "");
        format = format.replace("Tomorrow", "");
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            Date date = new Date(timestamp);
            return originalFormat.replace(format, dateFormat.format(date).replace("am", "AM").replace("pm", "PM"));
        } catch (Exception e) {
            return e.getLocalizedMessage();
        }
    }

    /**
     * used to group three digits and place comma in the place of decimal point
     **/
    public static String groupPriceFormatter(String currencySymbol, String price) {
        currencySymbol = decodeFromBase64(currencySymbol);
        price = price.replace(",",".").replace(" ","").replace(currencySymbol, "");
        double amount = 0.0;
        try {
            amount = Double.parseDouble(price);
        } catch (Exception ignore) {
            if (currencySymbol.equals("zł")) {
                return String.format(Locale.getDefault(), "%s%s", price, currencySymbol);
            } else {
                return String.format(Locale.getDefault(), "%s%s", currencySymbol, price);
            }
        }
        if (currencySymbol.equals("zł")) {

            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');  // Space for grouping
            symbols.setDecimalSeparator(',');   // Comma for decimal

            // Create DecimalFormat instance with the custom symbols
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);

            // Format the number
            String formattedNumber = decimalFormat.format(amount);
            return formattedNumber + " " + currencySymbol;
        } else {
            return String.format(Locale.getDefault(), "%s%.2f", currencySymbol, amount);
        }
    }

    /**
     * check if string is encoded in base64 or not
     * if encoded it will decode from base64
     *
     * @param input String -> string to decode
     * @return String -> decoded string
     */
    public static String decodeFromBase64(String input) {
        if (isBase64Encoded(input))
            return new String(Base64.decode(input.getBytes(), Base64.DEFAULT));
        else
            return input;
    }

    /**
     * check if string is encoded or not
     *
     * @param input String
     * @return boolean true if encoded and false if not encoded
     */
    public static boolean isBase64Encoded(String input) {
        try {
            // Decoding will succeed only for valid Base64-encoded strings
            byte[] decodedBytes = Base64.decode(input, Base64.NO_WRAP);
            // Encoding back to Base64 to check if it remains unchanged
            String reencoded = Base64.encodeToString(decodedBytes, Base64.NO_WRAP);
            return input.equals(reencoded);
        } catch (IllegalArgumentException e) {
            // If decoding fails, the string is not Base64-encoded
            return false;
        }
    }

    /**
     * convertPriceInToDecimal method is use for cpnverting amount in 2 decimal point and retrun in string
     * @param price
     * @return amount in String
     */
    public static String convertPriceInToDecimalIfDecimalPresent(String price) {
        String priceValue = price;
        try {
            double value = Double.parseDouble(priceValue);
            priceValue = String.format("%." + (value % 1 > 0.0 ? 2 : 0) + "f", value);
        }catch (Exception e){
            e.printStackTrace();
        }
        return priceValue;
    }

    /**
     * return if array is empty or not
     *
     * @param object List
     * @return boolean
     */
    public static boolean isEmptyArray(List object) {
        return object == null || object.size() == ZERO;
    }

    public static void printLog(String msg) {
        Log.i("IsometrikSdk", "msg  " + msg);
    }

}
