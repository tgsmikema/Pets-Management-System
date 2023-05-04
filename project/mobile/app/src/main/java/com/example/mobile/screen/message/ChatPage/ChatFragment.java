package com.example.mobile.screen.message.ChatPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.SPCApplication;
import com.example.mobile.databinding.FragmentChatWithPeopleBinding;
import com.example.mobile.model.Message;
import com.example.mobile.model.User;
import com.example.mobile.service.SPCAService;
import com.example.mobile.util.TimeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {

    private FragmentChatWithPeopleBinding binding;
    private TextView chatUserName;
    private TextView chatUserType;

    private RecyclerView chatContentRecyclerView;

    private ChatContentAdapter adapter;

    private EditText sendChatText;
    private ImageButton sendChatButton;

    private SPCAService spcaService;

    private List<Message> messages;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentChatWithPeopleBinding.inflate(inflater, container, false);
        Bundle bundle = getArguments();
        messages = new ArrayList<>();
        spcaService = new SPCAService();
        initialView(bundle);
        fetchChatHistory(bundle.getInt("id"));
        return binding.getRoot();
    }


    public void initialView(Bundle bundle){
        chatUserName = binding.chatPageUserName;
        chatUserType = binding.chatPageUserType;
        chatContentRecyclerView = binding.chatContentRecyclerview;
        sendChatText = binding.sendChatText;
        sendChatButton = binding.sendChatButton;

        chatUserName.setText(bundle.getString("name"));
        chatUserType.setText(bundle.getString("type"));

        initRecyclerViewAdapter();

        sendChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sendChatText.getEditableText().toString().trim().length() != 0){
                    sendMessage(bundle.getInt("id"),sendChatText.getEditableText().toString());
                }
            }
        });
    }

    public void initRecyclerViewAdapter(){
        adapter = new ChatContentAdapter(messages);
        chatContentRecyclerView.setAdapter(adapter);
        chatContentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onResume(){
        super.onResume();
        Bundle bundle = getArguments();
        initialView(bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void fetchChatHistory(int chatUserId){
        Call<List<Message>> listCall = spcaService.fetchChatHistory(SPCApplication.currentUser.getToken(), SPCApplication.currentUser.getId(), chatUserId);
        listCall.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                 messages = response.body();
                 initRecyclerViewAdapter();
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
            }
        });
    }

    public void sendMessage(int chatUserId,String messageContent){
        setSendMessageSuccessful(chatUserId,messageContent);
        Call<ResponseBody> responseCall = spcaService.sendMessage(SPCApplication.currentUser.getToken(), SPCApplication.currentUser.getId(), chatUserId, messageContent);
        responseCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void setSendMessageSuccessful(int charUserId,String messageContent){
         String date = TimeUtil.curTimeStamp() / 1000 + "";
         Message message = new Message(SPCApplication.currentUser.getId(),charUserId,date, messageContent);
         messages.add(message);
         initRecyclerViewAdapter();
        int lastPosition = adapter.getItemCount() - 1;
        chatContentRecyclerView.scrollToPosition(lastPosition);
         getActivity().runOnUiThread(() -> {
             sendChatText.setText("");
         });
    }

}
