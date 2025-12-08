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
            case FIX:
                if(payload instanceof Word word){
                    handleAdd(word);
                }
                break;
            default:
                break;
        }
    }



    private void toggleMenu() {
        menuVisible = !menuVisible;
        homeUI.setSideMenuVisible(menuVisible);
    }

    private void handleAddWord(Object payload) {
        Object[] inputData = homeUI.showAddWordDialogue();
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
                    JOptionPane.showMessageDialog(homeUI, "단어 추가 실패 : 동일 단어가 존재합니다.");
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

    private void handleAdd(Word word) {
        Object[] inputData = homeUI.showAddDialogue(word);
        handleAddJudgement(inputData);
    }
    private void handleAddJudgement(Object[] data) {
        JTextField newENGField = (JTextField) data[0];
        JTextField newKORField = (JTextField) data[1];
        JTextField newEXField = (JTextField) data[2];
        int result = (int) data[3];
        Word word = (Word) data[4];

        String eng = newENGField.getText().trim();
        String kor = newKORField.getText();
        String ex = newEXField.getText();

        if (result == JOptionPane.OK_OPTION) {
            if(vocabulary.isEmpty()){
                JOptionPane.showMessageDialog(homeUI, "단어 변경 실패 : 단어장이 비어있습니다.");
                return;
            }
            if (eng == null || kor == null) {
                JOptionPane.showMessageDialog(homeUI, "단어 변경 실패 : 아예 비어있습니다.");
                return;
            }
            if (!eng.isEmpty()) {
                word.setEng(eng);
            }

            if (!kor.isEmpty()) {
                word.setKor(wordManagement.splitKor(kor));
            }

            if (ex.isEmpty()) {
                word.setEx("");
            } else {
                word.setEx(ex);
            }

            homeUI.updateWords(vocabulary);
            if (userFileInfo != null) {
                FileManagement.saveVoca(vocabulary, userFileInfo.getVocaFilePath());
            } else {
                JOptionPane.showMessageDialog(homeUI, "사용자 정보가 없어 파일로 저장하지 못했습니다.");
            }
            JOptionPane.showMessageDialog(homeUI, "단어 변경 성공 : " + word);
        }

        if (result == JOptionPane.NO_OPTION) {
            if (vocabulary.isEmpty()) {
                JOptionPane.showMessageDialog(homeUI, "단어 삭제 실패 : 단어장이 비어있습니다.");
                return;
            }

            if (word == null) {
                return;
            }
            Object[] inputData = homeUI.showRemoveDialogue(word);
            handleAddRemoveJudgement(inputData);
        }
    }

    private void handleAddRemoveJudgement(Object[] data) {
        int result = (int) data[0];
        Word word = (Word) data[1];
        if(result == JOptionPane.OK_OPTION){
            vocabulary.remove(word);

            homeUI.updateWords(vocabulary);
            if (userFileInfo != null) {
                FileManagement.saveVoca(vocabulary, userFileInfo.getVocaFilePath());
            } else {
                JOptionPane.showMessageDialog(homeUI, "사용자 정보가 없어 파일로 저장하지 못했습니다.");
            }
            JOptionPane.showMessageDialog(homeUI, "단어 삭제 성공 : "+word.getEng()+"를 삭제했습니다");
        }
    }


    public void updateVocabulary(Vector<Word> newVocabulary) {
        this.vocabulary = new Vector<>(newVocabulary);
        homeUI.updateWords(newVocabulary);
    }

    public void updateUserFileInfo(UserFileInfo newUserFileInfo) {
        this.userFileInfo = newUserFileInfo;
    }
}
