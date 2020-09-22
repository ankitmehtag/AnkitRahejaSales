package com.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mixpanel.android.util.StringUtils;

import java.text.DecimalFormat;

public class StringUtil {

    public static boolean validEmail(String strEmail) {

        int length = strEmail.length();
        if (strEmail.indexOf('@') == -1 || strEmail.indexOf('.') == -1) {
            return false;
        } else if (strEmail.charAt(0) == '@'
                || strEmail.charAt(length - 1) == '@'
                || strEmail.charAt(0) == '.'
                || strEmail.charAt(length - 1) == '.') {
            return false;
        } else if (verifyForSpace(strEmail)) {
            return false;
        } else {
            if (strEmail.lastIndexOf('@') > strEmail.lastIndexOf('.')) {
                return false;
            }
            return true;
        }
    }

    public static boolean verifyForSpace(String str) {
        boolean spaceExist = false;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') {
                spaceExist = true;
                break;
            }
        }
        return spaceExist;
    }

    public static boolean checkSpecialCharacter(String name) {
        for (int i = 0; i < name.length(); ++i) {
            char ch = name.charAt(i);
            if (!Character.isLetter(ch) && !(ch == ' ')) {
                return true;
            }
        }
        return false;
    }

    public static String getCamelCase(String text) {
        String s1 = text.substring(0, 1).toUpperCase();
        String nameCapitalized = s1 + text.substring(1);
        return nameCapitalized;

    }

    public static String getExtension(String picturePath) {
        final String ext = picturePath.substring(picturePath
                .lastIndexOf(".") + 1);
        String ext1;
        if (ext.equals("jpg"))
            ext1 = "jpeg";
        else
            ext1 = ext;
        return ext1;
    }

    public static String removeLastComma(String str) {
        str = str.trim();
            if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static String amountCommaSeparate(String str) {
        if (!TextUtils.isEmpty(str)) {
            DecimalFormat numFormat = new DecimalFormat("##,##,##,###");
            String number = numFormat.format(Integer.valueOf(str.trim()));
            return number;
        }
        return "";
    }

    public static void createColoredProfileName(String text, ImageView imageView, Object o) {
        if (!TextUtils.isEmpty(text)) {
            String firstLetter = text.substring(0, 1).toUpperCase();
            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            // generate random color
            int color = generator.getColor(o);
            //int color = generator.getRandomColor();
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(firstLetter, color); // radius in px
            imageView.setImageDrawable(drawable);
        }
    }
}
