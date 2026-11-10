package week_01;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class frm_LichSu extends javax.swing.JFrame {

    private final JTable tbl_history = new JTable();
    private final JLabel lbl_status = new JLabel("Đang tải...");

    public frm_LichSu() {
        setTitle("Lịch sử thao tác (SQL Server)");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel header = new JLabel("LỊCH SỬ THAO TÁC - BẢNG DBO.HISTORY");
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        root.add(header, BorderLayout.NORTH);

        tbl_history.setModel(HistoryDao.emptyModel());
        JScrollPane sp = new JScrollPane(tbl_history);
        sp.setPreferredSize(new Dimension(760, 260));
        root.add(sp, BorderLayout.CENTER);

        JButton btn_refresh = new JButton("Tải lại");
        JPanel south = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        south.add(btn_refresh);
        south.add(lbl_status);
        root.add(south, BorderLayout.SOUTH);

        setContentPane(root);

        btn_refresh.addActionListener(e -> loadData());

        loadData();

        pack();
        setLocationRelativeTo(null);
    }

    /** Đọc 50 dòng mới nhất từ SQL Server và hiện lên JTable. */
    private void loadData() {
        try {
            tbl_history.setModel(HistoryDao.loadLatest(50));
            lbl_status.setText("Đã tải " + tbl_history.getRowCount() + " dòng mới nhất từ dbo.history.");
        } catch (java.sql.SQLException ex) {
            lbl_status.setText("Không đọc được lịch sử: " + ex.getMessage());
        }
    }
}
