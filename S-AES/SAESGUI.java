import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SAESGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public SAESGUI() {
        setTitle("加解密工具");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建CardLayout和主面板
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // 添加各个模式的面板
        cardPanel.add(createSAESPanel(), "S-AES加解密模式");
        cardPanel.add(createMultiEncryptPanel(), "多重加密模式");
        cardPanel.add(createAttackPanel(), "中间攻击破解模式");
        cardPanel.add(createCBCPanel(), "CBC加解密模式");

        // 创建菜单栏
        JMenuBar menuBar = new JMenuBar();
        JMenu modeMenu = new JMenu("选择模式");

        JMenuItem saesItem = new JMenuItem("S-AES加解密模式");
        saesItem.addActionListener(e -> cardLayout.show(cardPanel, "S-AES加解密模式"));
        modeMenu.add(saesItem);

        JMenuItem multiItem = new JMenuItem("多重加密模式");
        multiItem.addActionListener(e -> cardLayout.show(cardPanel, "多重加密模式"));
        modeMenu.add(multiItem);

        JMenuItem attackItem = new JMenuItem("中间攻击破解模式");
        attackItem.addActionListener(e -> cardLayout.show(cardPanel, "中间攻击破解模式"));
        modeMenu.add(attackItem);

        JMenuItem cbcItem = new JMenuItem("CBC加解密模式");
        cbcItem.addActionListener(e -> cardLayout.show(cardPanel, "CBC加解密模式"));
        modeMenu.add(cbcItem);

        menuBar.add(modeMenu);
        setJMenuBar(menuBar);

        // 添加面板到主窗口
        add(cardPanel, BorderLayout.CENTER);
    }

    private JPanel createSAESPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2));

        panel.add(new JLabel("明文/密文输入:"));
        JTextField inputField = new JTextField();
        panel.add(inputField);

        panel.add(new JLabel("密钥输入:"));
        JTextField keyField = new JTextField();
        panel.add(keyField);

        panel.add(new JLabel("选择:"));
        String[] options = {"Binary", "ASCII"};
        JComboBox<String> formatSelector = new JComboBox<>(options);
        panel.add(formatSelector);

        JButton encryptButton = new JButton("加密");
        panel.add(encryptButton);
        JButton decryptButton = new JButton("解密");
        panel.add(decryptButton);

        panel.add(new JLabel("输出:"));
        JTextArea outputArea = new JTextArea();
        panel.add(new JScrollPane(outputArea));
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             String input= inputField.getText();
             String key =keyField.getText();
                if(formatSelector.getSelectedItem().equals("Binary")) {
                    outputArea.setText(SAES.encrypt(input, key));
            }
                if(formatSelector.getSelectedItem().equals("ASCII")) {
                    outputArea.setText(SAES.ASCII_encrypt(input, key));
                }
            }
        });
        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input= inputField.getText();
                String key =keyField.getText();
                if(formatSelector.getSelectedItem().equals("Binary")) {
                    outputArea.setText(SAES.decrypt(input, key));
                }
                if(formatSelector.getSelectedItem().equals("ASCII")) {
                    outputArea.setText(SAES.ASCII_decrypt(input, key));
                }
            }
        });
        return panel;
    }

    private JPanel createMultiEncryptPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2));

        panel.add(new JLabel("明文/密文输入:"));
        JTextField inputField = new JTextField();
        panel.add(inputField);

        panel.add(new JLabel("密钥输入:"));
        JTextField keyField = new JTextField();
        panel.add(keyField);

        JButton doubleEncryptButton = new JButton("双重加密");
        panel.add(doubleEncryptButton);
        JButton doubleDecryptButton = new JButton("双重解密");
        panel.add(doubleDecryptButton);

        JButton tripleEncryptButton = new JButton("三重加密");
        panel.add(tripleEncryptButton);
        JButton tripleDecryptButton = new JButton("三重解密");
        panel.add(tripleDecryptButton);

        panel.add(new JLabel("输出:"));
        JTextArea outputArea = new JTextArea();
        panel.add(new JScrollPane(outputArea));
        doubleEncryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input= inputField.getText();
                String key =keyField.getText();
                outputArea.setText(SAES.Double_encrypt(input, key));
            }
        });
        doubleDecryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input= inputField.getText();
                String key =keyField.getText();
                outputArea.setText(SAES.Double_decrypt(input, key));
            }
        });
        tripleEncryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input= inputField.getText();
                String key =keyField.getText();
                outputArea.setText(SAES.Triple_encrypt(input, key));
            }
        });
        tripleDecryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input= inputField.getText();
                String key =keyField.getText();
                outputArea.setText(SAES.Triple_decrypt(input, key));
            }
        });
        return panel;
    }

    private JPanel createAttackPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2));

        panel.add(new JLabel("明文输入:"));
        JTextField plaintextField = new JTextField();
        panel.add(plaintextField);

        panel.add(new JLabel("密文输入:"));
        JTextField ciphertextField = new JTextField();
        panel.add(ciphertextField);

        JButton crackButton = new JButton("破解");
        panel.add(new JLabel(""));
        panel.add(crackButton);

        panel.add(new JLabel("可能密钥:"));
        JTextField possibleKeyField = new JTextField();
        panel.add(possibleKeyField);
        crackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String plain= plaintextField.getText();
                String cipher =ciphertextField.getText();
                possibleKeyField.setText(SAES.Attack(plain, cipher));
            }
        });
        return panel;
    }

    private JPanel createCBCPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 2));

        panel.add(new JLabel("明文输入:"));
        JTextField plaintextField = new JTextField();
        panel.add(plaintextField);

        panel.add(new JLabel("密钥输入:"));
        JTextField key = new JTextField();
        panel.add(key);

        JButton cbcEncryptButton = new JButton("CBC加密");
        panel.add(cbcEncryptButton);

        JTextArea ciphertextArea = new JTextArea();
        panel.add(new JScrollPane(ciphertextArea));

        panel.add(new JLabel("密文输入:"));
        JTextField ciphertextInputField = new JTextField();
        panel.add(ciphertextInputField);

        panel.add(new JLabel("密钥输入:"));
        JTextField key2 = new JTextField();
        panel.add(key2);

        panel.add(new JLabel("初始向量输入:"));
        JTextField ov = new JTextField();
        panel.add(ov);

        JButton cbcDecryptButton = new JButton("CBC解密");
        panel.add(cbcDecryptButton);

        JTextArea decryptedTextArea = new JTextArea();
        panel.add(new JScrollPane(decryptedTextArea));
        cbcEncryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String plain= plaintextField.getText();
                String k =key.getText();
                ciphertextArea.setText(SAES.CBC_encrypt(plain, k));
            }
        });
        cbcDecryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cipher=ciphertextInputField.getText();
                String k =key.getText();
                String vec=ov.getText();
                decryptedTextArea.setText(SAES.CBC_decrypt(cipher, k, vec));
            }
        });
        return panel;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SAESGUI app = new SAESGUI();
            app.setVisible(true);
        });
    }
}
