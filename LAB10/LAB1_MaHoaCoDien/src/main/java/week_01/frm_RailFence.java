package week_01;

import javax.swing.JOptionPane;

public class frm_RailFence extends AbstractCipherFrame {

    public frm_RailFence() {
        super("Rail Fence Cipher", "MẬT MÃ RAIL FENCE", "Số hàng (rails) >= 2, ví dụ 3", "3");
    }

    @Override
    protected void onEncrypt() {
        String plaintext = txt_plaintext.getText();
        if (plaintext.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hãy nhập Plaintext trước khi mã hóa.");
            return;
        }
        try {
            int rails = Integer.parseInt(txt_key.getText().trim());
            txt_ciphertext.setText(RailFenceCipher.encrypt(plaintext, rails));
            lbl_status.setText("Đã mã hóa với " + rails + " hàng.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số hàng phải là số nguyên, ví dụ 3.",
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
            int rails = Integer.parseInt(txt_key.getText().trim());
            txt_plaintext.setText(RailFenceCipher.decrypt(ciphertext, rails));
            lbl_status.setText("Đã giải mã với " + rails + " hàng.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số hàng phải là số nguyên, ví dụ 3.",
                    "Khóa không hợp lệ", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected String saveFileName() {
        return "railfence_ciphertext.txt";
    }
}
