package GUI.Home;

import java.awt.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Test.ExampleVector;
import Utill.MakePrettyInterface;
import Signal.Controller;
import voca.core.Word;
import GUI.Main.SideMenu;

public class HomeUI extends JPanel {

    private final SideMenu sideMenu;
    private final JPanel listContainer;
    private final JTextField searchField;
    private final Controller signalHandler;
    private Boolean firstFocus = false;
    // 단어 목록을 받아 홈 화면을 구성하는 생성자
    public HomeUI(Vector<Word> voca, Controller signalHandler) {
        this.signalHandler = signalHandler;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        sideMenu = new SideMenu(signalHandler);
        sideMenu.setVisible(false);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout(5, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        topPanel.setBackground(Color.WHITE);

        JButton menuBtn = sideMenu.getToggleButton();
        
        searchField = new JTextField(" 입력하세요");
        searchField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        MakePrettyInterface.setFixedSize(searchField, 400, 50);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                detectChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                detectChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                detectChange();
            }
            private void detectChange(){ 
                firstFocus = true;
                signalHandler.send(HomeSignal.CHANGE_TEXT_FIELD, searchField.getText());
            }
            
        });

        JButton searchBtn = new JButton("...");
        searchBtn.setFont(new Font("Arial", Font.BOLD, 30));
        MakePrettyInterface.setFixedSize(searchBtn, 50, 50);
        searchBtn.addActionListener(e -> sendSignal(HomeSignal.ADD_WORD, searchField.getText()));

        topPanel.add(menuBtn, BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(searchBtn, BorderLayout.EAST);

        listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(Color.WHITE);

        updateWords(voca);

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(new LineBorder(Color.GRAY, 1));

        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        add(sideMenu, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    // 단어 정보를 표시하는 리스트 행을 생성
    private JPanel createRowItem(Word word) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.white);

        panel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));


        String exText = word.getEx() == null ? "" : " - 예문 : " + word.getEx();
        String labelText = word.getEng() + " : " + String.join(", ", word.getKor())+exText;


        JLabel label = new JLabel(labelText);
        label.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        panel.add(label, BorderLayout.CENTER);

        JButton plusButton = new JButton("+");
        plusButton.setFont(new Font("Arial", Font.BOLD, 20));
        plusButton.setPreferredSize(new Dimension(30, 30));
        plusButton.setForeground(Color.WHITE);
        plusButton.setBackground(Color.BLACK);
        plusButton.setOpaque(true);
        plusButton.setBorder(null);
        plusButton.setFocusPainted(false);
        plusButton.addActionListener(e -> sendSignal(HomeSignal.FIX, word));

        panel.add(plusButton, BorderLayout.EAST);

        return panel;
    }

    // 사이드 메뉴 표시 여부를 설정
    public void setSideMenuVisible(boolean visible) {
        sideMenu.setVisible(visible);
        MakePrettyInterface.updateScreen(this);
    }

    // 단어 목록을 업데이트
    public void updateWords(Vector<Word> voca) {
        listContainer.removeAll();
        if (voca != null) {
            for (Word w : voca) {
                JPanel rowPanel = createRowItem(w);
                listContainer.add(rowPanel);
                listContainer.add(Box.createVerticalStrut(10));
            }
        }
        listContainer.add(Box.createVerticalGlue());
        listContainer.revalidate();
        listContainer.repaint();
    }

    private void sendSignal(HomeSignal signal, Object payload) {
        signalHandler.send(signal, payload);
    }

    // 사이드 메뉴 인스턴스를 반환
    public SideMenu getSideMenu() {
        return sideMenu;
    }

    public Object[] showAddWordDialogue() {
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("Label.background", Color.WHITE);

        JTextField newWordENGField = new JTextField(20);
        JTextField newWordKORField = new JTextField(20);
        JTextField newWordEXField = new JTextField(20);

        JLabel AddWordText = new JLabel("Add New Word");
        AddWordText.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        JPanel rootAddWordText = new JPanel(new GridBagLayout());
        rootAddWordText.add(AddWordText);
        MakePrettyInterface.makeWhite(rootAddWordText);

        JPanel basePanel = new JPanel(new GridBagLayout());
        MakePrettyInterface.makeWhite(basePanel);
        MakePrettyInterface.setFixedSize(basePanel, 450, 300);

        JPanel addWordPanel = new JPanel();
        addWordPanel.setLayout(new BoxLayout(addWordPanel, BoxLayout.Y_AXIS));
        MakePrettyInterface.makeWhite(addWordPanel);
        basePanel.add(addWordPanel);

        JLabel ENGText = new JLabel("New Word ");
        ENGText.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        JPanel ENGFlow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        MakePrettyInterface.makeWhite(ENGFlow);
        MakePrettyInterface.makeShadow(newWordENGField, false);
        ENGFlow.add(ENGText);
        ENGFlow.add(newWordENGField);

        JLabel KORText = new JLabel("New Meaning ");
        KORText.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        JPanel KORFlow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        MakePrettyInterface.makeWhite(KORFlow);
        MakePrettyInterface.makeShadow(newWordENGField, false);
        KORFlow.add(KORText);
        KORFlow.add(newWordKORField);

        JLabel EXText = new JLabel("New Example ");
        EXText.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        JPanel EXFlow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        MakePrettyInterface.makeWhite(EXFlow);
        MakePrettyInterface.makeShadow(newWordENGField, false);
        EXFlow.add(EXText);
        EXFlow.add(newWordEXField);

        JButton btnAdd = new JButton("추가");
        MakePrettyInterface.setFixedSize(btnAdd, 50, 20);
        btnAdd.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        JButton btnCancel = new JButton("취소");
        MakePrettyInterface.setFixedSize(btnCancel, 50, 20);
        btnCancel.setFont(new Font("맑은 고딕", Font.BOLD, 12));

        btnAdd.setBackground(Color.WHITE);
        btnCancel.setBackground(Color.WHITE);
        MakePrettyInterface.makeShadow(btnAdd, false);
        MakePrettyInterface.makeShadow(btnCancel, false);
        btnAdd.setFocusPainted(false);
        btnCancel.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        MakePrettyInterface.makeWhite(buttonPanel);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnCancel);

        addWordPanel.add(rootAddWordText);
        addWordPanel.add(Box.createVerticalStrut(30));
        addWordPanel.add(ENGFlow);
        addWordPanel.add(Box.createVerticalStrut(5));
        addWordPanel.add(KORFlow);
        addWordPanel.add(Box.createVerticalStrut(5));
        addWordPanel.add(EXFlow);
        addWordPanel.add(Box.createVerticalStrut(10));
        addWordPanel.add(buttonPanel);

        final int[] resultState = {JOptionPane.CANCEL_OPTION};

        btnAdd.addActionListener(e -> {
            resultState[0] = JOptionPane.OK_OPTION;
            Window w = SwingUtilities.getWindowAncestor(btnAdd);
            if (w != null) w.dispose();
        });

        btnCancel.addActionListener(e -> {
            resultState[0] = JOptionPane.CANCEL_OPTION;
            Window w = SwingUtilities.getWindowAncestor(btnCancel);
            if (w != null) w.dispose();
        });

        JOptionPane.showOptionDialog(
                this,
                basePanel,
                "Add Word",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{},
                null
        );

        return new Object[]{newWordENGField, newWordKORField,newWordEXField, resultState[0]};
    }

    public Object[] showAddDialogue(Word data){

        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("Label.background", Color.WHITE);

        JTextField newWordENGField = new JTextField(20);
        newWordENGField.setText(data.getEng());
        JTextField newWordKORField = new JTextField(20);
        String result = String.join(", ", data.getKor());
        newWordKORField.setText(result);
        JTextField newWordEXField = new JTextField(20);
        newWordEXField.setText(data.getEx());

        JLabel AddWordText = new JLabel("Fix Word");
        AddWordText.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        JPanel rootAddWordText = new JPanel(new GridBagLayout());
        rootAddWordText.add(AddWordText);
        MakePrettyInterface.makeWhite(rootAddWordText);

        JPanel basePanel = new JPanel(new GridBagLayout());
        MakePrettyInterface.makeWhite(basePanel);
        MakePrettyInterface.setFixedSize(basePanel, 450, 300);

        JPanel addWordPanel = new JPanel();
        addWordPanel.setLayout(new BoxLayout(addWordPanel, BoxLayout.Y_AXIS));
        MakePrettyInterface.makeWhite(addWordPanel);
        basePanel.add(addWordPanel);

        JLabel ENGText = new JLabel("Word ");
        ENGText.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        JPanel ENGFlow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        MakePrettyInterface.makeWhite(ENGFlow);
        MakePrettyInterface.makeShadow(newWordENGField, false);
        ENGFlow.add(ENGText);
        ENGFlow.add(newWordENGField);

        JLabel KORText = new JLabel("Meaning ");
        KORText.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        JPanel KORFlow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        MakePrettyInterface.makeWhite(KORFlow);
        MakePrettyInterface.makeShadow(newWordENGField, false);
        KORFlow.add(KORText);
        KORFlow.add(newWordKORField);

        JLabel EXText = new JLabel("Example ");
        EXText.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        JPanel EXFlow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        MakePrettyInterface.makeWhite(EXFlow);
        MakePrettyInterface.makeShadow(newWordENGField, false);
        EXFlow.add(EXText);
        EXFlow.add(newWordEXField);

        JButton btnAdd = new JButton("변경");
        MakePrettyInterface.setFixedSize(btnAdd, 50, 20);
        btnAdd.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        JButton btnRemove = new JButton("삭제");
        MakePrettyInterface.setFixedSize(btnRemove, 50, 20);
        btnRemove.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        JButton btnCancel = new JButton("취소");
        MakePrettyInterface.setFixedSize(btnCancel, 50, 20);
        btnCancel.setFont(new Font("맑은 고딕", Font.BOLD, 12));


        btnAdd.setBackground(Color.WHITE);
        btnRemove.setBackground(Color.WHITE);
        btnCancel.setBackground(Color.WHITE);
        MakePrettyInterface.makeShadow(btnAdd, false);
        MakePrettyInterface.makeShadow(btnRemove, false);
        MakePrettyInterface.makeShadow(btnCancel, false);
        btnAdd.setFocusPainted(false);
        btnRemove.setFocusPainted(false);
        btnCancel.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        MakePrettyInterface.makeWhite(buttonPanel);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnRemove);
        buttonPanel.add(btnCancel);

        addWordPanel.add(rootAddWordText);
        addWordPanel.add(Box.createVerticalStrut(30));
        addWordPanel.add(ENGFlow);
        addWordPanel.add(Box.createVerticalStrut(5));
        addWordPanel.add(KORFlow);
        addWordPanel.add(Box.createVerticalStrut(5));
        addWordPanel.add(EXFlow);
        addWordPanel.add(Box.createVerticalStrut(10));
        addWordPanel.add(buttonPanel);

        final int[] resultState = {JOptionPane.CANCEL_OPTION};

        btnAdd.addActionListener(e -> {
            resultState[0] = JOptionPane.OK_OPTION;
            Window w = SwingUtilities.getWindowAncestor(btnAdd);
            if (w != null) w.dispose();
        });

        btnRemove.addActionListener(e -> {
            resultState[0] = JOptionPane.NO_OPTION;
            Window w = SwingUtilities.getWindowAncestor(btnRemove);
            if (w != null) w.dispose();
        });

        btnCancel.addActionListener(e -> {
            resultState[0] = JOptionPane.CANCEL_OPTION;
            Window w = SwingUtilities.getWindowAncestor(btnCancel);
            if (w != null) w.dispose();
        });

        JOptionPane.showOptionDialog(
                this,
                basePanel,
                "Add Word",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{},
                null
        );
        return new Object[]{newWordENGField, newWordKORField, newWordEXField, resultState[0],data};
    }

    public Object[] showRemoveDialogue(Word data){
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("Label.background", Color.WHITE);

        JLabel RemoveText = new JLabel("Remove Word");
        RemoveText.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        JPanel rootAddWordText = new JPanel(new GridBagLayout());
        rootAddWordText.add(RemoveText);
        MakePrettyInterface.makeWhite(rootAddWordText);

        JLabel QText = new JLabel("다음 단어를 진짜 삭제하시겠습니까?");
        QText.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        JPanel QFlow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        MakePrettyInterface.makeWhite(QFlow);
        QFlow.add(QText);

        JLabel WordText = new JLabel(data.toString());
        WordText.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        JPanel WordFlow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        MakePrettyInterface.makeWhite(WordFlow);
        WordFlow.add(WordText);

        JButton btnRemove = new JButton("삭제");
        MakePrettyInterface.setFixedSize(btnRemove, 50, 20);
        btnRemove.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        JButton btnCancel = new JButton("취소");
        MakePrettyInterface.setFixedSize(btnCancel, 50, 20);
        btnCancel.setFont(new Font("맑은 고딕", Font.BOLD, 12));

        btnRemove.setBackground(Color.WHITE);
        btnCancel.setBackground(Color.WHITE);
        MakePrettyInterface.makeShadow(btnRemove, false);
        MakePrettyInterface.makeShadow(btnCancel, false);
        btnRemove.setFocusPainted(false);
        btnCancel.setFocusPainted(false);

        final int[] resultState = {JOptionPane.CANCEL_OPTION};

        btnRemove.addActionListener(e -> {
            resultState[0] = JOptionPane.OK_OPTION;
            Window w = SwingUtilities.getWindowAncestor(btnRemove);
            if (w != null) w.dispose();
        });

        btnCancel.addActionListener(e -> {
            resultState[0] = JOptionPane.CANCEL_OPTION;
            Window w = SwingUtilities.getWindowAncestor(btnCancel);
            if (w != null) w.dispose();
        });


        JPanel basePanel = new JPanel(new GridBagLayout());
        MakePrettyInterface.makeWhite(basePanel);
        MakePrettyInterface.setFixedSize(basePanel, 350, 200);

        JPanel addWordPanel = new JPanel();
        addWordPanel.setLayout(new BoxLayout(addWordPanel, BoxLayout.Y_AXIS));
        MakePrettyInterface.makeWhite(addWordPanel);
        basePanel.add(addWordPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        MakePrettyInterface.makeWhite(buttonPanel);
        buttonPanel.add(btnRemove);
        buttonPanel.add(btnCancel);

        addWordPanel.add(rootAddWordText);
        addWordPanel.add(Box.createVerticalStrut(30));
        addWordPanel.add(QFlow);
        addWordPanel.add(Box.createVerticalStrut(5));
        addWordPanel.add(WordFlow);
        addWordPanel.add(buttonPanel);

        JOptionPane.showOptionDialog(
                this,
                basePanel,
                "Remove",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{},
                null
        );

        return new Object[]{resultState[0],data};

    }

    // 홈 화면을 확인하기 위한 테스트 메인
    public static void main(String[] args) {
        ExampleVector v = new ExampleVector();
        HomeController homeController = new HomeController(v.voca);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 800);
        frame.add(homeController.getView());
        frame.setVisible(true);
    }
}
