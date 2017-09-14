/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.util;

/**
 *
 * @author db2admin
 */
public enum TUMResponseCodes {

    SUCCESS(0, "SUCCESS"),
    DEALER_NOT_EXISTS(10, "Dealer does not exist."),
    DEALER_NOT_ACTIVE(11, "Dealer is not active."),
    USER_NOT_EXISTS(12,	"Account/User does not exits"),
    INVALID_LOGIN_CREDENTIALS(30, "Invalid Login credentials"),
    NEW_PASSWORD_CONFIRM_ERROR(31, "Password and Password Confirmation Are Not Equal"),
    SYSTEM_ERROR(40, "System Error"),
    FAILED_REQUEST_TRANSACTION_ID(41, "Request Transaction ID could not be obtained"),
    INSUFFICIENT_DEALER_BALANCE(70, "Dealer balance is not sufficient for the amount."),
    MIN_AMOUNT_PER_TRANSACTION_LIMIT(71, "Amount is less then minimum allowed per transaction"),
    MAX_AMOUNT_PER_TRANSACTION_LIMIT(72, "Amount is more then maximum allowed per transaction"),
    MAX_TRANS_PER_DAY_LIMIT(73, "Dealer account has exceeded maximum allowed per day"),
    MAX_NUM_TRANS_PER_DAY_LIMIT(74,"Max number of transaction per day per dealer exceeded."),
    RESERVATION_NOT_FOUND(80, "Reservation not found."),
    RESERVATION_ALREADY_COMMITTED(81, "Reservation already committed."),
    RESERVATION_ALREADY_CANCELLED(82, "Reservation already cancelled."),
    RESERVATION_LOCKED(83, "Reservation locked."),
    SIGNATURE_ERROR(90,	"Signature Error"),
    IN_MAINTENANCE(91, "Maintenance mode is active."),
    CS_OTHER_ERROR(100, "Charging System Error"),
    CS_SUBSCRIBER_NOT_FOUND(102, "Subscriber Not Found"),
    CS_TEMPORARY_BLOCKED(104, "Subscriber Temporary Blocked In CS"),
    CS_DEDICATED_ACCOUNT_NOT_ALLOWED(105, "Dedicated Account Not Allowed For This Subscriber"),
    CS_INVALID_REFILL_PROFILE(120, "Invalid Refill Profile"),
    CS_UNAVAILABLE(125,	"Charging System Unavailable"),
    CS_ACCOUNT_NOT_ACTIVE(126, "Subscriber Account Not Active"),
    CS_DATE_ADJUSTMENT_ERROR(136, "CS Date Adjustment Error"),
    VOUCHER_USED_BY_SAME(200, "Voucher already used by same subscriber"),
    VOUCHER_RESERVED_BY_SAME(201, "Voucher already reserved by same subscriber"),
    VOUCHER_EXPIRED(202, "Voucher expired"),
    INVALID_VOUCHER_STATUS(203, "Invalid voucher status"),
    VOUCHER_DOES_NOT_EXIST(204, "Voucher does not exist"),
    VOUCHER_USED_BY_OTHER(207, "Voucher used by other subscriber"),
    VOUCHER_RESERVED_BY_OTHER(208, "Voucher reserved by other subscriber"),
    VOUCHER_MISSING_STOLEN(209, "Voucher missing/stolen"),
    VOUCHER_UNAVAILABLE(210, "Voucher unavailable"),
    CS_ILLEGAL_REQUEST_MESSAGE(1000, "Illegal Request message"),
    CS_MANDATORY_FIELD_MISSING(1001, "Mandatory Field Missing"),
    CS_ILLEGAL_DATATYPE(1002, "Illegal Data Type");
    
    
    private final Integer code;
    private final String message;
    
    private TUMResponseCodes(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public Integer getResponseCode() {
        return code;
    }
    
    @Override
    public String toString() {
        return message;
    }
    
    public static TUMResponseCodes getResponseCode(Integer responseCode) {
        for(TUMResponseCodes item : TUMResponseCodes.values()) {
            if(item.getResponseCode().equals(responseCode)) return item;
        }
        return CS_OTHER_ERROR;
    }
}
