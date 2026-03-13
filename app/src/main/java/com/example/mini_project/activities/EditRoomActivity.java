package com.example.mini_project.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mini_project.R;
import com.example.mini_project.model.Room;
import com.example.mini_project.repository.RoomRepository;

public class EditRoomActivity extends AppCompatActivity {

    private EditText etRoomId, etRoomName, etRoomPrice, etTenantName, etTenantPhone;
    private CheckBox cbIsRented;
    private Button btnSave, btnCancel, btnDelete;
    private int roomIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_room);

        roomIndex = getIntent().getIntExtra("ROOM_INDEX", -1);
        if (roomIndex == -1) {
            Toast.makeText(this, "Lỗi khi tải dữ liệu phòng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Room currentRoom = RoomRepository.getInstance().getAllRooms().get(roomIndex);

        etRoomId = findViewById(R.id.etRoomId);
        etRoomName = findViewById(R.id.etRoomName);
        etRoomPrice = findViewById(R.id.etRoomPrice);
        cbIsRented = findViewById(R.id.cbIsRented);
        etTenantName = findViewById(R.id.etTenantName);
        etTenantPhone = findViewById(R.id.etTenantPhone);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnDelete = findViewById(R.id.btnDelete);

        // Load data
        etRoomId.setText(currentRoom.getId());
        etRoomName.setText(currentRoom.getName());
        etRoomPrice.setText(String.valueOf(currentRoom.getPrice()));
        cbIsRented.setChecked(currentRoom.isRented());
        etTenantName.setText(currentRoom.getTenantName());
        etTenantPhone.setText(currentRoom.getTenantPhone());

        // Field states
        etTenantName.setEnabled(currentRoom.isRented());
        etTenantPhone.setEnabled(currentRoom.isRented());

        cbIsRented.setOnCheckedChangeListener((buttonView, isChecked) -> {
            etTenantName.setEnabled(isChecked);
            etTenantPhone.setEnabled(isChecked);
            if (!isChecked) {
                etTenantName.setText("");
                etTenantPhone.setText("");
            }
        });

        btnCancel.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> updateRoom());
        btnDelete.setOnClickListener(v -> showDeleteConfirmDialog());
    }

    private void updateRoom() {
        String id = etRoomId.getText().toString().trim();
        String name = etRoomName.getText().toString().trim();
        String priceStr = etRoomPrice.getText().toString().trim();
        boolean rented = cbIsRented.isChecked();
        String tName = etTenantName.getText().toString().trim();
        String tPhone = etTenantPhone.getText().toString().trim();

        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr)) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin bắt buộc (Mã, Tên, Giá)", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = 0;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá tiền không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rented && (TextUtils.isEmpty(tName) || TextUtils.isEmpty(tPhone))) {
            Toast.makeText(this, "Vui lòng nhập tên và SĐT người thuê", Toast.LENGTH_SHORT).show();
            return;
        }

        Room updatedRoom = new Room(id, name, price, rented, tName, tPhone);
        RoomRepository.getInstance().updateRoom(roomIndex, updatedRoom);
        
        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void showDeleteConfirmDialog() {
        Room currentRoom = RoomRepository.getInstance().getAllRooms().get(roomIndex);
        new AlertDialog.Builder(this)
                .setTitle("Xóa phòng")
                .setMessage("Bạn có chắc chắn muốn xóa " + currentRoom.getName() + " không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    RoomRepository.getInstance().deleteRoom(roomIndex);
                    Toast.makeText(this, "Đã xóa", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Không", null)
                .show();
    }
}
