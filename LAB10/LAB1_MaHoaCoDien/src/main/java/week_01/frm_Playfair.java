package week_01;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class frm_Playfair extends AbstractCipherFrame {

    private JTextArea txt_matrix;   // gán trong addExtras (gọi từ constructor lớp nền)
    private JButton btn_matrix;

    public frm_Playfair() {
        super("Playfair Cipher", "MẬT MÃ PLAYFAIR", "Chỉ gồm chữ A-Z, ví dụ KEYWORD", "KEYWORD");
    }

    @Override
    protected void addExtras(JPanel root) {
        JLabel l = new JLabel("Key Matrix:");
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(l);

        txt_matrix = new JTextArea(5, 9);
        txt_matrix.setEditable(false);
        txt_matrix.setFont(new Font("Monospaced", Font.BOLD, 14));
        JScrollPane sp = new JScrollPane(txt_matrix);
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        sp.setPreferredSize(new Dimension(180, 112));
        sp.setMaximumSize(new Dimension(220, 130));
        root.add(sp);

        btn_matrix = new JButton("Generate Key Matrix");
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        row.add(btn_matrix);
        root.add(row);
        root.add(Box.createVerticalStrut(4));

        btn_matrix.addActionListener(e -> onMatrix());
    }

    private void onMatrix() {
        try {
            txt_matrix.setText(PlayfairCipher.getKeyMatrix(txt_key.getText()));
            txt_matrix.setCaretPosition(0);
            lbl_status.setText("Đã tạo ma trận khóa 5x5 (I và J dùng chung ô).");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Khóa không hợp lệ", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void onEncrypt() {
        String plaintext = txt_plaintext.getText();
        if (plaintext.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hãy nhập Plaintext trước khi mã hóa.");
            return;
        }
        try {
            String key = txt_key.getText().trim();
            txt_ciphertext.setText(PlayfairCipher.encrypt(plaintext, key));
            lbl_status.setText("Đã mã hóa Playfair (bỏ dấu cách, J -> I, chèn X).");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void onDecrypt() {
        String ciphertext = txt_ciphertext.getText();
        if (ciphertext.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hãy nhập Ciphertext trước khi giải mã.");
            return;
        }
        try {
            String key = txt_key.getText().trim();
            txt_plaintext.setText(PlayfairCipher.decrypt(ciphertext, key));
            lbl_status.setText("Đã giải mã Playfair (còn ký tự đệm X).");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected String saveFileName() {
        return "playfair_ciphertext.txt";
    }
}
