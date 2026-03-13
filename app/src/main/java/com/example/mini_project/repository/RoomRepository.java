package com.example.mini_project.repository;

import com.example.mini_project.model.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomRepository {
    private static RoomRepository instance;
    private List<Room> roomList;

    private RoomRepository() {
        roomList = new ArrayList<>();
        // Demo data
        roomList.add(new Room("P01", "Phòng 101", 1500000, false, "", ""));
        roomList.add(new Room("P02", "Phòng 102", 2000000, true, "Nguyễn Văn A", "0123456789"));
    }

    public static RoomRepository getInstance() {
        if (instance == null) {
            instance = new RoomRepository();
        }
        return instance;
    }

    public List<Room> getAllRooms() {
        return roomList;
    }

    public void addRoom(Room room) {
        roomList.add(room);
    }

    public void updateRoom(int index, Room updatedRoom) {
        if (index >= 0 && index < roomList.size()) {
            roomList.set(index, updatedRoom);
        }
    }

    public void deleteRoom(int index) {
        if (index >= 0 && index < roomList.size()) {
            roomList.remove(index);
        }
    }
}
