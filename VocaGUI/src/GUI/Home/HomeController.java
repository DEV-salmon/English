package GUI.Home;

import java.awt.*;
import java.util.Locale;
import java.util.Scanner;
import java.util.Vector;
import java.util.stream.Collectors;

import GUI.Main.GlobalSignal;
import Signal.Controller;
import Signal.Signal;
import Utill.MakePrettyInterface;
import voca.core.UserFileInfo;
import voca.core.Word;
import voca.management.FileManagement;
import voca.management.WordManagement;

import javax.swing.*;

public class HomeController implements Controller {
    private final HomeUI homeUI;
    private Vector<Word> vocabulary;
    private boolean menuVisible;
    private final Controller globalHandler;
    // wordManagement 이용을 위한 scanner 사용안함
    //임시로 해놓음
    private final Scanner scanner = new Scanner(System.in);
    private final WordManagement wordManagement;
    private UserFileInfo userFileInfo;

    public HomeController() {
        this(new Vector<>(), null);
    }

    public HomeController(Vector<Word> vocabulary) {
        this(vocabulary, null);
    }

    public HomeController(Vector<Word> vocabulary, Controller globalHandler) {
        this.vocabulary = vocabulary == null ? new Vector<>() : new Vector<>(vocabulary);
        this.globalHandler = globalHandler;
        this.homeUI = new HomeUI(new Vector<>(this.vocabulary), this::send);
        this.homeUI.setSideMenuVisible(menuVisible);
        this.wordManagement = new WordManagement(scanner, this.vocabulary);
    }

    public HomeUI getView() {
        return homeUI;
    }

    @Override
    public void send(Signal signal, Object payload) {
        if (!(signal instanceof HomeSignal homeSignal)) {
            if (globalHandler != null && signal instanceof GlobalSignal) {
                globalHandler.send(signal, payload);
            }
            return;
        }

        switch (homeSignal) {
            case TOGGLE_MENU:
                toggleMenu();
                break;
            case CHANGE_TEXT_FIELD:
                handleSearch(payload);
                break;
            case ADD_WORD:
                handleAddWord(payload);
            default:
                break;
        }
    }

    private void toggleMenu() {
        menuVisible = !menuVisible;
        homeUI.setSideMenuVisible(menuVisible);
    }

    private void handleAddWord(Object payload) {
        Object[] inputData = showAddWordDialogue();
        handleAddWordJudgement(inputData);
    }

    private void handleAddWordJudgement(Object[] data) {
        JTextField newENGField = (JTextField) data[0];
        JTextField newKORField = (JTextField) data[1];
        JTextField newEXField = (JTextField) data[2];
        int result = (int) data[3];

        String eng = newENGField.getText().trim();
        String kor = newKORField.getText();
        String ex = newEXField.getText();

        if (result == JOptionPane.OK_OPTION) {
            for (Word word : vocabulary) {
                if (word.getEng().equalsIgnoreCase(eng)) {
                    JOptionPane.showMessageDialog(homeUI, "단어 추가 실패 : 동일 단어");
                    return;
                }
            }

            if (eng.isEmpty()||kor.isEmpty()) {
                JOptionPane.showMessageDialog(homeUI, "단어 추가 실패 : 단어나 뜻 중 하나가 적혀있지 않습니다.");
                return;
            }

            String[] korArray = wordManagement.splitKor(kor);

            if (ex.isEmpty()) {
                vocabulary.add(new Word(eng, korArray));
            } else {
                vocabulary.add(new Word(eng, korArray, ex));
            }
            homeUI.updateWords(vocabulary);
            if (userFileInfo != null) {
                FileManagement.saveVoca(vocabulary, userFileInfo.getVocaFilePath());
            } else {
                JOptionPane.showMessageDialog(homeUI, "사용자 정보가 없어 파일로 저장하지 못했습니다.");
            }
            JOptionPane.showMessageDialog(homeUI, "단어 추가 성공");
        }
    }

    private Object[] showAddWordDialogue() {
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
                homeUI,
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

    private void handleSearch(Object payload) {
        String query = payload == null ? "" : payload.toString();
        String normalized = query.trim().toLowerCase(Locale.ROOT);
        if (normalized.isEmpty()) {
            homeUI.updateWords(new Vector<>(vocabulary));
            return;
        }

        Vector<Word> filtered = vocabulary.stream()
                .filter(word -> matches(word, normalized))
                .collect(Collectors.toCollection(Vector::new));
        homeUI.updateWords(filtered);
    }

    private boolean matches(Word word, String query) {
        if (word == null) {
            return false;
        }

        if (word.getEng() != null && word.getEng().toLowerCase(Locale.ROOT).contains(query)) {
            return true;
        }

        for (String kor : word.getKor()) {
            if (kor.toLowerCase(Locale.ROOT).contains(query)) {
                return true;
            }
        }
        return false;
    }

    public void updateVocabulary(Vector<Word> newVocabulary) {
        this.vocabulary = new Vector<>(newVocabulary);
        homeUI.updateWords(newVocabulary);
    }

    public void updateUserFileInfo(UserFileInfo newUserFileInfo) {
        this.userFileInfo = newUserFileInfo;
    }
}
