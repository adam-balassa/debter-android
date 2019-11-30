package hu.bme.aut.debter.data;

import androidx.lifecycle.MutableLiveData;

import hu.bme.aut.debter.model.DebterRoom;
import retrofit2.Callback;

public class RoomDataSource {
    MutableLiveData<DebterRoom> room;
    private static RoomDataSource instance;

    public static RoomDataSource getInstance() {
        if (instance == null)
            instance = new RoomDataSource();
        return instance;
    }

    private RoomDataSource () {
        room = new MutableLiveData<>();
    }

    public MutableLiveData<DebterRoom> getRoom() {
        return room;
    }

    public void loadRoom(String roomKey, DebterAPI.DebterCallback<APIRoutes.Response<APIRoutes.FullRoomData>> callback) {
        APIRoutes api = DebterAPI.getInstance().getDebter();
        api.getRoomData(new APIRoutes.RoomKey(roomKey)).enqueue(
                new DebterAPI.DebterCallback<>((call, response) -> {
                    DebterRoom details = response.body().data.getRoom();
                    api.getFullRoomData(roomKey).enqueue(
                            new DebterAPI.DebterCallback<>((call1, response1) -> {
                                DebterRoom debterRoom = response1.body().data.getRoom(details);
                                room.postValue(debterRoom);
                                callback.onResponse(call1, response1);
                            }));
                }));
    }

    public void loadRoomDetails(String roomKey, DebterAPI.DebterCallback<APIRoutes.Response<APIRoutes.FullRoomData>> callback) {
        APIRoutes api = DebterAPI.getInstance().getDebter();
        api.getFullRoomData(roomKey).enqueue(
                new DebterAPI.DebterCallback<>((call1, response1) -> {
                    DebterRoom debterRoom = response1.body().data.getRoom(room.getValue());
                    room.postValue(debterRoom);
                    callback.onResponse(call1, response1);
                }));
    }
}
