package com.example.mini_project;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mini_project.model.Room;
import com.example.mini_project.repository.RoomRepository;

public class AddRoomActivity extends AppCompatActivity {

    private EditText etRoomId, etRoomName, etRoomPrice, etTenantName, etTenantPhone;
    private CheckBox cbIsRented;
    private Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        etRoomId = findViewById(R.id.etRoomId);
        etRoomName = findViewById(R.id.etRoomName);
        etRoomPrice = findViewById(R.id.etRoomPrice);
        cbIsRented = findViewById(R.id.cbIsRented);
        etTenantName = findViewById(R.id.etTenantName);
        etTenantPhone = findViewById(R.id.etTenantPhone);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // Initial state
        boolean isRented = cbIsRented.isChecked();
        etTenantName.setEnabled(isRented);
        etTenantPhone.setEnabled(isRented);

        cbIsRented.setOnCheckedChangeListener((buttonView, isChecked) -> {
            etTenantName.setEnabled(isChecked);
            etTenantPhone.setEnabled(isChecked);
            if (!isChecked) {
                etTenantName.setText("");
                etTenantPhone.setText("");
            }
        });

        btnCancel.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> saveRoom());
    }

    private void saveRoom() {
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

        Room newRoom = new Room(id, name, price, rented, tName, tPhone);
        RoomRepository.getInstance().addRoom(newRoom);
        
        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
        finish();
    }
}
