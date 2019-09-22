package app.android.meal;

public class User {
    public String UID;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uid) {
        this.UID = uid;
    }
}
