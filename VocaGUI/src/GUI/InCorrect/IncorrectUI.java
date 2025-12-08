package GUI.InCorrect;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;

import GUI.Main.SideMenu;
import Signal.Controller;
import Utill.MakePrettyInterface;
import voca.core.IncorrectWord;

public class IncorrectUI extends JPanel {

    private final Controller signalHandler;
    private final SideMenu sideMenu;
    private final JLabel titleLabel = MakePrettyInterface.createTitleLabel("InCorrect");
    private final JLabel statusLabel = new JLabel("오답을 불러오세요.");
    private final JLabel pathLabel = new JLabel("파일 위치: -");
    private final JButton refreshButton = new JButton("새로고침");
    private final JButton removeButton = new JButton("선택 삭제");
    private final JButton clearButton = new JButton("전체 삭제");
    private final IncorrectTableModel tableModel = new IncorrectTableModel();
    private final JTable table = new JTable(tableModel);

    public IncorrectUI(Controller signalHandler) {
        this.signalHandler = signalHandler;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        sideMenu = new SideMenu(signalHandler);
        sideMenu.setVisible(false);

        JPanel contentPanel = MakePrettyInterface.createContentContainer();

        JPanel topPanel = new JPanel();
        MakePrettyInterface.styleTopBar(topPanel);
        JButton menuBtn = sideMenu.getToggleButton();
        MakePrettyInterface.styleMenuToggleButton(menuBtn);
        topPanel.add(menuBtn);

        JPanel centerBasePanel = new JPanel(new GridBagLayout());
        MakePrettyInterface.makeWhite(centerBasePanel);

        JPanel centerPanel = MakePrettyInterface.createCenterColumn();
        centerBasePanel.add(centerPanel);

        JPanel titleWrapper = new JPanel(new GridBagLayout());
        MakePrettyInterface.makeWhite(titleWrapper);
        titleWrapper.add(titleLabel);

        configureTable();
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        MakePrettyInterface.setFixedSize(tableScroll, 560, 520);
        tableScroll.setAlignmentX(CENTER_ALIGNMENT);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        MakePrettyInterface.makeWhite(statusPanel);
        statusLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        pathLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        statusPanel.add(statusLabel);
        statusPanel.add(Box.createHorizontalStrut(12));
        statusPanel.add(pathLabel);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        MakePrettyInterface.makeWhite(buttonRow);
        MakePrettyInterface.stylePrimaryButton(refreshButton, 140, 45);
        MakePrettyInterface.stylePrimaryButton(removeButton, 140, 45);
        MakePrettyInterface.stylePrimaryButton(clearButton, 140, 45);
        refreshButton.addActionListener(e -> sendSignal(IncorrectSignal.REFRESH, null));
        removeButton.addActionListener(e -> sendSignal(IncorrectSignal.REMOVE_SELECTED, getSelectedWords()));
        clearButton.addActionListener(e -> sendSignal(IncorrectSignal.CLEAR_ALL, null));
        buttonRow.add(refreshButton);
        buttonRow.add(removeButton);
        buttonRow.add(clearButton);

        JPanel card = MakePrettyInterface.createCenterColumn();
        card.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        MakePrettyInterface.setFixedSize(card, 560, 640);
        card.setMaximumSize(new java.awt.Dimension(580, Integer.MAX_VALUE));
        card.add(Box.createVerticalStrut(15));
        card.add(statusPanel);
        card.add(Box.createVerticalStrut(10));
        card.add(tableScroll);
        card.add(Box.createVerticalStrut(15));
        card.add(buttonRow);
        card.add(Box.createVerticalGlue());

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(titleWrapper);
        centerPanel.add(MakePrettyInterface.spacer(0, 30));
        centerPanel.add(card);
        centerPanel.add(Box.createVerticalGlue());

        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(centerBasePanel, BorderLayout.CENTER);

        add(sideMenu, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    public void setSideMenuVisible(boolean visible) {
        sideMenu.setVisible(visible);
        MakePrettyInterface.updateScreen(this);
    }

    public void updateIncorrectWords(List<IncorrectWord> incorrectWords) {
        tableModel.setData(incorrectWords);
        boolean hasData = incorrectWords != null && !incorrectWords.isEmpty();
        if (hasData) {
            statusLabel.setText("총 " + incorrectWords.size() + "개 오답을 불러왔습니다.");
        } else {
            statusLabel.setText("기록된 오답이 없습니다.");
        }
    }

    public List<IncorrectWord> getSelectedWords() {
        int[] rows = table.getSelectedRows();
        List<IncorrectWord> selected = new ArrayList<>();
        for (int viewIndex : rows) {
            int modelIndex = table.convertRowIndexToModel(viewIndex);
            IncorrectWord word = tableModel.getAt(modelIndex);
            if (word != null) {
                selected.add(word);
            }
        }
        return selected;
    }

    public void setStatus(String text) {
        statusLabel.setText(text == null ? "" : text);
    }

    public void setFilePath(String path) {
        String normalized = (path == null || path.isEmpty()) ? "-" : path;
        pathLabel.setText("파일 위치: " + normalized);
    }

    public void setControlsEnabled(boolean enabled) {
        refreshButton.setEnabled(enabled);
        removeButton.setEnabled(enabled);
        clearButton.setEnabled(enabled);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void sendSignal(IncorrectSignal signal, Object payload) {
        signalHandler.send(signal, payload);
    }

    private void configureTable() {
        table.setFillsViewportHeight(true);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 13));
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
    }

    private static class IncorrectTableModel extends AbstractTableModel {
        private final String[] columns = {"영어", "뜻", "예문", "유형"};
        private final List<IncorrectWord> rows = new ArrayList<>();

        @Override
        public int getRowCount() {
            return rows.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            IncorrectWord word = rows.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> safe(word.getEng());
                case 1 -> String.join(", ", word.getKor());
                case 2 -> safe(word.getEx());
                case 3 -> safe(word.getQuizType());
                default -> "";
            };
        }

        public IncorrectWord getAt(int index) {
            if (index < 0 || index >= rows.size()) {
                return null;
            }
            return rows.get(index);
        }

        public void setData(List<IncorrectWord> data) {
            rows.clear();
            if (data != null) {
                rows.addAll(data);
            }
            fireTableDataChanged();
        }

        private String safe(String text) {
            if (text == null) {
                return "";
            }
            return text;
        }
    }
}
