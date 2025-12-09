package voca.app;

import voca.auth.LogInManagement;

public class TestMain {

    public static void main(String[] args) throws Exception {  
        LogInManagement login = new LogInManagement("VocaGUI/res/Loginlist");
        login.run();
    }
}
