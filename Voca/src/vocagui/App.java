package vocagui;

import voca.auth.LogInManagement;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args){
        SwingUtilities.invokeLater(BaseApp::new);
    }
}
