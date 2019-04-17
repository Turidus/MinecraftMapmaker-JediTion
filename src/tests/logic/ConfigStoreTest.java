package tests.logic;

import logic.ConfigStore;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class ConfigStoreTest {

    @Test
    void test() throws FileNotFoundException, IllegalAccessException {
        ConfigStore configStore = ConfigStore.getInstance();

        for (Field field : configStore.getClass().getFields()){
            System.out.println(field.get(configStore));

        }

    }

}