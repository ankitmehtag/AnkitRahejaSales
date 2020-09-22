package com.utils;

import com.payu.india.Payu.PayuErrors;

/**
 * Created by Naresh on 16-May-17.
 */

public class PayUErrorCodes implements PayuErrors {

    public static String getErrorMsg(int errorCode){
        String msg = "";
        switch (errorCode){
            case NO_ERROR :
                break;
            case MISSING_PARAMETER_EXCEPTION :
                break;
            case NUMBER_FORMAT_EXCEPTION:
                break;
            case INVALID_AMOUNT_EXCEPTION :
                break;
            case UN_SUPPORTED_ENCODING_EXCEPTION :
                break;
            case INVALID_BANKCODE_EXCEPTION :
                break;
            case INVALID_PG_EXCEPTION :
                break;
            case INVALID_CARD_TOKEN_EXCEPTION :
                break;
            case INVALID_CARD_NUMBER_EXCEPTION :
                msg = "Invalid card number";
                break;
            case INVALID_CVV_EXCEPTION :
                break;
            case INVALID_MONTH_EXCEPTION :
                break;
            case INVALID_YEAR_EXCEPTION :
                break;
            case CARD_EXPIRED_EXCEPTION :
                break;
            case USER_CREDENTIALS_NOT_FOUND_EXCEPTION :
                break;
            case NO_SUCH_ALGORITHM_EXCEPTION :
                break;
            case DELETE_CARD_EXCEPTION :
                break;
            case GET_USER_CARD_EXCEPTION :
                break;
            case INVALID_HASH :
                break;
            case APPLICATION_NOT_FOUND_EXCEPTION :
                break;
            case INVALID_SUBVENTION_AMOUNT_EXCEPTION :
                break;
            case INVALID_VPA :
                break;
        }
        return msg;
    }

}
