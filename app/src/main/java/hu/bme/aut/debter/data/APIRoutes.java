package hu.bme.aut.debter.data;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import hu.bme.aut.debter.model.Debt;
import hu.bme.aut.debter.model.DebterRoom;
import hu.bme.aut.debter.model.Member;
import hu.bme.aut.debter.model.MyDebt;
import hu.bme.aut.debter.model.Payment;
import hu.bme.aut.debter.model.Room;
import hu.bme.aut.debter.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIRoutes {
    @GET("user/rooms/{email}")
    Call<Response<ArrayList<RoomResult>>> getUsersRooms(@Path("email") String email);

    @POST("login")
    Call<Response<UserResult>> login(@Body LoginData data);

    @GET("user/debts/{email}")
    Call<Response<ArrayList<DebtResult>>> getUsersDebts(@Path("email") String email);

    @POST("register")
    Call<Response<Object>> register(@Body RegisterData data);

    @GET("room/{roomKey}")
    Call<Response<FullRoomData>> getFullRoomData(@Path("roomKey") String roomKey);

    @PATCH("room/login")
    Call<Response<RoomData>> getRoomData(@Body RoomKey roomKey);

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

    class RegisterData {
        String email;
        String password;
        String firstName;
        String lastName;

        public RegisterData(String email, String password, String firstName, String lastName) {
            this.email = email;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }

    class FullRoomData {
        @SerializedName("members") @Expose ArrayList<MemberData> members;
        @SerializedName("payments") @Expose ArrayList<PaymentData> payments;
        @SerializedName("users") @Expose ArrayList<UserData> users;
        @SerializedName("debts") @Expose ArrayList<DebtData> debts;

        class UserData {
            @SerializedName("id") @Expose String id;
            @SerializedName("firstName") @Expose String firstName;
            @SerializedName("lastName") @Expose String lastName;
            @SerializedName("email") @Expose String email;
        }

        class MemberData {
            @SerializedName("id") @Expose String id;
            @SerializedName("name") @Expose String name;
            @SerializedName("userId") @Expose String userId;
        }

        class PaymentData {
            @SerializedName("id") @Expose String id;
            @SerializedName("value") @Expose Double value;
            @SerializedName("currency") @Expose String currency;
            @SerializedName("realValue") @Expose Double realValue;
            @SerializedName("note") @Expose String note;
            @SerializedName("fromId") @Expose String fromId;
            @SerializedName("date") @Expose String date;
            @SerializedName("active") @Expose Integer active;
            @SerializedName("memberId") @Expose String memberId;
            @SerializedName("excluded") @Expose ArrayList<String> excluded;
            @SerializedName("included") @Expose ArrayList<String> included;
        }

        class DebtData {
            @SerializedName("value") @Expose Double value;
            @SerializedName("currency") @Expose String currency;
            @SerializedName("for") @Expose String forMember;
            @SerializedName("from") @Expose String fromMember;
            @SerializedName("arranged") @Expose Integer arranged;
        }

        public DebterRoom getRoom(DebterRoom roomDetails) {
            Map<String, User> debterUsers = new HashMap<>();
            for (UserData user : users)
                debterUsers.put(user.id, new User(user.lastName + " " + user.firstName, user.email, null));

            Map<String, Member> debterMembers = new HashMap<>();
            for (MemberData member : members) {
                User user;
                if (member.userId != null)
                    user = debterUsers.get(member.userId);
                else user = new User(member.name, "", null);
                debterMembers.put(member.id, new Member(member.id, user, new LinkedList<>()));
            }

            List<Payment> debterPayments = new LinkedList<>();
            for (PaymentData payment: payments) {
                if (payment.fromId != null)
                    continue;
                List<Member> included = new LinkedList<>();
                if (payment.included.size() == 0 && payment.excluded.size() == 0)
                    included.addAll(debterMembers.values());
                else if (payment.included.size() != 0) {
                    for (Member member : debterMembers.values())
                        if (payment.included.contains(member.getId()))
                            included.add(member);
                }
                else {
                    included.addAll(debterMembers.values());
                    for (Member member : debterMembers.values())
                        if (payment.excluded.contains(member.getId()))
                            included.remove(member);
                }
                @SuppressLint("SimpleDateFormat")
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'");
                try {
                    Date date = formatter.parse(payment.date);
                    debterPayments.add(new Payment(payment.id, debterMembers.get(payment.memberId), payment.value,
                            payment.currency, payment.realValue, date, payment.note, payment.active == 1, included));
                } catch (ParseException ignored) {
                }
            }
            for (ListIterator<Payment> it = debterPayments.listIterator(); it.hasNext(); ) {
                Payment payment = it.next();
                if (payment.getValue() < 0) {
                    it.remove();
                    for (Payment p : debterPayments)
                        if (p.getId().equals(payment.getNote())) {
                            p.getIncluded().clear();
                            p.getIncluded().add(payment.getMember());
                            break;
                        }
                }
            }
            for (DebtData debt : debts) {
                debterMembers.get(debt.fromMember).getDebts().add(new Debt(
                        debterMembers.get(debt.fromMember), debterMembers.get(debt.forMember),
                        debt.value, debt.currency, debt.arranged == 1
                ));
            }
            return new DebterRoom(roomDetails.getRoomKey(),
                    roomDetails.getTitle(),
                    new LinkedList<>(debterMembers.values()),
                    debterPayments,
                    roomDetails.getRounding(),
                    roomDetails.getCurrency());
        }
    }

    class RoomKey {
        String roomKey;
        public RoomKey(String roomKey) {
            this.roomKey = roomKey;
        }
    }

    class RoomData {
        @SerializedName("key") @Expose String key;
        @SerializedName("defaultCurrency") @Expose String defaultCurrency;
        @SerializedName("name") @Expose String name;
        @SerializedName("rounding") @Expose Double rounding;

        public DebterRoom getRoom() {
            return new DebterRoom(key, name, null, null, rounding, defaultCurrency);
        }
    }
}
