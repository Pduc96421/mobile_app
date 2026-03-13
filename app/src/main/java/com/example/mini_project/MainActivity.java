package com.example.mini_project;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mini_project.adapters.RoomAdapter;
import com.example.mini_project.model.Room;
import com.example.mini_project.repository.RoomRepository;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RoomAdapter adapter;
    private List<Room> roomList;
    private Button btnAddRoom;
    private TextView tvTotalRooms, tvRentedRooms, tvEmptyRooms;
    private EditText etSearchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewRooms);
        tvTotalRooms = findViewById(R.id.tvTotalRooms);
        tvRentedRooms = findViewById(R.id.tvRentedRooms);
        tvEmptyRooms = findViewById(R.id.tvEmptyRooms);
        etSearchBox = findViewById(R.id.etSearchBox);
        
        // Use RoomRepository for initial data
        roomList = RoomRepository.getInstance().getAllRooms();
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RoomAdapter(roomList);
        recyclerView.setAdapter(adapter);

        etSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                filterRooms(s.toString());
            }
        });


        btnAddRoom = findViewById(R.id.btnAddRoom);
        btnAddRoom.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddRoomActivity.class);
            startActivity(intent);
        });

        adapter.setOnItemClickListener(new RoomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Sửa phòng => Mở EditRoomActivity
                Intent intent = new Intent(MainActivity.this, EditRoomActivity.class);
                intent.putExtra("ROOM_INDEX", position);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(int position) {
                // Xóa phòng cũng nằm trong EditRoomActivity theo phân công
                Intent intent = new Intent(MainActivity.this, EditRoomActivity.class);
                intent.putExtra("ROOM_INDEX", position);
                startActivity(intent);
            }
        });
        updateStatistics();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data every time return to MainActivity
        roomList = RoomRepository.getInstance().getAllRooms();
        adapter.updateList(roomList);
        updateStatistics();
    }

    private void filterRooms(String text) {
        List<Room> filteredList = new ArrayList<>();
        for (Room item : roomList) {
            // Search by Room Name or Tenant Phone
            if (item.getName().toLowerCase().contains(text.toLowerCase()) || 
                (item.getTenantPhone() != null && item.getTenantPhone().contains(text))) {
                filteredList.add(item);
            }
        }
        adapter.updateList(filteredList);
    }

    private void updateStatistics() {
        int total = roomList.size();
        int rented = 0;
        int empty = 0;
        for (Room r : roomList) {
            if (r.isRented()) rented++;
            else empty++;
        }
        tvTotalRooms.setText("Tổng: " + total);
        tvRentedRooms.setText("Đã thuê: " + rented);
        tvEmptyRooms.setText("Trống: " + empty);
    }
}
