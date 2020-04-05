package com.androidapp.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidapp.chatapp.Activity.ZoomImageActivity;
import com.androidapp.chatapp.Model.ChatMessageDetailModel;
import com.androidapp.chatapp.R;

import java.util.ArrayList;

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ChatMessageDetailModel> mChatList;

    public static final int RECIPIENT = 1;
    public static final int SENDER = 2;
    private Context context;

    public ChatMessageAdapter(Context ctx, ArrayList<ChatMessageDetailModel> listOfFireChats) {
        this.mChatList = listOfFireChats;
        this.context =ctx;
    }

    @Override
    public int getItemViewType(int position) {
        // an important method to detect message is from Sender side or Receiver side
        if(mChatList.get(position).getChatUserType().equalsIgnoreCase("2")){
            return SENDER;
        }else {
            return RECIPIENT;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case SENDER:
                // View for Sender
                View viewSender = inflater.inflate(R.layout.layout_sender_message, viewGroup, false);
                viewHolder = new ViewHolderSender(viewSender);
                break;
            case RECIPIENT:
                // View for Receiver
                View viewRecipient = inflater.inflate(R.layout.layout_received_message, viewGroup, false);
                viewHolder = new ViewHolderRecipient(viewRecipient);
                break;
            default:
                // Default View will be Sender
                View viewSenderDefault = inflater.inflate(R.layout.layout_sender_message, viewGroup, false);
                viewHolder = new ViewHolderSender(viewSenderDefault);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {
            case SENDER:
                // Bind Chat message of Sender side
                ViewHolderSender viewHolderSender = (ViewHolderSender) viewHolder;
                // Below function is responsible to bind all the message, views to sender side
                configureSenderView(viewHolderSender, position);
                break;
            case RECIPIENT:
                // Bind Chat message of Receiver side
                ViewHolderRecipient viewHolderRecipient = (ViewHolderRecipient) viewHolder;
                // Below function is responsible to bind all the message, views to receiver side
                configureRecipientView(viewHolderRecipient, position);
                break;

        }
    }

    private void configureSenderView(final ViewHolderSender viewHolderSender, int position) {
        try {
            final ChatMessageDetailModel chatMessageDetailModel = mChatList.get(position);
            // In Meessage Type Paramter - 1 - Text, 2- Image (getChatMessageType)
            // In Chat User Type Parameter - 1 - Receiver User, 2 - Sender User
            if(chatMessageDetailModel.getChatMessageType().equalsIgnoreCase("1")){

                viewHolderSender.getSenderUserNameTextView().setText(chatMessageDetailModel.getChatUserName());
                viewHolderSender.getSenderMessageTextView().setText(chatMessageDetailModel.getChatMessage());
                viewHolderSender.getSenderMessageTimeTextView().setText(chatMessageDetailModel.getChatTime());
                viewHolderSender.getSenderMessageTextView().setVisibility(View.VISIBLE);
                viewHolderSender.getSenderImageView().setVisibility(View.GONE);

                // Dynamically set the size as per image layout and Text messgae layout
                ViewGroup.LayoutParams params = viewHolderSender.getSenderMessageRootLayout().getLayoutParams();

                params.width = dpToPx(250);

                viewHolderSender.getSenderMessageRootLayout().setLayoutParams(params);

            }else {
                viewHolderSender.getSenderUserNameTextView().setVisibility(View.VISIBLE);

                viewHolderSender.getSenderMessageTextView().setVisibility(View.GONE);
                viewHolderSender.getSenderMessageTimeTextView().setVisibility(View.VISIBLE);
                viewHolderSender.getSenderImageRoot().setVisibility(View.VISIBLE);

                viewHolderSender.getSenderUserNameTextView().setText(chatMessageDetailModel.getChatUserName());
                viewHolderSender.getSenderMessageTextView().setText(chatMessageDetailModel.getChatMessage());
                viewHolderSender.getSenderMessageTimeTextView().setText(chatMessageDetailModel.getChatTime());
                viewHolderSender.getSenderImageView().setImageResource(chatMessageDetailModel.getChatImageID());

                // Dynamically set the size as per image layout and Text messgae layout
                ViewGroup.LayoutParams params = viewHolderSender.getSenderMessageRootLayout().getLayoutParams();
                params.width = dpToPx(200);
                viewHolderSender.getSenderMessageRootLayout().setLayoutParams(params);

                viewHolderSender.getSenderImageRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ZoomImageActivity.class);

                    /*    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context,
                                viewHolderSender.getSenderImageView(), senderFireMessage.getImage_url_file());
                        ActivityCompat.*/
                       context.startActivity( intent);

                    }
                });


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configureRecipientView(final ViewHolderRecipient viewHolderRecipient, int position) {
        // In Meessage Type Paramter - 1 - Text, 2- Image (getChatMessageType)
        // In Chat User Type Parameter - 1 - Receiver User, 2 - Sender User
        final ChatMessageDetailModel recipientFireMessage = mChatList.get(position);

        try {
            if(recipientFireMessage.getChatMessageType().equalsIgnoreCase("1")){

                viewHolderRecipient.getRecipientUserNameTimeTextView().setText(recipientFireMessage.getChatUserName());
                viewHolderRecipient.getRecipientMessageTextView().setText(recipientFireMessage.getChatMessage());
                viewHolderRecipient.getRecipientMessageTimeTextView().setText(recipientFireMessage.getChatTime());
                viewHolderRecipient.getReceiptImageView().setVisibility(View.GONE);
                viewHolderRecipient.getRecipientMessageTextView().setVisibility(View.VISIBLE);

                // Dynamically set the size as per image layout and Text messgae layout
                ViewGroup.LayoutParams params = viewHolderRecipient.getLvReceivedMessageViewRoot().getLayoutParams();
                params.width = dpToPx(250);
                viewHolderRecipient.getLvReceivedMessageViewRoot().setLayoutParams(params);

            }else if(recipientFireMessage.getChatMessageType().equalsIgnoreCase("2")){

                viewHolderRecipient.getRecipientUserNameTimeTextView().setVisibility(View.VISIBLE);
                viewHolderRecipient.getRecipientMessageTextView().setVisibility(View.GONE);
                viewHolderRecipient.getRecipientMessageTimeTextView().setVisibility(View.VISIBLE);
                viewHolderRecipient.getRecipientImageRoot().setVisibility(View.VISIBLE);

                viewHolderRecipient.getRecipientUserNameTimeTextView().setText(recipientFireMessage.getChatUserName());
                viewHolderRecipient.getRecipientMessageTimeTextView().setText(recipientFireMessage.getChatTime());
                viewHolderRecipient.getReceiptImageView().setImageResource(recipientFireMessage.getChatImageID());

                // Dynamically set the size as per image layout and Text messgae layout
                ViewGroup.LayoutParams params = viewHolderRecipient.getLvReceivedMessageViewRoot().getLayoutParams();
                params.width = dpToPx(200);
                viewHolderRecipient.getLvReceivedMessageViewRoot().setLayoutParams(params);

                viewHolderRecipient.getRecipientImageRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ZoomImageActivity.class);

                    /*    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context,
                                viewHolderSender.getSenderImageView(), senderFireMessage.getImage_url_file());
                        ActivityCompat.*/
                        context.startActivity( intent);

                    }
                });


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Method to convert Pixel as per device
    public int dpToPx(int dp) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }


    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void refillAdapter(ChatMessageDetailModel newFireChatMessage) {

        notifyItemInserted(getItemCount() - 1);
    }

    public void cleanUp() {
        mChatList.clear();
    }


    /*==============ViewHolder===========*/

    /*ViewHolder for Sender*/

    public class ViewHolderSender extends RecyclerView.ViewHolder {

        /*ViewHolder for Sender*/
        TextView text_view_sender_name,text_view_sender_message, text_sender_time;

        ImageView img_sender_chat;
        RelativeLayout relSenderImageRoot;
        LinearLayout lvSenderMessageRootLayout;

        public ViewHolderSender(View itemView) {
            super(itemView);
            text_view_sender_name = itemView.findViewById(R.id.text_view_sender_name);

            text_view_sender_message =  itemView.findViewById(R.id.text_view_sender_message);
            text_sender_time = itemView.findViewById(R.id.text_sender_time);

            relSenderImageRoot =  itemView.findViewById(R.id.relSenderImageRoot);

            img_sender_chat = itemView.findViewById(R.id.img_sender_chat);
            lvSenderMessageRootLayout = itemView.findViewById(R.id.lvSenderMessageRootLayout);

        }
        // Method has been defined to get value of particular components
        public TextView getSenderUserNameTextView() {
            return text_view_sender_name;
        }

        public TextView getSenderMessageTextView() {
            return text_view_sender_message;
        }

        public TextView getSenderMessageTimeTextView() {
            return text_sender_time;
        }

        public RelativeLayout getSenderImageRoot() {
            return relSenderImageRoot;
        }

        public LinearLayout getSenderMessageRootLayout() {
            return lvSenderMessageRootLayout;
        }


        public ImageView getSenderImageView() {
            return img_sender_chat;
        }

    }

    /*ViewHolder for Recipient*/
    public class ViewHolderRecipient extends RecyclerView.ViewHolder {

        private TextView text_view_received_name, text_view_received_message,text_received_time;

        private ImageView img_received_chat;
        private RelativeLayout relReceivedImageRoot;
        private  LinearLayout lvReceivedMessageRootLayout;

        public ViewHolderRecipient(View itemView) {
            super(itemView);
            text_view_received_name = itemView.findViewById(R.id.text_view_received_name);
            text_view_received_message = itemView.findViewById(R.id.text_view_received_message);

            text_received_time =  itemView.findViewById(R.id.text_received_time);

            img_received_chat = itemView.findViewById(R.id.img_received_chat);
            relReceivedImageRoot = itemView.findViewById(R.id.relReceivedImageRoot);
            lvReceivedMessageRootLayout = itemView.findViewById(R.id.lvReceivedMessageRootLayout);

        }
        // Method has been defined to get value of particular components
        public TextView getRecipientUserNameTimeTextView() {
            return text_view_received_name;
        }

        public TextView getRecipientMessageTextView() {
            return text_view_received_message;
        }

        public TextView getRecipientMessageTimeTextView() {
            return text_received_time;
        }

        public RelativeLayout getRecipientImageRoot() {
            return relReceivedImageRoot;
        }


        public ImageView getReceiptImageView() {
            return img_received_chat;
        }

        public LinearLayout getLvReceivedMessageViewRoot() {
            return lvReceivedMessageRootLayout;
        }
    }
}