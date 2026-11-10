package week_01;

import javax.swing.JOptionPane;

public class frm_Caesar extends AbstractCipherFrame {

    public frm_Caesar() {
        super("Caesar Cipher", "MẬT MÃ CAESAR", "Số nguyên, ví dụ 3", "3");
    }

    @Override
    protected void onEncrypt() {
        String plaintext = txt_plaintext.getText();
        if (plaintext.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hãy nhập Plaintext trước khi mã hóa.");
            return;
        }
        try {
            int key = Integer.parseInt(txt_key.getText().trim());
            txt_ciphertext.setText(CaesarCipher.encrypt(plaintext, key));
            lbl_status.setText("Đã mã hóa với khóa k = " + key + ".");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Khóa Caesar phải là số nguyên, ví dụ 3.",
                    "Khóa không hợp lệ", JOptionPane.ERROR_MESSAGE);
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
            int key = Integer.parseInt(txt_key.getText().trim());
            txt_plaintext.setText(CaesarCipher.decrypt(ciphertext, key));
            lbl_status.setText("Đã giải mã với khóa k = " + key + ".");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Khóa Caesar phải là số nguyên, ví dụ 3.",
                    "Khóa không hợp lệ", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected String saveFileName() {
        return "caesar_ciphertext.txt";
    }
}
