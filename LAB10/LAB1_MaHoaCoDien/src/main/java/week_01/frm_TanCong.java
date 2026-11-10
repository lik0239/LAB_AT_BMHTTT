package week_01;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class frm_TanCong extends javax.swing.JFrame {

    private final JTextArea txt_cipher = new JTextArea(2, 50);
    private final JTextField txt_known_plain = new JTextField("HELLO", 10);
    private final JTextField txt_known_cipher = new JTextField("RIJVS", 10);
    private final JTextArea txt_result = new JTextArea(14, 50);
    private final JLabel lbl_status = new JLabel("Sẵn sàng.");

    public frm_TanCong() {
        setTitle("Phân tích mật mã cổ điển");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel header = new JLabel("PHÂN TÍCH (TẤN CÔNG) MẬT MÃ CỔ ĐIỂN");
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(header);
        root.add(Box.createVerticalStrut(10));

        root.add(leftLabel("Ciphertext:"));
        txt_cipher.setLineWrap(true);
        txt_cipher.setWrapStyleWord(true);
        JScrollPane spCipher = new JScrollPane(txt_cipher);
        spCipher.setAlignmentX(Component.LEFT_ALIGNMENT);
        spCipher.setPreferredSize(new Dimension(560, 50));
        spCipher.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        root.add(spCipher);
        root.add(Box.createVerticalStrut(6));

        JButton btn_bruteforce = new JButton("Brute Force Caesar");
        JButton btn_frequency = new JButton("Frequency Analysis");
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        row1.setAlignmentX(Component.LEFT_ALIGNMENT);
        row1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        row1.add(btn_bruteforce);
        row1.add(btn_frequency);
        root.add(row1);
        root.add(Box.createVerticalStrut(6));

        JButton btn_known = new JButton("Known Plaintext (Vigenere)");
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        row2.setAlignmentX(Component.LEFT_ALIGNMENT);
        row2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        row2.add(new JLabel("Bản rõ đã biết:"));
        row2.add(txt_known_plain);
        row2.add(new JLabel("Bản mã:"));
        row2.add(txt_known_cipher);
        row2.add(btn_known);
        root.add(row2);
        root.add(Box.createVerticalStrut(8));

        root.add(leftLabel("Kết quả:"));
        txt_result.setEditable(false);
        txt_result.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane spResult = new JScrollPane(txt_result);
        spResult.setAlignmentX(Component.LEFT_ALIGNMENT);
        spResult.setPreferredSize(new Dimension(560, 250));
        root.add(spResult);
        root.add(Box.createVerticalStrut(8));

        lbl_status.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(lbl_status);

        setContentPane(root);

        btn_bruteforce.addActionListener(e -> {
            String cipher = txt_cipher.getText().trim();
            if (cipher.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Hãy nhập Ciphertext.");
                return;
            }
            txt_result.setText(AttackDemos.caesarBruteForce(cipher));
            txt_result.setCaretPosition(0);
            lbl_status.setText("Đã thử đủ 26 khóa Caesar.");
        });

        btn_frequency.addActionListener(e -> {
            String cipher = txt_cipher.getText().trim();
            if (cipher.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Hãy nhập Ciphertext.");
                return;
            }
            txt_result.setText(AttackDemos.frequency(cipher));
            txt_result.setCaretPosition(0);
            lbl_status.setText("Đã thống kê tần suất chữ cái.");
        });

        btn_known.addActionListener(e -> {
            try {
                String key = AttackDemos.knownPlaintext(txt_known_plain.getText(), txt_known_cipher.getText());
                txt_result.setText("Khóa Vigenère suy ra từng vị trí: " + key
                        + "\nNếu khóa ngắn hơn bản rõ, khóa sẽ lặp lại (KEY KEY ...).");
                lbl_status.setText("Khóa suy ra: " + key);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
            }
        });

        pack();
        setMinimumSize(new Dimension(620, getHeight()));
        setLocationRelativeTo(null);
    }

    private JLabel leftLabel(String text) {
        JLabel l = new JLabel(text);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }
}
