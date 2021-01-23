package com.eeducationgo.markoupdrivers.util;

public interface Constance {

    //todo this for passenger user data
    public static String RootDriver = "Driver";
    public static String ChildDriverUID = "Uid";
    public static String ChildDriverName = "Name";
    public static String ChildDriverImage = "Image";
    public static String ChildDriverEmail = "Email";
    public static String ChildDriverPhone = "Phone";
    public static String ChildDriverType = "Type";
    public static String ChildDriverVehicleType = "VehicleType";
    public static String ChildDriverNumberSeats = "NumberSeats";
    public static String ChildDriverVehicleNumber = "VehicleNumber";
    public static String ChildDriverStorageUser = "StorageUser";
    public static String ChildDriverIndComp = "IndCom";
    public static String ChildDriverIsAvailable = "IsAvailable";
    public static String ChildDriverStation = "station";
    public static String ChildDriverDestination = "destination";
    public static String ChildDriverTripTime = "tripTime";
    public static String ChildDriverTripDate = "tripDate";
    public static String ChildDriverLate = "Late";
    public static String ChildDriverLong = "Long";
    public static String ChildDriverToken = "Token";

    //todo this for intent
    public  static  String userKey ="User";
    public  static  String typeKey ="Type";

    //todo Booking Request
    public static String RootBooking = "BookingRequest";
    public static String ChildBookingSUID = "SUid";
    public static String ChildBookingRUID = "RUid";
    public static String ChildBookingRequestType = "RequestType";
    public static String ChildBookingRequestSend = "Send";
    public static String ChildBookingRequestReceiver = "Receiver";

    //todo this for passneger
    public static String RootPassenger = "Passenger";

    //todo this is booking list
    public static String RootBookingList = "BookingList";
    public static String ChildBookingListBook = "Book";
    public static String ChildBookingListSaveValue = "Save";

    //todo this for add passanger manualy
    public static String ChildBookingListUserName = "UserName";
    public static String ChildBookingListPrice = "Price";


    // TODO: 9/27/2020 this for notification keys
    String CONTENT_TYPE_HEADER = "Content-Type";
    String CONTENT_TYPE = "application/json;charset=UTF-8";
    String AUTH_HEADER = "Authorization";
    String AUTH = "key=AAAAlT1jVXw:APA91bE36-8Z9XElrMQGoEyFvLbMZEEt6NMfHq-a994hPXdBlQ_FujH20PScJu_Rlb-8W8D4Tzhmc_JpolSlccq3XaNlhmix0U-cOFP1zwORoi42_56YwMVM0QuodPSZNcdsdZVGPAoW";
    String TO_HEADER = "to";
    String TITLE_HEADER = "title";
    String BODY_HEADER = "body";
    String NOTIFICATION_OBJECT_HEADER = "notification";
    String BASE_URL = "https://fcm.googleapis.com/fcm/send";
    String REQUEST_METHOD = "POST";
    // TODO: 9/27/2020 this for notification keys
}
