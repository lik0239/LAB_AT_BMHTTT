package week_01;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class frm_TongHop extends javax.swing.JFrame {

    private final JComboBox<String> cbo_algorithm = new JComboBox<>(CipherTongHop.ALGORITHMS);
    private final JLabel lbl_hint = new JLabel("Khóa mẫu: 3");
    private final JTextArea txt_plaintext = new JTextArea(3, 50);
    private final JTextArea txt_ciphertext = new JTextArea(3, 50);
    private final JTextField txt_key = new JTextField("3", 12);
    private final JLabel lbl_status = new JLabel("Sẵn sàng.");

    public frm_TongHop() {
        setTitle("Chương trình tổng hợp - 6 thuật toán");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel header = new JLabel("CHƯƠNG TRÌNH TỔNG HỢP 6 THUẬT TOÁN CỔ ĐIỂN");
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(header);
        root.add(Box.createVerticalStrut(10));

        JPanel algoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        algoRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        algoRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        algoRow.add(new JLabel("Thuật toán:"));
        cbo_algorithm.setPreferredSize(new Dimension(200, 26));
        algoRow.add(cbo_algorithm);
        algoRow.add(lbl_hint);
        root.add(algoRow);
        root.add(Box.createVerticalStrut(6));

        root.add(leftLabel("Plaintext:"));
        root.add(wrapArea(txt_plaintext));
        root.add(Box.createVerticalStrut(6));

        JPanel keyRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        keyRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        keyRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        keyRow.add(new JLabel("Key:"));
        txt_key.setPreferredSize(new Dimension(200, 26));
        keyRow.add(txt_key);
        root.add(keyRow);
        root.add(Box.createVerticalStrut(6));

        root.add(leftLabel("Ciphertext:"));
        root.add(wrapArea(txt_ciphertext));
        root.add(Box.createVerticalStrut(8));

        JButton btn_encrypt = new JButton("Encrypt");
        JButton btn_decrypt = new JButton("Decrypt");
        JButton btn_open = new JButton("Open File");
        JButton btn_save = new JButton("Save File");
        JButton btn_sql = new JButton("Kết nối SQL Server");
        JButton btn_history = new JButton("Xem lịch sử");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        buttons.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        buttons.add(btn_encrypt);
        buttons.add(btn_decrypt);
        buttons.add(btn_open);
        buttons.add(btn_save);
        buttons.add(btn_sql);
        buttons.add(btn_history);
        root.add(buttons);
        root.add(Box.createVerticalStrut(8));

        lbl_status.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(lbl_status);

        setContentPane(root);

        cbo_algorithm.addActionListener(e -> {
            String algorithm = (String) cbo_algorithm.getSelectedItem();
            txt_key.setText(CipherTongHop.sampleKey(algorithm));
            lbl_hint.setText("Khóa mẫu: " + CipherTongHop.sampleKey(algorithm));
        });

        btn_encrypt.addActionListener(e -> {
            String algorithm = (String) cbo_algorithm.getSelectedItem();
            String input = txt_plaintext.getText();
            try {
                String result = CipherTongHop.encrypt(algorithm, input, txt_key.getText());
                txt_ciphertext.setText(result);
                String saved = HistoryDao.tryInsert(algorithm, "ENCRYPT", input, result);
                lbl_status.setText("Đã mã hóa " + algorithm + ". " + saved);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
            }
        });

        btn_decrypt.addActionListener(e -> {
            String algorithm = (String) cbo_algorithm.getSelectedItem();
            String input = txt_ciphertext.getText();
            try {
                String result = CipherTongHop.decrypt(algorithm, input, txt_key.getText());
                txt_plaintext.setText(result);
                String saved = HistoryDao.tryInsert(algorithm, "DECRYPT", input, result);
                lbl_status.setText("Đã giải mã " + algorithm + ". " + saved);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
            }
        });

        btn_open.addActionListener(e -> {
            String path = FileTextIO.openText(this, txt_ciphertext);
            if (path != null) {
                lbl_status.setText("Đã mở tệp: " + path);
            }
        });

        btn_save.addActionListener(e -> {
            String path = FileTextIO.saveText(this, txt_ciphertext, "ciphertext.txt");
            if (path != null) {
                lbl_status.setText("Đã lưu tệp: " + path);
            }
        });

        btn_sql.addActionListener(e -> new frm_KetNoiSQL().setVisible(true));
        btn_history.addActionListener(e -> new frm_LichSu().setVisible(true));

        pack();
        setMinimumSize(new Dimension(660, getHeight()));
        setLocationRelativeTo(null);
    }

    private JLabel leftLabel(String text) {
        JLabel l = new JLabel(text);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JScrollPane wrapArea(JTextArea area) {
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        JScrollPane sp = new JScrollPane(area);
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        sp.setPreferredSize(new Dimension(600, 70));
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        return sp;
    }
}
