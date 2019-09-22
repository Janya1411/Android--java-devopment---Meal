package app.android.meal;

public class Delegates {
    private String Country;
    private String Name;

    public Delegates() {
    }

    public Delegates(String country, String name) {
        Country = country;
        Name = name;
    }

    public String getCountry() {
        return Country;
    }

    public String getName() {
        return Name;
    }
}
