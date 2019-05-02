package logic;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

class ConfigStoreTest {

    @Test
    void test() throws FileNotFoundException, IllegalAccessException, ClassNotFoundException {
        ConfigStore configStore = ConfigStore.getInstance();

        /*for (Field field : configStore.getClass().getFields()){
            System.out.println(field.get(configStore));

        }*/
    }

    @Test
    void setCurrentAsDefault() throws FileNotFoundException, IllegalAccessException, ClassNotFoundException {
        ConfigStore.getInstance().minY = 4;
        ConfigStore.getInstance().saveCurrent();
    }
}