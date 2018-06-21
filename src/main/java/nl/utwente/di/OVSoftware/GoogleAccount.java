package nl.utwente.di.OVSoftware;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GoogleAccount {
    private final String email;

    public String getEmail() {
        return email;
    }

    @JsonCreator
    public GoogleAccount (@JsonProperty("email") String email){
        this.email = email;
    }
}
