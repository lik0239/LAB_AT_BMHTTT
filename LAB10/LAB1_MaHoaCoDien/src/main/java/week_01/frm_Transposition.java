package week_01;

import javax.swing.JOptionPane;

public class frm_Transposition extends AbstractCipherFrame {

    public frm_Transposition() {
        super("Transposition Cipher", "MẬT MÃ HOÁN VỊ CỘT (TRANSPOSITION)",
                "Thứ tự đọc cột, cách nhau dấu phẩy, ví dụ 2,3,1", "2,3,1");
    }

    @Override
    protected void onEncrypt() {
        String plaintext = txt_plaintext.getText();
        if (plaintext.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hãy nhập Plaintext trước khi mã hóa.");
            return;
        }
        try {
            int[] key = TranspositionCipher.parseKey(txt_key.getText());
            txt_ciphertext.setText(TranspositionCipher.encrypt(plaintext, key));
            lbl_status.setText("Đã mã hóa với " + key.length + " cột.");
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
            int[] key = TranspositionCipher.parseKey(txt_key.getText());
            txt_plaintext.setText(TranspositionCipher.decrypt(ciphertext, key));
            lbl_status.setText("Đã giải mã với " + key.length + " cột.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected String saveFileName() {
        return "transposition_ciphertext.txt";
    }
}
