package com.adapters;


import android.app.AlertDialog;
import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.ChatServerModel.InitialMessageList;
import com.VO.UnitDetailVO;
import com.activities.ChatScreen;
import com.helper.ChatConstants;
import com.jsonparser.JsonParser;
import com.model.AcceptOfferStatus;
import com.model.SubmitOfferJsonModel;
import com.sendbird.android.AdminMessage;
import com.sendbird.android.FileMessage;
import com.sp.R;
import com.utils.DateUtil;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.utils.Utils.createTagWithOpenAndCloseTag;

/**
 * An adapter for a RecyclerView that displays messages in an Open Channel.
 */

public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = ChatRecyclerAdapter.class.getSimpleName();
    // private static final int VIEW_TYPE_USER_MESSAGE = 10;
    //private static final int VIEW_TYPE_FILE_MESSAGE = 20;
    //private static final int VIEW_TYPE_ADMIN_MESSAGE = 30;

    private static final int VIEW_TYPE_USER_CHAT = 40;
    private static final int VIEW_TYPE_BUILDER_CHAT = 50;

    private static final int VIEW_TYPE_BUILDER_CHAT_WHATS_YOUR_OFFER = 60;
    private static final int VIEW_TYPE_BUILDER_CHAT_WHATS_YOUR_OFFER_NEW = 110;
    private static final int VIEW_TYPE_USER_CHAT_SUBMIT_OFFER = 70;
    private static final int VIEW_TYPE_USER_CHAT_SUBMIT_OFFER_NEW = 80;
    private static final int VIEW_TYPE_USER_CHAT_SUBMIT_OFFER_NEW_BLUE = 120;
    private static final int VIEW_TYPE_USER_CHAT_ACCEPT_NEW_OFFER = 90;
    private static final int VIEW_TYPE_USER_CHAT_REJECT_NEW_OFFER = 100;
    private static final int VIEW_TYPE_USER_CHAT_ACCEPT_NEW_OFFER_BLUE = 130;
    private static final int VIEW_TYPE_USER_CHAT_REJECT_NEW_OFFER_BLUE = 140;
    public static final int ACCEPT = 1, REJECT = 0;
    TextView tv_msg;
    private Context mContext;
    UnitDetailVO mUnitDetails;
    private List<InitialMessageList.Data> mMessageList;
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;
    private String userChatId, builderChatId, unitId;
    private Integer is_reserved;

    /**
     * An interface to implement item click callbacks in the activity or fragment that
     * uses this adapter.
     */
    public interface OnItemClickListener {
        void onUserMessageItemClick(InitialMessageList.Data message);

        void onFileMessageItemClick(FileMessage message);

        void onAdminMessageItemClick(AdminMessage message);

        void onSubmitOfferClick(String message);

        void onSubmitAnotherOfferClick(String message);

        void onUpdateOfferClick(String offerCode, int status);// 0- reject 1- accept
    }

    public interface OnItemLongClickListener {
        void onBaseMessageLongClick(InitialMessageList.Data message, int position);
    }


    public ChatRecyclerAdapter(Context context, String userChatId, String builderChatId, String unitId, Integer isReserved) {
        mMessageList = new ArrayList<>();
        mContext = context;
        this.userChatId = userChatId;
        this.builderChatId = builderChatId;
        this.unitId = unitId;
        this.is_reserved = isReserved;

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mItemLongClickListener = listener;
    }

    public void setMessageList(ArrayList<InitialMessageList.Data> messages) {
        mMessageList = messages;
        Collections.reverse(mMessageList);
        notifyDataSetChanged();
    }

    public void addFirst(InitialMessageList.Data message) {
        mMessageList.add(0, message);
        notifyDataSetChanged();
    }

    public void addLast(InitialMessageList.Data message) {
        mMessageList.add(message);
        notifyDataSetChanged();
    }

    /*public void delete(long msgId) {
        for (BaseMessage msg : mMessageList) {
            if (msg.getMessageId() == msgId) {
                mMessageList.remove(msg);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public void update(BaseMessage message) {
        BaseMessage baseMessage;
        for (int index = 0; index < mMessageList.size(); index++) {
            baseMessage = mMessageList.get(index);
            if (message.getMessageId() == baseMessage.getMessageId()) {
                mMessageList.remove(index);
                mMessageList.add(index, message);
                notifyDataSetChanged();
                break;
            }
        }
    }*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("Naresh", "ViewType: " + viewType);
        if (viewType == VIEW_TYPE_USER_CHAT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_msg_white, parent, false);
            return new UserMessageHolder(view);
        } else if (viewType == VIEW_TYPE_USER_CHAT_SUBMIT_OFFER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_with_offer_code_blue, parent, false);
            return new UserMessageHolder(view);
        } else if (viewType == VIEW_TYPE_USER_CHAT_ACCEPT_NEW_OFFER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_reject_with_offercode, parent, false);
            return new UserMessageHolder(view);

        } else if (viewType == VIEW_TYPE_USER_CHAT_REJECT_NEW_OFFER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_reject_with_offercode, parent, false);
            return new UserMessageHolder(view);

        } else if (viewType == VIEW_TYPE_USER_CHAT_ACCEPT_NEW_OFFER_BLUE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_reject_with_offercode_blue, parent, false);
            return new UserMessageHolder(view);

        } else if (viewType == VIEW_TYPE_USER_CHAT_REJECT_NEW_OFFER_BLUE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_reject_with_offercode_blue, parent, false);
            return new UserMessageHolder(view);

        } else if (viewType == VIEW_TYPE_USER_CHAT_SUBMIT_OFFER_NEW) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_with_offer_code_blue, parent, false);
            return new UserMessageHolder(view);
        } else if (viewType == VIEW_TYPE_USER_CHAT_SUBMIT_OFFER_NEW_BLUE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_with_offer_code, parent, false);
            return new UserMessageHolder(view);
        } else if (viewType == VIEW_TYPE_BUILDER_CHAT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_msg_white, parent, false);
            return new UserMessageHolder(view);

        } else if (viewType == VIEW_TYPE_BUILDER_CHAT_WHATS_YOUR_OFFER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_bubble_make_offer, parent, false);
            return new UserMessageHolder(view);

        } else if (viewType == VIEW_TYPE_BUILDER_CHAT_WHATS_YOUR_OFFER_NEW) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_with_bubble_new_tag, parent, false);
            return new UserMessageHolder(view);

        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_msg_blue, parent, false);
            return new UserMessageHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mMessageList.get(position) instanceof InitialMessageList.Data) {
            InitialMessageList.Data userMessage = (InitialMessageList.Data) mMessageList.get(position);
            if (userMessage.getUser().getUser_id().equals(userChatId)) {
                if (userMessage.getMessage().startsWith(createTagWithOpenAndCloseTag(ChatConstants.SUBMIT_OFFER))) {
                    return VIEW_TYPE_USER_CHAT_SUBMIT_OFFER;
                }
                if (userMessage.getMessage().startsWith(createTagWithOpenAndCloseTag(ChatConstants.WHATS_YOUR_OFFER))) {
                    return VIEW_TYPE_USER_CHAT_SUBMIT_OFFER_NEW;
                }
                if (userMessage.getMessage().startsWith(Utils.createTagWithOpenAndCloseTag(ChatConstants.ACCEPT_NEW_OFFER))) {
                    return VIEW_TYPE_USER_CHAT_ACCEPT_NEW_OFFER_BLUE;
                }
                if (userMessage.getMessage().startsWith(Utils.createTagWithOpenAndCloseTag(ChatConstants.REJECT_NEW_OFFER))) {
                    return VIEW_TYPE_USER_CHAT_REJECT_NEW_OFFER_BLUE;
                }
            } else if (userMessage.getUser().getUser_id().equals(builderChatId)) {
                if (userMessage.getMessage().startsWith(Utils.createTagWithOpenAndCloseTag(ChatConstants.ACCEPT_OFFER))) {
                    return VIEW_TYPE_USER_CHAT_ACCEPT_NEW_OFFER;
                }
                if (userMessage.getMessage().startsWith(Utils.createTagWithOpenAndCloseTag(ChatConstants.REJECT_OFFER))) {
                    return VIEW_TYPE_USER_CHAT_REJECT_NEW_OFFER;
                }
                if (userMessage.getMessage().startsWith(createTagWithOpenAndCloseTag(ChatConstants.WHATS_YOUR_OFFER))) {
                    if (position == 0) {
                        return VIEW_TYPE_BUILDER_CHAT_WHATS_YOUR_OFFER;
                    } else {
                        return VIEW_TYPE_BUILDER_CHAT;
                    }
                }
                if (userMessage.getMessage().startsWith(createTagWithOpenAndCloseTag(ChatConstants.MAKE_ANOTHER_OFFER))) {
                    if (position == 0) {
                        return VIEW_TYPE_BUILDER_CHAT_WHATS_YOUR_OFFER_NEW;
                    } else {
                        return VIEW_TYPE_USER_CHAT_SUBMIT_OFFER_NEW_BLUE;
                    }
                } else {
                    return VIEW_TYPE_BUILDER_CHAT;
                }
            } else {
                Log.e(TAG, "getItemViewType()" + position);
                return VIEW_TYPE_BUILDER_CHAT;
            }
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        InitialMessageList.Data message = mMessageList.get(position);
        boolean isNewDay = false;
        if (position < mMessageList.size() - 1) {
            InitialMessageList.Data prevMessage = mMessageList.get(position + 1);
            if (!DateUtil.hasSameDate(Long.parseLong(message.getCreated_at()), Long.parseLong(prevMessage.getCreated_at()))) {
                isNewDay = true;
            }
        } else if (position == mMessageList.size() - 1) {
            isNewDay = true;
        }
        ((UserMessageHolder) holder).bind(mContext, holder.getItemViewType(), (InitialMessageList.Data) message, isNewDay, mItemClickListener, mItemLongClickListener, position);
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    private class UserMessageHolder extends RecyclerView.ViewHolder {

        UserMessageHolder(View itemView) {
            super(itemView);
            //mContext = itemView.getContext();
        }

        void bind(final Context context, int type, final InitialMessageList.Data message, boolean isNewDay, @Nullable final OnItemClickListener clickListener, @Nullable final OnItemLongClickListener longClickListener, final int postion) {
            if (type == VIEW_TYPE_BUILDER_CHAT_WHATS_YOUR_OFFER) {
                TextView tv_msg = (TextView) itemView.findViewById(R.id.tv_msg);
                TextView tv_time = (TextView) itemView.findViewById(R.id.tv_time);
                TextView tv_date = (TextView) itemView.findViewById(R.id.tv_date);
                Button btn_make_an_offer = (Button) itemView.findViewById(R.id.btn_make_an_offer);
                btn_make_an_offer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (context != null && context instanceof ChatScreen) {
                            showMakeAnOfferDialog(context, clickListener);
                        }
                    }
                });
                if (isNewDay) {
                    tv_date.setVisibility(View.VISIBLE);
                    tv_date.setText(DateUtil.formatDate(Long.parseLong(message.getCreated_at())));
                } else {
                    tv_date.setVisibility(View.GONE);
                }
                tv_msg.setText(Utils.getMessageWithOutTag(message.getMessage()));
                tv_time.setText(DateUtil.formatTime(Long.parseLong(message.getCreated_at())));
            } else if (type == VIEW_TYPE_BUILDER_CHAT_WHATS_YOUR_OFFER_NEW) {
                tv_msg = (TextView) itemView.findViewById(R.id.tv_msg);
                TextView tv_time = (TextView) itemView.findViewById(R.id.tv_time);
                TextView tv_date = (TextView) itemView.findViewById(R.id.tv_date);
                final TextView tv_offer_code = (TextView) itemView.findViewById(R.id.tv_offer_code);
                Button btn_accept = (Button) itemView.findViewById(R.id.btn_accept);
                Button btn_reject = (Button) itemView.findViewById(R.id.btn_reject);
                Button btn_make_another_offer = (Button) itemView.findViewById(R.id.btn_make_another_offer);
                String json = Utils.getMessageWithOutTag(message.getMessage());
                SubmitOfferJsonModel submitOfferJsonModel = (SubmitOfferJsonModel) JsonParser.convertJsonToBean(APIType.OFFER_JSON, json);
                btn_accept.setTag(R.integer.offer_code, submitOfferJsonModel.getOffer_code());
                btn_reject.setTag(R.integer.offer_code, submitOfferJsonModel.getOffer_code());
                btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.getTag(R.integer.offer_code) != null)
                            clickListener.onUpdateOfferClick((String) v.getTag(R.integer.offer_code), ACCEPT);
                    }
                });
                btn_reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.getTag(R.integer.offer_code) != null)
                            clickListener.onUpdateOfferClick((String) v.getTag(R.integer.offer_code), REJECT);
                    }
                });
                btn_make_another_offer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (context != null && context instanceof ChatScreen) {
                            showMakeAnOfferDialogAnother(context, clickListener);
                        }
                    }
                });
                if (isNewDay) {
                    tv_date.setVisibility(View.VISIBLE);
                    tv_date.setText(DateUtil.formatDate(Long.parseLong(message.getCreated_at())));
                } else {
                    tv_date.setVisibility(View.GONE);
                }
                if (submitOfferJsonModel != null) {
                    btn_accept.setVisibility(View.VISIBLE);
                    btn_reject.setVisibility(View.VISIBLE);
                    btn_make_another_offer.setVisibility(View.VISIBLE);
                    tv_offer_code.setVisibility(View.VISIBLE);
                    tv_offer_code.setText("Offer Code: " + submitOfferJsonModel.getOffer_code());
                    tv_msg.setText(submitOfferJsonModel.getDescription());
                } else {
                    btn_accept.setVisibility(View.GONE);
                    btn_reject.setVisibility(View.GONE);
                    btn_make_another_offer.setVisibility(View.GONE);
                    tv_offer_code.setVisibility(View.GONE);
                    tv_msg.setText("Error !!");
                }
                tv_time.setText(DateUtil.formatTime(Long.parseLong(message.getCreated_at())));
            } else if (type == VIEW_TYPE_USER_CHAT_ACCEPT_NEW_OFFER) {
                TextView tv_msg = (TextView) itemView.findViewById(R.id.tv_msg);
                TextView tv_time = (TextView) itemView.findViewById(R.id.tv_time);
                TextView tv_date = (TextView) itemView.findViewById(R.id.tv_date);
                TextView tv_rejected = (TextView) itemView.findViewById(R.id.tv_rejected);
                TextView tv_offer_code = (TextView) itemView.findViewById(R.id.tv_offer_code);
                TextView tv_coupon_code = (TextView) itemView.findViewById(R.id.tv_coupon_code);
                ImageView img_accept = (ImageView) itemView.findViewById(R.id.accept_reject);
                TextView tv_proceed_to_pay = (TextView) itemView.findViewById(R.id.tv_proceed_to_pay);
                TextView tv_payment_plan = (TextView) itemView.findViewById(R.id.tv_payment_plan);
                if (isNewDay) {
                    tv_date.setVisibility(View.VISIBLE);
                    tv_date.setText(DateUtil.formatDate(Long.parseLong(message.getCreated_at())));
                } else {
                    tv_date.setVisibility(View.GONE);
                }
                String json = Utils.getMessageWithOutTag(message.getMessage());
                final AcceptOfferStatus submitOfferJsonModel = (AcceptOfferStatus) JsonParser.convertJsonToBean(APIType.ACCEPT_REJECT, json);
                if (submitOfferJsonModel != null) {
                    if (is_reserved == 1) {
                        tv_proceed_to_pay.setVisibility(View.GONE);
                    } else {
                        tv_proceed_to_pay.setVisibility(View.VISIBLE);
                    }
                    tv_offer_code.setVisibility(View.VISIBLE);
                    tv_offer_code.setText("Offer Code: " + submitOfferJsonModel.getOffer_code());
                    tv_coupon_code.setText("Coupon Code: " + submitOfferJsonModel.getCoupon_code());
                    tv_msg.setText(submitOfferJsonModel.getDescription());
                    img_accept.setImageResource(R.drawable.ico_accept);
                    tv_payment_plan.setText(submitOfferJsonModel.getPlan_title());
                    tv_rejected.setText("Offer Accepted");
                    final String coupon_code = submitOfferJsonModel.getCoupon_code();
                    tv_proceed_to_pay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mContext instanceof ChatScreen) {
                                ((ChatScreen) mContext).sendData(coupon_code);
                            }
                        }
                    });
                } else {
                    tv_offer_code.setVisibility(View.GONE);
                    img_accept.setVisibility(View.GONE);
                    tv_proceed_to_pay.setVisibility(View.GONE);
                    tv_coupon_code.setVisibility(View.GONE);
                    tv_msg.setText("Error !!");
                }
                tv_time.setText(DateUtil.formatTime(Long.parseLong(message.getCreated_at())));

            } else if (type == VIEW_TYPE_USER_CHAT_REJECT_NEW_OFFER) {
                TextView tv_msg = (TextView) itemView.findViewById(R.id.tv_msg);
                TextView tv_time = (TextView) itemView.findViewById(R.id.tv_time);
                TextView tv_date = (TextView) itemView.findViewById(R.id.tv_date);
                TextView tv_rejected = (TextView) itemView.findViewById(R.id.tv_rejected);
                TextView tv_offer_code = (TextView) itemView.findViewById(R.id.tv_offer_code);
                TextView tv_coupon_code = (TextView) itemView.findViewById(R.id.tv_coupon_code);
                ImageView img_accept = (ImageView) itemView.findViewById(R.id.accept_reject);
                TextView tv_proceed_to_pay = (TextView) itemView.findViewById(R.id.tv_proceed_to_pay);
                TextView tv_payment_plan = (TextView) itemView.findViewById(R.id.tv_payment_plan);
                if (isNewDay) {
                    tv_date.setVisibility(View.VISIBLE);
                    tv_date.setText(DateUtil.formatDate(Long.parseLong(message.getCreated_at())));
                } else {
                    tv_date.setVisibility(View.GONE);
                }
                String json = Utils.getMessageWithOutTag(message.getMessage());
                SubmitOfferJsonModel submitOfferJsonModel = (SubmitOfferJsonModel) JsonParser.convertJsonToBean(APIType.OFFER_JSON, json);
                if (submitOfferJsonModel != null) {
                    tv_offer_code.setVisibility(View.VISIBLE);
                    tv_coupon_code.setVisibility(View.GONE);
                    tv_proceed_to_pay.setVisibility(View.GONE);
                    tv_offer_code.setText("Offer Code: " + submitOfferJsonModel.getOffer_code());
                    tv_payment_plan.setVisibility(View.GONE);
                    tv_msg.setText(submitOfferJsonModel.getDescription());
                    img_accept.setImageResource(R.drawable.ico_reject);
                    tv_rejected.setText("Offer Rejected");
                } else {
                    tv_offer_code.setVisibility(View.GONE);
                    tv_proceed_to_pay.setVisibility(View.GONE);
                    img_accept.setVisibility(View.GONE);
                    tv_coupon_code.setVisibility(View.GONE);
                    tv_msg.setText("Error !!");
                }
                tv_time.setText(DateUtil.formatTime(Long.parseLong(message.getCreated_at())));

            } else if (type == VIEW_TYPE_USER_CHAT_ACCEPT_NEW_OFFER_BLUE) {
                TextView tv_msg = (TextView) itemView.findViewById(R.id.tv_msg);
                TextView tv_time = (TextView) itemView.findViewById(R.id.tv_time);
                TextView tv_date = (TextView) itemView.findViewById(R.id.tv_date);
                TextView tv_rejected = (TextView) itemView.findViewById(R.id.tv_rejected);
                TextView tv_offer_code = (TextView) itemView.findViewById(R.id.tv_offer_code);
                TextView tv_coupon_code = (TextView) itemView.findViewById(R.id.tv_coupon_code);
                ImageView img_accept = (ImageView) itemView.findViewById(R.id.accept_reject);
                TextView tv_proceed_to_pay = (TextView) itemView.findViewById(R.id.tv_proceed_to_pay);
                TextView tv_payment_plan = (TextView) itemView.findViewById(R.id.tv_payment_plan);
                if (isNewDay) {
                    tv_date.setVisibility(View.VISIBLE);
                    tv_date.setText(DateUtil.formatDate(Long.parseLong(message.getCreated_at())));
                } else {
                    tv_date.setVisibility(View.GONE);
                }
                String json = Utils.getMessageWithOutTag(message.getMessage());
                final AcceptOfferStatus submitOfferJsonModel = (AcceptOfferStatus) JsonParser.convertJsonToBean(APIType.ACCEPT_REJECT, json);
                if (submitOfferJsonModel != null) {
                    if (is_reserved == 1) {
                        tv_proceed_to_pay.setVisibility(View.GONE);
                    } else {
                        tv_proceed_to_pay.setVisibility(View.VISIBLE);
                    }
                    tv_offer_code.setVisibility(View.VISIBLE);
                    tv_offer_code.setText("Offer Code: " + submitOfferJsonModel.getOffer_code());
                    tv_coupon_code.setText("Coupon Code: " + submitOfferJsonModel.getCoupon_code());
                    final String coupon_code = submitOfferJsonModel.getCoupon_code();
                    tv_proceed_to_pay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mContext instanceof ChatScreen) {
                                ((ChatScreen) mContext).sendData(coupon_code);
                            }
                        }
                    });
                    tv_msg.setText(submitOfferJsonModel.getDescription());
                    tv_payment_plan.setText(submitOfferJsonModel.getPlan_title());
                    img_accept.setImageResource(R.drawable.ico_accept);
                    tv_rejected.setText("Offer Accepted");
                } else {
                    tv_offer_code.setVisibility(View.GONE);
                    tv_proceed_to_pay.setVisibility(View.GONE);
                    img_accept.setVisibility(View.GONE);
                    tv_coupon_code.setVisibility(View.GONE);
                    tv_msg.setText("Error !!");
                }
                tv_time.setText(DateUtil.formatTime(Long.parseLong(message.getCreated_at())));

            } else if (type == VIEW_TYPE_USER_CHAT_REJECT_NEW_OFFER_BLUE) {
                TextView tv_msg = (TextView) itemView.findViewById(R.id.tv_msg);
                TextView tv_time = (TextView) itemView.findViewById(R.id.tv_time);
                TextView tv_date = (TextView) itemView.findViewById(R.id.tv_date);
                TextView tv_rejected = (TextView) itemView.findViewById(R.id.tv_rejected);
                TextView tv_offer_code = (TextView) itemView.findViewById(R.id.tv_offer_code);
                TextView tv_coupon_code = (TextView) itemView.findViewById(R.id.tv_coupon_code);
                ImageView img_accept = (ImageView) itemView.findViewById(R.id.accept_reject);
                TextView tv_proceed_to_pay = (TextView) itemView.findViewById(R.id.tv_proceed_to_pay);
                TextView tv_payment_plan = (TextView) itemView.findViewById(R.id.tv_payment_plan);

                if (isNewDay) {
                    tv_date.setVisibility(View.VISIBLE);
                    tv_date.setText(DateUtil.formatDate(Long.parseLong(message.getCreated_at())));
                } else {
                    tv_date.setVisibility(View.GONE);
                }
                String json = Utils.getMessageWithOutTag(message.getMessage());
                SubmitOfferJsonModel submitOfferJsonModel = (SubmitOfferJsonModel) JsonParser.convertJsonToBean(APIType.OFFER_JSON, json);
                if (submitOfferJsonModel != null) {
                    tv_offer_code.setVisibility(View.VISIBLE);
                    tv_coupon_code.setVisibility(View.GONE);
                    tv_proceed_to_pay.setVisibility(View.GONE);
                    tv_offer_code.setText("Offer Code: " + submitOfferJsonModel.getOffer_code());
                    tv_msg.setText(submitOfferJsonModel.getDescription());
                    tv_payment_plan.setVisibility(View.GONE);
                    img_accept.setImageResource(R.drawable.ico_reject);
                    tv_rejected.setText("Offer Rejected");
                } else {
                    tv_offer_code.setVisibility(View.GONE);
                    img_accept.setVisibility(View.GONE);
                    tv_proceed_to_pay.setVisibility(View.GONE);
                    tv_coupon_code.setVisibility(View.GONE);
                    tv_msg.setText("Error !!");
                }
                tv_time.setText(DateUtil.formatTime(Long.parseLong(message.getCreated_at())));

            } else if (type == VIEW_TYPE_BUILDER_CHAT) {
                TextView tv_msg = (TextView) itemView.findViewById(R.id.tv_msg);
                TextView tv_time = (TextView) itemView.findViewById(R.id.tv_time);
                TextView tv_date = (TextView) itemView.findViewById(R.id.tv_date);
                if (isNewDay) {
                    tv_date.setVisibility(View.VISIBLE);
                    tv_date.setText(DateUtil.formatDate(Long.parseLong(message.getCreated_at())));
                } else {
                    tv_date.setVisibility(View.GONE);
                }
                tv_msg.setText(Utils.getMessageWithOutTag(message.getMessage()));
                tv_time.setText(DateUtil.formatTime(Long.parseLong(message.getCreated_at())));
            } else if (type == VIEW_TYPE_USER_CHAT) {
                TextView tv_msg = (TextView) itemView.findViewById(R.id.tv_msg);
                TextView tv_time = (TextView) itemView.findViewById(R.id.tv_time);
                TextView tv_date = (TextView) itemView.findViewById(R.id.tv_date);
                if (isNewDay) {
                    tv_date.setVisibility(View.VISIBLE);
                    tv_date.setText(DateUtil.formatDate(Long.parseLong(message.getCreated_at())));
                } else {
                    tv_date.setVisibility(View.GONE);
                }
                tv_msg.setText(Utils.getMessageWithOutTag(message.getMessage()));
                tv_time.setText(DateUtil.formatTime(Long.parseLong(message.getCreated_at())));
                if (clickListener != null) {
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickListener.onUserMessageItemClick(message);
                        }
                    });
                }
                if (longClickListener != null) {
                    itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            longClickListener.onBaseMessageLongClick(message, postion);
                            return true;
                        }
                    });
                }
            } else if (type == VIEW_TYPE_USER_CHAT_SUBMIT_OFFER) {
                TextView tv_msg = (TextView) itemView.findViewById(R.id.tv_msg);
                TextView tv_time = (TextView) itemView.findViewById(R.id.tv_time);
                TextView tv_date = (TextView) itemView.findViewById(R.id.tv_date);
                TextView tv_offer_code = (TextView) itemView.findViewById(R.id.tv_offer_code);
                if (isNewDay) {
                    tv_date.setVisibility(View.VISIBLE);
                    tv_date.setText(DateUtil.formatDate(Long.parseLong(message.getCreated_at())));
                } else {
                    tv_date.setVisibility(View.GONE);
                }
                //TODO: parse json.
                String json = Utils.getMessageWithOutTag(message.getMessage());
                SubmitOfferJsonModel submitOfferJsonModel = (SubmitOfferJsonModel) JsonParser.convertJsonToBean(APIType.OFFER_JSON, json);
                if (submitOfferJsonModel != null) {
                    tv_offer_code.setVisibility(View.VISIBLE);
                    tv_offer_code.setText("Offer Code: " + submitOfferJsonModel.getOffer_code());
                    tv_msg.setText(submitOfferJsonModel.getDescription());
                } else {
                    tv_offer_code.setVisibility(View.GONE);
                    tv_msg.setText("Error !!");
                }
                tv_time.setText(DateUtil.formatTime(Long.parseLong(message.getCreated_at())));
                if (clickListener != null) {
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickListener.onUserMessageItemClick(message);
                        }
                    });
                }
                if (longClickListener != null) {
                    itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            longClickListener.onBaseMessageLongClick(message, postion);
                            return true;
                        }
                    });
                }
            } else if (type == VIEW_TYPE_USER_CHAT_SUBMIT_OFFER_NEW) {
                TextView tv_msg = (TextView) itemView.findViewById(R.id.tv_msg);
                TextView tv_time = (TextView) itemView.findViewById(R.id.tv_time);
                TextView tv_date = (TextView) itemView.findViewById(R.id.tv_date);
                TextView tv_offer_code = (TextView) itemView.findViewById(R.id.tv_offer_code);
                if (isNewDay) {
                    tv_date.setVisibility(View.VISIBLE);
                    tv_date.setText(DateUtil.formatDate(Long.parseLong(message.getCreated_at())));
                } else {
                    tv_date.setVisibility(View.GONE);
                }
                //TODO: parse json.
                String json = Utils.getMessageWithOutTag(message.getMessage());
                SubmitOfferJsonModel submitOfferJsonModel = (SubmitOfferJsonModel) JsonParser.convertJsonToBean(APIType.OFFER_JSON, json);
                if (submitOfferJsonModel != null) {
                    tv_offer_code.setVisibility(View.VISIBLE);
                    tv_offer_code.setText("Offer Code: " + submitOfferJsonModel.getOffer_code());
                    tv_msg.setText(submitOfferJsonModel.getDescription());
                } else {
                    tv_offer_code.setVisibility(View.GONE);
                    tv_msg.setText("Error !!");
                }
                tv_time.setText(DateUtil.formatTime(Long.parseLong(message.getCreated_at())));
                if (clickListener != null) {
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickListener.onUserMessageItemClick(message);
                        }
                    });
                }
                if (longClickListener != null) {
                    itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            longClickListener.onBaseMessageLongClick(message, postion);
                            return true;
                        }
                    });
                }
            } else if (type == VIEW_TYPE_USER_CHAT_SUBMIT_OFFER_NEW_BLUE) {
                TextView tv_msg = (TextView) itemView.findViewById(R.id.tv_msg);
                TextView tv_time = (TextView) itemView.findViewById(R.id.tv_time);
                TextView tv_date = (TextView) itemView.findViewById(R.id.tv_date);
                TextView tv_offer_code = (TextView) itemView.findViewById(R.id.tv_offer_code);
                if (isNewDay) {
                    tv_date.setVisibility(View.VISIBLE);
                    tv_date.setText(DateUtil.formatDate(Long.parseLong(message.getCreated_at())));
                } else {
                    tv_date.setVisibility(View.GONE);
                }
                //TODO: parse json.
                String json = Utils.getMessageWithOutTag(message.getMessage());
                SubmitOfferJsonModel submitOfferJsonModel = (SubmitOfferJsonModel) JsonParser.convertJsonToBean(APIType.OFFER_JSON, json);
                if (submitOfferJsonModel != null) {
                    tv_offer_code.setVisibility(View.VISIBLE);
                    tv_offer_code.setText("Offer Code: " + submitOfferJsonModel.getOffer_code());
                    tv_msg.setText(submitOfferJsonModel.getDescription());
                } else {
                    tv_offer_code.setVisibility(View.GONE);
                    tv_msg.setText("Error !!");
                }
                tv_time.setText(DateUtil.formatTime(Long.parseLong(message.getCreated_at())));
                if (clickListener != null) {
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickListener.onUserMessageItemClick(message);
                        }
                    });
                }
                if (longClickListener != null) {
                    itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            longClickListener.onBaseMessageLongClick(message, postion);
                            return true;
                        }
                    });
                }
            } else {
                TextView tv_msg = (TextView) itemView.findViewById(R.id.tv_msg);
                TextView tv_time = (TextView) itemView.findViewById(R.id.tv_time);
                TextView tv_date = (TextView) itemView.findViewById(R.id.tv_date);
                if (isNewDay) {
                    tv_date.setVisibility(View.VISIBLE);
                    tv_date.setText(DateUtil.formatDate(Long.parseLong(message.getCreated_at())));
                } else {
                    tv_date.setVisibility(View.GONE);
                }
                tv_msg.setText(Utils.getMessageWithOutTag(message.getMessage()));
                tv_time.setText(DateUtil.formatTime(Long.parseLong(message.getCreated_at())));
            }
        }
    }

    private void showMakeAnOfferDialog(final Context context, final OnItemClickListener clickListener) {
        if (context == null) return;
        LayoutInflater factory = LayoutInflater.from(context);
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        final View dialogView = factory.inflate(R.layout.chat_make_an_offer_dialog, null);
        final EditText et_offer_text = (EditText) dialogView.findViewById(R.id.et_offer_text);
        //et_offer_text.setText(tv_msg.getText());
        final Button btn_submit = (Button) dialogView.findViewById(R.id.btn_submit);
        final Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:
                String offerText = et_offer_text.getText().toString().trim();
                if (TextUtils.isEmpty(offerText))
                    Toast.makeText(context, R.string.please_enter_text, Toast.LENGTH_LONG).show();
                else {
                    clickListener.onSubmitAnotherOfferClick(offerText);
                    //((ChatScreen)context).sendUserMessage(createTaggedMessage(ChatConstants.SUBMIT_OFFER,context.getString(R.string.i_would_like_to_buy_it_for)+ "\n"+offerText));
                    dialog.dismiss();
                }
            }
        });

        dialog.setView(dialogView);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();

    }

    private void showMakeAnOfferDialogAnother(final Context context, final OnItemClickListener clickListener) {
        if (context == null) return;
        LayoutInflater factory = LayoutInflater.from(context);
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        final View dialogView = factory.inflate(R.layout.chat_make_an_offer_dialog, null);
        final EditText et_offer_text = (EditText) dialogView.findViewById(R.id.et_offer_text);
        et_offer_text.setText(tv_msg.getText());
        final Button btn_submit = (Button) dialogView.findViewById(R.id.btn_submit);
        final Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:
                String offerText = et_offer_text.getText().toString().trim();
                if (TextUtils.isEmpty(offerText))
                    Toast.makeText(context, R.string.please_enter_text, Toast.LENGTH_LONG).show();
                else {
                    clickListener.onSubmitAnotherOfferClick(offerText);
                    //((ChatScreen)context).sendUserMessage(createTaggedMessage(ChatConstants.SUBMIT_OFFER,context.getString(R.string.i_would_like_to_buy_it_for)+ "\n"+offerText));
                    dialog.dismiss();
                }
            }
        });

        dialog.setView(dialogView);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();

    }


}
