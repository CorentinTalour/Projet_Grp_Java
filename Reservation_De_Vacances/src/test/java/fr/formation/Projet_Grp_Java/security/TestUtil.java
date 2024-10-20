package fr.formation.Projet_Grp_Java.security;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtil {
    private TestUtil() {
    }

    public static String json(Object value) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(value);
        } catch (Exception ex) {
            return "";
        }
    }
}