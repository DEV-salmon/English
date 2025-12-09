package GUI.File;

import GUI.Main.SideMenu;
import Signal.Controller;
import Utill.MakePrettyInterface;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;

public class FileUI extends JPanel {
    private final Controller signalHandler;
    private final SideMenu sideMenu;
    private final JLabel fileText;
    private final JButton fileMerge;
    private final JButton fileLoad;
    private Font btnFont = new Font("맑은 고딕",Font.BOLD,12);

    public FileUI(Controller signalHandler) {
        this.signalHandler = signalHandler;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setOpaque(true);

        sideMenu = new SideMenu(signalHandler);
        sideMenu.setVisible(false);
        JPanel contentPanel = new JPanel(new BorderLayout());
        MakePrettyInterface.makeWhite(contentPanel);

        JPanel btns = new JPanel(new GridLayout(2,1,50,50));
        MakePrettyInterface.makeWhite(btns);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(new LineBorder(Color.BLACK,1, true));
        MakePrettyInterface.makeWhite(topPanel);
        topPanel.setBackground(Color.WHITE);
        topPanel.setOpaque(true);
        fileText = new JLabel("File Mode");
        fileText.setFont(new Font("맑은 고딕", Font.BOLD, 40));

        JPanel centerBasePanel = new JPanel(new GridBagLayout());
        MakePrettyInterface.makeWhite(centerBasePanel);
        JPanel centerPanel = new JPanel();
        MakePrettyInterface.makeWhite(centerPanel);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JPanel centerTopPanel = new JPanel(new GridBagLayout());
        MakePrettyInterface.makeWhite(centerTopPanel);

        //메뉴 버튼 만들기
        JButton menuBtn = sideMenu.getToggleButton();

        //하단 파일 버튼
        fileMerge = new JButton("파일 병합하기");
        btnSetting(fileMerge);
        fileMerge.addActionListener(e -> signalHandler.send(FileSignal.FILE_MERGE, null));
        fileLoad = new JButton("파일 불러오기");
        btnSetting(fileLoad);
        fileLoad.addActionListener(e -> signalHandler.send(FileSignal.FILE_LOAD, null));


        add(sideMenu, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        contentPanel.add(topPanel,BorderLayout.NORTH);
        topPanel.add(menuBtn);
        contentPanel.add(centerBasePanel, BorderLayout.CENTER);
        centerBasePanel.add(centerPanel);
        centerPanel.add(centerTopPanel);
        centerTopPanel.add(fileText);

        centerPanel.add(Box.createRigidArea(new Dimension(0,70)));

        centerPanel.add(btns);
        btns.add(fileMerge);
        btns.add(fileLoad);

    }


    private void btnSetting(JButton btn){
        btn.setFont(btnFont);
        MakePrettyInterface.setFixedSize(btn, 200, 70);
        btn.setBackground(Color.WHITE);
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(Color.BLACK, 1, true));
        MakePrettyInterface.makeShadow(btn);
    }

    public void setSideMenuVisible(boolean visible){
        sideMenu.setVisible(visible);
        MakePrettyInterface.updateScreen(this);
    }

    public FileMergeDialogResult showFileMergeDialogue(String currentPath) {
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("Label.background", Color.WHITE);


        JLabel MergeText = new JLabel("Merge File");
        MergeText.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        JPanel rootMergeText = new JPanel(new GridBagLayout());
        rootMergeText.add(MergeText);
        MakePrettyInterface.makeWhite(rootMergeText);

        JPanel basePanel = new JPanel(new GridBagLayout());
        MakePrettyInterface.makeWhite(basePanel);
        MakePrettyInterface.setFixedSize(basePanel, 450, 300);

        JPanel addWordPanel = new JPanel();
        addWordPanel.setLayout(new BoxLayout(addWordPanel, BoxLayout.Y_AXIS));
        MakePrettyInterface.makeWhite(addWordPanel);
        basePanel.add(addWordPanel);

        JLabel PathText = new JLabel("Absolute Path ");
        PathText.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        JTextField newFileField = new JTextField(20);
        newFileField.setText(currentPath);
        JPanel PathFlow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        MakePrettyInterface.makeWhite(PathFlow);
        MakePrettyInterface.makeShadow(newFileField, false);
        JButton btnFile = new JButton("파일 찾기");
        MakePrettyInterface.setFixedSize(btnFile, 50, 20);
        btnFile.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        btnFile.setBackground(Color.WHITE);
        btnFile.setFocusPainted(false);
        MakePrettyInterface.makeShadow(btnFile, false);
        PathFlow.add(PathText);
        PathFlow.add(newFileField);
        PathFlow.add(btnFile);

        JLabel PolicyText = new JLabel("중복 단어는 자동으로 건너뜁니다.");
        PolicyText.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        JPanel PolicyFlow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        MakePrettyInterface.makeWhite(PolicyFlow);
        PolicyFlow.add(PolicyText);


        JButton btnMerge = new JButton("병합");
        MakePrettyInterface.setFixedSize(btnMerge, 50, 20);
        btnMerge.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        JButton btnCancel = new JButton("취소");
        MakePrettyInterface.setFixedSize(btnCancel, 50, 20);
        btnCancel.setFont(new Font("맑은 고딕", Font.BOLD, 12));

        btnMerge.setBackground(Color.WHITE);
        btnCancel.setBackground(Color.WHITE);
        MakePrettyInterface.makeShadow(btnMerge, false);
        MakePrettyInterface.makeShadow(btnCancel, false);
        btnMerge.setFocusPainted(false);
        btnCancel.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        MakePrettyInterface.makeWhite(buttonPanel);
        buttonPanel.add(btnMerge);
        buttonPanel.add(btnCancel);

        addWordPanel.add(rootMergeText);
        addWordPanel.add(Box.createVerticalStrut(30));
        addWordPanel.add(PathFlow);
        addWordPanel.add(Box.createVerticalStrut(10));
        addWordPanel.add(PolicyFlow);
        addWordPanel.add(Box.createVerticalStrut(10));
        addWordPanel.add(buttonPanel);

        final int[] resultState = {JOptionPane.CANCEL_OPTION};

        btnMerge.addActionListener(e -> {
            resultState[0] = JOptionPane.OK_OPTION;
            Window w = SwingUtilities.getWindowAncestor(btnMerge);
            if (w != null) w.dispose();
        });

        btnCancel.addActionListener(e -> {
            resultState[0] = JOptionPane.CANCEL_OPTION;
            Window w = SwingUtilities.getWindowAncestor(btnCancel);
            if (w != null) w.dispose();
        });

        btnFile.addActionListener(e -> {
            File chosen = showFileChooser();
            if (chosen != null) {
                newFileField.setText(chosen.getAbsolutePath());
            }
        });

        JOptionPane.showOptionDialog(
                this,
                basePanel,
                "Merge File",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{},
                null
        );

        return new FileMergeDialogResult(newFileField.getText(), resultState[0]);
    }

    public File showFileChooser(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose File");

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return new File(selectedFile.getAbsolutePath());
        }
        return null;
    }


    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public static class FileMergeDialogResult {
        private final String path;
        private final int result;

        public FileMergeDialogResult(String path, int result) {
            this.path = path;
            this.result = result;
        }

        public String getPath() {
            return path;
        }

        public int getResult() {
            return result;
        }
    }

}
