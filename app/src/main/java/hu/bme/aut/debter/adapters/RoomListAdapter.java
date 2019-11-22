package hu.bme.aut.debter.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hu.bme.aut.debter.R;
import hu.bme.aut.debter.model.Room;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.RoomViewHolder> {

    final List<Room> rooms;
    final RoomClickListener listener;

    public RoomListAdapter(List<Room> rooms, RoomClickListener listener) {
        this.rooms = rooms;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View roomView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.room_fragment, parent, false);
        return new RoomViewHolder(roomView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.room = room;
        holder.roomName.setText(room.getTitle());
        holder.roomKey.setText(room.getRoomKey());
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public interface RoomClickListener{
        void onRoomClicked(Room room);
    }

    class RoomViewHolder extends RecyclerView.ViewHolder {

        TextView roomName;
        TextView roomKey;
        CardView card;
        Room room;

        RoomViewHolder(@NonNull View roomView) {
            super(roomView);
            this.roomName = roomView.findViewById(R.id.room_name);
            this.roomKey = roomView.findViewById(R.id.room_key);
            this.card = roomView.findViewById(R.id.room_card);

            this.card.setOnClickListener((view) -> listener.onRoomClicked(room));
        }
    }
}
