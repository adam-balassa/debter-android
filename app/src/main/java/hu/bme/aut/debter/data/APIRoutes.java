package hu.bme.aut.debter.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import hu.bme.aut.debter.model.Member;
import hu.bme.aut.debter.model.MyDebt;
import hu.bme.aut.debter.model.Room;
import hu.bme.aut.debter.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIRoutes {
    @GET("user/rooms/{email}")
    Call<Response<ArrayList<RoomResult>>> getUsersRooms(@Path("email") String email);

    @POST("login")
    Call<Response<UserResult>> login(@Body LoginData data);

    @GET("user/debts/{email}")
    Call<Response<ArrayList<DebtResult>>> getUsersDebts(@Path("email") String email);

    class Response <T> {
        @SerializedName("statusCode") @Expose public Integer statusCode;
        @SerializedName("error") @Expose public Boolean error;
        @SerializedName("data") @Expose public T data;
    }

    class LoginData {
        String email;
        String password;
        public LoginData (String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    class RoomResult {
        @SerializedName("room_key") @Expose private String roomKey;
        @SerializedName("name") @Expose private String roomName;
        public Room getRoom () {
            return new Room(roomKey, roomName);
        }
    }

    class DebtResult {
        @SerializedName("name") @Expose private String name;
        @SerializedName("id") @Expose private String id;
        @SerializedName("currency") @Expose private String currency;
        @SerializedName("value") @Expose private Double value;
        @SerializedName("roomName") @Expose private String roomName;
        public MyDebt getMyDebt () {
            Member to = new Member(id, name);
            return new MyDebt(to, currency, value, roomName);
        }
    }

    class UserResult {
        @SerializedName("firstName") @Expose private String firstName;
        @SerializedName("lastName") @Expose private String lastName;
        @SerializedName("email") @Expose private String email;
        public User getUser () {
            return new User(lastName + " " + firstName, email, new ArrayList<>());
        }
    }
}
