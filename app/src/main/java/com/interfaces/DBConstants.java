package com.interfaces;

public class DBConstants {
    String DATABASE_NAME = "/CRM.db";
    String DATA = "data";
    int DB_VERSION = 2;
    String ID ="id";
    String USERID ="userid";
    String ENCODED_PWD ="encoded_pwd";
    String USERNAME = "username";
    String FIRSTNAME = "firstname";
    String LASTNAME = "lastname";
    String EMAIL = "email";
    String CONTACT_NO = "contact_no";
    String DESIGNATION = "designation";
    String DESIGNATION_ID = "designation_id";
    String LABEL = "label";
    String VALUE = "value";
    String ACTION = "action";
    //String UPDATE_ACTION = "update_action";
    String IS_ACCEPTED = "is_accepted";
    String IS_REJECTED = "is_rejected";
    String IS_UPDATED = "is_updated";
    String IS_OVERDUE = "is_overdue";
    String IS_SELF_ASSIGNED = "is_self_assigned";
    String IS_TEAM_ASSIGNED = "is_team_assigned";
    String IS_EXTENDED = "is_extended";
    String ISSYNC = "issync";
    String ENQUIRY_ID = "enquiry_id";
    String LEAD_ADDED_BY = "lead_added_by";
    String CLIENT_NAME = "client_name";
    String CLIENT_NUMBER = "client_number";
    String CLIENT_ADDRESS = "client_address";
    String CURRENT_STATUS = "current_status";
    String CURRENT_STATUS_ID = "current_status_id";
    String ALTERNATE_NO = "alternate_no";
    String ADDRESS = "address";
    String FOLLOW_UP_DATETIME = "follow_up_datetime";
    String PROJECT = "project";
    String PROJECT_NAME = "project_name";
    String PROJECT_ID = "project_id";
    String ASM = "asm";
    String REMARKS = "remarks";
    String LAST_UPDATE_TIME = "last_update_time";
    String BUDGET= "budget";
    String HISTORY_ID = "history_id";
    String LEAD_NUMBER = "lead_number";
    String CREATED_AT = "created_at";
    String DETAILS = "details";
    String TYPE = "type";
    String NOTES = "notes";
    String META_DATA = "meta_data";
    String ACCEPTED_TIME = "accepted_time";
    String REJECTED_TIME = "rejected_time";
    String STATUS_ID = "status_id";
    String STATUS_NAME = "status_name";
    String STATUS = "status";
    String SUB_STATUS_ID = "sub_status_id";
    String SUB_STATUS_NAME = "status_name";
    String SUB_STATUS = "sub_status";
    String ACTIVITY_STATUS = "activity_status";
    //Update Table
    String CLOSURE_CHQ_CLTD = "closure_chq_cltd";
    //String CLOSURE_LEAD_CLOSE = "closure_lead_close";
    String CLOSURE_ONLINE_TRNS = "closure_online_trns";
    String CLOSURE_LOGIN_COMPLETED = "closure_login_completed";
    //String FUTURE_REF_CALLBACK = "future_ref_callback";
    //String FUTURE_REF_COLDCALL = "future_ref_coldcall";
    //String FUTURE_REF_FOLLOWUP = "future_ref_followup";
    String MEETING_DONE = "meeting_done";
    String MEETING_NOT_DONE = "meeting_not_done";
    String MEETING_CDL = "meeting_cdl";
    String NO_RESPONSE_CALLDIS = "no_response_calldis";
    String NO_RESPONSE_NO_ANS = "no_response_no_ans";
    String NO_RESPONSE_NOT_REACH = "no_response_not_reach";
    String NO_RESPONSE_OTHER = "no_response_other";
    String NO_RESPONSE_SWTCHOFF = "no_response_swtchoff";
    //String NOT_INTRSTD = "not_intrstd";
    String NOT_INTRSTD_LOW_BDGT = "not_intrstd_low_bdgt";
    String NOT_INTRSTD_LOC_ISSUE = "not_intrstd_loc_issue";
    String NOT_INTRSTD_OTHER = "not_intrstd_other";
    String SEND_MAIL = "send_mail";
    String JUST_ENQUIRY = "just_enquiry";
    String SITE_VISIT_DONE = "site_visit_done";
    String SITE_VISIT_NOT_DONE = "site_visit_not_done";
    String SITE_VISIT_CDL = "site_visit_cdl";
    //String SITE_VISIT_RECDL = "site_visit_recdl";
    String CALL_BACK = "callback";
    String TECHNICAL_ISSUE = "technical_issue";
    String ACTIVITY_ID = "activity_id";
    String ACTIVITY_NAME = "activity_name";
    String EMPLOYEE = "employee";
    String REMARK_DATE = "remark_date";
    String LAST_NOTIFIED_TIME = "last_notified_time";
    String IS_NOTIFIED = "is_notified";
    String IS_SNOOZE = "is_snooze";
    String SNOOZING_TIME = "snoozing_time";
    String SALES_PERSON_ID = "person_id";
    String SALES_PERSON_NAME = "person_name";
    String CAPACITY = "capacity";
    String REMAINING_CAPACITY = "remaining_capacity";
    String ASSIGN_TO_USER_NAME = "assign_to_user_name";
    String ASSIGN_TO_USER_ID = "assign_to_user_id";
    String ASSIGNED_TIME = "assigned_time";
    String EXTENDED_TIME = "extended_time";
    String CONTENT = "content";
    String ATTACHMENTS = "attachments"; //  attachment list json
    String TOTAL_ATTACHMENT = "total_attachment";
 	/*String SQL_CREATE_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS " + TableType.TBL_LOGIN.getTableName()+ "( 'id' INTEGER PRIMARY KEY  NOT NULL,"
			+ " " + USERID + " varchar,"
			+ " " + ENCODED_PWD + " varchar,"
			+ " " + USERNAME + " varchar,"
			+ " " + FIRSTNAME + " varchar,"
			+ " " + LASTNAME + " varchar,"
			+ " " + EMAIL + " varchar,"
			+ " " + CONTACT_NO + " varchar,"
			+ " " + DESIGNATION_ID + " varchar,"
			+ " " + DESIGNATION + " varchar);";
 	String SQL_CREATE_DASHBOARD_TABLE = "CREATE TABLE IF NOT EXISTS " + TableType.TBL_DASHBOARD.getTableName()+ "( 'id' INTEGER PRIMARY KEY  NOT NULL,"
			+ " " + USERID + " varchar,"
			+ " " + VALUE + " integer,"
			+ " " + LABEL + " varchar,"
			+ " " + ACTION + " integer,"
			+ " " + ISSYNC + " bool);";
 	String SQL_CREATE_ASM_DASHBOARD_TABLE = "CREATE TABLE IF NOT EXISTS " + TableType.TBL_ASM_DASHBOARD.getTableName()+ "( 'id' INTEGER PRIMARY KEY  NOT NULL,"
			+ " " + USERID + " varchar,"
			+ " " + VALUE + " integer,"
			+ " " + LABEL + " varchar,"
			+ " " + ACTION + " integer,"
			+ " " + ISSYNC + " bool);";
 // ================================================ //
 	String SQL_CREATE_ASSIGNED_LEAD_TABLE = "CREATE TABLE IF NOT EXISTS " + TableType.TBL_ASSIGNED_LEAD.getTableName()+ "( 'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ " " + USERID + " varchar,"
			+ " " + ENQUIRY_ID + " varchar,"
			+ " " + LEAD_ADDED_BY + " varchar,"
			+ " " + CLIENT_NAME + " varchar,"
			+ " " + CLIENT_NUMBER + " varchar,"
			+ " " + CLIENT_ADDRESS + " varchar,"
			+ " " + CURRENT_STATUS + " varchar,"
			+ " " + CURRENT_STATUS_ID + " varchar,"
			+ " " + SUB_STATUS_ID + " varchar,"
			+ " " + ACTION + " integer,"
			//+ " " + UPDATE_ACTION + " integer,"
			+ " " + ALTERNATE_NO + " varchar,"
			+ " " + EMAIL + " varchar,"
			+ " " + ADDRESS + " varchar,"
			+ " " + FOLLOW_UP_DATETIME + " varchar,"
			+ " " + PROJECT + " varchar,"
			+ " " + ASM + " varchar,"
			+ " " + REMARKS + " varchar,"
			+ " " + ASSIGN_TO_USER_ID + " varchar,"
			+ " " + ASSIGN_TO_USER_NAME + " varchar,"
			+ " " + LAST_UPDATE_TIME + " long,"
			+ " " + IS_ACCEPTED + " bool,"
			+ " " + IS_REJECTED + " bool,"
			+ " " + IS_OVERDUE + " bool,"
			+ " " + IS_UPDATED + " bool,"
			+ " " + IS_SELF_ASSIGNED + " bool,"
			+ " " + IS_TEAM_ASSIGNED + " bool,"
			+ " " + IS_EXTENDED + " bool,"
			+ " " + ISSYNC + " bool);";
 	String SQL_CREATE_ACTIVITY_TABLE = "CREATE TABLE IF NOT EXISTS " + TableType.TBL_ACTIVITY.getTableName()+ "( 'id' INTEGER PRIMARY KEY  NOT NULL,"
			+ " " + USERID + " varchar,"
			+ " " + ACTIVITY_ID + " varchar,"
			+ " " + ACTIVITY_NAME + " varchar,"
			+ " " + PROJECT + " varchar);"; //project json array as serialized data.
 	String SQL_CREATE_PROJECT_TABLE = "CREATE TABLE IF NOT EXISTS " + TableType.TBL_PROJECT.getTableName()+ "( 'id' INTEGER PRIMARY KEY  NOT NULL,"
			+ " " + USERID + " varchar,"
			+ " " + PROJECT_ID + " varchar,"
			+ " " + PROJECT_NAME + " varchar);";
 	String SQL_CREATE_ACTIVITY_LEAD_TABLE = "CREATE TABLE IF NOT EXISTS " + TableType.TBL_ACTIVITY_LEAD.getTableName()+ "( 'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ " " + USERID + " varchar,"
			+ " " + ENQUIRY_ID + " varchar,"
			+ " " + ACTIVITY_ID + " varchar,"
			+ " " + ACTIVITY_NAME + " varchar,"
			+ " " + CLIENT_NAME + " varchar,"
			+ " " + EMAIL + " varchar,"
			+ " " + CLIENT_NUMBER + " varchar,"
			+ " " + PROJECT_NAME + " varchar,"
			+ " " + ALTERNATE_NO + " varchar,"
			+ " " + CURRENT_STATUS + " varchar,"
			+ " " + LAST_UPDATE_TIME + " long,"
			+ " " + BUDGET + " varchar);";
 	String SQL_CREATE_HISTORY_TABLE = "CREATE TABLE IF NOT EXISTS " + TableType.TBL_HISTORY.getTableName()+ "( 'id' INTEGER PRIMARY KEY  NOT NULL,"
			+ " " + USERID + " varchar,"
			+ " " + ENQUIRY_ID + " varchar,"
			+ " " + HISTORY_ID + " varchar,"
			+ " " + LEAD_NUMBER + " varchar,"
			+ " " + CREATED_AT + " varchar,"
			+ " " + DETAILS + " varchar,"
			+ " " + TYPE + " varchar,"
			+ " " + META_DATA + " varchar);";
	String SQL_CREATE_COMMENT_HISTORY_TABLE = "CREATE TABLE IF NOT EXISTS " + TableType.TBL_COMMENT_HISTORY.getTableName()+ "( 'id' INTEGER PRIMARY KEY  NOT NULL,"
			+ " " + USERID + " varchar,"
			+ " " + ENQUIRY_ID + " varchar,"
			+ " " + EMPLOYEE + " varchar,"
			+ " " + REMARKS + " varchar,"
			+ " " + REMARK_DATE + " varchar);";
 	// ============================================== //
	String SQL_CREATE_STATUS_TABLE = "CREATE TABLE IF NOT EXISTS " + TableType.TBL_STATUS.getTableName()+ "( 'id' INTEGER PRIMARY KEY  NOT NULL,"
			+ " " + USERID + " varchar,"
			+ " " + STATUS_ID + " varchar,"
			+ " " + ACTIVITY_STATUS + " bool,"
			+ " " + STATUS_NAME + " varchar);";
 	String SQL_CREATE_SUB_STATUS_TABLE = "CREATE TABLE IF NOT EXISTS " + TableType.TBL_SUB_STATUS.getTableName()+ "( 'id' INTEGER PRIMARY KEY  NOT NULL,"
			+ " " + USERID + " varchar,"
			+ " " + STATUS_ID + " varchar,"
			+ " " + SUB_STATUS_ID + " varchar,"
			+ " " + ACTIVITY_STATUS + " bool,"
			+ " " + SUB_STATUS_NAME + " varchar);";
 // ================================================== //
 	String SQL_CREATE_ACCEPTED_LEAD_SYNC_TABLE = "CREATE TABLE IF NOT EXISTS " + TableType.TBL_ACCEPTED_LEAD_SYNC.getTableName()+ "( 'id' INTEGER PRIMARY KEY  NOT NULL,"
			+ " " + USERID + " varchar,"
			+ " " + ENQUIRY_ID + " varchar,"
			+ " " + ACCEPTED_TIME + " long,"
			+ " " + ISSYNC + " bool);";
 	String SQL_CREATE_REJECTED_LEAD_SYNC_TABLE = "CREATE TABLE IF NOT EXISTS " + TableType.TBL_REJECTED_LEAD_SYNC.getTableName()+ "( 'id' INTEGER PRIMARY KEY  NOT NULL,"
			+ " " + USERID + " varchar,"
			+ " " + ENQUIRY_ID + " varchar,"
			+ " " + REMARKS + " varchar,"
			+ " " + REJECTED_TIME + " long,"
			+ " " + ISSYNC + " bool);";
 	String SQL_CREATE_UPDATE_SYNC_TABLE = "CREATE TABLE IF NOT EXISTS " + TableType.TBL_UPDATE_SYNC.getTableName()+ "( 'id' INTEGER PRIMARY KEY  NOT NULL,"
			+ " " + USERID + " varchar,"
			+ " " + ENQUIRY_ID + " varchar,"
 			+ " " + CALL_BACK + " varchar,"
 			+ " " + NOT_INTRSTD_LOW_BDGT + " varchar,"
			+ " " + NOT_INTRSTD_LOC_ISSUE + " varchar,"
			+ " " + NOT_INTRSTD_OTHER + " varchar,"
 			+ " " + JUST_ENQUIRY + " varchar,"
 			+ " " + SEND_MAIL + " varchar,"
 			+ " " + MEETING_CDL + " varchar,"
			+ " " + MEETING_DONE + " varchar,"
			+ " " + MEETING_NOT_DONE + " varchar,"
 			+ " " + SITE_VISIT_CDL + " varchar,"
			+ " " + SITE_VISIT_NOT_DONE + " varchar,"
			+ " " + SITE_VISIT_DONE + " varchar,"
 			+ " " + NO_RESPONSE_CALLDIS + " varchar,"
			+ " " + NO_RESPONSE_NO_ANS + " varchar,"
			+ " " + NO_RESPONSE_NOT_REACH + " varchar,"
			+ " " + NO_RESPONSE_OTHER + " varchar,"
			+ " " + NO_RESPONSE_SWTCHOFF + " varchar,"
 			+ " " + TECHNICAL_ISSUE + " varchar,"
 			+ " " + CLOSURE_CHQ_CLTD + " blob,"
			+ " " + CLOSURE_LOGIN_COMPLETED + " varchar,"
			+ " " + CLOSURE_ONLINE_TRNS + " blob,"
 			*//*+ " " + FUTURE_REF_CALLBACK + " varchar,"
			+ " " + FUTURE_REF_COLDCALL + " varchar,"
			+ " " + FUTURE_REF_FOLLOWUP + " varchar,"*//*
     *//*+ " " + NOT_INTRSTD + " varchar,"*//*
 			+ " " + LAST_UPDATE_TIME + " long,"
			+ " " + ISSYNC + " bool);";
 	String SQL_CREATE_NOTIFICATION_TABLE = "CREATE TABLE IF NOT EXISTS " + TableType.TBL_NOTIFICATION.getTableName()+ "( 'id' INTEGER PRIMARY KEY  NOT NULL,"
			+ " " + USERID + " varchar,"
			+ " " + ENQUIRY_ID + " varchar,"
			+ " " + STATUS_ID + " varchar,"
			+ " " + SUB_STATUS_ID + " varchar,"
			+ " " + FOLLOW_UP_DATETIME + " varchar,"
			+ " " + LAST_NOTIFIED_TIME + " long,"
			+ " " + SNOOZING_TIME + " integer,"
			+ " " + IS_NOTIFIED + " bool,"
			+ " " + IS_SNOOZE + " bool);";
 	String SQL_CREATE_SALES_PERSON_TABLE = "CREATE TABLE IF NOT EXISTS " + TableType.TBL_SALES_PERSON.getTableName()+ "( 'id' INTEGER PRIMARY KEY  NOT NULL,"
			+ " " + USERID + " varchar,"
			+ " " + SALES_PERSON_ID + " varchar,"
			+ " " + SALES_PERSON_NAME + " varchar,"
			+ " " + REMAINING_CAPACITY + " integer,"
			+ " " + CAPACITY + " integer);";
 	String SQL_CREATE_ASSIGN_TO_SP_SYNC_TABLE = "CREATE TABLE IF NOT EXISTS " + TableType.TBL_ASSIGN_TO_SP_SYNC.getTableName()+ "( 'id' INTEGER PRIMARY KEY  NOT NULL,"
			+ " " + USERID + " varchar,"
			+ " " + ENQUIRY_ID + " varchar,"
			+ " " + SALES_PERSON_ID + " varchar,"
			+ " " + ASSIGNED_TIME + " long,"
			+ " " + ISSYNC + " bool);";
 	String SQL_CREATE_EXTENDED_SYNC_TABLE = "CREATE TABLE IF NOT EXISTS " + TableType.TBL_EXTENDED_SYNC.getTableName()+ "( 'id' INTEGER PRIMARY KEY  NOT NULL,"
			+ " " + USERID + " varchar,"
			+ " " + ENQUIRY_ID + " varchar,"
			+ " " + SALES_PERSON_ID + " varchar,"
			+ " " + EXTENDED_TIME + " long,"
			+ " " + ISSYNC + " bool);";
 	String SQL_CREATE_MAILER_PROJECT_TABLE = "CREATE TABLE IF NOT EXISTS " + TableType.TBL_MAILER_PROJECT.getTableName()+ "( 'id' INTEGER PRIMARY KEY  NOT NULL,"
			+ " " + USERID + " varchar,"
			+ " " + PROJECT_ID + " varchar,"
			+ " " + PROJECT_NAME + " varchar,"
			+ " " + CONTENT + " varchar,"
			+ " " + TOTAL_ATTACHMENT + " integer,"
			+ " " + ATTACHMENTS + " varchar);";
 //	***********************************************
//	Delete Queries
 	String SQL_DROP_LOGIN_TABLE = "DROP TABLE IF EXISTS " + TableType.TBL_LOGIN.getTableName();
	String SQL_DROP_DASHBOARD_TABLE = "DROP TABLE IF EXISTS " + TableType.TBL_DASHBOARD.getTableName();
	String SQL_DROP_ASM_DASHBOARD_TABLE = "DROP TABLE IF EXISTS " + TableType.TBL_ASM_DASHBOARD.getTableName();
 	String SQL_DROP_ASSIGNED_LEAD_TABLE = "DROP TABLE IF EXISTS " + TableType.TBL_ASSIGNED_LEAD.getTableName();
 	String SQL_DROP_HISTORY_TABLE = "DROP TABLE IF EXISTS " + TableType.TBL_HISTORY.getTableName();
	String SQL_DROP_COMMENT_HISTORY_TABLE = "DROP TABLE IF EXISTS " + TableType.TBL_COMMENT_HISTORY.getTableName();
 	String SQL_DROP_STATUS_TABLE = "DROP TABLE IF EXISTS " + TableType.TBL_STATUS.getTableName();
	String SQL_DROP_SUB_STATUS_TABLE = "DROP TABLE IF EXISTS " + TableType.TBL_SUB_STATUS.getTableName();
 	String SQL_DROP_ACCEPTED_SYNC_TABLE = "DROP TABLE IF EXISTS " + TableType.TBL_ACCEPTED_LEAD_SYNC.getTableName();
	String SQL_DROP_REJECTED_SYNC_TABLE = "DROP TABLE IF EXISTS " + TableType.TBL_REJECTED_LEAD_SYNC.getTableName();
	String SQL_DROP_UPDATE_SYNC_TABLE = "DROP TABLE IF EXISTS " + TableType.TBL_UPDATE_SYNC.getTableName();
 	String SQL_DROP_ACTIVITY_TABLE = "DROP TABLE IF EXISTS " + TableType.TBL_ACTIVITY.getTableName();
	String SQL_DROP_PROJECT_TABLE = "DROP TABLE IF EXISTS " + TableType.TBL_PROJECT.getTableName();
	String SQL_DROP_ACTIVITY_LEAD_TABLE = "DROP TABLE IF EXISTS " + TableType.TBL_ACTIVITY_LEAD.getTableName();
	String SQL_DROP_NOTIFICATION_TABLE = "DROP TABLE IF EXISTS " + TableType.TBL_NOTIFICATION.getTableName();
 	String SQL_DROP_SALES_PERSON_TABLE = "DROP TABLE IF EXISTS " + TableType.TBL_SALES_PERSON.getTableName();
 	String SQL_DROP_ASSIGN_TO_SP_SYNC_TABLE = "DROP TABLE IF EXISTS " + TableType.TBL_ASSIGN_TO_SP_SYNC.getTableName();
	String SQL_DROP_EXTENDED_SYNC_TABLE = "DROP TABLE IF EXISTS " + TableType.TBL_EXTENDED_SYNC.getTableName();
	String SQL_DROP_MAILER_PROJECT_TABLE = "DROP TABLE IF EXISTS " + TableType.TBL_MAILER_PROJECT.getTableName();*/

}
