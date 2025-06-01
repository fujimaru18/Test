MainApp → MainFrame
           ↓
         View (người dùng nhấn nút)
           ↓
        Controller
           ↓
        Service (xử lý logic, gọi Validator)
           ↓
          DAO (thực hiện SQL)
           ↓
   DBConnection (mở kết nối DB)
           ↓
        MySQL
           ↑
   → Trả kết quả lên từng lớp
   → Cập nhật lại giao diện
