package week_01;

import javax.swing.JOptionPane;

public class frm_Vigenere extends AbstractCipherFrame {

    public frm_Vigenere() {
        super("Vigenere Cipher", "MẬT MÃ VIGENÈRE", "Chỉ gồm chữ A-Z, ví dụ KEY", "KEY");
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
            txt_ciphertext.setText(VigenereCipher.encrypt(plaintext, key));
            lbl_status.setText("Đã mã hóa Vigenère với khóa " + key + ".");
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
            txt_plaintext.setText(VigenereCipher.decrypt(ciphertext, key));
            lbl_status.setText("Đã giải mã Vigenère với khóa " + key + ".");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected String saveFileName() {
        return "vigenere_ciphertext.txt";
    }
}
