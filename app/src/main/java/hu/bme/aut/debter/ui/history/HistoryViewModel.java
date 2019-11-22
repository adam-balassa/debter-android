package hu.bme.aut.debter.ui.history;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import hu.bme.aut.debter.data.RoomDataSource;
import hu.bme.aut.debter.model.Payment;
import hu.bme.aut.debter.model.Room;

public class HistoryViewModel extends ViewModel {

    private static HistoryViewModel instance;
    private RoomDataSource data;
    private MutableLiveData<Room> room;
    private Payment activePayment;

    public HistoryViewModel() {
        data = RoomDataSource.getInstance();
    }

    public static HistoryViewModel getInstance() {
        if (instance == null) instance = new HistoryViewModel();
        return instance;
    }

    public MutableLiveData<Room> getRoom() {
        return room;
    }

    public Payment getActivePayment() {
        return activePayment;
    }

    public void setActivePayment(Payment activePayment) {
        this.activePayment = activePayment;
    }
}