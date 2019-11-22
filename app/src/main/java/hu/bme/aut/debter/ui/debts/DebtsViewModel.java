package hu.bme.aut.debter.ui.debts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DebtsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DebtsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is debts fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
