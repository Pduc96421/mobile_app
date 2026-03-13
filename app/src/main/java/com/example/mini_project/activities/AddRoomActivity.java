package com.example.mini_project.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mini_project.R;
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

        boolean isValid = true;

        if (TextUtils.isEmpty(id)) {
            etRoomId.setError("Vui lòng nhập mã phòng");
            isValid = false;
        }

        if (TextUtils.isEmpty(name)) {
            etRoomName.setError("Vui lòng nhập tên phòng");
            isValid = false;
        }

        if (TextUtils.isEmpty(priceStr)) {
            etRoomPrice.setError("Vui lòng nhập giá thuê");
            isValid = false;
        }

        double price = 0;
        if (!TextUtils.isEmpty(priceStr)) {
            try {
                price = Double.parseDouble(priceStr);
                if (price <= 0) {
                    etRoomPrice.setError("Giá thuê phải lớn hơn 0");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                etRoomPrice.setError("Giá tiền không hợp lệ");
                isValid = false;
            }
        }

        if (rented) {
            if (TextUtils.isEmpty(tName)) {
                etTenantName.setError("Vui lòng nhập tên người thuê");
                isValid = false;
            }
            if (TextUtils.isEmpty(tPhone)) {
                etTenantPhone.setError("Vui lòng nhập số điện thoại");
                isValid = false;
            } else if (tPhone.length() < 9 || tPhone.length() > 11) {
                etTenantPhone.setError("Số điện thoại không hợp lệ (9-11 số)");
                isValid = false;
            }
        }

        if (!isValid) return;

        Room newRoom = new Room(id, name, price, rented, tName, tPhone);
        RoomRepository.getInstance().addRoom(newRoom);

        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
        finish();
    }
}
