package karma.convertor.utils

/**
 * Here in this class there are constants for the preference keys, argument keys and any other constant used in the
 * application except the api constants which resides in the APIConstants.
 *
 * The Preference and Arguments key's value type is appended in the key name itself.
 * If it is integer key contains _INT at end.
 * If it is boolean key contains _BOOL at end.
 * If it is float key contains _FLOAT at end.
 * If it is long key contains _LONG at end.
 * Other than this every single key's value is String data type.
 */
object Constants {
    const val PUSH_NOTIFICATION_CHANNEL_ID = "PUSH_NOTIFICATION_JEEWAN_JYOTI"
    const val ADD_MOB_ADD_ID = "ca-app-pub-1829431093631944~1902403058"


    const val IMAGE_TYPE_CAMERA = 0
    const val IMAGE_TYPE_GALLERY = 1

    const val API_OFFSET_ITEM = 15
    const val DEFAULT_CURRENCY = "₹"

    //region Preference Constants
    const val PREF_FIREBASE_TOKEN = "PREF_FIREBASE_TOKEN"
    const val PREF_BEARER_TOKEN = "PREF_BEARER_TOKEN"
    const val PREF_UPDATE_SKIPPED_VERSION = "PREF_UPDATE_SKIPPED_VERSION"

    const val PREF_USER_ID_INT = "PREF_USER_ID"
    const val PREF_USER_SLUG = "PREF_USER_SLUG"
    const val PREF_USER_FIRST_NAME = "PREF_USER_FIRST_NAME"
    const val PREF_USER_MIDDLE_NAME = "PREF_USER_MIDDLE_NAME"
    const val PREF_USER_LAST_NAME = "PREF_USER_LAST_NAME"
    const val PREF_USER_PHONE_NUMBER = "PREF_USER_PHONE_NUMBER"
    const val PREF_USER_EMAIL = "PREF_USER_EMAIL"
    const val PREF_USER_PROFILE_IMAGE = "PREF_USER_PROFILE_IMAGE"
    const val PREF_LOGIN_DATA = "PREF_LOGIN_DATA"
    const val PREF_IS_LANGUAGE_SELECTED = "PREF_IS_LANGUAGE_SELECTED"
    const val PREF_IS_INTRODUCTION_FINISHED_BOOL = "PREF_IS_INTRODUCTION_FINISHED"
    //endregion

    //region EXTRAS
    const val EXTRA_COMING_FROM_INT = "COMING_FROM"
    const val EXTRA_ACTIVITY_RESULT_REQUEST_CODE = 101
    const val EXTRA_CAMERA_REQUEST_CODE = 102
    const val EXTRA_GALLERY_REQUEST_CODE = 103
    const val EXTRA_LOCATION_REQUEST_CODE = 104

    const val EXTRA_BULLETIN_MODEL = "EXTRA_BULLETIN_MODEL"
    const val EXTRA_JOB_MODEL = "EXTRA_JOB_MODEL"
    const val EXTRA_IS_VIDEO = "EXTRA_JOB_MODEL"
    const val EXTRA_IS_FROM_MORE = "EXTRA_IS_FROM_MORE"
    const val EXTRA_CATEGORY_TYPE = "CATEGORY_TYPE"
    const val EXTRA_TITLE = "WEB_TITLE"
    const val EXTRA_LINK = "WEB_LINK"
    const val EXTRA_COUNTRY = "EXTRA_COUNTRY"

    //endregion

    //region CMS Type
    const val PRIVACY_POLICY = 1
    const val TERMS_CONDITION = 2
    const val ABOUT_US = 3
    //

    //region Language Type
    const val LANGUAGE_MARATHI = 1
    const val LANGUAGE_HINDI = 2
    const val LANGUAGE_ENGLISH = 3
    //endregion

    // category ids
    const val CATEGORY_BULLETINES = 1
    const val CATEGORY_COURSES = 2
    const val CATEGORY_JOBS = 3
    const val CATEGORY_BUSINESS_IDEA = 4
    const val CATEGORY_ALUMNI = 5
    const val CATEGORY_DASHBOARD = 6

    const val PICK_IMAGE_REQUESTCODE = 1003
    const val PICK_IMAGE_FROM_CAMERA = 1004
    const val PICK_IMAGE_FROM_GALLERY = 1005
    const val REQUESTCODE_SELECT_STATE = 1006
    const val REQUESTCODE_SELECT_CITY = 1007

    const val MEMBER_SHG = "1"
    const val MEMBER_ALUMNI = "2"
    const val MEMBER_TRAINEE = "3"
    const val MEMBER_CURRENT_STUDENT = "4"
    const val MEMBER_NONE = "5"
}