package com.example.stepnote;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "StepNoteSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    /**
     * Creates a login session for the user.
     * @param name The user's name.
     * @param email The user's email.
     */
    public void createLoginSession(String name, String email) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.commit();
    }

    /**
     * Gets the logged-in user's name.
     * @return User's name, or a default value if not found.
     */
    public String getUserName() {
        return pref.getString(KEY_USER_NAME, "User");
    }

    /**
     * Gets the logged-in user's email.
     * @return User's email, or a default value if not found.
     */
    public String getUserEmail() {
        return pref.getString(KEY_USER_EMAIL, "user@example.com");
    }

    /**
     * Clears session details.
     */
    public void logoutUser() {
        editor.clear();
        editor.commit();
    }

    /**
     * Checks if the user is logged in.
     * @return true if logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}
