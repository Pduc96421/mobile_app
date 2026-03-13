package com.example.mini_project.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mini_project.R;
import com.example.mini_project.model.Room;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<Room> roomList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public RoomAdapter(List<Room> roomList) {
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.tvRoomName.setText("Tên phòng: " + room.getName());
        holder.tvRoomPrice.setText(String.format("Giá thuê: %,.0f VND", room.getPrice()));

        if (room.isRented()) {
            holder.tvRoomStatus.setText("Tình trạng: Đã thuê");
            holder.tvRoomStatus.setTextColor(Color.RED);
            holder.tvTenantInfo.setVisibility(View.VISIBLE);
            holder.tvTenantInfo.setText("Người thuê: " + room.getTenantName() + " - SĐT: " + room.getTenantPhone());
        } else {
            holder.tvRoomStatus.setText("Tình trạng: Còn trống");
            holder.tvRoomStatus.setTextColor(Color.parseColor("#008000")); // Xanh lá cây
            holder.tvTenantInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public void updateList(List<Room> newList) {
        this.roomList = newList;
        notifyDataSetChanged();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvRoomPrice, tvRoomStatus, tvTenantInfo;

        public RoomViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvRoomPrice = itemView.findViewById(R.id.tvRoomPrice);
            tvRoomStatus = itemView.findViewById(R.id.tvRoomStatus);
            tvTenantInfo = itemView.findViewById(R.id.tvTenantInfo);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemLongClick(position);
                        return true;
                    }
                }
                return false;
            });
        }
    }
}